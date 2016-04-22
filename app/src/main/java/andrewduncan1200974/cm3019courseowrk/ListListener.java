package andrewduncan1200974.cm3019courseowrk;

/**
 * Created by Andrew on 22/04/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import andrewduncan1200974.cm3019courseowrk.FeedItem;

/**
 * Class implements a list listener.
 */
public class ListListener implements OnItemClickListener {
    // And a reference to a calling activity
    // Calling activity reference
    Activity mParent;
    /** We will set those references in our constructor.*/
    public ListListener(Activity parent) {
        mParent  = parent;
    }

    /** Start a browser with url from the rss item.*/
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        // We create an Intent which is going to display data
        Intent i = new Intent(Intent.ACTION_VIEW);
        // We have to set data for our new Intent;
        i.setData(Uri.parse(((FeedItem)(parent.getItemAtPosition(pos))).getLink()));
        // And start activity with our Intent
        mParent.startActivity(i);
    }
}

