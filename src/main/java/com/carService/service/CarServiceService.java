package com.carService.service;

import com.carService.model.CarService;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface CarServiceService {

    void addService(CarService carService);

    CarService editCarService(CarService carServiceDTO, int id);

    Optional<CarService> findById(int id);

    void delete(CarService carService);

    List<CarService> findAll();
}
