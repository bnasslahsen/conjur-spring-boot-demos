package org.cyberark.conjur.demos.common;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @author bnasslahsen
 */
//@Configuration(proxyBeanMethods = false)
//@RefreshScope
public class DBPropRefresh {
	
	private final DataSourceProperties dataSourceProperties;

	public DBPropRefresh(DataSourceProperties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}


	@Bean
	//@RefreshScope
	public DataSource getDatasource() {
		return DataSourceBuilder.create().url(dataSourceProperties.getUrl())
				.username(dataSourceProperties.getUsername())
				.password(dataSourceProperties.getPassword()).build();
	}
}