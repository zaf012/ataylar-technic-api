package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.ServiceCaseDto;
import com.ay_za.ataylar_technic.exception.DataAlreadyExistsException;
import com.ay_za.ataylar_technic.exception.ValidationException;
import com.ay_za.ataylar_technic.service.ServiceCaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/service-cases")
@Tag(name = "Service Cases", description = "Service case (servis durumu) yönetimi")
public class ServiceCaseController {

    private final ServiceCaseService serviceCaseService;

    public ServiceCaseController(ServiceCaseService serviceCaseService) {
        this.serviceCaseService = serviceCaseService;
    }

    /**
     * Tüm service case'leri getir
     */
    @GetMapping
    @Operation(summary = "Tüm service case'leri listele", description = "Sistemdeki tüm service case kayıtlarını getirir")
    public ResponseEntity<List<ServiceCaseDto>> getAll() {
        List<ServiceCaseDto> serviceCases = serviceCaseService.getAll();
        return ResponseEntity.ok(serviceCases);
    }

    /**
     * ID'ye göre service case getir
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre service case getir", description = "Belirtilen ID'ye sahip service case bilgisini getirir")
    public ResponseEntity<ServiceCaseDto> getById(@Parameter(description = "Service Case ID") @PathVariable String id) {
        Optional<ServiceCaseDto> serviceCase = serviceCaseService.getById(id);
        return serviceCase.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Yeni service case oluştur
     */
    @PostMapping
    @Operation(summary = "Yeni service case oluştur", description = "Yeni bir service case kaydı oluşturur")

    public ResponseEntity<?> create(@RequestBody ServiceCaseDto serviceCaseDto) {
        try {
            ServiceCaseDto createdServiceCase = serviceCaseService.create(serviceCaseDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdServiceCase);
        } catch (DataAlreadyExistsException | ValidationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Beklenmeyen bir hata oluştu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }



    /**
     * Service case güncelle
     */
    @PutMapping("/{id}")
    @Operation(summary = "Service case güncelle", description = "Mevcut service case bilgilerini günceller")
    public ResponseEntity<ServiceCaseDto> update(
            @Parameter(description = "Service Case ID") @PathVariable String id,
            @RequestBody ServiceCaseDto serviceCaseDto) {
        try {
            ServiceCaseDto updatedServiceCase = serviceCaseService.update(id, serviceCaseDto);
            return ResponseEntity.ok(updatedServiceCase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Service case sil
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Service case sil", description = "Belirtilen service case kaydını siler")
    public ResponseEntity<Void> delete(@Parameter(description = "Service Case ID") @PathVariable String id) {
        try {
            serviceCaseService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Service case name'e göre getir
     */
    @GetMapping("/by-name/{serviceCaseName}")
    @Operation(summary = "Service case adına göre getir", description = "Belirtilen service case adına sahip kaydı getirir")
    public ResponseEntity<ServiceCaseDto> getByServiceCaseName(
            @Parameter(description = "Service Case Name") @PathVariable String serviceCaseName) {
        Optional<ServiceCaseDto> serviceCase = serviceCaseService.getByServiceCaseName(serviceCaseName);
        return serviceCase.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

