package org.cyberark.conjur.demos.common;


import com.cyberark.conjur.sdk.endpoint.SecretsApi;
import com.cyberark.conjur.springboot.constant.ConjurConstant;
import com.cyberark.conjur.springboot.domain.ConjurProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * The type Conjur java db config.
 *
 * @author bnasslahsen
 */
@Profile("secured-java")
@Primary
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "spring.datasource")
public class ConjurJavaDbConfig extends DataSourceProperties {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ConjurJavaDbConfig.class);

	/**
	 * The Conjur config.
	 */
	private final ConjurProperties conjurProperties;

	/**
	 * The App config.
	 */
	private final DbVaultProperties dbVaultProperties;

	/**
	 * The Sql initialization properties.
	 */
	private final SqlInitializationProperties sqlInitializationProperties;

	/**
	 * The Secrets api.
	 */
	private final SecretsApi secretsApi;

	/**
	 * Instantiates a new Conjur java db config.
	 *
	 * @param conjurProperties            the conjur properties
	 * @param dbVaultProperties           the db vault properties
	 * @param sqlInitializationProperties the sql initialization properties
	 * @param secretsApi                  the secrets api
	 */
	public ConjurJavaDbConfig(ConjurProperties conjurProperties, DbVaultProperties dbVaultProperties, SqlInitializationProperties sqlInitializationProperties, SecretsApi secretsApi) {
		this.conjurProperties = conjurProperties;
		this.dbVaultProperties = dbVaultProperties;
		this.sqlInitializationProperties = sqlInitializationProperties;
		this.secretsApi = secretsApi;
	}

	/**
	 * After properties set.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		String dbAddress = secretsApi.getSecret(conjurProperties.getAccount(), ConjurConstant.CONJUR_KIND, dbVaultProperties.getAddress());
		LOGGER.debug("DB dbAddress = {}", dbAddress);
		String dbPort = secretsApi.getSecret(conjurProperties.getAccount(), ConjurConstant.CONJUR_KIND, dbVaultProperties.getPort());
		LOGGER.debug("DB dbPort = {}", dbPort);
		String dbUsername = secretsApi.getSecret(conjurProperties.getAccount(), ConjurConstant.CONJUR_KIND, dbVaultProperties.getUsername());
		LOGGER.debug("DB dbUsername = {}", dbUsername);
		String dbPassword = secretsApi.getSecret(conjurProperties.getAccount(), ConjurConstant.CONJUR_KIND, dbVaultProperties.getPassword());
		LOGGER.debug("DB dbPassword = {}", dbPassword);
		String database = secretsApi.getSecret(conjurProperties.getAccount(), ConjurConstant.CONJUR_KIND, dbVaultProperties.getDatabase());
		LOGGER.debug("DB database = {}", database);
		String url = "jdbc:" + sqlInitializationProperties.getPlatform() + "://" + dbAddress + ":" + dbPort + "/" + database;
		LOGGER.debug("DB url = {}", url);
		this.setUrl(url);
		this.setUsername(dbUsername);
		this.setPassword(dbPassword);
	}

}