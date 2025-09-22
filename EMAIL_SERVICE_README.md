# ATAYLAR Technic Email Servisi

Bu dokümantasyon, ATAYLAR Technic projesi için oluşturulan email servisinin kullanımını açıklar.

## Özellikler

- ✅ Basit metin email gönderimi
- ✅ HTML email gönderimi
- ✅ Template tabanlı email gönderimi
- ✅ CC/BCC desteği
- ✅ Dosya eki desteği (hazır)
- ✅ Toplu email gönderimi
- ✅ Email durumu kontrolü
- ✅ Otomatik email şablonları (Hoş geldin, Şifre sıfırlama, Bildirim)

## Konfigürasyon

### Environment Variables

Email servisini kullanmak için aşağıdaki environment variable'ları ayarlayın:

```bash
# Email SMTP Ayarları
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
EMAIL_FROM=noreply@ataylar.com
EMAIL_FROM_NAME=ATAYLAR Technic
EMAIL_ENABLED=true
```

### Gmail Kullanımı

Gmail kullanıyorsanız:
1. 2-Factor Authentication aktif olmalı
2. App Password oluşturun
3. App Password'ü `EMAIL_PASSWORD` olarak kullanın

## API Endpoints

### 1. Basit Email Gönder

```http
POST /api/email/send-simple
Content-Type: application/json

{
  "to": "user@example.com",
  "subject": "Test Email",
  "content": "Bu bir test emailidir."
}
```

### 2. HTML Email Gönder

```http
POST /api/email/send-html
Content-Type: application/json

{
  "to": "user@example.com",
  "subject": "HTML Email",
  "htmlContent": "<h1>Merhaba</h1><p>Bu bir <b>HTML</b> emailidir.</p>"
}
```

### 3. Gelişmiş Email Gönder

```http
POST /api/email/send-advanced
Content-Type: application/json

{
  "to": "user@example.com",
  "cc": ["cc@example.com"],
  "bcc": ["bcc@example.com"],
  "subject": "Gelişmiş Email",
  "content": "Email içeriği",
  "isHtml": true,
  "templateName": "email-template",
  "templateVariables": {
    "title": "Hoş Geldiniz",
    "message": "Sisteme başarıyla kayıt oldunuz.",
    "buttonText": "Giriş Yap",
    "buttonUrl": "https://ataylar.com/login"
  }
}
```

### 4. Hoş Geldin Emaili

```http
POST /api/email/send-welcome
Content-Type: application/json

{
  "to": "newuser@example.com",
  "userName": "Ahmet Yılmaz"
}
```

### 5. Bildirim Emaili

```http
POST /api/email/send-notification
Content-Type: application/json

{
  "to": "user@example.com",
  "title": "Sistem Bildirimi",
  "message": "Hesabınızda yeni bir aktivite tespit edildi."
}
```

### 6. Email Servis Durumu

```http
GET /api/email/status
```

## Kod İçinde Kullanım

### Servis Injection

```java
@Service
public class UserService {
    
    private final EmailServiceImpl emailService;
    
    public UserService(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }
    
    public void registerUser(User user) {
        // Kullanıcı kaydı...
        
        // Hoş geldin emaili gönder
        emailService.sendWelcomeEmail(user.getEmail(), user.getName());
    }
}
```

### Basit Email Gönderimi

```java
// Basit metin email
emailService.sendSimpleEmail(
    "user@example.com", 
    "Test Subject", 
    "Email içeriği"
);

// HTML email
emailService.sendHtmlEmail(
    "user@example.com", 
    "HTML Subject", 
    "<h1>HTML İçerik</h1>"
);
```

### Template ile Email

```java
Map<String, Object> variables = new HashMap<>();
variables.put("title", "Hesap Doğrulama");
variables.put("message", "Hesabınızı doğrulamak için aşağıdaki linke tıklayın.");
variables.put("buttonText", "Hesabı Doğrula");
variables.put("buttonUrl", "https://ataylar.com/verify?token=123");

emailService.sendTemplateEmail(
    "user@example.com",
    "Hesap Doğrulama",
    "email-template",
    variables
);
```

### EmailDto ile Gelişmiş Kullanım

```java
EmailDto emailDto = new EmailDto();
emailDto.setTo("user@example.com");
emailDto.setCc(Arrays.asList("manager@ataylar.com"));
emailDto.setSubject("Proje Durumu");
emailDto.setContent("<h2>Proje Tamamlandı</h2><p>Proje başarıyla tamamlanmıştır.</p>");
emailDto.setHtml(true);

emailService.sendEmail(emailDto);
```

## Template Oluşturma

Email template'leri `src/main/resources/templates/` klasöründe `.html` uzantılı olarak oluşturun.

### Örnek Template (welcome-email.html)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Hoş Geldiniz</title>
</head>
<body>
    <h1>Hoş Geldiniz <span th:text="${userName}">Kullanıcı</span>!</h1>
    <p th:text="${message}">Mesaj içeriği</p>
    <a th:href="${buttonUrl}" th:text="${buttonText}">Button</a>
</body>
</html>
```

## Güvenlik

- Email gönderiminde hassas bilgileri loglamayın
- SMTP bilgilerini environment variable'larda tutun
- Email rate limiting uygulamayı düşünün
- Spam önleme mekanizmaları ekleyin

## Test Etme

Email servisini test etmek için:

```http
GET /api/email/status
```

Bu endpoint email konfigürasyonunun doğru olup olmadığını kontrol eder.

## Sorun Giderme

### Email Gönderilmiyor
1. `EMAIL_ENABLED=true` olduğundan emin olun
2. SMTP bilgilerinin doğru olduğunu kontrol edin
3. Gmail için App Password kullandığınızdan emin olun
4. Firewall/Network ayarlarını kontrol edin

### Template Bulunamıyor
1. Template dosyasının `src/main/resources/templates/` klasöründe olduğunu kontrol edin
2. Template adının doğru yazıldığından emin olun
3. Thymeleaf dependency'sinin ekli olduğunu kontrol edin
