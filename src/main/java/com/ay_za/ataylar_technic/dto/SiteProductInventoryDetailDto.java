package com.ay_za.ataylar_technic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Site Ürün Envanter Detayı DTO
 */
public class SiteProductInventoryDetailDto {

    private String id;
    private String siteId;
    private String siteName;
    private String squareId;
    private String squareName;
    private String blockId;
    private String blockName;
    private Integer doorNumber;
    private Integer floorNumber;
    private String location;
    private String systemId;
    private String systemName;
    private String categoryId;
    private String categoryName;
    private String productInventoryDetailId;
    private String productName;
    private String qrCode;

    @JsonProperty("active")
    private Boolean active;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;

    private String createdBy;
    private String updatedBy;

    // Constructors
    public SiteProductInventoryDetailDto() {
    }

    public SiteProductInventoryDetailDto(String id, String siteId, String siteName, String squareId, String squareName,
                                        String blockId, String blockName, Integer doorNumber, Integer floorNumber,
                                        String location, String systemId, String systemName, String categoryId,
                                        String categoryName, String productInventoryDetailId, String productName,
                                        String qrCode, Boolean active, LocalDateTime createdDate, LocalDateTime updatedDate,
                                        String createdBy, String updatedBy) {
        this.id = id;
        this.siteId = siteId;
        this.siteName = siteName;
        this.squareId = squareId;
        this.squareName = squareName;
        this.blockId = blockId;
        this.blockName = blockName;
        this.doorNumber = doorNumber;
        this.floorNumber = floorNumber;
        this.location = location;
        this.systemId = systemId;
        this.systemName = systemName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productInventoryDetailId = productInventoryDetailId;
        this.productName = productName;
        this.qrCode = qrCode;
        this.active = active;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createdBy = createdBy;
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

    public Integer getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(Integer doorNumber) {
        this.doorNumber = doorNumber;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductInventoryDetailId() {
        return productInventoryDetailId;
    }

    public void setProductInventoryDetailId(String productInventoryDetailId) {
        this.productInventoryDetailId = productInventoryDetailId;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "SiteProductInventoryDetailDto{" +
                "id='" + id + '\'' +
                ", siteId='" + siteId + '\'' +
                ", siteName='" + siteName + '\'' +
                ", squareId='" + squareId + '\'' +
                ", squareName='" + squareName + '\'' +
                ", blockId='" + blockId + '\'' +
                ", blockName='" + blockName + '\'' +
                ", doorNumber=" + doorNumber +
                ", floorNumber=" + floorNumber +
                ", location='" + location + '\'' +
                ", systemId='" + systemId + '\'' +
                ", systemName='" + systemName + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", productInventoryDetailId='" + productInventoryDetailId + '\'' +
                ", productName='" + productName + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", active=" + active +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}

