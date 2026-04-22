package com.example.scooterrentalsystem.mapper;

import com.example.scooterrentalsystem.dto.RentalResponseDto;
import com.example.scooterrentalsystem.entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "scooterId", source = "scooter.id")
    @Mapping(target = "scooterModelName", source = "scooter.scooterModel.name")
    @Mapping(target = "tariffId", source = "tariff.id")
    @Mapping(target = "tariffName", source = "tariff.name")
    @Mapping(target = "startPointId", source = "startPoint.id")
    @Mapping(target = "endPointId", source = "endPoint.id")
    RentalResponseDto toDto(Rental rental);

    List<RentalResponseDto> toDtoList(List<Rental> rentals);
}
