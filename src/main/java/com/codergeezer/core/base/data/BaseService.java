package com.codergeezer.core.base.data;

import com.codergeezer.core.base.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.codergeezer.core.base.exception.CommonErrorCode.UNAUTHORIZED;

/**
 * This is the super interface for the service class of applications. <br> The service provides methods for read / write
 * operations on multiple entities using resource classes. <br> This interface defines the most common methods that
 * should be supported in all service classes.
 *
 * @author haidv
 * @version 1.0
 */
public abstract class BaseService {

    /**
     * Common logger for use in subclasses.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    protected Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    protected String getCurrentUsername() {
        Authentication authentication = getCurrentAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        throw new BaseException(UNAUTHORIZED);
    }
}
