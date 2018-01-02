package core;

import framework.MISConfig;
import framework.MongoDBInterface;
import framework.UpdateNotifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Application {

    protected static String rabbit="HEROKU";
    //HEROKU: MIS will send messages to a remote server using Rabbit
    //DEFAULT: MIS will send messages to a remote server using Rabbit. If no remote server is found, a local one will be created.
    //LOCALHOST: MIS will sent messages to a local server using Rabbit.
    //NORABBIT: MIS will NOT send any messages using Rabbit.


    protected final static String mongodb="ENABLED";
    //ENABLED: MIS will try to send every messages received using the sock endpoint to a remote server defined in MISConfig
    //DISABLED: MIS won't send any message to any server

    private static final String repoName="media_information_service";

    private static final String version_number="1.0.3";

    private static final String username="LithiumSR";

    public static void main(String[] args) {
        MISConfig.init("MIS_config.cfg"); //Get Api keys and some config variables
        if (mongodb.equals("ENABLED")) MongoDBInterface.init(); //Setup MongoDB Interface if needed
        if (!rabbit.equals("NORABBIT")) startRabbitMQ(); //Setup RabbitMQ Interface if needed
        SpringApplication.run(Application.class, args); //Start Spring App
        startScheduledThreads(); //Start update notifier

    }

    public static String getRabbitStatus() {
        return rabbit;
    }
    public static String getMongoDBStatus() {
        return mongodb;
    }
    private static void startRabbitMQ(){
        RabbitSend.init(); //Setup of the class used to send messages to the receiver
        if(rabbit.equals("DEFAULT")||rabbit.equals("LOCALHOST")){
            Thread t1 = new Thread(new RabbitReceive("MIS_Feedback")); //Start localhost receiver for MIS_Feedback queue
            Thread t2 = new Thread(new RabbitReceive("MIS_Info")); //Start localhost receiver for MIS_Info queue
            t1.start(); //Start receiver in its own thread
            t2.start();
        }

    }


    private static void startScheduledThreads(){
        //Schedule update notifier thread
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(1);
        ScheduledFuture scheduledFuture =
                scheduledExecutorService.scheduleWithFixedDelay(new UpdateNotifier(username,repoName,version_number),0,96, TimeUnit.HOURS);

    }


    public static void setRabbitStatus(String rabbitStatus) {
        Application.rabbit = rabbitStatus;
    }
}
