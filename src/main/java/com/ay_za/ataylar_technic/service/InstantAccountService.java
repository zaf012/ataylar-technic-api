package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.InstantAccountDto;
import com.ay_za.ataylar_technic.entity.InstantAccount;
import com.ay_za.ataylar_technic.entity.InstantGroup;
import com.ay_za.ataylar_technic.mapper.InstantAccountMapper;
import com.ay_za.ataylar_technic.repository.InstantAccountRepository;
import com.ay_za.ataylar_technic.repository.InstantGroupRepository;
import com.ay_za.ataylar_technic.service.base.InstantAccountServiceImpl;
import com.ay_za.ataylar_technic.service.base.InstantGroupServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class InstantAccountService implements InstantAccountServiceImpl {


    private final InstantAccountRepository instantAccountRepository;
    private final InstantGroupServiceImpl instantGroupServiceImpl;
    private final InstantGroupRepository instantGroupRepository;
    private final InstantAccountMapper instantAccountMapper;

    public InstantAccountService(InstantAccountRepository instantAccountRepository, InstantGroupServiceImpl instantGroupServiceImpl, InstantGroupRepository instantGroupRepository, InstantAccountMapper instantAccountMapper) {
        this.instantAccountRepository = instantAccountRepository;
        this.instantGroupServiceImpl = instantGroupServiceImpl;
        this.instantGroupRepository = instantGroupRepository;
        this.instantAccountMapper = instantAccountMapper;
    }

    /**
     * Yeni hesap oluştur
     */
    @Transactional
    @Override
    public InstantAccount createAccount(InstantAccountDto accountData) {

        // Temel validasyonlar
        validateAccountData(accountData);

        accountData.setId(UUID.randomUUID().toString());
        accountData.setCreatedBy("admin");
        accountData.setActive(true);
        accountData.setAccountGroupId(accountData.getAccountGroupId());
        accountData.setAccountGroupName(accountData.getAccountGroupName());
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

        InstantAccount instantAccount = instantAccountMapper.convertToEntity(accountData);

        return instantAccountRepository.save(instantAccount);
    }

    /**
     * Hesap bilgilerini güncelle
     */
    @Transactional
    @Override
    public InstantAccount updateAccount(String accountId, InstantAccountDto updatedData, String updatedBy) {
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

    private void updateAccountFields(InstantAccount existing, InstantAccountDto updated) {
        existing.setAccountGroupId(updated.getAccountGroupId());
        existing.setAccountGroupName(updated.getAccountGroupName());
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


    /**
     * Dummy data ile hesap oluştur (Test amaçlı) - Güncellenmiş versiyon
     */
    @Transactional
    public InstantAccount createAccount2(String createdBy) {
        // Önce rastgele bir aktif grup ID'si alalım
        List<InstantGroup> activeGroups = instantGroupRepository.findByIsActiveTrueOrderByGroupNameAsc();
        String groupId = activeGroups.isEmpty() ? "default-group-id" : activeGroups.get(0).getId();

        // Rastgele kullanıcı adı ve email oluştur
        String randomNumber = String.valueOf(System.currentTimeMillis()).substring(7);

        // Kişisel bilgiler
        String[] firstNames = {"Ahmet", "Mehmet", "Ali", "Veli", "Hasan", "Hüseyin", "İbrahim", "Mustafa", "Osman", "Ömer"};
        String[] lastNames = {"Yılmaz", "Kaya", "Demir", "Şahin", "Çelik", "Aydın", "Özkan", "Arslan", "Doğan", "Kılıç"};

        int firstIndex = (int) (Math.random() * firstNames.length);
        int lastIndex = (int) (Math.random() * lastNames.length);

        String firstName = firstNames[firstIndex];
        String lastName = lastNames[lastIndex];

        // Şirket bilgileri
        String[] companyNames = {
                "ZIRVE ELEKTRİK BOBİNAJ HİDROFOR POMPA SİSTEMLERİ",
                "Kentplus Centrum Site Yönetimi",
                "Atayıldız Plaza Yönetimi",
                "TEM 34 ESENYURT 2 SİTE YÖNETİMİ",
                "Tom 34 Esenyurt Site Yönetimi",
                "Nurol Kalman Site Yönetimi",
                "İNNOVA DEPARTMANLAR DAİRE B C BLOK SİTE YÖNETİMİ",
                "İlke Park Evleri Site Yönetimi",
                "Esas Flora Evleri Site Yönetimi"
        };

        int companyIndex = (int) (Math.random() * companyNames.length);
        String companyName = companyNames[companyIndex];

        InstantAccountDto instantAccountDto = new InstantAccountDto();

        instantAccountDto.setAccountGroupId(groupId);
        instantAccountDto.setSite("Ana Kullanıcı");
        instantAccountDto.setUserType("Müşteri");
        instantAccountDto.setUsername("user" + randomNumber + "@example.com");
        instantAccountDto.setPassword("password123");
        instantAccountDto.setName(firstName);
        instantAccountDto.setSurname(lastName);
        instantAccountDto.setCompanyName(companyName);
        instantAccountDto.setCompanyShortName(companyName.substring(0, 5));
        instantAccountDto.setAuthorizedPerson(firstName + " " + lastName);
        instantAccountDto.setPhoneCountryCode("+90");
        instantAccountDto.setPhone("212" + String.format("%07d", (int) (Math.random() * 10000000)));
        instantAccountDto.setGsmCountryCode("+90");
        instantAccountDto.setGsm("5" + String.format("%09d", (int) (Math.random() * 1000000000)));
        instantAccountDto.setEmail("user" + randomNumber + "@example.com");


        // Adres bilgileri
        String[] cities = {"İstanbul", "Ankara", "İzmir", "Bursa", "Antalya"};
        String[] districts = {"Şişli", "Beşiktaş", "Kadıköy", "Üsküdar", "Fatih", "Beyoğlu", "Bakırköy"};
        String[] neighborhoods = {"Merkez", "Çamlık", "Yenimahalle", "Kocatepe", "Bahçelievler"};


        String city = cities[(int) (Math.random() * cities.length)];
        String province = districts[(int) (Math.random() * districts.length)];
        String district = districts[(int) (Math.random() * districts.length)];
        String neighborhood = neighborhoods[(int) (Math.random() * neighborhoods.length)];

        instantAccountDto.setCity(city);
        instantAccountDto.setProvince(province);
        instantAccountDto.setDistrict(district);
        instantAccountDto.setNeighborhood(neighborhood);
        instantAccountDto.setAddress(neighborhood + " Mahallesi " +
                (int) (Math.random() * 100 + 1) + ". Sokak No:" +
                (int) (Math.random() * 50 + 1) + "/" +
                (int) (Math.random() * 20 + 1));
        instantAccountDto.setFax("212" + String.format("%07d", (int) (Math.random() * 10000000)));
        instantAccountDto.setPttBox("PK " + (int) (Math.random() * 9999 + 1));
        instantAccountDto.setPostalCode(String.format("%05d", (int) (Math.random() * 99999 + 1)));
        String[] taxOffices = {"Şişli", "Beşiktaş", "Kadıköy", "Üsküdar", "Fatih", "Beyoğlu", "Bakırköy"};
        instantAccountDto.setTaxOffice(taxOffices[(int) (Math.random() * taxOffices.length)] + " Vergi Dairesi");
        instantAccountDto.setTaxNumber(String.format("%010d", (int) (Math.random() * 9999999999L + 1)));
        instantAccountDto.setTcIdentityNo(String.format("%011d", (int) (Math.random() * 99999999999L + 1)));
        String[] banks = {"Ziraat Bankası", "İş Bankası", "Garanti BBVA", "Akbank", "Yapı Kredi"};

        instantAccountDto.setBankAddress(banks[(int) (Math.random() * banks.length)] + " " + city + " " + province + " Şubesi");
        instantAccountDto.setRiskLimitExplanation("dummy data");
        instantAccountDto.setUserStatus(true);
        instantAccountDto.setActive(true);

        instantAccountDto.setSignatureImage("null");

        // createAccount metodunu kullan
        return createAccount(instantAccountDto);
    }

    /**
     * Bulk dummy hesap oluştur - Güncellenmiş versiyon
     */
    @Transactional
    @Override
    public List<InstantAccount> createDummyAccounts(int count, String createdBy) {
        List<InstantAccount> accounts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            try {
                InstantAccount account = createAccount2(createdBy);
                accounts.add(account);

                // Aynı kullanıcı adı çakışmasını önlemek için kısa bekleme
                Thread.sleep(1);
            } catch (Exception e) {
                // Hata durumunda devam et
                System.err.println("Dummy hesap oluşturulurken hata: " + e.getMessage());
            }
        }

        return accounts;
    }

    // Helper method - Request body'den InstantAccount objesi oluştur
    private InstantAccount mapToInstantAccount(Map<String, Object> request) {
        InstantAccount account = new InstantAccount();

        // String alanlar
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
        account.setAddress((String) request.get("address"));
        account.setCity((String) request.get("city"));
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
        account.setRiskLimitExplanation((String) request.get("riskLimitExplanation"));
        account.setSignatureImage((String) request.get("signatureImage"));

        // Boolean alanlar
        if (request.get("userStatus") != null) {
            account.setUserStatus((Boolean) request.get("userStatus"));
        }
        if (request.get("isActive") != null) {
            account.setIsActive((Boolean) request.get("isActive"));
        }

        // BigDecimal alan
        if (request.get("riskLimit") != null) {
            Object riskLimitObj = request.get("riskLimit");
            if (riskLimitObj instanceof Number) {
                account.setRiskLimit(BigDecimal.valueOf(((Number) riskLimitObj).doubleValue()));
            } else if (riskLimitObj instanceof String) {
                try {
                    account.setRiskLimit(new BigDecimal((String) riskLimitObj));
                } catch (NumberFormatException e) {
                    // Invalid number format, ignore
                }
            }
        }

        return account;
    }
}
