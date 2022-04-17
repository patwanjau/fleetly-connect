package com.baeldung.examples.fleetly.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.baeldung.examples.fleetly.entities.Vehicle;

public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {
    void deleteByChassisNumber(String chassisNumber);

    Optional<Vehicle> findByChassisNumber(String chassisNumber);
}
