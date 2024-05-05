package com.carService.service;

import com.carService.model.Appointment;
import com.carService.model.CarService;
import com.carService.model.User;
import com.carService.model.Vehicle;
import com.carService.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {


    AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Integer> getAvailableHours(LocalDate date, CarService carService){
        int startHour = 8;
        int endHour = 18;
        List<Integer> result = new ArrayList<>();
        for (int i = startHour; i <= endHour; i++) {
            if(checkIfHourIsFree(date, carService, i)){
                result.add(i);
            }

        }
        return result;
    }

    private boolean checkIfHourIsFree(LocalDate date, CarService carService, int i) {
        return appointmentRepository.findByCarServiceAndDateAndHour(carService, date, i) == null;
    }

    @Transactional
    public Appointment addAppointment(Vehicle vehicle, User user, CarService carService, LocalDate date, int hour){
        if(checkIfHourIsFree(date, carService, hour)){
            Appointment appointment = new Appointment(vehicle,user,carService,date,hour);
            return appointmentRepository.save(appointment);
        }
        return null;
    }

    public List<Appointment> findAppointmentsByUser(User user)
    {
        return appointmentRepository.findByUser(user);
    }

    public List<Appointment> findAppointmentsByCarService(CarService carService)
    {
        return appointmentRepository.findByCarService(carService);
    }

    @Transactional
    public void deleteAppointment(Appointment appointment){
        appointmentRepository.delete(appointment);
    }

    public Optional<Appointment> findAppointmentById(Integer id){
        return appointmentRepository.findById(id);
    }
}
