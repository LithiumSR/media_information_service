package core;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import framework.MISConfig;
import framework.MediaOperations;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;


public class RabbitSend {
    public static Channel channel;
    private static String uri;
    private static boolean durable = false;    //durable - RabbitMQ will never lose the queue if a crash occurs
    private static boolean exclusive = false;  //exclusive - if queue only will be used by one connection
    private static boolean autoDelete = false; //autodelete - queue is deleted when last consumer unsubscribes
    private static Logger logger;

    public static void init() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger("core.RabbitSend");
        ConnectionFactory factory = new ConnectionFactory();
        if (!Application.getRabbitStatus().equals("LOCALHOST")) uri = MISConfig.getAMQP();
        if (uri == null) uri = "amqp://guest:guest@localhost";
        try {
            factory.setUri(uri);
            factory.setRequestedHeartbeat(30);
            factory.setConnectionTimeout(30000);
        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            logger.info("Connection enstablished with rabbitmq server");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            Application.setRabbitStatus("NORABBIT");
            logger.warn("RabbitMQ got disabled because of unintended behavior");
        }
    }

    private static void send(String toSend, String queue) throws IOException {
        try {
            channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
            String message = toSend;
            channel.basicPublish("", queue, null, message.getBytes());
            //System.out.println(" [x] Sent '" + message + "'");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Application.setRabbitStatus("NORABBIT");
            logger.warn("RabbitMQ got disabled because of unintended behavior");
        }

    }

    public static void sendMediaRequest(String name, String type, HttpServletRequest request) throws IOException{
        if (!Application.getRabbitStatus().equals("NORABBIT")) {
            send(type + " request by " + request.getRemoteAddr() + " " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                    .format(new Date()) + " : - " + "TITLE: " + name, "MIS_Info");
        }
    }

    public static void sendOAuth(String files, String service, HttpServletRequest request) throws IOException{
        if (!Application.getRabbitStatus().equals("NORABBIT")) {
            send(StringUtils.capitalize(service) + " request by " + request.getRemoteAddr() + " " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                    .format(new Date()) + " : \n - " + "Files: " + files + "\n", "MIS_Info");
        }
    }

    public static void sendFeedback(String from, String feedback, String time) throws IOException {
        if (!Application.getRabbitStatus().equals("NORABBIT")) {
            send("Feedback from "+from+": "+feedback + " ("+time+")", "MIS_Feedback");
        }
    }
}
