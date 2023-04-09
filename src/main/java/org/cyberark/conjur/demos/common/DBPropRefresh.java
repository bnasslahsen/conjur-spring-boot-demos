package org.cyberark.conjur.demos.common;

import javax.sql.DataSource;

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
public class DBPropRefresh {
	
	private final DataSourceProperties dataSourceProperties;

	public DBPropRefresh(DataSourceProperties dataSourceProperties) {
		System.out.println("conjur.auto-refresh.enabled");
		this.dataSourceProperties = dataSourceProperties;
	}


	@Bean
	@RefreshScope
	public DataSource getDatasource() {
		System.out.println("conjur.auto-refresh.enabled");
		return DataSourceBuilder.create().url(dataSourceProperties.getUrl())
				.username(dataSourceProperties.getUsername())
				.password(dataSourceProperties.getPassword()).build();
	}
}