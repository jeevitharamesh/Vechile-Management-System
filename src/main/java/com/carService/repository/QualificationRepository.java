package com.carService.repository;

import com.carService.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualificationRepository extends JpaRepository<Qualification,Integer> {
}
