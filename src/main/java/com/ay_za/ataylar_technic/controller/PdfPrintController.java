package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.service.base.MaintenanceChecklistPdfServiceImpl;
import com.ay_za.ataylar_technic.service.model.MaintenanceChecklistModel;
import com.ay_za.ataylar_technic.vm.FileResponseVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/pdf-print")
@CrossOrigin(origins = "*")
@Tag(name = "PDF Print", description = "Periyodik Bakım PDF Yazdırma")
public class PdfPrintController {

    private final MaintenanceChecklistPdfServiceImpl maintenanceChecklistPdfServiceImpl;

    public PdfPrintController(MaintenanceChecklistPdfServiceImpl maintenanceChecklistPdfServiceImpl) {
        this.maintenanceChecklistPdfServiceImpl = maintenanceChecklistPdfServiceImpl;
    }

    @PostMapping("/export-pdf")
    @Operation(summary = "PDF yazdır", description = "Periyodik bakım çeklistini PDF olarak yazdırır")
    public ResponseEntity<FileResponseVM> exportPdf(@RequestBody MaintenanceChecklistModel maintenanceChecklistModel) throws IOException {
        FileResponseVM fileResponseVM = maintenanceChecklistPdfServiceImpl.exportPdf(maintenanceChecklistModel);
        return ResponseEntity.ok(fileResponseVM);
    }
}