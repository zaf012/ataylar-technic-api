package com.ay_za.ataylar_technic.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "site_device_inventory_info")
public class SiteDeviceInventoryInfo {

    @Id
    @Column(name = "id", length = 200)
    private String id;

    // Site bilgileri
    @Column(name = "site_id", nullable = false, length = 200)
    private String siteId;

    @Column(name = "site_name", nullable = false, length = 200)
    private String siteName;

    @Column(name = "ada", nullable = false, length = 100)
    private String ada;

    @Column(name = "block_name", nullable = false, length = 100)
    private String blockName;

    // Manuel girilen bilgiler
    @Column(name = "apartment_number", length = 100)
    private String apartmentNumber; // Daire numarası (opsiyonel)

    @Column(name = "floor", nullable = false)
    private Integer floor; // Bulunduğu kat

    @Column(name = "location", nullable = false, length = 200)
    private String location; // Bulunduğu lokasyon

    // Envanter bilgileri
    @Column(name = "inventory_category_id", nullable = false, length = 200)
    private String inventoryCategoryId;

    @Column(name = "system_name", nullable = false, length = 200)
    private String systemName; // Envanter (Sistem) Tanımı

    @Column(name = "category_hierarchy", nullable = false, length = 500)
    private String categoryHierarchy; // Envanter Kategorisi (hiyerarşi string)

    @Column(name = "device_specification", length = 200)
    private String deviceSpecification; // Ürün (cihaz) seçiniz

    @Column(name = "qr_code", unique = true, nullable = false, length = 12)
    private String qrCode;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // Default constructor
    public SiteDeviceInventoryInfo() {
    }

    // Constructor for creating new entity
    public SiteDeviceInventoryInfo(String siteId, String siteName, String ada, String blockName,
                                   String apartmentNumber, Integer floor, String location,
                                   String inventoryCategoryId, String systemName, String categoryHierarchy,
                                   String deviceSpecification, String qrCode, String createdBy) {
        this.id = UUID.randomUUID().toString();
        this.siteId = siteId;
        this.siteName = siteName;
        this.ada = ada;
        this.blockName = blockName;
        this.apartmentNumber = apartmentNumber;
        this.floor = floor;
        this.location = location;
        this.inventoryCategoryId = inventoryCategoryId;
        this.systemName = systemName;
        this.categoryHierarchy = categoryHierarchy;
        this.deviceSpecification = deviceSpecification;
        this.qrCode = qrCode;
        this.isActive = true;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAda() {
        return ada;
    }

    public void setAda(String ada) {
        this.ada = ada;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInventoryCategoryId() {
        return inventoryCategoryId;
    }

    public void setInventoryCategoryId(String inventoryCategoryId) {
        this.inventoryCategoryId = inventoryCategoryId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getCategoryHierarchy() {
        return categoryHierarchy;
    }

    public void setCategoryHierarchy(String categoryHierarchy) {
        this.categoryHierarchy = categoryHierarchy;
    }

    public String getDeviceSpecification() {
        return deviceSpecification;
    }

    public void setDeviceSpecification(String deviceSpecification) {
        this.deviceSpecification = deviceSpecification;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "SiteDeviceInventoryInfo{" +
                "id='" + id + '\'' +
                ", siteId='" + siteId + '\'' +
                ", siteName='" + siteName + '\'' +
                ", ada='" + ada + '\'' +
                ", blockName='" + blockName + '\'' +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                ", floor=" + floor +
                ", location='" + location + '\'' +
                ", inventoryCategoryId='" + inventoryCategoryId + '\'' +
                ", systemName='" + systemName + '\'' +
                ", categoryHierarchy='" + categoryHierarchy + '\'' +
                ", deviceSpecification='" + deviceSpecification + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", isActive=" + isActive +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", updatedDate=" + updatedDate +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
