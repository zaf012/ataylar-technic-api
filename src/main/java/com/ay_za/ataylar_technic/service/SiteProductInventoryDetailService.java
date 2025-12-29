package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.SiteProductInventoryDetailDto;
import com.ay_za.ataylar_technic.entity.*;
import com.ay_za.ataylar_technic.exception.ResourceNotFoundException;
import com.ay_za.ataylar_technic.mapper.SiteProductInventoryDetailMapper;
import com.ay_za.ataylar_technic.repository.*;
import com.ay_za.ataylar_technic.service.base.SiteProductInventoryDetailServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Site Ürün Envanter Detayı Service
 */
@Service
public class SiteProductInventoryDetailService implements SiteProductInventoryDetailServiceImpl {

    private final SiteProductInventoryDetailRepository repository;
    private final SitesInfoRepository sitesInfoRepository;
    private final SquaresInfoRepository squaresInfoRepository;
    private final BlocksInfoRepository blocksInfoRepository;
    private final SystemInfoRepository systemInfoRepository;
    private final ProductInventoryCategoryRepository categoryRepository;
    private final ProductInventoryDetailRepository productInventoryDetailRepository;
    private final SiteProductInventoryDetailMapper mapper;

    public SiteProductInventoryDetailService(
            SiteProductInventoryDetailRepository repository,
            SitesInfoRepository sitesInfoRepository,
            SquaresInfoRepository squaresInfoRepository,
            BlocksInfoRepository blocksInfoRepository,
            SystemInfoRepository systemInfoRepository,
            ProductInventoryCategoryRepository categoryRepository,
            ProductInventoryDetailRepository productInventoryDetailRepository,
            SiteProductInventoryDetailMapper mapper) {
        this.repository = repository;
        this.sitesInfoRepository = sitesInfoRepository;
        this.squaresInfoRepository = squaresInfoRepository;
        this.blocksInfoRepository = blocksInfoRepository;
        this.systemInfoRepository = systemInfoRepository;
        this.categoryRepository = categoryRepository;
        this.productInventoryDetailRepository = productInventoryDetailRepository;
        this.mapper = mapper;
    }

    /**
     * Yeni site ürün detayı oluştur
     */
    @Transactional
    @Override
    public SiteProductInventoryDetailDto createSiteProductDetail(SiteProductInventoryDetailDto dto) {
        // Gerekli alanları kontrol et
        validateSiteProductDetail(dto);

        // İlişkili kayıtları kontrol et ve name alanlarını set et
        validateAndSetRelatedEntities(dto);

        // QR kod benzersizlik kontrolü (Frontend'den gönderiliyor)
        if (repository.existsByQrCode(dto.getQrCode())) {
            throw new IllegalArgumentException("Bu QR kod zaten kullanılıyor: " + dto.getQrCode());
        }

        // Varsayılan değerler
        if (dto.getActive() == null) {
            dto.setActive(true);
        }

        dto.setCreatedBy("System"); // TODO: SecurityContext'ten kullanıcı bilgisi alınacak
        dto.setCreatedDate(LocalDateTime.now());

        SiteProductInventoryDetail entity = mapper.toEntity(dto);
        SiteProductInventoryDetail savedEntity = repository.save(entity);

        return mapper.toDto(savedEntity);
    }

    /**
     * Site ürün detayını güncelle
     */
    @Transactional
    @Override
    public SiteProductInventoryDetailDto updateSiteProductDetail(String id, SiteProductInventoryDetailDto dto) {
        SiteProductInventoryDetail existingEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site ürün detayı bulunamadı: " + id));

        // Gerekli alanları kontrol et
        validateSiteProductDetail(dto);

        // İlişkili kayıtları kontrol et ve name alanlarını set et
        validateAndSetRelatedEntities(dto);

        // QR kod değiştirilmez, mevcut QR kod korunur (Frontend'den gelen ignore edilir)

        // Mevcut değerleri güncelle
        existingEntity.setSiteId(dto.getSiteId());
        existingEntity.setSiteName(dto.getSiteName());
        existingEntity.setSquareId(dto.getSquareId());
        existingEntity.setSquareName(dto.getSquareName());
        existingEntity.setBlockId(dto.getBlockId());
        existingEntity.setBlockName(dto.getBlockName());
        existingEntity.setFloorNumber(dto.getFloorNumber());
        existingEntity.setLocation(dto.getLocation());
        existingEntity.setSystemId(dto.getSystemId());
        existingEntity.setSystemName(dto.getSystemName());
        existingEntity.setCategoryId(dto.getCategoryId());
        existingEntity.setCategoryName(dto.getCategoryName());
        existingEntity.setProductInventoryDetailId(dto.getProductInventoryDetailId());
        existingEntity.setProductName(dto.getProductName());
        existingEntity.setBrandName(dto.getBrandName());
        existingEntity.setProductPurpose(dto.getProductPurpose());
        existingEntity.setActive(dto.getActive() != null ? dto.getActive() : true);
        existingEntity.setUpdatedBy("System"); // TODO: SecurityContext'ten kullanıcı bilgisi alınacak

        SiteProductInventoryDetail updatedEntity = repository.save(existingEntity);

        return mapper.toDto(updatedEntity);
    }

