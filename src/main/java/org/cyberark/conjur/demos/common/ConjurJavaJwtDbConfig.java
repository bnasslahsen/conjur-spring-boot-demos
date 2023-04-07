package org.cyberark.conjur.demos.common;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import com.cyberark.conjur.sdk.AccessToken;
import com.cyberark.conjur.sdk.ApiClient;
import com.cyberark.conjur.sdk.ApiException;
import com.cyberark.conjur.sdk.endpoint.AuthenticationApi;
import com.cyberark.conjur.sdk.endpoint.SecretsApi;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * The type Conjur java db config.
 *
 * @author bnasslahsen
 */
@Profile("secured-jwt")
@Primary
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "spring.datasource")
public class ConjurJavaJwtDbConfig extends DataSourceProperties {

	/**
	 * The constant VARIABLE.
	 */
	private static final String VARIABLE = ":variable:";

	private final ConjurProperties conjurProperties;

	/**
	 * The App config.
	 */
	private SecretsConfig secretsConfig;

	public ConjurJavaJwtDbConfig(SecretsConfig secretsConfig, ConjurProperties conjurProperties) {
		this.secretsConfig = secretsConfig;
		this.conjurProperties = conjurProperties;
	}

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ConjurJavaJwtDbConfig.class);

	/**
	 * After properties set.
	 */
	@Override
	public void afterPropertiesSet() {
		try {
			super.afterPropertiesSet();
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		try {
			initConjurClient();
		}
		catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		SecretsApi secretsApi = new SecretsApi();
		Map<String, String> response;


		try {
			String account = secretsApi.getApiClient().getAccount();
			StringBuilder bulkSecrets = new StringBuilder();
			String urlConjurKey = account + VARIABLE + secretsConfig.getUrl();
			bulkSecrets.append(urlConjurKey).append(",");
			String usernameConjurKey = account + VARIABLE + secretsConfig.getUsername();
			bulkSecrets.append(usernameConjurKey).append(",");
			String passwordConjurKey = account + VARIABLE + secretsConfig.getPassword();
			bulkSecrets.append(passwordConjurKey);

			response = (Map<String, String>) secretsApi.getSecrets(bulkSecrets.toString());
			this.setUrl(response.get(urlConjurKey));
			this.setUsername(response.get(usernameConjurKey));
			this.setPassword(response.get(passwordConjurKey));
		}
		catch (ApiException e) {
			LOGGER.error(e.getResponseBody(), e);
			String message = StringUtils.isEmpty(e.getMessage()) ? e.getResponseBody() : e.getMessage();
			throw new RuntimeException(message, e);
		}
	}

	/**
	 * Init conjur client.
	 *
	 * @throws IOException the io exception
	 */
	private void initConjurClient() throws IOException {
		ApiClient conjurClient = com.cyberark.conjur.sdk.Configuration.getDefaultApiClient();
		LOGGER.debug("Using Account: " + conjurClient.getAccount());
		LOGGER.debug("Using ApplianceUrl: " + conjurClient.getBasePath());
		LOGGER.debug("Using SSL CERT: " + System.getenv().get("CONJUR_SSL_CERTIFICATE"));

		InputStream sslInputStream = null;
		if (StringUtils.isNotEmpty(System.getenv().get("CONJUR_SSL_CERTIFICATE"))) {
			sslInputStream = new ByteArrayInputStream(System.getenv().get("CONJUR_SSL_CERTIFICATE").getBytes(StandardCharsets.UTF_8));
		}
		else if (StringUtils.isNotEmpty(System.getenv().get("CONJUR_CERT_FILE")))
			sslInputStream = new FileInputStream(System.getenv().get("CONJUR_CERT_FILE"));

		if (sslInputStream != null) {
			conjurClient.setSslCaCert(sslInputStream);
			sslInputStream.close();
		}


		final AccessToken accessToken;
		AuthenticationApi apiInstance = new AuthenticationApi(conjurClient);
		LOGGER.debug("Using ServiceId: " + conjurProperties.getServiceId());
		String xRequestId = UUID.randomUUID().toString();
		LOGGER.debug("Using xRequestId: " + UUID.randomUUID());

		String jwtTokenPath = conjurProperties.getJwtTokenPath();
		String jwt = Files.readString(Paths.get(jwtTokenPath), StandardCharsets.UTF_8);
		try {
			String accessTokenStr = apiInstance.getAccessTokenViaJWT(conjurClient.getAccount(), conjurProperties.getServiceId(), xRequestId, jwt);
			LOGGER.debug("getAccessTokenViaJWT Result:" + accessTokenStr);
			accessToken = AccessToken.fromEncodedToken(Base64.getEncoder().encodeToString(accessTokenStr.getBytes(StandardCharsets.UTF_8)));
		}
		catch (ApiException e) {
			LOGGER.error("Status code: " + e.getCode());
			LOGGER.error("Reason: " + e.getResponseBody());
			LOGGER.error("Response headers: " + e.getResponseHeaders());
			LOGGER.error("getAccessTokenViaJWT Failed", e);
			throw new RuntimeException(e);
		}

		if (accessToken == null)
			throw new IllegalArgumentException("Unable to get the Access Token from CyberArk Conjur. Please check your configuration!");
		conjurClient.setAccessToken(accessToken.getHeaderValue());
		com.cyberark.conjur.sdk.Configuration.setDefaultApiClient(conjurClient);
	}

}