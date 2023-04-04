package org.cyberark.conjur.demos.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type App config.
 *
 * @author bnasslahsen
 */
@ConfigurationProperties(prefix = "app.secret")
@Configuration(proxyBeanMethods = false)
public class SecretsConfig {

	/**
	 * The Url.
	 */
	private String url;

	/**
	 * The Username.
	 */
	private String username;

	/**
	 * The Password.
	 */
	private String password;

	/**
	 * Gets url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets url.
	 *
	 * @param url the url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets username.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets password.
	 *
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
