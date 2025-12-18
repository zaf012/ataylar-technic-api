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

    // TC Kimlik numarasına göre arama
    Optional<InstantAccount> findByTcIdentityNoAndIsActiveTrue(String tcIdentityNo);

    // Vergi numarasına göre arama
    Optional<InstantAccount> findByTaxNumberAndIsActiveTrue(String taxNumber);

    // Kullanıcı adı veya email'e göre arama
    @Query("SELECT a FROM InstantAccount a WHERE (a.username = :identifier OR a.email = :identifier) AND a.isActive = true")
    Optional<InstantAccount> findByUsernameOrEmail(@Param("identifier") String identifier);

    // Eksik bilgili hesapları getir (telefon veya email eksik)
    @Query("SELECT a FROM InstantAccount a WHERE (a.phone IS NULL OR a.phone = '' OR a.email IS NULL OR a.email = '') AND a.isActive = true")
    List<InstantAccount> findAccountsWithMissingContactInfo();

    // Yetkili kişiye göre hesapları getir
    List<InstantAccount> findByAuthorizedPersonnelContainingIgnoreCaseAndIsActiveTrue(String authorizedPerson);

}
