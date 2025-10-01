package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.ProjectsInfoDto;
import com.ay_za.ataylar_technic.dto.SitesInfoDto;
import com.ay_za.ataylar_technic.entity.SitesInfo;
import com.ay_za.ataylar_technic.mapper.SitesInfoMapper;
import com.ay_za.ataylar_technic.repository.SitesInfoRepository;
import com.ay_za.ataylar_technic.service.base.SitesInfoServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class SitesInfoService implements SitesInfoServiceImpl {

    private final SitesInfoRepository sitesInfoRepository;
    private final SitesInfoMapper sitesInfoMapper;
    private final ProjectsInfoService projectsInfoService;

    public SitesInfoService(SitesInfoRepository sitesInfoRepository, SitesInfoMapper sitesInfoMapper, ProjectsInfoService projectsInfoService) {
        this.sitesInfoRepository = sitesInfoRepository;
        this.sitesInfoMapper = sitesInfoMapper;
        this.projectsInfoService = projectsInfoService;
    }

    /**
     * Yeni site oluştur
     */
    @Transactional
    @Override
    public SitesInfoDto createSite(SitesInfoDto sitesInfoDto) {

        if (sitesInfoDto.getSiteName() == null || "".equals(sitesInfoDto.getSiteName())) {
            throw new IllegalArgumentException("Lütfen site adı giriniz");
        }

        // Site adı ve blok adı kombinasyonunun benzersizliğini kontrol et
        if (sitesInfoRepository.existsBySiteNameAndBlockNameIgnoreCase(
                sitesInfoDto.getSiteName(), sitesInfoDto.getBlockName())) {
            throw new IllegalArgumentException("Bu site için aynı blok adı zaten mevcut");
        }

        if (projectsInfoService.getProjectById(sitesInfoDto.getProjectId()).isEmpty()) {
            throw new IllegalArgumentException("Lütfen geçerli bir proje seçiniz");
        }

        SitesInfo sitesInfo = new SitesInfo();
        sitesInfo.setId(UUID.randomUUID().toString());
        sitesInfo.setSiteName(sitesInfoDto.getSiteName());
        sitesInfo.setProjectId(sitesInfoDto.getProjectId());
        sitesInfo.setProjectName(sitesInfoDto.getProjectName());
        sitesInfo.setDescription(sitesInfoDto.getDescription()); // DTO'dan al
        sitesInfo.setSquare(sitesInfoDto.getSquare()); // DTO'dan al
        sitesInfo.setBlockName(sitesInfoDto.getBlockName()); // DTO'dan al
        sitesInfo.setCreatedDate(LocalDateTime.now());
        sitesInfo.setUpdatedDate(LocalDateTime.now());
        sitesInfo.setCreatedBy("Admin");
        sitesInfo.setUpdatedBy("Admin");
        SitesInfo savedSite = sitesInfoRepository.save(sitesInfo);

        return sitesInfoMapper.convertToDTO(savedSite);
    }

    /**
     * Site güncelle
     */
    @Transactional
    @Override
    public SitesInfoDto updateSite(String id, String siteName) {
        // Parametreler kontrolü
        if (siteName == null || siteName.trim().isEmpty()) {
            throw new IllegalArgumentException("Site adı boş olamaz");
        }

        // Site var mı kontrol et
        SitesInfo site = sitesInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Site bulunamadı"));

        // Aynı isimde başka site var mı kontrol et (kendisi hariç)
        Optional<SitesInfo> siteById = sitesInfoRepository.findById(siteName.trim());
        if (siteById.isPresent() && !siteById.get().getId().equals(id)) {
            throw new IllegalArgumentException("Bu isimde başka bir site zaten mevcut");
        }

        site.setSiteName(siteName.trim());
        site.setUpdatedBy("Admin");

        SitesInfo savedSite = sitesInfoRepository.save(site);
        return sitesInfoMapper.convertToDTO(savedSite);
    }

    /**
     * Siteyi sil
     */
    @Transactional
    @Override
    public void deleteSite(String id) {
        // Site var mı kontrol et
        SitesInfo site = sitesInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Silinecek site bulunamadı"));

        // Firmayı sil
        sitesInfoRepository.delete(site);
    }

    /**
     * Tüm siteleri getir
     */
    @Override
    public List<SitesInfoDto> getAllSites() {
        List<SitesInfo> allByOrderBySiteNameAsc = sitesInfoRepository.findAllByOrderBySiteNameAsc();
        return sitesInfoMapper.convertAllToDTO(allByOrderBySiteNameAsc);
    }

    /**
     * Belirli bir site adına sahip tüm blokları getir
     */
    public List<SitesInfoDto> getBlocksBySiteName(String siteName) {
        List<SitesInfo> blocks = sitesInfoRepository.findBySiteNameIgnoreCaseOrderByBlockNameAsc(siteName);
        return sitesInfoMapper.convertAllToDTO(blocks);
    }

    /**
     * Site adına göre arama
     */
    public List<SitesInfoDto> searchSitesByName(String searchTerm) {
        List<SitesInfo> sites = sitesInfoRepository.findBySiteNameContainingIgnoreCaseOrderBySiteNameAsc(searchTerm);
        return sitesInfoMapper.convertAllToDTO(sites);
    }

    /**
     * Proje ID'sine göre siteleri getir
     */
    public List<SitesInfoDto> getSitesByProjectId(String projectId) {
        List<SitesInfo> sites = sitesInfoRepository.findByProjectIdOrderBySiteNameAsc(projectId);
        return sitesInfoMapper.convertAllToDTO(sites);
    }

    /**
     * Site ID'ye göre site getir
     */
    public Optional<SitesInfoDto> getSiteById(String siteId) {
        Optional<SitesInfo> site = sitesInfoRepository.findById(siteId);
        return site.map(sitesInfoMapper::convertToDTO);
    }

    /**
     * Site ve blok kombinasyonunun varlığını kontrol et
     */
    public boolean existsBySiteNameAndBlockName(String siteName, String blockName) {
        return sitesInfoRepository.existsBySiteNameAndBlockNameIgnoreCase(siteName, blockName);
    }

    /**
     * Default site verilerini oluştur
     */
    @Transactional
    public List<SitesInfoDto> createDefaultSites() {
        List<SitesInfoDto> createdSites = new ArrayList<>();
        String createdBy = "System Admin";

        // Önce mevcut projelerden birkaçını al
        List<ProjectsInfoDto> projects = projectsInfoService.getAllProjects();
        if (projects.isEmpty()) {
            throw new RuntimeException("Hiç proje bulunamadı. Önce proje oluşturun.");
        }

        // İlk birkaç projeyi kullan
        ProjectsInfoDto project1 = projects.get(0);
        ProjectsInfoDto project2 = projects.size() > 1 ? projects.get(1) : project1;
        ProjectsInfoDto project3 = projects.size() > 2 ? projects.get(2) : project1;

        // Sample site ve blok verileri
        Map<String, Map<String, String[]>> siteProjectData = new LinkedHashMap<>();

        // Her proje için site ve bloklar
        Map<String, String[]> project1Sites = new LinkedHashMap<>();
        project1Sites.put("DAP Validebağ Sitesi", new String[]{"123.blok", "456.blok", "789.blok"});
        project1Sites.put("DAP Mesa Kartal", new String[]{"A Blok", "B Blok", "C Blok"});
        siteProjectData.put(project1.getId(), project1Sites);

        Map<String, String[]> project2Sites = new LinkedHashMap<>();
        project2Sites.put("Emlak Konut Başakşehir", new String[]{"1.Etap", "2.Etap", "3.Etap"});
        project2Sites.put("Royal Garden Sitesi", new String[]{"Rose Blok", "Lily Blok"});
        siteProjectData.put(project2.getId(), project2Sites);

        Map<String, String[]> project3Sites = new LinkedHashMap<>();
        project3Sites.put("TOKİ Mamak Konutları", new String[]{"1.Kısım", "2.Kısım", "3.Kısım", "4.Kısım"});
        project3Sites.put("Nurol Park Evleri", new String[]{"Park A", "Park B"});
        siteProjectData.put(project3.getId(), project3Sites);

        // Site ve blokları oluştur
        for (Map.Entry<String, Map<String, String[]>> projectEntry : siteProjectData.entrySet()) {
            String projectId = projectEntry.getKey();
            Map<String, String[]> sitesData = projectEntry.getValue();

            // Proje adını bul
            String projectName = projects.stream()
                    .filter(p -> p.getId().equals(projectId))
                    .findFirst()
                    .map(ProjectsInfoDto::getProjectName)
                    .orElse("Unknown Project");

            for (Map.Entry<String, String[]> siteEntry : sitesData.entrySet()) {
                String siteName = siteEntry.getKey();
                String[] blocks = siteEntry.getValue();

                for (String blockName : blocks) {
                    try {
                        // Aynı site+blok kombinasyonu zaten var mı kontrol et
                        if (!existsBySiteNameAndBlockName(siteName, blockName)) {
                            SitesInfoDto siteDto = new SitesInfoDto();
                            siteDto.setSiteName(siteName);
                            siteDto.setProjectId(projectId);
                            siteDto.setProjectName(projectName);
                            siteDto.setBlockName(blockName);
                            siteDto.setCreatedDate(LocalDateTime.now());
                            siteDto.setCreatedBy(createdBy);
                            siteDto.setDescription(siteName + " - " + blockName + " açıklaması");
                            siteDto.setSquare("Ada " + (int)(Math.random() * 100 + 1));

                            SitesInfoDto created = createSite(siteDto);
                            createdSites.add(created);
                        }
                    } catch (Exception e) {
                        System.err.println("Site oluşturulurken hata: " + siteName + " - " + blockName + " - " + e.getMessage());
                    }
                }
            }
        }

        return createdSites;
    }
}