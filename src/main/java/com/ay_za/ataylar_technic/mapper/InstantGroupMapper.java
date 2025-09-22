package com.ay_za.ataylar_technic.mapper;


import com.ay_za.ataylar_technic.dto.InstantGroupDto;
import com.ay_za.ataylar_technic.entity.InstantGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstantGroupMapper extends BaseMapper<InstantGroup, InstantGroupDto> {


}
