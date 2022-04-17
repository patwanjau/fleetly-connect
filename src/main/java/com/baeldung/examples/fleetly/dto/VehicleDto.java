package com.baeldung.examples.fleetly.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VehicleDto implements Serializable {
    @NotBlank
    @NotNull
    private String make;
    private String model;
    private Integer yearOfManufacture;
    @NotNull
    @NotBlank
    private String chassisNumber;
    private String regNumber;

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public Integer getYearOfManufacture() {
        return yearOfManufacture;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYearOfManufacture(Integer yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleDto entity = (VehicleDto) o;
        return Objects.equals(this.make, entity.make) &&
            Objects.equals(this.model, entity.model) &&
            Objects.equals(this.yearOfManufacture, entity.yearOfManufacture) &&
            Objects.equals(this.chassisNumber, entity.chassisNumber) &&
            Objects.equals(this.regNumber, entity.regNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(make, model, yearOfManufacture, chassisNumber, regNumber);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "make = " + make + ", " +
            "model = " + model + ", " +
            "yearOfManufacture = " + yearOfManufacture + ", " +
            "chassisNumber = " + chassisNumber + ", " +
            "regNumber = " + regNumber + ")";
    }
}
