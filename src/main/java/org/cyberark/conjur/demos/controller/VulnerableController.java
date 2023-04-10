package org.cyberark.conjur.demos.controller;

import java.util.HashMap;
import java.util.Map;

import com.cyberark.conjur.sdk.ApiException;
import com.cyberark.conjur.sdk.endpoint.SecretsApi;
import com.cyberark.conjur.springboot.core.env.ConjurConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.http.ResponseEntity;
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
	 * The Secrets api.
	 */
	private final SecretsApi secretsApi;

	/**
	 * The Object mapper.
	 */
	private ObjectMapper objectMapper;

	/**
	 * The constant VARIABLE.
	 */
	private static final String VARIABLE = ":variable:";

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(VulnerableController.class);

	/**
	 * Instantiates a new Vulnerable controller.
	 *
	 * @param dataSourceProperties the data source properties
	 * @param secretsApi the secrets api
	 * @param objectMapper the object mapper
	 */
	public VulnerableController(DataSourceProperties dataSourceProperties, SecretsApi secretsApi, ObjectMapper objectMapper) {
		this.dataSourceProperties = dataSourceProperties;
		this.secretsApi = secretsApi;
		this.objectMapper=objectMapper;
	}


	/**
	 * Gets db properties.
	 *
	 * @return the db properties
	 * @throws JsonProcessingException the json processing exception
	 */
	@GetMapping("/db-properties")
	public ResponseEntity<String> getDbProperties() throws JsonProcessingException {
		Map<String, String> properties = new HashMap<>();
		properties.put("dbUrl", dataSourceProperties.getUrl());
		properties.put("dbUsername", dataSourceProperties.getUsername());
		properties.put("dbPassword", dataSourceProperties.getPassword());
		ObjectWriter writer = objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT);
		String json = writer.writeValueAsString(properties);
		return ResponseEntity.ok().body(json);
	}

	/**
	 * Gets secrets.
	 *
	 * @return the secrets
	 * @throws JsonProcessingException the json processing exception
	 */
	@GetMapping("/secrets")
	@Hidden
	public ResponseEntity<String> getSecrets() throws JsonProcessingException {
		Map<String, String> response;
		try {
			String account = secretsApi.getApiClient().getAccount();
			StringBuilder bulkSecrets = new StringBuilder();
			String urlConjurKey = account + VARIABLE + ConjurConfig.getInstance().mapProperty("db.url");
			bulkSecrets.append(urlConjurKey).append(",");
			String usernameConjurKey = account + VARIABLE + ConjurConfig.getInstance().mapProperty("db.username");
			bulkSecrets.append(usernameConjurKey).append(",");
			String passwordConjurKey = account + VARIABLE +  ConjurConfig.getInstance().mapProperty("db.password");
			bulkSecrets.append(passwordConjurKey);
			response = (Map<String, String>) secretsApi.getSecrets(bulkSecrets.toString());
		}
		catch (ApiException e) {
			LOGGER.error("Status code: " + e.getCode());
			LOGGER.error("Reason: " + e.getResponseBody());
			LOGGER.error(e.getMessage());
			String message = StringUtils.isEmpty(e.getMessage()) ? e.getResponseBody() : e.getMessage();
			throw new RuntimeException(message, e);
		}
		ObjectWriter writer = objectMapper.writer().with(SerializationFeature.INDENT_OUTPUT);
		String json = writer.writeValueAsString(response);
		return ResponseEntity.ok().body(json);
	}
}
