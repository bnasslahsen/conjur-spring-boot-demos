package org.cyberark.conjur.demos.common;

import com.cyberark.conjur.springboot.annotations.ConjurPropertySource;
import com.cyberark.conjur.springboot.annotations.ConjurValue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * @author bnasslahsen
 */
@Profile("secured-api-spring")
@Primary
@Configuration(proxyBeanMethods=false)
@ConfigurationProperties(prefix = "spring.datasource")
@ConjurPropertySource(value={"data/bnl/ocp-apps/"})
public class ConjurSpringDbConfig extends DataSourceProperties {

    @Value("${url}")
	private byte[] url;
	
    @Value("${username}")
    private byte[] username;

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