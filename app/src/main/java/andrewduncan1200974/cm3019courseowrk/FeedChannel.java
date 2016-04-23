package andrewduncan1200974.cm3019courseowrk;

/**
 * Created by Andrew on 23/04/2016.
 */
public class FeedChannel {
    private String mUrl;
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
