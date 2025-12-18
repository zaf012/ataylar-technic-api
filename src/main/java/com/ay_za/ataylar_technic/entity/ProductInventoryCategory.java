package com.ay_za.ataylar_technic.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Ürün Envanter Kategorileri Entity
 * Hiyerarşik kategori yapısı için parentCategoryId kullanılır
 */
@Entity
@Table(name = "product_inventory_category")
public class ProductInventoryCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "category_name", nullable = false, length = 200)
    private String categoryName;

    @Column(name = "category_description", length = 500)
    private String categoryDescription;

    /**
     * Üst kategori ID'si - null ise bu kategori en üst seviyedir
     */
    @Column(name = "parent_category_id", length = 36)
    private String parentCategoryId;

    /**
     * Kategori seviyesi (1: Ana Kategori, 2: Alt Kategori, 3: Alt-Alt Kategori, vb.)
     */
    @Column(name = "category_level")
    private Integer categoryLevel;

    /**
     * Sıralama için
     */
    @Column(name = "display_order")
    private Integer displayOrder;

    @JsonProperty("isActive")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

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
    public ProductInventoryCategory() {
    }

    public ProductInventoryCategory(String id, String categoryName,
                                   String categoryDescription, String parentCategoryId,
                                   Integer categoryLevel, Integer displayOrder, Boolean isActive,
                                   LocalDateTime createdDate, LocalDateTime updatedDate,
                                   String createdBy, String updatedBy) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.parentCategoryId = parentCategoryId;
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

    @Override
    public String toString() {
        return "ProductInventoryCategory{" +
                "id='" + id + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", parentCategoryId='" + parentCategoryId + '\'' +
                ", categoryLevel=" + categoryLevel +
                ", isActive=" + isActive +
                '}';
    }
}

