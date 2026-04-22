package com.example.scooterrentalsystem.mapper;

import com.example.scooterrentalsystem.dto.TariffCreateDto;
import com.example.scooterrentalsystem.dto.TariffResponseDto;
import com.example.scooterrentalsystem.entity.Tariff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TariffMapper {
    TariffResponseDto toDto(Tariff tariff);
    List<TariffResponseDto> toDtoList(List<Tariff> tariffs);
    @Mapping(target = "id",ignore = true)
    Tariff toEntity(TariffCreateDto responseDto);


}
