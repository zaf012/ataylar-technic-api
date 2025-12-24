package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.ServiceCaseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ServiceCaseServiceImpl {

    /**
     * Tüm service case kayıtlarını getir
     */
    List<ServiceCaseDto> getAll();

    /**
     * ID'ye göre service case getir
     */
    Optional<ServiceCaseDto> getById(String id);

    /**
     * Yeni service case oluştur
     */
    @Transactional
    ServiceCaseDto create(ServiceCaseDto serviceCaseDto);

    /**
     * Service case güncelle
     */
    @Transactional
    ServiceCaseDto update(String id, ServiceCaseDto serviceCaseDto);

    /**
     * Service case sil
     */
    @Transactional
    void delete(String id);

    /**
     * Service case name'e göre kayıt getir
     */
    Optional<ServiceCaseDto> getByServiceCaseName(String serviceCaseName);
}

