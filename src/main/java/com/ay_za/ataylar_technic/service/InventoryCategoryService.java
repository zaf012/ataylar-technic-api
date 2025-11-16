package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.InventoryCategoryDto;
import com.ay_za.ataylar_technic.entity.InventoryCategory;
import com.ay_za.ataylar_technic.mapper.InventoryCategoryMapper;
import com.ay_za.ataylar_technic.repository.InventoryCategoryRepository;
import com.ay_za.ataylar_technic.service.base.InventoryCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class InventoryCategoryService implements InventoryCategoryServiceImpl {

    private final InventoryCategoryRepository repository;
    private final InventoryCategoryMapper mapper;

    public InventoryCategoryService(InventoryCategoryRepository repository,
                                    @Qualifier("inventoryCategoryCustomMapper") InventoryCategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public InventoryCategoryDto createCategory(InventoryCategoryDto dto) {
        // ID yoksa oluştur
        if (dto.getId() == null || dto.getId().isEmpty()) {
            dto.setId(UUID.randomUUID().toString());
        }

        // Üst kategori kontrolü - eğer varsa, o kategorinin var olduğundan emin ol
        if (dto.getMainCategoryId() != null && !dto.getMainCategoryId().isEmpty()) {
            if (!repository.existsById(dto.getMainCategoryId())) {
                throw new IllegalArgumentException("Belirtilen üst kategori bulunamadı: " + dto.getMainCategoryId());
            }
            // Üst kategorisi olan herhangi bir kategori, ana kategori değildir
            dto.setIsMainCategory(false);
        } else {
            // mainCategoryId null ise, bu en üst seviye kategoridir
            dto.setIsMainCategory(true);
        }

        dto.setCreatedBy("Admin");
        dto.setUpdatedBy("-");
        dto.setCreatedDate(LocalDateTime.now());
        dto.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        InventoryCategory entity = mapper.toEntity(dto);
        InventoryCategory saved = repository.save(entity);
        return enrichWithHierarchicalName(mapper.toDto(saved));
    }

    @Override
    public InventoryCategoryDto updateCategory(String id, InventoryCategoryDto dto) {
        InventoryCategory existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı: " + id));

        // Üst kategori kontrolü
        if (dto.getMainCategoryId() != null && !dto.getMainCategoryId().isEmpty()) {
            // Kendi kendisinin alt kategorisi olamaz
            if (dto.getMainCategoryId().equals(id)) {
                throw new IllegalArgumentException("Bir kategori kendi alt kategorisi olamaz");
            }

            // Üst kategorinin var olduğunu kontrol et
            if (!repository.existsById(dto.getMainCategoryId())) {
                throw new IllegalArgumentException("Belirtilen üst kategori bulunamadı: " + dto.getMainCategoryId());
            }

            // Circular reference kontrolü - güncellenen kategori, hedef üst kategorinin üstü olamaz
            if (wouldCreateCircularReference(id, dto.getMainCategoryId())) {
                throw new IllegalArgumentException("Bu işlem döngüsel bir referans oluşturur");
            }

            dto.setIsMainCategory(false);
        } else {
            // mainCategoryId null ise, bu en üst seviye kategoridir
            dto.setIsMainCategory(true);
        }

        // Güncelleme
        existing.setCategoryName(dto.getCategoryName());
        existing.setMainCategoryId(dto.getMainCategoryId());
        existing.setIsMainCategory(dto.getIsMainCategory());
        existing.setMarketCode(dto.getMarketCode());
        existing.setProductName(dto.getProductName());
        existing.setBrandName(dto.getBrandName());
        existing.setIsActive(dto.getIsActive());
        existing.setUpdatedBy("Admin");
        existing.setUpdatedDate(LocalDateTime.now());

        InventoryCategory updated = repository.save(existing);
        return enrichWithHierarchicalName(mapper.toDto(updated));
    }

    @Override
    public void deleteCategory(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Kategori bulunamadı: " + id);
        }

        // Alt kategorileri kontrol et (sadece doğrudan alt kategoriler)
        List<InventoryCategory> directSubCategories = repository.findByMainCategoryId(id);
        if (!directSubCategories.isEmpty()) {
            throw new IllegalArgumentException(
                "Bu kategorinin " + directSubCategories.size() +
                " adet alt kategorisi var. Önce alt kategorileri silmelisiniz."
            );
        }

        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryCategoryDto getCategoryById(String id) {
        InventoryCategory entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı: " + id));
        return enrichWithHierarchicalName(mapper.toDto(entity));
    }

    /**
     * Kategori ID'ye göre kategori entity getir
     */
    public Optional<InventoryCategory> getCategoryEntityById(String id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getAllCategories() {
        List<InventoryCategory> entities = repository.findAll();
        return enrichListWithHierarchicalNames(mapper.toDtoList(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getMainCategories() {
        List<InventoryCategory> entities = repository.findMainCategories();
        return enrichListWithHierarchicalNames(mapper.toDtoList(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getSubCategoriesByMainCategoryId(String mainCategoryId) {
        List<InventoryCategory> entities = repository.findByMainCategoryId(mainCategoryId);
        return enrichListWithHierarchicalNames(mapper.toDtoList(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getActiveCategories() {
        List<InventoryCategory> entities = repository.findByIsActiveTrue();
        return enrichListWithHierarchicalNames(mapper.toDtoList(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getActiveMainCategories() {
        List<InventoryCategory> entities = repository.findActiveMainCategories();
        return enrichListWithHierarchicalNames(mapper.toDtoList(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getActiveSubCategoriesByMainCategoryId(String mainCategoryId) {
        List<InventoryCategory> entities = repository.findActiveSubCategoriesByMainCategoryId(mainCategoryId);
        return enrichListWithHierarchicalNames(mapper.toDtoList(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> searchCategoriesByName(String categoryName) {
        List<InventoryCategory> entities = repository.findByCategoryNameContainingIgnoreCase(categoryName);
        return enrichListWithHierarchicalNames(mapper.toDtoList(entities));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getCategoriesByMarketCode(String marketCode) {
        List<InventoryCategory> entities = repository.findByMarketCode(marketCode);
        return enrichListWithHierarchicalNames(mapper.toDtoList(entities));
    }

    // Helper metodları

    /**
     * Circular reference kontrolü - bir kategori kendi üst kategorilerinden biri olamaz
     * Örnek: A -> B -> C yapısında, C'nin üst kategorisini A yapmak circular reference oluşturur
     */
    private boolean wouldCreateCircularReference(String categoryId, String newMainCategoryId) {
        String currentId = newMainCategoryId;

        // Maksimum 100 seviye kontrol et (sonsuz döngüyü önlemek için)
        int maxDepth = 100;
        int currentDepth = 0;

        while (currentId != null && !currentId.isEmpty() && currentDepth < maxDepth) {
            if (currentId.equals(categoryId)) {
                // Döngü bulundu!
                return true;
            }

            // Bir üst kategoriye çık
            InventoryCategory parent = repository.findById(currentId).orElse(null);
            if (parent == null) {
                break;
            }

            currentId = parent.getMainCategoryId();
            currentDepth++;
        }

        return false;
    }

    // Hiyerarşik kategori adı oluşturma metodları

    /**
     * Tek bir DTO için hiyerarşik kategori adı oluşturur
     */
    private InventoryCategoryDto enrichWithHierarchicalName(InventoryCategoryDto dto) {
        if (dto == null) {
            return null;
        }

        // Ana kategori ise (mainCategoryId yoksa) sadece kendi adını göster
        if (dto.getMainCategoryId() == null || dto.getMainCategoryId().isEmpty()) {
            // Zaten categoryName doğru
            return dto;
        }

        // Alt kategori ise, hiyerarşik adı oluştur
        String hierarchicalName = buildHierarchicalCategoryName(dto.getMainCategoryId(), dto.getCategoryName());
        dto.setCategoryName(hierarchicalName);

        return dto;
    }

    /**
     * DTO listesi için hiyerarşik kategori adları oluşturur
     */
    private List<InventoryCategoryDto> enrichListWithHierarchicalNames(List<InventoryCategoryDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return dtos;
        }

        return dtos.stream()
                .map(this::enrichWithHierarchicalName)
                .toList();
    }

    /**
     * Recursive olarak üst kategorilerden başlayarak hiyerarşik ad oluşturur
     *
     * Örnek hiyerarşi:
     * "Yangın Sistemleri > Yangın Pompa Sistemleri > Yangın Pompaları > Kapalı Genleşme Tankları > Nema"
     *
     * Bu metod sınırsız derinlikte kategori hiyerarşisini destekler.
     * Her kategori kendi mainCategoryId'si üzerinden bir üst kategoriye referans verir.
     */
    private String buildHierarchicalCategoryName(String mainCategoryId, String currentCategoryName) {
        if (mainCategoryId == null || mainCategoryId.isEmpty()) {
            return currentCategoryName;
        }

        try {
            InventoryCategory parentCategory = repository.findById(mainCategoryId).orElse(null);
            if (parentCategory == null) {
                return currentCategoryName;
            }

            // Üst kategorinin de üstü varsa, recursive olarak onun adını oluştur
            // Bu sayede sınırsız derinlikte kategori hiyerarşisi oluşturulabilir
            String parentHierarchicalName;
            if (parentCategory.getMainCategoryId() != null && !parentCategory.getMainCategoryId().isEmpty()) {
                // Recursive çağrı - bir üst kategorinin hiyerarşik adını oluştur
                parentHierarchicalName = buildHierarchicalCategoryName(
                        parentCategory.getMainCategoryId(),
                        parentCategory.getCategoryName()
                );
            } else {
                // Ana kategoriye ulaştık (en üst seviye, mainCategoryId == null)
                parentHierarchicalName = parentCategory.getCategoryName();
            }

            // Üst kategori hiyerarşisi + " > " + mevcut kategori adı
            return parentHierarchicalName + " > " + currentCategoryName;

        } catch (Exception e) {
            // Hata durumunda sadece mevcut kategori adını döndür
            return currentCategoryName;
        }
    }
}

