package andrewduncan1200974.cm3019courseowrk;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * This class will process all of the URL's and will also Read the RSS data from xml.
 * Created by Andrew on 22/04/2016.
 */
public class ReadRss extends AsyncTask<Void, Void, Void> {

    Activity context;
    List<String> addresses;
    ProgressDialog progressDialog;
    URL url;
    ArrayList<FeedItem> feedItems;
    ListView listView;


    public ReadRss(Activity context, ListView listView){
        //Getting the list of URL's from SharedPreferences
        Map<String, ?> allKeyValues = PreferenceManager.getDefaultSharedPreferences(context).getAll();
        Iterator<String> urlsIterator = allKeyValues.keySet().iterator();
        //Creating a list of URL strings
        addresses = new ArrayList<>();
        while (urlsIterator.hasNext())
            addresses.add(urlsIterator.next());

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
        //Call and set MyAdapter, giving it the Activuty context and List of FeedItems
        MyAdapter adapter = new MyAdapter(context, feedItems);
        listView.setAdapter(adapter);
        //Allows the listView item to be clicked, this will take you to full article on website
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                final FeedItem current = feedItems.get(position);
                //start NewsDetails Activity
                Intent intent = new Intent(context, NewsDetails.class);
                intent.putExtra("Link", current.getLink());
                context.startActivity(intent);
            }
        });
    }

    /*For each XML Document in the ArrayList returned from getData() call the ProcessXml method to
      return the information rrom that xml file in a meaningful way.
     */
    @Override
    protected Void doInBackground(Void... params) {
        feedItems = new ArrayList<>();

            for (Document url : GetData()) {
                ProcessXml(url);
            }
        return null;
    }

    //This method will parse all of the xml data into usable information.
    private void ProcessXml(Document data) {
        if(data!=null) {

           org.w3c.dom.Element root=data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items=channel.getChildNodes();
            //retrieve all inforamtion surrounded within <Item> tags
            for(int i=0; i<items.getLength(); i++){
                Node currentchild=items.item(i);
                if(currentchild.getNodeName().equalsIgnoreCase("item")){
                    //Create new feed item object.
                    FeedItem item = new FeedItem();
                    NodeList itemchilds = currentchild.getChildNodes();
                    //for each item in the xml file, return its title, description, publication date and url
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
                    //Adding FeedItems into the feedItems arrayList
                    feedItems.add(item);
                    //Testing which shows the information in the log
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
        //Loop through all URL's taken from sharedPreferences
        for(int i = 0; i < addresses.size(); i++){
            try {
                //Connect to website and retrieve all XML info
                url = new URL(addresses.get(i));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                //Add all XML info into a Document
                InputStream inputStream = connection.getInputStream();
                DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                Document xmlDoc = builder.parse(inputStream);
                //Add this document to the ArrayList of XML Documents.
                urls.add(xmlDoc);
            } catch (Exception e) {
                //this will trigger if a bad url was added, and it will continue the for loop.
                e.printStackTrace();
            }
        }
        //return ArrayList of XML Documents so the can be parsed by ProcessXML()
        return urls;
    }

}
