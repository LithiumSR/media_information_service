package mediacontent;

public class Media {
    private String title;
    private String ISBN;
    public String language;
    public String getTitle() {
        return title;
    }
    public String getISBN() {
        return ISBN;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setISBN(String ISBN) { this.ISBN=ISBN; }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
}


