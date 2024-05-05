package com.carService.service;

import com.carService.model.Vehicle;
import com.carService.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService{


    private VehicleRepository vehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Optional<Vehicle> findByRegistrationNumber(String registrationNumber){
        return vehicleRepository.findByRegistrationNumber(registrationNumber);
    }

    @Transactional
    public Vehicle addVehicle(Vehicle vehicle){
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle editVehicle(String brand, String model, String registrationNumber, int productionYear, int id) {

        Vehicle vehicle = vehicleRepository.findById(id).orElse(null);

        if(vehicle != null){
            if (!brand.equals("") && !vehicle.getBrand().equals(brand)) {
                vehicle.setBrand(brand);
            }

            if (!model.equals("") && !vehicle.getModel().equals(model)) {
                vehicle.setModel(model);
            }

            if (!registrationNumber.equals("") && !vehicle.getRegistrationNumber().equals(registrationNumber)) {
                vehicle.setRegistrationNumber(registrationNumber);
            }

            if (productionYear != 0 && !vehicle.getProductionYear().equals(productionYear)) {
                vehicle.setProductionYear(productionYear);
            }

            return vehicleRepository.save(vehicle);
        }

        return null;

    }
}
