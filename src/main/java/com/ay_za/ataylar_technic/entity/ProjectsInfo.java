package com.ay_za.ataylar_technic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "projects_info")
public class ProjectsInfo {

    @Id
    @Column(name = "id", length = 200)
    private String id;

    @Column(name = "firm_id", nullable = false, length = 200)
    private String firmId;

    @Column(name = "firm_name", nullable = false, length = 200)
    private String firmName;

    @Column(name = "project_name", nullable = false, length = 200)
    private String projectName;

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
    public ProjectsInfo() {
    }

    public ProjectsInfo(String id, String firmId, String firmName, String projectName, LocalDateTime createdDate,
                        String createdBy, LocalDateTime updatedDate, String updatedBy) {
        this.id = id;
        this.firmId = firmId;
        this.firmName = firmName;
        this.projectName = projectName;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.updatedDate = updatedDate;
        this.updatedBy = updatedBy;
    }

    // Constructor
    public ProjectsInfo(String firmId, String firmName, String projectName, String createdBy) {
        this.id = UUID.randomUUID().toString();
        this.firmName = firmName;
        this.firmId = firmId;
        this.projectName = projectName;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    @Override
    public String toString() {
        return "ProjectsInfo{" +
                "id='" + id + '\'' +
                ", firmId='" + firmId + '\'' +
                ", firmName='" + firmName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", updatedDate=" + updatedDate +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
