package com.codergeezer.core.feign;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@ConfigurationProperties(prefix = "core.feign.client")
public class FeignProperties {

    private Boolean basicAuthEnabled;

    private List<BasicAuthProperties> basicAuthProperties;

    private Boolean oauth2Enabled;

    private List<Oauth2Properties> oauth2Properties;

    private Boolean transferHeaderEnabled;

    private List<String> transferHeaderProperties;

    public Boolean getBasicAuthEnabled() {
        return basicAuthEnabled;
    }

    public void setBasicAuthEnabled(Boolean basicAuthEnabled) {
        this.basicAuthEnabled = basicAuthEnabled;
    }

    public List<BasicAuthProperties> getBasicAuthProperties() {
        return basicAuthProperties;
    }

    public void setBasicAuthProperties(List<BasicAuthProperties> basicAuthProperties) {
        this.basicAuthProperties = basicAuthProperties;
    }

    public Boolean getOauth2Enabled() {
        return oauth2Enabled;
    }

    public void setOauth2Enabled(Boolean oauth2Enabled) {
        this.oauth2Enabled = oauth2Enabled;
    }

    public List<Oauth2Properties> getOauth2Properties() {
        return oauth2Properties;
    }

    public void setOauth2Properties(List<Oauth2Properties> oauth2Properties) {
        this.oauth2Properties = oauth2Properties;
    }

    public Boolean getTransferHeaderEnabled() {
        return transferHeaderEnabled;
    }

    public void setTransferHeaderEnabled(Boolean transferHeaderEnabled) {
        this.transferHeaderEnabled = transferHeaderEnabled;
    }

    public List<String> getTransferHeaderProperties() {
        return transferHeaderProperties;
    }

    public void setTransferHeaderProperties(List<String> transferHeaderProperties) {
        this.transferHeaderProperties = transferHeaderProperties;
    }
}
