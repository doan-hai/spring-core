package com.codergeezer.core.email;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Component("stringContent")
@ConditionalOnClass(JavaMailSender.class)
public class StringContentAbstractSendMailProvider extends AbstractSendMailProvider implements SendMailProvider {

    private final JavaMailSender javaMailSender;

    @Autowired
    public StringContentAbstractSendMailProvider(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(Email email) throws MessagingException {
        super.sendMail(email, javaMailSender);
    }

    @Override
    String buildContent(String thymeleafName, String content, Map<String, Object> data) {
        return StringSubstitutor.replace(content, data);
    }
}
