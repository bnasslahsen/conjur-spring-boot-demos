package org.cyberark.conjur.demos.common;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bnasslahsen
 */
@Configuration(proxyBeanMethods = false)
public class RefreshableDatasource {

	private static final Logger LOGGER = LoggerFactory.getLogger(RefreshableDatasource.class);

	//@RefreshScope
	@Bean
	public DataSource getDatasource(DataSourceProperties dataSourceProperties) {
		LOGGER.debug("Database Refresh Triggered");
		return DataSourceBuilder.create().url(dataSourceProperties.getUrl())
				.username(dataSourceProperties.getUsername())
				.password(dataSourceProperties.getPassword()).build();
	}

}