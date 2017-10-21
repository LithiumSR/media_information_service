package core;

import framework.MyAPIKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new RabbitSend();
        Thread t1= new Thread(new RabbitReceive());
        t1.start();

        new MyAPIKey("redacted_api.cfg");
        SpringApplication.run(Application.class, args);
        //SpringApplication.run(RabbitMQApp.class, args);

    }

}
