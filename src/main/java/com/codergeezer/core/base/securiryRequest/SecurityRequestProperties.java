package com.codergeezer.core.base.securiryRequest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the logging abstraction.
 *
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@ConfigurationProperties(prefix = "core.security.request")
public class SecurityRequestProperties {

    private boolean encryptResponseBody = false;

    private boolean decryptRequestBody = false;

    private String securityBeanName;
}
