package com.example.mapers;

import com.example.dtos.VehicleDto;
import com.example.models.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle toVehicle(VehicleDto vehicleDto);

    VehicleDto toVehicleDto(Vehicle vehicle);

    List<VehicleDto> toVehicleDtos(List<Vehicle> vehicles);

    void updateVehicle(@MappingTarget Vehicle target, Vehicle source);
}