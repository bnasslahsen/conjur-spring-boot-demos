package org.cyberark.conjur.demos.common;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.actuate.availability.LivenessStateHealthIndicator;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.boot.availability.LivenessState;
import org.springframework.stereotype.Component;

/**
 * @author bnasslahsen
 */
@Component
public class ConjurLivenessStateHealthIndicator extends LivenessStateHealthIndicator {

	private final String scriptPath = "/conjur/status/conjur-secrets-unchanged.sh";

	private static final Logger LOGGER = LoggerFactory.getLogger(ConjurLivenessStateHealthIndicator.class);

	public ConjurLivenessStateHealthIndicator(ApplicationAvailability availability) {
		super(availability);
	}

	protected AvailabilityState getState(ApplicationAvailability applicationAvailability) {
		if (secretsChanged())
			return LivenessState.BROKEN;
		return applicationAvailability.getLivenessState();
	}

	private boolean secretsChanged() {
		ProcessBuilder pb = new ProcessBuilder(scriptPath);
		try {
			Process p = pb.start();
			int exitCode = p.waitFor();
			if (exitCode == 0) {
				LOGGER.debug("No password change");
				return false;
			}
			else {
				LOGGER.debug("Password Change detected");
				return true;
			}
		}
		catch (IOException | InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
	}
}