package com.ay_za.ataylar_technic.error;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "error_logs")
public class ErrorLog {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "error_code", nullable = false, length = 50)
    private String errorCode;

    @Column(name = "error_message", nullable = false, columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "exception_type", nullable = false, length = 255)
    private String exceptionType;

    @Column(name = "http_method", length = 10)
    private String httpMethod;

    @Column(name = "request_url", length = 500)
    private String requestUrl;

    @Column(name = "request_parameters", columnDefinition = "TEXT")
    private String requestParameters;

    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody;

    @Column(name = "user_id", length = 36)
    private String userId;

    @Column(name = "user_ip", length = 45)
    private String userIp;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "session_id", length = 255)
    private String sessionId;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "severity", nullable = false, length = 20)
    private String severity = "ERROR";

    @Column(name = "resolved")
    private Boolean resolved = false;

    @Column(name = "resolved_by", length = 50)
    private String resolvedBy;

    @Column(name = "resolved_date")
    private LocalDateTime resolvedDate;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    // Constructors
    public ErrorLog() {
    }

    public ErrorLog(String id, String errorCode, String errorMessage, String stackTrace, String exceptionType,
                    String httpMethod, String requestUrl, String requestParameters, String requestBody, String userId,
                    String userIp, String userAgent, String sessionId, LocalDateTime createdDate, String severity,
                    Boolean resolved, String resolvedBy, LocalDateTime resolvedDate, String resolutionNotes) {
        this.id = id;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
        this.exceptionType = exceptionType;
        this.httpMethod = httpMethod;
        this.requestUrl = requestUrl;
        this.requestParameters = requestParameters;
        this.requestBody = requestBody;
        this.userId = userId;
        this.userIp = userIp;
        this.userAgent = userAgent;
        this.sessionId = sessionId;
        this.createdDate = createdDate;
        this.severity = severity;
        this.resolved = resolved;
        this.resolvedBy = resolvedBy;
        this.resolvedDate = resolvedDate;
        this.resolutionNotes = resolutionNotes;
    }

    public ErrorLog(String id) {
        this.id = id;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public LocalDateTime getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(LocalDateTime resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }
}
