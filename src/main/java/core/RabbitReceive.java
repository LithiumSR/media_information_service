package core;

import com.rabbitmq.client.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitReceive implements Runnable{
    private String QUEUE_NAME;
    private Channel channel;

    public RabbitReceive(String queue_name){
        QUEUE_NAME=queue_name;
        System.out.println("[RabbitMQ] Setup flow has started for "+queue_name+" queue...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //channel.queuePurge("MIS_Info"); //Remove unread messages from a previous run of the server
        } catch (TimeoutException e) {
            e.printStackTrace();
            Application.config="NORABBIT";
        } catch (IOException e) {
            e.printStackTrace();
            Application.config="NORABBIT";
        }


    }

    @Override
    public void run() {
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [RabbitMQ] Received '" + message + "'");
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
        File fl=new File(QUEUE_NAME+".txt");
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
