package com.codergeezer.core.base.swagger;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@ConfigurationProperties(prefix = "core.swagger")
public class SwaggerProperties {

    private Boolean enabled;

    private String title = StringUtils.EMPTY;

    private String description = StringUtils.EMPTY;

    private String version = StringUtils.EMPTY;

    private String license = StringUtils.EMPTY;

    private String licenseUrl = StringUtils.EMPTY;

    private String termsOfServiceUrl = StringUtils.EMPTY;

    private String contactName = StringUtils.EMPTY;

    private String contactUrl = StringUtils.EMPTY;

    private String contactEmail = StringUtils.EMPTY;

    private String basePackage = StringUtils.EMPTY;

    private String excludePath = StringUtils.EMPTY;

    private List<GlobalOperationParameter> globalOperationParameters = new ArrayList<>();

    private boolean authenticationWithOauth2;

    private String authOauth2AccessTokenUri;

    private String authOauth2DefaultClientId;

    private String authOauth2DefaultClientSecret;

    private List<TokenInfo> tokenInfos = new ArrayList<>();
}
