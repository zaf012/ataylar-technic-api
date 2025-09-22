package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.EmailDto;
import com.ay_za.ataylar_technic.service.base.EmailServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
@Tag(name = "Email", description = "Email gönderim API'leri")
public class EmailController {

    private final EmailServiceImpl emailService;

    public EmailController(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    /**
     * Basit email gönder
     */
    @Operation(summary = "Basit email gönder", description = "Düz metin email gönderir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email başarıyla gönderildi"),
            @ApiResponse(responseCode = "400", description = "Geçersiz email bilgileri"),
            @ApiResponse(responseCode = "500", description = "Email gönderim hatası")
    })
    @PostMapping("/send-simple")
    public ResponseEntity<Map<String, Object>> sendSimpleEmail(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String to = request.get("to");
            String subject = request.get("subject");
            String content = request.get("content");

            if (to == null || to.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Alıcı email adresi boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            if (subject == null || subject.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email konusu boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            boolean sent = emailService.sendSimpleEmail(to, subject, content);

            if (sent) {
                response.put("success", true);
                response.put("message", "Email başarıyla gönderildi");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Email gönderilemedi");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Email gönderilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * HTML email gönder
     */
    @Operation(summary = "HTML email gönder", description = "HTML formatlı email gönderir")
    @PostMapping("/send-html")
    public ResponseEntity<Map<String, Object>> sendHtmlEmail(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String to = request.get("to");
            String subject = request.get("subject");
            String htmlContent = request.get("htmlContent");

            if (to == null || to.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Alıcı email adresi boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            boolean sent = emailService.sendHtmlEmail(to, subject, htmlContent);

            if (sent) {
                response.put("success", true);
                response.put("message", "HTML email başarıyla gönderildi");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "HTML email gönderilemedi");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "HTML email gönderilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Gelişmiş email gönder (EmailDto ile)
     */
    @Operation(summary = "Gelişmiş email gönder", description = "EmailDto kullanarak gelişmiş email gönderir")
    @PostMapping("/send-advanced")
    public ResponseEntity<Map<String, Object>> sendAdvancedEmail(@Valid @RequestBody EmailDto emailDto) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean sent = emailService.sendEmail(emailDto);

            if (sent) {
                response.put("success", true);
                response.put("message", "Email başarıyla gönderildi");
                response.put("emailInfo", Map.of(
                    "to", emailDto.getTo(),
                    "subject", emailDto.getSubject(),
                    "isHtml", emailDto.isHtml()
                ));
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Email gönderilemedi");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Email gönderilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Hoş geldin emaili gönder
     */
    @Operation(summary = "Hoş geldin emaili gönder", description = "Yeni kullanıcıya hoş geldin emaili gönderir")
    @PostMapping("/send-welcome")
    public ResponseEntity<Map<String, Object>> sendWelcomeEmail(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String to = request.get("to");
            String userName = request.get("userName");

            if (to == null || to.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Alıcı email adresi boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            if (userName == null || userName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Kullanıcı adı boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            boolean sent = ((com.ay_za.ataylar_technic.service.EmailService) emailService)
                    .sendWelcomeEmail(to, userName);

            if (sent) {
                response.put("success", true);
                response.put("message", "Hoş geldin emaili başarıyla gönderildi");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Hoş geldin emaili gönderilemedi");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Hoş geldin emaili gönderilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Bildirim emaili gönder
     */
    @Operation(summary = "Bildirim emaili gönder", description = "Genel bildirim emaili gönderir")
    @PostMapping("/send-notification")
    public ResponseEntity<Map<String, Object>> sendNotificationEmail(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String to = request.get("to");
            String title = request.get("title");
            String message = request.get("message");

            if (to == null || to.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Alıcı email adresi boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            boolean sent = ((com.ay_za.ataylar_technic.service.EmailService) emailService)
                    .sendNotificationEmail(to, title, message);

            if (sent) {
                response.put("success", true);
                response.put("message", "Bildirim emaili başarıyla gönderildi");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Bildirim emaili gönderilemedi");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Bildirim emaili gönderilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Email servis durumunu kontrol et
     */
    @Operation(summary = "Email servis durumu", description = "Email servisinin aktif olup olmadığını kontrol eder")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getEmailStatus() {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean isEnabled = emailService.isEmailEnabled();
            boolean configOk = emailService.testEmailConfiguration();

            response.put("success", true);
            response.put("emailEnabled", isEnabled);
            response.put("configurationOk", configOk);
            response.put("message", isEnabled ? "Email servisi aktif" : "Email servisi devre dışı");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Email durumu kontrol edilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
