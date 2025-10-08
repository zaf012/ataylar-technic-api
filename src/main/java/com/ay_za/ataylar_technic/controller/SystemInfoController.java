package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.SystemInfoDto;
import com.ay_za.ataylar_technic.service.SystemInfoService;
import com.ay_za.ataylar_technic.service.request.ChecklistOrFaultCreateAndUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system-info")
@CrossOrigin(origins = "*")
@Tag(name = "System Info", description = "Sistem yönetimi ve çeklist/arıza tanımları")
public class SystemInfoController {

    private final SystemInfoService systemInfoService;

    public SystemInfoController(SystemInfoService systemInfoService) {
        this.systemInfoService = systemInfoService;
    }

    // ===== Sistem Tanımları Endpoints =====

    @GetMapping("/get-all-systems")
    @Operation(summary = "Tüm sistemleri getir", description = "Sistemlerin temel bilgilerini döner")
    public ResponseEntity<List<SystemInfoDto>> getAllSystems() {
        List<SystemInfoDto> systems = systemInfoService.getAllSystems();
        return ResponseEntity.ok(systems);
    }

    @GetMapping("/systems/active")
    @Operation(summary = "Aktif sistemleri getir", description = "Sadece aktif olan sistemleri döner")
    public ResponseEntity<List<SystemInfoDto>> getActiveSystems() {
        List<SystemInfoDto> activeSystems = systemInfoService.getActiveSystems();
        return ResponseEntity.ok(activeSystems);
    }

    @GetMapping("/systems/{id}")
    @Operation(summary = "ID ile sistem getir", description = "Belirtilen ID'ye sahip sistemi döner")
    public ResponseEntity<SystemInfoDto> getSystemById(
            @Parameter(description = "Sistem ID'si") @PathVariable String id) {
        SystemInfoDto system = systemInfoService.getSystemById(id);
        return ResponseEntity.ok(system);
    }

    @PostMapping("/systems")
    @Operation(summary = "Yeni sistem oluştur", description = "Sistem adı, sıra no ve aktiflik durumu ile yeni sistem oluşturur")
    public ResponseEntity<SystemInfoDto> createSystem(@RequestBody SystemInfoDto systemDto) {
        SystemInfoDto createdSystem = systemInfoService.createSystem(systemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSystem);
    }

    @PutMapping("/systems")
    @Operation(summary = "Sistem güncelle", description = "Mevcut sistemi günceller")
    public ResponseEntity<SystemInfoDto> updateSystem(@RequestBody SystemInfoDto systemDto) {
        SystemInfoDto updatedSystem = systemInfoService.updateSystem(systemDto);
        return ResponseEntity.ok(updatedSystem);
    }

    @DeleteMapping("/systems/{id}")
    @Operation(summary = "Sistem sil", description = "Belirtilen sistemi siler")
    public ResponseEntity<Void> deleteSystem(
            @Parameter(description = "Sistem ID'si") @PathVariable String id) {
        systemInfoService.deleteSystem(id);
        return ResponseEntity.noContent().build();
    }

    // ===== Çeklist/Arıza Endpoints =====

    @GetMapping("/systems/{systemName}/checklists")
    @Operation(summary = "Sistem çeklist maddeleri", description = "Belirtilen sisteme ait çeklist maddelerini döner")
    public ResponseEntity<List<SystemInfoDto>> getChecklistsBySystemName(
            @Parameter(description = "Sistem adı") @PathVariable String systemName) {
        List<SystemInfoDto> checklists = systemInfoService.getChecklistsBySystemName(systemName);
        return ResponseEntity.ok(checklists);
    }

    @GetMapping("/systems/{systemName}/faults")
    @Operation(summary = "Sistem arıza maddeleri", description = "Belirtilen sisteme ait arıza maddelerini döner")
    public ResponseEntity<List<SystemInfoDto>> getFaultsBySystemName(
            @Parameter(description = "Sistem adı") @PathVariable String systemName) {
        List<SystemInfoDto> faults = systemInfoService.getFaultsBySystemName(systemName);
        return ResponseEntity.ok(faults);
    }

    @GetMapping("/systems/{systemName}/checklists/active")
    @Operation(summary = "Aktif çeklist maddeleri", description = "Belirtilen sisteme ait aktif çeklist maddelerini döner")
    public ResponseEntity<List<SystemInfoDto>> getActiveChecklistsBySystemName(
            @Parameter(description = "Sistem adı") @PathVariable String systemName) {
        List<SystemInfoDto> activeChecklists = systemInfoService.getActiveChecklistsBySystemName(systemName);
        return ResponseEntity.ok(activeChecklists);
    }

