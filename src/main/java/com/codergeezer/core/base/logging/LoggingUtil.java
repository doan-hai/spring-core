package com.codergeezer.core.base.logging;

import com.codergeezer.core.base.constant.RequestConstant;
import com.codergeezer.core.base.utils.JsonUtils;
import com.codergeezer.core.base.utils.RequestUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author haidv
 * @version 1.0
 */
public class LoggingUtil {

    private static final String LOG_REQUEST_PREFIX = "REQUEST: {}";

    private static final String LOG_REQUEST_BODY_PREFIX = "REQUEST BODY: {}";

    private static final String LOG_RESPONSE_PREFIX = "RESPONSE: {}";

    private static final String LOG_RESPONSE_SUFFIX = "...";

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingUtil.class);

    private LoggingUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void logRequest(HttpServletRequest servletRequest, String serviceName,
                                  LoggingProperties loggingProperties) {
        var requestId = servletRequest.getHeader(RequestConstant.REQUEST_ID);
        ThreadContext.put(RequestConstant.REQUEST_ID, requestId == null ? UUID.randomUUID().toString() : requestId);
        ThreadContext.put(RequestConstant.SERVICE_NAME, serviceName);
        servletRequest.setAttribute(RequestConstant.REQUEST_TIME_START, System.currentTimeMillis());
        if (RequestUtils.matches(servletRequest, loggingProperties.getIgnoreLogUri())) {
            servletRequest.setAttribute(RequestConstant.REQUEST_LOGGING, false);
            return;
        }
        servletRequest.setAttribute(RequestConstant.REQUEST_LOGGING, true);
        var requestObject = LogRequestObject.builder()
                                            .localIp(servletRequest.getLocalAddr())
                                            .headers(buildHeadersMap(servletRequest))
                                            .httpMethod(servletRequest.getMethod())
                                            .httpPath(servletRequest.getRequestURI())
                                            .clientIp(servletRequest.getRemoteHost())
                                            .parameters(buildParametersMap(servletRequest))
                                            .build();
        var message = JsonUtils.toJson(requestObject);
        LOGGER.info(LOG_REQUEST_PREFIX, message);
    }

    public static void logRequest(HttpServletRequest servletRequest, LoggingProperties loggingProperties, Object body) {
        if (isNotLogging(servletRequest, loggingProperties.getIgnoreLogUri())) {
            return;
        }
        if (body instanceof String) {
            LOGGER.info(LOG_REQUEST_BODY_PREFIX, body);
        } else {
            String message = JsonUtils.toJson(body);
            LOGGER.info(LOG_REQUEST_BODY_PREFIX, message);
        }
    }

    public static void logResponse(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                                   LoggingProperties loggingProperties) {
        logResponse(servletRequest, servletResponse, loggingProperties, null);
    }

    public static void logResponse(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                                   LoggingProperties loggingProperties, Object object) {
        if (isNotLogging(servletRequest, loggingProperties.getIgnoreLogUri())) {
            return;
        }
        Object o = servletRequest.getAttribute(RequestConstant.REQUEST_TIME_START);
        var during = o == null ? 0 : (System.currentTimeMillis() - (long) o);
        var responseObject = LogResponseObject.builder()
                                              .responseCode(servletResponse.getStatus())
                                              .during(String.format("%.3f", (double) during / 1000))
                                              .localIp(servletRequest.getLocalAddr())
                                              .headers(buildHeadersMap(servletResponse))
                                              .clientIp(servletRequest.getRemoteHost())
                                              .body(loggingProperties.isExcludeResponseBody() ? object : null)
                                              .build();
        var str = JsonUtils.toJson(responseObject);
        if (str.length() > loggingProperties.getResponseMaxPayloadLength()) {
            str = str.substring(0, loggingProperties.getResponseMaxPayloadLength()) + LOG_RESPONSE_SUFFIX;
        }
        LOGGER.info(LOG_RESPONSE_PREFIX, str);
        ThreadContext.clearAll();
    }

    private static boolean isNotLogging(HttpServletRequest servletRequest, Set<String> ignorePatterns) {
        var isLog = servletRequest.getAttribute(RequestConstant.REQUEST_LOGGING);
        if (isLog == null) {
            return RequestUtils.matches(servletRequest, ignorePatterns);
        }
        return !(boolean) isLog;
    }

    private static Map<String, String> buildParametersMap(HttpServletRequest servletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        var parameterNames = servletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            var key = parameterNames.nextElement();
            var value = servletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    @SuppressWarnings("rawtypes")
    private static Map<String, String> buildHeadersMap(HttpServletRequest servletRequest) {
        Map<String, String> map = new HashMap<>();
        var headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            var key = (String) headerNames.nextElement();
            if (key.equalsIgnoreCase(RequestConstant.AUTHORIZATION)) {
                map.put(key, "<<Not log authorization record>>");
                continue;
            }
            map.put(key, servletRequest.getHeader(key));
        }
        return map;
    }

    private static Map<String, String> buildHeadersMap(HttpServletResponse servletResponse) {
        Map<String, String> map = new HashMap<>();
        var headerNames = servletResponse.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, servletResponse.getHeader(header));
        }
        return map;
    }

    @RequiredArgsConstructor @Builder @Getter
    public static class LogRequestObject {

        private final String httpMethod;

        private final String httpPath;

        private final String clientIp;

        private final String localIp;

        private final Map<String, String> headers;

        private final Map<String, String> parameters;
    }

    @RequiredArgsConstructor @Builder @Getter
    public static class LogResponseObject {

        private final int responseCode;

        private final String during;

        private final String clientIp;

        private final String localIp;

        private final Map<String, String> headers;

        private final Object body;
    }
}
