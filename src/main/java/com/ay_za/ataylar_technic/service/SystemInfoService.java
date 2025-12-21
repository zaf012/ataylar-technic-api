package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.SystemInfoDto;
import com.ay_za.ataylar_technic.entity.SystemInfo;
import com.ay_za.ataylar_technic.mapper.SystemInfoMapper;
import com.ay_za.ataylar_technic.repository.SystemInfoRepository;
import com.ay_za.ataylar_technic.service.base.SystemInfoServiceImpl;
import com.ay_za.ataylar_technic.service.request.ChecklistOrFaultCreateAndUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SystemInfoService implements SystemInfoServiceImpl {

    private final SystemInfoMapper systemInfoMapper;
    private final SystemInfoRepository systemInfoRepository;

    public SystemInfoService(SystemInfoMapper systemInfoMapper, SystemInfoRepository systemInfoRepository) {
        this.systemInfoMapper = systemInfoMapper;
        this.systemInfoRepository = systemInfoRepository;
    }

    // Sistem tanımları için metodlar
    @Override
    public List<SystemInfoDto> getAllSystems() {
        List<SystemInfo> systems = systemInfoRepository.findAllSystems();
        return systems.stream()
                .map(systemInfoMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemInfoDto> getActiveSystems() {
        List<SystemInfo> activeSystems = systemInfoRepository.findActiveSystemsOnly();
        return activeSystems.stream()
                .map(systemInfoMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SystemInfoDto createSystem(SystemInfoDto systemDto) {
        // Validation: systemName ve systemOrderNo zorunlu
        validateSystemName(systemDto.getSystemName());
        validateSystemOrderNo(systemDto.getSystemOrderNo());

        // Validation: Aynı systemName ve systemOrderNo kombinasyonu var mı kontrol et
        if (systemInfoRepository.existsBySystemNameAndSystemOrderNo(
                systemDto.getSystemName(), systemDto.getSystemOrderNo())) {
            throw new RuntimeException(String.format(
                    "Bu sistem adı ve sıra numarası zaten mevcut: %s - %d",
                    systemDto.getSystemName(), systemDto.getSystemOrderNo()
            ));
        }

        // Sistem tanımı için gerekli alanları set et
        systemDto.setCreatedBy("Admin"); // TODO: SecurityContext'ten al
        systemDto.setIsActive(systemDto.getIsActive() != null ? systemDto.getIsActive() : true);

        // Sistem tanımları için description, checklist, fault NULL olmalı
        systemDto.setDescription(null);
        systemDto.setIsChecklist(null);
        systemDto.setIsFault(null);
        systemDto.setControlPointOrder(null);
        systemDto.setControlPointIsActive(null);

        SystemInfo savedSystem = systemInfoRepository.save(systemInfoMapper.convertToEntity(systemDto));
        return systemInfoMapper.convertToDTO(savedSystem);
    }

    @Override
    @Transactional
    public SystemInfoDto updateSystem(SystemInfoDto systemDto) {
        // Validation: ID zorunlu
        if (systemDto.getId() == null || systemDto.getId().isEmpty()) {
            throw new RuntimeException("Sistem ID boş olamaz");
        }

        SystemInfo existingSystem = systemInfoRepository.findSystemInfoById(systemDto.getId());
        if (existingSystem == null) {
            throw new RuntimeException("Sistem bulunamadı: " + systemDto.getId());
        }

        // Validation
        validateSystemName(systemDto.getSystemName());
        validateSystemOrderNo(systemDto.getSystemOrderNo());

        // Eski sistem adını sakla (çeklist/arıza güncellemesi için)
        String oldSystemName = existingSystem.getSystemName();
        boolean systemNameChanged = !oldSystemName.equals(systemDto.getSystemName());

        // systemName veya systemOrderNo değişmiş mi kontrol et
        boolean nameOrOrderChanged = systemNameChanged
                || !existingSystem.getSystemOrderNo().equals(systemDto.getSystemOrderNo());

        if (nameOrOrderChanged) {
            // Başka bir kayıtta aynı kombinasyon var mı kontrol et
            if (systemInfoRepository.existsBySystemNameAndSystemOrderNoExcludingId(
                    systemDto.getSystemName(), systemDto.getSystemOrderNo(), systemDto.getId())) {
                throw new RuntimeException(String.format(
                        "Bu sistem adı ve sıra numarası başka bir kayıtta mevcut: %s - %d",
                        systemDto.getSystemName(), systemDto.getSystemOrderNo()
                ));
            }
        }

        // Alanları güncelle
        existingSystem.setSystemName(systemDto.getSystemName());
        existingSystem.setSystemOrderNo(systemDto.getSystemOrderNo());
        existingSystem.setIsActive(systemDto.getIsActive() != null ? systemDto.getIsActive() : existingSystem.getIsActive());
        existingSystem.setUpdatedBy("Admin"); // TODO: SecurityContext'ten al

        SystemInfo updatedSystem = systemInfoRepository.save(existingSystem);

        // Sistem adı değiştiyse, bu sistem adını kullanan çeklist/arıza maddelerini de güncelle
        if (systemNameChanged) {
            updateChecklistAndFaultSystemNames(oldSystemName, systemDto.getSystemName());
        }

        return systemInfoMapper.convertToDTO(updatedSystem);
    }

    @Override
    @Transactional
    public void deleteSystem(String id) {
        systemInfoRepository.deleteById(id);
    }

    @Override
    public SystemInfoDto getSystemById(String id) {
        SystemInfo system = systemInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sistem bulunamadı: " + id));
        return systemInfoMapper.convertToDTO(system);
    }

    /**
     * Sistem ID'ye göre sistem entity getir
     */
    public Optional<SystemInfo> getSystemEntityById(String id) {
        return systemInfoRepository.findById(id);
    }

    // Çeklist/Arıza maddeleri için metodlar
    @Override
    public List<SystemInfoDto> getChecklistsBySystemName(String systemName) {
        List<SystemInfo> checklists = systemInfoRepository.findChecklistsBySystemName(systemName);
        return checklists.stream()
                .map(systemInfoMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemInfoDto> getFaultsBySystemName(String systemName) {
        List<SystemInfo> faults = systemInfoRepository.findFaultsBySystemName(systemName);
        return faults.stream()
                .map(systemInfoMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemInfoDto> getActiveChecklistsBySystemName(String systemName) {
        List<SystemInfo> activeChecklists = systemInfoRepository.findActiveChecklistsBySystemName(systemName);
        return activeChecklists.stream()
                .map(systemInfoMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemInfoDto> getActiveFaultsBySystemName(String systemName) {
        List<SystemInfo> activeFaults = systemInfoRepository.findActiveFaultsBySystemName(systemName);
        return activeFaults.stream()
                .map(systemInfoMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SystemInfoDto createFaultOrChecklist(ChecklistOrFaultCreateAndUpdateRequest request) {
        // Validation: systemId zorunlu
        if (request.getSystemId() == null || request.getSystemId().isEmpty()) {
            throw new RuntimeException("Sistem ID boş olamaz");
        }

        // Önce sistem ID'si ile sistemi bul (validasyon için)
        SystemInfo systemInfo = systemInfoRepository.findById(request.getSystemId())
                .orElseThrow(() -> new RuntimeException("Sistem bulunamadı: " + request.getSystemId()));

        // Validation: description zorunlu
        validateDescription(request.getDescription());

        // Validation: controlPointOrder zorunlu
        validateControlPointOrder(request.getControlPointOrder());

        // Validation: isChecklist veya isFault birisi true olmalı
        if (!Boolean.TRUE.equals(request.getChecklist()) && !Boolean.TRUE.equals(request.getFault())) {
            throw new RuntimeException("Çeklist veya Arıza seçeneklerinden birisi seçilmelidir");
        }

        // Validation: Aynı sistemde aynı controlPointOrder var mı kontrol et
        if (systemInfoRepository.existsBySystemNameAndControlPointOrder(
                systemInfo.getSystemName(), request.getControlPointOrder())) {
            throw new RuntimeException(String.format(
                    "Bu sistemde (%s) aynı kontrol noktası sırası zaten mevcut: %d",
                    systemInfo.getSystemName(), request.getControlPointOrder()
            ));
        }

        // YENİ SystemInfo nesnesi oluştur (mevcut kaydı DEĞİŞTİRME!)
        SystemInfo newChecklistOrFault = new SystemInfo();

        // Sistem bilgilerini kopyala
        newChecklistOrFault.setSystemName(systemInfo.getSystemName());
        newChecklistOrFault.setSystemOrderNo(systemInfo.getSystemOrderNo());
        newChecklistOrFault.setIsActive(systemInfo.getIsActive());

        // Çeklist/Arıza bilgilerini set et
        newChecklistOrFault.setDescription(request.getDescription());
        newChecklistOrFault.setIsChecklist(request.getChecklist());
        newChecklistOrFault.setIsFault(request.getFault());
        newChecklistOrFault.setControlPointOrder(request.getControlPointOrder());
        newChecklistOrFault.setControlPointIsActive(request.getControlPointIsActive() != null ? request.getControlPointIsActive() : true);

        // Audit alanları
        newChecklistOrFault.setCreatedBy("Admin"); // TODO: SecurityContext'ten al

        // Kaydet
        SystemInfo savedChecklistOrFault = systemInfoRepository.save(newChecklistOrFault);
        return systemInfoMapper.convertToDTO(savedChecklistOrFault);
    }

    // Güncelleme metodları
    @Transactional
    @Override
    public SystemInfoDto updateChecklistOrFault(ChecklistOrFaultCreateAndUpdateRequest request) {
        // Validation: ID gerekli
        if (request.getId() == null || request.getId().isEmpty()) {
            throw new RuntimeException("Çeklist/Arıza ID'si boş olamaz");
        }

        SystemInfo existingChecklistOrFault = systemInfoRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Çeklist/Arıza kaydı bulunamadı: " + request.getId()));

        // Validation
        validateDescription(request.getDescription());
        validateControlPointOrder(request.getControlPointOrder());

        // controlPointOrder değişmiş mi kontrol et
        if (!existingChecklistOrFault.getControlPointOrder().equals(request.getControlPointOrder())) {
            // Başka bir kayıtta aynı sistem ve controlPointOrder var mı kontrol et
            if (systemInfoRepository.existsBySystemNameAndControlPointOrderExcludingId(
                    existingChecklistOrFault.getSystemName(),
                    request.getControlPointOrder(),
                    request.getId())) {
                throw new RuntimeException(String.format(
                        "Bu sistemde (%s) aynı kontrol noktası sırası başka bir kayıtta mevcut: %d",
                        existingChecklistOrFault.getSystemName(),
                        request.getControlPointOrder()
                ));
            }
        }

        // Alanları güncelle
        existingChecklistOrFault.setDescription(request.getDescription());
        existingChecklistOrFault.setIsChecklist(request.getChecklist());
        existingChecklistOrFault.setIsFault(request.getFault());
        existingChecklistOrFault.setControlPointOrder(request.getControlPointOrder());
        existingChecklistOrFault.setControlPointIsActive(request.getControlPointIsActive() != null ? request.getControlPointIsActive() : true);
        existingChecklistOrFault.setUpdatedBy("Admin"); // TODO: SecurityContext'ten al

        SystemInfo updatedChecklistOrFault = systemInfoRepository.save(existingChecklistOrFault);
        return systemInfoMapper.convertToDTO(updatedChecklistOrFault);
    }

    // Silme metodları
    @Override
    @Transactional
    public void deleteChecklist(String id) {
        SystemInfo checklist = systemInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Çeklist bulunamadı: " + id));

        if (!Boolean.TRUE.equals(checklist.getIsChecklist())) {
            throw new RuntimeException("Bu kayıt bir çeklist değil");
        }

        systemInfoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteFault(String id) {
        SystemInfo fault = systemInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arıza bulunamadı: " + id));

        if (!Boolean.TRUE.equals(fault.getIsFault())) {
            throw new RuntimeException("Bu kayıt bir arıza değil");
        }

        systemInfoRepository.deleteById(id);
    }

    // İstatistik metodları
    @Override
    public Long getChecklistCountBySystemName(String systemName) {
        return systemInfoRepository.countChecklistsBySystemName(systemName);
    }

    @Override
    public Long getFaultCountBySystemName(String systemName) {
        return systemInfoRepository.countFaultsBySystemName(systemName);
    }

    // Yardımcı metodlar
    @Override
    public List<String> getAllSystemNames() {
        return systemInfoRepository.findDistinctSystemNames();
    }

    @Override
    public List<String> getActiveChecklistSystemNames() {
        return systemInfoRepository.findDistinctActiveChecklistSystemNames();
    }

    @Override
    public List<SystemInfoDto> getAllBySystemName(String systemName) {
        List<SystemInfo> allRecords = systemInfoRepository.findAllBySystemName(systemName);
        return allRecords.stream()
                .map(systemInfoMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    // ===== Private Helper Methods =====

    /**
     * Belirli bir sistem adını kullanan tüm çeklist ve arıza maddelerinin sistem adını günceller
     */
    private void updateChecklistAndFaultSystemNames(String oldSystemName, String newSystemName) {
        // Eski sistem adına sahip tüm çeklist ve arıza maddelerini bul
        List<SystemInfo> checklistsAndFaults = systemInfoRepository.findAllBySystemName(oldSystemName);

        // Sadece çeklist ve arıza maddelerini filtrele (description dolu olanlar)
        List<SystemInfo> itemsToUpdate = checklistsAndFaults.stream()
                .filter(item -> item.getDescription() != null && !item.getDescription().trim().isEmpty())
                .collect(Collectors.toList());

        // Her birinin sistem adını güncelle
        if (!itemsToUpdate.isEmpty()) {
            itemsToUpdate.forEach(item -> {
                item.setSystemName(newSystemName);
                item.setUpdatedBy("Admin"); // TODO: SecurityContext'ten al
            });

            // Toplu güncelleme
            systemInfoRepository.saveAll(itemsToUpdate);
        }
    }

    private void validateSystemName(String systemName) {
        if (systemName == null || systemName.trim().isEmpty()) {
            throw new RuntimeException("Sistem adı boş olamaz");
        }
        if (systemName.length() > 200) {
            throw new RuntimeException("Sistem adı 200 karakterden uzun olamaz");
        }
    }

    private void validateSystemOrderNo(Integer systemOrderNo) {
        if (systemOrderNo == null) {
            throw new RuntimeException("Sistem sıra numarası boş olamaz");
        }
        if (systemOrderNo < 0) {
            throw new RuntimeException("Sistem sıra numarası negatif olamaz");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new RuntimeException("Açıklama boş olamaz");
        }
        if (description.length() > 500) {
            throw new RuntimeException("Açıklama 500 karakterden uzun olamaz");
        }
    }

    private void validateControlPointOrder(Integer controlPointOrder) {
        if (controlPointOrder == null) {
            throw new RuntimeException("Kontrol noktası sırası boş olamaz");
        }
        if (controlPointOrder < 0) {
            throw new RuntimeException("Kontrol noktası sırası negatif olamaz");
        }
    }
}
