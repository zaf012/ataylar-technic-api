package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.InstantGroupDto;
import com.ay_za.ataylar_technic.entity.InstantGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InstantGroupServiceImpl {
    @Transactional
    InstantGroupDto createGroup(String groupName, String createdBy);

    @Transactional
    InstantGroupDto updateGroupName(String groupId, String newGroupName, String updatedBy);

    @Transactional
    void deleteGroup(String groupId);

    Optional<InstantGroupDto> getGroupById(String groupId);

    List<InstantGroupDto> getAllGroups();

    Boolean checkGroupById(String id);

    List<InstantGroupDto> searchGroupsByName(String searchTerm);

    Optional<InstantGroup> getRandomGroup();

    Integer getGroupCount();

    @Transactional
    List<InstantGroupDto> createDefaultGroups(String createdBy);
}
