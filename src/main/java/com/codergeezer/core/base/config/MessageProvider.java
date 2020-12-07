package com.codergeezer.core.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author haidv
 * @version 1.0
 */
@Component
public class MessageProvider {

    private final MessageSource messageSource;

    @Autowired
    public MessageProvider(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get message by message code
     *
     * @param code message code
     * @return message
     */
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * Get message by message code
     *
     * @param code    message code
     * @param objects an array of arguments
     * @return message
     */
    public String getMessage(String code, Object[] objects) {
        return messageSource.getMessage(code, objects, LocaleContextHolder.getLocale());
    }
}
