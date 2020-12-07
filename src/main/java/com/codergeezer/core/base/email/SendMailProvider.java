package com.codergeezer.core.base.email;

import javax.mail.MessagingException;

/**
 * @author haidv
 * @version 1.0
 */
public interface SendMailProvider {

    void sendMail(Email email) throws MessagingException;
}
