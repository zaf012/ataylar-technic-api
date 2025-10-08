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
import java.util.ArrayList;
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
        systemDto.setDescription(systemDto.getDescription()); // Sistem tanımları için boş
        systemDto.setIsChecklist(systemDto.getIsChecklist());
        systemDto.setIsFault(systemDto.getIsFault());
        systemDto.setControlPointOrder(systemDto.getControlPointOrder());
        systemDto.setControlPointIsActive(systemDto.getControlPointIsActive());
        systemDto.setSystemName(systemDto.getSystemName());

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
    public SystemInfoDto updateSystem(SystemInfoDto systemDto) {
        SystemInfo existingSystem = systemInfoRepository.findById(systemDto.getId())
                .orElseThrow(() -> new RuntimeException("Sistem bulunamadı: " + systemDto.getId()));

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

    // ===== DUMMY DATA METHODS =====

    public String createDefaultSystemsAndData() {
        try {
            List<SystemInfoDto> createdSystems = createDefaultSystems();
            createDefaultChecklistsAndFaults(createdSystems);

            return "Dummy veriler başarıyla oluşturuldu! " +
                    "Toplam " + createdSystems.size() + " sistem ve " +
                    "her sistem için örnek çeklist ve arıza maddeleri eklendi.";
        } catch (Exception e) {
            return "Dummy veri oluşturulurken hata: " + e.getMessage();
        }
    }

    private List<SystemInfoDto> createDefaultSystems() {
        List<SystemInfoDto> systems = new ArrayList<>();

        // 1. Su Arıtma Sistemi
        SystemInfoDto suAritma = new SystemInfoDto();
        suAritma.setSystemName("Su Arıtmaları");
        suAritma.setSystemOrderNo(500);
        suAritma.setIsActive(true);
        suAritma.setDescription("Su arıtma ve filtre sistemleri");
        suAritma.setIsChecklist(false);
        suAritma.setIsFault(false);
        suAritma.setControlPointOrder(null);
        suAritma.setControlPointIsActive(null);
        suAritma.setCreatedDate(LocalDateTime.now());
        suAritma.setUpdatedDate(LocalDateTime.now());
        suAritma.setCreatedBy("System");
        suAritma.setUpdatedBy("System");
        systems.add(this.createSystem(suAritma));

        // 2. Hava Perdesi Sistemi
        SystemInfoDto havaPerdesi = new SystemInfoDto();
        havaPerdesi.setSystemName("Hava Perdeleri");
        havaPerdesi.setSystemOrderNo(490);
        havaPerdesi.setIsActive(true);
        havaPerdesi.setDescription("Hava perdesi fanları ve kontrol sistemleri");
        havaPerdesi.setIsChecklist(false);
        havaPerdesi.setIsFault(false);
        havaPerdesi.setControlPointOrder(null);
        havaPerdesi.setControlPointIsActive(null);
        havaPerdesi.setCreatedDate(LocalDateTime.now());
        havaPerdesi.setUpdatedDate(LocalDateTime.now());
        havaPerdesi.setCreatedBy("System");
        havaPerdesi.setUpdatedBy("System");
        systems.add(this.createSystem(havaPerdesi));

        // 3. Isı Geri Kazanım Sistemi
        SystemInfoDto isiGeriKazanim = new SystemInfoDto();
        isiGeriKazanim.setSystemName("Isı Geri Kazanım Taze Hava Fanları");
        isiGeriKazanim.setSystemOrderNo(480);
        isiGeriKazanim.setIsActive(true);
        isiGeriKazanim.setDescription("Enerji verimliliği için ısı geri kazanım fan sistemleri");
        isiGeriKazanim.setIsChecklist(false);
        isiGeriKazanim.setIsFault(false);
        isiGeriKazanim.setControlPointOrder(null);
        isiGeriKazanim.setControlPointIsActive(null);
        isiGeriKazanim.setCreatedDate(LocalDateTime.now());
        isiGeriKazanim.setUpdatedDate(LocalDateTime.now());
        isiGeriKazanim.setCreatedBy("System");
        isiGeriKazanim.setUpdatedBy("System");
        systems.add(this.createSystem(isiGeriKazanim));

        // 4. Isı Geri Kazanım Cihazları
        SystemInfoDto isiGeriKazanimCihazlari = new SystemInfoDto();
        isiGeriKazanimCihazlari.setSystemName("Isı Geri kazanım Cihazları");
        isiGeriKazanimCihazlari.setSystemOrderNo(470);
        isiGeriKazanimCihazlari.setIsActive(true);
        isiGeriKazanimCihazlari.setDescription("Merkezi ısı geri kazanım cihazları ve ekipmanları");
        isiGeriKazanimCihazlari.setIsChecklist(false);
        isiGeriKazanimCihazlari.setIsFault(false);
        isiGeriKazanimCihazlari.setControlPointOrder(null);
        isiGeriKazanimCihazlari.setControlPointIsActive(null);
        isiGeriKazanimCihazlari.setCreatedDate(LocalDateTime.now());
        isiGeriKazanimCihazlari.setUpdatedDate(LocalDateTime.now());
        isiGeriKazanimCihazlari.setCreatedBy("System");
        isiGeriKazanimCihazlari.setUpdatedBy("System");
        systems.add(this.createSystem(isiGeriKazanimCihazlari));

        // 5. Osmoz Cihazları
        SystemInfoDto osmozCihazlari = new SystemInfoDto();
        osmozCihazlari.setSystemName("Osmoz Cihazları");
        osmozCihazlari.setSystemOrderNo(510);
        osmozCihazlari.setIsActive(true);
        osmozCihazlari.setDescription("Ters osmoz su arıtma cihazları ve membran sistemleri");
        osmozCihazlari.setIsChecklist(false);
        osmozCihazlari.setIsFault(false);
        osmozCihazlari.setControlPointOrder(null);
        osmozCihazlari.setControlPointIsActive(null);
        osmozCihazlari.setCreatedDate(LocalDateTime.now());
        osmozCihazlari.setUpdatedDate(LocalDateTime.now());
        osmozCihazlari.setCreatedBy("System");
        osmozCihazlari.setUpdatedBy("System");
        systems.add(this.createSystem(osmozCihazlari));

        // 6. İntercomlar
        SystemInfoDto intercomlar = new SystemInfoDto();
        intercomlar.setSystemName("İntercomlar");
        intercomlar.setSystemOrderNo(520);
        intercomlar.setIsActive(true);
        intercomlar.setDescription("İç ve dış interkom sistemleri");
        intercomlar.setIsChecklist(false);
        intercomlar.setIsFault(false);
        intercomlar.setControlPointOrder(null);
        intercomlar.setControlPointIsActive(null);
        intercomlar.setCreatedDate(LocalDateTime.now());
        intercomlar.setUpdatedDate(LocalDateTime.now());
        intercomlar.setCreatedBy("System");
        intercomlar.setUpdatedBy("System");
        systems.add(this.createSystem(intercomlar));

        // 7. Blok Yağ Pislik Tutucuları
        SystemInfoDto blokYagPislik = new SystemInfoDto();
        blokYagPislik.setSystemName("Blok Yağ Pislik Tutucuları");
        blokYagPislik.setSystemOrderNo(530);
        blokYagPislik.setIsActive(true);
        blokYagPislik.setDescription("Mutfak atık su yağ ve pislik ayırma sistemleri");
        blokYagPislik.setIsChecklist(false);
        blokYagPislik.setIsFault(false);
        blokYagPislik.setControlPointOrder(null);
        blokYagPislik.setControlPointIsActive(null);
        blokYagPislik.setCreatedDate(LocalDateTime.now());
        blokYagPislik.setUpdatedDate(LocalDateTime.now());
        blokYagPislik.setCreatedBy("System");
        blokYagPislik.setUpdatedBy("System");
        systems.add(this.createSystem(blokYagPislik));

        return systems;
    }

    private void createDefaultChecklistsAndFaults(List<SystemInfoDto> systems) {
        for (SystemInfoDto system : systems) {
            createChecklistsForSystem(system);
            createFaultsForSystem(system);
        }
    }

    private void createChecklistsForSystem(SystemInfoDto system) {
        String systemName = system.getSystemName();
        String systemId = system.getId();

        switch (systemName) {
            case "Su Arıtmaları":
                createSuAritmaChecklists(systemId);
                break;
            case "Hava Perdeleri":
                createHavaPerdeleriChecklists(systemId);
                break;
            case "Isı Geri Kazanım Taze Hava Fanları":
                createIsiGeriKazanimChecklists(systemId);
                break;
            case "Isı Geri kazanım Cihazları":
                createIsiGeriKazanimCihazlariChecklists(systemId);
                break;
            case "Osmoz Cihazları":
                createOsmozCihazlariChecklists(systemId);
                break;
            case "İntercomlar":
                createIntercomlarChecklists(systemId);
                break;
            case "Blok Yağ Pislik Tutucuları":
                createBlokYagPislikChecklists(systemId);
                break;
        }
    }

    private void createFaultsForSystem(SystemInfoDto system) {
        String systemName = system.getSystemName();
        String systemId = system.getId();

        switch (systemName) {
            case "Su Arıtmaları":
                createSuAritmaFaults(systemId);
                break;
            case "Hava Perdeleri":
                createHavaPerdeseriFaults(systemId);
                break;
            case "Isı Geri Kazanım Taze Hava Fanları":
                createIsiGeriKazanimFaults(systemId);
                break;
            case "Isı Geri kazanım Cihazları":
                createIsiGeriKazanimCihazlariFaults(systemId);
                break;
            case "Osmoz Cihazları":
                createOsmozCihazlariFaults(systemId);
                break;
            case "İntercomlar":
                createIntercomlarFaults(systemId);
                break;
            case "Blok Yağ Pislik Tutucuları":
                createBlokYagPislikFaults(systemId);
                break;
        }
    }

    // Su Arıtmaları Çeklist Maddeleri
    private void createSuAritmaChecklists(String systemId) {
        createChecklistItem(systemId, "Sistemde periyodik bakım yapılacağı projeye bilgi verildi mi ?", 10);
        createChecklistItem(systemId, "Bakım öncesi tüm vanalar kapatıldı mı ?", 20);
        createChecklistItem(systemId, "Filtrelerin içinde bulunduğu aksamların açılması ve bol su ile temizlenmesi yapıldı mı ?", 30);
        createChecklistItem(systemId, "Yeni filtrelerin aksamlara tekrar yerleştirilmesi ve hava almayacak şekilde kapatılması yapıldı mı ?", 40);
        createChecklistItem(systemId, "Tatlandırıcının değiştirilmesi yapıldı mı ?", 50);
    }

    // Su Arıtmaları Arıza Maddeleri
    private void createSuAritmaFaults(String systemId) {
        createFaultItem(systemId, "Su arıtma cihazlarının elektrik fişleri kontrol edildi mi ?", 10);
        createFaultItem(systemId, "Su arıtma cihazları çalışır durumda mı ?", 20);
        createFaultItem(systemId, "Su arıtma cihazları temiz mi ?", 30);
    }

    // Hava Perdeleri Çeklist Maddeleri
    private void createHavaPerdeleriChecklists(String systemId) {
        createChecklistItem(systemId, "Hava perdesi fan motorları kontrol edildi mi ?", 10);
        createChecklistItem(systemId, "Hava perdesi elektrik bağlantıları kontrol edildi mi ?", 20);
        createChecklistItem(systemId, "Hava perdesi filtreler temizlendi mi ?", 30);
    }

    // Hava Perdeleri Arıza Maddeleri
    private void createHavaPerdeseriFaults(String systemId) {
        createFaultItem(systemId, "Hava perdesi çalışmıyor", 10);
        createFaultItem(systemId, "Hava perdesi ses yapıyor", 20);
        createFaultItem(systemId, "Hava perdesi titreşim yapıyor", 30);
    }

    // Isı Geri Kazanım Taze Hava Fanları Çeklist
    private void createIsiGeriKazanimChecklists(String systemId) {
        createChecklistItem(systemId, "Fan motorları kontrol edildi mi ?", 10);
        createChecklistItem(systemId, "Kayış gerginlik kontrolleri yapıldı mı ?", 20);
        createChecklistItem(systemId, "Filtreler temizlendi mi ?", 30);
    }

    // Isı Geri Kazanım Taze Hava Fanları Arıza
    private void createIsiGeriKazanimFaults(String systemId) {
        createFaultItem(systemId, "Fan çalışmıyor", 10);
        createFaultItem(systemId, "Kayış kopmuş", 20);
        createFaultItem(systemId, "Aşırı titreşim var", 30);
    }

    // Isı Geri Kazanım Cihazları Çeklist
    private void createIsiGeriKazanimCihazlariChecklists(String systemId) {
        createChecklistItem(systemId, "Cihaz temizliği yapıldı mı ?", 10);
        createChecklistItem(systemId, "Elektriksel kontroller yapıldı mı ?", 20);
        createChecklistItem(systemId, "Performans ölçümleri alındı mı ?", 30);
    }

    // Isı Geri Kazanım Cihazları Arıza
    private void createIsiGeriKazanimCihazlariFaults(String systemId) {
        createFaultItem(systemId, "Cihaz çalışmıyor", 10);
        createFaultItem(systemId, "Verimlilik düşük", 20);
        createFaultItem(systemId, "Elektrik arızası", 30);
    }

    // Osmoz Cihazları Çeklist
    private void createOsmozCihazlariChecklists(String systemId) {
        createChecklistItem(systemId, "Membran temizliği yapıldı mı ?", 10);
        createChecklistItem(systemId, "Su basıncı kontrol edildi mi ?", 20);
        createChecklistItem(systemId, "Filtreler değiştirildi mi ?", 30);
    }

    // Osmoz Cihazları Arıza
    private void createOsmozCihazlariFaults(String systemId) {
        createFaultItem(systemId, "Su akışı yok", 10);
        createFaultItem(systemId, "Düşük basınç", 20);
        createFaultItem(systemId, "Membran tıkalı", 30);
    }

    // İntercomlar Çeklist
    private void createIntercomlarChecklists(String systemId) {
        createChecklistItem(systemId, "Ses kalitesi kontrol edildi mi ?", 10);
        createChecklistItem(systemId, "Buton çalışmaları test edildi mi ?", 20);
        createChecklistItem(systemId, "Bağlantı kabloları kontrol edildi mi ?", 30);
    }

    // İntercomlar Arıza
    private void createIntercomlarFaults(String systemId) {
        createFaultItem(systemId, "Ses gelmiyor", 10);
        createFaultItem(systemId, "Butonlar çalışmıyor", 20);
        createFaultItem(systemId, "Bağlantı kopuk", 30);
    }

    // Blok Yağ Pislik Tutucuları Çeklist
    private void createBlokYagPislikChecklists(String systemId) {
        createChecklistItem(systemId, "Yağ seviyeleri kontrol edildi mi ?", 10);
        createChecklistItem(systemId, "Temizlik işlemi yapıldı mı ?", 20);
        createChecklistItem(systemId, "Filtrelerin durumu kontrol edildi mi ?", 30);
    }

    // Blok Yağ Pislik Tutucuları Arıza
    private void createBlokYagPislikFaults(String systemId) {
        createFaultItem(systemId, "Yağ seviyesi yüksek", 10);
        createFaultItem(systemId, "Filtre tıkalı", 20);
        createFaultItem(systemId, "Temizlik gerekli", 30);
    }

    // Yardımcı metodlar
    private void createChecklistItem(String systemId, String description, Integer order) {
        SystemInfoDto checklistDto = new SystemInfoDto();
        checklistDto.setId(UUID.randomUUID().toString());
        checklistDto.setDescription(description);
        checklistDto.setControlPointOrder(order);
        checklistDto.setControlPointIsActive(true);
        checklistDto.setIsChecklist(true);
        checklistDto.setIsFault(false);

        // Parent sistemden bilgileri al
        SystemInfo parentSystem = systemInfoRepository.findById(systemId)
                .orElseThrow(() -> new RuntimeException("Sistem bulunamadı: " + systemId));
        checklistDto.setSystemName(parentSystem.getSystemName());
        checklistDto.setCreatedBy("System");

        SystemInfo checklistEntity = systemInfoMapper.convertToEntity(checklistDto);
        systemInfoRepository.save(checklistEntity);
    }

    private void createFaultItem(String systemId, String description, Integer order) {
        SystemInfoDto faultDto = new SystemInfoDto();
        faultDto.setId(UUID.randomUUID().toString());
        faultDto.setDescription(description);
        faultDto.setControlPointOrder(order);
        faultDto.setControlPointIsActive(true);
        faultDto.setIsChecklist(false);
        faultDto.setIsFault(true);

        // Parent sistemden bilgileri al
        SystemInfo parentSystem = systemInfoRepository.findById(systemId)
                .orElseThrow(() -> new RuntimeException("Sistem bulunamadı: " + systemId));
        faultDto.setSystemName(parentSystem.getSystemName());
        faultDto.setCreatedBy("System");

        SystemInfo faultEntity = systemInfoMapper.convertToEntity(faultDto);
        systemInfoRepository.save(faultEntity);
    }
}
