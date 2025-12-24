package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.ServiceCaseDto;
import com.ay_za.ataylar_technic.entity.ServiceCase;
import com.ay_za.ataylar_technic.exception.DataAlreadyExistsException;
import com.ay_za.ataylar_technic.exception.DataNotFoundException;
import com.ay_za.ataylar_technic.exception.ValidationException;
import com.ay_za.ataylar_technic.mapper.ServiceCaseMapper;
import com.ay_za.ataylar_technic.repository.ServiceCaseRepository;
import com.ay_za.ataylar_technic.service.base.ServiceCaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceCaseService implements ServiceCaseServiceImpl {

    private final ServiceCaseRepository serviceCaseRepository;
    private final ServiceCaseMapper serviceCaseMapper;

    public ServiceCaseService(ServiceCaseRepository serviceCaseRepository,
                              ServiceCaseMapper serviceCaseMapper) {
        this.serviceCaseRepository = serviceCaseRepository;
        this.serviceCaseMapper = serviceCaseMapper;
    }

    /**
     * Tüm service case kayıtlarını getir
     */
    @Override
    public List<ServiceCaseDto> getAll() {
        List<ServiceCase> serviceCases = serviceCaseRepository.findAll();
        return serviceCaseMapper.convertAllToDTO(serviceCases);
    }

    /**
     * ID'ye göre service case getir
     */
    @Override
    public Optional<ServiceCaseDto> getById(String id) {
        return serviceCaseRepository.findById(id)
                .map(serviceCaseMapper::convertToDTO);
    }

    /**
     * Yeni service case oluştur
     */
    @Override
    @Transactional
    public ServiceCaseDto create(ServiceCaseDto serviceCaseDto) {
        // Validation
        validateServiceCaseName(serviceCaseDto.getServiceCaseName());

        // Service case name uniqueness kontrolü
        if (serviceCaseRepository.existsByServiceCaseName(serviceCaseDto.getServiceCaseName())) {
            throw new DataAlreadyExistsException(
                "Bu hizmet koşulu adı zaten mevcut: " + serviceCaseDto.getServiceCaseName()
            );
        }

        // Entity'e çevir ve kaydet
        ServiceCase serviceCase = serviceCaseMapper.convertToEntity(serviceCaseDto);
        ServiceCase savedServiceCase = serviceCaseRepository.save(serviceCase);

        return serviceCaseMapper.convertToDTO(savedServiceCase);
    }

    /**
     * Service case güncelle
     */
    @Override
    @Transactional
    public ServiceCaseDto update(String id, ServiceCaseDto serviceCaseDto) {
        // Mevcut kaydı bul
        ServiceCase existingServiceCase = serviceCaseRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                    "Hizmet koşulu bulunamadı: " + id
                ));

        // Validation
        validateServiceCaseName(serviceCaseDto.getServiceCaseName());

        // Service case name değişmiş mi kontrol et
        if (!existingServiceCase.getServiceCaseName().equals(serviceCaseDto.getServiceCaseName())) {
            // Başka bir kayıtta aynı isim var mı kontrol et
            if (serviceCaseRepository.existsByServiceCaseName(serviceCaseDto.getServiceCaseName())) {
                throw new DataAlreadyExistsException(
                    "Bu hizmet koşulu adı başka bir kayıtta mevcut: " + serviceCaseDto.getServiceCaseName()
                );
            }
        }

        // Güncelle
        existingServiceCase.setServiceCaseName(serviceCaseDto.getServiceCaseName());
        ServiceCase updatedServiceCase = serviceCaseRepository.save(existingServiceCase);

        return serviceCaseMapper.convertToDTO(updatedServiceCase);
    }

    /**
     * Service case sil
     */
    @Override
    @Transactional
    public void delete(String id) {
        if (!serviceCaseRepository.existsById(id)) {
            throw new DataNotFoundException(
                "Hizmet koşulu bulunamadı: " + id
            );
        }

        serviceCaseRepository.deleteById(id);
    }

    /**
     * Service case name'e göre kayıt getir
     */
    @Override
    public Optional<ServiceCaseDto> getByServiceCaseName(String serviceCaseName) {
        return serviceCaseRepository.findByServiceCaseName(serviceCaseName)
                .map(serviceCaseMapper::convertToDTO);
    }

    // ===== Private Helper Methods =====

    private void validateServiceCaseName(String serviceCaseName) {
        if (serviceCaseName == null || serviceCaseName.trim().isEmpty()) {
            throw new ValidationException(
                "Hizmet koşulu adı boş olamaz"
            );
        }
        if (serviceCaseName.length() > 255) {
            throw new ValidationException(
                "Hizmet koşulu adı 255 karakterden uzun olamaz"
            );
        }
    }
}

