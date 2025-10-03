package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.FirmsInfoDto;
import com.ay_za.ataylar_technic.entity.FirmsInfo;
import com.ay_za.ataylar_technic.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FirmsInfoMapper extends BaseMapper<FirmsInfo, FirmsInfoDto> {
}
