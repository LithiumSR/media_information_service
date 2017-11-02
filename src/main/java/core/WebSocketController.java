package core;

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
    public OutputMessage checkInfo(String type,String title) throws Exception {
        Thread.sleep(2000); //Added for testing purpose
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage("Server","Text message", time);
    }

}
