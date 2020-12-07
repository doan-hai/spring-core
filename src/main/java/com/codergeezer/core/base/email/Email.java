package com.codergeezer.core.base.email;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Builder @Getter
public class Email {

    private final String from;

    private final List<String> to;

    private final List<String> cc;

    private final List<String> bcc;

    private final String subject;

    private final String templateName;

    private final String content;

    private final boolean isHtml;

    private final String attachmentFileName;

    private final byte[] attachmentBytes;

    private final String attachmentContentType;

    private final Map<String, Object> templateData;

    private final boolean isInline;

    private final Map<String, String> inlineResource;
}
