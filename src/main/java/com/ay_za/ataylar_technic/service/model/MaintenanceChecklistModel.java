package com.ay_za.ataylar_technic.service.model;

import com.ay_za.ataylar_technic.dto.SystemInfoDto;
import com.ay_za.ataylar_technic.enums.ServiceScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaintenanceChecklistModel {

    private String description;
    private String customerFirmName;
    private String customerAddress;
    private String authorizedPersonnel;
    private String telNo;
    private String systemName;
    private String gsmNo;
    private String email;
    private String productSerialNo;
    private String productBrand;
    private String productModel;
    private String productPurpose;
    private String serviceCase;
    private String blockName;
    private String floor;
    private String location;
    private String serviceDate;
    private String entryTime;
    private String exitTime;
    private String serviceCarPlate;
    private String serviceCarKm;
    private String servicePersonnel;
    private String image1;
    private String image2;
    private String image3;
    private List<SystemInfoDto> systemInfoDtoList = new ArrayList<>();
    private String periodicMaintenanceContract;
    private String onsiteIntervention;
    private String warrantyCoverage;
    private String periodicMaintenance;
    private String nonWarrantyCoverage;
    private String damageAssessment;
    private String fault;
    private String workshop;
    private String reportNo;
    // Frontend'den hangi item'ların checked/unchecked olduğunu gönderebilmesi için
    // Key: systemOrderNo (veya başka unique identifier), Value: true ise checked, false ise unchecked
    private Map<Integer, Boolean> checkedItems = new HashMap<>();

    private List<ServiceScope> serviceScopes = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerFirmName() {
        return customerFirmName;
    }

    public void setCustomerFirmName(String customerFirmName) {
        this.customerFirmName = customerFirmName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getAuthorizedPersonnel() {
        return authorizedPersonnel;
    }

    public void setAuthorizedPersonnel(String authorizedPersonnel) {
        this.authorizedPersonnel = authorizedPersonnel;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getGsmNo() {
        return gsmNo;
    }

    public void setGsmNo(String gsmNo) {
        this.gsmNo = gsmNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProductSerialNo() {
        return productSerialNo;
    }

    public void setProductSerialNo(String productSerialNo) {
        this.productSerialNo = productSerialNo;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductPurpose() {
        return productPurpose;
    }

    public void setProductPurpose(String productPurpose) {
        this.productPurpose = productPurpose;
    }

    public String getServiceCase() {
        return serviceCase;
    }

    public void setServiceCase(String serviceCase) {
        this.serviceCase = serviceCase;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public String getServiceCarPlate() {
        return serviceCarPlate;
    }

    public void setServiceCarPlate(String serviceCarPlate) {
        this.serviceCarPlate = serviceCarPlate;
    }

    public String getServiceCarKm() {
        return serviceCarKm;
    }

    public void setServiceCarKm(String serviceCarKm) {
        this.serviceCarKm = serviceCarKm;
    }

    public String getServicePersonnel() {
        return servicePersonnel;
    }

    public void setServicePersonnel(String servicePersonnel) {
        this.servicePersonnel = servicePersonnel;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public List<SystemInfoDto> getSystemInfoDtoList() {
        return systemInfoDtoList;
    }

    public void setSystemInfoDtoList(List<SystemInfoDto> systemInfoDtoList) {
        this.systemInfoDtoList = systemInfoDtoList;
    }

    public String getPeriodicMaintenanceContract() {
        return periodicMaintenanceContract;
    }

    public void setPeriodicMaintenanceContract(String periodicMaintenanceContract) {
        this.periodicMaintenanceContract = periodicMaintenanceContract;
    }

    public String getOnsiteIntervention() {
        return onsiteIntervention;
    }

    public void setOnsiteIntervention(String onsiteIntervention) {
        this.onsiteIntervention = onsiteIntervention;
    }

    public String getWarrantyCoverage() {
        return warrantyCoverage;
    }

    public void setWarrantyCoverage(String warrantyCoverage) {
        this.warrantyCoverage = warrantyCoverage;
    }

    public String getPeriodicMaintenance() {
        return periodicMaintenance;
    }

    public void setPeriodicMaintenance(String periodicMaintenance) {
        this.periodicMaintenance = periodicMaintenance;
    }

    public String getNonWarrantyCoverage() {
        return nonWarrantyCoverage;
    }

    public void setNonWarrantyCoverage(String nonWarrantyCoverage) {
        this.nonWarrantyCoverage = nonWarrantyCoverage;
    }

    public String getDamageAssessment() {
        return damageAssessment;
    }

    public void setDamageAssessment(String damageAssessment) {
        this.damageAssessment = damageAssessment;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public String getWorkshop() {
        return workshop;
    }

    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public Map<Integer, Boolean> getCheckedItems() {
        return checkedItems;
    }

    public void setCheckedItems(Map<Integer, Boolean> checkedItems) {
        this.checkedItems = checkedItems;
    }

    public List<ServiceScope> getServiceScopes() {
        return serviceScopes;
    }

    public void setServiceScopes(List<ServiceScope> serviceScopes) {
        this.serviceScopes = serviceScopes;
    }
}
