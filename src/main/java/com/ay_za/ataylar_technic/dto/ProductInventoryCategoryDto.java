package com.ay_za.ataylar_technic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Ürün Envanter Kategorileri DTO
 */
public class ProductInventoryCategoryDto {

    private String id;
    private String categoryName;
    private String categoryDescription;
    private String parentCategoryId;
    private String parentCategoryName; // Ek bilgi için
    private Integer categoryLevel;
    private Integer displayOrder;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedDate;

    private String createdBy;
    private String updatedBy;

    // Hiyerarşik yapı için - alt kategoriler
    private List<ProductInventoryCategoryDto> subCategories;

    // Constructors
    public ProductInventoryCategoryDto() {
    }

    public ProductInventoryCategoryDto(String id, String categoryName,
                                      String categoryDescription, String parentCategoryId,
                                      String parentCategoryName, Integer categoryLevel,
                                      Integer displayOrder, Boolean isActive,
                                      LocalDateTime createdDate, LocalDateTime updatedDate,
                                      String createdBy, String updatedBy) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.parentCategoryId = parentCategoryId;
        this.parentCategoryName = parentCategoryName;
        this.categoryLevel = categoryLevel;
        this.displayOrder = displayOrder;
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

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
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

    public Integer getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(Integer categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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

    public List<ProductInventoryCategoryDto> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<ProductInventoryCategoryDto> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public String toString() {
        return "ProductInventoryCategoryDto{" +
                "id='" + id + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", parentCategoryId='" + parentCategoryId + '\'' +
                ", categoryLevel=" + categoryLevel +
                ", isActive=" + isActive +
                '}';
    }
}

