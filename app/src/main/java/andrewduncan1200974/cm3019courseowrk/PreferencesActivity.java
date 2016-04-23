package andrewduncan1200974.cm3019courseowrk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andrew on 23/04/2016.
 */
public class PreferencesActivity extends AppCompatActivity {
    public static final String PREF_CHANNELS = "channels";
    private ListView mFeedsLv;
    private EditText mFeedUrl;
    private EditText mFeedDescription;

    private ArrayAdapter<FeedChannel> mAdapter;
    private List<FeedChannel> mFeedChannels;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferencesEditor = mSharedPreferences.edit();

        mFeedChannels = getFeedChannels();

        mFeedsLv = (ListView)findViewById(R.id.feeds_lv);
        mFeedsLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                FeedChannel item = mFeedChannels.remove(position);
                //We remove the item from the shared preferences
                mSharedPreferencesEditor.remove(item.getmUrl());

                mAdapter.notifyDataSetChanged();

                setResult(RESULT_OK);

                return true;
            }
        });
        mAdapter = new ArrayAdapter<FeedChannel>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mFeedChannels);
        mFeedsLv.setAdapter(mAdapter);

        mFeedDescription = (EditText)findViewById(R.id.feed_description_et);
        mFeedUrl = (EditText)findViewById(R.id.feed_url_et);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSharedPreferencesEditor != null) {
            mSharedPreferencesEditor.commit();
        }
    }

    private List<FeedChannel> getFeedChannels() {
        //We need to make sure we dont store anything but Strings!
        Map<String, ?> allChannels = mSharedPreferences.getAll();
        List<FeedChannel> channels = new ArrayList<>();

        Iterator<String> iterator = allChannels.keySet().iterator();
        while(iterator.hasNext()) {
            String url = iterator.next();
            channels.add(new FeedChannel(url, mSharedPreferences.getString(url, "")));
        }
        return channels;
    }

    public void onAddChannelClick(View v) {
        String url = mFeedUrl.getText().toString().trim();
        String description = mFeedDescription.getText().toString().trim();

        if (url.isEmpty()) {
            Toast.makeText(this, R.string.empty_url_error, Toast.LENGTH_LONG).show();
            return;
        }

        if (mAdapter.getCount() >= 10) {
            Toast.makeText(this, R.string.limit_reached_error, Toast.LENGTH_LONG).show();
            return;
        }

        mAdapter.add(new FeedChannel(url, description));
        mSharedPreferencesEditor.putString(url, description);
        setResult(RESULT_OK);
    }
}
