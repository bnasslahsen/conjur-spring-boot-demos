package org.cyberark.conjur.demos.common;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author bnasslahsen
 */
@Component
public class ConjurLivenessStateHealthIndicator implements HealthIndicator {

	private final String scriptPath = "/conjur/status/conjur-secrets-unchanged.sh";

	private static final Logger LOGGER = LoggerFactory.getLogger(ConjurLivenessStateHealthIndicator.class);

	@Override
	public Health health() {
		ProcessBuilder pb = new ProcessBuilder(scriptPath);
		try {
			Process p = pb.start();
			int exitCode = p.waitFor();
			if (exitCode == 0) {
				LOGGER.debug("No password change");
				return Health.up().build();
			} else {
				LOGGER.debug("Password Change detected");
				return Health.down().withDetail("exitCode", exitCode).build();
			}
		} catch (IOException | InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
			return Health.down(e).build();
		}
	}
}