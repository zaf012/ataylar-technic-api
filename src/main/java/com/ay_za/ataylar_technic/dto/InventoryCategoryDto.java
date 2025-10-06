package com.ay_za.ataylar_technic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryCategoryDto {

    private String id;
    private String categoryName;
    private String categoryCode;
    private String qrCode;
    private String description;
    private Integer level;
    private Integer sortOrder;
    private Boolean isActive;
    private String parentCategoryId;
    private String parentCategoryName;
    private List<InventoryCategoryDto> subCategories;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime updatedDate;

    private String createdBy;
    private String updatedBy;

    // Constructors
    public InventoryCategoryDto() {
    }

    public InventoryCategoryDto(String id, String categoryName, String categoryCode, String qrCode,
                              String description, Integer level, Integer sortOrder, Boolean isActive,
                              String parentCategoryId, String parentCategoryName,
                              List<InventoryCategoryDto> subCategories, LocalDateTime createdDate,
                              LocalDateTime updatedDate, String createdBy, String updatedBy) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.qrCode = qrCode;
        this.description = description;
        this.level = level;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.parentCategoryId = parentCategoryId;
        this.parentCategoryName = parentCategoryName;
        this.subCategories = subCategories;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public List<InventoryCategoryDto> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<InventoryCategoryDto> subCategories) {
        this.subCategories = subCategories;
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
