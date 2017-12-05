package websocket;

public class OutputMessage {
    public String from;
    public String text;
    public String time;

    public OutputMessage(String from, String text, String time) {
        this.from=from;
        this.text=text;
        this.time=time;
    }

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }
}
