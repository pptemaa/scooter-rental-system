package com.example.scooterrentalsystem.controller;

import com.example.scooterrentalsystem.dto.UserCreateDto;
import com.example.scooterrentalsystem.dto.UserResponseDto;
import com.example.scooterrentalsystem.entity.User;
import com.example.scooterrentalsystem.mapper.UserMapper;
import com.example.scooterrentalsystem.service.JwtService;
import com.example.scooterrentalsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService, UserMapper userMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        try {
            User user = userService.findByEmail(email);

            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный пароль");
            }

            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не найден");
        }
    }
}
