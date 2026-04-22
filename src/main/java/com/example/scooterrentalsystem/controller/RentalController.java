package com.example.scooterrentalsystem.controller;

import com.example.scooterrentalsystem.dto.RentalFinishDto;
import com.example.scooterrentalsystem.dto.RentalResponseDto;
import com.example.scooterrentalsystem.dto.RentalStartDto;
import com.example.scooterrentalsystem.mapper.RentalMapper;
import com.example.scooterrentalsystem.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    public RentalController(RentalService rentalService, RentalMapper rentalMapper) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
    }

    @GetMapping
    public List<RentalResponseDto> listAll() {
        return rentalMapper.toDtoList(rentalService.getAllRentals());
    }

    @GetMapping("/{id}")
    public RentalResponseDto getById(@PathVariable Long id) {
        return rentalMapper.toDto(rentalService.getRentalById(id));
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public RentalResponseDto start(@Valid @RequestBody RentalStartDto dto) {
        return rentalMapper.toDto(
                rentalService.startRental(dto.userId(), dto.scooterId(), dto.tariffId()));
    }

    @PostMapping("/finish")
    public RentalResponseDto finish(@Valid @RequestBody RentalFinishDto dto) {
        return rentalMapper.toDto(
                rentalService.finishRental(dto.userId(), dto.endPointId(), dto.distanceKm()));
    }

    @GetMapping("/history/user/{userId}")
    public List<RentalResponseDto> userHistory(@PathVariable Long userId) {
        return rentalMapper.toDtoList(rentalService.getUserHistory(userId));
    }

    @GetMapping("/history/scooter/{scooterId}")
    public List<RentalResponseDto> scooterHistory(@PathVariable Long scooterId) {
        return rentalMapper.toDtoList(rentalService.getScooterHistory(scooterId));
    }
}
