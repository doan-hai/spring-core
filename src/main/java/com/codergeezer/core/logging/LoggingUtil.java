package com.codergeezer.core.logging;

import com.codergeezer.core.base.utils.JsonUtils;
import com.codergeezer.core.base.utils.RequestUtils;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.codergeezer.core.base.constant.RequestConstant.*;

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
        String requestId = servletRequest.getHeader(REQUEST_ID);
        ThreadContext.put(REQUEST_ID, requestId == null ? UUID.randomUUID().toString() : requestId);
        ThreadContext.put(SERVICE_NAME, serviceName);
        servletRequest.setAttribute(REQUEST_TIME_START, System.currentTimeMillis());
        if (RequestUtils.matches(servletRequest, loggingProperties.getIgnoreLogUri())) {
            servletRequest.setAttribute(REQUEST_LOGGING, false);
            return;
        }
        servletRequest.setAttribute(REQUEST_LOGGING, true);
        LogRequestObject requestObject = LogRequestObject.builder()
                                                         .localIp(servletRequest.getLocalAddr())
                                                         .headers(buildHeadersMap(servletRequest))
                                                         .httpMethod(servletRequest.getMethod())
                                                         .httpPath(servletRequest.getRequestURI())
                                                         .clientIp(servletRequest.getRemoteHost())
                                                         .parameters(buildParametersMap(servletRequest))
                                                         .build();
        String message = JsonUtils.toJson(requestObject);
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
        Object o = servletRequest.getAttribute(REQUEST_TIME_START);
        long during = o == null ? 0 : (System.currentTimeMillis() - (long) o);
        LogResponseObject responseObject = LogResponseObject.builder()
                                                            .responseCode(servletResponse.getStatus())
                                                            .during(String.format("%.3f", (double) during / 1000))
                                                            .localIp(servletRequest.getLocalAddr())
                                                            .headers(buildHeadersMap(servletResponse))
                                                            .clientIp(servletRequest.getRemoteHost())
                                                            .body(object)
                                                            .excludeBody(loggingProperties.isExcludeResponseBody())
                                                            .build();
        String str = JsonUtils.toJson(responseObject);
        if (str.length() > loggingProperties.getResponseMaxPayloadLength()) {
            str = str.substring(0, loggingProperties.getResponseMaxPayloadLength()) + LOG_RESPONSE_SUFFIX;
        }
        LOGGER.info(LOG_RESPONSE_PREFIX, str);
        ThreadContext.clearAll();
    }

    private static boolean isNotLogging(HttpServletRequest servletRequest, Set<String> ignorePatterns) {
        Object isLog = servletRequest.getAttribute(REQUEST_LOGGING);
        if (isLog == null) {
            return RequestUtils.matches(servletRequest, ignorePatterns);
        }
        return !(boolean) isLog;
    }

    private static Map<String, String> buildParametersMap(HttpServletRequest servletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = servletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = servletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    @SuppressWarnings("rawtypes")
    private static Map<String, String> buildHeadersMap(HttpServletRequest servletRequest) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            if (key.equalsIgnoreCase(AUTHORIZATION)) {
                map.put(key, "<<Not log authorization record>>");
                continue;
            }
            map.put(key, servletRequest.getHeader(key));
        }
        return map;
    }

    private static Map<String, String> buildHeadersMap(HttpServletResponse servletResponse) {
        Map<String, String> map = new HashMap<>();
        Collection<String> headerNames = servletResponse.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, servletResponse.getHeader(header));
        }
        return map;
    }

    public static class LogRequestObject {

        private final String httpMethod;

        private final String httpPath;

        private final String clientIp;

        private final String localIp;

        private final Map<String, String> headers;

        private final Map<String, String> parameters;

        LogRequestObject(String httpMethod, String httpPath, String clientIp, String localIp,
                         Map<String, String> headers, Map<String, String> parameters) {
            this.httpMethod = httpMethod;
            this.httpPath = httpPath;
            this.clientIp = clientIp;
            this.localIp = localIp;
            this.headers = headers;
            this.parameters = parameters;
        }

        public static LogRequestObjectBuilder builder() {
            return new LogRequestObjectBuilder();
        }

        @SuppressWarnings("unused")
        public String getHttpMethod() {
            return httpMethod;
        }

        @SuppressWarnings("unused")
        public String getHttpPath() {
            return httpPath;
        }

        @SuppressWarnings("unused")
        public String getClientIp() {
            return clientIp;
        }

        @SuppressWarnings("unused")
        public String getLocalIp() {
            return localIp;
        }

        @SuppressWarnings("unused")
        public Map<String, String> getHeaders() {
            return headers;
        }

        @SuppressWarnings("unused")
        public Map<String, String> getParameters() {
            return parameters;
        }

        public static class LogRequestObjectBuilder {

            private String httpMethod;

            private String httpPath;

            private String clientIp;

            private String localIp;

            private Map<String, String> headers;

            private Map<String, String> parameters;

            LogRequestObjectBuilder() {
            }

            public LogRequestObjectBuilder httpMethod(String httpMethod) {
                this.httpMethod = httpMethod;
                return this;
            }

            public LogRequestObjectBuilder httpPath(String httpPath) {
                this.httpPath = httpPath;
                return this;
            }

            public LogRequestObjectBuilder clientIp(String clientIp) {
                this.clientIp = clientIp;
                return this;
            }

            public LogRequestObjectBuilder localIp(String localIp) {
                this.localIp = localIp;
                return this;
            }

            public LogRequestObjectBuilder headers(Map<String, String> headers) {
                this.headers = headers;
                return this;
            }

            public LogRequestObjectBuilder parameters(Map<String, String> parameters) {
                this.parameters = parameters;
                return this;
            }

            public LogRequestObject build() {
                return new LogRequestObject(httpMethod, httpPath, clientIp, localIp, headers, parameters);
            }
        }
    }

    public static class LogResponseObject {

        private final int responseCode;

        private final String during;

        private final String clientIp;

        private final String localIp;

        private final Map<String, String> headers;

        private final Object body;

        LogResponseObject(int responseCode, String during, String clientIp,
                          String localIp, Map<String, String> headers, Object body) {
            this.responseCode = responseCode;
            this.during = during;
            this.clientIp = clientIp;
            this.localIp = localIp;
            this.headers = headers;
            this.body = body;
        }

        public static LogResponseObjectBuilder builder() {
            return new LogResponseObjectBuilder();
        }

        @SuppressWarnings("unused")
        public int getResponseCode() {
            return responseCode;
        }

        @SuppressWarnings("unused")
        public String getDuring() {
            return during;
        }

        @SuppressWarnings("unused")
        public String getClientIp() {
            return clientIp;
        }

        @SuppressWarnings("unused")
        public String getLocalIp() {
            return localIp;
        }

        @SuppressWarnings("unused")
        public Map<String, String> getHeaders() {
            return headers;
        }

        @SuppressWarnings("unused")
        public Object getBody() {
            return body;
        }

        public static class LogResponseObjectBuilder {

            private int responseCode;

            private String during;

            private String clientIp;

            private String localIp;

            private Map<String, String> headers;

            private Object body;

            private boolean excludeBody;

            LogResponseObjectBuilder() {
            }

            public LogResponseObjectBuilder responseCode(int responseCode) {
                this.responseCode = responseCode;
                return this;
            }

            public LogResponseObjectBuilder during(String during) {
                this.during = during;
                return this;
            }

            public LogResponseObjectBuilder clientIp(String clientIp) {
                this.clientIp = clientIp;
                return this;
            }

            public LogResponseObjectBuilder localIp(String localIp) {
                this.localIp = localIp;
                return this;
            }

            public LogResponseObjectBuilder headers(Map<String, String> headers) {
                this.headers = headers;
                return this;
            }

            public LogResponseObjectBuilder body(Object body) {
                this.body = body;
                return this;
            }

            public LogResponseObjectBuilder excludeBody(boolean excludeBody) {
                this.excludeBody = excludeBody;
                return this;
            }

            public LogResponseObject build() {
                if (this.excludeBody) {
                    this.body = null;
                }
                return new LogResponseObject(responseCode, during, clientIp, localIp, headers, body);
            }
        }
    }
}
