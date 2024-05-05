package com.carService.controller;

import com.carService.model.Appointment;
import com.carService.model.CarService;
import com.carService.model.User;
import com.carService.model.Vehicle;
import com.carService.model.dto.AppointmentRequest;
import com.carService.service.AppointmentServiceImpl;
import com.carService.service.CarServiceServiceImpl;
import com.carService.service.UserServiceImpl;
import com.carService.service.VehicleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class AppointmentController {

    CarServiceServiceImpl carServiceService;
    AppointmentServiceImpl appointmentService;
    UserServiceImpl userService;
    VehicleServiceImpl vehicleService;

    @Autowired
    public AppointmentController(CarServiceServiceImpl carServiceService, AppointmentServiceImpl appointmentService,
                                 UserServiceImpl userService, VehicleServiceImpl vehicleService) {
        this.carServiceService = carServiceService;
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/appointment")
    public String showAppointment(Model model, @RequestParam String id, @RequestParam Optional<String> error) {

        CarService carService = carServiceService.findById(Integer.parseInt(id)).orElse(null);

        if(error.isPresent()){
            model.addAttribute("dateError",true);
        }

        model.addAttribute("service",carService);

        return "appointment/appointment";
    }

    @PostMapping("/appointment")
    public String makeAppointment(@ModelAttribute AppointmentRequest appointmentRequest, @RequestParam String id) {
        if(!appointmentRequest.getDate().isAfter(LocalDate.now())){
            return "redirect:/appointment?id="+id+"&error=date";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<CarService> carService = carServiceService.findById(Integer.parseInt(id));
        if(carService.isPresent()){
            User user = userService.findUserByUsername(principal.getUsername());
            Vehicle vehicle = vehicleService.findByRegistrationNumber(appointmentRequest.getRegistrationNumber()).orElse(null);
            if(vehicle == null){
                vehicle = vehicleService.addVehicle(new Vehicle(appointmentRequest.getBrand(),appointmentRequest.getModel(),appointmentRequest.getRegistrationNumber(),appointmentRequest.getProductionYear()));
            }
            appointmentService.addAppointment(vehicle,user,carService.get(),appointmentRequest.getDate(),appointmentRequest.getHour());
        }

        return "redirect:/services";
    }

    @GetMapping("/myAppointments")
    public String showAppointments(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByUsername(principal.getUsername());

        model.addAttribute("appointments", appointmentService.findAppointmentsByUser(user));
        return "appointment/myAppointments";
    }

    @GetMapping("/cancelAppointment")
    public String cancelAppointment(@RequestParam String id){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByUsername(principal.getUsername());

        Appointment appointment = appointmentService.findAppointmentById(Integer.parseInt(id)).orElse(null);

        if(appointment!=null && appointment.getUser().getId() == user.getId()) appointmentService.deleteAppointment(appointment);

        return "redirect:/myAppointments";
    }

    @GetMapping("/deleteAppointment")
    public String deleteAppointment(@RequestParam String id){

        Appointment appointment = appointmentService.findAppointmentById(Integer.parseInt(id)).orElse(null);

        int serviceId = 0;
        if(appointment!=null){
            serviceId = appointment.getCarService().getId();
            appointmentService.deleteAppointment(appointment);
        }

        return "redirect:/serviceAppointments?id="+serviceId;
    }
}
