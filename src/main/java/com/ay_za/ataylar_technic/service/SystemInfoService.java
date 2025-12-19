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

        systemDto.setSystemName(systemDto.getSystemName());
        systemDto.setSystemOrderNo(systemDto.getSystemOrderNo());
        systemDto.setIsActive(systemDto.getIsActive());
        systemDto.setDescription(systemDto.getDescription());
        systemDto.setIsChecklist(systemDto.getIsChecklist());
        systemDto.setIsFault(systemDto.getIsFault());
        systemDto.setControlPointOrder(systemDto.getControlPointOrder());
        systemDto.setControlPointIsActive(systemDto.getIsActive());
        systemDto.setCreatedDate(LocalDateTime.now());
        systemDto.setUpdatedDate(LocalDateTime.now());
        systemDto.setCreatedBy("Admin");
        systemDto.setUpdatedBy("Admin");

        SystemInfo savedSystem = systemInfoRepository.save(systemInfoMapper.convertToEntity(systemDto));
        return systemInfoMapper.convertToDTO(savedSystem);
    }

    @Override
    @Transactional
    public SystemInfoDto updateSystem(SystemInfoDto systemDto) {
        SystemInfo existingSystem = systemInfoRepository.findSystemInfoById(systemDto.getId());

        if (existingSystem == null) {
            throw new RuntimeException("Sistem bulunamadı: " + systemDto.getId());
        }

        existingSystem.setSystemName(systemDto.getSystemName());
        existingSystem.setSystemOrderNo(systemDto.getSystemOrderNo());
        existingSystem.setActive(systemDto.getIsActive());
        existingSystem.setDescription(systemDto.getDescription());
        existingSystem.setChecklist(systemDto.getIsChecklist());
        existingSystem.setFault(systemDto.getIsFault());
        existingSystem.setControlPointOrder(systemDto.getControlPointOrder());
        existingSystem.setControlPointIsActive(systemDto.getControlPointIsActive());
        existingSystem.setCreatedDate(systemDto.getCreatedDate());
        existingSystem.setUpdatedDate(LocalDateTime.now());
        existingSystem.setCreatedBy(systemDto.getCreatedBy());
        existingSystem.setUpdatedBy("Admin");

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
        systemInfo.setCreatedBy("Admin");
        systemInfo.setUpdatedBy("Admin");

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
}
