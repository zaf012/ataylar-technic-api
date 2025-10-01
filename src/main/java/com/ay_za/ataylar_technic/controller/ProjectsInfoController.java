package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.FirmsInfoDto;
import com.ay_za.ataylar_technic.dto.ProjectsInfoDto;
import com.ay_za.ataylar_technic.service.base.FirmsInfoServiceImpl;
import com.ay_za.ataylar_technic.service.base.ProjectsInfoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
@Tag(name = "Projects Info", description = "Proje yönetimi API'leri")
public class ProjectsInfoController {

    private final ProjectsInfoServiceImpl projectsInfoService;
    private final FirmsInfoServiceImpl firmsInfoService;

    public ProjectsInfoController(ProjectsInfoServiceImpl projectsInfoService, FirmsInfoServiceImpl firmsInfoService) {
        this.projectsInfoService = projectsInfoService;
        this.firmsInfoService = firmsInfoService;
    }

    /**
     * Yeni proje oluştur
     */
    @Operation(summary = "Proje oluştur", description = "Yeni bir proje oluşturur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proje başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createProject(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String firmId = request.get("firmId");
            String projectName = request.get("projectName");
            String createdBy = request.get("createdBy");

            if (firmId == null || firmId.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Firma ID boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            if (projectName == null || projectName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Proje adı boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            ProjectsInfoDto project = projectsInfoService.createProject(firmId, projectName, createdBy);

            response.put("success", true);
            response.put("message", "Proje başarıyla oluşturuldu");
            response.put("data", project);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Proje oluşturulurken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Proje güncelle
     */
    @Operation(summary = "Proje güncelle", description = "Mevcut projeyi günceller")
    @PutMapping("/update/{projectId}")
    public ResponseEntity<Map<String, Object>> updateProject(
            @PathVariable String projectId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String projectName = request.get("projectName");
            String updatedBy = request.get("updatedBy");

            if (projectName == null || projectName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Proje adı boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            ProjectsInfoDto project = projectsInfoService.updateProject(projectId, projectName, updatedBy);

            response.put("success", true);
            response.put("message", "Proje başarıyla güncellendi");
            response.put("data", project);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Proje güncellenirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Proje sil
     */
    @Operation(summary = "Proje sil", description = "Projeyi siler")
    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<Map<String, Object>> deleteProject(@PathVariable String projectId) {
        Map<String, Object> response = new HashMap<>();
        try {
            projectsInfoService.deleteProject(projectId);

            response.put("success", true);
            response.put("message", "Proje başarıyla silindi");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Proje silinirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * ID'ye göre proje getir
     */
    @Operation(summary = "Proje getir", description = "ID'ye göre proje bilgilerini getirir")
    @GetMapping("/get/{projectId}")
    public ResponseEntity<Map<String, Object>> getProjectById(@PathVariable String projectId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ProjectsInfoDto> project = projectsInfoService.getProjectById(projectId);

            if (project.isPresent()) {
                response.put("success", true);
                response.put("data", project.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Proje bulunamadı");
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Proje getirilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Tüm projeleri getir
     */
    @Operation(summary = "Projeleri listele", description = "Tüm projeleri alfabetik sırayla getirir")
    @GetMapping("/get-all")
    public ResponseEntity<Map<String, Object>> getAllProjects() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ProjectsInfoDto> projects = projectsInfoService.getAllProjects();

            response.put("success", true);
            response.put("data", projects);
            response.put("count", projects.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Projeler getirilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Firma ID'sine göre projeleri getir
     */
    @Operation(summary = "Firmaya ait projeleri getir", description = "Belirtilen firmaya ait tüm projeleri getirir")
    @GetMapping("/get-by-firm/{firmId}")
    public ResponseEntity<Map<String, Object>> getProjectsByFirmId(@PathVariable String firmId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ProjectsInfoDto> projects = projectsInfoService.getProjectsByFirmId(firmId);

            response.put("success", true);
            response.put("data", projects);
            response.put("count", projects.size());
            response.put("firmId", firmId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Firmaya ait projeler getirilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Proje adında arama
     */
    @Operation(summary = "Proje ara", description = "Proje adında arama yapar")
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProjectsByName(@RequestParam String searchTerm) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ProjectsInfoDto> projects = projectsInfoService.searchProjectsByName(searchTerm);

            response.put("success", true);
            response.put("data", projects);
            response.put("count", projects.size());
            response.put("searchTerm", searchTerm);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Arama yapılırken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Proje sayısını getir
     */
    @Operation(summary = "Proje sayısı", description = "Toplam proje sayısını getirir")
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getProjectCount() {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer count = projectsInfoService.getProjectCount();

            response.put("success", true);
            response.put("count", count);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Proje sayısı alınırken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Firmaya ait proje sayısını getir
     */
    @Operation(summary = "Firmaya ait proje sayısı", description = "Belirtilen firmaya ait proje sayısını getirir")
    @GetMapping("/count-by-firm/{firmId}")
    public ResponseEntity<Map<String, Object>> getProjectCountByFirmId(@PathVariable String firmId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer count = projectsInfoService.getProjectCountByFirmId(firmId);

            response.put("success", true);
            response.put("count", count);
            response.put("firmId", firmId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Firmaya ait proje sayısı alınırken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Default proje verilerini oluştur
     */
    @Operation(summary = "Default proje verileri oluştur", description = "Örnek proje verilerini oluşturur")
    @PostMapping("/create-default-datas")
    public ResponseEntity<Map<String, Object>> createDefaultProjects() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ProjectsInfoDto> createdProjects = createSampleProjects();

            response.put("success", true);
            response.put("message", createdProjects.size() + " örnek proje oluşturuldu");
            response.put("data", createdProjects);
            response.put("count", createdProjects.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Örnek projeler oluşturulurken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Örnek proje verilerini oluştur
     */
    private List<ProjectsInfoDto> createSampleProjects() {
        List<ProjectsInfoDto> createdProjects = new ArrayList<>();
        String createdBy = "System Admin";

        // Önce firmaları kontrol et ve sample projeler için gerekli firmaları oluştur
        Map<String, String> firmProjectMapping = new LinkedHashMap<>();

        // DAP Yapı projeleri
        firmProjectMapping.put("DAP Yapı", "DAP Yapı Mesa Kartal");
        firmProjectMapping.put("DAP Yapı", "DAP Yapı Maltepe");
        firmProjectMapping.put("DAP Yapı", "DAP Yapı Royal");

        // Emlak Konut GYO projeleri
        firmProjectMapping.put("Emlak Konut GYO", "Emlak Konut Başakşehir");
        firmProjectMapping.put("Emlak Konut GYO", "Emlak Konut Kayaşehir");
        firmProjectMapping.put("Emlak Konut GYO", "Emlak Konut Sancaktepe");

        // TOKİ projeleri
        firmProjectMapping.put("TOKİ", "TOKİ Mamak");
        firmProjectMapping.put("TOKİ", "TOKİ Pursaklar");
        firmProjectMapping.put("TOKİ", "TOKİ Altındağ");

        // Sinpaş GYO projeleri
        firmProjectMapping.put("Sinpaş GYO", "Sinpaş Queen Central Park");
        firmProjectMapping.put("Sinpaş GYO", "Sinpaş Altınoran");
        firmProjectMapping.put("Sinpaş GYO", "Sinpaş Bosphorus City");

        // Nurol İnşaat projeleri
        firmProjectMapping.put("Nurol İnşaat", "Nurol Park");
        firmProjectMapping.put("Nurol İnşaat", "Nurol Life");
        firmProjectMapping.put("Nurol İnşaat", "Nurol Tower");

        for (Map.Entry<String, String> entry : firmProjectMapping.entrySet()) {
            String firmName = entry.getKey();
            String projectName = entry.getValue();

            try {
                // Firma var mı kontrol et
                List<FirmsInfoDto> firms = firmsInfoService.searchFirmsByName(firmName);
                String firmId = null;

                for (FirmsInfoDto firm : firms) {
                    if (firm.getFirmName().equalsIgnoreCase(firmName)) {
                        firmId = firm.getId();
                        break;
                    }
                }

                if (firmId != null) {
                    // Aynı proje adı zaten var mı kontrol et
                    if (!projectsInfoService.existsByFirmIdAndProjectName(firmId, projectName)) {
                        ProjectsInfoDto created = projectsInfoService.createProject(firmId, projectName, createdBy);
                        createdProjects.add(created);
                    }
                } else {
                    System.err.println("Firma bulunamadı: " + firmName);
                }

            } catch (Exception e) {
                // Hata durumunda devam et
                System.err.println("Proje oluşturulurken hata: " + projectName + " - " + e.getMessage());
            }
        }

        return createdProjects;
    }
}
