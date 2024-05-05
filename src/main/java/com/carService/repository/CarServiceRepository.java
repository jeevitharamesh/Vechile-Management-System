package com.carService.repository;

import com.carService.model.CarService;
import com.carService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CarServiceRepository extends JpaRepository<CarService,Integer> {

    Optional<CarService> findByEmployeeListContains(User user);
}
