package org.lazisba.sizakat;

import java.util.ArrayList;
import java.util.List;

import org.lazisba.sizakat.adapters.CustomListViewAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListPilihPesertaBus extends ActionBarActivity {

	public ListPilihPesertaBus() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_peserta_bus);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.act_listbus_container, new PilihBusFragment()).commit();
		}
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PilihBusFragment extends Fragment {

		public static final String[] titles = new String[] { "Ahmad Toyyib",
	            "Arifian", "Wahyu", "Wahyuni" };
	 
        public static final String[] descriptions = new String[] {
        	"MA Negeri 1",
            "MTS Sultan Agung",
            "MA Negeri 2",
            "MTS Ksatrian" };
 
        public static final Integer[] images = { R.drawable.emptyportrait,
            R.drawable.emptyportrait, R.drawable.emptyportrait, R.drawable.emptyportrait };
	        
		public PilihBusFragment() {
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
            
            Button buttonTambah = (Button) rootView.findViewById(R.id.frag_listbus_newbus);
            buttonTambah.setVisibility(View.GONE);
			return rootView;
		}
		
		private class BUSONItemClickListener implements OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
		            long id) {
				// DialogFragment.show() will take care of adding the fragment
			    // in a transaction.  We also want to remove any currently showing
			    // dialog, so make our own transaction and take care of that here.
			    FragmentTransaction ft = getFragmentManager().beginTransaction();
			    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
			    if (prev != null) {
			        ft.remove(prev);
			    }
			    ft.addToBackStack(null);

			    // Create and show the dialog.
			    DialogFragment newFragment = FragmentDetilBus.newInstance(1);
			    newFragment.show(ft, "dialog");

		    }
			
		}
		
	} // end class PlaceholderFragment

}
