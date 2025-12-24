package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.ServiceCaseDto;
import com.ay_za.ataylar_technic.entity.ServiceCase;
import com.ay_za.ataylar_technic.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceCaseMapper extends BaseMapper<ServiceCase, ServiceCaseDto> {
}

