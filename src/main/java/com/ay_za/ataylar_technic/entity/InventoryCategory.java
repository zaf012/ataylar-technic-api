package com.ay_za.ataylar_technic.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_category")
public class InventoryCategory {

    @Id
    @Column(name = "id", length = 200)
    private String id;

    @Column(name = "category_name", nullable = false, length = 200)
    private String categoryName;

    @Column(name = "main_category_id", length = 200)
    private String mainCategoryId;

    @Column(name = "main_category_name", length = 200)
    private String mainCategoryName;

    @JsonProperty("isMainCategory")
    @Column(name = "is_main_category")
    private Boolean isMainCategory;

    @Column(name = "market_code", length = 100)
    private String marketCode;

    @Column(name = "product_name", length = 200)
    private String productName;

    @Column(name = "brand_name", length = 200)
    private String brandName;

    @JsonProperty("isActive")
    @Column(name = "is_active")
    private Boolean isActive;


    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // Constructors
    public InventoryCategory() {
    }

    public InventoryCategory(String id, String categoryName, String mainCategoryId, String mainCategoryName,
                             Boolean isMainCategory, String marketCode, String productName, String brandName, Boolean isActive,
                             LocalDateTime createdDate, LocalDateTime updatedDate, String createdBy,
                             String updatedBy) {
        this.id = id;
        this.categoryName = categoryName;
        this.mainCategoryId = mainCategoryId;
        this.mainCategoryName = mainCategoryName;
        this.isMainCategory = isMainCategory;
        this.marketCode = marketCode;
        this.productName = productName;
        this.brandName = brandName;
        this.isActive = isActive;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(String mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public String getMainCategoryName() {
        return mainCategoryName;
    }

    public void setMainCategoryName(String mainCategoryName) {
        this.mainCategoryName = mainCategoryName;
    }

    public Boolean getIsMainCategory() {
        return isMainCategory;
    }

    public void setIsMainCategory(Boolean isMainCategory) {
        this.isMainCategory = isMainCategory;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
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
}
