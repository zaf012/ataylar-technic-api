package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.ProjectsInfoDto;
import com.ay_za.ataylar_technic.entity.ProjectsInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectsInfoServiceImpl {

    @Transactional
    ProjectsInfoDto createProject(String firmId, String firmName, String projectName);

    @Transactional
    ProjectsInfoDto updateProject(String projectId, String firmId, String firmName, String projectName);

    @Transactional
    void deleteProject(String projectId);

    Optional<ProjectsInfoDto> getProjectById(String projectId);

    List<ProjectsInfoDto> getAllProjects();

    List<ProjectsInfoDto> getProjectsByFirmId(String firmId);

    List<ProjectsInfoDto> searchProjectsByName(String searchTerm);

    boolean existsByProjectName(String projectName);

    boolean existsByFirmIdAndProjectName(String firmId, String projectName);

    Integer getProjectCount();

    Integer getProjectCountByFirmId(String firmId);

    boolean checkProjectById(String projectId);

    Optional<ProjectsInfo> getRandomProject();
}
