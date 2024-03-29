package com.codergeezer.core.base.exception;

import com.codergeezer.core.base.logging.LoggingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.codergeezer.core.base.data.ResponseUtils.getResponseDataError;
import static com.codergeezer.core.base.exception.CommonErrorCode.FORBIDDEN;
import static com.codergeezer.core.base.logging.LoggingUtil.logRequest;
import static com.codergeezer.core.base.logging.LoggingUtil.logResponse;
import static com.codergeezer.core.base.utils.JsonUtils.toJson;

/**
 * @author haidv
 * @version 1.0
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessDeniedHandler.class);

    private final LoggingProperties loggingProperties;

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    public CustomAccessDeniedHandler(LoggingProperties loggingProperties) {
        this.loggingProperties = loggingProperties;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc)
            throws IOException {
        logRequest(request, serviceName, loggingProperties);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            LOGGER.warn(String.format("User: %s attempted to access the protected URL: %s", auth.getName(),
                                      request.getRequestURI()));
        }
        var errorCode = FORBIDDEN;
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter()
                .write(toJson(getResponseDataError(errorCode.getCode(), errorCode.getMessage(), null, false)));
        logResponse(request, response, loggingProperties);
    }
}
