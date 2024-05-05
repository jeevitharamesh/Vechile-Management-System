package com.carService.service;

import com.carService.model.Appointment;
import com.carService.model.CarService;
import com.carService.model.User;
import com.carService.model.Vehicle;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    List<Integer> getAvailableHours(LocalDate date, CarService carService);

    Appointment addAppointment(Vehicle vehicle, User user, CarService carService, LocalDate date, int hour);

    List<Appointment> findAppointmentsByUser(User user);

    List<Appointment> findAppointmentsByCarService(CarService carService);

    void deleteAppointment(Appointment appointment);

    Optional<Appointment> findAppointmentById(Integer id);

}
