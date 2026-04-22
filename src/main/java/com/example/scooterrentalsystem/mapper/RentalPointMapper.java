package com.example.scooterrentalsystem.mapper;

import com.example.scooterrentalsystem.dto.RentalPointResponseDto;
import com.example.scooterrentalsystem.entity.RentalPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalPointMapper {

    @Mapping(target = "parentId", source = "parent.id")
    RentalPointResponseDto toDto(RentalPoint rentalPoint);

    List<RentalPointResponseDto> toDtoList(List<RentalPoint> rentalPoints);
}
