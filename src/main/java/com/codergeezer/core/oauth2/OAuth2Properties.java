package com.codergeezer.core.oauth2;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@ConfigurationProperties(prefix = "core.oauth2")
public class OAuth2Properties {

    private boolean resourceServer = true;

    private String publicKey;

    private List<String> ignoreSecuritiesUri = new ArrayList<>();

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public List<String> getIgnoreSecuritiesUri() {
        return ignoreSecuritiesUri;
    }

    public void setIgnoreSecuritiesUri(List<String> ignoreSecuritiesUri) {
        this.ignoreSecuritiesUri = ignoreSecuritiesUri;
    }

    public boolean isResourceServer() {
        return resourceServer;
    }

    public void setResourceServer(boolean resourceServer) {
        this.resourceServer = resourceServer;
    }
}
