package com.ay_za.ataylar_technic.mapper;


import com.ay_za.ataylar_technic.dto.UserTypeDto;
import com.ay_za.ataylar_technic.entity.UserType;
import com.ay_za.ataylar_technic.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserTypeMapper extends BaseMapper<UserType, UserTypeDto> {


}
