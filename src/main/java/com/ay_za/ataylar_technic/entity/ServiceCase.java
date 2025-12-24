package com.ay_za.ataylar_technic.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "service_cases")
public class ServiceCase {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "service_case_name", nullable = false)
    private String serviceCaseName;

    public ServiceCase() {
        this.id = UUID.randomUUID().toString();
    }

    public ServiceCase(String serviceCaseName) {
        this();
        this.serviceCaseName = serviceCaseName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceCaseName() {
        return serviceCaseName;
    }

    public void setServiceCaseName(String serviceCaseName) {
        this.serviceCaseName = serviceCaseName;
    }

    @Override
    public String toString() {
        return "ServiceCase{" +
                "id='" + id + '\'' +
                ", serviceCaseName='" + serviceCaseName + '\'' +
                '}';
    }
}

