package com.example.scooterrentalsystem.controller;

import com.example.scooterrentalsystem.dto.TariffCreateDto;
import com.example.scooterrentalsystem.dto.TariffResponseDto;
import com.example.scooterrentalsystem.entity.Tariff;
import com.example.scooterrentalsystem.mapper.TariffMapper;
import com.example.scooterrentalsystem.service.TariffService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;
    private final TariffMapper tariffMapper;

    public TariffController(TariffService tariffService, TariffMapper tariffMapper) {
        this.tariffService = tariffService;
        this.tariffMapper = tariffMapper;
    }

    @GetMapping
    public List<TariffResponseDto> getAllTariffs() {
        return tariffMapper.toDtoList(tariffService.getAllTariffs());
    }

    @GetMapping("/{id}")
    public TariffResponseDto getTariffById(@PathVariable Long id) {
        return tariffMapper.toDto(tariffService.getTariffById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TariffResponseDto createTariff(@Valid @RequestBody TariffCreateDto createDto) {
        Tariff tariff = tariffMapper.toEntity(createDto);
        Tariff savedTariff = tariffService.createTariff(tariff);
        return tariffMapper.toDto(savedTariff);
    }

    @PutMapping("/{id}")
    public TariffResponseDto updateTariff(@PathVariable Long id, @Valid @RequestBody TariffCreateDto dto) {
        Tariff patch = tariffMapper.toEntity(dto);
        return tariffMapper.toDto(tariffService.updateTariff(id, patch));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTariff(@PathVariable Long id) {
        tariffService.deleteTariff(id);
    }
}
