package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.SiteProductInventoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Site Product Inventory Detail Repository
 */
@Repository
public interface SiteProductInventoryDetailRepository extends JpaRepository<SiteProductInventoryDetail, String> {

    /**
     * QR code based search
     */
    Optional<SiteProductInventoryDetail> findByQrCode(String qrCode);

    /**
     * Check if QR code exists
     */
    boolean existsByQrCode(String qrCode);

    /**
     * Check if QR code exists for different record (for update)
     */
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM SiteProductInventoryDetail s WHERE s.qrCode = :qrCode AND s.id != :id")
    boolean existsByQrCodeAndIdNot(@Param("qrCode") String qrCode, @Param("id") String id);

    /**
     * Find by site ID
     */
    List<SiteProductInventoryDetail> findBySiteId(String siteId);

    /**
     * Find by square ID
     */
    List<SiteProductInventoryDetail> findBySquareId(String squareId);

    /**
     * Find by block ID
     */
    List<SiteProductInventoryDetail> findByBlockId(String blockId);

    /**
     * Find by system ID
     */
    List<SiteProductInventoryDetail> findBySystemId(String systemId);

    /**
     * Find by category ID
     */
    List<SiteProductInventoryDetail> findByCategoryId(String categoryId);

    /**
     * Find by product inventory detail ID
     */
    List<SiteProductInventoryDetail> findByProductInventoryDetailId(String productInventoryDetailId);

    /**
     * Find active records
     */
    List<SiteProductInventoryDetail> findByActiveTrue();

    /**
     * Find active records by site ID
     */
    @Query("SELECT s FROM SiteProductInventoryDetail s WHERE s.siteId = :siteId AND s.active = true")
    List<SiteProductInventoryDetail> findActiveBySiteId(@Param("siteId") String siteId);

    /**
     * Find active records by square ID
     */
    @Query("SELECT s FROM SiteProductInventoryDetail s WHERE s.squareId = :squareId AND s.active = true")
    List<SiteProductInventoryDetail> findActiveBySquareId(@Param("squareId") String squareId);

    /**
     * Find active records by block ID
     */
    @Query("SELECT s FROM SiteProductInventoryDetail s WHERE s.blockId = :blockId AND s.active = true")
    List<SiteProductInventoryDetail> findActiveByBlockId(@Param("blockId") String blockId);

    /**
     * Location based search (site, square, block)
     */
    @Query("SELECT s FROM SiteProductInventoryDetail s WHERE s.siteId = :siteId " +
           "AND s.squareId = :squareId AND s.blockId = :blockId")
    List<SiteProductInventoryDetail> findByLocation(
            @Param("siteId") String siteId,
            @Param("squareId") String squareId,
            @Param("blockId") String blockId);

    /**
     * System and category based search
     */
    @Query("SELECT s FROM SiteProductInventoryDetail s WHERE s.systemId = :systemId AND s.categoryId = :categoryId")
    List<SiteProductInventoryDetail> findBySystemAndCategory(
            @Param("systemId") String systemId,
            @Param("categoryId") String categoryId);
}

