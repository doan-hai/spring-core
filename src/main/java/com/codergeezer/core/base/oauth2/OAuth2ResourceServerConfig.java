package com.codergeezer.core.base.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration
@ConditionalOnMissingBean
@Import(ResourceServerAutoConfig.class)
@ConditionalOnProperty(name = "core.oauth2.resource-server")
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private OAuth2Properties oAuth2Properties;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(oAuth2Properties.getIgnoreSecuritiesUri().toArray(new String[0])).permitAll()
            .anyRequest().authenticated();
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.tokenServices(this.tokenServices());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "core.oauth2.resource-server")
    public TokenStore tokenStore() {
        return new JwtTokenStore(this.accessTokenConverter());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "core.oauth2.resource-server")
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifier(new RsaVerifier(this.oAuth2Properties.getPublicKey()));
        return converter;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "core.oauth2.resource-server")
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(this.tokenStore());
        return defaultTokenServices;
    }
}
