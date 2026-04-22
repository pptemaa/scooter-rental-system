package com.example.scooterrentalsystem.controller;

import com.example.scooterrentalsystem.dto.ScooterModelCreateDto;
import com.example.scooterrentalsystem.dto.ScooterModelResponseDto;
import com.example.scooterrentalsystem.mapper.ScooterModelMapper;
import com.example.scooterrentalsystem.service.ScooterModelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scooter-models")
public class ScooterModelController {

    private final ScooterModelService scooterModelService;
    private final ScooterModelMapper scooterModelMapper;

    public ScooterModelController(ScooterModelService scooterModelService, ScooterModelMapper scooterModelMapper) {
        this.scooterModelService = scooterModelService;
        this.scooterModelMapper = scooterModelMapper;
    }

    @GetMapping
    public List<ScooterModelResponseDto> list() {
        return scooterModelMapper.toDtoList(scooterModelService.getAllModels());
    }

    @GetMapping("/{id}")
    public ScooterModelResponseDto getById(@PathVariable Long id) {
        return scooterModelMapper.toDto(scooterModelService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScooterModelResponseDto create(@Valid @RequestBody ScooterModelCreateDto dto) {
        return scooterModelMapper.toDto(scooterModelService.create(scooterModelMapper.toEntity(dto)));
    }

    @PutMapping("/{id}")
    public ScooterModelResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody ScooterModelCreateDto dto
    ) {
        return scooterModelMapper.toDto(scooterModelService.update(id, dto.name(), dto.maxWeight()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        scooterModelService.delete(id);
    }
}
