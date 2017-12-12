package mediacontent;

public class MusicInfo extends MediaInfo {
    public String labels= "Information not avaiable";
    public String artist= "Information not avaiable";
    public String genre= "Information not avaiable";
    public String releaseDate= "Information not avaiable";
    public String linkpreview="Preview not avaiable";
    public String overview="Information not avaiable";
    public String collection="Information not avaiable";
    public String linkImage="Information not avaiable";

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

    public String getLinkpreview() {
        return linkpreview;
    }

    public void setLinkpreview(String linkpreview) {
        this.linkpreview = linkpreview;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkimage) {
        this.linkImage = linkimage;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "title='" + getTitle() + '\'' +
                ", artist='" + artist + '\'' +
                ", collection='" + collection + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", linkpreview='" + linkpreview + '\'' +
                '}';
    }
}
