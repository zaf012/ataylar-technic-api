package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.BlocksInfoDto;
import com.ay_za.ataylar_technic.dto.SquaresInfoDto;
import com.ay_za.ataylar_technic.entity.BlocksInfo;
import com.ay_za.ataylar_technic.mapper.BlocksInfoMapper;
import com.ay_za.ataylar_technic.repository.BlocksInfoRepository;
import com.ay_za.ataylar_technic.service.base.BlocksInfoServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BlocksInfoService implements BlocksInfoServiceImpl {

    private final BlocksInfoRepository blocksInfoRepository;
    private final BlocksInfoMapper blocksInfoMapper;
    private final SquaresInfoService squaresInfoService;

    public BlocksInfoService(BlocksInfoRepository blocksInfoRepository,
                             BlocksInfoMapper blocksInfoMapper,
                             SquaresInfoService squaresInfoService) {
        this.blocksInfoRepository = blocksInfoRepository;
        this.blocksInfoMapper = blocksInfoMapper;
        this.squaresInfoService = squaresInfoService;
    }

    /**
     * Yeni blok oluştur
     */
    @Transactional
    @Override
    public BlocksInfoDto createBlock(BlocksInfoDto blocksInfoDto) {

        if (blocksInfoDto.getBlockName() == null || blocksInfoDto.getBlockName().trim().isEmpty()) {
            throw new IllegalArgumentException("Lütfen blok adı giriniz");
        }

        if (blocksInfoDto.getSquareId() == null || blocksInfoDto.getSquareId().trim().isEmpty()) {
            throw new IllegalArgumentException("Lütfen ada seçiniz");
        }

        // Ada var mı kontrol et
        if (squaresInfoService.getSquareById(blocksInfoDto.getSquareId()).isEmpty()) {
            throw new IllegalArgumentException("Lütfen geçerli bir ada seçiniz");
        }

        // Aynı ada içinde blok adı benzersizliğini kontrol et
        if (blocksInfoRepository.existsBySquareIdAndBlockNameIgnoreCase(
                blocksInfoDto.getSquareId(), blocksInfoDto.getBlockName().trim())) {
            throw new IllegalArgumentException("Bu ada içinde aynı blok adı zaten mevcut");
        }

        // Blok kodu varsa benzersizliğini kontrol et
        if (blocksInfoDto.getBlockCode() != null && !blocksInfoDto.getBlockCode().trim().isEmpty()) {
            if (blocksInfoRepository.existsByBlockCodeIgnoreCase(blocksInfoDto.getBlockCode().trim())) {
                throw new IllegalArgumentException("Bu blok kodu zaten kullanılıyor");
            }
        }

        BlocksInfo blocksInfo = new BlocksInfo();
        blocksInfo.setId(UUID.randomUUID().toString());
        blocksInfo.setBlockName(blocksInfoDto.getBlockName().trim());
        blocksInfo.setBlockCode(blocksInfoDto.getBlockCode() != null ? blocksInfoDto.getBlockCode().trim() : null);
        blocksInfo.setSquareId(blocksInfoDto.getSquareId().trim());
        blocksInfo.setDescription(blocksInfoDto.getDescription());
        blocksInfo.setCreatedDate(LocalDateTime.now());
        blocksInfo.setUpdatedDate(LocalDateTime.now());
        blocksInfo.setCreatedBy(blocksInfoDto.getCreatedBy());
        blocksInfo.setUpdatedBy(blocksInfoDto.getUpdatedBy());

        BlocksInfo savedBlock = blocksInfoRepository.save(blocksInfo);
        return blocksInfoMapper.convertToDTO(savedBlock);
    }

    /**
     * Blok güncelle
     */
    @Transactional
    @Override
    public BlocksInfoDto updateBlock(String id, BlocksInfoDto blocksInfoDto) {
        // Blok var mı kontrol et
        BlocksInfo block = blocksInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blok bulunamadı"));

        // Güncellenecek alanları kontrol et ve güncelle
        if (blocksInfoDto.getBlockName() != null && !blocksInfoDto.getBlockName().trim().isEmpty()) {
            // Aynı ada içinde blok adı benzersizliği kontrol et (kendisi hariç)
            if (blocksInfoRepository.existsBySquareIdAndBlockNameIgnoreCase(
                    block.getSquareId(), blocksInfoDto.getBlockName().trim())) {
                // Mevcut kaydın kendisi mi kontrol et
                List<BlocksInfo> existingBlocks = blocksInfoRepository.findBySquareIdOrderByBlockNameAsc(block.getSquareId());
                boolean isDuplicate = existingBlocks.stream()
                        .anyMatch(existingBlock ->
                                !existingBlock.getId().equals(id) &&
                                        existingBlock.getBlockName().equalsIgnoreCase(blocksInfoDto.getBlockName().trim())
                        );
                if (isDuplicate) {
                    throw new IllegalArgumentException("Bu ada içinde aynı blok adı zaten mevcut");
                }
            }
            block.setBlockName(blocksInfoDto.getBlockName().trim());
        }

        if (blocksInfoDto.getBlockCode() != null && !blocksInfoDto.getBlockCode().trim().isEmpty()) {
            // Blok kodu benzersizliği kontrol et (kendisi hariç)
            Optional<BlocksInfo> existingBlockWithCode = blocksInfoRepository.findByBlockCodeIgnoreCase(blocksInfoDto.getBlockCode().trim());
            if (existingBlockWithCode.isPresent() && !existingBlockWithCode.get().getId().equals(id)) {
                throw new IllegalArgumentException("Bu blok kodu zaten kullanılıyor");
            }
            block.setBlockCode(blocksInfoDto.getBlockCode().trim());
        }

        if (blocksInfoDto.getSquareId() != null && !blocksInfoDto.getSquareId().trim().isEmpty()) {
            if (squaresInfoService.getSquareById(blocksInfoDto.getSquareId()).isEmpty()) {
                throw new IllegalArgumentException("Lütfen geçerli bir ada seçiniz");
            }
            block.setSquareId(blocksInfoDto.getSquareId().trim());
        }

        if (blocksInfoDto.getDescription() != null) {
            block.setDescription(blocksInfoDto.getDescription().trim());
        }

        if (blocksInfoDto.getUpdatedBy() != null) {
            block.setUpdatedBy(blocksInfoDto.getUpdatedBy());
        }

        block.setUpdatedDate(LocalDateTime.now());

        BlocksInfo savedBlock = blocksInfoRepository.save(block);
        return blocksInfoMapper.convertToDTO(savedBlock);
    }

    /**
     * Bloğu sil
     */
    @Transactional
    @Override
    public void deleteBlock(String id) {
        // Blok var mı kontrol et
        BlocksInfo block = blocksInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Silinecek blok bulunamadı"));

        // Bloğu sil
        blocksInfoRepository.delete(block);
    }

    /**
     * Tüm blokları getir
     */
    @Override
    public List<BlocksInfoDto> getAllBlocks() {
        List<BlocksInfo> blocks = blocksInfoRepository.findAll();
        return blocksInfoMapper.convertAllToDTO(blocks);
    }

    /**
     * Ada ID'ye göre blokları getir
     */
    public List<BlocksInfoDto> getBlocksBySquareId(String squareId) {
        List<BlocksInfo> blocks = blocksInfoRepository.findBySquareIdOrderByBlockNameAsc(squareId);
        return blocksInfoMapper.convertAllToDTO(blocks);
    }

    /**
     * Blok adına göre arama
     */
    public List<BlocksInfoDto> searchBlocksByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllBlocks();
        }
        List<BlocksInfo> blocks = blocksInfoRepository.findByBlockNameContainingIgnoreCaseOrderByBlockNameAsc(searchTerm.trim());
        return blocksInfoMapper.convertAllToDTO(blocks);
    }

    /**
     * Blok koduna göre blok getir
     */
    public Optional<BlocksInfoDto> getBlockByCode(String blockCode) {
        Optional<BlocksInfo> block = blocksInfoRepository.findByBlockCodeIgnoreCase(blockCode);
        return block.map(blocksInfoMapper::convertToDTO);
    }

    /**
     * Random blok getir
     */
    @Override
    public Optional<BlocksInfo> getRandomBlock() {
        return blocksInfoRepository.findRandomBlock();
    }

    /**
     * Belirli ada için random blok getir
     */
    public Optional<BlocksInfo> getRandomBlockBySquareId(String squareId) {
        return blocksInfoRepository.findRandomBlockBySquareId(squareId);
    }

    /**
     * Default blok verilerini oluştur
     */
    @Transactional
    public List<BlocksInfoDto> createDefaultBlocks() {
        List<BlocksInfoDto> createdBlocks = new ArrayList<>();
        String createdBy = "System Admin";

        // Mevcut adaları al
        List<SquaresInfoDto> squares = squaresInfoService.getAllSquares();
        if (squares.isEmpty()) {
            throw new RuntimeException("Hiç ada bulunamadı. Önce adalar oluşturun.");
        }

        // Her ada için sample blok verileri
        for (SquaresInfoDto square : squares) {
            String[] blockNames;

            String squareName = square.getSquareName();
            if (squareName.contains("Ada") || squareName.contains("Kısım")) {
                // Ada/Kısım bazında blok isimleri
                if (squareName.contains("1.") || squareName.contains("A")) {
                    blockNames = new String[]{"A Blok", "B Blok", "C Blok"};
                } else if (squareName.contains("2.") || squareName.contains("B")) {
                    blockNames = new String[]{"D Blok", "E Blok", "F Blok"};
                } else if (squareName.contains("3.") || squareName.contains("C")) {
                    blockNames = new String[]{"G Blok", "H Blok", "I Blok"};
                } else if (squareName.contains("4.")) {
                    blockNames = new String[]{"J Blok", "K Blok"};
                } else if (squareName.contains("5.")) {
                    blockNames = new String[]{"L Blok", "M Blok"};
                } else if (squareName.contains("Merkez")) {
                    blockNames = new String[]{"1. Etap", "2. Etap", "3. Etap"};
                } else if (squareName.contains("Doğu")) {
                    blockNames = new String[]{"4. Etap", "5. Etap"};
                } else if (squareName.contains("Batı")) {
                    blockNames = new String[]{"6. Etap", "7. Etap"};
                } else if (squareName.contains("Bahçe")) {
                    blockNames = new String[]{"Rose Blok", "Tulip Blok"};
                } else if (squareName.contains("Havuz")) {
                    blockNames = new String[]{"Lily Blok", "Orchid Blok"};
                } else if (squareName.contains("Yeşil")) {
                    blockNames = new String[]{"Park A", "Park B"};
                } else if (squareName.contains("Mavi")) {
                    blockNames = new String[]{"Park C", "Park D"};
                } else if (squareName.contains("Kırmızı")) {
                    blockNames = new String[]{"Park E", "Park F"};
                } else {
                    blockNames = new String[]{"1. Blok", "2. Blok"};
                }
            } else {
                // Genel blok isimleri
                blockNames = new String[]{"1. Blok", "2. Blok", "3. Blok"};
            }

            for (int i = 0; i < blockNames.length; i++) {
                try {
                    BlocksInfoDto blockDto = new BlocksInfoDto();
                    blockDto.setBlockName(blockNames[i]);
                    blockDto.setBlockCode("BLK-" + square.getId().substring(0, 8) + "-" + (i + 1));
                    blockDto.setSquareId(square.getId());
                    blockDto.setCreatedDate(LocalDateTime.now());
                    blockDto.setCreatedBy(createdBy);
                    blockDto.setDescription(blockNames[i] + " açıklaması");

                    BlocksInfoDto created = createBlock(blockDto);
                    createdBlocks.add(created);
                } catch (Exception e) {
                    System.err.println("Blok oluşturulurken hata: " + blockNames[i] + " - " + e.getMessage());
                }
            }
        }

        return createdBlocks;
    }

    /**
     * Blok ID'ye göre blok DTO getir
     */
    public Optional<BlocksInfoDto> getBlockDtoById(String id) {
        Optional<BlocksInfo> block = blocksInfoRepository.findById(id);
        return block.map(blocksInfoMapper::convertToDTO);
    }
}
