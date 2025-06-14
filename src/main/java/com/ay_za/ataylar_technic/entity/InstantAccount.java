package com.ay_za.ataylar_technic.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "instant_accounts")
public class InstantAccount {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    // Cari Grup referansı
    @Column(name = "account_group_id", length = 36)
    private String accountGroupId;

    @Column(name = "site", length = 100)
    private String site;

    @Column(name = "user_type", length = 100)
    private String userType;

    //email and username
    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "surname", length = 100)
    private String surname;

    @Column(name = "company_name", length = 200)
    private String companyName;

    @Column(name = "company_short_name", length = 100)
    private String companyShortName;

    @Column(name = "authorized_person", length = 100)
    private String authorizedPerson;

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

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "neighborhood", length = 100)
    private String neighborhood;

    @Column(name = "fax", length = 20)
    private String fax;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "ptt_box", length = 20)
    private String pttBox;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "tax_office", length = 100)
    private String taxOffice;

    @Column(name = "tax_number", length = 20)
    private String taxNumber;

    @Column(name = "tc_identity_no", length = 11)
    private String tcIdentityNo;

    @Column(name = "bank_address", length = 200)
    private String bankAddress;

    @Column(name = "risk_limit", precision = 15, scale = 2)
    private BigDecimal riskLimit;

    @Column(name = "risk_limit_explanation", length = 200)
    private String riskLimitExplanation;

    @Column(name = "user_status")
    private Boolean userStatus = true;

    // İmza için base64 string
    @Lob
    @Column(name = "signature_image")
    private String signatureImage;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
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
    public InstantAccount(String id, String accountGroupId, String site, String userType, String username, String password, String name, String surname, String companyName, String companyShortName, String authorizedPerson, String phoneCountryCode, String phone, String gsmCountryCode, String gsm, String address, String city, String province, String district, String neighborhood, String fax, String email, String pttBox, String postalCode, String taxOffice, String taxNumber, String tcIdentityNo, String bankAddress, BigDecimal riskLimit, String riskLimitExplanation, Boolean userStatus, String signatureImage, LocalDateTime createdDate, LocalDateTime updatedDate, String createdBy, String updatedBy, Boolean isActive) {
        this.id = id;
        this.accountGroupId = accountGroupId;
        this.site = site;
        this.userType = userType;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.companyName = companyName;
        this.companyShortName = companyShortName;
        this.authorizedPerson = authorizedPerson;
        this.phoneCountryCode = phoneCountryCode;
        this.phone = phone;
        this.gsmCountryCode = gsmCountryCode;
        this.gsm = gsm;
        this.address = address;
        this.city = city;
        this.province = province;
        this.district = district;
        this.neighborhood = neighborhood;
        this.fax = fax;
        this.email = email;
        this.pttBox = pttBox;
        this.postalCode = postalCode;
        this.taxOffice = taxOffice;
        this.taxNumber = taxNumber;
        this.tcIdentityNo = tcIdentityNo;
        this.bankAddress = bankAddress;
        this.riskLimit = riskLimit;
        this.riskLimitExplanation = riskLimitExplanation;
        this.userStatus = userStatus;
        this.signatureImage = signatureImage;
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public String getAuthorizedPerson() {
        return authorizedPerson;
    }

    public void setAuthorizedPerson(String authorizedPerson) {
        this.authorizedPerson = authorizedPerson;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
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

    public String getPttBox() {
        return pttBox;
    }

    public void setPttBox(String pttBox) {
        this.pttBox = pttBox;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTaxOffice() {
        return taxOffice;
    }

    public void setTaxOffice(String taxOffice) {
        this.taxOffice = taxOffice;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getTcIdentityNo() {
        return tcIdentityNo;
    }

    public void setTcIdentityNo(String tcIdentityNo) {
        this.tcIdentityNo = tcIdentityNo;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public BigDecimal getRiskLimit() {
        return riskLimit;
    }

    public void setRiskLimit(BigDecimal riskLimit) {
        this.riskLimit = riskLimit;
    }

    public String getRiskLimitExplanation() {
        return riskLimitExplanation;
    }

    public void setRiskLimitExplanation(String riskLimitExplanation) {
        this.riskLimitExplanation = riskLimitExplanation;
    }

    public Boolean getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Boolean userStatus) {
        this.userStatus = userStatus;
    }

    public String getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(String signatureImage) {
        this.signatureImage = signatureImage;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "InstantAccount{" +
                "id='" + id + '\'' +
                ", accountGroupId='" + accountGroupId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", companyName='" + companyName + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstantAccount that = (InstantAccount) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
