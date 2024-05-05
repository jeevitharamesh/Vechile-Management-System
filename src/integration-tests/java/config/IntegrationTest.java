package config;

import com.carService.CarServiceApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(classes = CarServiceApplication.class)
public class IntegrationTest {

    public static final String DOCKER_IMAGE_NAME = "postgres:alpine";
    public static final String DATABASE_NAME = "postgres";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "123456";
    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>(
            DOCKER_IMAGE_NAME)
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);
}
