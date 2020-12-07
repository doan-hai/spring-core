package com.codergeezer.core.base.oauth2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@ConfigurationProperties(prefix = "core.oauth2")
public class OAuth2Properties {

    private boolean resourceServer = true;

    private String publicKey;

    private List<String> ignoreSecuritiesUri = new ArrayList<>();
}
