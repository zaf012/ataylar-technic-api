package com.ay_za.ataylar_technic.service.model;

import com.ay_za.ataylar_technic.dto.SystemInfoDto;

/**
 * Periyodik Bakım Çeklisti için tablo satır verisi
 */
public class MaintenanceChecklistItemDto {

    private String item;          // description veya systemName
    private String checked;       // Uygun (X işareti için)
    private String unchecked;     // Uygun Değil (X işareti için)

    public MaintenanceChecklistItemDto() {
    }

    public MaintenanceChecklistItemDto(String item, String checked, String unchecked) {
        this.item = item;
        this.checked = checked;
        this.unchecked = unchecked;
    }

    // SystemInfoDto'dan dönüşüm için yardımcı metod
    public static MaintenanceChecklistItemDto fromSystemInfo(SystemInfoDto systemInfo) {
        MaintenanceChecklistItemDto item = new MaintenanceChecklistItemDto();
        item.setItem(systemInfo.getDescription());
        // Başlangıçta boş, kullanıcı işaretleyecek
        item.setChecked("");
        item.setUnchecked("");
        return item;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getUnchecked() {
        return unchecked;
    }

    public void setUnchecked(String unchecked) {
        this.unchecked = unchecked;
    }


    @Override
    public String toString() {
        return "MaintenanceChecklistItemDto{" +
                ", item='" + item + '\'' +
                ", checked='" + checked + '\'' +
                ", unchecked='" + unchecked + '\'' +
                '}';
    }
}