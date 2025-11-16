package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.SiteDeviceInventoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiteDeviceInventoryInfoRepository extends JpaRepository<SiteDeviceInventoryInfo, String> {

    // QR koda göre cihaz bulma
    Optional<SiteDeviceInventoryInfo> findByQrCode(String qrCode);

    // QR kod varlığını kontrol etme
    boolean existsByQrCode(String qrCode);

    // Site ID'ye göre cihazları bulma
    List<SiteDeviceInventoryInfo> findBySiteId(String siteId);

    // Site ve square (ada) bilgisine göre cihazları bulma
    List<SiteDeviceInventoryInfo> findBySiteIdAndSquareId(String siteId, String squareId);

    // Site, square ve blok bilgisine göre cihazları bulma
    List<SiteDeviceInventoryInfo> findBySiteIdAndSquareIdAndBlockId(String siteId, String squareId, String blockId);

    // Aktif cihazları bulma
    List<SiteDeviceInventoryInfo> findByIsActive(Boolean isActive);

    // Belirli bir envanter kategorisindeki cihazları bulma
    List<SiteDeviceInventoryInfo> findByInventoryCategoryId(String inventoryCategoryId);

    // Sistem ID'sine göre cihazları bulma
    List<SiteDeviceInventoryInfo> findBySystemId(String systemId);

    // Site adına göre arama (like)
    @Query("SELECT s FROM SiteDeviceInventoryInfo s WHERE s.siteName LIKE %:siteName%")
    List<SiteDeviceInventoryInfo> findBySiteNameContaining(@Param("siteName") String siteName);

    // Sistem adına göre arama (like)
    @Query("SELECT s FROM SiteDeviceInventoryInfo s WHERE s.systemName LIKE %:systemName%")
    List<SiteDeviceInventoryInfo> findBySystemNameContaining(@Param("systemName") String systemName);

    // Lokasyona göre arama (like)
    @Query("SELECT s FROM SiteDeviceInventoryInfo s WHERE s.location LIKE %:location%")
    List<SiteDeviceInventoryInfo> findByLocationContaining(@Param("location") String location);

    // Kat numarasına göre cihazları bulma
    List<SiteDeviceInventoryInfo> findByFloor(Integer floor);

    // Daire numarasına göre cihazları bulma (null kontrolü ile)
    List<SiteDeviceInventoryInfo> findByDoorNo(String doorNo);

    // Kompleks arama - site, square, blok ve aktiflik durumu
    @Query("SELECT s FROM SiteDeviceInventoryInfo s WHERE " +
           "(:siteId IS NULL OR s.siteId = :siteId) AND " +
           "(:squareId IS NULL OR s.squareId = :squareId) AND " +
           "(:blockId IS NULL OR s.blockId = :blockId) AND " +
           "(:isActive IS NULL OR s.isActive = :isActive)")
    List<SiteDeviceInventoryInfo> findByCriteria(@Param("siteId") String siteId,
                                                  @Param("squareId") String squareId,
                                                  @Param("blockId") String blockId,
                                                  @Param("isActive") Boolean isActive);

    // Site, square, blok ve kat bilgisine göre cihaz sayısını döndürme
    @Query("SELECT COUNT(s) FROM SiteDeviceInventoryInfo s WHERE " +
           "s.siteId = :siteId AND s.squareId = :squareId AND s.blockId = :blockId AND s.floor = :floor")
    Long countDevicesInLocation(@Param("siteId") String siteId,
                                @Param("squareId") String squareId,
                                @Param("blockId") String blockId,
                                @Param("floor") Integer floor);

    // Belirli bir kategori hiyerarşisinde kaç cihaz olduğunu sayma
    @Query("SELECT COUNT(s) FROM SiteDeviceInventoryInfo s WHERE s.inventoryCategoryName LIKE %:inventoryCategoryName%")
    Long countByInventoryCategoryName(@Param("inventoryCategoryName") String inventoryCategoryName);
}
