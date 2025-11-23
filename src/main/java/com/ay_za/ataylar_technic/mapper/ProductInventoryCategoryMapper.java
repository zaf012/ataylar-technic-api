package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.ProductInventoryCategoryDto;
import com.ay_za.ataylar_technic.entity.ProductInventoryCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ürün Envanter Kategorileri Mapper
 */
@Component
public class ProductInventoryCategoryMapper {

    /**
     * Entity'yi DTO'ya dönüştür
     */
    public ProductInventoryCategoryDto toDto(ProductInventoryCategory entity) {
        if (entity == null) {
            return null;
        }

        ProductInventoryCategoryDto dto = new ProductInventoryCategoryDto();
        dto.setId(entity.getId());
        dto.setCategoryName(entity.getCategoryName());
        dto.setCategoryDescription(entity.getCategoryDescription());
        dto.setParentCategoryId(entity.getParentCategoryId());
        dto.setCategoryLevel(entity.getCategoryLevel());
        dto.setDisplayOrder(entity.getDisplayOrder());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());

        return dto;
    }

    /**
     * DTO'yu Entity'ye dönüştür
     */
    public ProductInventoryCategory toEntity(ProductInventoryCategoryDto dto) {
        if (dto == null) {
            return null;
        }

        ProductInventoryCategory entity = new ProductInventoryCategory();
        entity.setId(dto.getId());
        entity.setCategoryName(dto.getCategoryName());
        entity.setCategoryDescription(dto.getCategoryDescription());
        entity.setParentCategoryId(dto.getParentCategoryId());
        entity.setCategoryLevel(dto.getCategoryLevel());
        entity.setDisplayOrder(dto.getDisplayOrder());
        entity.setIsActive(dto.getIsActive());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setUpdatedDate(dto.getUpdatedDate());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());

        return entity;
    }

    /**
     * Entity listesini DTO listesine dönüştür
     */
    public List<ProductInventoryCategoryDto> toDtoList(List<ProductInventoryCategory> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * DTO listesini Entity listesine dönüştür
     */
    public List<ProductInventoryCategory> toEntityList(List<ProductInventoryCategoryDto> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Mevcut entity'yi DTO'dan gelen verilerle güncelle
     * ID, createdDate ve createdBy korunur
     */
    public void updateEntityFromDto(ProductInventoryCategoryDto dto, ProductInventoryCategory entity) {
        if (dto == null || entity == null) {
            return;
        }

        // ID, createdDate ve createdBy değişmez
        if (dto.getCategoryName() != null) {
            entity.setCategoryName(dto.getCategoryName());
        }
        if (dto.getCategoryDescription() != null) {
            entity.setCategoryDescription(dto.getCategoryDescription());
        }
        if (dto.getParentCategoryId() != null) {
            entity.setParentCategoryId(dto.getParentCategoryId());
        }
        if (dto.getCategoryLevel() != null) {
            entity.setCategoryLevel(dto.getCategoryLevel());
        }
        if (dto.getDisplayOrder() != null) {
            entity.setDisplayOrder(dto.getDisplayOrder());
        }
        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
        if (dto.getUpdatedBy() != null) {
            entity.setUpdatedBy(dto.getUpdatedBy());
        }
    }
}

