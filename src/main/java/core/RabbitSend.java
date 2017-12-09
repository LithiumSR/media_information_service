package core;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import framework.MISConfig;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RabbitSend {
    private static Channel channel;
    private static String uri;
    private static boolean durable = false;    //durable - RabbitMQ will never lose the queue if a crash occurs
    private static boolean exclusive = false;  //exclusive - if queue only will be used by one connection
    private static boolean autoDelete = false; //autodelete - queue is deleted when last consumer unsubscribes
    private static Logger logger = Logger.getLogger("rabbit_send");
    public static void init(){
        ConnectionFactory factory = new ConnectionFactory();
        if (!Application.getRabbitStatus().equals("LOCALHOST")) uri = MISConfig.getAMQP();
        if (uri == null) uri = "amqp://guest:guest@localhost";
        try {
            factory.setUri(uri);
            factory.setRequestedHeartbeat(30);
            factory.setConnectionTimeout(30000);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        //System.out.println(uri);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            logger.log(Level.INFO,"Connection enstablished with rabbitmq server");
        } catch (IOException e) {
            e.printStackTrace();
            Application.setRabbitStatus("NORABBIT");
            logger.log(Level.SEVERE, "RabbitMQ got disabled because of unintended behavior");
        } catch (TimeoutException e) {
            e.printStackTrace();
            Application.setRabbitStatus("NORABBIT");
            logger.log(Level.SEVERE, "RabbitMQ got disabled because of unintended behavior");
        }
    }
    public static void send(String toSend,String queue){
        try {
            channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
            String message = toSend;
            channel.basicPublish("", queue, null, message.getBytes());
            //System.out.println(" [x] Sent '" + message + "'");
        } catch (IOException e) {
            e.printStackTrace();

        }
        catch (NullPointerException e) {
            e.printStackTrace();
            Application.setRabbitStatus("NORABBIT");
            logger.log(Level.SEVERE, "RabbitMQ got disabled because of unintended behavior");
        }

    }
}
