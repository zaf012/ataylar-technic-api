package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.SiteProductInventoryDetailDto;
import com.ay_za.ataylar_technic.service.SiteProductInventoryDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Site Ürün Envanter Detayı Controller
 */
@RestController
@RequestMapping("/api/site-product-inventory-details")
@Tag(name = "Site Product Inventory Detail", description = "Site Ürün Envanter Detayı API")
public class SiteProductInventoryDetailController {

    private final SiteProductInventoryDetailService service;

    public SiteProductInventoryDetailController(SiteProductInventoryDetailService service) {
        this.service = service;
    }

    /**
     * Yeni site ürün detayı oluştur
     */
    @PostMapping
    @Operation(summary = "Yeni site ürün detayı oluştur", description = "Yeni bir site ürün envanter detayı oluşturur ve benzersiz QR kod atar")
    public ResponseEntity<SiteProductInventoryDetailDto> createSiteProductDetail(
            @RequestBody SiteProductInventoryDetailDto dto) {
        SiteProductInventoryDetailDto created = service.createSiteProductDetail(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Site ürün detayını güncelle
     */
    @PutMapping("/{id}")
    @Operation(summary = "Site ürün detayını güncelle", description = "Mevcut bir site ürün detayını günceller")
    public ResponseEntity<SiteProductInventoryDetailDto> updateSiteProductDetail(
            @Parameter(description = "Site Ürün Detay ID") @PathVariable String id,
            @RequestBody SiteProductInventoryDetailDto dto) {
        SiteProductInventoryDetailDto updated = service.updateSiteProductDetail(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * ID'ye göre site ürün detayı getir
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre site ürün detayı getir", description = "Belirtilen ID'ye sahip site ürün detayını getirir")
    public ResponseEntity<SiteProductInventoryDetailDto> getSiteProductDetailById(
            @Parameter(description = "Site Ürün Detay ID") @PathVariable String id) {
        SiteProductInventoryDetailDto dto = service.getSiteProductDetailById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * QR koduna göre site ürün detayı getir
     */
    @GetMapping("/qr-code/{qrCode}")
    @Operation(summary = "QR koduna göre site ürün detayı getir", description = "Belirtilen QR koduna sahip site ürün detayını getirir")
    public ResponseEntity<SiteProductInventoryDetailDto> getSiteProductDetailByQrCode(
            @Parameter(description = "QR Kodu") @PathVariable String qrCode) {
        SiteProductInventoryDetailDto dto = service.getSiteProductDetailByQrCode(qrCode);
        return ResponseEntity.ok(dto);
    }

    /**
     * Tüm site ürün detaylarını getir
     */
    @GetMapping
    @Operation(summary = "Tüm site ürün detaylarını getir", description = "Tüm site ürün envanter detaylarını listeler")
    public ResponseEntity<List<SiteProductInventoryDetailDto>> getAllSiteProductDetails() {
        List<SiteProductInventoryDetailDto> dtos = service.getAllSiteProductDetails();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Aktif site ürün detaylarını getir
     */
    @GetMapping("/active")
    @Operation(summary = "Aktif site ürün detaylarını getir", description = "Sadece aktif site ürün detaylarını listeler")
    public ResponseEntity<List<SiteProductInventoryDetailDto>> getActiveSiteProductDetails() {
        List<SiteProductInventoryDetailDto> dtos = service.getActiveSiteProductDetails();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Site ürün detayını sil
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Site ürün detayını sil", description = "Belirtilen ID'ye sahip site ürün detayını siler")
    public ResponseEntity<Void> deleteSiteProductDetail(
            @Parameter(description = "Site Ürün Detay ID") @PathVariable String id) {
        service.deleteSiteProductDetail(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Site ürün detayını pasif yap
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Site ürün detayını pasif yap", description = "Belirtilen ID'ye sahip site ürün detayını pasif yapar (soft delete)")
    public ResponseEntity<SiteProductInventoryDetailDto> deactivateSiteProductDetail(
            @Parameter(description = "Site Ürün Detay ID") @PathVariable String id) {
        SiteProductInventoryDetailDto dto = service.deactivateSiteProductDetail(id);
        return ResponseEntity.ok(dto);
    }
}