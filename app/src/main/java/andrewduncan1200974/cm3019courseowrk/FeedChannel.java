package andrewduncan1200974.cm3019courseowrk;

/**
 * Class which stores the information from the Settings page.
 * Created by Andrew Duncan on 23/04/2016.
 */
public class FeedChannel {
    //URL of Website
    private String mUrl;
    //Users Description of website
    private String mDescription;

    public FeedChannel(String url, String description) {
        setmDescription(description);
        setmUrl(url);
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    @Override
    public String toString() {
        return mDescription.isEmpty() ? mUrl : mDescription;
    }
}
