package core;

import framework.MSIConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    protected static String config="DEFAULT";

    public static void main(String[] args) {
        new MSIConfig("redacted_api.cfg"); //Get Api keys
        if (config.equals("DEFAULT")) startRabbitMQ();
        SpringApplication.run(Application.class, args); //Start Spring App
        }

    public static String getConfig() {
        return config;
    }
    public static void startRabbitMQ(){
        new RabbitSend(); //Setup of the class used to send messages to the receiver
        //Thread t1 = new Thread(new RabbitReceive()); //Start localhost receiver
        //t1.start(); //Start receiver in its own thread
    }

}
