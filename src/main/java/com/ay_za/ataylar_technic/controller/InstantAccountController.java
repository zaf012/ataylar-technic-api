package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.InstantAccountDto;
import com.ay_za.ataylar_technic.entity.InstantAccount;
import com.ay_za.ataylar_technic.service.base.InstantAccountServiceImpl;
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
@RequestMapping("/api/instant-accounts")
@CrossOrigin(origins = "*")
@Tag(name = "Instant Account", description = "Cari hesap yönetimi API'leri")
public class InstantAccountController {

    private final InstantAccountServiceImpl instantAccountService;

    public InstantAccountController(InstantAccountServiceImpl instantAccountService) {
        this.instantAccountService = instantAccountService;
    }

    /**
     * Yeni hesap oluştur
     */
    @Operation(summary = "Yeni hesap oluştur", description = "Yeni bir cari hesap oluşturur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hesap başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz veri veya hesap zaten mevcut"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAccount(@RequestBody InstantAccountDto request) {
        Map<String, Object> response = new HashMap<>();
        try {

            InstantAccountDto account = instantAccountService.createAccount(request);

            response.put("success", true);
            response.put("message", "Hesap başarıyla oluşturuldu");
            response.put("data", account);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Hesap oluşturulurken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Hesap bilgilerini güncelle
     */
    @Operation(summary = "Hesap güncelle", description = "Mevcut hesap bilgilerini günceller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hesap başarıyla güncellendi"),
            @ApiResponse(responseCode = "400", description = "Geçersiz veri veya hesap bulunamadı"),
            @ApiResponse(responseCode = "500", description = "Sunucu hatası")
    })
    @PutMapping("/{accountId}")
    public ResponseEntity<Map<String, Object>> updateAccount(
            @PathVariable String accountId,
            @RequestBody InstantAccountDto request) {
        Map<String, Object> response = new HashMap<>();
        try {

            InstantAccountDto account = instantAccountService.updateAccount(accountId, request, "admin");

            response.put("success", true);
            response.put("message", "Hesap başarıyla güncellendi");
            response.put("data", account);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Hesap güncellenirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Hesap durumunu aktif/pasif yap
     */
    @PutMapping("/{accountId}/toggle-status")
    public ResponseEntity<Map<String, Object>> toggleAccountStatus(
            @PathVariable String accountId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String updatedBy = request.get("updatedBy");

            InstantAccountDto account = instantAccountService.toggleAccountStatus(accountId, updatedBy);

            response.put("success", true);
            response.put("message", "Hesap durumu başarıyla değiştirildi");
            response.put("data", account);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Hesap durumu değiştirilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Kullanıcı durumunu aktif/pasif yap
     */
    @PutMapping("/{accountId}/toggle-user-status")
    public ResponseEntity<Map<String, Object>> toggleUserStatus(
            @PathVariable String accountId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String updatedBy = request.get("updatedBy");

            InstantAccountDto account = instantAccountService.toggleUserStatus(accountId, updatedBy);

            response.put("success", true);
            response.put("message", "Kullanıcı durumu başarıyla değiştirildi");
            response.put("data", account);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Kullanıcı durumu değiştirilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Hesabı aktif yap
     */
    @PutMapping("/{accountId}/activate")
    public ResponseEntity<Map<String, Object>> activateAccount(
            @PathVariable String accountId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String updatedBy = request.get("updatedBy");

            InstantAccountDto account = instantAccountService.activateAccount(accountId, updatedBy);

            response.put("success", true);
            response.put("message", "Hesap başarıyla aktif edildi");
            response.put("data", account);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Hesap aktif edilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Hesabı pasif yap (soft delete)
     */
    @PutMapping("/{accountId}/deactivate")
    public ResponseEntity<Map<String, Object>> deactivateAccount(
            @PathVariable String accountId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String updatedBy = request.get("updatedBy");

            InstantAccountDto account = instantAccountService.deactivateAccount(accountId, updatedBy);

            response.put("success", true);
            response.put("message", "Hesap başarıyla pasif edildi");
            response.put("data", account);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Hesap pasif edilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Hesabı tamamen sil (hard delete)
     */
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Map<String, Object>> deleteAccount(@PathVariable String accountId) {
        Map<String, Object> response = new HashMap<>();
        try {
            instantAccountService.deleteAccount(accountId);

            response.put("success", true);
            response.put("message", "Hesap başarıyla silindi");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Hesap silinirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Şifre güncelle
     */
    @PutMapping("/{accountId}/password")
    public ResponseEntity<Map<String, Object>> updatePassword(
            @PathVariable String accountId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String newPassword = request.get("newPassword");
            String updatedBy = request.get("updatedBy");

            if (newPassword == null || newPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Yeni şifre boş olamaz");
                return ResponseEntity.badRequest().body(response);
            }

            InstantAccountDto account = instantAccountService.updatePassword(accountId, newPassword, updatedBy);

            response.put("success", true);
            response.put("message", "Şifre başarıyla güncellendi");
            response.put("data", account);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Şifre güncellenirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * ID'ye göre hesap getir
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<Map<String, Object>> getAccountById(@PathVariable String accountId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<InstantAccount> account = instantAccountService.getAccountById(accountId);

            if (account.isPresent()) {
                response.put("success", true);
                response.put("data", account.get());
            } else {
                response.put("success", false);
                response.put("message", "Hesap bulunamadı");
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Hesap getirilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Aktif hesap getir
     */
    @GetMapping("/{accountId}/active")
    public ResponseEntity<Map<String, Object>> getActiveAccountById(@PathVariable String accountId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<InstantAccount> account = instantAccountService.getActiveAccountById(accountId);

            if (account.isPresent()) {
                response.put("success", true);
                response.put("data", account.get());
            } else {
                response.put("success", false);
                response.put("message", "Aktif hesap bulunamadı");
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Hesap getirilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Tüm aktif hesapları getir
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getAllActiveAccounts() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<InstantAccountDto> accounts = instantAccountService.getAllActiveAccounts();

            response.put("success", true);
            response.put("data", accounts);
            response.put("count", accounts.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Hesaplar getirilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Cari gruba göre hesapları getir
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<Map<String, Object>> getAccountsByGroup(@PathVariable String groupId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<InstantAccountDto> accounts = instantAccountService.getAccountsByGroup(groupId);

            response.put("success", true);
            response.put("data", accounts);
            response.put("count", accounts.size());
            response.put("groupId", groupId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Grup hesapları getirilirken bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}