package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.entity.InstantAccount;
import com.ay_za.ataylar_technic.repository.InstantAccountRepository;
import com.ay_za.ataylar_technic.repository.InstantGroupRepository;
import com.ay_za.ataylar_technic.service.base.InstantAccountServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InstantAccountService implements InstantAccountServiceImpl {


    private final InstantAccountRepository instantAccountRepository;
    private final InstantGroupRepository instantGroupRepository;

    public InstantAccountService(InstantAccountRepository instantAccountRepository, InstantGroupRepository instantGroupRepository) {
        this.instantAccountRepository = instantAccountRepository;
        this.instantGroupRepository = instantGroupRepository;
    }

    /**
     * Yeni hesap oluştur
     */
    @Transactional
    @Override
    public InstantAccount createAccount(InstantAccount accountData, String createdBy) {
        // Temel validasyonlar
        validateAccountData(accountData);

        // Cari grup var mı kontrol et
        if (accountData.getAccountGroupId() != null) {
            if (!instantGroupRepository.existsById(accountData.getAccountGroupId())) {
                throw new IllegalArgumentException("Belirtilen cari grup bulunamadı");
            }
        }

        // Username benzersizlik kontrolü
        if (accountData.getUsername() != null && !accountData.getUsername().trim().isEmpty()) {
            if (instantAccountRepository.findByUsernameAndIsActiveTrue(accountData.getUsername().trim()).isPresent()) {
                throw new IllegalArgumentException("Bu kullanıcı adı zaten kullanılıyor");
            }
        }

        // Email benzersizlik kontrolü
        if (accountData.getEmail() != null && !accountData.getEmail().trim().isEmpty()) {
            if (instantAccountRepository.findByEmailAndIsActiveTrue(accountData.getEmail().trim()).isPresent()) {
                throw new IllegalArgumentException("Bu email adresi zaten kullanılıyor");
            }
        }

        // TC Kimlik No benzersizlik kontrolü
        if (accountData.getTcIdentityNo() != null && !accountData.getTcIdentityNo().trim().isEmpty()) {
            if (instantAccountRepository.findByTcIdentityNoAndIsActiveTrue(accountData.getTcIdentityNo().trim()).isPresent()) {
                throw new IllegalArgumentException("Bu TC Kimlik No zaten kayıtlı");
            }
        }

        // Vergi No benzersizlik kontrolü
        if (accountData.getTaxNumber() != null && !accountData.getTaxNumber().trim().isEmpty()) {
            if (instantAccountRepository.findByTaxNumberAndIsActiveTrue(accountData.getTaxNumber().trim()).isPresent()) {
                throw new IllegalArgumentException("Bu Vergi No zaten kayıtlı");
            }
        }

        // ID ve audit alanlarını set et
        accountData.setId(UUID.randomUUID().toString());
        accountData.setCreatedBy(createdBy);
        accountData.setIsActive(true);

        // Default değerler
        if (accountData.getUserStatus() == null) {
            accountData.setUserStatus(true);
        }

        return instantAccountRepository.save(accountData);
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

        // Cari grup var mı kontrol et
        if (updatedData.getAccountGroupId() != null) {
            if (!instantGroupRepository.existsById(updatedData.getAccountGroupId())) {
                throw new IllegalArgumentException("Belirtilen cari grup bulunamadı");
            }
        }

        // Username benzersizlik kontrolü (kendisi hariç)
        if (updatedData.getUsername() != null && !updatedData.getUsername().trim().isEmpty()) {
            Optional<InstantAccount> existingByUsername = instantAccountRepository.findByUsernameAndIsActiveTrue(updatedData.getUsername().trim());
            if (existingByUsername.isPresent() && !existingByUsername.get().getId().equals(accountId)) {
                throw new IllegalArgumentException("Bu kullanıcı adı zaten kullanılıyor");
            }
        }

        // Email benzersizlik kontrolü (kendisi hariç)
        if (updatedData.getEmail() != null && !updatedData.getEmail().trim().isEmpty()) {
            Optional<InstantAccount> existingByEmail = instantAccountRepository.findByEmailAndIsActiveTrue(updatedData.getEmail().trim());
            if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(accountId)) {
                throw new IllegalArgumentException("Bu email adresi zaten kullanılıyor");
            }
        }

        // TC Kimlik No benzersizlik kontrolü (kendisi hariç)
        if (updatedData.getTcIdentityNo() != null && !updatedData.getTcIdentityNo().trim().isEmpty()) {
            Optional<InstantAccount> existingByTc = instantAccountRepository.findByTcIdentityNoAndIsActiveTrue(updatedData.getTcIdentityNo().trim());
            if (existingByTc.isPresent() && !existingByTc.get().getId().equals(accountId)) {
                throw new IllegalArgumentException("Bu TC Kimlik No zaten kayıtlı");
            }
        }

        // Vergi No benzersizlik kontrolü (kendisi hariç)
        if (updatedData.getTaxNumber() != null && !updatedData.getTaxNumber().trim().isEmpty()) {
            Optional<InstantAccount> existingByTax = instantAccountRepository.findByTaxNumberAndIsActiveTrue(updatedData.getTaxNumber().trim());
            if (existingByTax.isPresent() && !existingByTax.get().getId().equals(accountId)) {
                throw new IllegalArgumentException("Bu Vergi No zaten kayıtlı");
            }
        }

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
