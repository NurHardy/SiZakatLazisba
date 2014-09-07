package org.lazisba.sizakat;

import java.util.ArrayList;
import java.util.List;

import org.lazisba.sizakat.adapters.CustomListViewAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
		 
	private String[] mPlanetTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private LinearLayout mLinearLayout;
	private CharSequence mTitle;
	private ActionBarDrawerToggle mDrawerToggle;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 
		mTitle = "Sistem Informasi Zakat";
		 
		mPlanetTitles = new String[]{"Laporan Keuangan", "Laporan Donasi", "Daftar BUS", "Logout"};
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawerList = (ListView) findViewById(R.id.left_menu);
		 
		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_listitem, mPlanetTitles));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		 
		mDrawerToggle = new ActionBarDrawerToggle(
			this, /* host Activity */
			mDrawerLayout, /* DrawerLayout object */
			R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
			R.string.drawer_open, /* "open drawer" description */
			R.string.drawer_close /* "close drawer" description */
			) {
		 
				/** Called when a drawer has settled in a completely closed state. */
				public void onDrawerClosed(View view) {
					getSupportActionBar().setTitle(mTitle);
				}
				 
				/** Called when a drawer has settled in a completely open state. */
				public void onDrawerOpened(View drawerView) {
					getSupportActionBar().setTitle(mTitle);
				}
			};
		 
		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// == Mendapatkan e-mail user yang login
		TextView txtViewUser = (TextView) findViewById(R.id.lblUserName);
		String mEmail = getIntent().getStringExtra(LoginActivity.EXTRA_EMAIL);
		txtViewUser.setText(mEmail);
		
		// == Set fragmen menjadi fragmen home
		Fragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt(MenuFragment.MENU_ID, -1);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	 
	 
	}
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}
	 
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...
		 
		return super.onOptionsItemSelected(item);
		}
	 
	@Override
	public void onBackPressed() {
		mDrawerLayout.openDrawer(mLinearLayout);
		//Toast.makeText(this, "Back button pressed!", Toast.LENGTH_SHORT).show();
	}
	/**
	* Swaps fragments in the main content view
	*/
	private void selectItem(int position) {
		if (position == 3) { // logout
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage("Logout dari sesi?")
		        .setCancelable(true)
		        .setPositiveButton("Yes",
			        new DialogInterface.OnClickListener() {
			            // On
			            // clicking
			            // "Yes"
			            // button
		
			            public void onClick(DialogInterface dialog,int id) {
			            	// Proses logout di sini...
			            	System.out.println(" onClick ");
			                finish();
			            }
			        })
		        .setNegativeButton("No",
		        new DialogInterface.OnClickListener() {
		            // On
		            // clicking
		            // "No"
		            // button
		            public void onClick(DialogInterface dialog,int id) {
		                dialog.cancel();
		            }
		        });

		    AlertDialog alert = builder.create();
		    alert.show();
			return;
		}
		//Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();
		
		Fragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt(MenuFragment.MENU_ID, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		//mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mLinearLayout);
	}
	 
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}
	 
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}
	
	/**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class MenuFragment extends Fragment {
        public static final String MENU_ID = "menu_id";
        
        public static final String[] titles = new String[] { "Ahmad Toyyib",
            "Arifian", "Wahyu", "Wahyuni" };
 
        public static final String[] descriptions = new String[] {
        	"MA Negeri 1",
            "MTS Sultan Agung",
            "MA Negeri 2",
            "MTS Ksatrian" };
 
        public static final Integer[] images = { R.drawable.emptyportrait,
            R.drawable.emptyportrait, R.drawable.emptyportrait, R.drawable.emptyportrait };
    
        public MenuFragment() {
            // Empty constructor required for fragment subclasses
        }

		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView;
            int i = getArguments().getInt(MENU_ID);
            if (i < 0) {
            	rootView = inflater.inflate(R.layout.fragment_home, container, false);
            } else if (i==0) {
            	rootView = inflater.inflate(R.layout.fragment_laporankeuangan, container, false);
            } else if (i==2) {
            	rootView = inflater.inflate(R.layout.fragment_daftaranakbus, container, false);
            	
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
                
			} else {
            	rootView = inflater.inflate(R.layout.fragment_activity, container, false);
            	((TextView) rootView.findViewById(R.id.fragment_label)).setText("Menu #"+i+" aktif!");
            }
            
           // String planet = getResources().getStringArray(R.array.planets_array)[i];
            return rootView;
        }
		
		private class BUSONItemClickListener implements OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
		            long id) {
				Intent intent = new Intent(MenuFragment.this.getActivity().getBaseContext(), DetilAnakBUS.class);
				startActivity(intent);
				
		        /*Toast toast = Toast.makeText(getApplicationContext(),
		            "Item " + (position + 1) + ": " + rowItems.get(position),
		            Toast.LENGTH_SHORT);
		        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		        toast.show();*/
		    }
			
		}
    }
}