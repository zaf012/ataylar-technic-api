package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.InstantAccountDto;
import com.ay_za.ataylar_technic.entity.*;
import com.ay_za.ataylar_technic.mapper.InstantAccountMapper;
import com.ay_za.ataylar_technic.repository.InstantAccountRepository;
import com.ay_za.ataylar_technic.service.base.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InstantAccountService implements InstantAccountServiceImpl {


    private final InstantAccountRepository instantAccountRepository;
    private final InstantGroupServiceImpl instantGroupServiceImpl;
    private final InstantAccountMapper instantAccountMapper;
    private final UserTypeServiceImpl userTypeServiceImpl;
    private final FirmsInfoServiceImpl firmsInfoServiceImpl;
    private final ProjectsInfoServiceImpl projectsInfoServiceImpl;

    public InstantAccountService(InstantAccountRepository instantAccountRepository, InstantGroupServiceImpl instantGroupServiceImpl, InstantAccountMapper instantAccountMapper, UserTypeServiceImpl userTypeServiceImpl, FirmsInfoServiceImpl firmsInfoServiceImpl, ProjectsInfoServiceImpl projectsInfoServiceImpl) {
        this.instantAccountRepository = instantAccountRepository;
        this.instantGroupServiceImpl = instantGroupServiceImpl;
        this.instantAccountMapper = instantAccountMapper;
        this.userTypeServiceImpl = userTypeServiceImpl;
        this.firmsInfoServiceImpl = firmsInfoServiceImpl;
        this.projectsInfoServiceImpl = projectsInfoServiceImpl;
    }


    /**
     * Yeni hesap oluştur
     */
    @Transactional
    @Override
    public InstantAccountDto createAccount(InstantAccountDto accountData) {

        // Temel validasyonlar
        validateAccountData(accountData);

        accountData.setId(UUID.randomUUID().toString());
        accountData.setCreatedBy("admin");
        accountData.setActive(true);
        accountData.setAccountGroupId(accountData.getAccountGroupId());
        accountData.setAccountGroupName(accountData.getAccountGroupName());
        accountData.setSite(accountData.getSite());
        accountData.setUserTypeId(accountData.getUserTypeId());
        accountData.setUserTypeName(accountData.getUserTypeName());
        accountData.setUsername(accountData.getUsername());
        accountData.setPassword(accountData.getPassword());
        accountData.setFirmId(accountData.getFirmId());
        accountData.setFirmName(accountData.getFirmName());
        accountData.setProjectId(accountData.getProjectId());
        accountData.setProjectName(accountData.getProjectName());
        accountData.setCompanyShortName(accountData.getCompanyShortName());
        accountData.setPhoneCountryCode(accountData.getPhoneCountryCode());
        accountData.setPhone(accountData.getPhone());
        accountData.setGsmCountryCode(accountData.getGsmCountryCode());
        accountData.setGsm(accountData.getGsm());
        accountData.setAddress(accountData.getAddress());
        accountData.setFax(accountData.getFax());
        accountData.setEmail(accountData.getEmail());
        accountData.setPttBox(accountData.getPttBox());
        accountData.setPostalCode(accountData.getPostalCode());
        accountData.setTaxNumber(accountData.getTaxNumber());
        accountData.setTcIdentityNo(accountData.getTcIdentityNo());
        accountData.setBankAddress(accountData.getBankAddress());
        accountData.setRiskLimit(accountData.getRiskLimit());
        accountData.setRiskLimitExplanation(accountData.getRiskLimitExplanation());
        accountData.setUserStatus(accountData.getUserStatus());
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

        // Aktif yaparken benzersizlik kontrolleri
        validateUniqueFieldsForActivation(account, accountId);

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
     * Ad, soyad veya şirket adında arama
     */
    public List<InstantAccountDto> searchAccounts(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllActiveAccounts();
        }
        List<InstantAccount> instantAccounts = instantAccountRepository.searchByCompany(searchTerm.trim());
        return instantAccountMapper.convertAllToDTO(instantAccounts);
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
        existing.setUserTypeId(updated.getUserTypeId());
        existing.setUserTypeName(updated.getUserTypeName());
        existing.setUsername(updated.getUsername());
        existing.setPassword(updated.getPassword());
        existing.setFirmId(updated.getFirmId());
        existing.setFirmName(updated.getFirmName());
        existing.setProjectId(updated.getProjectId());
        existing.setProjectName(updated.getProjectName());
        existing.setCompanyShortName(updated.getCompanyShortName());
        existing.setPhoneCountryCode(updated.getPhoneCountryCode());
        existing.setPhone(updated.getPhone());
        existing.setGsmCountryCode(updated.getGsmCountryCode());
        existing.setGsm(updated.getGsm());
        existing.setAddress(updated.getAddress());
        existing.setFax(updated.getFax());
        existing.setEmail(updated.getEmail());
        existing.setPttBox(updated.getPttBox());
        existing.setPostalCode(updated.getPostalCode());
        existing.setTaxNumber(updated.getTaxNumber());
        existing.setTcIdentityNo(updated.getTcIdentityNo());
        existing.setBankAddress(updated.getBankAddress());
        existing.setRiskLimit(updated.getRiskLimit());
        existing.setRiskLimitExplanation(updated.getRiskLimitExplanation());
        existing.setUserStatus(updated.getUserStatus());
    }


    /**
     * Dummy data ile hesap oluştur (Test amaçlı) - Güncellenmiş versiyon
     */
    @Transactional
    public InstantAccountDto createAccount2(String createdBy) {
        createdBy = "admin";

        // Rastgele bir aktif grup ID'si al - güvenli yaklaşım
        Optional<InstantGroup> randomGroupOpt = instantGroupServiceImpl.getRandomGroup();
        if (randomGroupOpt.isEmpty()) {
            throw new RuntimeException("Hiç grup bulunamadı. Önce grup oluşturun.");
        }
        InstantGroup randomGroup = randomGroupOpt.get();

        // Rastgele UserType al - güvenli yaklaşım
        Optional<UserType> randomUserTypeOpt = userTypeServiceImpl.getRandomUserType();
        if (randomUserTypeOpt.isEmpty()) {
            throw new RuntimeException("Hiç kullanıcı tipi bulunamadı. Önce user type oluşturun.");
        }
        UserType randomUserType = randomUserTypeOpt.get();

        // Rastgele FirmsInfo al - güvenli yaklaşım
        Optional<FirmsInfo> randomFirmOpt = firmsInfoServiceImpl.getRandomFirm();
        if (randomFirmOpt.isEmpty()) {
            throw new RuntimeException("Hiç firma bulunamadı. Önce firma oluşturun.");
        }
        FirmsInfo randomFirm = randomFirmOpt.get();

        // Rastgele ProjectsInfo al - güvenli yaklaşım
        Optional<ProjectsInfo> randomProjectOpt = projectsInfoServiceImpl.getRandomProject();
        if (randomProjectOpt.isEmpty()) {
            throw new RuntimeException("Hiç proje bulunamadı. Önce proje oluşturun.");
        }
        ProjectsInfo randomProject = randomProjectOpt.get();

        // Rastgele kullanıcı adı ve email oluştur
        String randomNumber = String.valueOf(System.currentTimeMillis()).substring(7);

        // Kişisel bilgiler
        String[] firstNames = {"Ahmet", "Mehmet", "Ali", "Veli", "Hasan", "Hüseyin", "İbrahim", "Mustafa", "Osman", "Ömer"};
        String[] lastNames = {"Yılmaz", "Kaya", "Demir", "Şahin", "Çelik", "Aydın", "Özkan", "Arslan", "Doğan", "Kılıç"};

        int firstIndex = (int) (Math.random() * firstNames.length);
        int lastIndex = (int) (Math.random() * lastNames.length);

        String firstName = firstNames[firstIndex];
        String lastName = lastNames[lastIndex];

        InstantAccountDto instantAccountDto = new InstantAccountDto();

        instantAccountDto.setAccountGroupId(randomGroup.getId());
        instantAccountDto.setAccountGroupName(randomGroup.getGroupName());
        instantAccountDto.setUserTypeId(randomUserType.getId());
        instantAccountDto.setUserTypeName(randomUserType.getUserTypeName());
        instantAccountDto.setSite("Ana Kullanıcı");
        instantAccountDto.setUsername("user" + randomNumber + "@example.com");
        instantAccountDto.setPassword("password123");
        instantAccountDto.setAuthorizedPersonnel(firstName + " " + lastName);

        // Random seçilen firma ve proje bilgilerini kullan
        instantAccountDto.setFirmId(randomFirm.getId());
        instantAccountDto.setFirmName(randomFirm.getFirmName());
        instantAccountDto.setProjectId(randomProject.getId());
        instantAccountDto.setProjectName(randomProject.getProjectName());

        instantAccountDto.setCompanyShortName(randomFirm.getFirmName().length() > 5 ? randomFirm.getFirmName().substring(0, 5) : randomFirm.getFirmName());
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

        instantAccountDto.setAddress(neighborhood + " Mahallesi " +
                (int) (Math.random() * 100 + 1) + ". Sokak No:" +
                (int) (Math.random() * 50 + 1) + "/" +
                (int) (Math.random() * 20 + 1));
        instantAccountDto.setFax("212" + String.format("%07d", (int) (Math.random() * 10000000)));
        instantAccountDto.setPttBox("PK " + (int) (Math.random() * 9999 + 1));
        instantAccountDto.setPostalCode(String.format("%05d", (int) (Math.random() * 99999 + 1)));
        instantAccountDto.setTaxNumber(String.format("%010d", (int) (Math.random() * 9999999999L + 1)));
        instantAccountDto.setTcIdentityNo(String.format("%011d", (int) (Math.random() * 99999999999L + 1)));
        String[] banks = {"Ziraat Bankası", "İş Bankası", "Garanti BBVA", "Akbank", "Yapı Kredi"};

        instantAccountDto.setBankAddress(banks[(int) (Math.random() * banks.length)] + " " + city + " " + province + " Şubesi");
        instantAccountDto.setRiskLimitExplanation("dummy data");
        instantAccountDto.setUserStatus(true);
        instantAccountDto.setActive(true);
        instantAccountDto.setCreatedBy(createdBy);

        // createAccount metodunu kullan
        return createAccount(instantAccountDto);
    }

    /**
     * Bulk dummy hesap oluştur - Güncellenmiş versiyon
     */
    @Transactional
    @Override
    public List<InstantAccountDto> createDummyAccounts(int count, String createdBy) {
        List<InstantAccountDto> accounts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            try {
                InstantAccountDto accountDto = createAccount2(createdBy);
                accounts.add(accountDto);

                // Aynı kullanıcı adı çakışmasını önlemek için kısa bekleme
                Thread.sleep(1);
            } catch (Exception e) {
                // Hata durumunda devam et
                System.err.println("Dummy hesap oluşturulurken hata: " + e.getMessage());
            }
        }

        return accounts;
    }
}