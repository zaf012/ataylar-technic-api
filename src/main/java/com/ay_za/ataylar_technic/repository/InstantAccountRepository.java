package com.ay_za.ataylar_technic.repository;


import com.ay_za.ataylar_technic.entity.InstantAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstantAccountRepository extends JpaRepository<InstantAccount, String> {

    // Aktif hesapları getir
    List<InstantAccount> findByIsActiveTrue();

    // Aktif hesap getir
    Optional<InstantAccount> findByIdAndIsActiveTrue(String id);

    // Kullanıcı adına göre arama
    Optional<InstantAccount> findByUsernameAndIsActiveTrue(String username);

    // Email'e göre arama
    Optional<InstantAccount> findByEmailAndIsActiveTrue(String email);

    // Cari grup ID'sine göre hesapları getir
    List<InstantAccount> findByAccountGroupIdAndIsActiveTrue(String accountGroupId);

    // Şirket adına göre arama (case-insensitive)
    List<InstantAccount> findByCompanyNameContainingIgnoreCaseAndIsActiveTrue(String companyName);

    // TC Kimlik numarasına göre arama
    Optional<InstantAccount> findByTcIdentityNoAndIsActiveTrue(String tcIdentityNo);

    // Vergi numarasına göre arama
    Optional<InstantAccount> findByTaxNumberAndIsActiveTrue(String taxNumber);

    // Site'e göre hesapları getir
    List<InstantAccount> findBySiteAndIsActiveTrue(String site);

    // Kullanıcı tipine göre hesapları getir
    List<InstantAccount> findByUserTypeAndIsActiveTrue(String userType);

    // İl ve ilçeye göre hesapları getir
    List<InstantAccount> findByCityAndProvinceAndIsActiveTrue(String city, String province);

    // Kullanıcı durumuna göre hesapları getir
    List<InstantAccount> findByUserStatusAndIsActiveTrue(Boolean userStatus);

    // Kullanıcı adı veya email'e göre arama
    @Query("SELECT a FROM InstantAccount a WHERE (a.username = :identifier OR a.email = :identifier) AND a.isActive = true")
    Optional<InstantAccount> findByUsernameOrEmail(@Param("identifier") String identifier);

    // Şirket adı, ad veya soyada göre arama
    @Query("SELECT a FROM InstantAccount a WHERE " +
            "(UPPER(a.companyName) LIKE UPPER(CONCAT('%', :searchTerm, '%')) OR " +
            "UPPER(a.name) LIKE UPPER(CONCAT('%', :searchTerm, '%')) OR " +
            "UPPER(a.surname) LIKE UPPER(CONCAT('%', :searchTerm, '%'))) AND " +
            "a.isActive = true")
    List<InstantAccount> searchByNameOrCompany(@Param("searchTerm") String searchTerm);

    // Cari grup ve site'e göre hesapları getir
    List<InstantAccount> findByAccountGroupIdAndSiteAndIsActiveTrue(String accountGroupId, String site);

    // Risk limiti olan hesapları getir
    @Query("SELECT a FROM InstantAccount a WHERE a.riskLimit IS NOT NULL AND a.riskLimit > 0 AND a.isActive = true")
    List<InstantAccount> findAccountsWithRiskLimit();

    // Eksik bilgili hesapları getir (telefon veya email eksik)
    @Query("SELECT a FROM InstantAccount a WHERE (a.phone IS NULL OR a.phone = '' OR a.email IS NULL OR a.email = '') AND a.isActive = true")
    List<InstantAccount> findAccountsWithMissingContactInfo();

    // Yetkili kişiye göre hesapları getir
    List<InstantAccount> findByAuthorizedPersonContainingIgnoreCaseAndIsActiveTrue(String authorizedPerson);

}
