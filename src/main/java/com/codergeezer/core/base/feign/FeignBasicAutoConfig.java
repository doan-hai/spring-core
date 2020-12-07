package com.codergeezer.core.base.feign;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@EnableConfigurationProperties(FeignProperties.class)
public class FeignBasicAutoConfig {

    private static final String CONTEXT_PREFIX = "FeignContext-";

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "core.feign.client.basic-auth-enabled")
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(FeignProperties feignProperties) {
        List<BasicAuthProperties> properties = feignProperties.getBasicAuthProperties();
        if (CollectionUtils.isEmpty(properties)) {
            throw new IllegalArgumentException("init bean failed!");
        }
        String className = applicationContext.getDisplayName().replace(CONTEXT_PREFIX, "").toLowerCase();
        Optional<BasicAuthProperties> opt = properties.stream().filter(v -> v.getFeignClassName()
                                                                             .contains(className))
                                                      .findFirst();
        if (!opt.isPresent()) {
            throw new IllegalArgumentException(
                    String.format("init bean failed because feign class %s not config!", className));
        }
        BasicAuthProperties prop = opt.get();
        return new BasicAuthRequestInterceptor(prop.getUsername(), prop.getPassword());
    }
}
