package org.cyberark.conjur.demos.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import org.springframework.stereotype.Component;

/**
 * @author bnasslahsen
 */
@OpenAPIDefinition(info = @Info(title = "PetClinic API",
		description = "PetClinic Sample API", version = "v1"))
@Component
public class OpenApiConfig {}
