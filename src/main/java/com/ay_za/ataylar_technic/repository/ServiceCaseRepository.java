package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.ServiceCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceCaseRepository extends JpaRepository<ServiceCase, String> {

    /**
     * Service case name ile kayıt var mı kontrol et
     */
    boolean existsByServiceCaseName(String serviceCaseName);

    /**
     * Service case name ile kayıt bul
     */
    Optional<ServiceCase> findByServiceCaseName(String serviceCaseName);

    /**
     * Service case name ile kayıt bul (case-insensitive)
     */
    Optional<ServiceCase> findByServiceCaseNameIgnoreCase(String serviceCaseName);
}

