package com.example.scooterrentalsystem.controller;

import com.example.scooterrentalsystem.dto.RentalPointCreateDto;
import com.example.scooterrentalsystem.dto.RentalPointResponseDto;
import com.example.scooterrentalsystem.dto.RentalPointUpdateDto;
import com.example.scooterrentalsystem.dto.ScooterResponseDto;
import com.example.scooterrentalsystem.mapper.RentalPointMapper;
import com.example.scooterrentalsystem.mapper.ScooterMapper;
import com.example.scooterrentalsystem.service.RentalPointService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rental-points")
public class RentalPointController {

    private final RentalPointService rentalPointService;
    private final RentalPointMapper rentalPointMapper;
    private final ScooterMapper scooterMapper;

    public RentalPointController(
            RentalPointService rentalPointService,
            RentalPointMapper rentalPointMapper,
            ScooterMapper scooterMapper
    ) {
        this.rentalPointService = rentalPointService;
        this.rentalPointMapper = rentalPointMapper;
        this.scooterMapper = scooterMapper;
    }

    @GetMapping
    public List<RentalPointResponseDto> listAll() {
        return rentalPointMapper.toDtoList(rentalPointService.getAllPoints());
    }

    @GetMapping("/roots")
    public List<RentalPointResponseDto> roots() {
        return rentalPointMapper.toDtoList(rentalPointService.getRootPoints());
    }

    @GetMapping("/{id}")
    public RentalPointResponseDto getById(@PathVariable Long id) {
        return rentalPointMapper.toDto(rentalPointService.getById(id));
    }

    @GetMapping("/{parentId}/children")
    public List<RentalPointResponseDto> children(@PathVariable Long parentId) {
        return rentalPointMapper.toDtoList(rentalPointService.getSubPoints(parentId));
    }

    @GetMapping("/{pointId}/available-scooters")
    public List<ScooterResponseDto> availableScooters(@PathVariable Long pointId) {
        return scooterMapper.toDtoList(rentalPointService.getAvailableScootersAtPoint(pointId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalPointResponseDto create(@Valid @RequestBody RentalPointCreateDto dto) {
        return rentalPointMapper.toDto(
                rentalPointService.addRentalPoint(dto.name(), dto.latitude(), dto.longitude(), dto.parentId()));
    }

    @PutMapping("/{id}")
    public RentalPointResponseDto update(@PathVariable Long id, @Valid @RequestBody RentalPointUpdateDto dto) {
        rentalPointService.updateRentalPoint(id, dto.name(), dto.latitude(), dto.longitude(), dto.parentId());
        return rentalPointMapper.toDto(rentalPointService.getById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rentalPointService.deleteRentalPoint(id);
    }
}
