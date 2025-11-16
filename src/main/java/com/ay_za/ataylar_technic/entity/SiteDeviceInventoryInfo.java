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

    @Column(name = "square_id", nullable = false, length = 200)
    private String squareId;

    @Column(name = "square_name", nullable = false, length = 200)
    private String squareName;

    @Column(name = "block_id", nullable = false, length = 200)
    private String blockId;

    @Column(name = "block_name", nullable = false, length = 200)
    private String blockName;

    // Manuel girilen bilgiler (opsiyonel)
    @Column(name = "door_no", length = 100)
    private String doorNo; // Daire numarası (opsiyonel)

    @Column(name = "floor")
    private Integer floor; // Bulunduğu kat (opsiyonel)

    @Column(name = "location", length = 200)
    private String location; // Bulunduğu lokasyon (opsiyonel)

    // Envanter bilgileri
    @Column(name = "system_id", nullable = false, length = 200)
    private String systemId;

    @Column(name = "system_name", nullable = false, length = 200)
    private String systemName; // Envanter (Sistem) Tanımı

    @Column(name = "inventory_category_id", nullable = false, length = 200)
    private String inventoryCategoryId; // Seçili olan productName'in ID'si

    @Column(name = "inventory_category_name", nullable = false, length = 500)
    private String inventoryCategoryName; // Hiyerarşik kategori adı (örn: Eşanjör Sistemleri > Eşanjör Denge Kabları > Wates)

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName; // En alt kategori adı (örn: Wates, 2500/16)

    @Column(name = "qr_code", unique = true, nullable = false, length = 12)
    private String qrCode; // 10 basamaklı QR kod

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
    public SiteDeviceInventoryInfo(String siteId, String siteName, String squareId, String squareName,
                                   String blockId, String blockName, String doorNo, Integer floor, String location,
                                   String systemId, String systemName, String inventoryCategoryId,
                                   String inventoryCategoryName, String productName, String qrCode, String createdBy) {
        this.id = UUID.randomUUID().toString();
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
}
