package com.ay_za.ataylar_technic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class SiteDeviceInventoryInfoDto {

    private String id;
    private String siteId;
    private String siteName;
    private String squareId;
    private String squareName;
    private String blockId;
    private String blockName;
    private String doorNo; // Opsiyonel
    private Integer floor; // Opsiyonel
    private String location; // Opsiyonel
    private String systemId;
    private String systemName;
    private String inventoryCategoryId;
    private String inventoryCategoryName; // Hiyerarşik
    private String productName;
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
    public SiteDeviceInventoryInfoDto(String id, String siteId, String siteName, String squareId, String squareName,
                                      String blockId, String blockName, String doorNo, Integer floor, String location,
                                      String systemId, String systemName, String inventoryCategoryId,
                                      String inventoryCategoryName, String productName, String qrCode, Boolean isActive,
                                      LocalDateTime createdDate, String createdBy,
                                      LocalDateTime updatedDate, String updatedBy) {
        this.id = id;
        this.siteId = siteId;
        this.siteName = siteName;
        this.squareId = squareId;
        this.squareName = squareName;
        this.blockId = blockId;
        this.blockName = blockName;
        this.doorNo = doorNo;
        this.floor = floor;
        this.location = location;
        this.systemId = systemId;
        this.systemName = systemName;
        this.inventoryCategoryId = inventoryCategoryId;
        this.inventoryCategoryName = inventoryCategoryName;
        this.productName = productName;
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

    public String getSquareId() {
        return squareId;
    }

    public void setSquareId(String squareId) {
        this.squareId = squareId;
    }

    public String getSquareName() {
        return squareName;
    }

    public void setSquareName(String squareName) {
        this.squareName = squareName;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
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

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getInventoryCategoryId() {
        return inventoryCategoryId;
    }

    public void setInventoryCategoryId(String inventoryCategoryId) {
        this.inventoryCategoryId = inventoryCategoryId;
    }

    public String getInventoryCategoryName() {
        return inventoryCategoryName;
    }

    public void setInventoryCategoryName(String inventoryCategoryName) {
        this.inventoryCategoryName = inventoryCategoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
                ", squareId='" + squareId + '\'' +
                ", squareName='" + squareName + '\'' +
                ", blockId='" + blockId + '\'' +
                ", blockName='" + blockName + '\'' +
                ", doorNo='" + doorNo + '\'' +
                ", floor=" + floor +
                ", location='" + location + '\'' +
                ", systemId='" + systemId + '\'' +
                ", systemName='" + systemName + '\'' +
                ", inventoryCategoryId='" + inventoryCategoryId + '\'' +
                ", inventoryCategoryName='" + inventoryCategoryName + '\'' +
                ", productName='" + productName + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", isActive=" + isActive +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", updatedDate=" + updatedDate +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
