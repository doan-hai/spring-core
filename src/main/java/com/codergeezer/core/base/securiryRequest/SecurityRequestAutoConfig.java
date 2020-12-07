package com.codergeezer.core.base.securiryRequest;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SecurityRequestProperties.class)
public class SecurityRequestAutoConfig {

}
