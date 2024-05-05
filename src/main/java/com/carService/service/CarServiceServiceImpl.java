package com.carService.service;

import com.carService.model.CarService;
import com.carService.repository.CarServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class CarServiceServiceImpl implements CarServiceService{


    CarServiceRepository carServiceRepository;

    @Autowired
    public CarServiceServiceImpl(CarServiceRepository carServiceRepository) {
        this.carServiceRepository = carServiceRepository;
    }

    @Transactional
    public void addService(CarService carService) {
        carServiceRepository.save(carService);
    }

    @Transactional
    public CarService editCarService(CarService carServiceDTO, int id) {

        CarService carService = carServiceRepository.findById(id).orElse(null);

        if(carService != null){
            if (carServiceDTO.getName() != null && !carServiceDTO.getName().equals(carService.getName())) {
                carService.setName(carServiceDTO.getName());
            }

            if (carServiceDTO.getAddress() != null && !carServiceDTO.getAddress().equals(carService.getAddress())) {
                carService.setAddress(carServiceDTO.getAddress());
            }

            return carServiceRepository.save(carService);
        }

        return null;

    }

    public Optional<CarService> findById(int id){
        return carServiceRepository.findById(id);
    }

    @Transactional
    public void delete(CarService carService) {
        carServiceRepository.delete(carService);
    }

    public List<CarService> findAll() {
        return carServiceRepository.findAll();
    }
}
