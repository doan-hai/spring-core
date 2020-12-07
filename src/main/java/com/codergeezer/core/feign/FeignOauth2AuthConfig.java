package com.codergeezer.core.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@EnableConfigurationProperties(FeignProperties.class)
public class FeignOauth2AuthConfig {

    private static final String CONTEXT_PREFIX = "FeignContext-";

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "core.feign.client.oauth2-enabled")
    public RequestInterceptor oAuth2FeignRequestInterceptor(FeignProperties feignProperties) {
        ClientCredentialsResourceDetails details;
        try {
            List<Oauth2Properties> properties = feignProperties.getOauth2Properties();
            if (CollectionUtils.isEmpty(properties)) {
                throw new IllegalArgumentException("init bean failed!");
            }
            String className = applicationContext.getDisplayName().replace(CONTEXT_PREFIX, "").toLowerCase();
            Optional<Oauth2Properties> opt = properties.stream().filter(v -> v.getFeignClassName()
                                                                              .contains(className))
                                                       .findFirst();
            if (!opt.isPresent()) {
                throw new IllegalArgumentException(
                        String.format("init bean failed because feign class %s not config!", className));
            }
            Oauth2Properties prop = opt.get();
            details = new ClientCredentialsResourceDetails();
            details.setAccessTokenUri(prop.getAccessTokenUri());
            details.setClientId(prop.getClientId());
            details.setClientSecret(prop.getClientSecret());
            if (prop.getScopes() != null) {
                details.setScope(Arrays.asList(prop.getScopes().split(",")));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("init bean failed because: %s", e.getMessage()));
        }
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), details);
    }
}
