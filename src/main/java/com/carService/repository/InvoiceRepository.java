package com.carService.repository;

import com.carService.model.CarService;
import com.carService.model.Invoice;
import com.carService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    List<Invoice> findInvoicesByCarService(CarService carService);

    List<Invoice> findInvoicesByClient(User client);

    //SELECT production_year, COUNT(production_year) FROM car_service.invoice INNER JOIN vehicle ON invoice.vehicle_id=vehicle.id group by production_year
    //SELECT brand, COUNT(brand) FROM car_service.invoice INNER JOIN vehicle ON invoice.vehicle_id=vehicle.id where car_service_id=1 group by brand

    @Query(value = "SELECT production_year, COUNT(production_year) FROM car_service.invoice " +
            "INNER JOIN vehicle ON invoice.vehicle_id=vehicle.id where car_service_id=?1 group by production_year",
            nativeQuery = true)
    List<Object[]> getInvoicesByProductionYear(int serviceID);

    @Query(value = "SELECT brand, COUNT(brand) FROM car_service.invoice " +
            "INNER JOIN vehicle ON invoice.vehicle_id=vehicle.id where car_service_id=?1 group by brand",
            nativeQuery = true)
    List<Object[]> getInvoicesByBrand(int serviceID);
}
