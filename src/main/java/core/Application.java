package core;

import framework.MyAPIKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new MyAPIKey("redacted_api.cfg");
        SpringApplication.run(Application.class, args);
        //SpringApplication.run(RabbitMQApp.class, args);

    }

}
