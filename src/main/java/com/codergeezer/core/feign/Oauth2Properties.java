package com.codergeezer.core.feign;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
public class Oauth2Properties {

    private String clientId;

    private String clientSecret;

    private String accessTokenUri;

    private String scopes;

    private List<String> feignClassName;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public List<String> getFeignClassName() {
        return feignClassName.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public void setFeignClassName(List<String> feignClassName) {
        this.feignClassName = feignClassName;
    }
}
