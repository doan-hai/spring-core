package com.codergeezer.core.base.feign;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author haidv
 * @version 1.0
 */
@EnableConfigurationProperties(FeignProperties.class)
public class FeignTransferHeaderAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "core.feign.client.transfer-header-enabled")
    public RequestInterceptor transferAuthInterceptor(FeignProperties feignProperties) {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest httpServletRequest = attributes.getRequest();
                Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String headerName = headerNames.nextElement();
                        if (CollectionUtils.isEmpty(feignProperties.getTransferHeaderProperties()) ||
                            feignProperties.getTransferHeaderProperties().contains(headerName)) {
                            template.header(headerName, httpServletRequest.getHeader(headerName));
                        }
                    }
                }
            }
        };
    }
}
