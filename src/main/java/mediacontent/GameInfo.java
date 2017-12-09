package mediacontent;

public class GameInfo extends MediaInfo {
    public String vote= "Information not avaiable";
    public String overview= "Information not avaiable";
    public String pegi= "Information not avaiable";
    public String age_required= "Information not avaiable";
    public String webSite= "Information not avaiable";
    public String releaseDate= "Information not avaiable";
    public String wiki="Information not avaiable";

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPegi() {
        return pegi;
    }

    public void setPegi(String pegi) {
        this.pegi = pegi;
    }

    public void setAgeRequired(String age) {
        age_required=age;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getWiki() { return wiki; }

    public void setWiki(String wiki) { this.wiki = wiki; }
}
