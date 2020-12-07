package com.codergeezer.core.base.exception;

import com.codergeezer.core.base.config.MessageProvider;
import com.codergeezer.core.logging.LoggingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.codergeezer.core.base.data.ResponseUtils.getResponseDataError;
import static com.codergeezer.core.base.exception.CommonErrorCode.FORBIDDEN;
import static com.codergeezer.core.base.utils.JsonUtils.toJson;
import static com.codergeezer.core.logging.LoggingUtil.logRequest;
import static com.codergeezer.core.logging.LoggingUtil.logResponse;

/**
 * @author haidv
 * @version 1.0
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationEntryPoint.class);

    private final MessageProvider messageProvider;

    private final LoggingProperties loggingProperties;

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    public CustomAuthenticationEntryPoint(MessageProvider messageProvider,
                                          LoggingProperties loggingProperties) {
        this.messageProvider = messageProvider;
        this.loggingProperties = loggingProperties;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        logRequest(request, serviceName, loggingProperties);
        LOGGER.warn("You need to login first in order to perform this action.");
        CommonErrorCode errorCode = FORBIDDEN;
        String message;
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            message = messageProvider.getMessage(errorCode.getMessageCode());
        } catch (Exception ex) {
            message = errorCode.getDefaultMessage();
        }
        response.getWriter().print(toJson(getResponseDataError(errorCode.getCode(), message, null)));
        logResponse(request, response, loggingProperties);
    }
}
