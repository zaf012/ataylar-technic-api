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
        dto.setIsMainCategory(entity.getIsMainCategory());
        dto.setMarketCode(entity.getMarketCode());
        dto.setProductName(entity.getProductName());
        dto.setCategoryCode(entity.getCategoryCode());
        dto.setQrCode(entity.getQrCode());
        dto.setDescription(entity.getDescription());
        dto.setSortOrder(entity.getSortOrder());
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
        entity.setIsMainCategory(dto.getIsMainCategory());
        entity.setMarketCode(dto.getMarketCode());
        entity.setProductName(dto.getProductName());
        entity.setCategoryCode(dto.getCategoryCode());
        entity.setQrCode(dto.getQrCode());
        entity.setDescription(dto.getDescription());
        entity.setSortOrder(dto.getSortOrder());
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

    // Alt kategorilerle birlikte DTO'ya çevirme (hiyerarşik yapı için)
    public InventoryCategoryDto toDtoWithSubCategories(InventoryCategory entity, List<InventoryCategory> subCategories) {
        InventoryCategoryDto dto = toDto(entity);
        if (subCategories != null && !subCategories.isEmpty()) {
            dto.setSubCategories(toDtoList(subCategories));
        }
        return dto;
    }
}

