package com.ay_za.ataylar_technic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public class InventoryCategoryDto {

    private String id;
    private String categoryName;
    private String mainCategoryId;
    private Boolean isMainCategory;
    private String marketCode;
    private String productName;
    private String categoryCode;
    private String qrCode;
    private String description;
    private Integer sortOrder;
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;

    private String createdBy;
    private String updatedBy;

    // Alt kategorileri getirmek için (opsiyonel)
    private List<InventoryCategoryDto> subCategories;

    // Ana kategori bilgisi (opsiyonel)
    private String mainCategoryName;

    // Constructors
    public InventoryCategoryDto() {
    }

    public InventoryCategoryDto(String id, String categoryName, String mainCategoryId, Boolean isMainCategory,
                              String marketCode, String productName, String categoryCode, String qrCode,
                              String description, Integer sortOrder, Boolean isActive,
                              LocalDateTime createdDate, LocalDateTime updatedDate,
                              String createdBy, String updatedBy) {
        this.id = id;
        this.categoryName = categoryName;
        this.mainCategoryId = mainCategoryId;
        this.isMainCategory = isMainCategory;
        this.marketCode = marketCode;
        this.productName = productName;
        this.categoryCode = categoryCode;
        this.qrCode = qrCode;
        this.description = description;
        this.sortOrder = sortOrder;
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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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

    public List<InventoryCategoryDto> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<InventoryCategoryDto> subCategories) {
        this.subCategories = subCategories;
    }

    public String getMainCategoryName() {
        return mainCategoryName;
    }

    public void setMainCategoryName(String mainCategoryName) {
        this.mainCategoryName = mainCategoryName;
    }
}

