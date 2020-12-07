package com.codergeezer.core.base.swagger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getExcludePath() {
        return excludePath;
    }

    public void setExcludePath(String excludePath) {
        this.excludePath = excludePath;
    }

    public List<TokenInfo> getTokenInfos() {
        return tokenInfos;
    }

    public void setTokenInfos(List<TokenInfo> tokenInfos) {
        this.tokenInfos = tokenInfos;
    }

    public List<GlobalOperationParameter> getGlobalOperationParameters() {
        return globalOperationParameters;
    }

    public void setGlobalOperationParameters(List<GlobalOperationParameter> globalOperationParameters) {
        this.globalOperationParameters = globalOperationParameters;
    }

    public boolean isAuthenticationWithOauth2() {
        return authenticationWithOauth2;
    }

    public void setAuthenticationWithOauth2(boolean authenticationWithOauth2) {
        this.authenticationWithOauth2 = authenticationWithOauth2;
    }

    public String getAuthOauth2AccessTokenUri() {
        return authOauth2AccessTokenUri;
    }

    public void setAuthOauth2AccessTokenUri(String authOauth2AccessTokenUri) {
        this.authOauth2AccessTokenUri = authOauth2AccessTokenUri;
    }

    public String getAuthOauth2DefaultClientId() {
        return authOauth2DefaultClientId;
    }

    public void setAuthOauth2DefaultClientId(String authOauth2DefaultClientId) {
        this.authOauth2DefaultClientId = authOauth2DefaultClientId;
    }

    public String getAuthOauth2DefaultClientSecret() {
        return authOauth2DefaultClientSecret;
    }

    public void setAuthOauth2DefaultClientSecret(String authOauth2DefaultClientSecret) {
        this.authOauth2DefaultClientSecret = authOauth2DefaultClientSecret;
    }
}
