package org.cyberark.conjur.demos.common;

import com.cyberark.conjur.springboot.annotations.ConjurPropertySource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * @author bnasslahsen
 */
@Profile("secured-api")
@Primary
@Configuration(proxyBeanMethods=false)
@ConfigurationProperties(prefix = "spring.datasource")
@ConjurPropertySource(value={"data/vault/bnl-ocp-safe/Database-MySQL-jdbch2memtestdb-h2-user/"})
public class ConjurSpringDbConfig extends DataSourceProperties {

    @Value("${address}")
	private byte[] url;
    @Value("${username}")
    private byte[] username;

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