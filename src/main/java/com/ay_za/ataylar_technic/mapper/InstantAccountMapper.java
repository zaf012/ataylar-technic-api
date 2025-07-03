package com.ay_za.ataylar_technic.mapper;


import com.ay_za.ataylar_technic.dto.InstantAccountDto;
import com.ay_za.ataylar_technic.entity.InstantAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstantAccountMapper extends BaseMapper<InstantAccount, InstantAccountDto> {


}
