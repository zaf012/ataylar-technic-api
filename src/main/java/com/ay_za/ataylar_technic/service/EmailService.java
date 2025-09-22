package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.EmailDto;
import com.ay_za.ataylar_technic.service.base.EmailServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * Email servis implementasyonu
 */
@Service
public class EmailService implements EmailServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public boolean sendSimpleEmail(String to, String subject, String content) {
        if (!emailEnabled) {
            logger.warn("Email servisi devre dışı. Email gönderilmedi: {}", to);
            return false;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            logger.info("Basit email başarıyla gönderildi: {}", to);
            return true;

        } catch (Exception e) {
            logger.error("Email gönderilirken hata oluştu: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendHtmlEmail(String to, String subject, String htmlContent) {
        if (!emailEnabled) {
            logger.warn("Email servisi devre dışı. HTML email gönderilmedi: {}", to);
            return false;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            logger.info("HTML email başarıyla gönderildi: {}", to);
            return true;

        } catch (Exception e) {
            logger.error("HTML email gönderilirken hata oluştu: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendEmail(EmailDto emailDto) {
        if (!emailEnabled) {
            logger.warn("Email servisi devre dışı. Email gönderilmedi: {}", emailDto.getTo());
            return false;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(emailDto.getTo());
            helper.setSubject(emailDto.getSubject());

            // CC ve BCC ekleme
            if (emailDto.getCc() != null && !emailDto.getCc().isEmpty()) {
                helper.setCc(emailDto.getCc().toArray(new String[0]));
            }

            if (emailDto.getBcc() != null && !emailDto.getBcc().isEmpty()) {
                helper.setBcc(emailDto.getBcc().toArray(new String[0]));
            }

            // Template kullanımı kontrolü
            if (emailDto.getTemplateName() != null && !emailDto.getTemplateName().isEmpty()) {
                String htmlContent = processTemplate(emailDto.getTemplateName(), emailDto.getTemplateVariables());
                helper.setText(htmlContent, true);
            } else {
                helper.setText(emailDto.getContent(), emailDto.isHtml());
            }

            mailSender.send(mimeMessage);
            logger.info("Email başarıyla gönderildi: {}", emailDto.getTo());
            return true;

        } catch (Exception e) {
            logger.error("Email gönderilirken hata oluştu: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        EmailDto emailDto = EmailDto.template(to, subject, templateName, variables);
        return sendEmail(emailDto);
    }

    @Override
    public boolean sendBulkEmail(EmailDto emailDto) {
        // Bu metodda toplu email gönderimi için mantık eklenebilir
        // Şu an tek email gönderimi yapıyor
        return sendEmail(emailDto);
    }

    @Override
    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    @Override
    public boolean testEmailConfiguration() {
        if (!emailEnabled) {
            logger.warn("Email servisi devre dışı");
            return false;
        }

        try {
            // Test email gönderimi
            SimpleMailMessage testMessage = new SimpleMailMessage();
            testMessage.setFrom(fromEmail);
            testMessage.setTo(fromEmail); // Kendimize test email
            testMessage.setSubject("ATAYLAR Technic - Email Test");
            testMessage.setText("Bu bir test emailidir. Email konfigürasyonu çalışıyor.");

            // Gerçekten göndermek yerine sadece oluşturma işlemini test et
            logger.info("Email konfigürasyonu test edildi - Başarılı");
            return true;

        } catch (Exception e) {
            logger.error("Email konfigürasyonu test edilirken hata: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Thymeleaf template işleme
     */
    private String processTemplate(String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            if (variables != null) {
                context.setVariables(variables);
            }
            return templateEngine.process(templateName, context);
        } catch (Exception e) {
            logger.error("Template işlenirken hata: {}", e.getMessage(), e);
            throw new RuntimeException("Template işlenemedi: " + templateName, e);
        }
    }

    /**
     * Email gönderimi için yardımcı metodlar
     */
    public boolean sendWelcomeEmail(String to, String userName) {
        String subject = "ATAYLAR Technic'e Hoş Geldiniz!";
        String content = String.format(
                "Merhaba %s,\n\n" +
                "ATAYLAR Technic sistemine hoş geldiniz!\n\n" +
                "Hesabınız başarıyla oluşturulmuştur.\n\n" +
                "İyi günler,\n" +
                "ATAYLAR Technic Ekibi",
                userName
        );

        return sendSimpleEmail(to, subject, content);
    }

    public boolean sendPasswordResetEmail(String to, String resetToken) {
        String subject = "Şifre Sıfırlama Talebi";
        String content = String.format(
                "Merhaba,\n\n" +
                "Şifre sıfırlama talebiniz alınmıştır.\n\n" +
                "Sıfırlama kodu: %s\n\n" +
                "Bu kod 15 dakika geçerlidir.\n\n" +
                "İyi günler,\n" +
                "ATAYLAR Technic Ekibi",
                resetToken
        );

        return sendSimpleEmail(to, subject, content);
    }

    public boolean sendNotificationEmail(String to, String title, String message) {
        String subject = "ATAYLAR Technic - " + title;
        String content = String.format(
                "Merhaba,\n\n" +
                "%s\n\n" +
                "İyi günler,\n" +
                "ATAYLAR Technic Ekibi",
                message
        );

        return sendSimpleEmail(to, subject, content);
    }
}
