package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.BlocksInfoDto;
import com.ay_za.ataylar_technic.entity.BlocksInfo;
import com.ay_za.ataylar_technic.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlocksInfoMapper extends BaseMapper<BlocksInfo, BlocksInfoDto> {
}
