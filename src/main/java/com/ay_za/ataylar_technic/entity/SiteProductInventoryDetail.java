package com.ay_za.ataylar_technic.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Site Ürün Envanter Detayı Entity
 * Site lokasyonlarına bağlı ürün detay bilgilerini tutar
 */
@Entity
@Table(name = "site_product_inventory_detail",
       uniqueConstraints = @UniqueConstraint(columnNames = "qr_code"))
public class SiteProductInventoryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "site_id", nullable = false, length = 200)
    private String siteId;

    @Column(name = "site_name", nullable = false, length = 100)
    private String siteName;

    @Column(name = "square_id", nullable = false, length = 200)
    private String squareId;

    @Column(name = "square_name", nullable = false, length = 100)
    private String squareName;

    @Column(name = "block_id", nullable = false, length = 200)
    private String blockId;

    @Column(name = "block_name", nullable = false, length = 100)
    private String blockName;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "location", length = 500)
    private String location;

    @Column(name = "system_id", nullable = false, length = 200)
    private String systemId;

    @Column(name = "system_name", nullable = false, length = 100)
    private String systemName;

    @Column(name = "category_id", nullable = false, length = 36)
    private String categoryId;

    @Column(name = "category_name", nullable = false, length = 200)
    private String categoryName;

    @Column(name = "product_inventory_detail_id", nullable = false, length = 36)
    private String productInventoryDetailId;

    @Column(name = "product_name", length = 500)
    private String productName;

    @Column(name = "brand_name", length = 500)
    private String brandName;

    @Column(name = "qr_code", nullable = false, unique = true, length = 12)
    private String qrCode;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // Constructors
    public SiteProductInventoryDetail() {
    }

    public SiteProductInventoryDetail(String id, String siteId, String siteName, String squareId, String squareName,
                                     String blockId, String blockName, Integer floorNumber,
                                     String location, String systemId, String systemName, String categoryId,
                                     String categoryName, String productInventoryDetailId, String productName, String brandName,
                                     String qrCode, Boolean active, LocalDateTime createdDate, LocalDateTime updatedDate,
                                     String createdBy, String updatedBy) {
        this.id = id;
        this.siteId = siteId;
        this.siteName = siteName;
        this.squareId = squareId;
        this.squareName = squareName;
        this.blockId = blockId;
        this.blockName = blockName;
        this.floorNumber = floorNumber;
        this.location = location;
        this.systemId = systemId;
        this.systemName = systemName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productInventoryDetailId = productInventoryDetailId;
        this.productName = productName;
        this.brandName = brandName;
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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
        return "SiteProductInventoryDetail{" +
                "id='" + id + '\'' +
                ", siteId='" + siteId + '\'' +
                ", siteName='" + siteName + '\'' +
                ", squareId='" + squareId + '\'' +
                ", squareName='" + squareName + '\'' +
                ", blockId='" + blockId + '\'' +
                ", blockName='" + blockName + '\'' +
                ", floorNumber=" + floorNumber +
                ", location='" + location + '\'' +
                ", systemId='" + systemId + '\'' +
                ", systemName='" + systemName + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", productInventoryDetailId='" + productInventoryDetailId + '\'' +
                ", productName='" + productName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", active=" + active +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}

