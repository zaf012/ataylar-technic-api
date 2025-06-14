package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.entity.InstantGroup;
import com.ay_za.ataylar_technic.repository.InstantGroupRepository;
import com.ay_za.ataylar_technic.service.base.InstantGroupServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class InstantGroupService implements InstantGroupServiceImpl {

    private final InstantGroupRepository instantGroupRepository;

    public InstantGroupService(InstantGroupRepository instantGroupRepository) {
        this.instantGroupRepository = instantGroupRepository;
    }

    /**
     * Yeni grup oluştur
     */
    @Transactional
    @Override
    public InstantGroup createGroup(String groupName, String createdBy) {
        // Grup adı kontrolü
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Grup adı boş olamaz");
        }

        // Aynı isimde grup var mı kontrol et
        if (instantGroupRepository.existsByGroupNameIgnoreCaseAndIsActiveTrue(groupName.trim())) {
            throw new IllegalArgumentException("Bu isimde bir grup zaten mevcut");
        }

        InstantGroup group = new InstantGroup();
        group.setId(UUID.randomUUID().toString());
        group.setGroupName(groupName.trim());
        group.setCreatedBy(createdBy);
        group.setIsActive(true);

        return instantGroupRepository.save(group);
    }

    /**
     * Grup adını güncelle
     */
    @Transactional
    @Override
    public InstantGroup updateGroupName(String groupId, String newGroupName, String updatedBy) {
        // Parametreler kontrolü
        if (newGroupName == null || newGroupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Grup adı boş olamaz");
        }

        // Grup var mı kontrol et
        InstantGroup group = instantGroupRepository.findByIdAndIsActiveTrue(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grup bulunamadı"));

        // Aynı isimde başka grup var mı kontrol et (kendisi hariç)
        Optional<InstantGroup> existingGroup = instantGroupRepository.findByGroupNameIgnoreCaseAndIsActiveTrue(newGroupName.trim());
        if (existingGroup.isPresent() && !existingGroup.get().getId().equals(groupId)) {
            throw new IllegalArgumentException("Bu isimde başka bir grup zaten mevcut");
        }

        group.setGroupName(newGroupName.trim());
        group.setUpdatedBy(updatedBy);

        return instantGroupRepository.save(group);
    }

    /**
     * Grubu aktif/pasif yap
     */
    @Transactional
    @Override
    public InstantGroup toggleGroupStatus(String groupId, String updatedBy) {
        InstantGroup group = instantGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grup bulunamadı"));

        group.setIsActive(!group.getIsActive());
        group.setUpdatedBy(updatedBy);

        return instantGroupRepository.save(group);
    }

    /**
     * Grubu tamamen sil (hard delete)
     */
    @Transactional
    @Override
    public void deleteGroup(String groupId) {
        // Grup var mı kontrol et
        InstantGroup group = instantGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Silinecek grup bulunamadı"));

        // Grubu sil
        instantGroupRepository.delete(group);
    }

    /**
     * Grubu pasif yap (soft delete)
     */
    @Transactional
    @Override
    public InstantGroup deactivateGroup(String groupId, String updatedBy) {
        InstantGroup group = instantGroupRepository.findByIdAndIsActiveTrue(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Aktif grup bulunamadı"));

        group.setIsActive(false);
        group.setUpdatedBy(updatedBy);

        return instantGroupRepository.save(group);
    }

    /**
     * Grubu aktif yap
     */
    @Transactional
    @Override
    public InstantGroup activateGroup(String groupId, String updatedBy) {
        InstantGroup group = instantGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grup bulunamadı"));

        if (group.getIsActive()) {
            throw new IllegalArgumentException("Grup zaten aktif");
        }

        // Aktif yaparken isim çakışması kontrol et
        if (instantGroupRepository.existsByGroupNameIgnoreCaseAndIsActiveTrue(group.getGroupName())) {
            throw new IllegalArgumentException("Bu isimde aktif bir grup zaten mevcut");
        }

        group.setIsActive(true);
        group.setUpdatedBy(updatedBy);

        return instantGroupRepository.save(group);
    }

    /**
     * ID'ye göre grup getir
     */
    @Override
    public Optional<InstantGroup> getGroupById(String groupId) {
        return instantGroupRepository.findById(groupId);
    }

    /**
     * Aktif grup getir
     */
    @Override
    public Optional<InstantGroup> getActiveGroupById(String groupId) {
        return instantGroupRepository.findByIdAndIsActiveTrue(groupId);
    }

    /**
     * Tüm aktif grupları getir (alfabetik sıralı)
     */
    @Override
    public List<InstantGroup> getAllActiveGroups() {
        return instantGroupRepository.findByIsActiveTrueOrderByGroupNameAsc();
    }

    /**
     * Tüm grupları getir (aktif + pasif)
     */
    @Override
    public List<InstantGroup> getAllGroups() {
        return instantGroupRepository.findAll();
    }

    /**
     * Grup adına göre arama
     */
    @Override
    public List<InstantGroup> searchGroupsByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllActiveGroups();
        }
        return instantGroupRepository.findByGroupNameContainingIgnoreCaseAndIsActiveTrue(searchTerm.trim());
    }

    /**
     * Grup adının benzersiz olup olmadığını kontrol et
     */
    public boolean isGroupNameUnique(String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            return false;
        }
        return !instantGroupRepository.existsByGroupNameIgnoreCaseAndIsActiveTrue(groupName.trim());
    }

    /**
     * Grup adının benzersiz olup olmadığını kontrol et (güncelleme için)
     */
    public boolean isGroupNameUniqueForUpdate(String groupId, String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            return false;
        }

        Optional<InstantGroup> existingGroup = instantGroupRepository.findByGroupNameIgnoreCaseAndIsActiveTrue(groupName.trim());
        return existingGroup.isEmpty() || existingGroup.get().getId().equals(groupId);
    }

    /**
     * Aktif grup sayısını getir
     */
    @Override
    public Integer getActiveGroupCount() {
        return instantGroupRepository.countActiveGroups();
    }

