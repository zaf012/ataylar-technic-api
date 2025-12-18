package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.SiteProductInventoryDetailDto;
import com.ay_za.ataylar_technic.entity.SiteProductInventoryDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Site Ürün Envanter Detayı Mapper
 */
@Component
public class SiteProductInventoryDetailMapper {

    /**
     * Entity'yi DTO'ya dönüştür
     */
    public SiteProductInventoryDetailDto toDto(SiteProductInventoryDetail entity) {
        if (entity == null) {
            return null;
        }

        SiteProductInventoryDetailDto dto = new SiteProductInventoryDetailDto();
        dto.setId(entity.getId());
        dto.setSiteId(entity.getSiteId());
        dto.setSiteName(entity.getSiteName());
        dto.setSquareId(entity.getSquareId());
        dto.setSquareName(entity.getSquareName());
        dto.setBlockId(entity.getBlockId());
        dto.setBlockName(entity.getBlockName());
        dto.setFloorNumber(entity.getFloorNumber());
        dto.setLocation(entity.getLocation());
        dto.setSystemId(entity.getSystemId());
        dto.setSystemName(entity.getSystemName());
        dto.setCategoryId(entity.getCategoryId());
        dto.setCategoryName(entity.getCategoryName());
        dto.setProductInventoryDetailId(entity.getProductInventoryDetailId());
        dto.setProductName(entity.getProductName());
        dto.setQrCode(entity.getQrCode());
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
    public SiteProductInventoryDetail toEntity(SiteProductInventoryDetailDto dto) {
        if (dto == null) {
            return null;
        }

        SiteProductInventoryDetail entity = new SiteProductInventoryDetail();
        entity.setId(dto.getId());
        entity.setSiteId(dto.getSiteId());
        entity.setSiteName(dto.getSiteName());
        entity.setSquareId(dto.getSquareId());
        entity.setSquareName(dto.getSquareName());
        entity.setBlockId(dto.getBlockId());
        entity.setBlockName(dto.getBlockName());
        entity.setFloorNumber(dto.getFloorNumber());
        entity.setLocation(dto.getLocation());
        entity.setSystemId(dto.getSystemId());
        entity.setSystemName(dto.getSystemName());
        entity.setCategoryId(dto.getCategoryId());
        entity.setCategoryName(dto.getCategoryName());
        entity.setProductInventoryDetailId(dto.getProductInventoryDetailId());
        entity.setProductName(dto.getProductName());
        entity.setQrCode(dto.getQrCode());
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
    public List<SiteProductInventoryDetailDto> toDtoList(List<SiteProductInventoryDetail> entities) {
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
    public List<SiteProductInventoryDetail> toEntityList(List<SiteProductInventoryDetailDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

