package com.example.scooterrentalsystem.mapper;

import com.example.scooterrentalsystem.dto.UserResponseDto;
import com.example.scooterrentalsystem.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roleName", source = "role.name")
    UserResponseDto toDto(User user);

    List<UserResponseDto> toDtoList(List<User> users);
}
