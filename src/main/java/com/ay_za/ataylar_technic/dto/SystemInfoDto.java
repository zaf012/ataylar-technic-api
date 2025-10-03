package com.ay_za.ataylar_technic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class SystemInfoDto {

    private String id;
    private String systemName;
    private Integer systemOrderNo;
    private Boolean isActive;
    private String description;
    private Boolean isChecklist;
    private Boolean isFault;
    private Integer controlPointOrder;
    private Boolean controlPointIsActive;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime updatedDate;

    private String createdBy;
    private String updatedBy;

    public SystemInfoDto() {
    }

    public SystemInfoDto(String id, String systemName, Integer systemOrderNo, Boolean isActive, String description,
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsChecklist() {
        return isChecklist;
    }

    public void setIsChecklist(Boolean checklist) {
        isChecklist = checklist;
    }

    public Boolean getIsFault() {
        return isFault;
    }

    public void setIsFault(Boolean fault) {
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
