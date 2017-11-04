package mediacontent;

public class MediaRequest {
    private String type;
    private String title;

    public MediaRequest(String t, String title){
        type=t;
        this.title=title;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MediaRequest{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
