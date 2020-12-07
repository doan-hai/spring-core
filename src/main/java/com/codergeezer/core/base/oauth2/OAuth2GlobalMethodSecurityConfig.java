package com.codergeezer.core.base.oauth2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * @author haidv
 * @version 1.0
 */
@ConditionalOnMissingBean
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(name = "core.oauth2.resource-server")
public class OAuth2GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return this.getOAuth2MethodSecurityExpressionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "core.oauth2.resource-server")
    public OAuth2MethodSecurityExpressionHandler getOAuth2MethodSecurityExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }
}
