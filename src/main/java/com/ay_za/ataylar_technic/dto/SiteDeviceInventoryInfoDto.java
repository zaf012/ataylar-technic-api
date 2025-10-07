package com.ay_za.ataylar_technic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class SiteDeviceInventoryInfoDto {

    private String id;
    private String siteId;
    private String siteName;
    private String ada;
    private String blockName;
    private String apartmentNumber;
    private Integer floor;
    private String location;
    private String inventoryCategoryId;
    private String systemName;
    private String categoryHierarchy;
    private String deviceSpecification;
    private String qrCode;
    private Boolean isActive;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime createdDate;

    private String createdBy;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime updatedDate;

    private String updatedBy;

    // Default constructor
    public SiteDeviceInventoryInfoDto() {
    }

    // Constructor
    public SiteDeviceInventoryInfoDto(String id, String siteId, String siteName, String ada, String blockName,
                                      String apartmentNumber, Integer floor, String location,
                                      String inventoryCategoryId, String systemName, String categoryHierarchy,
                                      String deviceSpecification, String qrCode, Boolean isActive,
                                      LocalDateTime createdDate, String createdBy,
                                      LocalDateTime updatedDate, String updatedBy) {
        this.id = id;
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
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.updatedDate = updatedDate;
        this.updatedBy = updatedBy;
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
        return "SiteDeviceInventoryInfoDto{" +
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
