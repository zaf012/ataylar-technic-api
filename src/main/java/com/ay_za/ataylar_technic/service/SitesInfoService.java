package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.BlocksInfoDto;
import com.ay_za.ataylar_technic.dto.ProjectsInfoDto;
import com.ay_za.ataylar_technic.dto.SitesInfoDto;
import com.ay_za.ataylar_technic.dto.SquaresInfoDto;
import com.ay_za.ataylar_technic.entity.SitesInfo;
import com.ay_za.ataylar_technic.mapper.SitesInfoMapper;
import com.ay_za.ataylar_technic.repository.SitesInfoRepository;
import com.ay_za.ataylar_technic.service.base.SitesInfoServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SitesInfoService implements SitesInfoServiceImpl {

    private final SitesInfoRepository sitesInfoRepository;
    private final SitesInfoMapper sitesInfoMapper;
    private final ProjectsInfoService projectsInfoService;
    private final SquaresInfoService squaresInfoService;
    private final BlocksInfoService blocksInfoService;

    public SitesInfoService(SitesInfoRepository sitesInfoRepository,
                           SitesInfoMapper sitesInfoMapper,
                           ProjectsInfoService projectsInfoService,
                           SquaresInfoService squaresInfoService,
                           BlocksInfoService blocksInfoService) {
        this.sitesInfoRepository = sitesInfoRepository;
        this.sitesInfoMapper = sitesInfoMapper;
        this.projectsInfoService = projectsInfoService;
        this.squaresInfoService = squaresInfoService;
        this.blocksInfoService = blocksInfoService;
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

        // Site adı benzersizliğini kontrol et
        if (sitesInfoRepository.existsBySiteNameIgnoreCase(
                sitesInfoDto.getSiteName())) {
            throw new IllegalArgumentException("Bu site zaten mevcut");
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
        sitesInfo.setCreatedDate(LocalDateTime.now());
        sitesInfo.setUpdatedDate(LocalDateTime.now());
        SitesInfo savedSite = sitesInfoRepository.save(sitesInfo);

        return sitesInfoMapper.convertToDTO(savedSite);
    }

    /**
     * Site güncelle
     */
    @Transactional
    @Override
    public SitesInfoDto updateSite(String id, SitesInfoDto sitesInfoDto) {
        // Site var mı kontrol et
        SitesInfo site = sitesInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Site bulunamadı"));

        // Güncellenecek alanları kontrol et ve güncelle
        if (sitesInfoDto.getSiteName() != null && !sitesInfoDto.getSiteName().trim().isEmpty()) {
            // Aynı site adı ve blok adı kombinasyonu var mı kontrol et (kendisi hariç)
                boolean exists = sitesInfoRepository.existsBySiteNameIgnoreCase(
                        sitesInfoDto.getSiteName().trim());
                if (exists) {
                    // Mevcut kaydın kendisi mi kontrol et
                    List<SitesInfo> existingSites = sitesInfoRepository.findBySiteNameIgnoreCase(
                            sitesInfoDto.getSiteName().trim());
                    boolean isDuplicate = existingSites.stream()
                            .anyMatch(existingSite -> !existingSite.getId().equals(id));
                    if (isDuplicate) {
                        throw new IllegalArgumentException("Bu site adı zaten mevcut");
                    }
                }

            site.setSiteName(sitesInfoDto.getSiteName().trim());
        }

        if (sitesInfoDto.getProjectId() != null && !sitesInfoDto.getProjectId().trim().isEmpty()) {
            if (projectsInfoService.getProjectById(sitesInfoDto.getProjectId()).isEmpty()) {
                throw new IllegalArgumentException("Lütfen geçerli bir proje seçiniz");
            }
            site.setProjectId(sitesInfoDto.getProjectId().trim());
        }

        if (sitesInfoDto.getProjectName() != null && !sitesInfoDto.getProjectName().trim().isEmpty()) {
            site.setProjectName(sitesInfoDto.getProjectName().trim());
        }

        if (sitesInfoDto.getDescription() != null) {
            site.setDescription(sitesInfoDto.getDescription().trim());
        }

        site.setUpdatedDate(LocalDateTime.now());

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
     * Site ID'ye göre site entity getir
     */
    public Optional<SitesInfo> getSiteEntityById(String siteId) {
        return sitesInfoRepository.findById(siteId);
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

        // Sample site verileri
        Map<String, String> siteProjectData = new LinkedHashMap<>();
        siteProjectData.put("DAP Validebağ Sitesi", project1.getId());
        siteProjectData.put("DAP Mesa Kartal", project1.getId());
        siteProjectData.put("Emlak Konut Başakşehir", project2.getId());
        siteProjectData.put("Royal Garden Sitesi", project2.getId());
        siteProjectData.put("TOKİ Mamak Konutları", project3.getId());
        siteProjectData.put("Nurol Park Evleri", project3.getId());

        // Siteleri oluştur
        for (Map.Entry<String, String> entry : siteProjectData.entrySet()) {
            String siteName = entry.getKey();
            String projectId = entry.getValue();

            try {
                // Aynı site adı zaten var mı kontrol et
                if (!sitesInfoRepository.existsBySiteNameIgnoreCase(siteName)) {
                    // Proje adını bul
                    String projectName = projects.stream()
                            .filter(p -> p.getId().equals(projectId))
                            .findFirst()
                            .map(ProjectsInfoDto::getProjectName)
                            .orElse("Unknown Project");

                    SitesInfoDto siteDto = new SitesInfoDto();
                    siteDto.setSiteName(siteName);
                    siteDto.setProjectId(projectId);
                    siteDto.setProjectName(projectName);
                    siteDto.setCreatedDate(LocalDateTime.now());
                    siteDto.setCreatedBy(createdBy);
                    siteDto.setDescription(siteName + " projesi açıklaması");

                    SitesInfoDto created = createSite(siteDto);
                    createdSites.add(created);
                }
            } catch (Exception e) {
                System.err.println("Site oluşturulurken hata: " + siteName + " - " + e.getMessage());
            }
        }

        return createdSites;
    }



    /**
     * Tüm default verileri oluştur (Site, Ada, Blok)
     */
    @Transactional
    public Map<String, Object> createAllDefaultData() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. Siteleri oluştur
            List<SitesInfoDto> sites = createDefaultSites();
            result.put("sites", sites);
            result.put("sitesCount", sites.size());

            // 2. Adaları oluştur
            List<SquaresInfoDto> squares = squaresInfoService.createDefaultSquares();
            result.put("squares", squares);
            result.put("squaresCount", squares.size());

            // 3. Blokları oluştur
            List<BlocksInfoDto> blocks = blocksInfoService.createDefaultBlocks();
            result.put("blocks", blocks);
            result.put("blocksCount", blocks.size());

            result.put("success", true);
            result.put("message", "Tüm default veriler başarıyla oluşturuldu");
            result.put("totalCount", sites.size() + squares.size() + blocks.size());

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Default veriler oluşturulurken hata: " + e.getMessage());
        }

        return result;
    }

    /**
     * Rastgele bir site getir
     */
    @Override
    public Optional<SitesInfo> getRandomSite() {
        List<SitesInfo> allSites = sitesInfoRepository.findAll();
        if (allSites.isEmpty()) {
            return Optional.empty();
        }

        int randomIndex = (int) (Math.random() * allSites.size());
        return Optional.of(allSites.get(randomIndex));
    }
}