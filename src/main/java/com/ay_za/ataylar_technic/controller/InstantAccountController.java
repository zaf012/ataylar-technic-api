package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.entity.InstantAccount;
import com.ay_za.ataylar_technic.service.base.InstantAccountServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public ResponseEntity<Map<String, Object>> createAccount(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String createdBy = (String) request.get("createdBy");

            InstantAccount account = instantAccountService.createAccount(request, createdBy);

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
            @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            InstantAccount updatedData = mapToInstantAccount(request);
            String updatedBy = (String) request.get("updatedBy");

            InstantAccount account = instantAccountService.updateAccount(accountId, updatedData, updatedBy);

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

            InstantAccount account = instantAccountService.toggleAccountStatus(accountId, updatedBy);

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

            InstantAccount account = instantAccountService.toggleUserStatus(accountId, updatedBy);

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

            InstantAccount account = instantAccountService.activateAccount(accountId, updatedBy);

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

            InstantAccount account = instantAccountService.deactivateAccount(accountId, updatedBy);

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

            InstantAccount account = instantAccountService.updatePassword(accountId, newPassword, updatedBy);

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
            List<InstantAccount> accounts = instantAccountService.getAllActiveAccounts();

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
            List<InstantAccount> accounts = instantAccountService.getAccountsByGroup(groupId);

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

    // Helper method - Request body'den InstantAccount objesi oluştur
    private InstantAccount mapToInstantAccount(Map<String, Object> request) {
        InstantAccount account = new InstantAccount();

        // String alanlar
        account.setAccountGroupId((String) request.get("accountGroupId"));
        account.setSite((String) request.get("site"));
        account.setUserType((String) request.get("userType"));
        account.setUsername((String) request.get("username"));
        account.setPassword((String) request.get("password"));
        account.setName((String) request.get("name"));
        account.setSurname((String) request.get("surname"));
        account.setCompanyName((String) request.get("companyName"));
        account.setCompanyShortName((String) request.get("companyShortName"));
        account.setAuthorizedPerson((String) request.get("authorizedPerson"));
        account.setPhoneCountryCode((String) request.get("phoneCountryCode"));
        account.setPhone((String) request.get("phone"));
        account.setGsmCountryCode((String) request.get("gsmCountryCode"));
        account.setGsm((String) request.get("gsm"));
        account.setAddress((String) request.get("address"));
        account.setCity((String) request.get("city"));
        account.setProvince((String) request.get("province"));
        account.setDistrict((String) request.get("district"));
        account.setNeighborhood((String) request.get("neighborhood"));
        account.setFax((String) request.get("fax"));
        account.setEmail((String) request.get("email"));
        account.setPttBox((String) request.get("pttBox"));
        account.setPostalCode((String) request.get("postalCode"));
        account.setTaxOffice((String) request.get("taxOffice"));
        account.setTaxNumber((String) request.get("taxNumber"));
        account.setTcIdentityNo((String) request.get("tcIdentityNo"));
        account.setBankAddress((String) request.get("bankAddress"));
        account.setRiskLimitExplanation((String) request.get("riskLimitExplanation"));
        account.setSignatureImage((String) request.get("signatureImage"));

        // Boolean alanlar
        if (request.get("userStatus") != null) {
            account.setUserStatus((Boolean) request.get("userStatus"));
        }
        if (request.get("isActive") != null) {
            account.setIsActive((Boolean) request.get("isActive"));
        }

        // BigDecimal alan
        if (request.get("riskLimit") != null) {
            Object riskLimitObj = request.get("riskLimit");
            if (riskLimitObj instanceof Number) {
                account.setRiskLimit(BigDecimal.valueOf(((Number) riskLimitObj).doubleValue()));
            } else if (riskLimitObj instanceof String) {
                try {
                    account.setRiskLimit(new BigDecimal((String) riskLimitObj));
                } catch (NumberFormatException e) {
                    // Invalid number format, ignore
                }
            }
        }

        return account;
    }
}