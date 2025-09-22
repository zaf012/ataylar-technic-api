package com.ay_za.ataylar_technic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

/**
 * Email gönderimi için DTO sınıfı
 */
public class EmailDto {

    @NotBlank(message = "Alıcı email adresi boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String to;

    private List<String> cc;

    private List<String> bcc;

    @NotBlank(message = "Email konusu boş olamaz")
    @Size(max = 200, message = "Email konusu 200 karakterden fazla olamaz")
    private String subject;

    @NotBlank(message = "Email içeriği boş olamaz")
    private String content;

    private boolean isHtml = false;

    private List<String> attachments;

    private String templateName;

    private Map<String, Object> templateVariables;

    // Constructors
    public EmailDto() {
    }

    public EmailDto(String to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public EmailDto(String to, String subject, String content, boolean isHtml) {
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.isHtml = isHtml;
    }

    // Static factory methods
    public static EmailDto plainText(String to, String subject, String content) {
        return new EmailDto(to, subject, content, false);
    }

    public static EmailDto html(String to, String subject, String content) {
        return new EmailDto(to, subject, content, true);
    }

    public static EmailDto template(String to, String subject, String templateName, Map<String, Object> variables) {
        EmailDto email = new EmailDto();
        email.setTo(to);
        email.setSubject(subject);
        email.setTemplateName(templateName);
        email.setTemplateVariables(variables);
        email.setHtml(true);
        return email;
    }

    // Getters and Setters
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, Object> getTemplateVariables() {
        return templateVariables;
    }

    public void setTemplateVariables(Map<String, Object> templateVariables) {
        this.templateVariables = templateVariables;
    }

    @Override
    public String toString() {
        return "EmailDto{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", isHtml=" + isHtml +
                ", templateName='" + templateName + '\'' +
                '}';
    }
}
