package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.InstantGroupDto;
import com.ay_za.ataylar_technic.entity.InstantGroup;
import com.ay_za.ataylar_technic.mapper.InstantGroupMapper;
import com.ay_za.ataylar_technic.repository.InstantGroupRepository;
import com.ay_za.ataylar_technic.service.base.InstantGroupServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class InstantGroupService implements InstantGroupServiceImpl {

    private final InstantGroupRepository instantGroupRepository;
    private final InstantGroupMapper instantGroupMapper;

    public InstantGroupService(InstantGroupRepository instantGroupRepository, InstantGroupMapper instantGroupMapper) {
        this.instantGroupRepository = instantGroupRepository;
        this.instantGroupMapper = instantGroupMapper;
    }

    /**
     * Yeni grup oluştur
     */
    @Transactional
    @Override
    public InstantGroupDto createGroup(String groupName, String createdBy) {
        // Grup adı kontrolü
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Grup adı boş olamaz");
        }

        // Aynı isimde grup var mı kontrol et
        if (instantGroupRepository.existsByGroupNameIgnoreCase(groupName.trim())) {
            throw new IllegalArgumentException("Bu isimde bir grup zaten mevcut");
        }

        InstantGroup group = new InstantGroup();
        group.setId(UUID.randomUUID().toString());
        group.setGroupName(groupName.trim());
        group.setCreatedBy(createdBy);
        InstantGroup saved = instantGroupRepository.save(group);
        return instantGroupMapper.convertToDTO(saved);

    }

    /**
     * Grup adını güncelle
     */
    @Transactional
    @Override
    public InstantGroupDto updateGroupName(String groupId, String newGroupName, String updatedBy) {
        // Parametreler kontrolü
        if (newGroupName == null || newGroupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Grup adı boş olamaz");
        }

        // Grup var mı kontrol et
        InstantGroup group = instantGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grup bulunamadı"));

        // Aynı isimde başka grup var mı kontrol et (kendisi hariç)
        Optional<InstantGroup> existingGroup = instantGroupRepository.findByGroupNameIgnoreCase(newGroupName.trim());
        if (existingGroup.isPresent() && !existingGroup.get().getId().equals(groupId)) {
            throw new IllegalArgumentException("Bu isimde başka bir grup zaten mevcut");
        }

        group.setGroupName(newGroupName.trim());
        group.setUpdatedBy(updatedBy);

        InstantGroup savedGroup = instantGroupRepository.save(group);
        return instantGroupMapper.convertToDTO(savedGroup);
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
     * ID'ye göre grup getir
     */
    @Override
    public Optional<InstantGroupDto> getGroupById(String groupId) {
        Optional<InstantGroup> group = instantGroupRepository.findById(groupId);
        return group.map(instantGroupMapper::convertToDTO);
    }

    /**
     * Tüm grupları getir
     */
    @Override
    public List<InstantGroupDto> getAllGroups() {
        List<InstantGroup> groups = instantGroupRepository.findAll();
        return instantGroupMapper.convertAllToDTO(groups);
    }

    /**
     * Grup varlık kontrolü
     */
    @Override
    public Boolean checkGroupById(String id) {
        return instantGroupRepository.existsById(id);
    }

    /**
     * Grup adına göre arama
     */
    @Override
    public List<InstantGroupDto> searchGroupsByName(String searchTerm) {
        List<InstantGroup> groups;
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            groups = instantGroupRepository.findAll();
        } else {
            groups = instantGroupRepository.findByGroupNameContainingIgnoreCase(searchTerm.trim());
        }
        return instantGroupMapper.convertAllToDTO(groups);
    }

    /**
     * Grup adının benzersiz olup olmadığını kontrol et
     */
    public boolean isGroupNameUnique(String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            return false;
        }
        return !instantGroupRepository.existsByGroupNameIgnoreCase(groupName.trim());
    }

    /**
     * Grup adının benzersiz olup olmadığını kontrol et (güncelleme için)
     */
    public boolean isGroupNameUniqueForUpdate(String groupId, String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            return false;
        }

        Optional<InstantGroup> existingGroup = instantGroupRepository.findByGroupNameIgnoreCase(groupName.trim());
        return existingGroup.isEmpty() || existingGroup.get().getId().equals(groupId);
    }

    /**
     * Random grup getir
     */
    @Override
    public Optional<InstantGroup> getRandomGroup() {
        List<InstantGroup> allGroups = instantGroupRepository.findAllGroups();
        if (allGroups.isEmpty()) {
            return Optional.empty();
        }
        Random random = new Random();
        return Optional.of(allGroups.get(random.nextInt(allGroups.size())));
    }

    /**
     * Grup sayısını getir
     */
    @Override
    public Integer getGroupCount() {
        return instantGroupRepository.countGroups();
    }

    /**
     * Bulk grup oluştur - Birden fazla grubu tek seferde oluştur
     */
    @Transactional
    public List<InstantGroupDto> createBulkGroups(List<String> groupNames, String createdBy) {
        if (groupNames == null || groupNames.isEmpty()) {
            throw new IllegalArgumentException("Grup listesi boş olamaz");
        }

        List<InstantGroup> createdGroups = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (String groupName : groupNames) {
            try {
                // Grup adı kontrolü
                if (groupName == null || groupName.trim().isEmpty()) {
                    errors.add("Boş grup adı atlandı");
                    continue;
                }

                // Aynı isimde grup var mı kontrol et
                if (instantGroupRepository.existsByGroupNameIgnoreCase(groupName.trim())) {
                    errors.add("'" + groupName + "' isimli grup zaten mevcut");
                    continue;
                }

                InstantGroup group = new InstantGroup();
                group.setId(UUID.randomUUID().toString());
                group.setGroupName(groupName.trim());
                group.setCreatedBy(createdBy);

                InstantGroup savedGroup = instantGroupRepository.save(group);
                createdGroups.add(savedGroup);

            } catch (Exception e) {
                errors.add("'" + groupName + "' oluşturulurken hata: " + e.getMessage());
            }
        }

        // Eğer hiç grup oluşturulamadıysa hata fırlat
        if (createdGroups.isEmpty()) {
            throw new IllegalArgumentException("Hiçbir grup oluşturulamadı. Hatalar: " + String.join(", ", errors));
        }

        return instantGroupMapper.convertAllToDTO(createdGroups);
    }

    /**
     * Varsayılan grupları oluştur
     */
    @Transactional
    @Override
    public List<InstantGroupDto> createDefaultGroups(String createdBy) {
        List<String> defaultGroupNames = Arrays.asList(
                "Muhasebe/Finans",
                "Personel",
                "Teknik Müdür Yardımcısı",
                "Teknik Müdür",
                "Genel Kordinatör",
                "Araçlar",
                "Personeller",
                "Kat Sakinleri",
                "Müşteri/Tedarikçi",
                "Tedarikçi",
                "Müşteri"
        );

        return createBulkGroups(defaultGroupNames, createdBy);
    }
}
