package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.SitesInfoDto;
import com.ay_za.ataylar_technic.entity.SitesInfo;
import com.ay_za.ataylar_technic.mapper.SitesInfoMapper;
import com.ay_za.ataylar_technic.repository.SitesInfoRepository;
import com.ay_za.ataylar_technic.service.base.SitesInfoServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SitesInfoService implements SitesInfoServiceImpl {

    private final SitesInfoRepository sitesInfoRepository;
    private final SitesInfoMapper sitesInfoMapper;

    public SitesInfoService(SitesInfoRepository sitesInfoRepository,
                            SitesInfoMapper sitesInfoMapper) {
        this.sitesInfoRepository = sitesInfoRepository;
        this.sitesInfoMapper = sitesInfoMapper;
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

        SitesInfo sitesInfo = new SitesInfo();
        sitesInfo.setSiteName(sitesInfoDto.getSiteName());
        sitesInfo.setDescription(sitesInfoDto.getDescription() != null ? sitesInfo.getDescription() : ""); // DTO'dan al
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

}