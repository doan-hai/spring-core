package com.codergeezer.core.base.email;

import java.util.List;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
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

    private Email(EmailBuilder emailBuilder) {
        this.from = emailBuilder.getFrom();
        this.to = emailBuilder.getTo();
        this.cc = emailBuilder.getCc();
        this.bcc = emailBuilder.getBcc();
        this.subject = emailBuilder.getSubject();
        this.templateName = emailBuilder.getTemplateName();
        this.content = emailBuilder.getContent();
        this.isHtml = emailBuilder.isHtml();
        this.attachmentFileName = emailBuilder.getAttachmentFileName();
        this.attachmentBytes = emailBuilder.getAttachmentBytes();
        this.attachmentContentType = emailBuilder.getAttachmentContentType();
        this.templateData = emailBuilder.getTemplateData();
        this.isInline = emailBuilder.isInline();
        this.inlineResource = emailBuilder.getInlineResource();
    }

    public static EmailBuilder builder() {
        return new EmailBuilder();
    }

    public String getFrom() {
        return from;
    }

    public List<String> getTo() {
        return to;
    }

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public String getSubject() {
        return subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getContent() {
        return content;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public String getAttachmentFileName() {
        return attachmentFileName;
    }

    public byte[] getAttachmentBytes() {
        return attachmentBytes;
    }

    public String getAttachmentContentType() {
        return attachmentContentType;
    }

    public Map<String, Object> getTemplateData() {
        return templateData;
    }

    public boolean isInline() {
        return isInline;
    }

    public Map<String, String> getInlineResource() {
        return inlineResource;
    }

    public static class EmailBuilder {

        private String from;

        private List<String> to;

        private List<String> cc;

        private List<String> bcc;

        private String subject;

        private String templateName;

        private String content;

        private boolean isHtml;

        private String attachmentFileName;

        private byte[] attachmentBytes;

        private String attachmentContentType;

        private Map<String, Object> templateData;

        private boolean isInline;

        private Map<String, String> inlineResource;

        public EmailBuilder from(String from) {
            this.from = from;
            return this;
        }

        public EmailBuilder to(List<String> to) {
            this.to = to;
            return this;
        }

        public EmailBuilder cc(List<String> cc) {
            this.cc = cc;
            return this;
        }

        public EmailBuilder bcc(List<String> bcc) {
            this.bcc = bcc;
            return this;
        }

        public EmailBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailBuilder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public EmailBuilder content(String content) {
            this.content = content;
            return this;
        }

        public EmailBuilder isHtml(boolean isHtml) {
            this.isHtml = isHtml;
            return this;
        }

        public EmailBuilder attachmentFileName(String attachmentFileName) {
            this.attachmentFileName = attachmentFileName;
            return this;
        }

        public EmailBuilder attachmentBytes(byte[] attachmentBytes) {
            this.attachmentBytes = attachmentBytes;
            return this;
        }

        public EmailBuilder attachmentContentType(String attachmentContentType) {
            this.attachmentContentType = attachmentContentType;
            return this;
        }

        public EmailBuilder templateData(Map<String, Object> templateData) {
            this.templateData = templateData;
            return this;
        }

        public EmailBuilder isInline(boolean isInline) {
            this.isInline = isInline;
            return this;
        }

        public EmailBuilder inlineResource(Map<String, String> inlineResource) {
            this.inlineResource = inlineResource;
            return this;
        }

        private String getFrom() {
            return from;
        }

        private List<String> getTo() {
            return to;
        }

        private List<String> getCc() {
            return cc;
        }

        private List<String> getBcc() {
            return bcc;
        }

        private String getSubject() {
            return subject;
        }

        private String getTemplateName() {
            return templateName;
        }

        private String getContent() {
            return content;
        }

        private boolean isHtml() {
            return isHtml;
        }

        private String getAttachmentFileName() {
            return attachmentFileName;
        }

        private byte[] getAttachmentBytes() {
            return attachmentBytes;
        }

        private String getAttachmentContentType() {
            return attachmentContentType;
        }

        private Map<String, Object> getTemplateData() {
            return templateData;
        }

        private boolean isInline() {
            return isInline;
        }

        private Map<String, String> getInlineResource() {
            return inlineResource;
        }

        public Email build() {
            return new Email(this);
        }
    }
}
