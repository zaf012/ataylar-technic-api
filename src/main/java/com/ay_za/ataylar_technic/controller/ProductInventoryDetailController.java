package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.ProductInventoryDetailDto;
import com.ay_za.ataylar_technic.service.ProductInventoryDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Ürün Envanter Detayı Controller
 */
@RestController
@RequestMapping("/api/product-inventory-details")
@Tag(name = "Product Inventory Detail", description = "Ürün Envanter Detayı API")
public class ProductInventoryDetailController {

    private final ProductInventoryDetailService service;

    public ProductInventoryDetailController(ProductInventoryDetailService service) {
        this.service = service;
    }

    /**
     * Yeni ürün detayı oluştur
     */
    @PostMapping
    @Operation(summary = "Yeni ürün detayı oluştur", description = "Yeni bir ürün envanter detayı oluşturur")
    public ResponseEntity<ProductInventoryDetailDto> createProductDetail(
            @RequestBody ProductInventoryDetailDto dto) {
        ProductInventoryDetailDto created = service.createProductDetail(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Ürün detayını güncelle
     */
    @PutMapping("/{id}")
    @Operation(summary = "Ürün detayını güncelle", description = "Mevcut bir ürün detayını günceller")
    public ResponseEntity<ProductInventoryDetailDto> updateProductDetail(
            @Parameter(description = "Ürün Detay ID") @PathVariable String id,
            @RequestBody ProductInventoryDetailDto dto) {
        ProductInventoryDetailDto updated = service.updateProductDetail(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * ID'ye göre ürün detayı getir
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre ürün detayı getir", description = "Belirtilen ID'ye sahip ürün detayını getirir")
    public ResponseEntity<ProductInventoryDetailDto> getProductDetailById(
            @Parameter(description = "Ürün Detay ID") @PathVariable String id) {
        ProductInventoryDetailDto dto = service.getProductDetailById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Market koduna göre ürün detayı getir
     */
    @GetMapping("/market-code/{marketCode}")
    @Operation(summary = "Market koduna göre ürün detayı getir", description = "Belirtilen market koduna sahip ürün detayını getirir")
    public ResponseEntity<ProductInventoryDetailDto> getProductDetailByMarketCode(
            @Parameter(description = "Market Kodu") @PathVariable String marketCode) {
        ProductInventoryDetailDto dto = service.getProductDetailByMarketCode(marketCode);
        return ResponseEntity.ok(dto);
    }

    /**
     * Tüm ürün detaylarını getir
     */
    @GetMapping
    @Operation(summary = "Tüm ürün detaylarını getir", description = "Tüm ürün envanter detaylarını listeler")
    public ResponseEntity<List<ProductInventoryDetailDto>> getAllProductDetails() {
        List<ProductInventoryDetailDto> dtos = service.getAllProductDetails();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Aktif ürün detaylarını getir
     */
    @GetMapping("/active")
    @Operation(summary = "Aktif ürün detaylarını getir", description = "Sadece aktif ürün detaylarını listeler")
    public ResponseEntity<List<ProductInventoryDetailDto>> getActiveProductDetails() {
        List<ProductInventoryDetailDto> dtos = service.getActiveProductDetails();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Kategoriye göre ürün detaylarını getir
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Kategoriye göre ürün detaylarını getir", description = "Belirtilen kategoriye ait tüm ürün detaylarını getirir")
    public ResponseEntity<List<ProductInventoryDetailDto>> getProductDetailsByCategoryId(
            @Parameter(description = "Kategori ID") @PathVariable String categoryId) {
        List<ProductInventoryDetailDto> dtos = service.getProductDetailsByCategoryId(categoryId);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Kategoriye göre aktif ürün detaylarını getir
     */
    @GetMapping("/category/{categoryId}/active")
    @Operation(summary = "Kategoriye göre aktif ürün detaylarını getir", description = "Belirtilen kategoriye ait aktif ürün detaylarını getirir")
    public ResponseEntity<List<ProductInventoryDetailDto>> getActiveProductDetailsByCategoryId(
            @Parameter(description = "Kategori ID") @PathVariable String categoryId) {
        List<ProductInventoryDetailDto> dtos = service.getActiveProductDetailsByCategoryId(categoryId);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Markaya göre ürün detaylarını getir
     */
    @GetMapping("/brand/{brandName}")
    @Operation(summary = "Markaya göre ürün detaylarını getir", description = "Belirtilen markaya ait ürün detaylarını getirir")
    public ResponseEntity<List<ProductInventoryDetailDto>> getProductDetailsByBrandName(
            @Parameter(description = "Marka Adı") @PathVariable String brandName) {
        List<ProductInventoryDetailDto> dtos = service.getProductDetailsByBrandName(brandName);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Ürün adına göre arama yap
     */
    @GetMapping("/search/product-name")
    @Operation(summary = "Ürün adına göre arama yap", description = "Ürün adında arama yapar")
    public ResponseEntity<List<ProductInventoryDetailDto>> searchProductDetailsByProductName(
            @Parameter(description = "Ürün Adı") @RequestParam String productName) {
        List<ProductInventoryDetailDto> dtos = service.searchProductDetailsByProductName(productName);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Anahtar kelimeye göre arama yap
     */
    @GetMapping("/search")
    @Operation(summary = "Anahtar kelimeye göre arama yap", description = "Market kodu, ürün adı veya marka adında arama yapar")
    public ResponseEntity<List<ProductInventoryDetailDto>> searchProductDetailsByKeyword(
            @Parameter(description = "Arama Kelimesi") @RequestParam String keyword) {
        List<ProductInventoryDetailDto> dtos = service.searchProductDetailsByKeyword(keyword);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Ürün detayını sil
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Ürün detayını sil", description = "Belirtilen ürün detayını kalıcı olarak siler")
    public ResponseEntity<Void> deleteProductDetail(
            @Parameter(description = "Ürün Detay ID") @PathVariable String id) {
        service.deleteProductDetail(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Ürün detayını pasif yap
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Ürün detayını pasif yap", description = "Ürün detayını pasif yapar (soft delete)")
    public ResponseEntity<ProductInventoryDetailDto> deactivateProductDetail(
            @Parameter(description = "Ürün Detay ID") @PathVariable String id) {
        ProductInventoryDetailDto dto = service.deactivateProductDetail(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Ürün detayını aktif yap
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Ürün detayını aktif yap", description = "Ürün detayını aktif yapar")
    public ResponseEntity<ProductInventoryDetailDto> activateProductDetail(
            @Parameter(description = "Ürün Detay ID") @PathVariable String id) {
        ProductInventoryDetailDto dto = service.activateProductDetail(id);
        return ResponseEntity.ok(dto);
    }
}

