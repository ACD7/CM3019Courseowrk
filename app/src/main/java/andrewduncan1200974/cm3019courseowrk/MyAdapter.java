package andrewduncan1200974.cm3019courseowrk;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andrew on 22/04/2016.
 */
public class MyAdapter extends ArrayAdapter<FeedItem> {

    private final List<FeedItem> list;
    private final Activity context;


    public MyAdapter(Activity context, List<FeedItem> list) {
        super(context, R.layout.custom_row_news_item, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView title;
        protected TextView description;
        protected TextView pubDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.custom_row_news_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.title_text);
            viewHolder.description = (TextView) view.findViewById(R.id.description_text);
            viewHolder.pubDate = (TextView) view.findViewById(R.id.date_text);
            view.setTag(viewHolder);

        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getDescription());
        holder.pubDate.setText(list.get(position).getPubDate());
        return view;
    }
}
