package com.ay_za.ataylar_technic.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_info")
public class SystemInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "system_name", length = 100)
    private String systemName;

    @Column(name = "system_order_no", length = 100)
    private Integer systemOrderNo;

    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * Çeklist/Arıza tanımlaması
     */
    @Column(name = "description")
    private String description;

    @Column(name = "is_checklist")
    private Boolean isChecklist;

    @Column(name = "is_fault")
    private Boolean isFault;

    /**
     * Kontrol Maddesi sıra nosu
     */
    @Column(name = "control_point_order")
    private Integer controlPointOrder;

    /**
     * Kontrol Maddesi aktif/pasif
     */
    @Column(name = "control_point_is_active")
    private Boolean controlPointIsActive;

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

    public SystemInfo() {
    }

    public SystemInfo(String id, String systemName, Integer systemOrderNo, Boolean isActive, String description,
                      Boolean isChecklist, Boolean isFault, Integer controlPointOrder, Boolean controlPointIsActive,
                      LocalDateTime createdDate, LocalDateTime updatedDate, String createdBy, String updatedBy) {
        this.id = id;
        this.systemName = systemName;
        this.systemOrderNo = systemOrderNo;
        this.isActive = isActive;
        this.description = description;
        this.isChecklist = isChecklist;
        this.isFault = isFault;
        this.controlPointOrder = controlPointOrder;
        this.controlPointIsActive = controlPointIsActive;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getSystemOrderNo() {
        return systemOrderNo;
    }

    public void setSystemOrderNo(Integer systemOrderNo) {
        this.systemOrderNo = systemOrderNo;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getChecklist() {
        return isChecklist;
    }

    public void setChecklist(Boolean checklist) {
        isChecklist = checklist;
    }

    public Boolean getFault() {
        return isFault;
    }

    public void setFault(Boolean fault) {
        isFault = fault;
    }

    public Integer getControlPointOrder() {
        return controlPointOrder;
    }

    public void setControlPointOrder(Integer controlPointOrder) {
        this.controlPointOrder = controlPointOrder;
    }

    public Boolean getControlPointIsActive() {
        return controlPointIsActive;
    }

    public void setControlPointIsActive(Boolean controlPointIsActive) {
        this.controlPointIsActive = controlPointIsActive;
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
