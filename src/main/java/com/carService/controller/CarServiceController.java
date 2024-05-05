package com.carService.controller;

import com.carService.model.CarService;
import com.carService.model.User;
import com.carService.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CarServiceController {

    CarServiceServiceImpl carServiceService;
    AppointmentServiceImpl appointmentService;
    UserServiceImpl userService;
    VehicleServiceImpl vehicleService;
    InvoiceServiceImpl invoiceService;

    @Autowired
    public CarServiceController(CarServiceServiceImpl carServiceService, AppointmentServiceImpl appointmentService,
                                UserServiceImpl userService, VehicleServiceImpl vehicleService, InvoiceServiceImpl invoiceService) {
        this.carServiceService = carServiceService;
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/addService")
    public String showAddService() {

        return "service/addService";
    }

    @PostMapping("/addService")
    public String addService(CarService carService) {

        carServiceService.addService(carService);
        return "redirect:/services";
    }

    @GetMapping("/editService")
    public String showEditService(Model model, @RequestParam String id) {

        CarService carService = carServiceService.findById(Integer.parseInt(id)).orElse(null);

        if(carService!=null){
            model.addAttribute("service",carService);
        }

        return "service/editService";
    }

    @PostMapping("/editService")
    public String editService(CarService carService, @RequestParam String id) {

        carServiceService.editCarService(carService, Integer.parseInt(id));

        return "redirect:/services";
    }

    @GetMapping("/deleteService")
    public String deleteService(@RequestParam String id){

         CarService carService = carServiceService.findById(Integer.parseInt(id)).orElse(null);

        if(carService!=null){
            carServiceService.delete(carService);

        }

        return "redirect:/services";
    }

    @GetMapping("/service")
    public String shoService(Model model, @RequestParam String id) {

        CarService carService = carServiceService.findById(Integer.parseInt(id)).orElse(null);

        boolean employeeInService = false;

        if(carService != null){
            model.addAttribute("service",carService);

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(!principal.toString().equals("anonymousUser")){
                User user = userService.findUserByUsername(((UserDetails)principal).getUsername());
                employeeInService = carService.getEmployeeList().contains(user);
            }
        }

        model.addAttribute("employeeInService",employeeInService);

        return "service/servicePage";
    }

    @GetMapping("/services")
    public String showServices(Model model) {

        model.addAttribute("services", carServiceService.findAll());
        return "service/carServices";
    }

    @GetMapping("/getAvailableHours")
    public ResponseEntity<List<Integer>> getAvailableHours(@RequestParam("date") LocalDate date, @RequestParam("id") String id){
        CarService carService = carServiceService.findById(Integer.parseInt(id)).orElse(null);
        if(carService != null)
            return new ResponseEntity<List<Integer>>(appointmentService.getAvailableHours(date,carService),HttpStatus.OK);
        else
            return new ResponseEntity<List<Integer>>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/employees")
    public String showEmployees(Model model, @RequestParam String id){

        CarService carService = carServiceService.findById(Integer.parseInt(id)).orElse(null);

        if (carService != null){
            model.addAttribute("employees",carService.getEmployeeList());
            model.addAttribute("service_id",carService.getId());
        }

        return "employee/employees";
    }

}
