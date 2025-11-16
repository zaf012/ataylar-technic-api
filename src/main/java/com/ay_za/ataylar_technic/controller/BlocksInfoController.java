package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.BlocksInfoDto;
import com.ay_za.ataylar_technic.entity.BlocksInfo;
import com.ay_za.ataylar_technic.service.BlocksInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/blocks")
@Tag(name = "Blocks Info", description = "Blok bilgileri yönetimi")
public class BlocksInfoController {

    private final BlocksInfoService blocksInfoService;

    public BlocksInfoController(BlocksInfoService blocksInfoService) {
        this.blocksInfoService = blocksInfoService;
    }

    /**
     * Yeni blok oluştur
     */
    @PostMapping
    @Operation(summary = "Yeni blok oluştur", description = "Yeni bir blok kaydı oluşturur")
    public ResponseEntity<BlocksInfoDto> createBlock(@RequestBody BlocksInfoDto blocksInfoDto) {
        try {
            BlocksInfoDto createdBlock = blocksInfoService.createBlock(blocksInfoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBlock);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Blok güncelle
     */
    @PutMapping("/{id}")
    @Operation(summary = "Blok güncelle", description = "Mevcut blok bilgilerini günceller")
    public ResponseEntity<BlocksInfoDto> updateBlock(
            @Parameter(description = "Blok ID") @PathVariable String id,
            @RequestBody BlocksInfoDto blocksInfoDto) {
        try {
            BlocksInfoDto updatedBlock = blocksInfoService.updateBlock(id, blocksInfoDto);
            return ResponseEntity.ok(updatedBlock);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Blok sil
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Blok sil", description = "Belirtilen blok kaydını siler")
    public ResponseEntity<Void> deleteBlock(@Parameter(description = "Blok ID") @PathVariable String id) {
        try {
            blocksInfoService.deleteBlock(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Tüm blokları getir
     */
    @GetMapping("/get-all")
    @Operation(summary = "Tüm blokları listele", description = "Sistemdeki tüm blok kayıtlarını getirir")
    public ResponseEntity<List<BlocksInfoDto>> getAllBlocks() {
        List<BlocksInfoDto> blocks = blocksInfoService.getAllBlocks();
        return ResponseEntity.ok(blocks);
    }

    /**
     * ID'ye göre blok getir
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre blok getir", description = "Belirtilen ID'ye sahip blok bilgisini getirir")
    public ResponseEntity<BlocksInfoDto> getBlockById(@Parameter(description = "Blok ID") @PathVariable String id) {
        Optional<BlocksInfoDto> block = blocksInfoService.getBlockDtoById(id);
        return block.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Ada ID'ye göre blokları getir
     */
    @GetMapping("/by-square/{squareId}")
    @Operation(summary = "Ada ID'ye göre blokları getir", description = "Belirtilen ada ID'ye ait tüm blok kayıtlarını getirir")
    public ResponseEntity<List<BlocksInfoDto>> getBlocksBySquareId(
            @Parameter(description = "Ada ID") @PathVariable String squareId) {
        List<BlocksInfoDto> blocks = blocksInfoService.getBlocksBySquareId(squareId);
        return ResponseEntity.ok(blocks);
    }

    /**
     * Blok adına göre arama
     */
    @GetMapping("/search")
    @Operation(summary = "Blok adına göre arama", description = "Blok adında arama yapar")
    public ResponseEntity<List<BlocksInfoDto>> searchBlocksByName(
            @Parameter(description = "Arama terimi") @RequestParam String searchTerm) {
        List<BlocksInfoDto> blocks = blocksInfoService.searchBlocksByName(searchTerm);
        return ResponseEntity.ok(blocks);
    }

    /**
     * Blok koduna göre blok getir
     */
    @GetMapping("/by-code/{blockCode}")
    @Operation(summary = "Blok koduna göre blok getir", description = "Belirtilen blok koduna sahip blok bilgisini getirir")
    public ResponseEntity<BlocksInfoDto> getBlockByCode(
            @Parameter(description = "Blok kodu") @PathVariable String blockCode) {
        Optional<BlocksInfoDto> block = blocksInfoService.getBlockByCode(blockCode);
        return block.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Random blok getir
     */
    @GetMapping("/random")
    @Operation(summary = "Random blok getir", description = "Rastgele bir blok kaydı getirir")
    public ResponseEntity<BlocksInfoDto> getRandomBlock() {
        Optional<BlocksInfo> randomBlock = blocksInfoService.getRandomBlock();
        return randomBlock.map(block -> ResponseEntity.ok(new BlocksInfoDto(
                block.getId(),
                block.getBlockName(),
                block.getBlockCode(),
                block.getSquareId(),
                block.getSquareName(),
                block.getDescription(),
                block.getCreatedDate(),
                block.getUpdatedDate(),
                block.getCreatedBy(),
                block.getUpdatedBy()
        ))).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Belirli ada için random blok getir
     */
    @GetMapping("/random/by-square/{squareId}")
    @Operation(summary = "Belirli ada için random blok getir", description = "Belirtilen ada için rastgele bir blok kaydı getirir")
    public ResponseEntity<BlocksInfoDto> getRandomBlockBySquareId(
            @Parameter(description = "Ada ID") @PathVariable String squareId) {
        Optional<BlocksInfo> randomBlock = blocksInfoService.getRandomBlockBySquareId(squareId);
        return randomBlock.map(block -> ResponseEntity.ok(new BlocksInfoDto(
                block.getId(),
                block.getBlockName(),
                block.getBlockCode(),
                block.getSquareId(),
                block.getSquareName(),
                block.getDescription(),
                block.getCreatedDate(),
                block.getUpdatedDate(),
                block.getCreatedBy(),
                block.getUpdatedBy()
        ))).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Default blok verilerini oluştur
     */
    @PostMapping("/create-default-data")
    @Operation(summary = "Default blok verileri oluştur", description = "Örnek blok verilerini oluşturur")
    public ResponseEntity<Map<String, Object>> createDefaultBlocks() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BlocksInfoDto> createdBlocks = blocksInfoService.createDefaultBlocks();

            response.put("success", true);
            response.put("message", createdBlocks.size() + " örnek blok oluşturuldu");
            response.put("data", createdBlocks);
            response.put("count", createdBlocks.size());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Örnek bloklar oluşturulurken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
