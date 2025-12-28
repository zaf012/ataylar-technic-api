package com.ay_za.ataylar_technic.enums;

public enum ServiceScope {
    PERIODIC_MAINTENANCE_CONTRACT("Periyodik Bakım Sözleşmeli"),
    ONSITE_INTERVENTION("Yerinde Müdahale"),
    WARRANTY_COVERAGE("Garanti İçi"),
    PERIODIC_MAINTENANCE("Periyodik Bakım"),
    NON_WARRANTY_COVERAGE("Garanti Dışı"),
    DAMAGE_ASSESSMENT("Hasar Tespit"),
    FAULT("Arıza"),
    WORKSHOP("Atölye");

    private final String displayName;

    ServiceScope(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}