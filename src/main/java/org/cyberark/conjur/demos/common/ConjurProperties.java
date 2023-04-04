package org.cyberark.conjur.demos.common;

/**
 * @author bnasslahsen
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 *
 *  @ConjurProperties is POJO class annotated with @ConfigurationProperties to map
 * conjur authentication properties which are either externalized or set as Environment
 * parameters. ConjurProperties attributes will be used to get connection to Conjur Server.
 *
 *
 */
@Configuration
@ConfigurationProperties(prefix = "conjur")
public class ConjurProperties{

	private static final Logger LOGGER= LoggerFactory.getLogger(ConjurProperties.class);

	private String account;
	private String applianceUrl;
	private String authTokenFile;
	private String authApiKey;
	private String authnLogin;
	private String certFile;
	private String sslCertificate;
	/**
	 * The Jwt token path.
	 */
	private String jwtTokenPath;

	/**
	 * The Authn url.
	 */
	private String serviceId;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getApplianceUrl() {
		return applianceUrl;
	}

	public void setApplianceUrl(String applianceUrl) {
		this.applianceUrl = applianceUrl;
	}


	public String getAuthTokenFile() {
		return authTokenFile;
	}

	public void setAuthTokenFile(String authToken) {
		this.authTokenFile = authToken;
	}

	public String getAuthnLogin() {
		return authnLogin;
	}

	public void setAuthnLogin(String authnLogin) {
		this.authnLogin = authnLogin;
	}

	public String getCertFile() {
		return certFile;
	}

	public void setCertFile(String certFile) {
		this.certFile = certFile;
	}

	public String getSslCertificate() {
		return sslCertificate;
	}

	public void setSslCertificate(String sslCertificate) {
		this.sslCertificate = sslCertificate;
	}


	public String getJwtTokenPath() {
		return jwtTokenPath;
	}

	public void setJwtTokenPath(String jwtTokenPath) {
		this.jwtTokenPath = jwtTokenPath;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String toString() {
		return "ConjurAuthParam [account=" + account + ", applianceUrl=" + applianceUrl + ", authTokenFile=" + authTokenFile
				+ ", authnLogin=" + authnLogin + ", certFile=" + certFile + ", sslCertificate=" + sslCertificate
				+ "]";
	}

	public String getAuthApiKey() {
		return authApiKey;
	}

	public void setAuthApiKey(String authApiKey) {
		this.authApiKey = authApiKey;
	}




}