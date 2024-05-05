package com.carService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Qualification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    boolean chassis;

    boolean engine;

    boolean breakSystem;

    boolean changingConsumables;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isChassis() {
        return chassis;
    }

    public void setChassis(boolean chassis) {
        this.chassis = chassis;
    }

    public boolean isEngine() {
        return engine;
    }

    public void setEngine(boolean engine) {
        this.engine = engine;
    }

    public boolean isBreakSystem() {
        return breakSystem;
    }

    public void setBreakSystem(boolean breakSystem) {
        this.breakSystem = breakSystem;
    }

    public boolean isChangingConsumables() {
        return changingConsumables;
    }

    public void setChangingConsumables(boolean changingConsumables) {
        this.changingConsumables = changingConsumables;
    }

    public Qualification() {
    }

    public Qualification(boolean chassis, boolean engine, boolean breakSystem, boolean changingConsumables) {
        this.chassis = chassis;
        this.engine = engine;
        this.breakSystem = breakSystem;
        this.changingConsumables = changingConsumables;
    }
}
