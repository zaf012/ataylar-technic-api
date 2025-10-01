package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.FirmsInfoDto;
import com.ay_za.ataylar_technic.dto.ProjectsInfoDto;
import com.ay_za.ataylar_technic.entity.ProjectsInfo;
import com.ay_za.ataylar_technic.mapper.ProjectsInfoMapper;
import com.ay_za.ataylar_technic.repository.ProjectsInfoRepository;
import com.ay_za.ataylar_technic.service.base.FirmsInfoServiceImpl;
import com.ay_za.ataylar_technic.service.base.ProjectsInfoServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectsInfoService implements ProjectsInfoServiceImpl {

    private final ProjectsInfoRepository projectsInfoRepository;
    private final FirmsInfoServiceImpl firmsInfoServiceImpl;
    private final ProjectsInfoMapper projectsInfoMapper;

    public ProjectsInfoService(ProjectsInfoRepository projectsInfoRepository, FirmsInfoServiceImpl firmsInfoServiceImpl,
                               ProjectsInfoMapper projectsInfoMapper) {
        this.projectsInfoRepository = projectsInfoRepository;
        this.firmsInfoServiceImpl = firmsInfoServiceImpl;
        this.projectsInfoMapper = projectsInfoMapper;
    }

    /**
     * Yeni proje oluştur
     */
    @Transactional
    @Override
    public ProjectsInfoDto createProject(String firmId, String projectName, String createdBy) {
        // Parametreler kontrolü
        if (firmId == null || firmId.trim().isEmpty()) {
            throw new IllegalArgumentException("Firma ID boş olamaz");
        }

        if (projectName == null || projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Proje adı boş olamaz");
        }

        // Firma var mı kontrol et
        if (!firmsInfoServiceImpl.checkFirmById(firmId)) {
            throw new IllegalArgumentException("Belirtilen firma bulunamadı");
        }

        // Aynı firma için aynı isimde proje var mı kontrol et
        if (projectsInfoRepository.existsByFirmIdAndProjectNameIgnoreCase(firmId, projectName.trim())) {
            throw new IllegalArgumentException("Bu firmada aynı isimde bir proje zaten mevcut");
        }

        Optional<FirmsInfoDto> firmById = firmsInfoServiceImpl.getFirmById(firmId);

        ProjectsInfo project = new ProjectsInfo();
        project.setId(UUID.randomUUID().toString());
        project.setFirmId(firmId.trim());
        project.setFirmName(firmById.get().getFirmName());
        project.setProjectName(projectName.trim());
        project.setCreatedBy(createdBy);

        ProjectsInfo savedProject = projectsInfoRepository.save(project);
        return projectsInfoMapper.convertToDTO(savedProject);
    }

    /**
     * Proje güncelle
     */
    @Transactional
    @Override
    public ProjectsInfoDto updateProject(String projectId, String projectName, String updatedBy) {
        // Parametreler kontrolü
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Proje adı boş olamaz");
        }

        // Proje var mı kontrol et
        ProjectsInfo project = projectsInfoRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Proje bulunamadı"));

        // Aynı firma için aynı isimde başka proje var mı kontrol et (kendisi hariç)
        if (projectsInfoRepository.existsByFirmIdAndProjectNameIgnoreCase(project.getFirmId(), projectName.trim())) {
            Optional<ProjectsInfo> existingProject = projectsInfoRepository.findByProjectNameIgnoreCase(projectName.trim());
            if (existingProject.isPresent() && !existingProject.get().getId().equals(projectId)) {
                throw new IllegalArgumentException("Bu firmada aynı isimde başka bir proje zaten mevcut");
            }
        }

        project.setProjectName(projectName.trim());
        project.setUpdatedBy(updatedBy);

        ProjectsInfo savedProject = projectsInfoRepository.save(project);
        return projectsInfoMapper.convertToDTO(savedProject);
    }

    /**
     * Projeyi sil
     */
    @Transactional
    @Override
    public void deleteProject(String projectId) {
        // Proje var mı kontrol et
        ProjectsInfo project = projectsInfoRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Silinecek proje bulunamadı"));

        // Projeyi sil
        projectsInfoRepository.delete(project);
    }

    /**
     * ID'ye göre proje getir
     */
    @Override
    public Optional<ProjectsInfoDto> getProjectById(String projectId) {
        Optional<ProjectsInfo> project = projectsInfoRepository.findById(projectId);
        return project.map(projectsInfoMapper::convertToDTO);
    }

    /**
     * Tüm projeleri getir
     */
    @Override
    public List<ProjectsInfoDto> getAllProjects() {
        List<ProjectsInfo> projects = projectsInfoRepository.findAllByOrderByProjectNameAsc();
        return projectsInfoMapper.convertAllToDTO(projects);
    }

    /**
     * Firma ID'sine göre projeleri getir
     */
    @Override
    public List<ProjectsInfoDto> getProjectsByFirmId(String firmId) {
        List<ProjectsInfo> projects = projectsInfoRepository.findByFirmIdOrderByProjectNameAsc(firmId);
        return projectsInfoMapper.convertAllToDTO(projects);
    }

    /**
     * Proje adına göre arama
     */
    @Override
    public List<ProjectsInfoDto> searchProjectsByName(String searchTerm) {
        List<ProjectsInfo> projects;
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            projects = projectsInfoRepository.findAllByOrderByProjectNameAsc();
        } else {
            projects = projectsInfoRepository.findByProjectNameContainingIgnoreCase(searchTerm.trim());
        }
        return projectsInfoMapper.convertAllToDTO(projects);
    }

    /**
     * Proje adının varlığını kontrol et
     */
    @Override
    public boolean existsByProjectName(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            return false;
        }
        return projectsInfoRepository.existsByProjectNameIgnoreCase(projectName.trim());
    }

    /**
     * Firma ID ve proje adı kombinasyonunun varlığını kontrol et
     */
    @Override
    public boolean existsByFirmIdAndProjectName(String firmId, String projectName) {
        if (firmId == null || firmId.trim().isEmpty() || projectName == null || projectName.trim().isEmpty()) {
            return false;
        }
        return projectsInfoRepository.existsByFirmIdAndProjectNameIgnoreCase(firmId.trim(), projectName.trim());
    }

    /**
     * Toplam proje sayısını getir
     */
    @Override
    public Integer getProjectCount() {
        return projectsInfoRepository.countProjects();
    }

    /**
     * Belirli firmadaki proje sayısını getir
     */
    @Override
    public Integer getProjectCountByFirmId(String firmId) {
        return projectsInfoRepository.countProjectsByFirmId(firmId);
    }

    /**
     * Proje varlık kontrolü
     */
    @Override
    public boolean checkProjectById(String projectId) {
        return projectsInfoRepository.existsById(projectId);
    }
}
