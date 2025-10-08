package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.FirmsInfoDto;
import com.ay_za.ataylar_technic.entity.FirmsInfo;
import com.ay_za.ataylar_technic.mapper.FirmsInfoMapper;
import com.ay_za.ataylar_technic.repository.FirmsInfoRepository;
import com.ay_za.ataylar_technic.service.base.FirmsInfoServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FirmsInfoService implements FirmsInfoServiceImpl {

    private final FirmsInfoRepository firmsInfoRepository;
    private final FirmsInfoMapper firmsInfoMapper;

    public FirmsInfoService(FirmsInfoRepository firmsInfoRepository, FirmsInfoMapper firmsInfoMapper) {
        this.firmsInfoRepository = firmsInfoRepository;
        this.firmsInfoMapper = firmsInfoMapper;
    }

    /**
     * Yeni firma oluştur
     */
    @Transactional
    @Override
    public FirmsInfoDto createFirm(String firmName) {
        // Firma adı kontrolü
        if (firmName == null || firmName.trim().isEmpty()) {
            throw new IllegalArgumentException("Firma adı boş olamaz");
        }

        // Aynı isimde firma var mı kontrol et
        if (firmsInfoRepository.existsByFirmNameIgnoreCase(firmName.trim())) {
            throw new IllegalArgumentException("Bu isimde bir firma zaten mevcut");
        }

        FirmsInfo firm = new FirmsInfo();
        firm.setId(UUID.randomUUID().toString());
        firm.setFirmName(firmName.trim());

        FirmsInfo savedFirm = firmsInfoRepository.save(firm);
        return firmsInfoMapper.convertToDTO(savedFirm);
    }

    /**
     * Firma güncelle
     */
    @Transactional
    @Override
    public FirmsInfoDto updateFirm(String firmId, String firmName) {
        // Parametreler kontrolü
        if (firmName == null || firmName.trim().isEmpty()) {
            throw new IllegalArgumentException("Firma adı boş olamaz");
        }

        // Firma var mı kontrol et
        FirmsInfo firm = firmsInfoRepository.findById(firmId)
                .orElseThrow(() -> new IllegalArgumentException("Firma bulunamadı"));

        // Aynı isimde başka firma var mı kontrol et (kendisi hariç)
        Optional<FirmsInfo> existingFirm = firmsInfoRepository.findByFirmNameIgnoreCase(firmName.trim());
        if (existingFirm.isPresent() && !existingFirm.get().getId().equals(firmId)) {
            throw new IllegalArgumentException("Bu isimde başka bir firma zaten mevcut");
        }

        firm.setFirmName(firmName.trim());

        FirmsInfo savedFirm = firmsInfoRepository.save(firm);
        return firmsInfoMapper.convertToDTO(savedFirm);
    }

    /**
     * Firmayı sil
     */
    @Transactional
    @Override
    public void deleteFirm(String firmId) {
        // Firma var mı kontrol et
        FirmsInfo firm = firmsInfoRepository.findById(firmId)
                .orElseThrow(() -> new IllegalArgumentException("Silinecek firma bulunamadı"));

        // Firmayı sil
        firmsInfoRepository.delete(firm);
    }

    /**
     * ID'ye göre firma getir
     */
    @Override
    public Optional<FirmsInfoDto> getFirmById(String firmId) {
        Optional<FirmsInfo> firm = firmsInfoRepository.findById(firmId);
        return firm.map(firmsInfoMapper::convertToDTO);
    }

    /**
     * Tüm firmaları getir
     */
    @Override
    public List<FirmsInfoDto> getAllFirms() {
        List<FirmsInfo> firms = firmsInfoRepository.findAllByOrderByFirmNameAsc();
        return firmsInfoMapper.convertAllToDTO(firms);
    }

    /**
     * Firma adına göre arama
     */
    @Override
    public List<FirmsInfoDto> searchFirmsByName(String searchTerm) {
        List<FirmsInfo> firms;
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            firms = firmsInfoRepository.findAllByOrderByFirmNameAsc();
        } else {
            firms = firmsInfoRepository.findByFirmNameContainingIgnoreCase(searchTerm.trim());
        }
        return firmsInfoMapper.convertAllToDTO(firms);
    }

    /**
     * Firma adının varlığını kontrol et
     */
    @Override
    public boolean existsByFirmName(String firmName) {
        if (firmName == null || firmName.trim().isEmpty()) {
            return false;
        }
        return firmsInfoRepository.existsByFirmNameIgnoreCase(firmName.trim());
    }

    /**
     * Firma sayısını getir
     */
    @Override
    public Integer getFirmCount() {
        return firmsInfoRepository.countFirms();
    }

    /**
     * Firma varlık kontrolü
     */
    @Override
    public boolean checkFirmById(String firmId) {
        return firmsInfoRepository.existsById(firmId);
    }


    /**
     * Örnek firma verilerini oluştur
     */
    @Override
    public List<FirmsInfoDto> createSampleFirms() {
        List<String> sampleFirms = Arrays.asList(
                "DAP Yapı",
                "Emlak Konut GYO",
                "TOKİ",
                "Sinpaş GYO",
                "Nurol İnşaat",
                "Akfen İnşaat",
                "Mesa Mesken",
                "Rönesans Holding",
                "Ağaoğlu Grubu",
                "Babacan Holding"
        );

        List<FirmsInfoDto> createdFirms = new ArrayList<>();

        for (String firmName : sampleFirms) {
            try {
                // Var olan firmaları tekrar oluşturmaya çalışma
                if (!existsByFirmName(firmName)) {
                    FirmsInfoDto created = createFirm(firmName);
                    createdFirms.add(created);
                }
            } catch (Exception e) {
                // Hata durumunda devam et
                System.err.println("Firma oluşturulurken hata: " + firmName + " - " + e.getMessage());
            }
        }

        return createdFirms;
    }

    /**
     * Rastgele bir firma getir
     */
    @Override
    public Optional<FirmsInfo> getRandomFirm() {
        List<FirmsInfo> allFirms = firmsInfoRepository.findAll();
        if (allFirms.isEmpty()) {
            return Optional.empty();
        }

        int randomIndex = (int) (Math.random() * allFirms.size());
        return Optional.of(allFirms.get(randomIndex));
    }
}
