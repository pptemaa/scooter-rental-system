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
import java.util.Map;
import java.util.stream.Collectors;

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
        return rentalPointService.getAllPoints().stream()
                .map(p -> new RentalPointResponseDto(p.getId(), p.getName(), p.getLatitude(), p.getLongitude(), p.getParent() != null ? p.getParent().getId() : null, 0, Map.of(), Map.of()))
                .toList();
    }

    @GetMapping("/roots")
    public List<RentalPointResponseDto> roots() {
        return rentalPointService.getRootPoints().stream()
                .map(p -> new RentalPointResponseDto(p.getId(), p.getName(), p.getLatitude(), p.getLongitude(), p.getParent() != null ? p.getParent().getId() : null, 0, Map.of(), Map.of()))
                .toList();
    }

    @GetMapping("/{id}")
    public RentalPointResponseDto getById(@PathVariable Long id) {
        com.example.scooterrentalsystem.entity.RentalPoint point = rentalPointService.getById(id);
        List<com.example.scooterrentalsystem.entity.Scooter> scooters = rentalPointService.getScootersAtPoint(id);

        long total = scooters.size();
        Map<String, Long> modelCount = scooters.stream()
                .collect(Collectors.groupingBy(s -> s.getScooterModel().getName(), Collectors.counting()));
        Map<String, Long> statusCount = scooters.stream()
                .collect(Collectors.groupingBy(s -> s.getStatus().name(), Collectors.counting()));

        return new RentalPointResponseDto(
                point.getId(),
                point.getName(),
                point.getLatitude(),
                point.getLongitude(),
                point.getParent() != null ? point.getParent().getId() : null,
                total,
                modelCount,
                statusCount
        );
    }

    @GetMapping("/{parentId}/children")
    public List<RentalPointResponseDto> children(@PathVariable Long parentId) {
        return rentalPointService.getSubPoints(parentId).stream()
                .map(p -> new RentalPointResponseDto(p.getId(), p.getName(), p.getLatitude(), p.getLongitude(), p.getParent() != null ? p.getParent().getId() : null, 0, Map.of(), Map.of()))
                .toList();
    }

    @GetMapping("/{pointId}/available-scooters")
    public List<ScooterResponseDto> availableScooters(@PathVariable Long pointId) {
        return scooterMapper.toDtoList(rentalPointService.getAvailableScootersAtPoint(pointId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalPointResponseDto create(@Valid @RequestBody RentalPointCreateDto dto) {
        com.example.scooterrentalsystem.entity.RentalPoint p = 
                rentalPointService.addRentalPoint(dto.name(), dto.latitude(), dto.longitude(), dto.parentId());
        return new RentalPointResponseDto(p.getId(), p.getName(), p.getLatitude(), p.getLongitude(), p.getParent() != null ? p.getParent().getId() : null, 0, Map.of(), Map.of());
    }

    @PutMapping("/{id}")
    public RentalPointResponseDto update(@PathVariable Long id, @Valid @RequestBody RentalPointUpdateDto dto) {
        rentalPointService.updateRentalPoint(id, dto.name(), dto.latitude(), dto.longitude(), dto.parentId());
        com.example.scooterrentalsystem.entity.RentalPoint p = rentalPointService.getById(id);
        return new RentalPointResponseDto(p.getId(), p.getName(), p.getLatitude(), p.getLongitude(), p.getParent() != null ? p.getParent().getId() : null, 0, Map.of(), Map.of());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rentalPointService.deleteRentalPoint(id);
    }
}
