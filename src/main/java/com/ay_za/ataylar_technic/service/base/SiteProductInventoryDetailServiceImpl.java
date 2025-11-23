package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.SiteProductInventoryDetailDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SiteProductInventoryDetailServiceImpl {

    @Transactional
    SiteProductInventoryDetailDto createSiteProductDetail(SiteProductInventoryDetailDto dto);

    @Transactional
    SiteProductInventoryDetailDto updateSiteProductDetail(String id, SiteProductInventoryDetailDto dto);

    @Transactional(readOnly = true)
    SiteProductInventoryDetailDto getSiteProductDetailById(String id);

    @Transactional(readOnly = true)
    SiteProductInventoryDetailDto getSiteProductDetailByQrCode(String qrCode);

    @Transactional(readOnly = true)
    List<SiteProductInventoryDetailDto> getAllSiteProductDetails();

    @Transactional(readOnly = true)
    List<SiteProductInventoryDetailDto> getActiveSiteProductDetails();

    @Transactional
    void deleteSiteProductDetail(String id);

    SiteProductInventoryDetailDto deactivateSiteProductDetail(String id);
}
