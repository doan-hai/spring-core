package com.codergeezer.core.base.logging;

import com.codergeezer.core.base.data.BodyRequest;
import com.codergeezer.core.base.securiryRequest.AbstractSecurityRequest;
import com.codergeezer.core.base.securiryRequest.SecurityRequestProperties;
import com.codergeezer.core.base.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.codergeezer.core.base.logging.LoggingUtil.logRequest;

/**
 * @author haidv
 * @version 1.0
 */
@ControllerAdvice
public class FilterRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    private final HttpServletRequest httpServletRequest;

    private final LoggingProperties loggingProperties;

    private final SecurityRequestProperties securityRequestProperties;

    private final ApplicationContext applicationContext;

    @Autowired
    public FilterRequestBodyAdviceAdapter(HttpServletRequest httpServletRequest,
                                          LoggingProperties loggingProperties,
                                          SecurityRequestProperties securityRequestProperties,
                                          ApplicationContext applicationContext) {
        this.httpServletRequest = httpServletRequest;
        this.loggingProperties = loggingProperties;
        this.securityRequestProperties = securityRequestProperties;
        this.applicationContext = applicationContext;
    }

    /**
     * Invoked first to determine if this interceptor applies.
     *
     * @param methodParameter the method parameter
     * @param targetType      the target type, not necessarily the same as the method parameter type, e.g. for {@code
     *                        HttpEntity<String>}.
     * @param converterType   the selected converter type
     * @return whether this interceptor should be invoked or not
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * Invoked second before the request body is read and converted.
     *
     * @param inputMessage  the request
     * @param parameter     the target method parameter
     * @param targetType    the target type, not necessarily the same as the method parameter type, e.g. for {@code
     *                      HttpEntity<String>}.
     * @param converterType the converter used to deserialize the body
     * @return the input request or a new instance, never {@code null}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        if (securityRequestProperties.isDecryptRequestBody() && body instanceof BodyRequest) {
            BodyRequest bodyRequest = (BodyRequest) body;
            var json = applicationContext.getAutowireCapableBeanFactory()
                                         .getBean(securityRequestProperties.getSecurityBeanName(),
                                                  AbstractSecurityRequest.class)
                                         .decryptData(bodyRequest.getEncryptData());
            bodyRequest.setRawData(
                    JsonUtils.fromJson(json, (Class) ((ParameterizedType) targetType).getActualTypeArguments()[0]));
            logRequest(httpServletRequest, loggingProperties, bodyRequest);
            return super.afterBodyRead(bodyRequest, inputMessage, parameter, targetType, converterType);
        } else {
            logRequest(httpServletRequest, loggingProperties, body);
            return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
        }
    }
}
