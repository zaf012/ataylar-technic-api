package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.EmailDto;

import java.util.Map;

/**
 * Email servis interface'i
 */
public interface EmailServiceImpl {

    /**
     * Basit email gönder
     */
    boolean sendSimpleEmail(String to, String subject, String content);

    /**
     * HTML email gönder
     */
    boolean sendHtmlEmail(String to, String subject, String htmlContent);

    /**
     * EmailDto ile email gönder
     */
    boolean sendEmail(EmailDto emailDto);

    /**
     * Template ile email gönder
     */
    boolean sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables);

    /**
     * Toplu email gönder
     */
    boolean sendBulkEmail(EmailDto emailDto);

    /**
     * Email servisi aktif mi kontrol et
     */
    boolean isEmailEnabled();

    /**
     * Email konfigürasyonunu test et
     */
    boolean testEmailConfiguration();
}
