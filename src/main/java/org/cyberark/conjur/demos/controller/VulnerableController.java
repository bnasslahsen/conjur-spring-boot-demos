package org.cyberark.conjur.demos.controller;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Vulnerable controller.
 */
@RestController
@Tag(name = "Vulnerable")
class VulnerableController {

	/**
	 * The Data source properties.
	 */
	private final DataSourceProperties dataSourceProperties;

	/**
	 * Instantiates a new Vulnerable controller.
	 *
	 * @param dataSourceProperties the data source properties
	 */
	public VulnerableController(DataSourceProperties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}


	/**
	 * Gets db properties.
	 *
	 * @return the db properties
	 */
	@GetMapping("/db-properties")
	public Map<String, String> getDbProperties() {
		Map<String, String> properties = new HashMap<>();
		properties.put("dbUrl", dataSourceProperties.getUrl());
		properties.put("dbUsername", dataSourceProperties.getUsername());
		properties.put("dbPassword", dataSourceProperties.getPassword());
		return properties;
	}

}
