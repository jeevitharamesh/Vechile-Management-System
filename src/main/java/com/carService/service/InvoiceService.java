package com.carService.service;

import com.carService.model.CarService;
import com.carService.model.Invoice;
import com.carService.model.User;
import com.carService.model.Vehicle;
import com.carService.model.dto.InvoiceEdit;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {

    InvoiceEdit makeInvoiceEdit(Invoice invoice);

    List<Invoice> findInvoicesByCarService(CarService carService);

    void addInvoice(Invoice invoice);

    Optional<Invoice> findById(int id);

    void delete(Invoice invoice);

    List<Object[]> getInvoicesByProductionYear(int id);

    List<Object[]> getInvoicesByBrand(int id);
    void editInvoice(InvoiceEdit invoiceEdit, int id);

    List<Invoice> findInvoicesByClient(User user);
}
