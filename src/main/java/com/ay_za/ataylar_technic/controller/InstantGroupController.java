package com.ay_za.ataylar_technic.controller;


import com.ay_za.ataylar_technic.entity.InstantGroup;
import com.ay_za.ataylar_technic.service.base.InstantGroupServiceImpl;
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
@RequestMapping("/api/instant-groups")
@CrossOrigin(origins = "*")
@Tag(name = "Instant Group", description = "Cari grup yönetimi API'leri")
public class InstantGroupController {

    private final InstantGroupServiceImpl instantGroupService;

    public InstantGroupController(InstantGroupServiceImpl instantGroupService) {
        this.instantGroupService = instantGroupService;
    }

    /**
     * Yeni grup oluştur
     */
    @Operation(summary = "Yeni grup oluştur", description = "Yeni bir cari grup oluşturur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grup başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek veya grup zaten mevcut"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @PostMapping("/create-group")
    public ResponseEntity<Map<String, Object>> createGroup(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String groupName = request.get("groupName");
            String createdBy = request.get("createdBy");

            if (groupName == null || groupName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Grup adı boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            InstantGroup group = instantGroupService.createGroup(groupName, createdBy);

            response.put("success", true);
            response.put("message", "Grup başarıyla oluşturuldu");
            response.put("data", group);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Grup oluşturulurken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Grup adını güncelle
     */
    @PutMapping("/update-group-name/{groupId}")
    public ResponseEntity<Map<String, Object>> updateGroupName(
            @PathVariable String groupId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String newGroupName = request.get("groupName");
            String updatedBy = request.get("updatedBy");

            if (newGroupName == null || newGroupName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Grup adı boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            InstantGroup group = instantGroupService.updateGroupName(groupId, newGroupName, updatedBy);

            response.put("success", true);
            response.put("message", "Grup adı başarıyla güncellendi");
            response.put("data", group);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Grup güncellenirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Grup durumunu aktif/pasif yap
     */
    @PutMapping("/toggle-status/{groupId}")
    public ResponseEntity<Map<String, Object>> toggleGroupStatus(
            @PathVariable String groupId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String updatedBy = request.get("updatedBy");

            InstantGroup group = instantGroupService.toggleGroupStatus(groupId, updatedBy);

            response.put("success", true);
            response.put("message", "Grup durumu başarıyla değiştirildi");
            response.put("data", group);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Grup durumu değiştirilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Grubu aktif yap
     */
    @PutMapping("/activate-group/{groupId}")
    public ResponseEntity<Map<String, Object>> activateGroup(
            @PathVariable String groupId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String updatedBy = request.get("updatedBy");

            InstantGroup group = instantGroupService.activateGroup(groupId, updatedBy);

            response.put("success", true);
            response.put("message", "Grup başarıyla aktif edildi");
            response.put("data", group);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Grup aktif edilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Grubu pasif yap (soft delete)
     */
    @PutMapping("/deactivate/{groupId}")
    public ResponseEntity<Map<String, Object>> deactivateGroup(
            @PathVariable String groupId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String updatedBy = request.get("updatedBy");

            InstantGroup group = instantGroupService.deactivateGroup(groupId, updatedBy);

            response.put("success", true);
            response.put("message", "Grup başarıyla pasif edildi");
            response.put("data", group);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Grup pasif edilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Grubu tamamen sil (hard delete)
     */
    @DeleteMapping("/delete-group/{groupId}")
    public ResponseEntity<Map<String, Object>> deleteGroup(@PathVariable String groupId) {
        Map<String, Object> response = new HashMap<>();
        try {
            instantGroupService.deleteGroup(groupId);

            response.put("success", true);
            response.put("message", "Grup başarıyla silindi");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Grup silinirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * ID'ye göre grup getir
     */
    @Operation(summary = "Grup getir", description = "ID'ye göre grup bilgilerini getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grup bulundu"),
            @ApiResponse(responseCode = "404", description = "Grup bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @GetMapping("/get-group-by-id/{groupId}")
    public ResponseEntity<Map<String, Object>> getGroupById(@PathVariable String groupId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<InstantGroup> group = instantGroupService.getGroupById(groupId);

            if (group.isPresent()) {
                response.put("success", true);
                response.put("data", group.get());
            } else {
                response.put("success", false);
                response.put("message", "Grup bulunamadı");
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Grup getirilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Aktif grup getir
     */
    @GetMapping("/get-active-group-by-id/{groupId}")
    public ResponseEntity<Map<String, Object>> getActiveGroupById(@PathVariable String groupId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<InstantGroup> group = instantGroupService.getActiveGroupById(groupId);

            if (group.isPresent()) {
                response.put("success", true);
                response.put("data", group.get());
            } else {
                response.put("success", false);
                response.put("message", "Aktif grup bulunamadı");
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Grup getirilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Tüm aktif grupları getir
     */
    @Operation(summary = "Aktif grupları listele", description = "Tüm aktif grupları alfabetik sırayla getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gruplar başarıyla getirildi"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @GetMapping("/get-all-active-groups")
    public ResponseEntity<Map<String, Object>> getAllActiveGroups() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<InstantGroup> groups = instantGroupService.getAllActiveGroups();

            response.put("success", true);
            response.put("data", groups);
            response.put("count", groups.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Gruplar getirilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Tüm grupları getir (aktif + pasif)
     */
    @GetMapping("/get-all-groups")
    public ResponseEntity<Map<String, Object>> getAllGroups() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<InstantGroup> groups = instantGroupService.getAllGroups();

            response.put("success", true);
            response.put("data", groups);
            response.put("count", groups.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Gruplar getirilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Grup adında arama
     */
    @GetMapping("/search-in-group-name")
    public ResponseEntity<Map<String, Object>> searchGroupsByName(@RequestParam String searchTerm) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<InstantGroup> groups = instantGroupService.searchGroupsByName(searchTerm);

            response.put("success", true);
            response.put("data", groups);
            response.put("count", groups.size());
            response.put("searchTerm", searchTerm);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Arama yapılırken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Aktif grup sayısını getir
     */
    @GetMapping("/get-count-active-groups")
    public ResponseEntity<Map<String, Object>> getActiveGroupCount() {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer count = instantGroupService.getActiveGroupCount();

            response.put("success", true);
            response.put("count", count);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Grup sayısı alınırken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Varsayılan grupları oluştur
     */
    @PostMapping("/create-defaults-za")
    public ResponseEntity<Map<String, Object>> createDefaultGroups(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String createdBy = request.get("createdBy");

            if (createdBy == null || createdBy.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "createdBy alanı boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            List<InstantGroup> createdGroups = instantGroupService.createDefaultGroups(createdBy);

            response.put("success", true);
            response.put("message", createdGroups.size() + " varsayılan grup başarıyla oluşturuldu");
            response.put("data", createdGroups);
            response.put("count", createdGroups.size());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Varsayılan gruplar oluşturulurken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}