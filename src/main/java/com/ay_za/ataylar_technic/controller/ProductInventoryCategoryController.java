package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.ProductInventoryCategoryDto;
import com.ay_za.ataylar_technic.service.base.ProductInventoryCategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Ürün Envanter Kategorileri Controller
 */
@RestController
@RequestMapping("/api/product-inventory-categories")
@Tag(name = "Product Inventory Category", description = "Ürün Envanter Kategorileri API")
public class ProductInventoryCategoryController {

    private final ProductInventoryCategoryServiceImpl service;

    public ProductInventoryCategoryController(ProductInventoryCategoryServiceImpl service) {
        this.service = service;
    }

    /**
     * Yeni kategori oluştur
     */
    @PostMapping
    @Operation(summary = "Yeni kategori oluştur", description = "Yeni bir ürün envanter kategorisi oluşturur")
    public ResponseEntity<ProductInventoryCategoryDto> createCategory(
            @RequestBody ProductInventoryCategoryDto dto) {
        ProductInventoryCategoryDto created = service.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Kategori güncelle
     */
    @PutMapping("/{id}")
    @Operation(summary = "Kategori güncelle", description = "Mevcut bir kategoriyi günceller")
    public ResponseEntity<ProductInventoryCategoryDto> updateCategory(
            @Parameter(description = "Kategori ID") @PathVariable String id,
            @RequestBody ProductInventoryCategoryDto dto) {
        ProductInventoryCategoryDto updated = service.updateCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Kategori sil
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Kategori sil", description = "Belirtilen kategoriyi siler")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Kategori ID") @PathVariable String id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ID'ye göre kategori getir
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre kategori getir", description = "Belirtilen ID'ye sahip kategoriyi getirir")
    public ResponseEntity<ProductInventoryCategoryDto> getCategoryById(
            @Parameter(description = "Kategori ID") @PathVariable String id) {
        ProductInventoryCategoryDto category = service.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Tüm kategorileri getir
     */
    @GetMapping
    @Operation(summary = "Tüm kategorileri getir", description = "Tüm ürün envanter kategorilerini getirir")
    public ResponseEntity<List<ProductInventoryCategoryDto>> getAllCategories() {
        List<ProductInventoryCategoryDto> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Ana kategorileri getir
     */
    @GetMapping("/main")
    @Operation(summary = "Ana kategorileri getir", description = "Üst kategorisi olmayan ana kategorileri getirir")
    public ResponseEntity<List<ProductInventoryCategoryDto>> getMainCategories() {
        List<ProductInventoryCategoryDto> categories = service.getMainCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Alt kategorileri getir
     */
    @GetMapping("/sub/{parentId}")
    @Operation(summary = "Alt kategorileri getir", description = "Belirtilen üst kategoriye ait alt kategorileri getirir")
    public ResponseEntity<List<ProductInventoryCategoryDto>> getSubCategories(
            @Parameter(description = "Üst Kategori ID") @PathVariable String parentId) {
        List<ProductInventoryCategoryDto> categories = service.getSubCategories(parentId);
        return ResponseEntity.ok(categories);
    }

    /**
     * Hiyerarşik kategori ağacı getir
     */
    @GetMapping("/tree")
    @Operation(summary = "Kategori ağacı getir", description = "Hiyerarşik kategori ağacını getirir")
    public ResponseEntity<List<ProductInventoryCategoryDto>> getCategoryTree() {
        List<ProductInventoryCategoryDto> tree = service.getCategoryTree();
        return ResponseEntity.ok(tree);
    }

    /**
     * Aktif kategorileri getir
     */
    @GetMapping("/active")
    @Operation(summary = "Aktif kategorileri getir", description = "Sadece aktif kategorileri getirir")
    public ResponseEntity<List<ProductInventoryCategoryDto>> getActiveCategories() {
        List<ProductInventoryCategoryDto> categories = service.getActiveCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Kategori adına göre ara
     */
    @GetMapping("/search")
    @Operation(summary = "Kategori ara", description = "Kategori adına göre arama yapar")
    public ResponseEntity<List<ProductInventoryCategoryDto>> searchCategories(
            @Parameter(description = "Arama terimi") @RequestParam String name) {
        List<ProductInventoryCategoryDto> categories = service.searchCategories(name);
        return ResponseEntity.ok(categories);
    }
}

