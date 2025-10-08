package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.SystemInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemInfoRepository extends JpaRepository<SystemInfo, String> {

    // Sadece sistem tanımlarını getir (çeklist/arıza maddeleri olmayan)
    @Query("SELECT s FROM SystemInfo s")
    List<SystemInfo> findAllSystems();

    @Query("SELECT s.id FROM SystemInfo s WHERE s.id = :id")
    SystemInfo findSystemInfoById(@Param("id") String id);

    // Aktif sistemleri getir
    @Query("SELECT s FROM SystemInfo s WHERE s.isActive = true AND (s.description IS NULL OR s.description = '') ORDER BY s.systemOrderNo")
    List<SystemInfo> findActiveSystemsOnly();

    // Belirli bir sistemin çeklist maddelerini getir
    @Query("SELECT s FROM SystemInfo s WHERE s.systemName = :systemName AND s.isChecklist = true ORDER BY s.controlPointOrder")
    List<SystemInfo> findChecklistsBySystemName(@Param("systemName") String systemName);

    // Belirli bir sistemin arıza maddelerini getir
    @Query("SELECT s FROM SystemInfo s WHERE s.systemName = :systemName AND s.isFault = true ORDER BY s.controlPointOrder")
    List<SystemInfo> findFaultsBySystemName(@Param("systemName") String systemName);

    // Belirli bir sistemin aktif çeklist maddelerini getir
    @Query("SELECT s FROM SystemInfo s WHERE s.systemName = :systemName AND s.isChecklist = true AND s.controlPointIsActive = true ORDER BY s.controlPointOrder")
    List<SystemInfo> findActiveChecklistsBySystemName(@Param("systemName") String systemName);

    // Belirli bir sistemin aktif arıza maddelerini getir
    @Query("SELECT s FROM SystemInfo s WHERE s.systemName = :systemName AND s.isFault = true AND s.controlPointIsActive = true ORDER BY s.controlPointOrder")
    List<SystemInfo> findActiveFaultsBySystemName(@Param("systemName") String systemName);

    // Sistem adına göre tüm kayıtları getir (sistem + çeklist + arıza)
    @Query("SELECT s FROM SystemInfo s WHERE s.systemName = :systemName ORDER BY s.systemOrderNo, s.controlPointOrder")
    List<SystemInfo> findAllBySystemName(@Param("systemName") String systemName);

    // Çeklist sayısını getir
    @Query("SELECT COUNT(s) FROM SystemInfo s WHERE s.systemName = :systemName AND s.isChecklist = true")
    Long countChecklistsBySystemName(@Param("systemName") String systemName);

    // Arıza sayısını getir
    @Query("SELECT COUNT(s) FROM SystemInfo s WHERE s.systemName = :systemName AND s.isFault = true")
    Long countFaultsBySystemName(@Param("systemName") String systemName);

    // Sistem adı ile benzersiz sistem adlarını getir
    @Query("SELECT DISTINCT s.systemName FROM SystemInfo s WHERE s.systemName IS NOT NULL ORDER BY s.systemName")
    List<String> findDistinctSystemNames();

    // Son sistem sıra numarasını getir
    @Query("SELECT MAX(s.systemOrderNo) FROM SystemInfo s WHERE s.description IS NULL OR s.description = ''")
    Integer findMaxSystemOrderNo();

    // Belirli sistem için son kontrol maddesi sıra numarasını getir
    @Query("SELECT MAX(s.controlPointOrder) FROM SystemInfo s WHERE s.systemName = :systemName AND s.description IS NOT NULL")
    Integer findMaxControlPointOrderBySystemName(@Param("systemName") String systemName);

}
