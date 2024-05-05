package com.carService;

import com.carService.model.Role;
import com.carService.repository.CarServiceRepository;
import com.carService.repository.InvoiceRepository;
import com.carService.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CarServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(RoleRepository roleRepository, CarServiceRepository carServiceRepository, InvoiceRepository invoiceRepository) {
		return (args) -> {
			if(roleRepository.findByName("ADMIN").isEmpty()) roleRepository.save(new Role("ADMIN"));
			if(roleRepository.findByName("USER").isEmpty()) roleRepository.save(new Role("USER"));
			if(roleRepository.findByName("EMPLOYEE").isEmpty()) roleRepository.save(new Role("EMPLOYEE"));
		};
	}

}
