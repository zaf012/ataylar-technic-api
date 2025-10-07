package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.SiteDeviceInventoryInfoDto;
import com.ay_za.ataylar_technic.entity.SiteDeviceInventoryInfo;
import com.ay_za.ataylar_technic.mapper.base.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SiteDeviceInventoryInfoMapper implements BaseMapper<SiteDeviceInventoryInfo, SiteDeviceInventoryInfoDto> {

    @Override
    public SiteDeviceInventoryInfo convertToEntity(SiteDeviceInventoryInfoDto dto) {
        if (dto == null) {
            return null;
        }

        SiteDeviceInventoryInfo entity = new SiteDeviceInventoryInfo();
        entity.setId(dto.getId());
        entity.setSiteId(dto.getSiteId());
        entity.setSiteName(dto.getSiteName());
        entity.setAda(dto.getAda());
        entity.setBlockName(dto.getBlockName());
        entity.setApartmentNumber(dto.getApartmentNumber());
        entity.setFloor(dto.getFloor());
        entity.setLocation(dto.getLocation());
        entity.setInventoryCategoryId(dto.getInventoryCategoryId());
        entity.setSystemName(dto.getSystemName());
        entity.setCategoryHierarchy(dto.getCategoryHierarchy());
        entity.setDeviceSpecification(dto.getDeviceSpecification());
        entity.setQrCode(dto.getQrCode());
        entity.setIsActive(dto.getIsActive());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedDate(dto.getUpdatedDate());
        entity.setUpdatedBy(dto.getUpdatedBy());

        return entity;
    }

    @Override
    public SiteDeviceInventoryInfoDto convertToDTO(SiteDeviceInventoryInfo entity) {
        if (entity == null) {
            return null;
        }

        SiteDeviceInventoryInfoDto dto = new SiteDeviceInventoryInfoDto();
        dto.setId(entity.getId());
        dto.setSiteId(entity.getSiteId());
        dto.setSiteName(entity.getSiteName());
        dto.setAda(entity.getAda());
        dto.setBlockName(entity.getBlockName());
        dto.setApartmentNumber(entity.getApartmentNumber());
        dto.setFloor(entity.getFloor());
        dto.setLocation(entity.getLocation());
        dto.setInventoryCategoryId(entity.getInventoryCategoryId());
        dto.setSystemName(entity.getSystemName());
        dto.setCategoryHierarchy(entity.getCategoryHierarchy());
        dto.setDeviceSpecification(entity.getDeviceSpecification());
        dto.setQrCode(entity.getQrCode());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setUpdatedBy(entity.getUpdatedBy());

        return dto;
    }

    @Override
    public List<SiteDeviceInventoryInfo> convertAllToEntity(List<SiteDeviceInventoryInfoDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SiteDeviceInventoryInfoDto> convertAllToDTO(List<SiteDeviceInventoryInfo> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
