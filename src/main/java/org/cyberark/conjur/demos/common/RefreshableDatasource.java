package org.cyberark.conjur.demos.common;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
		LOGGER.debug("Database Refresh Triggered");
		return DataSourceBuilder.create().url(dataSourceProperties.getUrl())
				.username(dataSourceProperties.getUsername())
				.password(dataSourceProperties.getPassword()).build();
	}
}