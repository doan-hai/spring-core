package com.codergeezer.core.email;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
public abstract class AbstractSendMailProvider {

    public void sendMail(Email email, JavaMailSender javaMailSender) throws MessagingException {
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
        message.setSubject(email.getSubject());
        message.setFrom(email.getFrom());
        message.setTo(email.getTo().toArray(new String[0]));
        if (!CollectionUtils.isEmpty(email.getCc())) {
            message.setCc(email.getCc().toArray(new String[0]));
        }
        if (!CollectionUtils.isEmpty(email.getBcc())) {
            message.setCc(email.getBcc().toArray(new String[0]));
        }
        message.setText(buildContent(email.getTemplateName(), email.getContent(), email.getTemplateData()),
                        email.isHtml());
        if (!StringUtils.isEmpty(email.getAttachmentFileName()) &&
            !StringUtils.isEmpty(email.getAttachmentContentType())) {
            // add the attachment
            InputStreamSource attachmentSource = new ByteArrayResource(email.getAttachmentBytes());
            message.addAttachment(email.getAttachmentFileName(), attachmentSource,
                                  email.getAttachmentContentType());
        }
        if (email.isHtml() && email.isInline() && !CollectionUtils.isEmpty(email.getInlineResource())) {
            for (Map.Entry<String, String> entry : email.getInlineResource().entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                FileSystemResource res = new FileSystemResource(new File(v));
                message.addInline(k, res);
            }
        }
        // send mail
        javaMailSender.send(mimeMessage);
    }

    abstract String buildContent(String thymeleafName, String content, Map<String, Object> data);
}
