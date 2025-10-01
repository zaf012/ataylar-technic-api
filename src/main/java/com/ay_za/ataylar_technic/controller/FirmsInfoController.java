package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.FirmsInfoDto;
import com.ay_za.ataylar_technic.service.base.FirmsInfoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/firms")
@CrossOrigin(origins = "*")
@Tag(name = "Firms Info", description = "Firma yönetimi API'leri")
public class FirmsInfoController {

    private final FirmsInfoServiceImpl firmsInfoService;

    public FirmsInfoController(FirmsInfoServiceImpl firmsInfoService) {
        this.firmsInfoService = firmsInfoService;
    }

    /**
     * Yeni firma oluştur
     */
    @Operation(summary = "Firma oluştur", description = "Yeni bir firma oluşturur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Firma başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createFirm(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String firmName = request.get("firmName");
            String createdBy = request.get("createdBy");

            if (firmName == null || firmName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Firma adı boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            FirmsInfoDto firm = firmsInfoService.createFirm(firmName, createdBy);

            response.put("success", true);
            response.put("message", "Firma başarıyla oluşturuldu");
            response.put("data", firm);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Firma oluşturulurken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Firma güncelle
     */
    @Operation(summary = "Firma güncelle", description = "Mevcut firmayı günceller")
    @PutMapping("/update/{firmId}")
    public ResponseEntity<Map<String, Object>> updateFirm(
            @PathVariable String firmId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String firmName = request.get("firmName");
            String updatedBy = request.get("updatedBy");

            if (firmName == null || firmName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Firma adı boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            FirmsInfoDto firm = firmsInfoService.updateFirm(firmId, firmName, updatedBy);

            response.put("success", true);
            response.put("message", "Firma başarıyla güncellendi");
            response.put("data", firm);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Firma güncellenirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Firma sil
     */
    @Operation(summary = "Firma sil", description = "Firmayı siler")
    @DeleteMapping("/delete/{firmId}")
    public ResponseEntity<Map<String, Object>> deleteFirm(@PathVariable String firmId) {
        Map<String, Object> response = new HashMap<>();
        try {
            firmsInfoService.deleteFirm(firmId);

            response.put("success", true);
            response.put("message", "Firma başarıyla silindi");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Firma silinirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * ID'ye göre firma getir
     */
    @Operation(summary = "Firma getir", description = "ID'ye göre firma bilgilerini getirir")
    @GetMapping("/get/{firmId}")
    public ResponseEntity<Map<String, Object>> getFirmById(@PathVariable String firmId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<FirmsInfoDto> firm = firmsInfoService.getFirmById(firmId);

            if (firm.isPresent()) {
                response.put("success", true);
                response.put("data", firm.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Firma bulunamadı");
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Firma getirilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Tüm firmaları getir
     */
    @Operation(summary = "Firmaları listele", description = "Tüm firmaları alfabetik sırayla getirir")
    @GetMapping("/get-all")
    public ResponseEntity<Map<String, Object>> getAllFirms() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<FirmsInfoDto> firms = firmsInfoService.getAllFirms();

            response.put("success", true);
            response.put("data", firms);
            response.put("count", firms.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Firmalar getirilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Firma adında arama
     */
    @Operation(summary = "Firma ara", description = "Firma adında arama yapar")
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchFirmsByName(@RequestParam String searchTerm) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<FirmsInfoDto> firms = firmsInfoService.searchFirmsByName(searchTerm);

            response.put("success", true);
            response.put("data", firms);
            response.put("count", firms.size());
            response.put("searchTerm", searchTerm);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Arama yapılırken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Firma sayısını getir
     */
    @Operation(summary = "Firma sayısı", description = "Toplam firma sayısını getirir")
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getFirmCount() {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer count = firmsInfoService.getFirmCount();

            response.put("success", true);
            response.put("count", count);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Firma sayısı alınırken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Default firma verilerini oluştur
     */
    @Operation(summary = "Default firma verileri oluştur", description = "Örnek firma verilerini oluşturur")
    @PostMapping("/create-default-datas")
    public ResponseEntity<Map<String, Object>> createDefaultFirms() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<FirmsInfoDto> createdFirms = createSampleFirms();

            response.put("success", true);
            response.put("message", createdFirms.size() + " örnek firma oluşturuldu");
            response.put("data", createdFirms);
            response.put("count", createdFirms.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Örnek firmalar oluşturulurken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Örnek firma verilerini oluştur
     */
    private List<FirmsInfoDto> createSampleFirms() {
        List<String> sampleFirms = Arrays.asList(
                "DAP Yapı",
                "Emlak Konut GYO",
                "TOKİ",
                "Sinpaş GYO",
                "Nurol İnşaat",
                "Akfen İnşaat",
                "Mesa Mesken",
                "Rönesans Holding",
                "Ağaoğlu Grubu",
                "Babacan Holding"
        );

        List<FirmsInfoDto> createdFirms = new ArrayList<>();
        String createdBy = "System Admin";

        for (String firmName : sampleFirms) {
            try {
                // Var olan firmaları tekrar oluşturmaya çalışma
                if (!firmsInfoService.existsByFirmName(firmName)) {
                    FirmsInfoDto created = firmsInfoService.createFirm(firmName, createdBy);
                    createdFirms.add(created);
                }
            } catch (Exception e) {
                // Hata durumunda devam et
                System.err.println("Firma oluşturulurken hata: " + firmName + " - " + e.getMessage());
            }
        }

        return createdFirms;
    }
}
