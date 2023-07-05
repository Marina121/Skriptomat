package ba.sum.fpmoz.skriptomat.model;

public class NewsItem {
    private String title;
    private String alias;
    private String image;

    public  NewsItem() {

    }

    public NewsItem(String title, String alias, String image) {
        this.title = title;
        this.alias = alias;
        this.image = "https://web-admin.sum.ba/api/storage/" + image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

