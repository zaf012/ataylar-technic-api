package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.SiteDeviceInventoryInfoDto;
import com.ay_za.ataylar_technic.entity.*;
import com.ay_za.ataylar_technic.mapper.SiteDeviceInventoryInfoMapper;
import com.ay_za.ataylar_technic.repository.SiteDeviceInventoryInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiteDeviceInventoryInfoService {

    private final SiteDeviceInventoryInfoRepository siteDeviceInventoryInfoRepository;
    private final SiteDeviceInventoryInfoMapper siteDeviceInventoryInfoMapper;
    private final SitesInfoService sitesInfoService;
    private final BlocksInfoService blocksInfoService;
    private final SystemInfoService systemInfoService;
    private final InventoryCategoryService inventoryCategoryService;

    public SiteDeviceInventoryInfoService(SiteDeviceInventoryInfoRepository siteDeviceInventoryInfoRepository,
                                          SiteDeviceInventoryInfoMapper siteDeviceInventoryInfoMapper,
                                          SitesInfoService sitesInfoService,
                                          BlocksInfoService blocksInfoService,
                                          SystemInfoService systemInfoService,
                                          InventoryCategoryService inventoryCategoryService) {
        this.siteDeviceInventoryInfoRepository = siteDeviceInventoryInfoRepository;
        this.siteDeviceInventoryInfoMapper = siteDeviceInventoryInfoMapper;
        this.sitesInfoService = sitesInfoService;
        this.blocksInfoService = blocksInfoService;
        this.systemInfoService = systemInfoService;
        this.inventoryCategoryService = inventoryCategoryService;
    }

    /**
     * Yeni cihaz envanteri oluştur
     */
    @Transactional
    public SiteDeviceInventoryInfoDto createDeviceInventory(SiteDeviceInventoryInfoDto dto) {
        validateCreateRequest(dto);

        // Site bilgilerini servis üzerinden al
        SitesInfo siteInfo = sitesInfoService.getSiteEntityById(dto.getSiteId())
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz site ID"));

        // Blok bilgilerini servis üzerinden al (içinde square bilgileri de var)
        BlocksInfo blockInfo = blocksInfoService.getBlockEntityById(dto.getBlockId())
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz blok ID"));

        // Sistem bilgilerini servis üzerinden al
        SystemInfo systemInfo = systemInfoService.getSystemEntityById(dto.getSystemId())
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz sistem ID"));

        // Envanter kategori bilgilerini servis üzerinden al
        InventoryCategory inventoryCategory = inventoryCategoryService.getCategoryEntityById(dto.getInventoryCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz envanter kategori ID"));

        // QR kod kontrolü
        if (siteDeviceInventoryInfoRepository.existsByQrCode(dto.getQrCode())) {
            throw new IllegalArgumentException("Bu QR kod zaten kullanılıyor");
        }

        // Kategori hiyerarşisini oluştur
        String categoryHierarchy = buildCategoryHierarchy(inventoryCategory);

        // Entity oluştur
        SiteDeviceInventoryInfo entity = new SiteDeviceInventoryInfo(
                dto.getSiteId(),
                siteInfo.getSiteName(),
                blockInfo.getSquareId(),
                blockInfo.getSquareName(),
                dto.getBlockId(),
                blockInfo.getBlockName(),
                dto.getDoorNo(),
                dto.getFloor(),
                dto.getLocation(),
                dto.getSystemId(),
                systemInfo.getSystemName(),
                dto.getInventoryCategoryId(),
                categoryHierarchy,
                inventoryCategory.getCategoryName(),
                dto.getQrCode(),
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
            SitesInfo siteInfo = sitesInfoService.getSiteEntityById(dto.getSiteId())
                    .orElseThrow(() -> new IllegalArgumentException("Geçersiz site ID"));
            existingEntity.setSiteName(siteInfo.getSiteName());
        }

        // Blok bilgilerini güncelle (eğer blok değişmişse)
        if (!existingEntity.getBlockId().equals(dto.getBlockId())) {
            BlocksInfo blockInfo = blocksInfoService.getBlockEntityById(dto.getBlockId())
                    .orElseThrow(() -> new IllegalArgumentException("Geçersiz blok ID"));
            existingEntity.setSquareId(blockInfo.getSquareId());
            existingEntity.setSquareName(blockInfo.getSquareName());
            existingEntity.setBlockName(blockInfo.getBlockName());
        }

        // Sistem bilgilerini güncelle (eğer sistem değişmişse)
        if (!existingEntity.getSystemId().equals(dto.getSystemId())) {
            SystemInfo systemInfo = systemInfoService.getSystemEntityById(dto.getSystemId())
                    .orElseThrow(() -> new IllegalArgumentException("Geçersiz sistem ID"));
            existingEntity.setSystemName(systemInfo.getSystemName());
        }

        // Envanter kategori bilgilerini güncelle (eğer kategori değişmişse)
        if (!existingEntity.getInventoryCategoryId().equals(dto.getInventoryCategoryId())) {
            InventoryCategory inventoryCategory = inventoryCategoryService.getCategoryEntityById(dto.getInventoryCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Geçersiz envanter kategori ID"));
            existingEntity.setInventoryCategoryName(buildCategoryHierarchy(inventoryCategory));
            existingEntity.setProductName(inventoryCategory.getCategoryName());
        }

        // Diğer alanları güncelle
        existingEntity.setSiteId(dto.getSiteId());
        existingEntity.setBlockId(dto.getBlockId());
        existingEntity.setDoorNo(dto.getDoorNo());
        existingEntity.setFloor(dto.getFloor());
        existingEntity.setLocation(dto.getLocation());
        existingEntity.setSystemId(dto.getSystemId());
        existingEntity.setInventoryCategoryId(dto.getInventoryCategoryId());
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
     * Site, square ve blok bilgisine göre cihazları getir
     */
    public List<SiteDeviceInventoryInfoDto> getDevicesBySiteSquareBlock(String siteId, String squareId, String blockId) {
        List<SiteDeviceInventoryInfo> entities = siteDeviceInventoryInfoRepository
                .findBySiteIdAndSquareIdAndBlockId(siteId, squareId, blockId);
        return siteDeviceInventoryInfoMapper.convertAllToDTO(entities);
    }

    /**
     * Kriterlere göre cihaz arama
     */
    public List<SiteDeviceInventoryInfoDto> searchDevices(String siteId, String squareId, String blockId, Boolean isActive) {
        List<SiteDeviceInventoryInfo> entities = siteDeviceInventoryInfoRepository
                .findByCriteria(siteId, squareId, blockId, isActive);
        return siteDeviceInventoryInfoMapper.convertAllToDTO(entities);
    }

    /**
     * Sistem ID'ye göre cihazları getir
     */
    public List<SiteDeviceInventoryInfoDto> getDevicesBySystemId(String systemId) {
        List<SiteDeviceInventoryInfo> entities = siteDeviceInventoryInfoRepository.findBySystemId(systemId);
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
     * Oluşturma validasyonu
     */
    private void validateCreateRequest(SiteDeviceInventoryInfoDto dto) {
        if (dto.getSiteId() == null || dto.getSiteId().trim().isEmpty()) {
            throw new IllegalArgumentException("Site seçimi zorunludur");
        }
        if (dto.getBlockId() == null || dto.getBlockId().trim().isEmpty()) {
            throw new IllegalArgumentException("Blok seçimi zorunludur");
        }
        if (dto.getSystemId() == null || dto.getSystemId().trim().isEmpty()) {
            throw new IllegalArgumentException("Sistem seçimi zorunludur");
        }
        if (dto.getInventoryCategoryId() == null || dto.getInventoryCategoryId().trim().isEmpty()) {
            throw new IllegalArgumentException("Envanter kategorisi seçimi zorunludur");
        }
        if (dto.getQrCode() == null || dto.getQrCode().trim().isEmpty()) {
            throw new IllegalArgumentException("QR kod zorunludur");
        }
        if (dto.getQrCode().length() != 12) {
            throw new IllegalArgumentException("QR kod 10 basamaklı olmalıdır");
        }
    }

    /**
     * Güncelleme validasyonu
     */
    private void validateUpdateRequest(SiteDeviceInventoryInfoDto dto, SiteDeviceInventoryInfo existingEntity) {
        validateCreateRequest(dto);

        // QR kod değiştirilmeye çalışılıyorsa kontrol et
        if (dto.getQrCode() != null && !dto.getQrCode().equals(existingEntity.getQrCode())) {
            if (siteDeviceInventoryInfoRepository.existsByQrCode(dto.getQrCode())) {
                throw new IllegalArgumentException("Bu QR kod başka bir cihaz tarafından kullanılıyor");
            }
        }
    }

    /**
     * Kategori hiyerarşisini oluştur
     */
    private String buildCategoryHierarchy(InventoryCategory category) {
        StringBuilder hierarchy = new StringBuilder();
        buildHierarchyRecursive(category, hierarchy);
        return hierarchy.toString();
    }

    /**
     * Rekürsif olarak kategori hiyerarşisini oluştur
     */
    private void buildHierarchyRecursive(InventoryCategory category, StringBuilder hierarchy) {
        // Ana kategorisi varsa önce onu ekle
        if (category.getMainCategoryId() != null && !category.getMainCategoryId().isEmpty()) {
            Optional<InventoryCategory> mainCategory = inventoryCategoryService.getCategoryEntityById(category.getMainCategoryId());
            if (mainCategory.isPresent()) {
                buildHierarchyRecursive(mainCategory.get(), hierarchy);
                hierarchy.append(" > ");
            }
        }
        hierarchy.append(category.getCategoryName());
    }
}
