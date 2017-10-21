package core;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class RabbitSend {
    private final static String QUEUE_NAME = "MSI_Info";
    private static Channel channel;

    public RabbitSend() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void send(String toSend){
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = toSend;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            //System.out.println(" [x] Sent '" + message + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
