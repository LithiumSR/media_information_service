package core;

import framework.MyAPIKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    protected static String config="DEFAULT";

    public static void main(String[] args) {
        startRabbit();
        new MyAPIKey("redacted_api.cfg"); //Get Api keys
        SpringApplication.run(Application.class, args); //Start Spring App
        }

    public static String getConfig() {
        return config;
    }
    public static void startRabbit(){
        new RabbitSend(); //Setup of the class used to send messages to the receiver
        Thread t1 = new Thread(new RabbitReceive()); //Start receiver
        t1.start(); //Start receiver in its own thread
    }

}
