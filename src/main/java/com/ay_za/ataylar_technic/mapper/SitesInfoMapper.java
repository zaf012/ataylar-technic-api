package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.SitesInfoDto;
import com.ay_za.ataylar_technic.entity.SitesInfo;
import com.ay_za.ataylar_technic.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SitesInfoMapper extends BaseMapper<SitesInfo, SitesInfoDto> {
}
