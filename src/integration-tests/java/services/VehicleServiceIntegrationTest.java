package services;

import com.carService.model.Vehicle;
import com.carService.service.VehicleService;
import config.IntegrationTest;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VehicleServiceIntegrationTest extends IntegrationTest {

    private final VehicleService vehicleService;

    static {
        POSTGRES_CONTAINER.start();
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
    }

    @Autowired
    public VehicleServiceIntegrationTest(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Test
    public void test() {

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("BMW");
        vehicle.setModel("X5");
        vehicle.setProductionYear(2005);
        vehicle.setRegistrationNumber("A1234BV");

        vehicleService.addVehicle(vehicle);

        Optional<Vehicle> actual = vehicleService.findByRegistrationNumber("A1234BV");
        assertTrue(actual.isPresent());
        assertEquals(vehicle.getBrand(), actual.get().getBrand());
        assertEquals(vehicle.getModel(), actual.get().getModel());
        assertEquals(vehicle.getProductionYear(), actual.get().getProductionYear());
    }
}
