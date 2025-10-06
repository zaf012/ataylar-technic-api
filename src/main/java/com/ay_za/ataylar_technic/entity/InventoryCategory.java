package com.ay_za.ataylar_technic.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inventory_category")
public class InventoryCategory {

    @Id
    @Column(name = "id", length = 200)
    private String id;

    @Column(name = "category_name", nullable = false, length = 200)
    private String categoryName;

    @Column(name = "category_code", unique = true, length = 50)
    private String categoryCode;

    @Column(name = "qr_code", unique = true, length = 12)
    private String qrCode;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "level")
    private Integer level; // 0: Ana kategori, 1,2,3... alt kategoriler

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    // Self-referencing relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private InventoryCategory parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InventoryCategory> subCategories = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
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

    public InventoryCategory(String id, String categoryName, String categoryCode, String qrCode,
                           String description, Integer level, Integer sortOrder, Boolean isActive,
                           InventoryCategory parentCategory, LocalDateTime createdDate,
                           LocalDateTime updatedDate, String createdBy, String updatedBy) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.qrCode = qrCode;
        this.description = description;
        this.level = level;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.parentCategory = parentCategory;
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

    public InventoryCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(InventoryCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<InventoryCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<InventoryCategory> subCategories) {
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

    // Utility methods
    public boolean isRootCategory() {
        return parentCategory == null;
    }

    public boolean hasSubCategories() {
        return subCategories != null && !subCategories.isEmpty();
    }
}
