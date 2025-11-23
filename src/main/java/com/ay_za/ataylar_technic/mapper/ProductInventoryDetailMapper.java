package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.ProductInventoryDetailDto;
import com.ay_za.ataylar_technic.entity.ProductInventoryDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Ürün Envanter Detayı Mapper
 */
@Component
public class ProductInventoryDetailMapper {

    /**
     * Entity'yi DTO'ya dönüştür
     */
    public ProductInventoryDetailDto toDto(ProductInventoryDetail entity) {
        if (entity == null) {
            return null;
        }

        ProductInventoryDetailDto dto = new ProductInventoryDetailDto();
        dto.setId(entity.getId());
        dto.setCategoryId(entity.getCategoryId());
        dto.setCategoryName(entity.getCategoryName());
        dto.setMarketCode(entity.getMarketCode());
        dto.setBrandName(entity.getBrandName());
        dto.setProductName(entity.getProductName());
        dto.setActive(entity.getActive());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());

        return dto;
    }

    /**
     * DTO'yu Entity'ye dönüştür
     */
    public ProductInventoryDetail toEntity(ProductInventoryDetailDto dto) {
        if (dto == null) {
            return null;
        }

        ProductInventoryDetail entity = new ProductInventoryDetail();
        entity.setId(dto.getId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setCategoryName(dto.getCategoryName());
        entity.setMarketCode(dto.getMarketCode());
        entity.setBrandName(dto.getBrandName());
        entity.setProductName(dto.getProductName());
        entity.setActive(dto.getActive());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setUpdatedDate(dto.getUpdatedDate());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedBy(dto.getUpdatedBy());

        return entity;
    }

    /**
     * Entity listesini DTO listesine dönüştür
     */
    public List<ProductInventoryDetailDto> toDtoList(List<ProductInventoryDetail> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * DTO listesini Entity listesine dönüştür
     */
    public List<ProductInventoryDetail> toEntityList(List<ProductInventoryDetailDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

