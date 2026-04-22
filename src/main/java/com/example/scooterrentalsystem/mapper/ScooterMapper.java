package com.example.scooterrentalsystem.mapper;

import com.example.scooterrentalsystem.dto.ScooterResponseDto;
import com.example.scooterrentalsystem.entity.Scooter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScooterMapper {

    @Mapping(target = "modelName", source = "scooterModel.name")
    @Mapping(target = "rentalPointName", source = "rentalPoint.name")
    @Mapping(target = "status", expression = "java(scooter.getStatus() != null ? scooter.getStatus().name() : null)")
    ScooterResponseDto toDto(Scooter scooter);

    List<ScooterResponseDto> toDtoList(List<Scooter> scooters);
}
