package andrewduncan1200974.cm3019courseowrk;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.renderscript.Element;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Andrew on 22/04/2016.
 */
public class ReadRss extends AsyncTask<Void, Void, Void> {
    Activity context;
    private ArrayAdapter<FeedItem> mAdapter;
    String[] addresses = {"http://feeds.bbci.co.uk/news/world/rss.xml"};//, "http://feeds.skynews.com/feeds/rss/uk.xml"};
    ProgressDialog progressDialog;
    URL url;
    ArrayList<FeedItem> feedItems;
    ListView listView;

    private static final int LOADER_ID = 1;
    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<List<FeedItem>> mCallbacks;
    private LoaderManager mLoaderManager;

    public ReadRss(Activity context, ListView listView){
        this.listView=listView;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        MyAdapter adapter = new MyAdapter(context, feedItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                final FeedItem current = feedItems.get(position);
                Intent intent = new Intent(context, NewsDetails.class);
                intent.putExtra("Link", current.getLink());
                context.startActivity(intent);
            }
        });
    }
    @Override
    protected Void doInBackground(Void... params) {
        for (Document url: GetData()) {
            ProcessXml(url);
        }
        return null;
    }

    private void ProcessXml(Document data) {
        if(data!=null) {
            feedItems = new ArrayList<>();
           org.w3c.dom.Element root=data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items=channel.getChildNodes();
            for(int i=0; i<items.getLength(); i++){
                Node currentchild=items.item(i);
                if(currentchild.getNodeName().equalsIgnoreCase("item")){
                    FeedItem item = new FeedItem();
                    NodeList itemchilds = currentchild.getChildNodes();
                    for(int j=0; j < itemchilds.getLength(); j++){
                        Node current = itemchilds.item(j);
                        if(current.getNodeName().equalsIgnoreCase("title")){
                            item.setTitle(current.getTextContent());
                        } else if(current.getNodeName().equalsIgnoreCase("description")){
                            item.setDescription(current.getTextContent());
                        } else if(current.getNodeName().equalsIgnoreCase("pubDate")) {
                            item.setPubDate(current.getTextContent());
                        } else if(current.getNodeName().equalsIgnoreCase("link")) {
                            item.setLink(current.getTextContent());
                        }
                    }
                    feedItems.add(item);
                    Log.d("itemTitle", item.getTitle());
                    Log.d("itemDescription", item.getDescription());
                    Log.d("itemLink", item.getLink());
                    Log.d("itemPubDate", item.getPubDate());
                }
            }
        }
    }

    public ArrayList<Document> GetData(){
        ArrayList<Document> urls = new ArrayList<>();
        for(int i = 0; i < addresses.length; i++){
            try {
                url = new URL(addresses[i]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                Document xmlDoc = builder.parse(inputStream);
                urls.add(xmlDoc);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return urls;
    }

}
