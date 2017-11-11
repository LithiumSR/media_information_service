package core;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import framework.MSIConfig;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;


public class RabbitSend {
    private final static String QUEUE_NAME = "MSI_Info";
    private static Channel channel;
    private static String uri;

    private static boolean durable = true;    //durable - RabbitMQ will never lose the queue if a crash occurs
    private static boolean exclusive = false;  //exclusive - if queue only will be used by one connection
    private static boolean autoDelete = false; //autodelete - queue is deleted when last consumer unsubscribes

    public RabbitSend() {
        ConnectionFactory factory = new ConnectionFactory();
        uri = MSIConfig.getAMQP();
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    public static void send(String toSend){
        try {
            channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, null);
            String message = toSend;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            //System.out.println(" [x] Sent '" + message + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