    @GetMapping("/systems/{systemName}/faults/active")
    @Operation(summary = "Aktif arıza maddeleri", description = "Belirtilen sisteme ait aktif arıza maddelerini döner")
    public ResponseEntity<List<SystemInfoDto>> getActiveFaultsBySystemName(
            @Parameter(description = "Sistem adı") @PathVariable String systemName) {
        List<SystemInfoDto> activeFaults = systemInfoService.getActiveFaultsBySystemName(systemName);
        return ResponseEntity.ok(activeFaults);
    }

    @PostMapping("/checklists-faults")
    @Operation(summary = "Çeklist/Arıza oluştur", description = "Sistem ID'si ile yeni çeklist veya arıza maddesi oluşturur")
    public ResponseEntity<SystemInfoDto> createChecklistOrFault(@RequestBody ChecklistOrFaultCreateAndUpdateRequest request) {
        SystemInfoDto created = systemInfoService.createFaultOrChecklist(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/checklists-faults")
    @Operation(summary = "Çeklist/Arıza güncelle", description = "Mevcut çeklist veya arıza maddesini günceller")
    public ResponseEntity<SystemInfoDto> updateChecklistOrFault(@RequestBody ChecklistOrFaultCreateAndUpdateRequest request) {
        SystemInfoDto updated = systemInfoService.updateChecklistOrFault(request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/checklists/{id}")
    @Operation(summary = "Çeklist sil", description = "Belirtilen çeklist maddesini siler")
    public ResponseEntity<Void> deleteChecklist(
            @Parameter(description = "Çeklist ID'si") @PathVariable String id) {
        systemInfoService.deleteChecklist(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/faults/{id}")
    @Operation(summary = "Arıza sil", description = "Belirtilen arıza maddesini siler")
    public ResponseEntity<Void> deleteFault(
            @Parameter(description = "Arıza ID'si") @PathVariable String id) {
        systemInfoService.deleteFault(id);
        return ResponseEntity.noContent().build();
    }

    // ===== İstatistik ve Yardımcı Endpoints =====

    @GetMapping("/systems/{systemName}/checklists/count")
    @Operation(summary = "Çeklist sayısı", description = "Belirtilen sisteme ait çeklist sayısını döner")
    public ResponseEntity<Long> getChecklistCount(
            @Parameter(description = "Sistem adı") @PathVariable String systemName) {
        Long count = systemInfoService.getChecklistCountBySystemName(systemName);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/systems/{systemName}/faults/count")
    @Operation(summary = "Arıza sayısı", description = "Belirtilen sisteme ait arıza sayısını döner")
    public ResponseEntity<Long> getFaultCount(
            @Parameter(description = "Sistem adı") @PathVariable String systemName) {
        Long count = systemInfoService.getFaultCountBySystemName(systemName);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/system-names")
    @Operation(summary = "Sistem adları", description = "Tüm sistem adlarını döner")
    public ResponseEntity<List<String>> getAllSystemNames() {
        List<String> systemNames = systemInfoService.getAllSystemNames();
        return ResponseEntity.ok(systemNames);
    }

    @GetMapping("/systems/{systemName}/all")
    @Operation(summary = "Sistem ve tüm maddeleri", description = "Belirtilen sistem ve ona ait tüm çeklist/arıza maddelerini döner")
    public ResponseEntity<List<SystemInfoDto>> getAllBySystemName(
            @Parameter(description = "Sistem adı") @PathVariable String systemName) {
        List<SystemInfoDto> allRecords = systemInfoService.getAllBySystemName(systemName);
        return ResponseEntity.ok(allRecords);
    }

    // ===== Dummy Data Endpoint =====

    @PostMapping("/create-default-data")
    @Operation(
        summary = "Örnek sistem verileri oluştur",
        description = "Sistemler ve bu sistemlere ait örnek çeklist/arıza maddelerini oluşturur. " +
                     "Su Arıtmaları, Hava Perdeleri, Isı Geri Kazanım sistemleri vs. ile " +
                     "her sistem için gerçekçi çeklist ve arıza maddeleri ekler."
    )
    public ResponseEntity<String> createDefaultData() {
        String result = systemInfoService.createDefaultSystemsAndData();
        return ResponseEntity.ok(result);
    }
}
