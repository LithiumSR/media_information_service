package mediacontent;

public class BookInfo extends MediaInfo {
    public String ISBN= "Information not avaiable";
    public String author= "Information not avaiable";
    public String publisher= "Information not avaiable";
    public String releaseDate= "Information not avaiable";
    public String overview= "Information not avaiable";



    public String getISBN() {
        return ISBN;
    }


    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }


    public String getAuthor() {
        return author;
    }


    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }


    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }


    public void setOverview(String summary) {
        this.overview = summary;

    }

    @Override
    public String toString() {
        return
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", overview='" + overview + '\'' +
                ", ISBN='" + ISBN + '\'' +
                '}';
    }
}
