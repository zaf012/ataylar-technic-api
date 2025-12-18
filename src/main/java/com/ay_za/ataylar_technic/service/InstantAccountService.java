package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.InstantAccountDto;
import com.ay_za.ataylar_technic.dto.SitesInfoDto;
import com.ay_za.ataylar_technic.entity.InstantAccount;
import com.ay_za.ataylar_technic.mapper.InstantAccountMapper;
import com.ay_za.ataylar_technic.repository.InstantAccountRepository;
import com.ay_za.ataylar_technic.service.base.InstantAccountServiceImpl;
import com.ay_za.ataylar_technic.service.base.InstantGroupServiceImpl;
import com.ay_za.ataylar_technic.service.base.SitesInfoServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InstantAccountService implements InstantAccountServiceImpl {


    private final InstantAccountRepository instantAccountRepository;
    private final InstantGroupServiceImpl instantGroupServiceImpl;
    private final SitesInfoServiceImpl sitesInfoServiceImpl;
    private final InstantAccountMapper instantAccountMapper;

    public InstantAccountService(InstantAccountRepository instantAccountRepository, InstantGroupServiceImpl instantGroupServiceImpl, SitesInfoServiceImpl sitesInfoServiceImpl, InstantAccountMapper instantAccountMapper) {
        this.instantAccountRepository = instantAccountRepository;
        this.instantGroupServiceImpl = instantGroupServiceImpl;
        this.sitesInfoServiceImpl = sitesInfoServiceImpl;
        this.instantAccountMapper = instantAccountMapper;
    }

    /**
     * Yeni hesap oluştur
     */
    @Transactional
    @Override
    public InstantAccountDto createAccount(InstantAccountDto accountData) {

        // Temel validasyonlar
        validateAccountData(accountData);

        SitesInfoDto sitesInfoDto = new SitesInfoDto();
        sitesInfoDto.setSiteName(accountData.getSiteName());

        SitesInfoDto savedSite = sitesInfoServiceImpl.createSite(sitesInfoDto);

        // 2. Generate edilen siteId'yi accountData'ya set et
        accountData.setSiteId(savedSite.getId());
        accountData.setSiteName(savedSite.getSiteName());

        accountData.setCreatedBy("admin");
        accountData.setActive(true);
        accountData.setAccountGroupId(accountData.getAccountGroupId() != null ? accountData.getAccountGroupId() : "");
        accountData.setAccountGroupName(accountData.getAccountGroupName() != null ? accountData.getAccountGroupName() : "");
        accountData.setUserTypeId(accountData.getUserTypeId() != null ? accountData.getUserTypeId() : 0);
        accountData.setUserTypeName(accountData.getUserTypeName() != null ? accountData.getUserTypeName() : "");
        accountData.setUsername(accountData.getUsername() != null ? accountData.getUsername() : "");
        accountData.setPassword(accountData.getPassword() != null ? accountData.getPassword() : "");
        accountData.setPhoneCountryCode(accountData.getPhoneCountryCode() != null ? accountData.getPhoneCountryCode() : "");
        accountData.setPhone(accountData.getPhone() != null ? accountData.getPhone() : "");
        accountData.setGsmCountryCode(accountData.getGsmCountryCode() != null ? accountData.getGsmCountryCode() : "");
        accountData.setGsm(accountData.getGsm() != null ? accountData.getGsm() : "");
        accountData.setAddress(accountData.getAddress() != null ? accountData.getAddress() : "");
        accountData.setFax(accountData.getFax() != null ? accountData.getFax() : "");
        accountData.setEmail(accountData.getEmail() != null ? accountData.getEmail() : "");
        accountData.setPostalCode(accountData.getPostalCode() != null ? accountData.getPostalCode() : "");
        accountData.setTaxNumber(accountData.getTaxNumber() != null ? accountData.getTaxNumber() : "");
        accountData.setTaxOffice(accountData.getTaxOffice() != null ? accountData.getTaxOffice() : "");
        accountData.setTcIdentityNo(accountData.getTcIdentityNo() != null ? accountData.getTcIdentityNo() : "");
        accountData.setIban(accountData.getIban() != null ? accountData.getIban() : "");
        accountData.setUserStatus(accountData.getUserStatus() != null ? accountData.getUserStatus() : true);
        accountData.setCreatedDate(LocalDateTime.now());
        accountData.setUpdatedDate(null);
        accountData.setUpdatedBy(null);

        InstantAccount instantAccount = instantAccountMapper.convertToEntity(accountData);
        InstantAccount saved = instantAccountRepository.save(instantAccount);

        return instantAccountMapper.convertToDTO(saved);
    }

    /**
     * Hesap bilgilerini güncelle
     */
    @Transactional
    @Override
    public InstantAccountDto updateAccount(String accountId, InstantAccountDto updatedData, String updatedBy) {
        // Mevcut hesabı getir
        InstantAccount existingAccount = instantAccountRepository.findByIdAndIsActiveTrue(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı"));

        // Temel validasyonlar
        validateAccountData(updatedData);

        // Güncellenebilir alanları kopyala
        updateAccountFields(existingAccount, updatedData);
        existingAccount.setUpdatedBy(updatedBy);

        InstantAccount updated = instantAccountRepository.save(existingAccount);

        return instantAccountMapper.convertToDTO(updated);
    }

    /**
     * Hesabı aktif/pasif yap
     */
    @Transactional
    @Override
    public InstantAccountDto toggleAccountStatus(String accountId, String updatedBy) {
        InstantAccount account = instantAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı"));

        account.setActive(!account.getActive());
        account.setUpdatedBy(updatedBy);

        InstantAccount updatedStatusData = instantAccountRepository.save(account);
        return instantAccountMapper.convertToDTO(updatedStatusData);
    }

    /**
     * Kullanıcı durumunu aktif/pasif yap
     */
    @Transactional
    @Override
    public InstantAccountDto toggleUserStatus(String accountId, String updatedBy) {
        InstantAccount account = instantAccountRepository.findByIdAndIsActiveTrue(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Aktif hesap bulunamadı"));

        account.setUserStatus(!account.getUserStatus());
        account.setUpdatedBy(updatedBy);

        InstantAccount updatedUserStatusData = instantAccountRepository.save(account);

        return instantAccountMapper.convertToDTO(updatedUserStatusData);
    }

    /**
     * Hesabı tamamen sil (hard delete)
     */
    @Transactional
    @Override
    public void deleteAccount(String accountId) {
        InstantAccount account = instantAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı"));

        instantAccountRepository.delete(account);
    }

    /**
     * Hesabı pasif yap (soft delete)
     */
    @Transactional
    @Override
    public InstantAccountDto deactivateAccount(String accountId, String updatedBy) {
        InstantAccount account = instantAccountRepository.findByIdAndIsActiveTrue(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Aktif hesap bulunamadı"));

        account.setActive(false);
        account.setUpdatedBy(updatedBy);

        InstantAccount updatedAccountStatus = instantAccountRepository.save(account);

        return instantAccountMapper.convertToDTO(updatedAccountStatus);
    }

    /**
     * Hesabı aktif yap
     */
    @Transactional
    @Override
    public InstantAccountDto activateAccount(String accountId, String updatedBy) {
        InstantAccount account = instantAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı"));

        if (account.getActive()) {
            throw new IllegalArgumentException("Hesap zaten aktif");
        }

        account.setActive(true);
        account.setUpdatedBy(updatedBy);

        InstantAccount updatedAccountStatusData = instantAccountRepository.save(account);

        return instantAccountMapper.convertToDTO(updatedAccountStatusData);
    }

    /**
     * ID'ye göre hesap getir
     */
    @Override
    public Optional<InstantAccount> getAccountById(String accountId) {
        return instantAccountRepository.findById(accountId);
    }

    /**
     * Aktif hesap getir
     */
    @Override
    public Optional<InstantAccount> getActiveAccountById(String accountId) {
        return instantAccountRepository.findByIdAndIsActiveTrue(accountId);
    }

    /**
     * Tüm aktif hesapları getir
     */
    @Override
    public List<InstantAccountDto> getAllActiveAccounts() {
        List<InstantAccount> byIsActiveTrue = instantAccountRepository.findByIsActiveTrue();
        return instantAccountMapper.convertAllToDTO(byIsActiveTrue);
    }

    /**
     * Cari gruba göre hesapları getir
     */
    @Override
    public List<InstantAccountDto> getAccountsByGroup(String groupId) {
        List<InstantAccount> byAccountGroupIdAndIsActiveTrue = instantAccountRepository.findByAccountGroupIdAndIsActiveTrue(groupId);
        return instantAccountMapper.convertAllToDTO(byAccountGroupIdAndIsActiveTrue);
    }

    /**
     * Username veya email ile hesap arama
     */
    public Optional<InstantAccount> findByUsernameOrEmail(String identifier) {
        return instantAccountRepository.findByUsernameOrEmail(identifier);
    }

    /**
     * Şifre güncelle
     */
    @Transactional
    @Override
    public InstantAccountDto updatePassword(String accountId, String newPassword, String updatedBy) {
        InstantAccount account = instantAccountRepository.findByIdAndIsActiveTrue(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Aktif hesap bulunamadı"));

        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Şifre boş olamaz");
        }

        account.setPassword(newPassword);
        account.setUpdatedBy(updatedBy);

        InstantAccount saved = instantAccountRepository.save(account);
        return instantAccountMapper.convertToDTO(saved);
    }

//    /**
//     * İmza güncelle
//     */
//    @Transactional
//    public InstantAccount updateSignature(String accountId, String signatureBase64, String updatedBy) {
//        InstantAccount account = instantAccountRepository.findByIdAndIsActiveTrue(accountId)
//                .orElseThrow(() -> new IllegalArgumentException("Aktif hesap bulunamadı"));
//
//        account.setSignatureImage(signatureBase64);
//        account.setUpdatedBy(updatedBy);
//
//        return instantAccountRepository.save(account);
//    }

    // Private helper metodlar

    private void validateAccountData(InstantAccountDto account) {
        if (account == null) {
            throw new IllegalArgumentException("Hesap bilgileri boş olamaz");
        }

        // Cari grup var mı kontrol et
        if (account.getAccountGroupId() != null) {

            try {
                if (!instantGroupServiceImpl.checkGroupById(account.getAccountGroupId())) {
                    throw new IllegalArgumentException("Belirtilen cari grup bulunamadı");
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        // Username kontrolü
        if (account.getUsername() != null && account.getUsername().trim().length() < 3) {
            throw new IllegalArgumentException("Kullanıcı adı en az 3 karakter olmalıdır");
        }
    }

    private void updateAccountFields(InstantAccount existing, InstantAccountDto updated) {
        existing.setAccountGroupId(updated.getAccountGroupId() != null ? updated.getAccountGroupId() : "");
        existing.setAccountGroupName(updated.getAccountGroupName() != null ? updated.getAccountGroupName() : "");
        existing.setSiteId(updated.getSiteId() != null ? updated.getSiteId() : "");
        existing.setSiteName(updated.getSiteName() != null ? updated.getSiteName() : "");
        existing.setUserTypeId(updated.getUserTypeId() != null ? updated.getUserTypeId() : 0);
        existing.setUserTypeName(updated.getUserTypeName() != null ? updated.getUserTypeName() : "");
        existing.setUsername(updated.getUsername() != null ? updated.getUsername() : "");
        existing.setPassword(updated.getPassword() != null ? updated.getPassword() : "");
        existing.setPhoneCountryCode(updated.getPhoneCountryCode() != null ? updated.getPhoneCountryCode() : "");
        existing.setPhone(updated.getPhone() != null ? updated.getPhone() : "");
        existing.setGsmCountryCode(updated.getGsmCountryCode() != null ? updated.getGsmCountryCode() : "");
        existing.setGsm(updated.getGsm() != null ? updated.getGsm() : "");
        existing.setAddress(updated.getAddress() != null ? updated.getAddress() : "");
        existing.setFax(updated.getFax() != null ? updated.getFax() : "");
        existing.setEmail(updated.getEmail() != null ? updated.getEmail() : "");
        existing.setPostalCode(updated.getPostalCode() != null ? updated.getPostalCode() : "");
        existing.setTaxNumber(updated.getTaxNumber() != null ? updated.getTaxNumber() : "");
        existing.setTaxOffice(updated.getTaxOffice() != null ? updated.getTaxOffice() : "");
        existing.setTcIdentityNo(updated.getTcIdentityNo() != null ? updated.getTcIdentityNo() : "");
        existing.setIban(updated.getIban() != null ? updated.getIban() : "");
        existing.setUserStatus(updated.getUserStatus() != null ? updated.getUserStatus() : true);
        existing.setUpdatedBy("admin");
        existing.setUpdatedDate(LocalDateTime.now());
    }
}

