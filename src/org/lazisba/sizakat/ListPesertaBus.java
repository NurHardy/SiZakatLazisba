package org.lazisba.sizakat;

import java.util.ArrayList;
import java.util.List;

import org.lazisba.sizakat.adapters.CustomListViewAdapter;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListPesertaBus extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_peserta_bus);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.act_listbus_container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_peserta_bus, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		 public static final String[] titles = new String[] { "Ahmad Toyyib",
	            "Arifian", "Wahyu", "Wahyuni" };
	 
        public static final String[] descriptions = new String[] {
        	"MA Negeri 1",
            "MTS Sultan Agung",
            "MA Negeri 2",
            "MTS Ksatrian" };
 
        public static final Integer[] images = { R.drawable.emptyportrait,
            R.drawable.emptyportrait, R.drawable.emptyportrait, R.drawable.emptyportrait };
	        
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_daftaranakbus, container, false);
			ListView listView;
            List<RowItem> rowItems;
            
        	rowItems = new ArrayList<RowItem>();
            for (int i1 = 0; i1 < titles.length; i1++) {
                RowItem item = new RowItem(images[i1], titles[i1], descriptions[i1]);
                rowItems.add(item);
            }
            
            listView = (ListView) rootView.findViewById(R.id.daftarAnakBUS);
            CustomListViewAdapter adapter = new CustomListViewAdapter(this.getActivity(),
                    R.layout.listitem_anakbus, rowItems);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new BUSONItemClickListener());
            
			return rootView;
		}
		
		private class BUSONItemClickListener implements OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
		            long id) {
				Intent intent = new Intent(PlaceholderFragment.this.getActivity().getBaseContext(), DetilAnakBUS.class);
				startActivity(intent);
				
		        /*Toast toast = Toast.makeText(getApplicationContext(),
		            "Item " + (position + 1) + ": " + rowItems.get(position),
		            Toast.LENGTH_SHORT);
		        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		        toast.show();*/
		    }
			
		}
		
	} // end class PlaceholderFragment
	
} // end class ListPesertaBus
