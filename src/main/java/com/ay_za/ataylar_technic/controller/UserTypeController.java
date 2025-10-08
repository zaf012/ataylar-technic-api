package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.UserTypeDto;
import com.ay_za.ataylar_technic.service.base.UserTypeServiceImpl;
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
@RequestMapping("/api/user-types")
@CrossOrigin(origins = "*")
@Tag(name = "User Type", description = "Kullanıcı tipi yönetimi API'leri")
public class UserTypeController {

    private final UserTypeServiceImpl userTypeService;

    public UserTypeController(UserTypeServiceImpl userTypeService) {
        this.userTypeService = userTypeService;
    }

    /**
     * Tüm kullanıcı tiplerini getir
     */
    @Operation(summary = "Kullanıcı tiplerini listele", description = "Tüm kullanıcı tiplerini getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı tipleri başarıyla getirildi"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @GetMapping("/get-all")
    public ResponseEntity<Map<String, Object>> getAllUserTypes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<UserTypeDto> userTypes = userTypeService.getAllUserTypes();

            response.put("success", true);
            response.put("data", userTypes);
            response.put("count", userTypes.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Kullanıcı tipleri getirilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Yeni kullanıcı tipi oluştur
     */
    @Operation(summary = "Kullanıcı tipi oluştur", description = "Yeni kullanıcı tipi oluşturur. Boş bırakılırsa ID=0 olur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı tipi başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUserType(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userTypeName = request.get("userTypeName");

            UserTypeDto userType = userTypeService.createUserType(userTypeName);

            response.put("success", true);
            response.put("message", userTypeName == null || userTypeName.trim().isEmpty()
                ? "Belirtilmemiş kullanıcı tipi oluşturuldu (ID=0)"
                : "Kullanıcı tipi başarıyla oluşturuldu");
            response.put("data", userType);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Kullanıcı tipi oluşturulurken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Default kullanıcı tiplerini oluştur
     */
    @Operation(summary = "Default kullanıcı tiplerini oluştur",
               description = "Ana Kullanıcı, Site Sakini, Personel, Cari Hesap tiplerini oluşturur")
    @PostMapping("/create-defaults")
    public ResponseEntity<Map<String, Object>> createDefaultUserTypes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<UserTypeDto> createdTypes = userTypeService.createDefaultUserTypes();

            response.put("success", true);
            response.put("message", createdTypes.size() + " adet default kullanıcı tipi oluşturuldu");
            response.put("data", createdTypes);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Default kullanıcı tipleri oluşturulurken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * ID'ye göre kullanıcı tipi getir
     */
    @Operation(summary = "Kullanıcı tipi getir", description = "Belirtilen ID'ye sahip kullanıcı tipini getirir")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserTypeById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<UserTypeDto> userType = userTypeService.getUserTypeById(id);

            if (userType.isPresent()) {
                response.put("success", true);
                response.put("data", userType.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Kullanıcı tipi bulunamadı");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Kullanıcı tipi getirilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Kullanıcı tipini güncelle
     */
    @Operation(summary = "Kullanıcı tipini güncelle", description = "Belirtilen ID'ye sahip kullanıcı tipini günceller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı tipi başarıyla güncellendi"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı tipi bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateUserType(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String userTypeName = request.get("userTypeName");

            UserTypeDto updatedUserType = userTypeService.updateUserType(id, userTypeName);

            response.put("success", true);
            response.put("message", "Kullanıcı tipi başarıyla güncellendi");
            response.put("data", updatedUserType);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Kullanıcı tipi güncellenirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Kullanıcı tipini sil
     */
    @Operation(summary = "Kullanıcı tipini sil", description = "Belirtilen ID'ye sahip kullanıcı tipini siler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı tipi başarıyla silindi"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek - Varsayılan tip silinemez"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı tipi bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserType(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userTypeService.deleteUserType(id);

            response.put("success", true);
            response.put("message", "Kullanıcı tipi başarıyla silindi");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Kullanıcı tipi silinirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Kullanıcı tipi varlık kontrolü
     */
    @Operation(summary = "Kullanıcı tipi varlık kontrolü", description = "Belirtilen ID'ye sahip kullanıcı tipinin var olup olmadığını kontrol eder")
    @GetMapping("/check/{id}")
    public ResponseEntity<Map<String, Object>> checkUserTypeById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean exists = userTypeService.checkUserTypeById(id);

            response.put("success", true);
            response.put("exists", exists);
            response.put("message", exists ? "Kullanıcı tipi mevcut" : "Kullanıcı tipi bulunamadı");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Kullanıcı tipi kontrol edilirken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
