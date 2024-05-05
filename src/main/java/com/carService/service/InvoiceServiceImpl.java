package com.carService.service;

import com.carService.model.CarService;
import com.carService.model.Invoice;
import com.carService.model.User;
import com.carService.model.Vehicle;
import com.carService.model.dto.InvoiceEdit;
import com.carService.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService{

    InvoiceRepository invoiceRepository;
    VehicleServiceImpl vehicleService;
    UserServiceImpl userService;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, VehicleServiceImpl vehicleService, UserServiceImpl userService) {
        this.invoiceRepository = invoiceRepository;
        this.vehicleService = vehicleService;
        this.userService = userService;
    }

    public InvoiceEdit makeInvoiceEdit(Invoice invoice){
        return new InvoiceEdit(invoice.getId(),invoice.getPrice(),invoice.getFixedProblem(),
                invoice.getVehicle().getBrand(),invoice.getVehicle().getModel(),invoice.getVehicle().getRegistrationNumber(),invoice.getVehicle().getProductionYear(),
                invoice.getClient().getFirstName(),invoice.getClient().getLastName(),invoice.getClient().getEmail(),invoice.getClient().getUsername());
    }

    public List<Invoice> findInvoicesByCarService(CarService carService) {
        return  invoiceRepository.findInvoicesByCarService(carService);
    }

    @Transactional
    public void addInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public Optional<Invoice> findById(int id) {
       return invoiceRepository.findById(id);
    }

    @Transactional
    public void delete(Invoice invoice) {
        invoiceRepository.delete(invoice);
    }

    public List<Object[]> getInvoicesByProductionYear(int id) {
        return invoiceRepository.getInvoicesByProductionYear(id);
    }

    public List<Object[]> getInvoicesByBrand(int id) {
        return invoiceRepository.getInvoicesByBrand(id);
    }

    @Transactional
    public void editInvoice(InvoiceEdit invoiceEdit, int id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);

        if(invoice != null){
            if (invoiceEdit.getPrice() != null && !invoiceEdit.getPrice().equals(invoice.getPrice())) {
                invoice.setPrice(invoiceEdit.getPrice());
            }

            if (invoiceEdit.getFixedProblem() != null && !invoiceEdit.getFixedProblem().equals(invoice.getFixedProblem())) {
                invoice.setFixedProblem(invoiceEdit.getFixedProblem());
            }

            if (invoiceEdit.getModel() != null && !invoiceEdit.getModel().equals(invoice.getVehicle().getModel())) {
                invoice.setFixedProblem(invoiceEdit.getFixedProblem());
            }

            if(invoiceEdit.getBrand() != null || invoiceEdit.getModel() != null || invoiceEdit.getRegistrationNumber()!=null || invoiceEdit.getProductionYear() != null){
                Vehicle vehicle = vehicleService.editVehicle(invoiceEdit.getBrand(),invoiceEdit.getModel(),invoiceEdit.getRegistrationNumber(),invoiceEdit.getProductionYear(),invoice.getVehicle().getId());
                invoice.setVehicle(vehicle);
            }

            if(invoiceEdit.getFirstName() != null || invoiceEdit.getLastName() != null || invoiceEdit.getUsername()!=null || invoiceEdit.getEmail() != null){
                User user = userService.editUser(invoiceEdit.getFirstName(),invoiceEdit.getLastName(),invoiceEdit.getUsername(),invoiceEdit.getEmail(),invoice.getClient().getId());
                invoice.setClient(user);
            }

            invoiceRepository.save(invoice);
        }


    }

    public List<Invoice> findInvoicesByClient(User user) {
        return invoiceRepository.findInvoicesByClient(user);
    }
}
