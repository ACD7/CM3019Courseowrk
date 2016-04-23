package andrewduncan1200974.cm3019courseowrk;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 22/04/2016.
 */
public class MyAdapter extends ArrayAdapter<FeedItem> implements Filterable{

    private List<FeedItem> list;
    private final Activity context;

    private List<FeedItem> filterResults;

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
    public FeedItem getItem(int position) {
        return filterResults != null ? filterResults.get(position) : list.get(position);
    }

    @Override
    public int getCount() {
        return filterResults != null ? filterResults.size() : list.size();
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
        FeedItem item = getItem(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.pubDate.setText(item.getPubDate());
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<FeedItem> filteredItems = new ArrayList<>();
                constraint = constraint.toString().toLowerCase();
                if (constraint.length() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).containsKeyword(constraint.toString())) {
                            filteredItems.add(list.get(i));
                        }
                    }
                }
                filterResults.count = filteredItems.size();
                filterResults.values = filteredItems;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterResults = (List<FeedItem>)results.values;
                //We have no filter
                if (constraint.length() == 0) {
                    filterResults = null;
                }
                notifyDataSetChanged();
            }
        };
    }
}
