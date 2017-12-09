package framework;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.LoggerFactory;
import websocket.OutputMessage;

import java.time.Instant;


public class MongoDBInterface {

    private static boolean check_flag=true;
    private static MongoCollection<Document> collection;

    public static void addCollection(OutputMessage mo){
        Document document = new Document("title", "MIS_Webchat")
                .append("from", mo.getFrom())
                .append("text",mo.getText())
                .append("time", Instant.now().toEpochMilli());
        try{
            if (collection!=null && check_flag) collection.insertOne(document);
        } catch (MongoTimeoutException e){
            e.printStackTrace();
            check_flag=false;
        }
    }

    public static void init(){
        //Change MongoDB's logger settings
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.INFO);

        //Setup Connection
        MongoClientURI connectionString = new MongoClientURI(MISConfig.getMongoDB());
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("media_information_service_db");
        collection = database.getCollection("messages");
    }

}
