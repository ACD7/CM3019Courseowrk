package andrewduncan1200974.cm3019courseowrk;

/**
 * The FeedItem class stores the information from the xml, into a FeedItem Object
 * Created by Andrew Duncan on 22/04/2016.
 */
public class FeedItem {
    //Title of feedItem
    String title;
    //URL to article
    String link;
    //Small Description of article
    String description;
    //publication date of article
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

    //Check if search term is contained in itemDescription
    public boolean containsKeyword(String keyword) {
        return description.toString().toLowerCase().contains(keyword) || title.toLowerCase().contains(keyword);
    }
}

