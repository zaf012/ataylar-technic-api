package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.SitesInfoDto;
import com.ay_za.ataylar_technic.dto.SquaresInfoDto;
import com.ay_za.ataylar_technic.entity.SquaresInfo;
import com.ay_za.ataylar_technic.mapper.SquaresInfoMapper;
import com.ay_za.ataylar_technic.repository.SquaresInfoRepository;
import com.ay_za.ataylar_technic.service.base.SquaresInfoServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SquaresInfoService implements SquaresInfoServiceImpl {

    private final SquaresInfoRepository squaresInfoRepository;
    private final SquaresInfoMapper squaresInfoMapper;
    private final SitesInfoService sitesInfoService;

    public SquaresInfoService(SquaresInfoRepository squaresInfoRepository,
                              SquaresInfoMapper squaresInfoMapper,
                              @Lazy SitesInfoService sitesInfoService) {
        this.squaresInfoRepository = squaresInfoRepository;
        this.squaresInfoMapper = squaresInfoMapper;
        this.sitesInfoService = sitesInfoService;
    }

    /**
     * Yeni ada oluştur
     */
    @Transactional
    @Override
    public SquaresInfoDto createSquare(SquaresInfoDto squaresInfoDto) {

        if (squaresInfoDto.getSquareName() == null || "".equals(squaresInfoDto.getSquareName().trim())) {
            throw new IllegalArgumentException("Lütfen ada adı giriniz");
        }

        if (squaresInfoDto.getSiteId() == null || "".equals(squaresInfoDto.getSiteId().trim())) {
            throw new IllegalArgumentException("Lütfen site seçiniz");
        }

        // Site var mı kontrol et
        if (sitesInfoService.getSiteEntityById(squaresInfoDto.getSiteId()).isEmpty()) {
            throw new IllegalArgumentException("Lütfen geçerli bir site seçiniz");
        }

        // Aynı site içinde ada adı benzersizliğini kontrol et
        if (squaresInfoRepository.existsBySiteIdAndSquareNameIgnoreCase(
                squaresInfoDto.getSiteId(), squaresInfoDto.getSquareName().trim())) {
            throw new IllegalArgumentException("Bu site içinde aynı ada adı zaten mevcut");
        }

        SquaresInfo squaresInfo = new SquaresInfo();
        squaresInfo.setSquareName(squaresInfoDto.getSquareName().trim());
        squaresInfo.setSiteId(squaresInfoDto.getSiteId().trim());
        squaresInfo.setSiteName(squaresInfoDto.getSiteName());
        squaresInfo.setDescription(squaresInfoDto.getDescription());
        squaresInfo.setCreatedDate(LocalDateTime.now());
        squaresInfo.setUpdatedDate(LocalDateTime.now());
        squaresInfo.setCreatedBy(squaresInfoDto.getCreatedBy());
        squaresInfo.setUpdatedBy(squaresInfoDto.getUpdatedBy());

        SquaresInfo savedSquare = squaresInfoRepository.save(squaresInfo);
        return squaresInfoMapper.convertToDTO(savedSquare);
    }

    /**
     * Ada güncelle
     */
    @Transactional
    @Override
    public SquaresInfoDto updateSquare(String id, SquaresInfoDto squaresInfoDto) {
        // Ada var mı kontrol et
        SquaresInfo square = squaresInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ada bulunamadı"));

        // Güncellenecek alanları kontrol et ve güncelle
        if (squaresInfoDto.getSquareName() != null && !squaresInfoDto.getSquareName().trim().isEmpty()) {
            // Aynı site içinde ada adı benzersizliği kontrol et (kendisi hariç)
            if (squaresInfoRepository.existsBySiteIdAndSquareNameIgnoreCase(
                    square.getSiteId(), squaresInfoDto.getSquareName().trim())) {
                // Mevcut kaydın kendisi mi kontrol et
                List<SquaresInfo> existingSquares = squaresInfoRepository.findBySiteIdOrderBySquareNameAsc(square.getSiteId());
                boolean isDuplicate = existingSquares.stream()
                        .anyMatch(existingSquare ->
                            !existingSquare.getId().equals(id) &&
                            existingSquare.getSquareName().equalsIgnoreCase(squaresInfoDto.getSquareName().trim())
                        );
                if (isDuplicate) {
                    throw new IllegalArgumentException("Bu site içinde aynı ada adı zaten mevcut");
                }
            }
            square.setSquareName(squaresInfoDto.getSquareName().trim());
        }

        if (squaresInfoDto.getSiteId() != null && !squaresInfoDto.getSiteId().trim().isEmpty()) {
            if (sitesInfoService.getSiteEntityById(squaresInfoDto.getSiteId()).isEmpty()) {
                throw new IllegalArgumentException("Lütfen geçerli bir site seçiniz");
            }
            square.setSiteId(squaresInfoDto.getSiteId().trim());
        }

        if (squaresInfoDto.getSiteName() != null && !squaresInfoDto.getSiteName().trim().isEmpty()) {
            square.setSiteName(squaresInfoDto.getSiteName().trim());
        }

        if (squaresInfoDto.getDescription() != null) {
            square.setDescription(squaresInfoDto.getDescription().trim());
        }

        if (squaresInfoDto.getUpdatedBy() != null) {
            square.setUpdatedBy(squaresInfoDto.getUpdatedBy());
        }

        square.setUpdatedDate(LocalDateTime.now());

        SquaresInfo savedSquare = squaresInfoRepository.save(square);
        return squaresInfoMapper.convertToDTO(savedSquare);
    }

    /**
     * Adayı sil
     */
    @Transactional
    @Override
    public void deleteSquare(String id) {
        // Ada var mı kontrol et
        SquaresInfo square = squaresInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Silinecek ada bulunamadı"));

        // Adayı sil
        squaresInfoRepository.delete(square);
    }

    /**
     * Tüm adaları getir
     */
    @Override
    public List<SquaresInfoDto> getAllSquares() {
        List<SquaresInfo> squares = squaresInfoRepository.findAll();
        return squaresInfoMapper.convertAllToDTO(squares);
    }

    /**
     * Site ID'ye göre adaları getir
     */
    public List<SquaresInfoDto> getSquaresBySiteId(String siteId) {
        List<SquaresInfo> squares = squaresInfoRepository.findBySiteIdOrderBySquareNameAsc(siteId);
        return squaresInfoMapper.convertAllToDTO(squares);
    }

    /**
     * Ada adına göre arama
     */
    public List<SquaresInfoDto> searchSquaresByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllSquares();
        }
        List<SquaresInfo> squares = squaresInfoRepository.findBySquareNameContainingIgnoreCaseOrderBySquareNameAsc(searchTerm.trim());
        return squaresInfoMapper.convertAllToDTO(squares);
    }

    /**
     * Site adına göre adaları getir
     */
    public List<SquaresInfoDto> getSquaresBySiteName(String siteName) {
        List<SquaresInfo> squares = squaresInfoRepository.findBySiteNameIgnoreCaseOrderBySquareNameAsc(siteName);
        return squaresInfoMapper.convertAllToDTO(squares);
    }

    /**
     * Random ada getir
     */
    @Override
    public Optional<SquaresInfo> getRandomSquare() {
        return squaresInfoRepository.findRandomSquare();
    }

    /**
     * Belirli site için random ada getir
     */
    public Optional<SquaresInfo> getRandomSquareBySiteId(String siteId) {
        return squaresInfoRepository.findRandomSquareBySiteId(siteId);
    }

    /**
     * Ada ID'ye göre ada getir
     */
    public Optional<SquaresInfo> getSquareById(String id) {
        return squaresInfoRepository.findById(id);
    }

    /**
     * Default ada verilerini oluştur
     */
    @Transactional
    public List<SquaresInfoDto> createDefaultSquares() {
        List<SquaresInfoDto> createdSquares = new ArrayList<>();
        String createdBy = "System Admin";

        // Mevcut siteleri al
        List<SitesInfoDto> sites = sitesInfoService.getAllSites();
        if (sites.isEmpty()) {
            throw new RuntimeException("Hiç site bulunamadı. Önce siteler oluşturun.");
        }

        // Her site için sample ada verileri
        Map<String, String[]> siteSquareData = new LinkedHashMap<>();

        for (SitesInfoDto site : sites) {
            String siteName = site.getSiteName();

            if (siteName.contains("Validebağ")) {
                siteSquareData.put(site.getId(), new String[]{"1. Ada", "2. Ada", "3. Ada", "4. Ada"});
            } else if (siteName.contains("Mesa")) {
                siteSquareData.put(site.getId(), new String[]{"A Ada", "B Ada", "C Ada"});
            } else if (siteName.contains("Başakşehir")) {
                siteSquareData.put(site.getId(), new String[]{"Merkez Ada", "Doğu Ada", "Batı Ada"});
            } else if (siteName.contains("Royal")) {
                siteSquareData.put(site.getId(), new String[]{"Bahçe Ada", "Havuz Ada"});
            } else if (siteName.contains("TOKİ")) {
                siteSquareData.put(site.getId(), new String[]{"1. Kısım", "2. Kısım", "3. Kısım", "4. Kısım", "5. Kısım"});
            } else if (siteName.contains("Nurol")) {
                siteSquareData.put(site.getId(), new String[]{"Yeşil Ada", "Mavi Ada", "Kırmızı Ada"});
            } else {
                // Genel ada isimleri
                siteSquareData.put(site.getId(), new String[]{"1. Ada", "2. Ada", "3. Ada"});
            }
        }

        // Adaları oluştur
        for (Map.Entry<String, String[]> entry : siteSquareData.entrySet()) {
            String siteId = entry.getKey();
            String[] squareNames = entry.getValue();

            // Site bilgilerini al
            Optional<SitesInfoDto> siteOpt = sitesInfoService.getSiteById(siteId);
            if (siteOpt.isEmpty()) continue;

            SitesInfoDto site = siteOpt.get();

            for (String squareName : squareNames) {
                try {
                    SquaresInfoDto squareDto = new SquaresInfoDto();
                    squareDto.setSquareName(squareName);
                    squareDto.setSiteId(siteId);
                    squareDto.setSiteName(site.getSiteName());
                    squareDto.setCreatedDate(LocalDateTime.now());
                    squareDto.setCreatedBy(createdBy);
                    squareDto.setDescription(squareName + " açıklaması");

                    SquaresInfoDto created = createSquare(squareDto);
                    createdSquares.add(created);
                } catch (Exception e) {
                    System.err.println("Ada oluşturulurken hata: " + squareName + " - " + e.getMessage());
                }
            }
        }

        return createdSquares;
    }
}
