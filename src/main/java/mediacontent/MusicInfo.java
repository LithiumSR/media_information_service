package mediacontent;

public class MusicInfo extends MediaInfo {
    public String labels= "Information not avaiable";
    public String genre= "Information not avaiable";
    public String releaseDate= "Information not avaiable";

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
