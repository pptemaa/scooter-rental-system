package com.example.scooterrentalsystem.controller;

import com.example.scooterrentalsystem.dto.UserCreateDto;
import com.example.scooterrentalsystem.dto.UserResponseDto;
import com.example.scooterrentalsystem.dto.UserUpdateDto;
import com.example.scooterrentalsystem.mapper.UserMapper;
import com.example.scooterrentalsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Valid @RequestBody UserCreateDto dto) {
        return userMapper.toDto(
                userService.registerUser(
                        dto.email(),
                        dto.password(),
                        dto.firstName(),
                        dto.lastName(),
                        dto.roleName(),
                        dto.balance()
                ));
    }

    @PatchMapping("/{id}/balance")
    public void topUp(@PathVariable Long id, @RequestParam BigDecimal amount) {
        userService.topUpBalance(id, amount);
    }

    @GetMapping
    public List<UserResponseDto> list() {
        return userMapper.toDtoList(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable Long id) {
        return userMapper.toDto(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        return userMapper.toDto(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
