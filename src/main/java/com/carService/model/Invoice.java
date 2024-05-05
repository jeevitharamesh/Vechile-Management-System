package com.carService.model;

import jakarta.persistence.*;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private CarService carService;

    @ManyToOne
    private User client;

    @ManyToOne
    private Vehicle vehicle;

    private Double price;

    private String fixedProblem;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getFixedProblem() {
        return fixedProblem;
    }

    public void setFixedProblem(String fixedProblem) {
        this.fixedProblem = fixedProblem;
    }

    public CarService getCarService() {
        return carService;
    }

    public void setCarService(CarService carService) {
        this.carService = carService;
    }

    public Invoice() {
    }

    public Invoice(CarService carService, User client, Vehicle vehicle, Double price, String fixedProblem) {
        this.carService = carService;
        this.client = client;
        this.vehicle = vehicle;
        this.price = price;
        this.fixedProblem = fixedProblem;
    }
}
