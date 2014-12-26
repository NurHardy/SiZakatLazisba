package org.lazisba.sizakat.adapters;

import java.util.List;

import org.lazisba.sizakat.R;
import org.lazisba.sizakat.util.TransactionItem;

import android.content.Context;
import android.text.TextUtils.StringSplitter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DonasikuTransactionAdapter extends BaseAdapter {

	private List<TransactionItem> trxItems;
	private Context context;
	
	public DonasikuTransactionAdapter(Context ctx, List<TransactionItem> items) {
		context = ctx;
		trxItems = items;
	}

	@Override
	public int getCount() {
		return trxItems.size();
	}

	@Override
	public TransactionItem getItem(int idx) {
		return trxItems.get(idx);
	}

	@Override
	public long getItemId(int idx) {
		return idx;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder;
        if (convertView == null) {
            convertView = (ViewGroup) View.inflate(context, R.layout.donasiku_transaction, null);
            holder = new ViewHolder();
            holder.itemTitle = (TextView) convertView.findViewById(R.id.item_trx_title);
            holder.itemDate = (TextView) convertView.findViewById(R.id.item_trx_subtitle);
            holder.itemAmount = (TextView) convertView.findViewById(R.id.item_trx_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemTitle.setText(trxItems.get(pos).getType());
        holder.itemDate.setText(trxItems.get(pos).getDate());
        holder.itemAmount.setText(trxItems.get(pos).getAmount());
        return convertView;
    }
 
    static class ViewHolder {
        TextView itemTitle;
        TextView itemDate;
        TextView itemAmount;
    }

}
