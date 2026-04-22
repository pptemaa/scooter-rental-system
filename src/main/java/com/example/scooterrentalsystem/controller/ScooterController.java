package com.example.scooterrentalsystem.controller;

import com.example.scooterrentalsystem.dto.ScooterCreateDto;
import com.example.scooterrentalsystem.dto.ScooterResponseDto;
import com.example.scooterrentalsystem.dto.ScooterUpdateDto;
import com.example.scooterrentalsystem.entity.Status;
import com.example.scooterrentalsystem.mapper.ScooterMapper;
import com.example.scooterrentalsystem.service.ScooterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scooters")
public class ScooterController {

    private final ScooterService scooterService;
    private final ScooterMapper scooterMapper;

    public ScooterController(ScooterService scooterService, ScooterMapper scooterMapper) {
        this.scooterService = scooterService;
        this.scooterMapper = scooterMapper;
    }

    @GetMapping
    public List<ScooterResponseDto> getAll() {
        return scooterMapper.toDtoList(scooterService.getAllScooters());
    }

    @GetMapping("/{id}")
    public ScooterResponseDto getById(@PathVariable Long id) {
        return scooterMapper.toDto(scooterService.getScooterById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScooterResponseDto create(@Valid @RequestBody ScooterCreateDto dto) {
        return scooterMapper.toDto(scooterService.addScooter(dto.modelId(), dto.rentalPointId()));
    }

    @PutMapping("/{id}")
    public ScooterResponseDto update(@PathVariable Long id, @RequestBody ScooterUpdateDto dto) {
        scooterService.updateScooter(id, dto);
        return scooterMapper.toDto(scooterService.getScooterById(id));
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Long id, @RequestParam Status status) {
        scooterService.updateScooterStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        scooterService.deleteScooter(id);
    }
}
