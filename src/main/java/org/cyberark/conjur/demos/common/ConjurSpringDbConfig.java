package org.cyberark.conjur.demos.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * The type Conjur spring db config.
 *
 * @author bnasslahsen
 */
@Profile("secured")
@Primary
@Configuration(proxyBeanMethods=false)
@ConfigurationProperties(prefix = "spring.datasource")
public class ConjurSpringDbConfig extends DataSourceProperties {

	/**
	 * The Url.
	 */
	@Value("${data/bnl/ocp-apps/url}")
	private byte[] url;

	/**
	 * The Username.
	 */
	@Value("${data/bnl/ocp-apps/username}")
	private byte[] username;

	/**
	 * The Password.
	 */
	@Value("${data/bnl/ocp-apps/password}")
	private byte[] password;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		this.setUrl(new String(url));
		this.setUsername(new String(username));
		this.setPassword(new String(password));
	}
}