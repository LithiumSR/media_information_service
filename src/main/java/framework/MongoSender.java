package framework;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.LoggerFactory;

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
        boolean errorFlag=false;
        //Clone elements in stash
        try {
            semaphore.acquire();
            lis2= (LinkedList<Document>) lis.clone();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }

        //Send element cloned from stash
        int sizeStash=0;
        try {
            if (lis2!=null && collection!=null){
                sizeStash=lis2.size();
                //Send messages to MongoDB server
                for (Document t : lis2){
                    collection.insertOne(t);
                }
            }
            lis.removeAll(lis2); //Remove sent elements from the stashed ones
        } catch (MongoException e){
            errorFlag=true;
            logger.warn("The sending process of a stash of "+lis.size()+ " messages got delayed");
        }
        if (sizeStash!=0 && !errorFlag) logger.info("Sent stash of "+lis2.size()+" messages to the remote MongoDB server");
    }

    protected synchronized void stashMessage(Document dt){
        //Add document to the list that is going to be sent
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
