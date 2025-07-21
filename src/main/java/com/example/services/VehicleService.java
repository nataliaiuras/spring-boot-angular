package com.example.services;

import com.example.dtos.VehicleDto;
import com.example.models.Vehicle;
import com.example.exceptions.AppException;
import com.example.mapers.VehicleMapper;
import com.example.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private static final String NOT_FOUND = "Vehicle not found";

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public List<VehicleDto> allVehicles() {
        return vehicleMapper.toVehicleDtos(vehicleRepository.findAll());
    }

    public VehicleDto createVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleMapper.toVehicle(vehicleDto);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toVehicleDto(savedVehicle);
    }

    public VehicleDto updateVehicle(Long id, VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new AppException(NOT_FOUND, HttpStatus.NOT_FOUND));
        vehicleMapper.updateVehicle(vehicle, vehicleMapper.toVehicle(vehicleDto));
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toVehicleDto(savedVehicle);
    }

    public VehicleDto patchVehicle(Long id, VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new AppException(NOT_FOUND, HttpStatus.NOT_FOUND));

        if (vehicleDto.getBrand() != null) {
            vehicle.setBrand(vehicleDto.getBrand());
        }
        if (vehicleDto.getModel() != null) {
            vehicle.setModel(vehicleDto.getModel());
        }
        if (vehicleDto.getYearProd() != 0) {
            vehicle.setYearProd(vehicleDto.getYearProd());
        }
        if (vehicleDto.getColor() != null) {
            vehicle.setColor(vehicleDto.getColor());
        }

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toVehicleDto(savedVehicle);
    }

    public VehicleDto deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new AppException(NOT_FOUND, HttpStatus.NOT_FOUND));
        VehicleDto vehicleDto = vehicleMapper.toVehicleDto(vehicle);

        vehicleRepository.deleteById(id);

        return vehicleDto;
    }

    public VehicleDto getVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new AppException(NOT_FOUND, HttpStatus.NOT_FOUND));
        return vehicleMapper.toVehicleDto(vehicle);
    }
}