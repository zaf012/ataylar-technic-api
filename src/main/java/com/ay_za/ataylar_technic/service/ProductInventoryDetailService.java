package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.ProductInventoryCategoryDto;
import com.ay_za.ataylar_technic.dto.ProductInventoryDetailDto;
import com.ay_za.ataylar_technic.entity.ProductInventoryCategory;
import com.ay_za.ataylar_technic.entity.ProductInventoryDetail;
import com.ay_za.ataylar_technic.exception.ResourceNotFoundException;
import com.ay_za.ataylar_technic.mapper.ProductInventoryDetailMapper;
import com.ay_za.ataylar_technic.repository.ProductInventoryCategoryRepository;
import com.ay_za.ataylar_technic.repository.ProductInventoryDetailRepository;
import com.ay_za.ataylar_technic.service.base.ProductInventoryCategoryServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Ürün Envanter Detayı Service
 */
@Service
@Transactional
public class ProductInventoryDetailService {

    private final ProductInventoryDetailRepository repository;
    private final ProductInventoryCategoryRepository categoryRepository;
    private final ProductInventoryCategoryServiceImpl productInventoryCategoryServiceImpl;
    private final ProductInventoryDetailMapper mapper;

    public ProductInventoryDetailService(ProductInventoryDetailRepository repository,
                                         ProductInventoryCategoryRepository categoryRepository, ProductInventoryCategoryServiceImpl productInventoryCategoryServiceImpl,
                                         ProductInventoryDetailMapper mapper) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.productInventoryCategoryServiceImpl = productInventoryCategoryServiceImpl;
        this.mapper = mapper;
    }

    /**
     * Yeni ürün detayı oluştur
     */
    public ProductInventoryDetailDto createProductDetail(ProductInventoryDetailDto dto) {
        // Gerekli alanları kontrol et
        validateProductDetail(dto);

        ProductInventoryCategoryDto categoryById = productInventoryCategoryServiceImpl.getById(dto.getCategoryId());

        if (categoryById == null) {
            throw new ResourceNotFoundException("Kategori bulunamadı: " + dto.getCategoryId());
        } else {
            dto.setCategoryName(categoryById.getCategoryName());

        }

        // Varsayılan değerler
        if (dto.getActive() == null) {
            dto.setActive(true);
        }

        dto.setCreatedBy("System"); // TODO: SecurityContext'ten kullanıcı bilgisi alınacak
        dto.setCreatedDate(LocalDateTime.now());

        ProductInventoryDetail entity = mapper.toEntity(dto);
        ProductInventoryDetail savedEntity = repository.save(entity);

        return mapper.toDto(savedEntity);
    }

    /**
     * Ürün detayını güncelle
     */
    public ProductInventoryDetailDto updateProductDetail(String id, ProductInventoryDetailDto dto) {
        ProductInventoryDetail existingEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün detayı bulunamadı: " + id));

        // Gerekli alanları kontrol et
        validateProductDetail(dto);

        // Kategori kontrolü ve kategori adını set et
        ProductInventoryCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı: " + dto.getCategoryId()));

        dto.setCategoryName(category.getCategoryName());

        // Mevcut değerleri güncelle
        existingEntity.setCategoryId(dto.getCategoryId());
        existingEntity.setCategoryName(dto.getCategoryName());
        existingEntity.setMarketCode(dto.getMarketCode());
        existingEntity.setBrandName(dto.getBrandName());
        existingEntity.setProductName(dto.getProductName());
        existingEntity.setActive(dto.getActive() != null ? dto.getActive() : true);
        existingEntity.setUpdatedBy("System"); // TODO: SecurityContext'ten kullanıcı bilgisi alınacak

        ProductInventoryDetail updatedEntity = repository.save(existingEntity);

        return mapper.toDto(updatedEntity);
    }

    /**
     * ID'ye göre ürün detayı getir
     */
    @Transactional(readOnly = true)
    public ProductInventoryDetailDto getProductDetailById(String id) {
        ProductInventoryDetail entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün detayı bulunamadı: " + id));
        return mapper.toDto(entity);
    }

    /**
     * Tüm ürün detaylarını getir
     */
    @Transactional(readOnly = true)
    public List<ProductInventoryDetailDto> getAllProductDetails() {
        List<ProductInventoryDetail> entities = repository.findAll();
        return mapper.toDtoList(entities);
    }

    /**
     * Aktif ürün detaylarını getir
     */
    @Transactional(readOnly = true)
    public List<ProductInventoryDetailDto> getActiveProductDetails() {
        List<ProductInventoryDetail> entities = repository.findByActiveTrue();
        return mapper.toDtoList(entities);
    }

    /**
     * Kategoriye göre ürün detaylarını getir
     */
    @Transactional(readOnly = true)
    public List<ProductInventoryDetailDto> getProductDetailsByCategoryId(String categoryId) {
        List<ProductInventoryDetail> entities = repository.findByCategoryId(categoryId);
        return mapper.toDtoList(entities);
    }

    /**
     * Kategoriye göre aktif ürün detaylarını getir
     */
    @Transactional(readOnly = true)
    public List<ProductInventoryDetailDto> getActiveProductDetailsByCategoryId(String categoryId) {
        List<ProductInventoryDetail> entities = repository.findActiveByCategoryId(categoryId);
        return mapper.toDtoList(entities);
    }

    /**
     * Markaya göre ürün detaylarını getir
     */
    @Transactional(readOnly = true)
    public List<ProductInventoryDetailDto> getProductDetailsByBrandName(String brandName) {
        List<ProductInventoryDetail> entities = repository.findByBrandName(brandName);
        return mapper.toDtoList(entities);
    }

    /**
     * Ürün adına göre arama yap
     */
    @Transactional(readOnly = true)
    public List<ProductInventoryDetailDto> searchProductDetailsByProductName(String productName) {
        List<ProductInventoryDetail> entities = repository.searchByProductName(productName);
        return mapper.toDtoList(entities);
    }

    /**
     * Anahtar kelimeye göre arama yap (market kodu, ürün adı, marka)
     */
    @Transactional(readOnly = true)
    public List<ProductInventoryDetailDto> searchProductDetailsByKeyword(String keyword) {
        List<ProductInventoryDetail> entities = repository.searchByKeyword(keyword);
        return mapper.toDtoList(entities);
    }

    /**
     * Ürün detayını sil
     */
    public void deleteProductDetail(String id) {
        ProductInventoryDetail entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün detayı bulunamadı: " + id));
        repository.delete(entity);
    }

    /**
     * Ürün detayını pasif yap (soft delete)
     */
    public ProductInventoryDetailDto deactivateProductDetail(String id) {
        ProductInventoryDetail entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün detayı bulunamadı: " + id));

        entity.setActive(false);
        entity.setUpdatedBy("System"); // TODO: SecurityContext'ten kullanıcı bilgisi alınacak

        ProductInventoryDetail updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    /**
     * Ürün detayını aktif yap
     */
    public ProductInventoryDetailDto activateProductDetail(String id) {
        ProductInventoryDetail entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün detayı bulunamadı: " + id));

        entity.setActive(true);
        entity.setUpdatedBy("System"); // TODO: SecurityContext'ten kullanıcı bilgisi alınacak

        ProductInventoryDetail updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    /**
     * Ürün detayı validasyonu
     */
    private void validateProductDetail(ProductInventoryDetailDto dto) {
        if (dto.getCategoryId() == null || dto.getCategoryId().trim().isEmpty()) {
            throw new IllegalArgumentException("Kategori ID'si boş olamaz");
        }
    }
}

