package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.InventoryCategoryDto;
import com.ay_za.ataylar_technic.entity.InventoryCategory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("inventoryCategoryCustomMapper")
public class InventoryCategoryMapper {

    public InventoryCategoryDto toDto(InventoryCategory entity) {
        if (entity == null) {
            return null;
        }

        InventoryCategoryDto dto = new InventoryCategoryDto();
        dto.setId(entity.getId());
        dto.setCategoryName(entity.getCategoryName());
        dto.setMainCategoryId(entity.getMainCategoryId());
        dto.setMainCategoryName(entity.getMainCategoryName());
        dto.setIsMainCategory(entity.getIsMainCategory());
        dto.setMarketCode(entity.getMarketCode());
        dto.setProductName(entity.getProductName());
        dto.setBrandName(entity.getBrandName());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());

        return dto;
    }

    public InventoryCategory toEntity(InventoryCategoryDto dto) {
        if (dto == null) {
            return null;
        }

        InventoryCategory entity = new InventoryCategory();
        entity.setId(dto.getId());
        entity.setCategoryName(dto.getCategoryName());
        entity.setMainCategoryId(dto.getMainCategoryId());
        entity.setMainCategoryName(dto.getMainCategoryName());
        entity.setIsMainCategory(dto.getIsMainCategory());
        entity.setMarketCode(dto.getMarketCode());
        entity.setProductName(dto.getProductName());
        entity.setBrandName(dto.getBrandName());
        entity.setIsActive(dto.getIsActive());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());

        return entity;
    }

    public List<InventoryCategoryDto> toDtoList(List<InventoryCategory> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<InventoryCategory> toEntityList(List<InventoryCategoryDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

