package andrewduncan1200974.cm3019courseowrk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQ_MODIFY_FEEDS) {
            ReadRss readRss = new ReadRss(this, listView);
            readRss.execute();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private static final int REQ_MODIFY_FEEDS = 0x1337;
    ListView listView;
    EditText filterEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get search bar object
        filterEt = (EditText)findViewById(R.id.filter_query_et);
        filterEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //Update MyAdapter when a search query is inserted, this will refresh the List of feedItems
            @Override
            public void afterTextChanged(Editable s) {
                MyAdapter adapter = (MyAdapter) listView.getAdapter();
                adapter.getFilter().filter(s.toString());
            }
        });
        listView = (ListView) findViewById(R.id.list_view);
        //If No feeds exist, show the error string.
        TextView emptyText = (TextView)findViewById(R.id.textView);
        listView.setEmptyView(emptyText);
        ReadRss readRss = new ReadRss(this, listView);
        readRss.execute();
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
