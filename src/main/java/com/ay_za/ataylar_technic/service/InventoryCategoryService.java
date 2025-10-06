package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.InventoryCategoryDto;
import com.ay_za.ataylar_technic.entity.InventoryCategory;
import com.ay_za.ataylar_technic.exception.*;
import com.ay_za.ataylar_technic.mapper.InventoryCategoryMapper;
import com.ay_za.ataylar_technic.repository.InventoryCategoryRepository;
import com.ay_za.ataylar_technic.service.base.InventoryCategoryServiceImpl;
import com.ay_za.ataylar_technic.util.QrCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InventoryCategoryService implements InventoryCategoryServiceImpl {

    private final InventoryCategoryRepository categoryRepository;
    private final InventoryCategoryMapper categoryMapper;
    private final QrCodeGenerator qrCodeGenerator;

    public InventoryCategoryService(InventoryCategoryRepository categoryRepository,
                                  InventoryCategoryMapper categoryMapper,
                                  QrCodeGenerator qrCodeGenerator) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    // Temel CRUD işlemleri
    @Override
    public List<InventoryCategoryDto> getAllCategories() {
        List<InventoryCategory> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryCategoryDto> getActiveCategories() {
        List<InventoryCategory> activeCategories = categoryRepository.findByIsActiveTrueOrderBySortOrder();
        return activeCategories.stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryCategoryDto getCategoryById(String id) {
        validateCategoryId(id);
        InventoryCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryMapper.convertToDTO(category);
    }

    @Override
    @Transactional
    public InventoryCategoryDto createCategory(InventoryCategoryDto categoryDto) {
        try {
            // Input validations
            validateCategoryDto(categoryDto);

            // ID oluştur
            categoryDto.setId(UUID.randomUUID().toString());

            // Unique QR kod oluştur
            categoryDto.setQrCode(generateUniqueQrCode());

            // Parent category ve level ayarları
            InventoryCategory parentCategory = null;
            if (categoryDto.getParentCategoryId() != null) {
                parentCategory = categoryRepository.findById(categoryDto.getParentCategoryId())
                        .orElseThrow(() -> new CategoryNotFoundException("Parent kategori", categoryDto.getParentCategoryId()));
                categoryDto.setLevel(parentCategory.getLevel() + 1);
            } else {
                categoryDto.setLevel(0); // Root category
            }

            // Sort order ayarla
            if (categoryDto.getSortOrder() == null) {
                Integer maxSortOrder;
                if (parentCategory != null) {
                    maxSortOrder = categoryRepository.findMaxSortOrderByParentId(parentCategory.getId());
                } else {
                    maxSortOrder = categoryRepository.findMaxSortOrderForRootCategories();
                }
                categoryDto.setSortOrder(maxSortOrder != null ? maxSortOrder + 10 : 10);
            }

            // Varsayılan aktiflik
            if (categoryDto.getIsActive() == null) {
                categoryDto.setIsActive(true);
            }

            // Aynı parent altında aynı isim kontrolü
            validateCategoryNameUniqueness(categoryDto.getCategoryName(), categoryDto.getParentCategoryId(), null);

            // Kategori kodu kontrolü
            if (categoryDto.getCategoryCode() != null && !isCategoryCodeUnique(categoryDto.getCategoryCode())) {
                throw CategoryValidationException.categoryCodeAlreadyExists(categoryDto.getCategoryCode());
            }

            // Entity'ye dönüştür ve parent'ı set et
            InventoryCategory categoryEntity = categoryMapper.convertToEntity(categoryDto);
            if (parentCategory != null) {
                categoryEntity.setParentCategory(parentCategory);
            }

            InventoryCategory savedCategory = categoryRepository.save(categoryEntity);
            return categoryMapper.convertToDTO(savedCategory);

        } catch (InventoryCategoryException e) {
            throw e; // Custom exception'ları olduğu gibi fırlat
        } catch (Exception e) {
            throw new InventoryCategoryException("Kategori oluşturulurken beklenmeyen bir hata oluştu: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public InventoryCategoryDto updateCategory(String id, InventoryCategoryDto categoryDto) {
        try {
            validateCategoryId(id);
            validateCategoryDto(categoryDto);

            InventoryCategory existingCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new CategoryNotFoundException(id));

            // Aynı parent altında aynı isim kontrolü (kendisi hariç)
            validateCategoryNameUniqueness(categoryDto.getCategoryName(),
                    existingCategory.getParentCategory() != null ? existingCategory.getParentCategory().getId() : null,
                    id);

            // Kategori kodu kontrolü (değiştirilmişse)
            if (categoryDto.getCategoryCode() != null &&
                !categoryDto.getCategoryCode().equals(existingCategory.getCategoryCode()) &&
                !isCategoryCodeUnique(categoryDto.getCategoryCode())) {
                throw CategoryValidationException.categoryCodeAlreadyExists(categoryDto.getCategoryCode());
            }

            // Güncelleme
            existingCategory.setCategoryName(categoryDto.getCategoryName());
            existingCategory.setCategoryCode(categoryDto.getCategoryCode());
            existingCategory.setDescription(categoryDto.getDescription());
            existingCategory.setSortOrder(categoryDto.getSortOrder());
            existingCategory.setIsActive(categoryDto.getIsActive());
            existingCategory.setUpdatedBy(categoryDto.getUpdatedBy());

            InventoryCategory updatedCategory = categoryRepository.save(existingCategory);
            return categoryMapper.convertToDTO(updatedCategory);

        } catch (InventoryCategoryException e) {
            throw e;
        } catch (Exception e) {
            throw new InventoryCategoryException("Kategori güncellenirken beklenmeyen bir hata oluştu: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteCategory(String id) {
        try {
            validateCategoryId(id);

            InventoryCategory category = categoryRepository.findById(id)
                    .orElseThrow(() -> new CategoryNotFoundException(id));

            Long subCategoryCount = getSubCategoryCount(id);
            if (subCategoryCount > 0) {
                throw new CategoryDeletionException(category.getCategoryName(), subCategoryCount.intValue());
            }

            categoryRepository.deleteById(id);

        } catch (InventoryCategoryException e) {
            throw e;
        } catch (Exception e) {
            throw new InventoryCategoryException("Kategori silinirken beklenmeyen bir hata oluştu: " + e.getMessage(), e);
        }
    }

    // Hiyerarşik işlemler
    @Override
    public List<InventoryCategoryDto> getRootCategories() {
        List<InventoryCategory> rootCategories = categoryRepository.findRootCategories();
        return rootCategories.stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryCategoryDto> getActiveRootCategories() {
        List<InventoryCategory> activeRootCategories = categoryRepository.findActiveRootCategories();
        return activeRootCategories.stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryCategoryDto> getSubCategories(String parentId) {
        List<InventoryCategory> subCategories = categoryRepository.findByParentCategoryId(parentId);
        return subCategories.stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryCategoryDto> getActiveSubCategories(String parentId) {
        List<InventoryCategory> activeSubCategories = categoryRepository.findActiveByParentCategoryId(parentId);
        return activeSubCategories.stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryCategoryDto getCategoryWithSubCategories(String id) {
        InventoryCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı: " + id));

        InventoryCategoryDto categoryDto = categoryMapper.convertToDTO(category);

        // Alt kategorileri yükle
        List<InventoryCategoryDto> subCategories = getSubCategories(id);
        categoryDto.setSubCategories(subCategories);

        return categoryDto;
    }

    @Override
    public List<InventoryCategoryDto> getCategoryHierarchy() {
        List<InventoryCategory> rootCategories = categoryRepository.findActiveRootCategories();

        return rootCategories.stream()
                .map(this::buildCategoryHierarchy)
                .collect(Collectors.toList());
    }

    private InventoryCategoryDto buildCategoryHierarchy(InventoryCategory category) {
        InventoryCategoryDto categoryDto = categoryMapper.convertToDTO(category);

        // Alt kategorileri recursive olarak yükle
        List<InventoryCategory> subCategories = categoryRepository.findActiveByParentCategoryId(category.getId());

        if (!subCategories.isEmpty()) {
            List<InventoryCategoryDto> subCategoryDtos = subCategories.stream()
                    .map(this::buildCategoryHierarchy)
                    .collect(Collectors.toList());
            categoryDto.setSubCategories(subCategoryDtos);
        }

        return categoryDto;
    }

    // Arama işlemleri
    @Override
    public InventoryCategoryDto getCategoryByQrCode(String qrCode) {
        validateQrCode(qrCode);
        InventoryCategory category = categoryRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new CategoryNotFoundException("QR kod", qrCode));
        return categoryMapper.convertToDTO(category);
    }

    @Override
    public InventoryCategoryDto getCategoryByCategoryCode(String categoryCode) {
        validateCategoryCode(categoryCode);
        InventoryCategory category = categoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new CategoryNotFoundException("Kategori kodu", categoryCode));
        return categoryMapper.convertToDTO(category);
    }

    @Override
    public List<InventoryCategoryDto> searchCategoriesByName(String name) {
        List<InventoryCategory> categories = categoryRepository.findByCategoryNameContaining(name);
        return categories.stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryCategoryDto> getCategoriesByLevel(Integer level) {
        List<InventoryCategory> categories = categoryRepository.findByLevel(level);
        return categories.stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    // Yardımcı işlemler
    @Override
    public String generateUniqueQrCode() {
        String qrCode;
        int attempts = 0;
        int maxAttempts = 100;

        do {
            qrCode = qrCodeGenerator.generateQrCode();
            attempts++;
            if (attempts > maxAttempts) {
                throw new QrCodeGenerationException(attempts);
            }
        } while (categoryRepository.existsByQrCode(qrCode));

        return qrCode;
    }

    @Override
    public boolean isQrCodeUnique(String qrCode) {
        return !categoryRepository.existsByQrCode(qrCode);
    }

    @Override
    public boolean isCategoryCodeUnique(String categoryCode) {
        return !categoryRepository.existsByCategoryCode(categoryCode);
    }

    @Override
    public Long getSubCategoryCount(String parentId) {
        return categoryRepository.countByParentCategoryId(parentId);
    }

    // Validation işlemleri
    @Override
    public boolean canDeleteCategory(String id) {
        validateCategoryId(id);
        Long subCategoryCount = getSubCategoryCount(id);
        return subCategoryCount == 0;
    }

    @Override
    public boolean canMoveCategory(String categoryId, String newParentId) {
        validateCategoryId(categoryId);

        // Kendi kendine parent olamaz
        if (categoryId.equals(newParentId)) {
            return false;
        }

        // Alt kategorilerinden birine parent olamaz (circular reference)
        if (newParentId != null) {
            validateCategoryId(newParentId);
            return !isDescendant(categoryId, newParentId);
        }

        return true;
    }

    private boolean isDescendant(String ancestorId, String descendantId) {
        InventoryCategory category = categoryRepository.findById(descendantId).orElse(null);

        while (category != null && category.getParentCategory() != null) {
            if (category.getParentCategory().getId().equals(ancestorId)) {
                return true;
            }
            category = category.getParentCategory();
        }

        return false;
    }

    private void validateCategoryNameUniqueness(String categoryName, String parentId, String excludeId) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw CategoryValidationException.invalidCategoryName(categoryName);
        }

        boolean exists;
        if (parentId != null) {
            exists = categoryRepository.findByParentCategoryIdAndCategoryNameIgnoreCase(parentId, categoryName)
                    .filter(category -> excludeId == null || !category.getId().equals(excludeId))
                    .isPresent();
        } else {
            exists = categoryRepository.findRootByCategoryNameIgnoreCase(categoryName)
                    .filter(category -> excludeId == null || !category.getId().equals(excludeId))
                    .isPresent();
        }

        if (exists) {
            if (parentId != null) {
                InventoryCategory parentCategory = categoryRepository.findById(parentId).orElse(null);
                String parentName = parentCategory != null ? parentCategory.getCategoryName() : "Bilinmeyen";
                throw new CategoryAlreadyExistsException(categoryName, parentName);
            } else {
                throw new CategoryAlreadyExistsException(categoryName);
            }
        }
    }

    // ===== VALIDATION METHODS =====

    private void validateCategoryId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new CategoryValidationException("Kategori ID'si boş olamaz");
        }
    }

    private void validateCategoryDto(InventoryCategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new CategoryValidationException("Kategori bilgileri boş olamaz");
        }

        if (categoryDto.getCategoryName() == null || categoryDto.getCategoryName().trim().isEmpty()) {
            throw CategoryValidationException.invalidCategoryName(categoryDto.getCategoryName());
        }

        // QR kod formatı kontrolü (eğer manuel girilmişse)
        if (categoryDto.getQrCode() != null && !qrCodeGenerator.isValidQrCodeFormat(categoryDto.getQrCode())) {
            throw CategoryValidationException.invalidQrCodeFormat(categoryDto.getQrCode());
        }
    }

    private void validateQrCode(String qrCode) {
        if (qrCode == null || qrCode.trim().isEmpty()) {
            throw new CategoryValidationException("QR kod boş olamaz");
        }

        if (!qrCodeGenerator.isValidQrCodeFormat(qrCode)) {
            throw CategoryValidationException.invalidQrCodeFormat(qrCode);
        }
    }

    private void validateCategoryCode(String categoryCode) {
        if (categoryCode == null || categoryCode.trim().isEmpty()) {
            throw new CategoryValidationException("Kategori kodu boş olamaz");
        }
    }

    // ===== DUMMY DATA METHODS =====

    public String createDefaultCategoriesAndData() {
        try {
            createFireSystemCategories();
            createPumpAndHydroforCategories();
            createGeneratorCategories();
            createUpsCategories();
            createClimateCategories();

            return "Envanter kategorileri başarıyla oluşturuldu! " +
                   "Yangın Sistemleri, Pompa ve Hidrofor Sistemleri, Jeneratör Sistemleri, " +
                   "UPS Sistemleri ve Klima/VRF sistemleri kategorileri eklendi.";
        } catch (Exception e) {
            return "Dummy kategori verileri oluşturulurken hata: " + e.getMessage();
        }
    }

    private void createFireSystemCategories() {
        // Ana kategori: Yangın Sistemleri
        InventoryCategoryDto yanginSistemleri = createRootCategory("Yangın Sistemleri", "FIRE_SYS", "Yangın güvenlik sistemleri");

        // Alt kategoriler
        InventoryCategoryDto yanginPompaSistemleri = createSubCategory("Yangın Pompa Sistemleri", "FIRE_PUMP", yanginSistemleri.getId(), "Yangın pompa sistemleri");

        // Yangın Jokey Panoları
        InventoryCategoryDto yanginJokeyPanolari = createSubCategory("Yangın Jokey Panoları", "FIRE_JOKEY_PANEL", yanginPompaSistemleri.getId(), "Yangın jokey panoları");
        createBrandSubCategories(yanginJokeyPanolari.getId(), "JOKEY_PANEL");

        // Yangın Jokey Pompaları
        InventoryCategoryDto yanginJokeyPompalari = createSubCategory("Yangın Jokey Pompaları", "FIRE_JOKEY_PUMP", yanginPompaSistemleri.getId(), "Yangın jokey pompaları");
        createBrandSubCategories(yanginJokeyPompalari.getId(), "JOKEY_PUMP");

        // Yangın Elektrikli Ana Yük Panoları
        InventoryCategoryDto yanginElektrikliAnaYukPanolari = createSubCategory("Yangın Elektrikli Ana Yük Panoları", "FIRE_MAIN_PANEL", yanginPompaSistemleri.getId(), "Yangın elektrikli ana yük panoları");
        createBrandSubCategories(yanginElektrikliAnaYukPanolari.getId(), "MAIN_PANEL");

        // Yangın Elektrikli Ana Yük Pompaları
        InventoryCategoryDto yanginElektrikliAnaYukPompalari = createSubCategory("Yangın Elektrikli Ana Yük Pompaları", "FIRE_MAIN_PUMP", yanginPompaSistemleri.getId(), "Yangın elektrikli ana yük pompaları");
        createBrandSubCategories(yanginElektrikliAnaYukPompalari.getId(), "MAIN_PUMP");

        // Yangın Söndürme Sistemleri
        InventoryCategoryDto yanginSondirmeSistemleri = createSubCategory("Yangın Söndürme Sistemleri", "FIRE_EXTINGUISH", yanginSistemleri.getId(), "Yangın söndürme sistemleri");

        // Yangın İhbar Sistemleri
        InventoryCategoryDto yanginIhbarSistemleri = createSubCategory("Yangın İhbar Sistemleri", "FIRE_ALARM", yanginSistemleri.getId(), "Yangın ihbar sistemleri");

        // Alt kategorilere panel component ekle
        createPanelComponents(yanginIhbarSistemleri.getId());

        // Acil Anons Sistemleri
        createSubCategory("Acil Anons Sistemleri", "EMERGENCY_PA", yanginSistemleri.getId(), "Acil anons sistemleri");
    }

    private void createPumpAndHydroforCategories() {
        // Ana kategori: Pompa ve Hidrofor Sistemleri
        InventoryCategoryDto pompaHidroforSistemleri = createRootCategory("Pompa ve Hidrofor Sistemleri", "PUMP_HYDROFOR", "Pompa ve hidrofor sistemleri");

        // Hidrofor Pompa Sistemleri
        InventoryCategoryDto hidroforPompaSistemleri = createSubCategory("Hidrofor Pompa Sistemleri", "HYDROFOR_PUMP", pompaHidroforSistemleri.getId(), "Hidrofor pompa sistemleri");
        createBrandSubCategories(hidroforPompaSistemleri.getId(), "HYDROFOR");

        // Sirkülasyon Pompa Sistemleri
        InventoryCategoryDto sirkulasyonPompaSistemleri = createSubCategory("Sirkülasyon Pompa Sistemleri", "CIRCULATION_PUMP", pompaHidroforSistemleri.getId(), "Sirkülasyon pompa sistemleri");
        createBrandSubCategories(sirkulasyonPompaSistemleri.getId(), "CIRCULATION");
    }

    private void createGeneratorCategories() {
        // Ana kategori: Jeneratör Sistemleri
        InventoryCategoryDto jeneratorSistemleri = createRootCategory("Jeneratör Sistemleri", "GENERATOR", "Jeneratör sistemleri");

        // Dizel Jeneratör
        InventoryCategoryDto dizelJenerator = createSubCategory("Dizel Jeneratör", "DIESEL_GEN", jeneratorSistemleri.getId(), "Dizel jeneratörler");

        // Jeneratör markaları
        createSubCategory("Aksa", "AKSA_GEN", dizelJenerator.getId(), "Aksa jeneratörler");
        createSubCategory("Teksan", "TEKSAN_GEN", dizelJenerator.getId(), "Teksan jeneratörler");
        createSubCategory("İdea", "IDEA_GEN", dizelJenerator.getId(), "İdea jeneratörler");
        createSubCategory("Alimar", "ALIMAR_GEN", dizelJenerator.getId(), "Alimar jeneratörler");
        createSubCategory("Borusan Cat", "BORUSAN_GEN", dizelJenerator.getId(), "Borusan Cat jeneratörler");
        createSubCategory("Güçbir", "GUCBIR_GEN", dizelJenerator.getId(), "Güçbir jeneratörler");
        createSubCategory("Emsa", "EMSA_GEN", dizelJenerator.getId(), "Emsa jeneratörler");

        // Benzinli Jeneratör
        createSubCategory("Benzinli Jeneratör", "GASOLINE_GEN", jeneratorSistemleri.getId(), "Benzinli jeneratörler");

        // Doğalgazlı Jeneratör
        createSubCategory("Doğalgazlı Jeneratör", "NATURAL_GAS_GEN", jeneratorSistemleri.getId(), "Doğalgazlı jeneratörler");
    }

    private void createUpsCategories() {
        // Ana kategori: UPS Sistemleri
        InventoryCategoryDto upsSistemleri = createRootCategory("UPS Sistemleri", "UPS", "UPS sistemleri");

        createSubCategory("Klima,Vrf, Vrv ve Vrş İklimlendirme Sistemleri", "CLIMATE_SYSTEMS", upsSistemleri.getId(), "İklimlendirme sistemleri");
    }

    private void createClimateCategories() {
        // Ana kategori: Klima,Vrf, Vrv ve Vrş İklimlendirme Sistemleri
        InventoryCategoryDto klimaSistemleri = createRootCategory("Klima,Vrf, Vrv ve Vrş İklimlendirme Sistemleri", "CLIMATE", "İklimlendirme sistemleri");

        // Klima,Vrf, Vrv ve Vrş Dış Ünite Sistemleri
        InventoryCategoryDto disUniteSistemleri = createSubCategory("Klima,Vrf, Vrv ve Vrş Dış Ünite Sistemleri", "OUTDOOR_UNIT", klimaSistemleri.getId(), "Dış ünite sistemleri");

        // Marka kategorileri
        createSubCategory("Daikin Vrv ve Klima", "DAIKIN_VRV", disUniteSistemleri.getId(), "Daikin VRV ve klima");
        createSubCategory("Mitsubishi Heavy Vrf", "MITSUBISHI_VRF", disUniteSistemleri.getId(), "Mitsubishi Heavy VRF");
        createSubCategory("Samsung Vrf", "SAMSUNG_VRF", disUniteSistemleri.getId(), "Samsung VRF");
        createSubCategory("General Vrf", "GENERAL_VRF", disUniteSistemleri.getId(), "General VRF");
        createSubCategory("Panasonic Vrf", "PANASONIC_VRF", disUniteSistemleri.getId(), "Panasonic VRF");
        createSubCategory("Toshiba Vrf", "TOSHIBA_VRF", disUniteSistemleri.getId(), "Toshiba VRF");
        createSubCategory("Midea Vrf", "MIDEA_VRF", disUniteSistemleri.getId(), "Midea VRF");
        createSubCategory("LG Vrf", "LG_VRF", disUniteSistemleri.getId(), "LG VRF");
        createSubCategory("Vestel Vrf", "VESTEL_VRF", disUniteSistemleri.getId(), "Vestel VRF");
        createSubCategory("York Vrf", "YORK_VRF", disUniteSistemleri.getId(), "York VRF");
        createSubCategory("Sanyo Vrf", "SANYO_VRF", disUniteSistemleri.getId(), "Sanyo VRF");
        createSubCategory("Arçelik Vrs", "ARCELIK_VRS", disUniteSistemleri.getId(), "Arçelik VRS");
        createSubCategory("Baymak", "BAYMAK", disUniteSistemleri.getId(), "Baymak");
        createSubCategory("Hotpoint", "HOTPOINT", disUniteSistemleri.getId(), "Hotpoint");
        createSubCategory("Demirdöküm", "DEMIRDOKUM", disUniteSistemleri.getId(), "Demirdöküm");
        createSubCategory("Fujiplus", "FUJIPLUS", disUniteSistemleri.getId(), "Fujiplus");
        createSubCategory("İndesit", "INDESIT", disUniteSistemleri.getId(), "İndesit");
    }

    private void createBrandSubCategories(String parentId, String prefix) {
        createSubCategory("Wilo", prefix + "_WILO", parentId, "Wilo marka");
        createSubCategory("Grundfos", prefix + "_GRUNDFOS", parentId, "Grundfos marka");
        createSubCategory("Ksb", prefix + "_KSB", parentId, "KSB marka");
        createSubCategory("Lowara", prefix + "_LOWARA", parentId, "Lowara marka");
        createSubCategory("Standart", prefix + "_STANDART", parentId, "Standart marka");
        createSubCategory("Etna", prefix + "_ETNA", parentId, "Etna marka");
        createSubCategory("Alfen", prefix + "_ALFEN", parentId, "Alfen marka");
        createSubCategory("Norm", prefix + "_NORM", parentId, "Norm marka");
        createSubCategory("Tornatech", prefix + "_TORNATECH", parentId, "Tornatech marka");
        createSubCategory("Mas & Daf", prefix + "_MAS_DAF", parentId, "Mas & Daf marka");
    }

    private void createPanelComponents(String parentId) {
        // Konvansiyonel Yangın Paneli
        InventoryCategoryDto konvansiyonelYanginPaneli = createSubCategory("Konvansiyonel Yangın Paneli", "CONV_FIRE_PANEL", parentId, "Konvansiyonel yangın paneli");

        // Panel markaları
        createSubCategory("Mavili", "MAVILI_CONV", konvansiyonelYanginPaneli.getId(), "Mavili marka");
        createSubCategory("Detnov", "DETNOV_CONV", konvansiyonelYanginPaneli.getId(), "Detnov marka");
        createSubCategory("Honeywell", "HONEYWELL_CONV", konvansiyonelYanginPaneli.getId(), "Honeywell marka");
        createSubCategory("Cooper", "COOPER_CONV", konvansiyonelYanginPaneli.getId(), "Cooper marka");

        // Adresli Yangın Paneli
        InventoryCategoryDto adresliYanginPaneli = createSubCategory("Adresli Yangın Paneli", "ADDR_FIRE_PANEL", parentId, "Adresli yangın paneli");

        createSubCategory("Mavili", "MAVILI_ADDR", adresliYanginPaneli.getId(), "Mavili marka");
        createSubCategory("Detnov", "DETNOV_ADDR", adresliYanginPaneli.getId(), "Detnov marka");
        createSubCategory("Honeywell", "HONEYWELL_ADDR", adresliYanginPaneli.getId(), "Honeywell marka");
        createSubCategory("Cooper", "COOPER_ADDR", adresliYanginPaneli.getId(), "Cooper marka");
        createSubCategory("Teletek", "TELETEK_ADDR", adresliYanginPaneli.getId(), "Teletek marka");

        // Konvansiyonel Panel Component
        InventoryCategoryDto konvansiyonelPanelComponent = createSubCategory("Konvansiyonel Panel Component", "CONV_PANEL_COMP", parentId, "Konvansiyonel panel komponentleri");

        createSubCategory("Mavili", "MAVILI_COMP", konvansiyonelPanelComponent.getId(), "Mavili komponentler");
        createSubCategory("Detnov", "DETNOV_COMP", konvansiyonelPanelComponent.getId(), "Detnov komponentler");
        createSubCategory("Honeywell", "HONEYWELL_COMP", konvansiyonelPanelComponent.getId(), "Honeywell komponentler");
        createSubCategory("Cooper", "COOPER_COMP", konvansiyonelPanelComponent.getId(), "Cooper komponentler");

        // Adresli Panel Component
        InventoryCategoryDto adresliPanelComponent = createSubCategory("Adresli Panel Component", "ADDR_PANEL_COMP", parentId, "Adresli panel komponentleri");

        createSubCategory("Mavili", "MAVILI_ADDR_COMP", adresliPanelComponent.getId(), "Mavili adresli komponentler");
        createSubCategory("Detnov", "DETNOV_ADDR_COMP", adresliPanelComponent.getId(), "Detnov adresli komponentler");
        createSubCategory("Honeywell", "HONEYWELL_ADDR_COMP", adresliPanelComponent.getId(), "Honeywell adresli komponentler");
        createSubCategory("Cooper", "COOPER_ADDR_COMP", adresliPanelComponent.getId(), "Cooper adresli komponentler");
    }

    private InventoryCategoryDto createRootCategory(String name, String code, String description) {
        InventoryCategoryDto category = new InventoryCategoryDto();
        category.setCategoryName(name);
        category.setCategoryCode(code);
        category.setDescription(description);
        category.setIsActive(true);
        category.setCreatedBy("System");
        return createCategory(category);
    }

    private InventoryCategoryDto createSubCategory(String name, String code, String parentId, String description) {
        InventoryCategoryDto category = new InventoryCategoryDto();
        category.setCategoryName(name);
        category.setCategoryCode(code);
        category.setParentCategoryId(parentId);
        category.setDescription(description);
        category.setIsActive(true);
        category.setCreatedBy("System");
        return createCategory(category);
    }
}
