package org.cyberark.conjur.demos.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * The type Db vault properties.
 *
 * @author bnasslahsen
 */
@Profile("secured-java")
@ConfigurationProperties(prefix = "db.secret")
@Configuration(proxyBeanMethods = false)
public class DbVaultProperties {

	/**
	 * The Url.
	 */
	private String address;

	/**
	 * The Port.
	 */
	private String port;

	/**
	 * The Username.
	 */
	private String username;

	/**
	 * The Password.
	 */
	private String password;

	/**
	 * The Database.
	 */
	private String database;

	/**
	 * Gets address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets address.
	 *
	 * @param address the address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets port.
	 *
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * Sets port.
	 *
	 * @param port the port
	 */
	public void setPort(String port) {
		this.port = port;
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

	/**
	 * Gets database.
	 *
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * Sets database.
	 *
	 * @param database the database
	 */
	public void setDatabase(String database) {
		this.database = database;
	}
}
