package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.entity.InstantGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InstantGroupServiceImpl {
    @Transactional
    InstantGroup createGroup(String groupName, String createdBy);

    @Transactional
    InstantGroup updateGroupName(String groupId, String newGroupName, String updatedBy);

    @Transactional
    InstantGroup toggleGroupStatus(String groupId, String updatedBy);

    @Transactional
    void deleteGroup(String groupId);

    @Transactional
    InstantGroup deactivateGroup(String groupId, String updatedBy);

    @Transactional
    InstantGroup activateGroup(String groupId, String updatedBy);

    Optional<InstantGroup> getGroupById(String groupId);

    Optional<InstantGroup> getActiveGroupById(String groupId);

    List<InstantGroup> getAllActiveGroups();

    List<InstantGroup> getAllGroups();

    Boolean checkGroupById(String id);

    List<InstantGroup> searchGroupsByName(String searchTerm);

    Integer getActiveGroupCount();

   @Transactional
   List<InstantGroup> createDefaultGroups(String createdBy);
}
