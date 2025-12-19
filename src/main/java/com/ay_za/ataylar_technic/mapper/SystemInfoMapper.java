package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.SystemInfoDto;
import com.ay_za.ataylar_technic.entity.SystemInfo;
import com.ay_za.ataylar_technic.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SystemInfoMapper extends BaseMapper<SystemInfo, SystemInfoDto> {
    // Artık özel mapping'e gerek yok - alan adları aynı
}