    /**
     * ID'ye göre site ürün detayı getir
     */
    @Transactional(readOnly = true)
    @Override
    public SiteProductInventoryDetailDto getSiteProductDetailById(String id) {
        SiteProductInventoryDetail entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site ürün detayı bulunamadı: " + id));
        return mapper.toDto(entity);
    }

    /**
     * QR koduna göre site ürün detayı getir
     */
    @Transactional(readOnly = true)
    @Override
    public SiteProductInventoryDetailDto getSiteProductDetailByQrCode(String qrCode) {
        SiteProductInventoryDetail entity = repository.findByQrCode(qrCode)
                .orElseThrow(() -> new ResourceNotFoundException("Site ürün detayı bulunamadı. QR kod: " + qrCode));
        return mapper.toDto(entity);
    }

    /**
     * Tüm site ürün detaylarını getir
     */
    @Transactional(readOnly = true)
    @Override
    public List<SiteProductInventoryDetailDto> getAllSiteProductDetails() {
        List<SiteProductInventoryDetail> entities = repository.findAll();
        return mapper.toDtoList(entities);
    }

    /**
     * Aktif site ürün detaylarını getir
     */
    @Transactional(readOnly = true)
    @Override
    public List<SiteProductInventoryDetailDto> getActiveSiteProductDetails() {
        List<SiteProductInventoryDetail> entities = repository.findByActiveTrue();
        return mapper.toDtoList(entities);
    }

    /**
     * Site ürün detayını sil
     */
    @Transactional
    @Override
    public void deleteSiteProductDetail(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Site ürün detayı bulunamadı: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Site ürün detayını pasif yap (soft delete)
     */
    @Override
    public SiteProductInventoryDetailDto deactivateSiteProductDetail(String id) {
        SiteProductInventoryDetail entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site ürün detayı bulunamadı: " + id));

        entity.setActive(false);
        entity.setUpdatedBy("System"); // TODO: SecurityContext'ten kullanıcı bilgisi alınacak

        SiteProductInventoryDetail updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    /**
     * Site ürün detayını validasyona tabi tut
     */
    private void validateSiteProductDetail(SiteProductInventoryDetailDto dto) {
        if (dto.getQrCode() == null || dto.getQrCode().trim().isEmpty()) {
            throw new IllegalArgumentException("QR kod zorunludur");
        }
        if (dto.getSiteId() == null || dto.getSiteId().trim().isEmpty()) {
            throw new IllegalArgumentException("Site ID zorunludur");
        }
        if (dto.getSquareId() == null || dto.getSquareId().trim().isEmpty()) {
            throw new IllegalArgumentException("Square ID zorunludur");
        }
        if (dto.getBlockId() == null || dto.getBlockId().trim().isEmpty()) {
            throw new IllegalArgumentException("Block ID zorunludur");
        }
        if (dto.getSystemId() == null || dto.getSystemId().trim().isEmpty()) {
            throw new IllegalArgumentException("Sistem ID zorunludur");
        }
        if (dto.getCategoryId() == null || dto.getCategoryId().trim().isEmpty()) {
            throw new IllegalArgumentException("Kategori ID zorunludur");
        }
        if (dto.getProductInventoryDetailId() == null || dto.getProductInventoryDetailId().trim().isEmpty()) {
            throw new IllegalArgumentException("Ürün Envanter Detay ID zorunludur");
        }
    }

    /**
     * İlişkili entity'leri kontrol et ve name alanlarını set et
     */
    private void validateAndSetRelatedEntities(SiteProductInventoryDetailDto dto) {
        // Site kontrolü
        SitesInfo site = sitesInfoRepository.findById(dto.getSiteId())
                .orElseThrow(() -> new ResourceNotFoundException("Site bulunamadı: " + dto.getSiteId()));
        dto.setSiteName(site.getSiteName());

        // Square kontrolü
        SquaresInfo square = squaresInfoRepository.findById(dto.getSquareId())
                .orElseThrow(() -> new ResourceNotFoundException("Square bulunamadı: " + dto.getSquareId()));
        dto.setSquareName(square.getSquareName());

        // Block kontrolü
        BlocksInfo block = blocksInfoRepository.findById(dto.getBlockId())
                .orElseThrow(() -> new ResourceNotFoundException("Block bulunamadı: " + dto.getBlockId()));
        dto.setBlockName(block.getBlockName());

        // System kontrolü
        SystemInfo system = systemInfoRepository.findById(dto.getSystemId())
                .orElseThrow(() -> new ResourceNotFoundException("Sistem bulunamadı: " + dto.getSystemId()));
        dto.setSystemName(system.getSystemName());

        // Category kontrolü
        ProductInventoryCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı: " + dto.getCategoryId()));
        dto.setCategoryName(category.getCategoryName());

        // Product Inventory Detail kontrolü
        ProductInventoryDetail productDetail = productInventoryDetailRepository.findById(dto.getProductInventoryDetailId())
                .orElseThrow(() -> new ResourceNotFoundException("Ürün envanter detayı bulunamadı: " + dto.getProductInventoryDetailId()));
        dto.setProductName(productDetail.getProductName());
    }
}

