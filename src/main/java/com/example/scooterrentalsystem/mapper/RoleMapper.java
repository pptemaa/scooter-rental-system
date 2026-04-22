package com.example.scooterrentalsystem.mapper;

import com.example.scooterrentalsystem.dto.RoleResponseDto;
import com.example.scooterrentalsystem.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponseDto toDto(Role role);

    List<RoleResponseDto> toDtoList(List<Role> roles);
}
