package core;

import framework.MISConfig;
import framework.MongoDBInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Application {

    protected static String config="HEROKU";
    //HEROKU: MIS will send messages to a remote server using Rabbit
    //DEFAULT: MIS will send messages to a remote server using Rabbit. If no remote server is found, a local one will be created.
    //LOCALHOST: MIS will sent messages to a local server using Rabbit.
    //NORABBIT: MIS will NOT send any messages using Rabbit.


    protected static String mongodb="ENABLED";
    //ENABLED: MIS will try to send every messages received using the sock endpoint to a remote server defined in MISConfig
    //DISABLED: MIS won't send any message to any server


    public static void main(String[] args) {
        new MISConfig("redacted_api.cfg"); //Get Api keys and some config variables
        if (mongodb.equals("ENABLED")) new MongoDBInterface();
        if (!config.equals("NORABBIT")) startRabbitMQ();
        SpringApplication.run(Application.class, args); //Start Spring App
        }

    public static String getConfig() {
        return config;
    }
    public static String getMongoDBStatus() {
        return mongodb;
    }
    public static void startRabbitMQ(){
        new RabbitSend(); //Setup of the class used to send messages to the receiver
        if(config.equals("DEFAULT")||config.equals("LOCALHOST")){
            Thread t1 = new Thread(new RabbitReceive("MIS_Feedback")); //Start localhost receiver for MIS_Feedback queue
            Thread t2 = new Thread(new RabbitReceive("MIS_Info")); //Start localhost receiver for MIS_Info queue
            t1.start(); //Start receiver in its own thread
            t2.start();
        }

    }

}
