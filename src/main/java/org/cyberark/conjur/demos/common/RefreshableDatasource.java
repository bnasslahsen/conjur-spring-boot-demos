package org.cyberark.conjur.demos.common;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bnasslahsen
 */
@Configuration(proxyBeanMethods = false)
@RefreshScope
@ConditionalOnProperty(name = "conjur.refresh.enabled")
public class RefreshableDatasource {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RefreshableDatasource.class);
	
	private final DataSourceProperties dataSourceProperties;

	public RefreshableDatasource(DataSourceProperties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}


	@Bean
	@RefreshScope
	public DataSource getDatasource() {
		LOGGER.debug("Conjur Refresh Enabled");
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(dataSourceProperties.getUrl());
		config.setUsername(dataSourceProperties.getUsername());
		config.setPassword(dataSourceProperties.getPassword());
		return new HikariDataSource(config);
	}
}