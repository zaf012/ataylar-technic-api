package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.SquaresInfoDto;
import com.ay_za.ataylar_technic.entity.SquaresInfo;
import com.ay_za.ataylar_technic.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SquaresInfoMapper extends BaseMapper<SquaresInfo, SquaresInfoDto> {
}
