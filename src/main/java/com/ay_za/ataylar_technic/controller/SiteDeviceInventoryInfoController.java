package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.SiteDeviceInventoryInfoDto;
import com.ay_za.ataylar_technic.service.SiteDeviceInventoryInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/site-device-inventory")
@Tag(name = "Site Device Inventory", description = "Site Cihaz Envanter Yönetimi")
public class SiteDeviceInventoryInfoController {

    private final SiteDeviceInventoryInfoService siteDeviceInventoryInfoService;

    public SiteDeviceInventoryInfoController(SiteDeviceInventoryInfoService siteDeviceInventoryInfoService) {
        this.siteDeviceInventoryInfoService = siteDeviceInventoryInfoService;
    }

    /**
     * Yeni cihaz envanteri oluştur
     */
    @PostMapping("/create")
    @Operation(summary = "Yeni cihaz envanteri oluştur", description = "Site için yeni bir cihaz envanteri kaydı oluşturur")
    public ResponseEntity<?> createDeviceInventory(@RequestBody SiteDeviceInventoryInfoDto dto) {
        try {
            SiteDeviceInventoryInfoDto createdDevice = siteDeviceInventoryInfoService.createDeviceInventory(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cihaz envanteri oluşturulurken hata: " + e.getMessage());
        }
    }

    /**
     * Cihaz envanteri güncelle
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "Cihaz envanteri güncelle", description = "Mevcut cihaz envanteri kaydını günceller")
    public ResponseEntity<?> updateDeviceInventory(
            @Parameter(description = "Cihaz ID") @PathVariable String id,
            @RequestBody SiteDeviceInventoryInfoDto dto) {
        try {
            SiteDeviceInventoryInfoDto updatedDevice = siteDeviceInventoryInfoService.updateDeviceInventory(id, dto);
            return ResponseEntity.ok(updatedDevice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cihaz envanteri güncellenirken hata: " + e.getMessage());
        }
    }

    /**
     * ID'ye göre cihaz getir
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre cihaz getir", description = "Belirtilen ID'ye sahip cihaz envanteri kaydını getirir")
    public ResponseEntity<?> getDeviceById(@Parameter(description = "Cihaz ID") @PathVariable String id) {
        try {
            Optional<SiteDeviceInventoryInfoDto> device = siteDeviceInventoryInfoService.getDeviceById(id);
            if (device.isPresent()) {
                return ResponseEntity.ok(device.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cihaz getirilirken hata: " + e.getMessage());
        }
    }

    /**
     * QR koda göre cihaz getir
     */
    @GetMapping("/qr/{qrCode}")
    @Operation(summary = "QR koda göre cihaz getir", description = "Belirtilen QR koda sahip cihaz envanteri kaydını getirir")
    public ResponseEntity<?> getDeviceByQRCode(@Parameter(description = "QR Kod") @PathVariable String qrCode) {
        try {
            Optional<SiteDeviceInventoryInfoDto> device = siteDeviceInventoryInfoService.getDeviceByQRCode(qrCode);
            if (device.isPresent()) {
                return ResponseEntity.ok(device.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cihaz getirilirken hata: " + e.getMessage());
        }
    }

    /**
     * Tüm cihazları listele
     */
    @GetMapping("/list")
    @Operation(summary = "Tüm cihazları listele", description = "Sistemdeki tüm cihaz envanteri kayıtlarını listeler")
    public ResponseEntity<?> getAllDevices() {
        try {
            List<SiteDeviceInventoryInfoDto> devices = siteDeviceInventoryInfoService.getAllDevices();
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cihazlar listelenirken hata: " + e.getMessage());
        }
    }

    /**
     * Aktif cihazları listele
     */
    @GetMapping("/list/active")
    @Operation(summary = "Aktif cihazları listele", description = "Sistemdeki aktif cihaz envanteri kayıtlarını listeler")
    public ResponseEntity<?> getActiveDevices() {
        try {
            List<SiteDeviceInventoryInfoDto> devices = siteDeviceInventoryInfoService.getActiveDevices();
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Aktif cihazlar listelenirken hata: " + e.getMessage());
        }
    }

    /**
     * Site ID'ye göre cihazları listele
     */
    @GetMapping("/list/site/{siteId}")
    @Operation(summary = "Site ID'ye göre cihazları listele", description = "Belirtilen site ID'sine ait cihazları listeler")
    public ResponseEntity<?> getDevicesBySiteId(@Parameter(description = "Site ID") @PathVariable String siteId) {
        try {
            List<SiteDeviceInventoryInfoDto> devices = siteDeviceInventoryInfoService.getDevicesBySiteId(siteId);
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Site cihazları listelenirken hata: " + e.getMessage());
        }
    }

    /**
     * Site, ada ve blok bilgisine göre cihazları listele
     */
    @GetMapping("/list/location")
    @Operation(summary = "Lokasyona göre cihazları listele", description = "Site, ada ve blok bilgisine göre cihazları listeler")
    public ResponseEntity<?> getDevicesByLocation(
            @Parameter(description = "Site ID") @RequestParam String siteId,
            @Parameter(description = "Ada") @RequestParam String ada,
            @Parameter(description = "Blok Adı") @RequestParam String blockName) {
        try {
            List<SiteDeviceInventoryInfoDto> devices = siteDeviceInventoryInfoService
                    .getDevicesBySiteAdaBlock(siteId, ada, blockName);
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lokasyon cihazları listelenirken hata: " + e.getMessage());
        }
    }

    /**
     * Kriterlere göre cihaz arama
     */
    @GetMapping("/search")
    @Operation(summary = "Kriterlere göre cihaz arama", description = "Belirtilen kriterlere göre cihaz arama yapar")
    public ResponseEntity<?> searchDevices(
            @Parameter(description = "Site ID") @RequestParam(required = false) String siteId,
            @Parameter(description = "Ada") @RequestParam(required = false) String ada,
            @Parameter(description = "Blok Adı") @RequestParam(required = false) String blockName,
            @Parameter(description = "Aktiflik Durumu") @RequestParam(required = false) Boolean isActive) {
        try {
            List<SiteDeviceInventoryInfoDto> devices = siteDeviceInventoryInfoService
                    .searchDevices(siteId, ada, blockName, isActive);
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cihaz arama işlemi sırasında hata: " + e.getMessage());
        }
    }

    /**
     * Cihazı sil
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Cihazı sil", description = "Belirtilen ID'ye sahip cihaz envanteri kaydını siler")
    public ResponseEntity<?> deleteDevice(@Parameter(description = "Cihaz ID") @PathVariable String id) {
        try {
            siteDeviceInventoryInfoService.deleteDevice(id);
            return ResponseEntity.ok("Cihaz başarıyla silindi");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cihaz silinirken hata: " + e.getMessage());
        }
    }

    /**
     * Cihazı pasif yap
     */
    @PutMapping("/deactivate/{id}")
    @Operation(summary = "Cihazı pasif yap", description = "Belirtilen ID'ye sahip cihazı pasif duruma getirir")
    public ResponseEntity<?> deactivateDevice(
            @Parameter(description = "Cihaz ID") @PathVariable String id,
            @Parameter(description = "Güncelleyen Kullanıcı") @RequestParam String updatedBy) {
        try {
            SiteDeviceInventoryInfoDto deactivatedDevice = siteDeviceInventoryInfoService.deactivateDevice(id, updatedBy);
            return ResponseEntity.ok(deactivatedDevice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Hata: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cihaz pasif yapılırken hata: " + e.getMessage());
        }
    }

    /**
     * Dummy data oluştur
     */
    @PostMapping("/create-default-data")
    @Operation(summary = "Varsayılan cihaz verilerini oluştur", description = "Test amaçlı varsayılan cihaz envanteri verilerini oluşturur")
    public ResponseEntity<?> createDefaultDevices() {
        try {
            siteDeviceInventoryInfoService.createDefaultDevices();
            return ResponseEntity.ok("Varsayılan cihaz verileri başarıyla oluşturuldu");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Varsayılan cihaz verileri oluşturulurken hata: " + e.getMessage());
        }
    }
}
