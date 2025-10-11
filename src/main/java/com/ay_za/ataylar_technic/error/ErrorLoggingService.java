package com.ay_za.ataylar_technic.error;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ErrorLoggingService {

    private static final Logger logger = LoggerFactory.getLogger(ErrorLoggingService.class);

    @Autowired
    private ErrorLogRepository errorLogRepository;

    /**
     * Hata logu kaydet
     */
    @Transactional
    public void logError(Exception exception, String errorCode, String severity) {
        try {
            ErrorLog errorLog = new ErrorLog();
            errorLog.setId(UUID.randomUUID().toString());
            errorLog.setErrorCode(errorCode);
            errorLog.setErrorMessage(exception.getMessage());
            errorLog.setStackTrace(getStackTrace(exception));
            errorLog.setExceptionType(exception.getClass().getSimpleName());
            errorLog.setSeverity(severity != null ? severity : "ERROR");
            errorLog.setCreatedDate(LocalDateTime.now());

            // HTTP request bilgilerini al
            populateRequestInfo(errorLog);

            errorLogRepository.save(errorLog);

            logger.error("Error logged with ID: {} - {}", errorLog.getId(), exception.getMessage());
        } catch (Exception e) {
            logger.error("Failed to log error to database", e);
        }
    }

    /**
     * HTTP request bilgilerini doldur
     */
    private void populateRequestInfo(ErrorLog errorLog) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                errorLog.setHttpMethod(request.getMethod());
                errorLog.setRequestUrl(request.getRequestURL().toString());
                errorLog.setUserIp(getClientIpAddress(request));
                errorLog.setUserAgent(request.getHeader("User-Agent"));
                errorLog.setSessionId(request.getSession().getId());

                // Query parameters
                if (request.getQueryString() != null) {
                    errorLog.setRequestParameters(request.getQueryString());
                }

                // User ID'yi session'dan veya header'dan al
                String userId = request.getHeader("X-User-ID");
                if (userId == null) {
                    userId = (String) request.getSession().getAttribute("userId");
                }
                errorLog.setUserId(userId);
            }
        } catch (Exception e) {
            logger.warn("Could not populate request info for error log", e);
        }
    }

    /**
     * Client IP adresini al
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }

    /**
     * Stack trace'i string'e çevir
     */
    private String getStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Tüm error logları getir (sayfalama ile)
     */
    public Page<ErrorLog> getAllErrorLogs(Pageable pageable) {
        return errorLogRepository.findAllByOrderByCreatedDateDesc(pageable);
    }

    /**
     * Çözülmemiş hataları getir
     */
    public Page<ErrorLog> getUnresolvedErrors(Pageable pageable) {
        return errorLogRepository.findByResolvedFalseOrderByCreatedDateDesc(pageable);
    }

    /**
     * Severity'ye göre hataları getir
     */
    public Page<ErrorLog> getErrorsBySeverity(String severity, Boolean resolved, Pageable pageable) {
        return errorLogRepository.findBySeverityAndResolvedOrderByCreatedDateDesc(severity, resolved, pageable);
    }

    /**
     * Son 24 saatteki hataları getir
     */
    public List<ErrorLog> getRecentErrors() {
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        return errorLogRepository.findRecentErrors(since);
    }

    /**
     * Error istatistiklerini getir
     */
    public List<Object[]> getErrorStatsBySeverity() {
        return errorLogRepository.getErrorStatsBySeverity();
    }

    public List<Object[]> getErrorStatsByExceptionType() {
        return errorLogRepository.getErrorStatsByExceptionType();
    }

    /**
     * Error'u çözüldü olarak işaretle
     */
    @Transactional
    public void markAsResolved(String errorId, String resolvedBy, String resolutionNotes) {
        ErrorLog errorLog = errorLogRepository.findById(errorId).orElse(null);
        if (errorLog != null) {
            errorLog.setResolved(true);
            errorLog.setResolvedBy(resolvedBy);
            errorLog.setResolvedDate(LocalDateTime.now());
            errorLog.setResolutionNotes(resolutionNotes);
            errorLogRepository.save(errorLog);
        }
    }

    /**
     * Belirli ID'li error log'u getir
     */
    public ErrorLog getErrorLogById(String id) {
        return errorLogRepository.findById(id).orElse(null);
    }

    /**
     * User ID'ye göre hataları getir
     */
    public List<ErrorLog> getErrorsByUserId(String userId) {
        return errorLogRepository.findByUserId(userId);
    }

    /**
     * Exception tipine göre hataları getir
     */
    public List<ErrorLog> getErrorsByExceptionType(String exceptionType) {
        return errorLogRepository.findByExceptionType(exceptionType);
    }
}
