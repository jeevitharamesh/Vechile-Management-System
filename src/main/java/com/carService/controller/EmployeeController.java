package com.carService.controller;

import com.carService.model.*;
import com.carService.model.dto.InvoiceEdit;
import com.carService.model.dto.StatisticsObject;
import com.carService.repository.QualificationRepository;
import com.carService.service.AppointmentServiceImpl;
import com.carService.service.CarServiceServiceImpl;
import com.carService.service.InvoiceServiceImpl;
import com.carService.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {

    CarServiceServiceImpl carServiceService;
    AppointmentServiceImpl appointmentService;
    UserServiceImpl userService;
    InvoiceServiceImpl invoiceService;
    QualificationRepository qualificationRepository;

    @Autowired
    public EmployeeController(CarServiceServiceImpl carServiceService, AppointmentServiceImpl appointmentService, UserServiceImpl userService, InvoiceServiceImpl invoiceService, QualificationRepository qualificationRepository) {
        this.carServiceService = carServiceService;
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.qualificationRepository = qualificationRepository;
    }

    @GetMapping("/registerEmployee")
    public String showRegister() {

        return "employee/registerEmployee";
    }

    @PostMapping("/registerEmployee")
    public String register(@ModelAttribute User user, Model model, @RequestParam(value = "chassis", required = false) boolean chassis,
                           @RequestParam(value = "engine", required = false) boolean engine,
                           @RequestParam(value = "breakSystem", required = false) boolean breakSystem,
                           @RequestParam(value = "changingConsumables", required = false) boolean changingConsumables) {

        Qualification qualification = qualificationRepository.save(new Qualification(chassis,engine,breakSystem,changingConsumables));
        user.setQualification(qualification);

        User userToRegister = userService.registerUser(user,true);
        if (userToRegister == null)
        {
            model.addAttribute("error",true);
            return "/register";
        }
        return "redirect:/login";
    }

    @GetMapping("/addEmployee")
    public String showAddEmployee(Model model, @RequestParam String id) {

        model.addAttribute("unemployedWorkers",userService.findAllUnemployedWorkers());
        model.addAttribute("service_id",id);
        return "employee/addEmployee";
    }

    @GetMapping("/addEmployeeToService")
    public String addEmployee(@RequestParam("service_id") String service_id, @RequestParam("user_id") String user_id) {

        CarService carService = carServiceService.findById(Integer.parseInt(service_id)).orElse(null);
        User user = userService.findUserById(Integer.parseInt(user_id)).orElse(null);

        if(carService != null && user != null){
            carService.getEmployeeList().add(user);
            carServiceService.addService(carService);
        }

        return "redirect:/services";
    }

    @GetMapping("/serviceAppointments")
    public String showAppointment(Model model, @RequestParam String id) {

        CarService carService = carServiceService.findById(Integer.parseInt(id)).orElse(null);
        List<Appointment> appointments = null;


        if(carService != null){
            appointments = appointmentService.findAppointmentsByCarService(carService);

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(!principal.toString().equals("anonymousUser")){
                User user = userService.findUserByUsername(((UserDetails)principal).getUsername());
                if(!carService.getEmployeeList().contains(user) && !isAdmin(user)) return "redirect:/services";
            }
        }

        model.addAttribute("appointments",appointments);
        model.addAttribute("service",carService);

        return "appointment/serviceAppointments";
    }

    @GetMapping("/serviceInvoices")
    public String showInvoices(Model model, @RequestParam String id) {

        CarService carService = carServiceService.findById(Integer.parseInt(id)).orElse(null);
        List<Invoice> invoices = null;

        if(carService != null){
            invoices = invoiceService.findInvoicesByCarService(carService);

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(!principal.toString().equals("anonymousUser")){
                User user = userService.findUserByUsername(((UserDetails)principal).getUsername());
                if(!carService.getEmployeeList().contains(user) &&
                        !isAdmin(user)) return "redirect:/services";

            }
        }

        model.addAttribute("invoices",invoices);

        return "invoice/serviceInvoices";
    }

    @GetMapping("/createInvoice")
    public String showCreateInvoice(Model model, @RequestParam String id) {

        Appointment appointment = appointmentService.findAppointmentById(Integer.parseInt(id)).orElse(null);

        model.addAttribute("appointment",appointment);

        return "invoice/createInvoice";
    }

    @PostMapping("/createInvoice")
    public String createInvoice(@RequestParam String id,@RequestParam("price") String price, @RequestParam("fixedProblem") String fixedProblem) {
        Appointment appointment = appointmentService.findAppointmentById(Integer.parseInt(id)).orElse(null);

        User user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!principal.toString().equals("anonymousUser")){
            user = userService.findUserByUsername(((UserDetails)principal).getUsername());
        }
        Invoice invoice = null;
        if(appointment != null){
            invoice = new Invoice(appointment.getCarService(), user, appointment.getVehicle(), Double.parseDouble(price), fixedProblem);
            invoiceService.addInvoice(invoice);
            appointmentService.deleteAppointment(appointment);
        }

        return "redirect:/services";
    }

    @GetMapping("/editInvoice")
    public String showEditInvoice(Model model, @RequestParam String id) {

        Optional<Invoice> optionalInvoice = invoiceService.findById(Integer.parseInt(id));

        if(optionalInvoice.isPresent()){
            Invoice invoice = optionalInvoice.get();
            InvoiceEdit invoiceEdit = invoiceService.makeInvoiceEdit(invoice);
            model.addAttribute("invoice",invoiceEdit);
        }

        return "invoice/editInvoice";
    }
    @PostMapping("/editInvoice")
    public String EditInvoice(@ModelAttribute InvoiceEdit invoiceEdit, @RequestParam String id) {

        invoiceService.editInvoice(invoiceEdit, Integer.parseInt(id));

        return "redirect:/services";
    }

    @GetMapping("/deleteInvoice")
    public String deleteInvoice(Model model, @RequestParam String id) {

        Optional<Invoice> invoice = invoiceService.findById(Integer.parseInt(id));

        if(invoice.isPresent()) invoiceService.delete(invoice.get());

        return "redirect:/services";
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model, @RequestParam String id) {


        List<Object[]> listByProductionYear = invoiceService.getInvoicesByProductionYear(Integer.parseInt(id));

        List<Object[]> listByBrand = invoiceService.getInvoicesByBrand(Integer.parseInt(id));

        List<StatisticsObject> invoicesByProductionYear = new ArrayList<>();

        List<StatisticsObject> invoicesByBrand = new ArrayList<>();

        for(Object[] el : listByProductionYear){
            invoicesByProductionYear.add(new StatisticsObject(el[0].toString(),el[1].toString()));
        }

        for(Object[] el : listByBrand){
            invoicesByBrand.add(new StatisticsObject(el[0].toString(),el[1].toString()));
        }

        model.addAttribute("invoicesByProductionYear",invoicesByProductionYear);

        model.addAttribute("invoicesByBrand",invoicesByBrand);

        return "statistics";
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
    }
}