//    /**
//     * Bulk grup oluştur - Birden fazla grubu tek seferde oluştur
//     */
//    @Transactional
//    public List<InstantGroup> createBulkGroups(List<String> groupNames, String createdBy) {
//        if (groupNames == null || groupNames.isEmpty()) {
//            throw new IllegalArgumentException("Grup listesi boş olamaz");
//        }
//
//        List<InstantGroup> createdGroups = new ArrayList<>();
//        List<String> errors = new ArrayList<>();
//
//        for (String groupName : groupNames) {
//            try {
//                // Grup adı kontrolü
//                if (groupName == null || groupName.trim().isEmpty()) {
//                    errors.add("Boş grup adı atlandı");
//                    continue;
//                }
//
//                // Aynı isimde grup var mı kontrol et
//                if (instantGroupRepository.existsByGroupNameIgnoreCaseAndIsActiveTrue(groupName.trim())) {
//                    errors.add("'" + groupName + "' isimli grup zaten mevcut");
//                    continue;
//                }
//
//                InstantGroup group = new InstantGroup();
//                group.setId(UUID.randomUUID().toString());
//                group.setGroupName(groupName.trim());
//                group.setCreatedBy(createdBy);
//                group.setIsActive(true);
//
//                InstantGroup savedGroup = instantGroupRepository.save(group);
//                createdGroups.add(savedGroup);
//
//            } catch (Exception e) {
//                errors.add("'" + groupName + "' oluşturulurken hata: " + e.getMessage());
//            }
//        }
//
//        // Eğer hiç grup oluşturulamadıysa hata fırlat
//        if (createdGroups.isEmpty()) {
//            throw new IllegalArgumentException("Hiçbir grup oluşturulamadı. Hatalar: " + String.join(", ", errors));
//        }
//
//        return createdGroups;
//    }
//
//    /**
//     * Varsayılan grupları oluştur
//     */
//    @Transactional
//    @Override
//    public List<InstantGroup> createDefaultGroups(String createdBy) {
//        List<String> defaultGroupNames = Arrays.asList(
//                "Muhasebe/Finans",
//                "Personel",
//                "Teknik Müdür Yardımcısı",
//                "Teknik Müdür",
//                "Genel Kordinatör",
//                "Araçlar",
//                "Personeller",
//                "Kat Sakinleri",
//                "Müşteri/Tedarikçi",
//                "Tedarikçi",
//                "Müşteri"
//        );
//
//        return createBulkGroups(defaultGroupNames, createdBy);
//    }
}
