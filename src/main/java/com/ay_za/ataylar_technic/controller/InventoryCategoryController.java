package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.InventoryCategoryDto;
import com.ay_za.ataylar_technic.service.InventoryCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-categories")
@Tag(name = "Inventory Categories", description = "Envanter kategori yönetimi - Hiyerarşik kategori sistemi")
public class InventoryCategoryController {

    private final InventoryCategoryService inventoryCategoryService;

    public InventoryCategoryController(InventoryCategoryService inventoryCategoryService) {
        this.inventoryCategoryService = inventoryCategoryService;
    }

    // ===== Temel CRUD Endpoints =====

    @GetMapping
    @Operation(summary = "Tüm kategorileri getir", description = "Sistemdeki tüm kategorileri döner")
    public ResponseEntity<List<InventoryCategoryDto>> getAllCategories() {
        List<InventoryCategoryDto> categories = inventoryCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/active")
    @Operation(summary = "Aktif kategorileri getir", description = "Sadece aktif olan kategorileri döner")
    public ResponseEntity<List<InventoryCategoryDto>> getActiveCategories() {
        List<InventoryCategoryDto> activeCategories = inventoryCategoryService.getActiveCategories();
        return ResponseEntity.ok(activeCategories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile kategori getir", description = "Belirtilen ID'ye sahip kategoriyi döner")
    public ResponseEntity<InventoryCategoryDto> getCategoryById(
            @Parameter(description = "Kategori ID'si") @PathVariable String id) {
        InventoryCategoryDto category = inventoryCategoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @Operation(summary = "Yeni kategori oluştur", description = "Yeni kategori oluşturur. QR kod otomatik oluşturulur.")
    public ResponseEntity<InventoryCategoryDto> createCategory(@RequestBody InventoryCategoryDto categoryDto) {
        InventoryCategoryDto createdCategory = inventoryCategoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Kategori güncelle", description = "Mevcut kategoriyi günceller")
    public ResponseEntity<InventoryCategoryDto> updateCategory(
            @Parameter(description = "Kategori ID'si") @PathVariable String id,
            @RequestBody InventoryCategoryDto categoryDto) {
        InventoryCategoryDto updatedCategory = inventoryCategoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Kategori sil", description = "Belirtilen kategoriyi siler. Alt kategorisi varsa silinemez.")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Kategori ID'si") @PathVariable String id) {
        inventoryCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // ===== Hiyerarşik Endpoints =====

    @GetMapping("/root")
    @Operation(summary = "Ana kategorileri getir", description = "Üst kategorisi olmayan ana kategorileri döner")
    public ResponseEntity<List<InventoryCategoryDto>> getRootCategories() {
        List<InventoryCategoryDto> rootCategories = inventoryCategoryService.getRootCategories();
        return ResponseEntity.ok(rootCategories);
    }

    @GetMapping("/root/active")
    @Operation(summary = "Aktif ana kategorileri getir", description = "Aktif olan ana kategorileri döner")
    public ResponseEntity<List<InventoryCategoryDto>> getActiveRootCategories() {
        List<InventoryCategoryDto> activeRootCategories = inventoryCategoryService.getActiveRootCategories();
        return ResponseEntity.ok(activeRootCategories);
    }

    @GetMapping("/{parentId}/subcategories")
    @Operation(summary = "Alt kategorileri getir", description = "Belirtilen kategorinin alt kategorilerini döner")
    public ResponseEntity<List<InventoryCategoryDto>> getSubCategories(
            @Parameter(description = "Üst kategori ID'si") @PathVariable String parentId) {
        List<InventoryCategoryDto> subCategories = inventoryCategoryService.getSubCategories(parentId);
        return ResponseEntity.ok(subCategories);
    }

    @GetMapping("/{parentId}/subcategories/active")
    @Operation(summary = "Aktif alt kategorileri getir", description = "Belirtilen kategorinin aktif alt kategorilerini döner")
    public ResponseEntity<List<InventoryCategoryDto>> getActiveSubCategories(
            @Parameter(description = "Üst kategori ID'si") @PathVariable String parentId) {
        List<InventoryCategoryDto> activeSubCategories = inventoryCategoryService.getActiveSubCategories(parentId);
        return ResponseEntity.ok(activeSubCategories);
    }

    @GetMapping("/{id}/with-subcategories")
    @Operation(summary = "Kategori ve alt kategorileri", description = "Kategoriyi alt kategorileriyle birlikte döner")
    public ResponseEntity<InventoryCategoryDto> getCategoryWithSubCategories(
            @Parameter(description = "Kategori ID'si") @PathVariable String id) {
        InventoryCategoryDto categoryWithSubs = inventoryCategoryService.getCategoryWithSubCategories(id);
        return ResponseEntity.ok(categoryWithSubs);
    }

    @GetMapping("/hierarchy")
    @Operation(summary = "Tam kategori hiyerarşisi", description = "Tüm kategori ağacını hiyerarşik yapıda döner")
    public ResponseEntity<List<InventoryCategoryDto>> getCategoryHierarchy() {
        List<InventoryCategoryDto> hierarchy = inventoryCategoryService.getCategoryHierarchy();
        return ResponseEntity.ok(hierarchy);
    }

    // ===== Arama Endpoints =====

    @GetMapping("/by-qr-code/{qrCode}")
    @Operation(summary = "QR kod ile kategori bul", description = "QR kod ile kategori arar")
    public ResponseEntity<InventoryCategoryDto> getCategoryByQrCode(
            @Parameter(description = "QR kod") @PathVariable String qrCode) {
        InventoryCategoryDto category = inventoryCategoryService.getCategoryByQrCode(qrCode);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/by-category-code/{categoryCode}")
    @Operation(summary = "Kategori kodu ile bul", description = "Kategori kodu ile kategori arar")
    public ResponseEntity<InventoryCategoryDto> getCategoryByCategoryCode(
            @Parameter(description = "Kategori kodu") @PathVariable String categoryCode) {
        InventoryCategoryDto category = inventoryCategoryService.getCategoryByCategoryCode(categoryCode);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/search")
    @Operation(summary = "İsme göre arama", description = "Kategori ismine göre arama yapar")
    public ResponseEntity<List<InventoryCategoryDto>> searchCategoriesByName(
            @Parameter(description = "Aranacak isim") @RequestParam String name) {
        List<InventoryCategoryDto> categories = inventoryCategoryService.searchCategoriesByName(name);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/by-level/{level}")
    @Operation(summary = "Seviyeye göre kategoriler", description = "Belirtilen seviyedeki kategorileri döner (0=ana kategori)")
    public ResponseEntity<List<InventoryCategoryDto>> getCategoriesByLevel(
            @Parameter(description = "Kategori seviyesi") @PathVariable Integer level) {
        List<InventoryCategoryDto> categories = inventoryCategoryService.getCategoriesByLevel(level);
        return ResponseEntity.ok(categories);
    }

    // ===== Yardımcı Endpoints =====

    @GetMapping("/generate-qr-code")
    @Operation(summary = "Yeni QR kod oluştur", description = "Benzersiz QR kod oluşturur")
    public ResponseEntity<String> generateUniqueQrCode() {
        String qrCode = inventoryCategoryService.generateUniqueQrCode();
        return ResponseEntity.ok(qrCode);
    }

    @GetMapping("/validate-qr-code/{qrCode}")
    @Operation(summary = "QR kod benzersizlik kontrolü", description = "QR kodun benzersiz olup olmadığını kontrol eder")
    public ResponseEntity<Boolean> isQrCodeUnique(
            @Parameter(description = "Kontrol edilecek QR kod") @PathVariable String qrCode) {
        boolean isUnique = inventoryCategoryService.isQrCodeUnique(qrCode);
        return ResponseEntity.ok(isUnique);
    }

    @GetMapping("/validate-category-code/{categoryCode}")
    @Operation(summary = "Kategori kodu benzersizlik kontrolü", description = "Kategori kodunun benzersiz olup olmadığını kontrol eder")
    public ResponseEntity<Boolean> isCategoryCodeUnique(
            @Parameter(description = "Kontrol edilecek kategori kodu") @PathVariable String categoryCode) {
        boolean isUnique = inventoryCategoryService.isCategoryCodeUnique(categoryCode);
        return ResponseEntity.ok(isUnique);
    }

    @GetMapping("/{parentId}/subcategory-count")
    @Operation(summary = "Alt kategori sayısı", description = "Belirtilen kategorinin alt kategori sayısını döner")
    public ResponseEntity<Long> getSubCategoryCount(
            @Parameter(description = "Üst kategori ID'si") @PathVariable String parentId) {
        Long count = inventoryCategoryService.getSubCategoryCount(parentId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/can-delete")
    @Operation(summary = "Silinebilir mi kontrolü", description = "Kategorinin silinip silinemeyeceğini kontrol eder")
    public ResponseEntity<Boolean> canDeleteCategory(
            @Parameter(description = "Kategori ID'si") @PathVariable String id) {
        boolean canDelete = inventoryCategoryService.canDeleteCategory(id);
        return ResponseEntity.ok(canDelete);
    }

    // ===== Dummy Data Endpoint =====

    @PostMapping("/create-default-data")
    @Operation(
        summary = "Örnek envanter kategorileri oluştur",
        description = "Yangın Sistemleri, Pompa Sistemleri, Jeneratör Sistemleri vs. ile " +
                     "tam hiyerarşik kategori yapısını oluşturur. Ekran görüntülerindeki " +
                     "kategori yapısına uygun örnek veriler ekler."
    )
    public ResponseEntity<String> createDefaultData() {
        String result = inventoryCategoryService.createDefaultCategoriesAndData();
        return ResponseEntity.ok(result);
    }
}
