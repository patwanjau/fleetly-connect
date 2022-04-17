package com.baeldung.examples.fleetly.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.baeldung.examples.fleetly.entities.Vehicle;
import com.baeldung.examples.fleetly.repositories.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getVehicles() {
        return StreamSupport
            .stream(vehicleRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    public Optional<Vehicle> getVehicle(String chassisNumber) {
        return vehicleRepository.findByChassisNumber(chassisNumber);
    }

    @Transactional
    public Vehicle manageVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public void deleteVehicle(String chassisNumber) {
        vehicleRepository.deleteByChassisNumber(chassisNumber);
    }
}
