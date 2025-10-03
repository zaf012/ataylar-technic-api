package com.ay_za.ataylar_technic.service.request;

public class ChecklistOrFaultCreateAndUpdateRequest {

    private String systemId;
    private Boolean isChecklist;
    private Boolean isFault;
    private Integer controlPointOrder;
    private Boolean controlPointIsActive;
    private String description;

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}