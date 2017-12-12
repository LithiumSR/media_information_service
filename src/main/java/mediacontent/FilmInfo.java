package mediacontent;

public class FilmInfo extends MediaInfo {
    public String overview= "Information not avaiable";
    public String releaseDate= "Information not avaiable";
    public String vote= "Information not avaiable";
    public String linkImage="Information not avaiable";


    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    @Override
    public String toString() {
        return "FilmInfo{" +
                "overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", vote='" + vote + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
