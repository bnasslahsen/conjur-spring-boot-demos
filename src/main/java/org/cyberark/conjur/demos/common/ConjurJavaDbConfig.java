package org.cyberark.conjur.demos.common;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.cyberark.conjur.sdk.AccessToken;
import com.cyberark.conjur.sdk.ApiClient;
import com.cyberark.conjur.sdk.ApiException;
import com.cyberark.conjur.sdk.ApiResponse;
import com.cyberark.conjur.sdk.endpoint.AuthenticationApi;
import com.cyberark.conjur.sdk.endpoint.SecretsApi;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
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
@Profile("secured-api-java")
@Primary
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "spring.datasource")
public class ConjurJavaDbConfig extends DataSourceProperties {


	/**
	 * The App config.
	 */
	private SecretsConfig secretsConfig;

	/**
	 * The constant VARIABLE.
	 */
	private static final String VARIABLE = ":variable:";

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ConjurJavaDbConfig.class);

	public ConjurJavaDbConfig(SecretsConfig secretsConfig) {
		this.secretsConfig = secretsConfig;
	}

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
		LOGGER.debug("Using AuthnLogin: " + System.getenv().get("CONJUR_AUTHN_LOGIN"));
		LOGGER.debug("Using Password: " + System.getenv().get("CONJUR_AUTHN_API_KEY"));
		LOGGER.debug("Using SSL CERT: " + System.getenv().get("CONJUR_SSL_CERTIFICATE"));

		AccessToken accessToken = conjurClient.getNewAccessToken();
		if (accessToken == null)
			throw new IllegalArgumentException("Unable to get the Access Token from CyberArk Conjur. Please check your configuration!");
		conjurClient.setAccessToken(accessToken.getHeaderValue());
		com.cyberark.conjur.sdk.Configuration.setDefaultApiClient(conjurClient);
	}


}