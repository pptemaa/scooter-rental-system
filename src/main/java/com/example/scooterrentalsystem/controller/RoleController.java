package com.example.scooterrentalsystem.controller;

import com.example.scooterrentalsystem.dto.RoleCreateDto;
import com.example.scooterrentalsystem.dto.RoleResponseDto;
import com.example.scooterrentalsystem.mapper.RoleMapper;
import com.example.scooterrentalsystem.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    public RoleController(RoleService roleService, RoleMapper roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    @GetMapping
    public List<RoleResponseDto> list() {
        return roleMapper.toDtoList(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public RoleResponseDto getById(@PathVariable Long id) {
        return roleMapper.toDto(roleService.getRoleById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponseDto create(@Valid @RequestBody RoleCreateDto dto) {
        return roleMapper.toDto(roleService.createRole(dto.name()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        roleService.deleteRole(id);
    }
}
