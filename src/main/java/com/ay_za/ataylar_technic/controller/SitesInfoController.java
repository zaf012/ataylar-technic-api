package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.SitesInfoDto;
import com.ay_za.ataylar_technic.service.SitesInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sites")
@CrossOrigin(origins = "*")
@Tag(name = "Sites Info", description = "Site yönetimi API'leri")
public class SitesInfoController {

    private final SitesInfoService sitesInfoService;

    public SitesInfoController(SitesInfoService sitesInfoService) {
        this.sitesInfoService = sitesInfoService;
    }

    /**
     * Yeni site oluştur
     */
    @Operation(summary = "Site oluştur", description = "Yeni bir site oluşturur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Site başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createSite(@RequestBody SitesInfoDto sitesInfoDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            SitesInfoDto site = sitesInfoService.createSite(sitesInfoDto);

            response.put("success", true);
            response.put("message", "Site başarıyla oluşturuldu");
            response.put("data", site);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Site oluşturulurken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Site güncelle
     */
    @Operation(summary = "Site güncelle", description = "Mevcut siteyi günceller")
    @PutMapping("/{siteId}")
    public ResponseEntity<Map<String, Object>> updateSite(
            @PathVariable String siteId,
            @RequestBody SitesInfoDto sitesInfoDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            SitesInfoDto site = sitesInfoService.updateSite(siteId, sitesInfoDto);

            response.put("success", true);
            response.put("message", "Site başarıyla güncellendi");
            response.put("data", site);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Site güncellenirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Site sil
     */
    @Operation(summary = "Site sil", description = "Siteyi siler")
    @DeleteMapping("/{siteId}")
    public ResponseEntity<Map<String, Object>> deleteSite(@PathVariable String siteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            sitesInfoService.deleteSite(siteId);

            response.put("success", true);
            response.put("message", "Site başarıyla silindi");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Site silinirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * ID'ye göre site getir
     */
    @Operation(summary = "Site getir", description = "ID'ye göre site bilgilerini getirir")
    @GetMapping("/{siteId}")
    public ResponseEntity<Map<String, Object>> getSiteById(@PathVariable String siteId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<SitesInfoDto> site = sitesInfoService.getSiteById(siteId);

            if (site.isPresent()) {
                response.put("success", true);
                response.put("data", site.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Site bulunamadı");
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Site getirilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Tüm siteleri getir
     */
    @Operation(summary = "Siteleri listele", description = "Tüm siteleri alfabetik sırayla getirir")
    @GetMapping("/get-all")
    public ResponseEntity<Map<String, Object>> getAllSites() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<SitesInfoDto> sites = sitesInfoService.getAllSites();

            response.put("success", true);
            response.put("data", sites);
            response.put("count", sites.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Siteler getirilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Proje ID'sine göre siteleri getir
     */
    @Operation(summary = "Projeye ait siteleri getir", description = "Belirtilen projeye ait tüm siteleri getirir")
    @GetMapping("/get-by-project/{projectId}")
    public ResponseEntity<Map<String, Object>> getSitesByProjectId(@PathVariable String projectId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<SitesInfoDto> sites = sitesInfoService.getSitesByProjectId(projectId);

            response.put("success", true);
            response.put("data", sites);
            response.put("count", sites.size());
            response.put("projectId", projectId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Projeye ait siteler getirilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Site adında arama
     */
    @Operation(summary = "Site ara", description = "Site adında arama yapar")
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchSitesByName(@RequestParam String searchTerm) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<SitesInfoDto> sites = sitesInfoService.searchSitesByName(searchTerm);

            response.put("success", true);
            response.put("data", sites);
            response.put("count", sites.size());
            response.put("searchTerm", searchTerm);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Arama yapılırken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Default site verilerini oluştur
     */
    @Operation(summary = "Default site verileri oluştur", description = "Örnek site ve blok verilerini oluşturur")
    @PostMapping("/create-default-datas")
    public ResponseEntity<Map<String, Object>> createDefaultSites() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<SitesInfoDto> createdSites = sitesInfoService.createDefaultSites();

            response.put("success", true);
            response.put("message", createdSites.size() + " örnek site/blok oluşturuldu");
            response.put("data", createdSites);
            response.put("count", createdSites.size());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Örnek siteler oluşturulurken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
