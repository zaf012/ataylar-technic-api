package com.ay_za.ataylar_technic.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_pdf_records")
public class MaintenancePdfRecord {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "customer_firm_name", length = 255)
    private String customerFirmName;

    @Column(name = "customer_address", length = 500)
    private String customerAddress;

    @Column(name = "authorized_personnel", length = 255)
    private String authorizedPersonnel;

    @Column(name = "tel_no", length = 50)
    private String telNo;

    @Column(name = "gsm_no", length = 50)
    private String gsmNo;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "system_name", length = 255)
    private String systemName;

    @Column(name = "product_serial_no", length = 255)
    private String productSerialNo;

    @Column(name = "service_date")
    private LocalDate serviceDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "file_name", nullable = false, length = 500)
    private String fileName;

    @Column(name = "file_path", nullable = false, length = 1000)
    private String filePath;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "report_no", nullable = false)
    private Integer reportNo;

    // Constructors
    public MaintenancePdfRecord() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerFirmName() {
        return customerFirmName;
    }

    public void setCustomerFirmName(String customerFirmName) {
        this.customerFirmName = customerFirmName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getAuthorizedPersonnel() {
        return authorizedPersonnel;
    }

    public void setAuthorizedPersonnel(String authorizedPersonnel) {
        this.authorizedPersonnel = authorizedPersonnel;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getGsmNo() {
        return gsmNo;
    }

    public void setGsmNo(String gsmNo) {
        this.gsmNo = gsmNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getProductSerialNo() {
        return productSerialNo;
    }

    public void setProductSerialNo(String productSerialNo) {
        this.productSerialNo = productSerialNo;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getReportNo() {
        return reportNo;
    }

    public void setReportNo(Integer reportNo) {
        this.reportNo = reportNo;
    }
}

