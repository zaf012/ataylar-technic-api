package com.ay_za.ataylar_technic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class InstantAccountDto {

    private String id;
    private String accountGroupId;
    private String accountGroupName;
    private String siteId;
    private String siteName;
    private Integer userTypeId;
    private String userTypeName;
    private String username;
    private String password;
    private String authorizedPersonnel;
    private String phoneCountryCode;
    private String phone;
    private String gsmCountryCode;
    private String gsm;
    private String address;
    private String fax;
    private String email;
    private String postalCode;
    private String taxNumber;
    private String taxOffice;
    private String tcIdentityNo;
    private String iban;
    private Boolean userStatus;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdBy;
    private String updatedBy;
    @JsonProperty("isActive")
    private Boolean isActive;

    public InstantAccountDto() {
    }

    public InstantAccountDto(String id, String accountGroupId, String accountGroupName, String siteId, String siteName, Integer userTypeId, String userTypeName, String username, String password, String authorizedPersonnel, String phoneCountryCode, String phone, String gsmCountryCode, String gsm, String address, String fax, String email, String postalCode, String taxNumber, String taxOffice, String tcIdentityNo, String iban, Boolean userStatus, LocalDateTime createdDate, LocalDateTime updatedDate, String createdBy, String updatedBy, Boolean isActive) {
        this.id = id;
        this.accountGroupId = accountGroupId;
        this.accountGroupName = accountGroupName;
        this.siteId = siteId;
        this.siteName = siteName;
        this.userTypeId = userTypeId;
        this.userTypeName = userTypeName;
        this.username = username;
        this.password = password;
        this.authorizedPersonnel = authorizedPersonnel;
        this.phoneCountryCode = phoneCountryCode;
        this.phone = phone;
        this.gsmCountryCode = gsmCountryCode;
        this.gsm = gsm;
        this.address = address;
        this.fax = fax;
        this.email = email;
        this.postalCode = postalCode;
        this.taxNumber = taxNumber;
        this.taxOffice = taxOffice;
        this.tcIdentityNo = tcIdentityNo;
        this.iban = iban;
        this.userStatus = userStatus;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.isActive = isActive;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountGroupId() {
        return accountGroupId;
    }

    public void setAccountGroupId(String accountGroupId) {
        this.accountGroupId = accountGroupId;
    }

    public String getAccountGroupName() {
        return accountGroupName;
    }

    public void setAccountGroupName(String accountGroupName) {
        this.accountGroupName = accountGroupName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthorizedPersonnel() {
        return authorizedPersonnel;
    }

    public void setAuthorizedPersonnel(String authorizedPersonnel) {
        this.authorizedPersonnel = authorizedPersonnel;
    }

    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGsmCountryCode() {
        return gsmCountryCode;
    }

    public void setGsmCountryCode(String gsmCountryCode) {
        this.gsmCountryCode = gsmCountryCode;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getTaxOffice() {
        return taxOffice;
    }

    public void setTaxOffice(String taxOffice) {
        this.taxOffice = taxOffice;
    }

    public String getTcIdentityNo() {
        return tcIdentityNo;
    }

    public void setTcIdentityNo(String tcIdentityNo) {
        this.tcIdentityNo = tcIdentityNo;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Boolean getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Boolean userStatus) {
        this.userStatus = userStatus;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}