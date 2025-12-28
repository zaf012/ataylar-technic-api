package com.ay_za.ataylar_technic.mapper;

import com.ay_za.ataylar_technic.dto.MaintenancePdfRecordDto;
import com.ay_za.ataylar_technic.entity.MaintenancePdfRecord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MaintenancePdfRecordMapper {

    public MaintenancePdfRecordDto toDto(MaintenancePdfRecord entity) {
        if (entity == null) {
            return null;
        }

        MaintenancePdfRecordDto dto = new MaintenancePdfRecordDto();
        dto.setId(entity.getId());
        dto.setCustomerFirmName(entity.getCustomerFirmName());
        dto.setCustomerAddress(entity.getCustomerAddress());
        dto.setAuthorizedPersonnel(entity.getAuthorizedPersonnel());
        dto.setTelNo(entity.getTelNo());
        dto.setGsmNo(entity.getGsmNo());
        dto.setEmail(entity.getEmail());
        dto.setSystemName(entity.getSystemName());
        dto.setProductSerialNo(entity.getProductSerialNo());
        dto.setServiceDate(entity.getServiceDate());
        dto.setDescription(entity.getDescription());
        dto.setFileName(entity.getFileName());
        dto.setFilePath(entity.getFilePath());
        dto.setFileSizeBytes(entity.getFileSizeBytes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setReportNo(entity.getReportNo());

        return dto;
    }

    public MaintenancePdfRecord toEntity(MaintenancePdfRecordDto dto) {
        if (dto == null) {
            return null;
        }

        MaintenancePdfRecord entity = new MaintenancePdfRecord();
        entity.setId(dto.getId());
        entity.setCustomerFirmName(dto.getCustomerFirmName());
        entity.setCustomerAddress(dto.getCustomerAddress());
        entity.setAuthorizedPersonnel(dto.getAuthorizedPersonnel());
        entity.setTelNo(dto.getTelNo());
        entity.setGsmNo(dto.getGsmNo());
        entity.setEmail(dto.getEmail());
        entity.setSystemName(dto.getSystemName());
        entity.setProductSerialNo(dto.getProductSerialNo());
        entity.setServiceDate(dto.getServiceDate());
        entity.setDescription(dto.getDescription());
        entity.setFileName(dto.getFileName());
        entity.setFilePath(dto.getFilePath());
        entity.setFileSizeBytes(dto.getFileSizeBytes());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setReportNo(dto.getReportNo());

        return entity;
    }

    public List<MaintenancePdfRecordDto> toDtoList(List<MaintenancePdfRecord> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<MaintenancePdfRecord> toEntityList(List<MaintenancePdfRecordDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

