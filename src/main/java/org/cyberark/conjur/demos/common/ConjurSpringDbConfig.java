package org.cyberark.conjur.demos.common;

import com.cyberark.conjur.springboot.annotations.ConjurPropertySource;

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
@ConjurPropertySource({})
public class ConjurSpringDbConfig extends DataSourceProperties {

	/**
	 * The Url.
	 */
	@Value("${url}")
	private byte[] url;

	/**
	 * The Username.
	 */
	@Value("${username}")
	private byte[] username;

	/**
	 * The Password.
	 */
	@Value("${password}")
	private byte[] password;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		this.setUrl(new String(url));
		this.setUsername(new String(username));
		this.setPassword(new String(password));
	}
}