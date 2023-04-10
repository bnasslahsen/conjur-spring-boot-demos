package org.cyberark.conjur.demos.common;

import java.io.IOException;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author bnasslahsen
 */
@Component
public class ConjurLivenessStateHealthIndicator implements HealthIndicator {

	private final String scriptPath = "/conjur/status/conjur-secrets-unchanged.sh";

	@Override
	public Health health() {
		ProcessBuilder pb = new ProcessBuilder(scriptPath);
		try {
			Process p = pb.start();
			int exitCode = p.waitFor();
			if (exitCode == 0) {
				return Health.up().build();
			} else {
				return Health.down().withDetail("exitCode", exitCode).build();
			}
		} catch (IOException | InterruptedException e) {
			return Health.down(e).build();
		}
	}
}