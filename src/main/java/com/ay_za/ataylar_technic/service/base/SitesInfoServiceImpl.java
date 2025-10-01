package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.SitesInfoDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SitesInfoServiceImpl {
    SitesInfoDto createSite(SitesInfoDto sitesInfoDto);

    @Transactional
    SitesInfoDto updateSite(String id, String siteName);

    @Transactional
    void deleteSite(String id);

    List<SitesInfoDto> getAllSites();
}
