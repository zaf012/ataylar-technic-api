package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.ProjectsInfoDto;
import com.ay_za.ataylar_technic.entity.ProjectsInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectsInfoMapper extends BaseMapper<ProjectsInfo, ProjectsInfoDto> {
}
