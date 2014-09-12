package org.lazisba.sizakat;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RssAdapter extends BaseAdapter {
	 
    private final List<RssItem> items;
    private final Context context;
 
    public RssAdapter(Context context, List<RssItem> items) {
        this.items = items;
        this.context = context;
    }
 
    @Override
    public int getCount() {
        return items.size();
    }
 
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }
 
    @Override
    public long getItemId(int id) {
        return id;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.listitem_anakbus, null);
            holder = new ViewHolder();
            holder.itemTitle = (TextView) convertView.findViewById(R.id.txtViewJudul);
            holder.itemDate = (TextView) convertView.findViewById(R.id.txtViewSubJudul);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemTitle.setText(items.get(position).getTitle());
        holder.itemDate.setText(items.get(position).getDate());
        return convertView;
    }
 
    static class ViewHolder {
        TextView itemTitle;
        TextView itemDate;
    }
}
