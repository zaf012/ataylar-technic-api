package com.ay_za.ataylar_technic.dto;

import java.util.List;

public class PdfMergeRequestDto {

    private List<String> pdfRecordIds;

    // Constructors
    public PdfMergeRequestDto() {
    }

    public PdfMergeRequestDto(List<String> pdfRecordIds) {
        this.pdfRecordIds = pdfRecordIds;
    }

    // Getters and Setters
    public List<String> getPdfRecordIds() {
        return pdfRecordIds;
    }

    public void setPdfRecordIds(List<String> pdfRecordIds) {
        this.pdfRecordIds = pdfRecordIds;
    }
}

