package andrewduncan1200974.cm3019courseowrk;

/**
 * Created by Andrew on 22/04/2016.
 */
public class FeedItem {
    String title;
    String link;
    String description;
    String pubDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public boolean containsKeyword(String keyword) {
        return description.toString().toLowerCase().contains(keyword) || title.toLowerCase().contains(keyword);
    }
}

