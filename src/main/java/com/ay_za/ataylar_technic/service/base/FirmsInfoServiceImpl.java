package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.FirmsInfoDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FirmsInfoServiceImpl {

    @Transactional
    FirmsInfoDto createFirm(String firmName, String createdBy);

    @Transactional
    FirmsInfoDto updateFirm(String firmId, String firmName, String updatedBy);

    @Transactional
    void deleteFirm(String firmId);

    Optional<FirmsInfoDto> getFirmById(String firmId);

    List<FirmsInfoDto> getAllFirms();

    List<FirmsInfoDto> searchFirmsByName(String searchTerm);

    boolean existsByFirmName(String firmName);

    Integer getFirmCount();

    boolean checkFirmById(String firmId);
}
