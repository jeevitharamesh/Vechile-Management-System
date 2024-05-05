package com.carService.repository;

import com.carService.model.Appointment;
import com.carService.model.CarService;
import com.carService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {

    Appointment findByCarServiceAndDateAndHour(CarService carService, LocalDate date, int hour);

    List<Appointment> findByUser(User user);

    List<Appointment> findByCarService(CarService carService);
}
