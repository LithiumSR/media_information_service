package core;

import framework.MISConfig;
import framework.MongoDBInterface;
import framework.UpdateNotifier;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@PropertySource(value = "classpath:META-INF/maven/io.pivotal.poc.tzolov/hawq-rest-server/pom.properties", ignoreResourceNotFound=true)
public class Application {

    protected static String config="HEROKU";
    //HEROKU: MIS will send messages to a remote server using Rabbit
    //DEFAULT: MIS will send messages to a remote server using Rabbit. If no remote server is found, a local one will be created.
    //LOCALHOST: MIS will sent messages to a local server using Rabbit.
    //NORABBIT: MIS will NOT send any messages using Rabbit.


    protected final static String mongodb="ENABLED";
    //ENABLED: MIS will try to send every messages received using the sock endpoint to a remote server defined in MISConfig
    //DISABLED: MIS won't send any message to any server

    protected static String repoName="";
    protected final static String username="LithiumSR";
    protected static String version_number="0.0.0";

    public static void main(String[] args) {
        getProjectInfo();
        new MISConfig("redacted_api.cfg"); //Get Api keys and some config variables
        if (mongodb.equals("ENABLED")) new MongoDBInterface(); //Setup MongoDB Interface if needed
        if (!config.equals("NORABBIT")) startRabbitMQ(); //Setup RabbitMQ Interface if needed
        SpringApplication.run(Application.class, args); //Start Spring App
        startUpdateNotifier(); //Start update notifier

    }

    public static String getConfig() {
        return config;
    }
    public static String getMongoDBStatus() {
        return mongodb;
    }
    private static void startRabbitMQ(){
        new RabbitSend(); //Setup of the class used to send messages to the receiver
        if(config.equals("DEFAULT")||config.equals("LOCALHOST")){
            Thread t1 = new Thread(new RabbitReceive("MIS_Feedback")); //Start localhost receiver for MIS_Feedback queue
            Thread t2 = new Thread(new RabbitReceive("MIS_Info")); //Start localhost receiver for MIS_Info queue
            t1.start(); //Start receiver in its own thread
            t2.start();
        }

    }

    private static void getProjectInfo(){
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader("pom.xml"));
            //System.out.println(model.getVersion());
            //System.out.println(model.getArtifactId());
            version_number = model.getVersion();
            repoName=model.getArtifactId().toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

    private static void startUpdateNotifier(){
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(1);
        ScheduledFuture scheduledFuture =
                scheduledExecutorService.scheduleWithFixedDelay(new UpdateNotifier(username,repoName,version_number),0,96, TimeUnit.HOURS);

    }

}
