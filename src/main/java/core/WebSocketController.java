package core;

import framework.MediaOperations;
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
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }

    @MessageMapping("/chat_check")
    @SendTo("/topic/messages")
    public OutputMessage checkInfo(Message message) throws Exception {
        if(message.getText().contains("!help")) {
            String time = new SimpleDateFormat("HH:mm").format(new Date());
            return new OutputMessage("MIS Bot","Hey, it seems that you are interested in what this bot is capable of :) \n"+
            "Right now you can write ~type:{media type} {title}~ and get some useful information."+" \n"+
                    "(e.g: ~type:film&book Harry Potter and the philosopher's stone)." + " \n"+"Be aware that you can find for more media infos at once, just use the character "+"'&'."+ " \n" +
                    "Happy chatting ^_^",time);

        }
        else {
            String time = new SimpleDateFormat("HH:mm").format(new Date());
            if (StringUtils.countMatches(message.getText(), "~") >= 2) {
                return new OutputMessage("MIS Bot", MediaOperations
                        .generateResponse(MediaOperations.parseMessage(message.getText())), "");
            } else return new OutputMessage("", "", "");
        }
    }

}