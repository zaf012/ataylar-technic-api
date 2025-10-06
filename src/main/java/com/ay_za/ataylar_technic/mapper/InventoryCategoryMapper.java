package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.InventoryCategoryDto;
import com.ay_za.ataylar_technic.entity.InventoryCategory;
import com.ay_za.ataylar_technic.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryCategoryMapper extends BaseMapper<InventoryCategory, InventoryCategoryDto> {

    @Mapping(source = "parentCategory.id", target = "parentCategoryId")
    @Mapping(source = "parentCategory.categoryName", target = "parentCategoryName")
    @Mapping(target = "subCategories", ignore = true) // Manuel olarak handle edeceğiz
    InventoryCategoryDto convertToDTO(InventoryCategory entity);

    @Mapping(target = "parentCategory", ignore = true) // Service layer'da handle edeceğiz
    @Mapping(target = "subCategories", ignore = true)
    InventoryCategory convertToEntity(InventoryCategoryDto dto);
//
//    // Ek metodlar
//    default InventoryCategoryDto toDto(InventoryCategory entity) {
//        return convertToDTO(entity);
//    }
//
//    default InventoryCategory toEntity(InventoryCategoryDto dto) {
//        return convertToEntity(dto);
//    }
}
