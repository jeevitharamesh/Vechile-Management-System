package com.carService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Vehicle vehicle;

    @ManyToOne
    private User user;

    @ManyToOne
    private CarService carService;

    private LocalDate date;

    @Range(min = 8, max = 18)
    private int hour;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CarService getCarService() {
        return carService;
    }

    public void setCarService(CarService carService) {
        this.carService = carService;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Appointment() {
    }

    public Appointment(Vehicle vehicle, User user, CarService carService, LocalDate date, @Size(min = 8, max = 18) int hour) {
        this.vehicle = vehicle;
        this.user = user;
        this.carService = carService;
        this.date = date;
        this.hour = hour;
    }
}
