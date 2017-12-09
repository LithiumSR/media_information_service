package mediacontent;

public class Media {
    private String title;
    private String ISBN;
    public String language;
    public String year;
    public String maxResult;
    public String orderBy;
    public String author;
    public String service;
    public String getTitle() {
        return title;
    }
    public String getISBN() {
        return ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setISBN(String ISBN) { this.ISBN=ISBN; }

    public String getLanguage() {
        return language;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(String maxResult) {
        this.maxResult = maxResult;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}


