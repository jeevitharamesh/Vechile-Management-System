package com.carService.model.dto;

import jakarta.persistence.Column;

public class InvoiceEdit {

    private int id;

    private Double price;

    private String fixedProblem;

    private String brand;

    private String model;

    private String registrationNumber;

    private Integer productionYear;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    public InvoiceEdit() {
    }

    public InvoiceEdit(int id, Double price, String fixedProblem, String brand, String model, String registrationNumber, Integer productionYear,
                       String firstName, String lastName, String email, String username) {
        this.id = id;
        this.price = price;
        this.fixedProblem = fixedProblem;
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.productionYear = productionYear;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
