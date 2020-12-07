package com.codergeezer.core.base.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
public class RequestConstant {

    public static final String REQUEST_ID = "X-Request-ID";

    public static final String SERVICE_NAME = "service-name";

    public static final String REQUEST_TIME_START = "time-start";

    public static final String AUTHORIZATION = "authorization";

    public static final String REQUEST_LOGGING = "logging";

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String BASIC_PREFIX = "Basic ";

    private static final List<String> WHITE_LIST_REQUEST = Arrays.asList("/swagger-ui.html/**", "/swagger-resources/**",
                                                                         "/webjars/**", "/swagger-ui.html#!/**",
                                                                         "/v2/api-docs", "/actuator/**", "/css/**",
                                                                         "/js/**", "/**/*.png", "/**/*.gif",
                                                                         "/**/*.svg", "/**/*.jpg", "/**/*.html",
                                                                         "/**/*.css", "/**/*.js", "/favicon.ico", "/",
                                                                         "/error", "/csrf");

    private RequestConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static List<String> getWhiteListRequest() {
        return WHITE_LIST_REQUEST;
    }
}
