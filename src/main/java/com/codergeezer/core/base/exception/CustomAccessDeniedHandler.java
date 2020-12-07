package com.codergeezer.core.base.exception;

import com.codergeezer.core.base.config.MessageProvider;
import com.codergeezer.core.logging.LoggingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
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
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessDeniedHandler.class);

    private final MessageProvider messageProvider;

    private final LoggingProperties loggingProperties;

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    public CustomAccessDeniedHandler(MessageProvider messageProvider,
                                     LoggingProperties loggingProperties) {
        this.messageProvider = messageProvider;
        this.loggingProperties = loggingProperties;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc)
            throws IOException {
        logRequest(request, serviceName, loggingProperties);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            LOGGER.warn(String.format("User: %s attempted to access the protected URL: %s", auth.getName(),
                                      request.getRequestURI()));
        }
        CommonErrorCode errorCode = FORBIDDEN;
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message;
        try {
            message = messageProvider.getMessage(errorCode.getMessageCode());
        } catch (Exception ex) {
            message = errorCode.getDefaultMessage();
        }
        response.getWriter().write(toJson(getResponseDataError(errorCode.getCode(), message, null)));
        logResponse(request, response, loggingProperties);
    }
}
