package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.SquaresInfoDto;
import com.ay_za.ataylar_technic.entity.SquaresInfo;
import com.ay_za.ataylar_technic.service.SquaresInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.*;

@RestController
@RequestMapping("/api/squares")
@Tag(name = "Squares Info", description = "Ada bilgileri yönetimi")
public class SquaresInfoController {

    private final SquaresInfoService squaresInfoService;

    public SquaresInfoController(SquaresInfoService squaresInfoService) {
        this.squaresInfoService = squaresInfoService;
    }

    /**
     * Yeni ada oluştur
     */
    @PostMapping
    @Operation(summary = "Yeni ada oluştur", description = "Yeni bir ada kaydı oluşturur")
    public ResponseEntity<SquaresInfoDto> createSquare(@RequestBody SquaresInfoDto squaresInfoDto) {
        try {
            SquaresInfoDto createdSquare = squaresInfoService.createSquare(squaresInfoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSquare);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Ada güncelle
     */
    @PutMapping("/{id}")
    @Operation(summary = "Ada güncelle", description = "Mevcut ada bilgilerini günceller")
    public ResponseEntity<SquaresInfoDto> updateSquare(
            @Parameter(description = "Ada ID") @PathVariable String id,
            @RequestBody SquaresInfoDto squaresInfoDto) {
        try {
            SquaresInfoDto updatedSquare = squaresInfoService.updateSquare(id, squaresInfoDto);
            return ResponseEntity.ok(updatedSquare);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Ada sil
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Ada sil", description = "Belirtilen ada kaydını siler")
    public ResponseEntity<Void> deleteSquare(@Parameter(description = "Ada ID") @PathVariable String id) {
        try {
            squaresInfoService.deleteSquare(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Tüm adaları getir
     */
    @GetMapping
    @Operation(summary = "Tüm adaları listele", description = "Sistemdeki tüm ada kayıtlarını getirir")
    public ResponseEntity<List<SquaresInfoDto>> getAllSquares() {
        List<SquaresInfoDto> squares = squaresInfoService.getAllSquares();
        return ResponseEntity.ok(squares);
    }

    /**
     * ID'ye göre ada getir
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre ada getir", description = "Belirtilen ID'ye sahip ada bilgisini getirir")
    public ResponseEntity<SquaresInfoDto> getSquareById(@Parameter(description = "Ada ID") @PathVariable String id) {
        Optional<SquaresInfo> square = squaresInfoService.getSquareById(id);
        return square.map(s -> ResponseEntity.ok(new SquaresInfoDto(
                s.getId(),
                s.getSquareName(),
                s.getSiteId(),
                s.getSiteName(),
                s.getDescription(),
                s.getCreatedDate(),
                s.getUpdatedDate(),
                s.getCreatedBy(),
                s.getUpdatedBy()
        ))).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Site ID'ye göre adaları getir
     */
    @GetMapping("/by-site/{siteId}")
    @Operation(summary = "Site ID'ye göre adaları getir", description = "Belirtilen site ID'ye ait tüm ada kayıtlarını getirir")
    public ResponseEntity<List<SquaresInfoDto>> getSquaresBySiteId(
            @Parameter(description = "Site ID") @PathVariable String siteId) {
        List<SquaresInfoDto> squares = squaresInfoService.getSquaresBySiteId(siteId);
        return ResponseEntity.ok(squares);
    }

    /**
     * Ada adına göre arama
     */
    @GetMapping("/search")
    @Operation(summary = "Ada adına göre arama", description = "Ada adında arama yapar")
    public ResponseEntity<List<SquaresInfoDto>> searchSquaresByName(
            @Parameter(description = "Arama terimi") @RequestParam String searchTerm) {
        List<SquaresInfoDto> squares = squaresInfoService.searchSquaresByName(searchTerm);
        return ResponseEntity.ok(squares);
    }

    /**
     * Site adına göre adaları getir
     */
    @GetMapping("/by-site-name/{siteName}")
    @Operation(summary = "Site adına göre adaları getir", description = "Belirtilen site adına ait tüm ada kayıtlarını getirir")
    public ResponseEntity<List<SquaresInfoDto>> getSquaresBySiteName(
            @Parameter(description = "Site adı") @PathVariable String siteName) {
        List<SquaresInfoDto> squares = squaresInfoService.getSquaresBySiteName(siteName);
        return ResponseEntity.ok(squares);
    }

    /**
     * Default ada verilerini oluştur
     */
    @PostMapping("/create-default-data")
    @Operation(summary = "Default ada verileri oluştur", description = "Örnek ada verilerini oluşturur")
    public ResponseEntity<Map<String, Object>> createDefaultSquares() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<SquaresInfoDto> createdSquares = squaresInfoService.createDefaultSquares();

            response.put("success", true);
            response.put("message", createdSquares.size() + " örnek ada oluşturuldu");
            response.put("data", createdSquares);
            response.put("count", createdSquares.size());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Örnek adalar oluşturulurken hata oluştu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Random ada getir
     */
    @GetMapping("/random")
    @Operation(summary = "Random ada getir", description = "Rastgele bir ada kaydı getirir")
    public ResponseEntity<SquaresInfoDto> getRandomSquare() {
        Optional<SquaresInfo> randomSquare = squaresInfoService.getRandomSquare();
        return randomSquare.map(square -> ResponseEntity.ok(new SquaresInfoDto(
                square.getId(),
                square.getSquareName(),
                square.getSiteId(),
                square.getSiteName(),
                square.getDescription(),
                square.getCreatedDate(),
                square.getUpdatedDate(),
                square.getCreatedBy(),
                square.getUpdatedBy()
        ))).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Belirli site için random ada getir
     */
    @GetMapping("/random/by-site/{siteId}")
    @Operation(summary = "Belirli site için random ada getir", description = "Belirtilen site için rastgele bir ada kaydı getirir")
    public ResponseEntity<SquaresInfoDto> getRandomSquareBySiteId(
            @Parameter(description = "Site ID") @PathVariable String siteId) {
        Optional<SquaresInfo> randomSquare = squaresInfoService.getRandomSquareBySiteId(siteId);
        return randomSquare.map(square -> ResponseEntity.ok(new SquaresInfoDto(
                square.getId(),
                square.getSquareName(),
                square.getSiteId(),
                square.getSiteName(),
                square.getDescription(),
                square.getCreatedDate(),
                square.getUpdatedDate(),
                square.getCreatedBy(),
                square.getUpdatedBy()
        ))).orElse(ResponseEntity.notFound().build());
    }
}
