package org.cyberark.conjur.demos.common;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

/**
 * @author bnasslahsen
 */
@Profile("refreshable")
@Configuration(proxyBeanMethods = false)
public class RefreshableDatasource {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RefreshableDatasource.class);

	/**
	 * The Data source properties.
	 */
	private final DataSourceProperties dataSourceProperties;

	/**
	 * Instantiates a new Refreshable datasource.
	 *
	 * @param dataSourceProperties the data source properties
	 */
	public RefreshableDatasource(DataSourceProperties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}

	@RefreshScope
	@Bean
	public DataSource getDatasource() {
		LOGGER.debug("Database Refresh Triggered");
		return DataSourceBuilder.create().url(dataSourceProperties.getUrl())
				.username(dataSourceProperties.getUsername())
				.password(dataSourceProperties.getPassword()).build();
	}

	/**
	 * This method reacts to the refresh event and prints properties value.
	 */
	@EventListener(RefreshScopeRefreshedEvent.class)
	public void reactOnRefresh() {
		LOGGER.debug("********************************");
		LOGGER.debug("*** Refresh Event Triggered ***");
		LOGGER.debug("********************************");
		LOGGER.debug("Refreshing database connection...");
		LOGGER.debug("DB Url = {}", dataSourceProperties.getUrl());
		LOGGER.debug("DB Username = {}", dataSourceProperties.getUsername());
		LOGGER.debug("DB Password = {}", dataSourceProperties.getPassword());
	}
}