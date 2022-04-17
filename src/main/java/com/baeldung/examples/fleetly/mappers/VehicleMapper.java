package com.baeldung.examples.fleetly.mappers;

import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.baeldung.examples.fleetly.dto.VehicleDto;
import com.baeldung.examples.fleetly.entities.Vehicle;

@Component
public class VehicleMapper {

    private final ModelMapper modelMapper;

    public VehicleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public VehicleDto toDTO(Vehicle vehicle) {
        return Objects.isNull(vehicle) ? null : modelMapper.map(vehicle, VehicleDto.class);
    }

    public Vehicle toEntity(VehicleDto vehicleDto) {
        return Objects.isNull(vehicleDto) ? null : modelMapper.map(vehicleDto, Vehicle.class);
    }
}
