package com.carService.service;

import com.carService.model.Vehicle;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface VehicleService {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    Vehicle addVehicle(Vehicle vehicle);

    Vehicle editVehicle(String brand, String model, String registrationNumber, int productionYear, int id);
}
