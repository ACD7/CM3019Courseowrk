package andrewduncan1200974.cm3019courseowrk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

    private ListView mFeedsLv;
    private EditText mFeedUrl;
    private EditText mFeedDescription;

    private ArrayAdapter<FeedChannel> mAdapter;
    private List<FeedChannel> mFeedChannels;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;
    private static final int REQ_MODIFY_FEEDS = 0x1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        //Get shared preferences object
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Object to allow me to edit sharedPreferences
        mSharedPreferencesEditor = mSharedPreferences.edit();

        //Get List of user input from the preferences page
        mFeedChannels = getFeedChannels();

        //Deleting an object from the list view and from shared Preferences
        mFeedsLv = (ListView)findViewById(R.id.feeds_lv);
        mFeedsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Pop up window asking if the user wants to delete the selected item.
                AlertDialog.Builder adb = new AlertDialog.Builder(PreferencesActivity.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + mFeedChannels.get(position).getmDescription());
                final FeedChannel item = mFeedChannels.remove(position);
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //remove item from shared prefs using the url as a key
                        mSharedPreferencesEditor.remove(item.getmUrl());

                        mAdapter.notifyDataSetChanged();

                        setResult(RESULT_OK);
                    }

                });
                adb.show();
            }
        });
        //call basic array adapter to set results into the ListView
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
        //Making sure i don't store anything but strings.
        Map<String, ?> allChannels = mSharedPreferences.getAll();
        List<FeedChannel> channels = new ArrayList<>();

        Iterator<String> iterator = allChannels.keySet().iterator();
        while(iterator.hasNext()) {
            String url = iterator.next();
            channels.add(new FeedChannel(url, mSharedPreferences.getString(url, "")));
        }
        return channels;
    }

    //On click for add channel button.
    public void onAddChannelClick(View v) {
        String url = mFeedUrl.getText().toString().trim();
        String description = mFeedDescription.getText().toString().trim();

        //error handling if URL is left empty
        if (url.isEmpty()) {
            Toast.makeText(this, R.string.empty_url_error, Toast.LENGTH_LONG).show();
            return;
        }

        // error handling if description is left empty
        if (description.isEmpty()) {
            Toast.makeText(this, R.string.empty_description_error, Toast.LENGTH_LONG).show();
            return;
        }

        //only allows 10 feeds to be added
        if (mAdapter.getCount() >= 10) {
            Toast.makeText(this, R.string.limit_reached_error, Toast.LENGTH_LONG).show();
            return;
        }

        mAdapter.add(new FeedChannel(url, description));
        mSharedPreferencesEditor.putString(url, description);
        setResult(RESULT_OK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Change activity to the Preference activity or home activity when clicked from the dropdown menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivityForResult(intent, REQ_MODIFY_FEEDS);
            return true;
        }

        if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, REQ_MODIFY_FEEDS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
