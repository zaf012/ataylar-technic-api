package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.ProductInventoryCategoryDto;
import com.ay_za.ataylar_technic.entity.ProductInventoryCategory;
import com.ay_za.ataylar_technic.exception.ResourceNotFoundException;
import com.ay_za.ataylar_technic.mapper.ProductInventoryCategoryMapper;
import com.ay_za.ataylar_technic.repository.ProductInventoryCategoryRepository;
import com.ay_za.ataylar_technic.service.base.ProductInventoryCategoryServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Ürün Envanter Kategorileri Service
 */
@Service
@Transactional
public class ProductInventoryCategoryService implements ProductInventoryCategoryServiceImpl {

    private final ProductInventoryCategoryRepository repository;
    private final ProductInventoryCategoryMapper mapper;

    public ProductInventoryCategoryService(ProductInventoryCategoryRepository repository,
                                           ProductInventoryCategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Yeni kategori oluştur
     */
    @Transactional
    @Override
    public ProductInventoryCategoryDto createCategory(ProductInventoryCategoryDto dto) {
        // Kategori adı kontrolü
        if (dto.getCategoryName() == null || dto.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("Kategori adı boş olamaz");
        }

        // Üst kategori varsa, kontrol et
        if (dto.getParentCategoryId() != null && !dto.getParentCategoryId().trim().isEmpty()) {
            Optional<ProductInventoryCategory> parentCategory = repository.findById(dto.getParentCategoryId());
            if (parentCategory.isEmpty()) {
                throw new ResourceNotFoundException("Üst kategori bulunamadı: " + dto.getParentCategoryId());
            }

            // Üst kategorinin seviyesine göre bu kategorinin seviyesini belirle
            Integer parentLevel = parentCategory.get().getCategoryLevel();
            dto.setCategoryLevel(parentLevel != null ? parentLevel + 1 : 2);

            // Üst kategori adını set et
            dto.setParentCategoryName(parentCategory.get().getCategoryName());
        } else {
            // Ana kategori - seviye 1
            dto.setCategoryLevel(1);
            dto.setParentCategoryId(null);
            dto.setParentCategoryName(null);
        }

        // Varsayılan değerler
        if (dto.getIsActive() == null) {
            dto.setIsActive(true);
        }
        if (dto.getDisplayOrder() == null) {
            dto.setDisplayOrder(0);
        }

        dto.setCreatedBy("System"); // TODO: SecurityContext'ten kullanıcı bilgisi alınabilir
        dto.setCreatedDate(LocalDateTime.now());

        ProductInventoryCategory entity = mapper.toEntity(dto);
        ProductInventoryCategory savedEntity = repository.save(entity);

        return mapper.toDto(savedEntity);
    }

    /**
     * Kategori güncelle
     */
    @Transactional
    @Override
    public ProductInventoryCategoryDto updateCategory(String id, ProductInventoryCategoryDto dto) {
        ProductInventoryCategory existingEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı: " + id));


        // Üst kategori değiştiriliyorsa
        if (dto.getParentCategoryId() != null && !dto.getParentCategoryId().equals(existingEntity.getParentCategoryId())) {
            // Kendi kendisinin alt kategorisi olamaz
            if (dto.getParentCategoryId().equals(id)) {
                throw new IllegalArgumentException("Bir kategori kendi alt kategorisi olamaz");
            }

            // Döngüsel bağımlılık kontrolü
            if (wouldCreateCircularDependency(id, dto.getParentCategoryId())) {
                throw new IllegalArgumentException("Bu işlem döngüsel bağımlılık oluşturur");
            }

            Optional<ProductInventoryCategory> parentCategory = repository.findById(dto.getParentCategoryId());
            if (parentCategory.isEmpty()) {
                throw new ResourceNotFoundException("Üst kategori bulunamadı: " + dto.getParentCategoryId());
            }

            Integer parentLevel = parentCategory.get().getCategoryLevel();
            dto.setCategoryLevel(parentLevel != null ? parentLevel + 1 : 2);
            dto.setParentCategoryName(parentCategory.get().getCategoryName());
        }

        dto.setUpdatedBy("System"); // TODO: SecurityContext'ten kullanıcı bilgisi alınabilir
        dto.setUpdatedDate(LocalDateTime.now());

        mapper.updateEntityFromDto(dto, existingEntity);
        ProductInventoryCategory updatedEntity = repository.save(existingEntity);

        return mapper.toDto(updatedEntity);
    }

    /**
     * Kategori sil
     */
    @Transactional
    @Override
    public void deleteCategory(String id) {
        ProductInventoryCategory category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı: " + id));

        // Alt kategorisi var mı kontrol et
        if (repository.hasSubCategories(id)) {
            throw new IllegalArgumentException("Bu kategorinin alt kategorileri var, önce onları silmelisiniz");
        }

        repository.delete(category);
    }

    /**
     * ID'ye göre kategori getir
     */
    @Transactional(readOnly = true)
    @Override
    public ProductInventoryCategoryDto getCategoryById(String id) {
        ProductInventoryCategory entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı: " + id));

        ProductInventoryCategoryDto dto = mapper.toDto(entity);

        // Üst kategori adını set et
        if (entity.getParentCategoryId() != null) {
            repository.findById(entity.getParentCategoryId())
                    .ifPresent(parent -> dto.setParentCategoryName(parent.getCategoryName()));
        }

        return dto;
    }

    /**
     * Tüm kategorileri getir
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductInventoryCategoryDto> getAllCategories() {
        List<ProductInventoryCategory> entities = repository.findAll();
        return enrichDtosWithParentNames(mapper.toDtoList(entities));
    }

    /**
     * Ana kategorileri getir
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductInventoryCategoryDto> getMainCategories() {
        List<ProductInventoryCategory> entities = repository.findMainCategories();
        return mapper.toDtoList(entities);
    }

    /**
     * Alt kategorileri getir
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductInventoryCategoryDto> getSubCategories(String parentId) {
        List<ProductInventoryCategory> entities = repository.findByParentCategoryId(parentId);
        return mapper.toDtoList(entities);
    }

    /**
     * Hiyerarşik kategori ağacı getir
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductInventoryCategoryDto> getCategoryTree() {
        List<ProductInventoryCategory> allCategories = repository.findAll();
        Map<String, ProductInventoryCategoryDto> categoryMap = new HashMap<>();

        // Tüm kategorileri DTO'ya çevir ve map'e ekle
        for (ProductInventoryCategory entity : allCategories) {
            ProductInventoryCategoryDto dto = mapper.toDto(entity);
            categoryMap.put(entity.getId(), dto);
        }

        // Ana kategorileri bul ve alt kategorileri ekle
        List<ProductInventoryCategoryDto> rootCategories = new ArrayList<>();

        for (ProductInventoryCategoryDto dto : categoryMap.values()) {
            if (dto.getParentCategoryId() == null) {
                // Ana kategori
                rootCategories.add(dto);
            } else {
                // Alt kategori - parent'a ekle
                ProductInventoryCategoryDto parent = categoryMap.get(dto.getParentCategoryId());
                if (parent != null) {
                    if (parent.getSubCategories() == null) {
                        parent.setSubCategories(new ArrayList<>());
                    }
                    parent.getSubCategories().add(dto);
                }
            }
        }

        // Sıralama
        sortCategoriesRecursively(rootCategories);

        return rootCategories;
    }

    /**
     * Aktif kategorileri getir
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductInventoryCategoryDto> getActiveCategories() {
        List<ProductInventoryCategory> entities = repository.findByIsActiveTrue();
        return enrichDtosWithParentNames(mapper.toDtoList(entities));
    }

    /**
     * Kategori adına göre ara
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductInventoryCategoryDto> searchCategories(String name) {
        List<ProductInventoryCategory> entities = repository.searchByCategoryName(name);
        return enrichDtosWithParentNames(mapper.toDtoList(entities));
    }


    // ========== Yardımcı Metodlar ==========

    /**
     * Döngüsel bağımlılık kontrolü
     */
    private boolean wouldCreateCircularDependency(String categoryId, String newParentId) {
        if (newParentId == null) {
            return false;
        }

        Set<String> visited = new HashSet<>();
        String currentId = newParentId;

        while (currentId != null) {
            if (currentId.equals(categoryId)) {
                return true; // Döngü bulundu
            }

            if (visited.contains(currentId)) {
                return false; // Zaten kontrol edilmiş
            }

            visited.add(currentId);

            Optional<ProductInventoryCategory> current = repository.findById(currentId);
            if (current.isEmpty()) {
                return false;
            }

            currentId = current.get().getParentCategoryId();
        }

        return false;
    }

    /**
     * DTO listesine üst kategori adlarını ekle
     */
    private List<ProductInventoryCategoryDto> enrichDtosWithParentNames(List<ProductInventoryCategoryDto> dtos) {
        Set<String> parentIds = dtos.stream()
                .map(ProductInventoryCategoryDto::getParentCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (parentIds.isEmpty()) {
            return dtos;
        }

        Map<String, String> parentNames = new HashMap<>();
        for (String parentId : parentIds) {
            repository.findById(parentId)
                    .ifPresent(parent -> parentNames.put(parentId, parent.getCategoryName()));
        }

        dtos.forEach(dto -> {
            if (dto.getParentCategoryId() != null) {
                dto.setParentCategoryName(parentNames.get(dto.getParentCategoryId()));
            }
        });

        return dtos;
    }

    /**
     * Kategorileri ve alt kategorilerini sırala
     */
    private void sortCategoriesRecursively(List<ProductInventoryCategoryDto> categories) {
        if (categories == null || categories.isEmpty()) {
            return;
        }

        categories.sort(Comparator
                .comparing(ProductInventoryCategoryDto::getDisplayOrder, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(ProductInventoryCategoryDto::getCategoryName, Comparator.nullsLast(Comparator.naturalOrder())));

        for (ProductInventoryCategoryDto category : categories) {
            if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
                sortCategoriesRecursively(category.getSubCategories());
            }
        }
    }

    @Override
    public ProductInventoryCategoryDto getById(String id) {
        Optional<ProductInventoryCategory> inventoryCategory = repository.findById(id);
        return inventoryCategory.map(mapper::toDto).orElse(null);
    }
}

