package org.cyberark.conjur.demos.common;

import com.cyberark.conjur.springboot.annotations.ConjurValue;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

/**
 * The type Conjur spring db config.
 *
 * @author bnasslahsen
 */
//@Primary
//@Profile("secured")
//@Configuration(proxyBeanMethods=false)
//@ConfigurationProperties(prefix = "spring.datasource")
public class ConjurSpringDbConfig extends DataSourceProperties {

	/**
	 * The Url.
	 */
	@ConjurValue(key="data/bnl/ocp-apps/url")
	private byte[] url;

	/**
	 * The Username.
	 */
	@ConjurValue(key="data/bnl/ocp-apps/username")
	private byte[] username;

	/**
	 * The Password.
	 */
	@ConjurValue(key="data/bnl/ocp-apps/password")
    private byte[] password;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        this.setUrl(new String(url));
        this.setUsername(new String(username));
        this.setPassword(new String(password));
    }
}