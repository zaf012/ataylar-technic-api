package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.SiteDeviceInventoryInfoDto;
import com.ay_za.ataylar_technic.entity.InventoryCategory;
import com.ay_za.ataylar_technic.entity.SiteDeviceInventoryInfo;
import com.ay_za.ataylar_technic.entity.SitesInfo;
import com.ay_za.ataylar_technic.mapper.SiteDeviceInventoryInfoMapper;
import com.ay_za.ataylar_technic.repository.InventoryCategoryRepository;
import com.ay_za.ataylar_technic.repository.SiteDeviceInventoryInfoRepository;
import com.ay_za.ataylar_technic.repository.SitesInfoRepository;
import com.ay_za.ataylar_technic.util.QrCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SiteDeviceInventoryInfoService {

    private final SiteDeviceInventoryInfoRepository siteDeviceInventoryInfoRepository;
    private final SiteDeviceInventoryInfoMapper siteDeviceInventoryInfoMapper;
    private final SitesInfoRepository sitesInfoRepository;
    private final InventoryCategoryRepository inventoryCategoryRepository;
    private final QrCodeGenerator qrCodeGenerator;

    public SiteDeviceInventoryInfoService(SiteDeviceInventoryInfoRepository siteDeviceInventoryInfoRepository,
                                          SiteDeviceInventoryInfoMapper siteDeviceInventoryInfoMapper,
                                          SitesInfoRepository sitesInfoRepository,
                                          InventoryCategoryRepository inventoryCategoryRepository,
                                          QrCodeGenerator qrCodeGenerator) {
        this.siteDeviceInventoryInfoRepository = siteDeviceInventoryInfoRepository;
        this.siteDeviceInventoryInfoMapper = siteDeviceInventoryInfoMapper;
        this.sitesInfoRepository = sitesInfoRepository;
        this.inventoryCategoryRepository = inventoryCategoryRepository;
        this.qrCodeGenerator = qrCodeGenerator;
    }


    /**
     * Yeni cihaz envanteri oluştur
     */
    @Transactional
    public SiteDeviceInventoryInfoDto createDeviceInventory(SiteDeviceInventoryInfoDto dto) {
        validateCreateRequest(dto);

        // Site bilgilerini al
        SitesInfo siteInfo = sitesInfoRepository.findById(dto.getSiteId())
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz site ID"));

        // Envanter kategori bilgilerini al
        InventoryCategory inventoryCategory = inventoryCategoryRepository.findById(dto.getInventoryCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz envanter kategori ID"));

        // QR kod oluştur
        String qrCode = generateUniqueQRCode();

        // Kategori hiyerarşisini oluştur
        String categoryHierarchy = buildCategoryHierarchy(inventoryCategory);

        // Entity oluştur
        SiteDeviceInventoryInfo entity = new SiteDeviceInventoryInfo(
                dto.getSiteId(),
                siteInfo.getSiteName(),
                dto.getAda(),
                dto.getBlockName(),
                dto.getApartmentNumber(),
                dto.getFloor(),
                dto.getLocation(),
                dto.getInventoryCategoryId(),
                inventoryCategory.getCategoryName(),
                categoryHierarchy,
                dto.getDeviceSpecification(),
                qrCode,
                dto.getCreatedBy()
        );

        SiteDeviceInventoryInfo savedEntity = siteDeviceInventoryInfoRepository.save(entity);
        return siteDeviceInventoryInfoMapper.convertToDTO(savedEntity);
    }

    /**
     * Cihaz envanteri güncelle
     */
    @Transactional
    public SiteDeviceInventoryInfoDto updateDeviceInventory(String id, SiteDeviceInventoryInfoDto dto) {
        SiteDeviceInventoryInfo existingEntity = siteDeviceInventoryInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cihaz bulunamadı"));

        validateUpdateRequest(dto, existingEntity);

        // Site bilgilerini güncelle (eğer site değişmişse)
        if (!existingEntity.getSiteId().equals(dto.getSiteId())) {
            SitesInfo siteInfo = sitesInfoRepository.findById(dto.getSiteId())
                    .orElseThrow(() -> new IllegalArgumentException("Geçersiz site ID"));
            existingEntity.setSiteName(siteInfo.getSiteName());
        }

        // Envanter kategori bilgilerini güncelle (eğer kategori değişmişse)
        if (!existingEntity.getInventoryCategoryId().equals(dto.getInventoryCategoryId())) {
            InventoryCategory inventoryCategory = inventoryCategoryRepository.findById(dto.getInventoryCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Geçersiz envanter kategori ID"));
            existingEntity.setSystemName(inventoryCategory.getCategoryName());
            existingEntity.setCategoryHierarchy(buildCategoryHierarchy(inventoryCategory));
        }

        // Diğer alanları güncelle
        existingEntity.setSiteId(dto.getSiteId());
        existingEntity.setAda(dto.getAda());
        existingEntity.setBlockName(dto.getBlockName());
        existingEntity.setApartmentNumber(dto.getApartmentNumber());
        existingEntity.setFloor(dto.getFloor());
        existingEntity.setLocation(dto.getLocation());
        existingEntity.setInventoryCategoryId(dto.getInventoryCategoryId());
        existingEntity.setDeviceSpecification(dto.getDeviceSpecification());
        existingEntity.setIsActive(dto.getIsActive());
        existingEntity.setUpdatedBy(dto.getUpdatedBy());

        SiteDeviceInventoryInfo savedEntity = siteDeviceInventoryInfoRepository.save(existingEntity);
        return siteDeviceInventoryInfoMapper.convertToDTO(savedEntity);
    }

    /**
     * ID'ye göre cihaz getir
     */
    public Optional<SiteDeviceInventoryInfoDto> getDeviceById(String id) {
        return siteDeviceInventoryInfoRepository.findById(id)
                .map(siteDeviceInventoryInfoMapper::convertToDTO);
    }

    /**
     * QR koda göre cihaz getir
     */
    public Optional<SiteDeviceInventoryInfoDto> getDeviceByQRCode(String qrCode) {
        return siteDeviceInventoryInfoRepository.findByQrCode(qrCode)
                .map(siteDeviceInventoryInfoMapper::convertToDTO);
    }

    /**
     * Tüm cihazları getir
     */
    public List<SiteDeviceInventoryInfoDto> getAllDevices() {
        List<SiteDeviceInventoryInfo> entities = siteDeviceInventoryInfoRepository.findAll();
        return siteDeviceInventoryInfoMapper.convertAllToDTO(entities);
    }

    /**
     * Aktif cihazları getir
     */
    public List<SiteDeviceInventoryInfoDto> getActiveDevices() {
        List<SiteDeviceInventoryInfo> entities = siteDeviceInventoryInfoRepository.findByIsActive(true);
        return siteDeviceInventoryInfoMapper.convertAllToDTO(entities);
    }

    /**
     * Site ID'ye göre cihazları getir
     */
    public List<SiteDeviceInventoryInfoDto> getDevicesBySiteId(String siteId) {
        List<SiteDeviceInventoryInfo> entities = siteDeviceInventoryInfoRepository.findBySiteId(siteId);
        return siteDeviceInventoryInfoMapper.convertAllToDTO(entities);
    }

    /**
     * Site, ada ve blok bilgisine göre cihazları getir
     */
    public List<SiteDeviceInventoryInfoDto> getDevicesBySiteAdaBlock(String siteId, String ada, String blockName) {
        List<SiteDeviceInventoryInfo> entities = siteDeviceInventoryInfoRepository
                .findBySiteIdAndAdaAndBlockName(siteId, ada, blockName);
        return siteDeviceInventoryInfoMapper.convertAllToDTO(entities);
    }

    /**
     * Kriterlere göre cihaz arama
     */
    public List<SiteDeviceInventoryInfoDto> searchDevices(String siteId, String ada, String blockName, Boolean isActive) {
        List<SiteDeviceInventoryInfo> entities = siteDeviceInventoryInfoRepository
                .findByCriteria(siteId, ada, blockName, isActive);
        return siteDeviceInventoryInfoMapper.convertAllToDTO(entities);
    }

    /**
     * Cihazı sil
     */
    @Transactional
    public void deleteDevice(String id) {
        if (!siteDeviceInventoryInfoRepository.existsById(id)) {
            throw new IllegalArgumentException("Cihaz bulunamadı");
        }
        siteDeviceInventoryInfoRepository.deleteById(id);
    }

    /**
     * Cihazı pasif yap
     */
    @Transactional
    public SiteDeviceInventoryInfoDto deactivateDevice(String id, String updatedBy) {
        SiteDeviceInventoryInfo entity = siteDeviceInventoryInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cihaz bulunamadı"));

        entity.setIsActive(false);
        entity.setUpdatedBy(updatedBy);

        SiteDeviceInventoryInfo savedEntity = siteDeviceInventoryInfoRepository.save(entity);
        return siteDeviceInventoryInfoMapper.convertToDTO(savedEntity);
    }

    /**
     * Dummy data oluştur
     */
    @Transactional
    public void createDefaultDevices() {
        try {
            // Mevcut site ve kategori verilerini al
            List<SitesInfo> sites = sitesInfoRepository.findAll();
            List<InventoryCategory> categories = inventoryCategoryRepository.findAll();

            if (sites.isEmpty() || categories.isEmpty()) {
                throw new IllegalStateException("Önce site ve envanter kategorileri oluşturulmalı");
            }

            // Örnek cihazlar oluştur
            createSampleDevice(sites.get(0), categories.get(0), "Ada", "123.Blok", "1A", 2, "Makine Isı Odası");
            createSampleDevice(sites.get(0), categories.get(0), "Ada", "456.Blok", null, -2, "Makine Dairesi");

            if (sites.size() > 1 && categories.size() > 1) {
                createSampleDevice(sites.get(1), categories.get(1), "Güzeller OSGB", "Ana Bina", null, 3, "Üst Stüdyo Çekim Alanı");
            }

        } catch (Exception e) {
            throw new RuntimeException("Dummy cihaz verileri oluşturulurken hata: " + e.getMessage());
        }
    }

    private void createSampleDevice(SitesInfo site, InventoryCategory category, String ada, String blockName,
                                    String apartmentNumber, Integer floor, String location) {
        String qrCode = generateUniqueQRCode();
        String categoryHierarchy = buildCategoryHierarchy(category);

        SiteDeviceInventoryInfo device = new SiteDeviceInventoryInfo(
                site.getId(),
                site.getSiteName(),
                ada,
                blockName,
                apartmentNumber,
                floor,
                location,
                category.getId(),
                category.getCategoryName(),
                categoryHierarchy,
                category.getDescription(),
                qrCode,
                "SYSTEM"
        );

        siteDeviceInventoryInfoRepository.save(device);
    }

    private void validateCreateRequest(SiteDeviceInventoryInfoDto dto) {
        if (dto.getSiteId() == null || dto.getSiteId().trim().isEmpty()) {
            throw new IllegalArgumentException("Site seçimi zorunludur");
        }
        if (dto.getAda() == null || dto.getAda().trim().isEmpty()) {
            throw new IllegalArgumentException("Ada bilgisi zorunludur");
        }
        if (dto.getBlockName() == null || dto.getBlockName().trim().isEmpty()) {
            throw new IllegalArgumentException("Blok adı zorunludur");
        }
        if (dto.getFloor() == null) {
            throw new IllegalArgumentException("Kat bilgisi zorunludur");
        }
        if (dto.getLocation() == null || dto.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Lokasyon bilgisi zorunludur");
        }
        if (dto.getInventoryCategoryId() == null || dto.getInventoryCategoryId().trim().isEmpty()) {
            throw new IllegalArgumentException("Envanter kategorisi seçimi zorunludur");
        }
    }

    private void validateUpdateRequest(SiteDeviceInventoryInfoDto dto, SiteDeviceInventoryInfo existingEntity) {
        validateCreateRequest(dto);

        // QR kod değiştirilmeye çalışılıyorsa kontrol et
        if (dto.getQrCode() != null && !dto.getQrCode().equals(existingEntity.getQrCode())) {
            if (siteDeviceInventoryInfoRepository.existsByQrCode(dto.getQrCode())) {
                throw new IllegalArgumentException("Bu QR kod başka bir cihaz tarafından kullanılıyor");
            }
        }
    }

    private String generateUniqueQRCode() {
        String qrCode;
        do {
            qrCode = qrCodeGenerator.generateQRCode();
        } while (siteDeviceInventoryInfoRepository.existsByQrCode(qrCode));
        return qrCode;
    }

    private String buildCategoryHierarchy(InventoryCategory category) {
        StringBuilder hierarchy = new StringBuilder();
        buildHierarchyRecursive(category, hierarchy);
        return hierarchy.toString();
    }

    private void buildHierarchyRecursive(InventoryCategory category, StringBuilder hierarchy) {
        // Ana kategorisi varsa önce onu ekle
        if (category.getMainCategoryId() != null && !category.getMainCategoryId().isEmpty()) {
            Optional<InventoryCategory> mainCategory = inventoryCategoryRepository.findById(category.getMainCategoryId());
            if (mainCategory.isPresent()) {
                buildHierarchyRecursive(mainCategory.get(), hierarchy);
                hierarchy.append(" > ");
            }
        }
        hierarchy.append(category.getCategoryName());
    }
}
