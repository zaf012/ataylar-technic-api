package com.ay_za.ataylar_technic.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "instant_accounts")
public class InstantAccount {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Cari Grup Id
    @Column(name = "account_group_id", length = 36)
    private String accountGroupId;

    @Column(name = "account_group_name", length = 100)
    private String accountGroupName;

    @Column(name = "site_id")
    private String siteId;

    @Column(name = "site_name", length = 150)
    private String siteName;

    @Column(name = "user_type_id", length = 100)
    private Integer userTypeId;

    @Column(name = "user_type_name", length = 100)
    private String userTypeName;

    //email and username
    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "authorized_personnel", length = 200)
    private String authorizedPersonnel;

    @Column(name = "phone_country_code", length = 10)
    private String phoneCountryCode;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "gsm_country_code", length = 10)
    private String gsmCountryCode;

    @Column(name = "gsm", length = 20)
    private String gsm;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "fax", length = 20)
    private String fax;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "tax_number", length = 20)
    private String taxNumber;

    @Column(name = "tax_office", length = 100)
    private String taxOffice;

    @Column(name = "tc_identity_no", length = 11)
    private String tcIdentityNo;

    @Column(name = "iban", length = 200)
    private String iban;

    @Column(name = "user_status")
    private Boolean userStatus = true;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Default constructor
    public InstantAccount() {
    }

    // Constructor with required fields

    public InstantAccount(String id, String accountGroupId, String accountGroupName, String siteId, String siteName, Integer userTypeId, String userTypeName, String username, String password, String authorizedPersonnel, String phoneCountryCode, String phone, String gsmCountryCode, String gsm, String address, String fax, String email, String postalCode, String taxNumber, String taxOffice, String tcIdentityNo, String iban, Boolean userStatus, LocalDateTime createdDate, LocalDateTime updatedDate, String createdBy, String updatedBy, Boolean isActive) {
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