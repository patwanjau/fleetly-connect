package com.baeldung.examples.fleetly.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.examples.fleetly.dto.Fault;
import com.baeldung.examples.fleetly.dto.VehicleDto;
import com.baeldung.examples.fleetly.entities.Vehicle;
import com.baeldung.examples.fleetly.mappers.VehicleMapper;
import com.baeldung.examples.fleetly.services.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/cars")
public class VehicleController {
    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;

    public VehicleController(VehicleService vehicleService, VehicleMapper vehicleMapper) {
        this.vehicleService = vehicleService;
        this.vehicleMapper = vehicleMapper;
    }

    @Operation(summary = "Get registered car by chassis number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found registered car",
            content = {@Content(schema = @Schema(implementation = VehicleDto.class),
                mediaType = MediaType.APPLICATION_JSON_VALUE)}),
        @ApiResponse(responseCode = "404", description = "Vehicle not found", content = @Content)
    })
    @GetMapping(value = "/{chassisNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable("chassisNumber") String chassisNumber) {
        Optional<VehicleDto> vehicle = vehicleService.getVehicle(chassisNumber).map(vehicleMapper::toDTO);
        return vehicle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all registered cars")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found registered cars",
            content = {@Content(schema = @Schema(anyOf = VehicleDto.class),
                mediaType = MediaType.APPLICATION_JSON_VALUE)}),
        @ApiResponse(responseCode = "404", description = "No vehicles found", content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VehicleDto>> getVehicles() {
        List<VehicleDto> vehicles = vehicleService.getVehicles()
            .stream().map(vehicleMapper::toDTO).collect(Collectors.toList());
        return vehicles.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(vehicles);
    }

    @Operation(summary = "Update details of an existing car, overwriting all existing details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "An existing car was found and modified with provided details",
            content = {@Content(schema = @Schema(implementation = VehicleDto.class),
                mediaType = MediaType.APPLICATION_JSON_VALUE)}),
        @ApiResponse(responseCode = "404",
            description = "A vehicle with provided Chassis number was not found", content = @Content)
    })
    @PutMapping(value = "/{chassisNumber}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VehicleDto> modifyVehicle(@PathVariable("chassisNumber") String chassisNumber,
                                                    @Valid VehicleDto vehicle) {
        Optional<Vehicle> existingVehicle = vehicleService.getVehicle(chassisNumber);
        if (existingVehicle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Vehicle updatedVehicle = vehicleMapper.toEntity(vehicle);
        updatedVehicle.setId(existingVehicle.get().getId());
        updatedVehicle = vehicleService.manageVehicle(updatedVehicle);
        return ResponseEntity.accepted().body(vehicleMapper.toDTO(updatedVehicle));
    }

    @Operation(summary = "Selectively modify some details of the car.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "404",
            description = "A car with provided chassis number was not found", content = @Content),
        @ApiResponse(responseCode = "202", description = "An existing car was found and provided details modified",
            content = {@Content(schema = @Schema(implementation = VehicleDto.class),
                mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PatchMapping("/{chassisNumber}")
    public ResponseEntity<VehicleDto> changeVehicleDetail(@PathVariable("chassisNumber") String chassisNumber,
                                                          VehicleDto vehicle) {
        Optional<Vehicle> existingVehicle = vehicleService.getVehicle(chassisNumber);
        if (existingVehicle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Vehicle updatableVehicle = existingVehicle.get();
        if (vehicle != null) {
            if (vehicle.getChassisNumber() != null && !vehicle.getChassisNumber().isBlank()) {
                updatableVehicle.setChassisNumber(vehicle.getChassisNumber().trim());
            }
            if (vehicle.getMake() != null && !vehicle.getMake().isBlank()) {
                updatableVehicle.setMake(vehicle.getMake().trim());
            }
            if (vehicle.getModel() != null) {
                updatableVehicle.setModel(vehicle.getModel() == null || vehicle.getModel().isBlank()
                    ? null : vehicle.getModel().trim());
            }
        }
        throw new RuntimeException("Pending complete implementation");
    }

    @Operation(summary = "Register a car in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400",
            description = "The car could not be registered with provided details", content = @Content),
        @ApiResponse(responseCode = "202", description = "A car was registered successfully with details provided.")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerVehicle(@RequestBody @Valid VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        Vehicle result = vehicleService.manageVehicle(vehicle);
        if (result.getId() == null) {
            Fault fault = new Fault("Vehicle registration was not successful");
            return ResponseEntity.badRequest().body(fault);
        }
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Delete a registered car by provided Chassis Number")
    @ApiResponses(value = {
        @ApiResponse(content = @Content, responseCode = "204",
            description = "If an existing car was found by the provided Chassis number, it was removed." +
                " If none was found, no action was performed.")
    })
    @DeleteMapping("/{chassisNumber}")
    public ResponseEntity<VehicleDto> removeVehicle(@PathVariable("chassisNumber") String chassisNumber) {
        vehicleService.deleteVehicle(chassisNumber);
        return ResponseEntity.noContent().build();
    }
}
