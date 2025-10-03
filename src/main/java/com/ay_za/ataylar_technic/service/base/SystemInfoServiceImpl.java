package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.SystemInfoDto;
import com.ay_za.ataylar_technic.service.request.ChecklistOrFaultCreateAndUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SystemInfoServiceImpl {

    // Sistem tanımları için metodlar
    List<SystemInfoDto> getAllSystems();
    List<SystemInfoDto> getActiveSystems();
    SystemInfoDto createSystem(SystemInfoDto systemDto);
    SystemInfoDto updateSystem(String id, SystemInfoDto systemDto);
    void deleteSystem(String id);
    SystemInfoDto getSystemById(String id);

    // Çeklist/Arıza maddeleri için metodlar
    List<SystemInfoDto> getChecklistsBySystemName(String systemName);
    List<SystemInfoDto> getFaultsBySystemName(String systemName);
    List<SystemInfoDto> getActiveChecklistsBySystemName(String systemName);
    List<SystemInfoDto> getActiveFaultsBySystemName(String systemName);

    // Çeklist/Arıza oluşturma metodları - sistem ID'si ile
    @Transactional
    SystemInfoDto createFaultOrChecklist(ChecklistOrFaultCreateAndUpdateRequest request);

    // Güncelleme metodları
    @Transactional
    SystemInfoDto updateChecklistOrFault(ChecklistOrFaultCreateAndUpdateRequest request);

    // Silme metodları
    void deleteChecklist(String id);
    void deleteFault(String id);

    // İstatistik metodları
    Long getChecklistCountBySystemName(String systemName);
    Long getFaultCountBySystemName(String systemName);

    // Yardımcı metodlar
    List<String> getAllSystemNames();
    List<SystemInfoDto> getAllBySystemName(String systemName);

}
