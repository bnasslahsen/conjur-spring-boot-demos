package org.cyberark.conjur.demos.common;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

/**
 * @author bnasslahsen
 */
//@Configuration(proxyBeanMethods = false)
public class RefreshableDatasource {

	@RefreshScope
	@Bean
	public DataSource getDatasource(DataSourceProperties dataSourceProperties) {
		return DataSourceBuilder.create().url(dataSourceProperties.getUrl())
				.username(dataSourceProperties.getUsername())
				.password(dataSourceProperties.getPassword()).build();
	}
}