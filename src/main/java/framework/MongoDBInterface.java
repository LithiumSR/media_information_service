package framework;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.LoggerFactory;
import websocket.OutputMessage;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MongoDBInterface {

    private static MongoCollection<Document> collection;
    private static  MongoSender ms;
    public static void addCollection(OutputMessage mo){
        Document document = new Document("title", "MIS_Webchat")
                .append("from", mo.getFrom())
                .append("text",mo.getText())
                .append("time", Instant.now().toEpochMilli());
        ms.stashMessage(document);
    }

    public static void init() {
        connect(MISConfig.getMongoDB());
        if (collection!=null) startMongoServerThread();
    }

    private static void connect(String URI){
        //Change MongoDB's logger settings
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.INFO);

        //Setup Connection
        MongoClientURI connectionString = new MongoClientURI(URI);
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("media_information_service_db");
        collection = database.getCollection("messages");

    }

    private static void startMongoServerThread(){
        ms= new MongoSender(collection);
        ScheduledExecutorService scheduledExecutorService2 =
                Executors.newScheduledThreadPool(1);
        ScheduledFuture scheduledFuture2 =
                scheduledExecutorService2.scheduleWithFixedDelay(ms, 1, 5, TimeUnit.MINUTES);
    }
}
