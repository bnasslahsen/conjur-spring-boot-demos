package org.cyberark.conjur.demos.common;

import java.util.Map;

import com.cyberark.conjur.sdk.ApiException;
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
	 * The Secrets api.
	 */
	private final SecretsApi secretsApi;

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ConjurJavaDbConfig.class);


	/**
	 * Instantiates a new Conjur java db config.
	 *
	 * @param secretsConfig the secrets config
	 * @param secretsApi the secrets api
	 * @param secretsApi1 the secrets api 1
	 */
	public ConjurJavaDbConfig(SecretsConfig secretsConfig, SecretsApi secretsApi, SecretsApi secretsApi1) {
		this.secretsConfig = secretsConfig;
		this.secretsApi = secretsApi1;
	}

	/**
	 * After properties set.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
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
			LOGGER.error("Status code: " + e.getCode());
			LOGGER.error("Reason: " + e.getResponseBody());
			LOGGER.error(e.getMessage());
			String message = StringUtils.isEmpty(e.getMessage()) ? e.getResponseBody() : e.getMessage();
			throw new RuntimeException(message, e);
		}
	}

}