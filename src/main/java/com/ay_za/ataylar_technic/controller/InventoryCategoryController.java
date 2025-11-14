package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.InventoryCategoryDto;
import com.ay_za.ataylar_technic.service.InventoryCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-categories")
@Tag(name = "Inventory Category", description = "Envanter Kategori Yönetimi")
public class InventoryCategoryController {

    private final InventoryCategoryService service;

    public InventoryCategoryController(InventoryCategoryService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Yeni kategori oluştur", description = "Ana kategori veya alt kategori oluşturur")
    public ResponseEntity<InventoryCategoryDto> createCategory(@RequestBody InventoryCategoryDto dto) {
        InventoryCategoryDto created = service.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Kategori güncelle", description = "Mevcut bir kategoriyi günceller")
    public ResponseEntity<InventoryCategoryDto> updateCategory(
            @PathVariable String id,
            @RequestBody InventoryCategoryDto dto) {
        InventoryCategoryDto updated = service.updateCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Kategori sil", description = "Kategoriyi siler (alt kategorisi yoksa)")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Kategori detayı", description = "ID'ye göre kategori getirir")
    public ResponseEntity<InventoryCategoryDto> getCategoryById(@PathVariable String id) {
        InventoryCategoryDto category = service.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    @Operation(summary = "Tüm kategorileri listele", description = "Tüm kategorileri getirir")
    public ResponseEntity<List<InventoryCategoryDto>> getAllCategories() {
        List<InventoryCategoryDto> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/main")
    @Operation(summary = "Ana kategorileri listele", description = "Sadece ana kategorileri getirir")
    public ResponseEntity<List<InventoryCategoryDto>> getMainCategories() {
        List<InventoryCategoryDto> categories = service.getMainCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/sub-categories/{mainCategoryId}")
    @Operation(summary = "Alt kategorileri listele", description = "Belirli bir ana kategorinin alt kategorilerini getirir")
    public ResponseEntity<List<InventoryCategoryDto>> getSubCategories(@PathVariable String mainCategoryId) {
        List<InventoryCategoryDto> categories = service.getSubCategoriesByMainCategoryId(mainCategoryId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/hierarchy")
    @Operation(summary = "Hiyerarşik yapı", description = "Tüm kategorileri hiyerarşik yapıda getirir")
    public ResponseEntity<List<InventoryCategoryDto>> getCategoryHierarchy() {
        List<InventoryCategoryDto> hierarchy = service.getCategoryHierarchy();
        return ResponseEntity.ok(hierarchy);
    }

    @GetMapping("/{id}/with-subs")
    @Operation(summary = "Alt kategorilerle birlikte getir", description = "Kategoriyi alt kategorileriyle birlikte getirir")
    public ResponseEntity<InventoryCategoryDto> getCategoryWithSubCategories(@PathVariable String id) {
        InventoryCategoryDto category = service.getCategoryWithSubCategories(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/active")
    @Operation(summary = "Aktif kategorileri listele", description = "Sadece aktif kategorileri getirir")
    public ResponseEntity<List<InventoryCategoryDto>> getActiveCategories() {
        List<InventoryCategoryDto> categories = service.getActiveCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/active/main")
    @Operation(summary = "Aktif ana kategorileri listele", description = "Sadece aktif ana kategorileri getirir")
    public ResponseEntity<List<InventoryCategoryDto>> getActiveMainCategories() {
        List<InventoryCategoryDto> categories = service.getActiveMainCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/active/sub-categories/{mainCategoryId}")
    @Operation(summary = "Aktif alt kategorileri listele", description = "Belirli bir ana kategorinin aktif alt kategorilerini getirir")
    public ResponseEntity<List<InventoryCategoryDto>> getActiveSubCategories(@PathVariable String mainCategoryId) {
        List<InventoryCategoryDto> categories = service.getActiveSubCategoriesByMainCategoryId(mainCategoryId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/by-code/{categoryCode}")
    @Operation(summary = "Kategori koduna göre getir", description = "Kategori koduna göre kategori getirir")
    public ResponseEntity<InventoryCategoryDto> getCategoryByCategoryCode(@PathVariable String categoryCode) {
        InventoryCategoryDto category = service.getCategoryByCategoryCode(categoryCode);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/by-qr/{qrCode}")
    @Operation(summary = "QR koduna göre getir", description = "QR koduna göre kategori getirir")
    public ResponseEntity<InventoryCategoryDto> getCategoryByQrCode(@PathVariable String qrCode) {
        InventoryCategoryDto category = service.getCategoryByQrCode(qrCode);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/search")
    @Operation(summary = "İsme göre ara", description = "Kategori adına göre arama yapar")
    public ResponseEntity<List<InventoryCategoryDto>> searchCategoriesByName(@RequestParam String name) {
        List<InventoryCategoryDto> categories = service.searchCategoriesByName(name);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/by-market/{marketCode}")
    @Operation(summary = "Market koduna göre getir", description = "Market koduna göre kategorileri getirir")
    public ResponseEntity<List<InventoryCategoryDto>> getCategoriesByMarketCode(@PathVariable String marketCode) {
        List<InventoryCategoryDto> categories = service.getCategoriesByMarketCode(marketCode);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/exists/code/{categoryCode}")
    @Operation(summary = "Kategori kodu kontrolü", description = "Kategori kodunun kullanılıp kullanılmadığını kontrol eder")
    public ResponseEntity<Boolean> existsByCategoryCode(@PathVariable String categoryCode) {
        boolean exists = service.existsByCategoryCode(categoryCode);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/qr/{qrCode}")
    @Operation(summary = "QR kodu kontrolü", description = "QR kodunun kullanılıp kullanılmadığını kontrol eder")
    public ResponseEntity<Boolean> existsByQrCode(@PathVariable String qrCode) {
        boolean exists = service.existsByQrCode(qrCode);
        return ResponseEntity.ok(exists);
    }
}

