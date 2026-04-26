package com.example.scooterrentalsystem.controller;

import com.example.scooterrentalsystem.config.JwtFilter;
import com.example.scooterrentalsystem.config.SecurityConfig;
import com.example.scooterrentalsystem.dto.UserResponseDto;
import com.example.scooterrentalsystem.entity.User;
import com.example.scooterrentalsystem.mapper.UserMapper;
import com.example.scooterrentalsystem.service.JwtService;
import com.example.scooterrentalsystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false) // Отключаем фильтры для стабильности маппинга
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "MANAGER")
    @DisplayName("GET /api/users should return list of users")
    void getAllUsers_Success() throws Exception {
        UserResponseDto dto = new UserResponseDto(1L, "test@ex.com", "First", "Last", "ROLE_USER", BigDecimal.ZERO);

        when(userService.getAllUsers()).thenReturn(List.of(new User()));
        when(userMapper.toDtoList(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@ex.com"));
    }

    @Test
    @WithMockUser(authorities = "MANAGER")
    @DisplayName("GET /api/users/{id} should return user by id")
    void getUserById_Success() throws Exception {
        Long userId = 1L;
        UserResponseDto dto = new UserResponseDto(userId, "test@ex.com", "First", "Last", "ROLE_USER", BigDecimal.ZERO);

        when(userService.getUserById(eq(userId))).thenReturn(new User());
        when(userMapper.toDto(any())).thenReturn(dto);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    @WithMockUser(authorities = "MANAGER")
    @DisplayName("PATCH /api/users/{id}/balance should top up balance")
    void topUpBalance_Success() throws Exception {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("50");

        mockMvc.perform(patch("/api/users/{id}/balance", userId)
                        .param("amount", "50")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(userService).topUpBalance(eq(userId), argThat(a -> a.compareTo(amount) == 0));
    }

    @Test
    @WithMockUser(authorities = "MANAGER")
    @DisplayName("DELETE /api/users/{id} should return 204")
    void deleteUser_Success() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/api/users/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(userId);
    }
}
