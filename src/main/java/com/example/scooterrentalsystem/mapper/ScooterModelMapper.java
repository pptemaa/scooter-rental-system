package com.example.scooterrentalsystem.mapper;

import com.example.scooterrentalsystem.dto.ScooterModelCreateDto;
import com.example.scooterrentalsystem.dto.ScooterModelResponseDto;
import com.example.scooterrentalsystem.entity.ScooterModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScooterModelMapper {

    @Mapping(target = "maxWeight", source = "max_weight")
    ScooterModelResponseDto toDto(ScooterModel model);

    List<ScooterModelResponseDto> toDtoList(List<ScooterModel> models);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "max_weight", source = "maxWeight")
    ScooterModel toEntity(ScooterModelCreateDto dto);
}
