package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.InventoryCategoryDto;
import com.ay_za.ataylar_technic.entity.InventoryCategory;
import com.ay_za.ataylar_technic.mapper.InventoryCategoryMapper;
import com.ay_za.ataylar_technic.repository.InventoryCategoryRepository;
import com.ay_za.ataylar_technic.service.InventoryCategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryCategoryServiceImpl implements InventoryCategoryService {

    private final InventoryCategoryRepository repository;
    private final InventoryCategoryMapper mapper;

    public InventoryCategoryServiceImpl(InventoryCategoryRepository repository,
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

        // Category code kontrolü
        if (dto.getCategoryCode() != null && repository.existsByCategoryCode(dto.getCategoryCode())) {
            throw new IllegalArgumentException("Bu kategori kodu zaten kullanılıyor: " + dto.getCategoryCode());
        }

        // QR code kontrolü
        if (dto.getQrCode() != null && repository.existsByQrCode(dto.getQrCode())) {
            throw new IllegalArgumentException("Bu QR kodu zaten kullanılıyor: " + dto.getQrCode());
        }

        // Ana kategori kontrolü
        if (dto.getMainCategoryId() != null && !dto.getMainCategoryId().isEmpty()) {
            if (!repository.existsById(dto.getMainCategoryId())) {
                throw new IllegalArgumentException("Belirtilen ana kategori bulunamadı: " + dto.getMainCategoryId());
            }
            // Alt kategori ise isMainCategory false olmalı
            dto.setIsMainCategory(false);
        } else {
            // Ana kategori ise isMainCategory true olmalı
            dto.setIsMainCategory(true);
        }

        InventoryCategory entity = mapper.toEntity(dto);
        InventoryCategory saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public InventoryCategoryDto updateCategory(String id, InventoryCategoryDto dto) {
        InventoryCategory existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı: " + id));

        // Category code kontrolü (kendi dışında)
        if (dto.getCategoryCode() != null && !dto.getCategoryCode().equals(existing.getCategoryCode())) {
            if (repository.existsByCategoryCode(dto.getCategoryCode())) {
                throw new IllegalArgumentException("Bu kategori kodu zaten kullanılıyor: " + dto.getCategoryCode());
            }
        }

        // QR code kontrolü (kendi dışında)
        if (dto.getQrCode() != null && !dto.getQrCode().equals(existing.getQrCode())) {
            if (repository.existsByQrCode(dto.getQrCode())) {
                throw new IllegalArgumentException("Bu QR kodu zaten kullanılıyor: " + dto.getQrCode());
            }
        }

        // Ana kategori kontrolü
        if (dto.getMainCategoryId() != null && !dto.getMainCategoryId().isEmpty()) {
            if (!repository.existsById(dto.getMainCategoryId())) {
                throw new IllegalArgumentException("Belirtilen ana kategori bulunamadı: " + dto.getMainCategoryId());
            }
            dto.setIsMainCategory(false);
        } else {
            dto.setIsMainCategory(true);
        }

        // Güncelleme
        existing.setCategoryName(dto.getCategoryName());
        existing.setMainCategoryId(dto.getMainCategoryId());
        existing.setIsMainCategory(dto.getIsMainCategory());
        existing.setMarketCode(dto.getMarketCode());
        existing.setProductName(dto.getProductName());
        existing.setCategoryCode(dto.getCategoryCode());
        existing.setQrCode(dto.getQrCode());
        existing.setDescription(dto.getDescription());
        existing.setSortOrder(dto.getSortOrder());
        existing.setIsActive(dto.getIsActive());
        existing.setUpdatedBy(dto.getUpdatedBy());

        InventoryCategory updated = repository.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void deleteCategory(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Kategori bulunamadı: " + id);
        }

        // Alt kategorileri kontrol et
        List<InventoryCategory> subCategories = repository.findByMainCategoryId(id);
        if (!subCategories.isEmpty()) {
            throw new IllegalArgumentException("Bu kategorinin alt kategorileri var. Önce alt kategorileri silmelisiniz.");
        }

        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryCategoryDto getCategoryById(String id) {
        InventoryCategory entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı: " + id));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getAllCategories() {
        List<InventoryCategory> entities = repository.findAll();
        return mapper.toDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getMainCategories() {
        List<InventoryCategory> entities = repository.findMainCategories();
        return mapper.toDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getSubCategoriesByMainCategoryId(String mainCategoryId) {
        List<InventoryCategory> entities = repository.findByMainCategoryId(mainCategoryId);
        return mapper.toDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getCategoryHierarchy() {
        // Tüm kategorileri getir
        List<InventoryCategory> allCategories = repository.findAllOrderedByHierarchy();

        // Ana kategorileri filtrele
        List<InventoryCategory> mainCategories = allCategories.stream()
                .filter(c -> c.getIsMainCategory() == null || c.getIsMainCategory() || c.getMainCategoryId() == null)
                .collect(Collectors.toList());

        // Her ana kategori için alt kategorileri ekle
        List<InventoryCategoryDto> result = new ArrayList<>();
        for (InventoryCategory mainCategory : mainCategories) {
            List<InventoryCategory> subCategories = allCategories.stream()
                    .filter(c -> mainCategory.getId().equals(c.getMainCategoryId()))
                    .collect(Collectors.toList());

            InventoryCategoryDto dto = mapper.toDtoWithSubCategories(mainCategory, subCategories);
            result.add(dto);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryCategoryDto getCategoryWithSubCategories(String id) {
        InventoryCategory entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı: " + id));

        List<InventoryCategory> subCategories = repository.findByMainCategoryId(id);
        return mapper.toDtoWithSubCategories(entity, subCategories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getActiveCategories() {
        List<InventoryCategory> entities = repository.findByIsActiveTrue();
        return mapper.toDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getActiveMainCategories() {
        List<InventoryCategory> entities = repository.findActiveMainCategories();
        return mapper.toDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getActiveSubCategoriesByMainCategoryId(String mainCategoryId) {
        List<InventoryCategory> entities = repository.findActiveSubCategoriesByMainCategoryId(mainCategoryId);
        return mapper.toDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryCategoryDto getCategoryByCategoryCode(String categoryCode) {
        InventoryCategory entity = repository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new IllegalArgumentException("Kategori kodu bulunamadı: " + categoryCode));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryCategoryDto getCategoryByQrCode(String qrCode) {
        InventoryCategory entity = repository.findByQrCode(qrCode)
                .orElseThrow(() -> new IllegalArgumentException("QR kodu bulunamadı: " + qrCode));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> searchCategoriesByName(String categoryName) {
        List<InventoryCategory> entities = repository.findByCategoryNameContainingIgnoreCase(categoryName);
        return mapper.toDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryCategoryDto> getCategoriesByMarketCode(String marketCode) {
        List<InventoryCategory> entities = repository.findByMarketCode(marketCode);
        return mapper.toDtoList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCategoryCode(String categoryCode) {
        return repository.existsByCategoryCode(categoryCode);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByQrCode(String qrCode) {
        return repository.existsByQrCode(qrCode);
    }
}

