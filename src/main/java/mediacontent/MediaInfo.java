package mediacontent;

public class MediaInfo {
    public String title;
    public String ISBN;
    public String author;
    public String summary;
    public String publisher;
    public String releaseDate;
    public String genre;



    public String getTitle() {
        return title;
    }

    public String getISBN() {
        return ISBN;
    }
    public String getAuthor() {
        return author;
    }
    public String getSummary() {
        return summary;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setISBN(String iSBN) {
        this.ISBN = iSBN;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Media{" +
                "title='" + title + '\'' +
                ", iSBN='" + ISBN + '\'' +
                ", author='" + author + '\'' +
                ", summary='" + summary + '\'' +
                ", publisher='" + publisher + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public void setGenre(String genre) {
        this.genre = genre;
    }
}
