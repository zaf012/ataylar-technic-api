package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.SystemInfoDto;
import com.ay_za.ataylar_technic.entity.SystemInfo;
import com.ay_za.ataylar_technic.mapper.SystemInfoMapper;
import com.ay_za.ataylar_technic.repository.SystemInfoRepository;
import com.ay_za.ataylar_technic.service.base.SystemInfoServiceImpl;
import com.ay_za.ataylar_technic.service.request.ChecklistOrFaultCreateAndUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
        // Sistem için ID oluştur
        systemDto.setId(UUID.randomUUID().toString());

        // Sistem sıra numarasını otomatik belirle eğer verilmemişse
        if (systemDto.getSystemOrderNo() == null) {
            Integer maxOrderNo = systemInfoRepository.findMaxSystemOrderNo();
            systemDto.setSystemOrderNo(maxOrderNo != null ? maxOrderNo + 10 : 10);
        }

        // Sistem tanımı için diğer alanları temizle/varsayılan değerler ata
        systemDto.setDescription(""); // Sistem tanımları için boş
        systemDto.setIsChecklist(false);
        systemDto.setIsFault(false);
        systemDto.setControlPointOrder(null);
        systemDto.setControlPointIsActive(null);

        // Aktiflik durumu verilmemişse varsayılan true
        if (systemDto.getIsActive() == null) {
            systemDto.setIsActive(true);
        }

        SystemInfo systemEntity = systemInfoMapper.convertToEntity(systemDto);
        SystemInfo savedSystem = systemInfoRepository.save(systemEntity);
        return systemInfoMapper.convertToDTO(savedSystem);
    }

    @Override
    @Transactional
    public SystemInfoDto updateSystem(String id, SystemInfoDto systemDto) {
        SystemInfo existingSystem = systemInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sistem bulunamadı: " + id));

        // Sadece sistem bilgilerini güncelle
        existingSystem.setSystemName(systemDto.getSystemName());
        existingSystem.setSystemOrderNo(systemDto.getSystemOrderNo());
        existingSystem.setActive(systemDto.getIsActive());
        existingSystem.setUpdatedBy(systemDto.getUpdatedBy());

        SystemInfo updatedSystem = systemInfoRepository.save(existingSystem);
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
        // Önce sistem ID'si ile sistemi bul
        SystemInfo systemInfo = systemInfoRepository.findById(request.getSystemId())
                .orElseThrow(() -> new RuntimeException("Sistem bulunamadı: " + request.getSystemId()));

        systemInfo.setDescription(request.getDescription());
        systemInfo.setChecklist(request.getChecklist());
        systemInfo.setFault(request.getFault());
        systemInfo.setControlPointOrder(request.getControlPointOrder());
        systemInfo.setControlPointIsActive(request.getControlPointIsActive() != null ? request.getControlPointIsActive() : true);
        systemInfo.setCreatedDate(LocalDateTime.now());
        systemInfo.setUpdatedDate(LocalDateTime.now());
        systemInfo.setCreatedBy("Admin"); // TODO: Bunu dinamik yap
        systemInfo.setUpdatedBy("Admin"); // TODO: Bunu dinamik yap

        SystemInfo savedFaultOrChecklistPoint = systemInfoRepository.save(systemInfo);
        return systemInfoMapper.convertToDTO(savedFaultOrChecklistPoint);
    }

    // Güncelleme metodları
    @Transactional
    @Override
    public SystemInfoDto updateChecklistOrFault(ChecklistOrFaultCreateAndUpdateRequest request) {
        SystemInfo existingChecklistOrFault = systemInfoRepository.findById(request.getSystemId())
                .orElseThrow(() -> new RuntimeException("Çeklist/Arıza kaydı bulunamadı: " + request.getSystemId()));

        existingChecklistOrFault.setDescription(request.getDescription());
        existingChecklistOrFault.setChecklist(request.getChecklist());
        existingChecklistOrFault.setFault(request.getFault());
        existingChecklistOrFault.setControlPointOrder(request.getControlPointOrder());
        existingChecklistOrFault.setControlPointIsActive(request.getControlPointIsActive() != null ? request.getControlPointIsActive() : true);
        existingChecklistOrFault.setUpdatedDate(LocalDateTime.now());
        existingChecklistOrFault.setUpdatedBy("Admin");

        SystemInfo updatedChecklistOrFault = systemInfoRepository.save(existingChecklistOrFault);
        return systemInfoMapper.convertToDTO(updatedChecklistOrFault);
    }

    // Silme metodları
    @Override // TODO : silme metodları güncellenecek.
    @Transactional
    public void deleteChecklist(String id) {
        SystemInfo checklist = systemInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Çeklist bulunamadı: " + id));

        if (!Boolean.TRUE.equals(checklist.getChecklist())) {
            throw new RuntimeException("Bu kayıt bir çeklist değil");
        }

        systemInfoRepository.deleteById(id);
    }

    @Override // TODO : silme metodları güncellenecek.
    @Transactional
    public void deleteFault(String id) {
        SystemInfo fault = systemInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arıza bulunamadı: " + id));

        if (!Boolean.TRUE.equals(fault.getFault())) {
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
    public List<SystemInfoDto> getAllBySystemName(String systemName) {
        List<SystemInfo> allRecords = systemInfoRepository.findAllBySystemName(systemName);
        return allRecords.stream()
                .map(systemInfoMapper::convertToDTO)
                .collect(Collectors.toList());
    }
}
