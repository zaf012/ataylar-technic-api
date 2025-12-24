package com.ay_za.ataylar_technic.dto;

public class ServiceCaseDto {

    private String id;
    private String serviceCaseName;

    public ServiceCaseDto() {
    }

    public ServiceCaseDto(String id, String serviceCaseName) {
        this.id = id;
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
        return "ServiceCaseDto{" +
                "id='" + id + '\'' +
                ", serviceCaseName='" + serviceCaseName + '\'' +
                '}';
    }
}

