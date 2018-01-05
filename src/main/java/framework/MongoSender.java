package framework;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.LoggerFactory;
import websocket.OutputMessage;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public  class MongoSender implements Runnable {
    private MongoCollection<Document> collection;
    private LinkedList<Document> lis;
    private final Semaphore semaphore;
    private Logger logger;

    public MongoSender(MongoCollection<Document> collection){
        this.collection=collection;
        lis=new LinkedList<Document>();
        semaphore = new Semaphore(1);
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        logger= loggerContext.getLogger("framework.MongoSender");
    }

    public synchronized void run() {
        LinkedList<Document> lis2=null;
        try {
            semaphore.acquire();
            lis2= (LinkedList<Document>) lis.clone();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
        int size_stash=0;
        try {

            if (lis2!=null){
                size_stash=lis2.size();
                for (Document t : lis2){
                    if (collection!=null) collection.insertOne(t);
                }
            }
            lis.removeAll(lis2);
        } catch (MongoException e){
            e.printStackTrace();
            logger.warn("The sending process of a stash of "+lis.size()+ " got delayed");
        }
        if (size_stash!=0) logger.info("Sent stash of "+lis2.size()+" to the remote MongoDB server");
    }

    protected synchronized void stashMessage(Document dt){
        try {
            semaphore.acquire();
            lis.add(dt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            semaphore.release();
        }

    }
}
