package com.codergeezer.core.base.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Component("thymeleaf")
@ConditionalOnClass({JavaMailSender.class, TemplateEngine.class})
public class ThymeleafAbstractSendMailProvider extends AbstractSendMailProvider implements SendMailProvider {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Autowired
    public ThymeleafAbstractSendMailProvider(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMail(Email email) throws MessagingException {
        super.sendMail(email, javaMailSender);
    }

    String buildContent(String thymeleafName, String content, Map<String, Object> data) {
        Context context = new Context();
        if (!CollectionUtils.isEmpty(data)) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return templateEngine.process(thymeleafName, context);
    }
}
