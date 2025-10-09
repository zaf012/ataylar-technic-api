package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.SystemInfoDto;
import com.ay_za.ataylar_technic.entity.SystemInfo;
import com.ay_za.ataylar_technic.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SystemInfoMapper extends BaseMapper<SystemInfo, SystemInfoDto> {

    @Override
    @Mapping(source = "isActive", target = "active")
    @Mapping(source = "isChecklist", target = "checklist")
    @Mapping(source = "isFault", target = "fault")
    SystemInfo convertToEntity(SystemInfoDto dto);

    @Override
    @Mapping(source = "active", target = "isActive")
    @Mapping(source = "checklist", target = "isChecklist")
    @Mapping(source = "fault", target = "isFault")
    SystemInfoDto convertToDTO(SystemInfo entity);
}
