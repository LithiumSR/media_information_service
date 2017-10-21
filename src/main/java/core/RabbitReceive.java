package core;
import com.rabbitmq.client.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitReceive implements Runnable{
    private final static String QUEUE_NAME = "MSI_Info";
    private static Channel channel;

    public RabbitReceive(){

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            channel = connection.createChannel();
            System.out.println(channel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(channel);
        System.out.println(" [*] Waiting for messages...");

    }

    @Override
    public void run() {
        System.out.println(channel);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                fileOP(message);
            }
        };
        try {
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void fileOP(String text){
        File fl=new File("media_request.txt");
        if(!fl.exists()) try {
            fl.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long sizeInBytes=fl.length();
        long sizeInMb = sizeInBytes/ (1024 * 1024);

        if (sizeInMb> 20){
            fl.delete();
            try {
                fl.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter fw=new FileWriter(fl,true);
            fw.append(text);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
