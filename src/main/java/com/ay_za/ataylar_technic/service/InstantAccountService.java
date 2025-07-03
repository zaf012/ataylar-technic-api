package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.entity.InstantAccount;
import com.ay_za.ataylar_technic.repository.InstantAccountRepository;
import com.ay_za.ataylar_technic.repository.InstantGroupRepository;
import com.ay_za.ataylar_technic.service.base.InstantAccountServiceImpl;
import com.ay_za.ataylar_technic.service.base.InstantGroupServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class InstantAccountService implements InstantAccountServiceImpl {


    private final InstantAccountRepository instantAccountRepository;
    private final InstantGroupServiceImpl instantGroupServiceImpl;

    public InstantAccountService(InstantAccountRepository instantAccountRepository, InstantGroupServiceImpl instantGroupServiceImpl) {
        this.instantAccountRepository = instantAccountRepository;
        this.instantGroupServiceImpl = instantGroupServiceImpl;
    }

    /**
     * Yeni hesap oluştur
     */
    @Transactional
    @Override
    public InstantAccount createAccount(@RequestBody Map<String, Object> request, String createdBy) {

        InstantAccount accountData = mapToInstantAccount(request);

        // Temel validasyonlar
        validateAccountData(accountData);

        accountData.setId(UUID.randomUUID().toString());
        accountData.setCreatedBy(createdBy);
        accountData.setIsActive(true);
        accountData.setAccountGroupId(accountData.getAccountGroupId());
        accountData.setSite(accountData.getSite());
        accountData.setUserType(accountData.getUserType());
        accountData.setUsername(accountData.getUsername());
        accountData.setPassword(accountData.getPassword());
        accountData.setName(accountData.getName());
        accountData.setSurname(accountData.getSurname());
        accountData.setCompanyName(accountData.getCompanyName());
        accountData.setCompanyShortName(accountData.getCompanyShortName());
        accountData.setAuthorizedPerson(accountData.getAuthorizedPerson());
        accountData.setPhoneCountryCode(accountData.getPhoneCountryCode());
        accountData.setPhone(accountData.getPhone());
        accountData.setGsmCountryCode(accountData.getGsmCountryCode());
        accountData.setGsm(accountData.getGsm());
        accountData.setCity(accountData.getCity());
        accountData.setActive(true);
        accountData.setAddress(accountData.getAddress());
        accountData.setProvince(accountData.getProvince());
        accountData.setDistrict(accountData.getDistrict());
        accountData.setNeighborhood(accountData.getNeighborhood());
        accountData.setFax(accountData.getFax());
        accountData.setEmail(accountData.getEmail());
        accountData.setPttBox(accountData.getPttBox());
        accountData.setPostalCode(accountData.getPostalCode());
        accountData.setTaxOffice(accountData.getTaxOffice());
        accountData.setTaxNumber(accountData.getTaxNumber());
        accountData.setTcIdentityNo(accountData.getTcIdentityNo());
        accountData.setBankAddress(accountData.getBankAddress());
        accountData.setRiskLimit(accountData.getRiskLimit());
        accountData.setRiskLimitExplanation(accountData.getRiskLimitExplanation());
        accountData.setUserStatus(accountData.getUserStatus());
        accountData.setSignatureImage(accountData.getSignatureImage());
        accountData.setCreatedDate(LocalDateTime.now());
        accountData.setUpdatedDate(null);
        accountData.setUpdatedBy(null);

        return instantAccountRepository.save(accountData);
    }

    // Helper method - Request body'den InstantAccount objesi oluştur
    private InstantAccount mapToInstantAccount(Map<String, Object> request) {
        InstantAccount account = new InstantAccount();

        account.setAccountGroupId((String) request.get("accountGroupId"));
        account.setSite((String) request.get("site"));
        account.setUserType((String) request.get("userType"));
        account.setUsername((String) request.get("username"));
        account.setPassword((String) request.get("password"));
        account.setName((String) request.get("name"));
        account.setSurname((String) request.get("surname"));
        account.setCompanyName((String) request.get("companyName"));
        account.setCompanyShortName((String) request.get("companyShortName"));
        account.setAuthorizedPerson((String) request.get("authorizedPerson"));
        account.setPhoneCountryCode((String) request.get("phoneCountryCode"));
        account.setPhone((String) request.get("phone"));
        account.setGsmCountryCode((String) request.get("gsmCountryCode"));
        account.setGsm((String) request.get("gsm"));
        account.setCity((String) request.get("city"));
        account.setActive(true);
        account.setAddress((String) request.get("address"));
        account.setProvince((String) request.get("province"));
        account.setDistrict((String) request.get("district"));
        account.setNeighborhood((String) request.get("neighborhood"));
        account.setFax((String) request.get("fax"));
        account.setEmail((String) request.get("email"));
        account.setPttBox((String) request.get("pttBox"));
        account.setPostalCode((String) request.get("postalCode"));
        account.setTaxOffice((String) request.get("taxOffice"));
        account.setTaxNumber((String) request.get("taxNumber"));
        account.setTcIdentityNo((String) request.get("tcIdentityNo"));
        account.setBankAddress((String) request.get("bankAddress"));
        account.setRiskLimit(BigDecimal.ZERO);
        account.setRiskLimitExplanation((String) request.get("riskLimitExplanation"));
        account.setUserStatus(true);
        account.setSignatureImage((String) request.get("signatureImage"));


        // Boolean alanlar
        if (request.get("userStatus") != null) {
            account.setUserStatus((Boolean) request.get("userStatus"));
        }
        if (request.get("isActive") != null) {
            account.setIsActive((Boolean) request.get("isActive"));
        }

        return account;
    }

    /**
     * Hesap bilgilerini güncelle
     */
    @Transactional
    @Override
    public InstantAccount updateAccount(String accountId, InstantAccount updatedData, String updatedBy) {
        // Mevcut hesabı getir
        InstantAccount existingAccount = instantAccountRepository.findByIdAndIsActiveTrue(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı"));

        // Temel validasyonlar
        validateAccountData(updatedData);

        // Güncellenebilir alanları kopyala
        updateAccountFields(existingAccount, updatedData);
        existingAccount.setUpdatedBy(updatedBy);

        return instantAccountRepository.save(existingAccount);
    }

    /**
     * Hesabı aktif/pasif yap
     */
    @Transactional
    @Override
    public InstantAccount toggleAccountStatus(String accountId, String updatedBy) {
        InstantAccount account = instantAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı"));

        account.setIsActive(!account.getIsActive());
        account.setUpdatedBy(updatedBy);

        return instantAccountRepository.save(account);
    }

    /**
     * Kullanıcı durumunu aktif/pasif yap
     */
    @Transactional
    @Override
    public InstantAccount toggleUserStatus(String accountId, String updatedBy) {
        InstantAccount account = instantAccountRepository.findByIdAndIsActiveTrue(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Aktif hesap bulunamadı"));

        account.setUserStatus(!account.getUserStatus());
        account.setUpdatedBy(updatedBy);

        return instantAccountRepository.save(account);
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
    public InstantAccount deactivateAccount(String accountId, String updatedBy) {
        InstantAccount account = instantAccountRepository.findByIdAndIsActiveTrue(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Aktif hesap bulunamadı"));

        account.setIsActive(false);
        account.setUpdatedBy(updatedBy);

        return instantAccountRepository.save(account);
    }

    /**
     * Hesabı aktif yap
     */
    @Transactional
    @Override
    public InstantAccount activateAccount(String accountId, String updatedBy) {
        InstantAccount account = instantAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı"));

        if (account.getIsActive()) {
            throw new IllegalArgumentException("Hesap zaten aktif");
        }

        // Aktif yaparken benzersizlik kontrolleri
        validateUniqueFieldsForActivation(account, accountId);

        account.setIsActive(true);
        account.setUpdatedBy(updatedBy);

        return instantAccountRepository.save(account);
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
    public List<InstantAccount> getAllActiveAccounts() {
        return instantAccountRepository.findByIsActiveTrue();
    }

    /**
     * Cari gruba göre hesapları getir
     */
    @Override
    public List<InstantAccount> getAccountsByGroup(String groupId) {
        return instantAccountRepository.findByAccountGroupIdAndIsActiveTrue(groupId);
    }

    /**
     * Site'e göre hesapları getir
     */
    public List<InstantAccount> getAccountsBySite(String site) {
        return instantAccountRepository.findBySiteAndIsActiveTrue(site);
    }

    /**
     * Kullanıcı tipine göre hesapları getir
     */
    public List<InstantAccount> getAccountsByUserType(String userType) {
        return instantAccountRepository.findByUserTypeAndIsActiveTrue(userType);
    }

    /**
     * Username veya email ile hesap arama
     */
    public Optional<InstantAccount> findByUsernameOrEmail(String identifier) {
        return instantAccountRepository.findByUsernameOrEmail(identifier);
    }

    /**
     * Ad, soyad veya şirket adında arama
     */
    public List<InstantAccount> searchAccounts(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllActiveAccounts();
        }
        return instantAccountRepository.searchByNameOrCompany(searchTerm.trim());
    }

    /**
     * Şifre güncelle
     */
    @Transactional
    @Override
    public InstantAccount updatePassword(String accountId, String newPassword, String updatedBy) {
        InstantAccount account = instantAccountRepository.findByIdAndIsActiveTrue(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Aktif hesap bulunamadı"));

        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Şifre boş olamaz");
        }

        account.setPassword(newPassword);
        account.setUpdatedBy(updatedBy);

        return instantAccountRepository.save(account);
    }

    /**
     * Risk limiti güncelle
     */
    @Transactional
    public InstantAccount updateRiskLimit(String accountId, BigDecimal riskLimit, String explanation, String updatedBy) {
        InstantAccount account = instantAccountRepository.findByIdAndIsActiveTrue(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Aktif hesap bulunamadı"));

        account.setRiskLimit(riskLimit);
        account.setRiskLimitExplanation(explanation);
        account.setUpdatedBy(updatedBy);

        return instantAccountRepository.save(account);
    }

    /**
     * İmza güncelle
     */
    @Transactional
    public InstantAccount updateSignature(String accountId, String signatureBase64, String updatedBy) {
        InstantAccount account = instantAccountRepository.findByIdAndIsActiveTrue(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Aktif hesap bulunamadı"));

        account.setSignatureImage(signatureBase64);
        account.setUpdatedBy(updatedBy);

        return instantAccountRepository.save(account);
    }

    // Private helper metodlar

    private void validateAccountData(InstantAccount account) {
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

        // Email format kontrolü
        if (account.getEmail() != null && !account.getEmail().trim().isEmpty()) {
            if (!isValidEmail(account.getEmail().trim())) {
                throw new IllegalArgumentException("Geçerli bir email adresi giriniz");
            }
        }

        // TC Kimlik No kontrolü
        if (account.getTcIdentityNo() != null && !account.getTcIdentityNo().trim().isEmpty()) {
            if (account.getTcIdentityNo().trim().length() != 11) {
                throw new IllegalArgumentException("TC Kimlik No 11 haneli olmalıdır");
            }
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private void validateUniqueFieldsForActivation(InstantAccount account, String accountId) {
        // Username kontrolü
        if (account.getUsername() != null) {
            Optional<InstantAccount> existingByUsername = instantAccountRepository.findByUsernameAndIsActiveTrue(account.getUsername());
            if (existingByUsername.isPresent() && !existingByUsername.get().getId().equals(accountId)) {
                throw new IllegalArgumentException("Bu kullanıcı adı zaten aktif bir hesapta kullanılıyor");
            }
        }

        // Email kontrolü
        if (account.getEmail() != null) {
            Optional<InstantAccount> existingByEmail = instantAccountRepository.findByEmailAndIsActiveTrue(account.getEmail());
            if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(accountId)) {
                throw new IllegalArgumentException("Bu email adresi zaten aktif bir hesapta kullanılıyor");
            }
        }

        // TC Kimlik No kontrolü
        if (account.getTcIdentityNo() != null) {
            Optional<InstantAccount> existingByTc = instantAccountRepository.findByTcIdentityNoAndIsActiveTrue(account.getTcIdentityNo());
            if (existingByTc.isPresent() && !existingByTc.get().getId().equals(accountId)) {
                throw new IllegalArgumentException("Bu TC Kimlik No zaten aktif bir hesapta kayıtlı");
            }
        }

        // Vergi No kontrolü
        if (account.getTaxNumber() != null) {
            Optional<InstantAccount> existingByTax = instantAccountRepository.findByTaxNumberAndIsActiveTrue(account.getTaxNumber());
            if (existingByTax.isPresent() && !existingByTax.get().getId().equals(accountId)) {
                throw new IllegalArgumentException("Bu Vergi No zaten aktif bir hesapta kayıtlı");
            }
        }
    }

    private void updateAccountFields(InstantAccount existing, InstantAccount updated) {
        existing.setAccountGroupId(updated.getAccountGroupId());
        existing.setSite(updated.getSite());
        existing.setUserType(updated.getUserType());
        existing.setUsername(updated.getUsername());
        existing.setPassword(updated.getPassword());
        existing.setName(updated.getName());
        existing.setSurname(updated.getSurname());
        existing.setCompanyName(updated.getCompanyName());
        existing.setCompanyShortName(updated.getCompanyShortName());
        existing.setAuthorizedPerson(updated.getAuthorizedPerson());
        existing.setPhoneCountryCode(updated.getPhoneCountryCode());
        existing.setPhone(updated.getPhone());
        existing.setGsmCountryCode(updated.getGsmCountryCode());
        existing.setGsm(updated.getGsm());
        existing.setAddress(updated.getAddress());
        existing.setCity(updated.getCity());
        existing.setProvince(updated.getProvince());
        existing.setDistrict(updated.getDistrict());
        existing.setNeighborhood(updated.getNeighborhood());
        existing.setFax(updated.getFax());
        existing.setEmail(updated.getEmail());
        existing.setPttBox(updated.getPttBox());
        existing.setPostalCode(updated.getPostalCode());
        existing.setTaxOffice(updated.getTaxOffice());
        existing.setTaxNumber(updated.getTaxNumber());
        existing.setTcIdentityNo(updated.getTcIdentityNo());
        existing.setBankAddress(updated.getBankAddress());
        existing.setRiskLimit(updated.getRiskLimit());
        existing.setRiskLimitExplanation(updated.getRiskLimitExplanation());
        existing.setUserStatus(updated.getUserStatus());
        existing.setSignatureImage(updated.getSignatureImage());
    }
}
