package core;

import framework.MediaOperations;
import framework.MongoDBInterface;
import org.apache.commons.lang.StringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import websocket.Message;
import websocket.OutputMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) throws Exception {
        if(message.getFrom().toLowerCase().equals("mis bot") || message.getFrom().toLowerCase().equals("server")) {
            throw new Exception("Illegal sender username");
        }
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        OutputMessage om = new OutputMessage(message.getFrom(), message.getText(), time);
        if(!om.getFrom().equals("Alert") && !om.getFrom().equals("MIS Bot") && !om.text.trim().equals("")) MongoDBInterface.addCollection(om);
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }

    @MessageMapping("/chat_check")
    @SendTo("/topic/messages")
    public OutputMessage checkInfo(Message message) throws Exception {
        if(message.getText().toLowerCase().contains("!help")) {
            String time = new SimpleDateFormat("HH:mm").format(new Date());
            Thread.sleep(700);
            return new OutputMessage("MIS Bot","Hey, it seems that you are interested in what this bot is capable of :) \n"+
            "Right now you can write ~type:{media type} {title}~ and get some useful information."+" \n"+
                    "(e.g: ~type:film&book Harry Potter and the philosopher's stone~ )." + " \n"+"Be aware that you can find for more media infos at once, just use the character "+"'&'."+ " \n" +
                    "You can also leave a feedback using the command !feedback."+"\n"+
                    "Happy chatting :)",time);

        }
        else {
            String time = new SimpleDateFormat("HH:mm").format(new Date());
            if (StringUtils.countMatches(message.getText(), "~") >= 2) {
                return new OutputMessage("MIS Bot", MediaOperations.generateResponse(MediaOperations.parseMessage(message.getText()),message.getFrom()), time);
            } else return new OutputMessage("", "", "");
        }
    }

    @MessageMapping("/chat_feedback")
    @SendTo("/topic/messages")
    public OutputMessage getFeedback(Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        if(message.getText().trim().startsWith("!feedback ")) {
            String s = message.getText().substring(message.getText().indexOf(" "));
            RabbitSend.sendFeedback(message.getFrom(),s);
            Thread.sleep(1000);
            return new OutputMessage("MIS Bot","Thank you for your feedback :)",time);
        }
        else {
            Thread.sleep(1000);
            return new OutputMessage("MIS Bot","You can't leave a feedback without text :(",time);
            }
        }
    }



