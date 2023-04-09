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
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * @author bnasslahsen
 */
@Configuration(proxyBeanMethods = false)
@RefreshScope(proxyMode = ScopedProxyMode.NO)
@ConditionalOnProperty(name = "conjur.refresh.enabled")
public class DBPropRefresh {

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DBPropRefresh.class);
	
	private final DataSourceProperties dataSourceProperties;

	public DBPropRefresh(DataSourceProperties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}


	@Bean
	@RefreshScope(proxyMode = ScopedProxyMode.NO)
	public DataSource getDatasource() {
		LOGGER.debug("Conjur Refresh Enabled");
		return DataSourceBuilder.create().url(dataSourceProperties.getUrl())
				.username(dataSourceProperties.getUsername())
				.password(dataSourceProperties.getPassword()).build();
	}
}