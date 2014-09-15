package org.lazisba.sizakat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.lazisba.sizakat.MainActivity.MenuFragment;
import org.lazisba.sizakat.adapters.CustomListViewAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LazisbaHome extends ActionBarActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lazisba_home);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i)).setIcon(R.drawable.tab_ico_home)
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lazisba_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this.getBaseContext(), AccountSetting.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public void jumpToPage(int pageId) {
		mViewPager.setCurrentItem(pageId);
	}
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
	    
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
	 
		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			
			if (position == 1) {
				Log.d(getPackageName(), "Position = 1!");
				RssFragment fragment = new RssFragment();
                return fragment;
			} else return PlaceholderFragment.newInstance(position + 1);
			//return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "Beranda";//getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return "Berita";//getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return "Laporan";//getString(R.string.title_section2).toUpperCase(l);
			case 3:
				return "SiZakat";//getString(R.string.title_section3).toUpperCase(l);
			case 4:
				return "Profil";//getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;
			
            int i = getArguments().getInt(ARG_SECTION_NUMBER);
            if (i == 1) {
            	rootView = inflater.inflate(R.layout.fragment_home, container, false);
            	final Button buttonKalkHome = (Button) rootView.findViewById(R.id.frag_home_kalkzakat);
            	buttonKalkHome.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	Intent intent = new Intent(PlaceholderFragment.this.getActivity().getBaseContext(), KalkulatorZakat.class);
        				startActivity(intent);
                    }
                });
            	final Button buttonKas = (Button) rootView.findViewById(R.id.frag_home_viewkas);
            	buttonKas.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	((LazisbaHome)getActivity()).jumpToPage(2);
                    }
                });
            } else if (i==3) {
            	rootView = inflater.inflate(R.layout.fragment_laporankeuangan, container, false);
            } else if (i==2) {
            	rootView = inflater.inflate(R.layout.fragment_news, container, false);
            	/*
            	ListView listView;
                List<RowItem> rowItems;
                
            	rowItems = new ArrayList<RowItem>();
               // for (int i1 = 0; i1 < titles.length; i1++) {
                    RowItem item = new RowItem(R.drawable.emptyportrait,"Nama", "Sekolah Test");
                    rowItems.add(item);
                //}
                
                listView = (ListView) rootView.findViewById(R.id.frg_news_listv);
                CustomListViewAdapter adapter = new CustomListViewAdapter(this.getActivity(),
                        R.layout.listitem_anakbus, rowItems);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new BUSONItemClickListener());
                */
            } else if (i==5) {
            	rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            	final Button button = (Button) rootView.findViewById(R.id.frag_profile_viewmap);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	Intent intent = new Intent(PlaceholderFragment.this.getActivity().getBaseContext(), CompanyMap.class);
        				startActivity(intent);
                    }
                });

            } else if (i==4) {
            	rootView = inflater.inflate(R.layout.fragment_activity, container, false);
            	//((TextView) rootView.findViewById(R.id.fragment_label)).setText("Menu #"+i+" aktif!");
            	final Button buttonKalk = (Button) rootView.findViewById(R.id.sizakat_kalkzakat);
            	buttonKalk.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	Intent intent = new Intent(PlaceholderFragment.this.getActivity().getBaseContext(), KalkulatorZakat.class);
        				startActivity(intent);
                    }
                });
            	final Button buttonListBus = (Button) rootView.findViewById(R.id.sizakat_daftarbus);
            	buttonListBus.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	Context context = getActivity().getApplicationContext();
                    	CharSequence text = "Hello toast!";
                    	int duration = Toast.LENGTH_SHORT;

                    	Toast toast = Toast.makeText(context, text, duration);
                    	toast.show();
                    }
                });
            }
            
            return rootView;
			//View rootView = inflater.inflate(R.layout.fragment_lazisba_home,
			//		container, false);
			//return rootView;
		} // end procedure
		
		/*
		private class ViewKasListener implements OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
		            long id) {
				LazisbaHome.this.mViewPager.setCurrentItem(2);
				//Intent intent = new Intent(PlaceholderFragment.this.getActivity().getBaseContext(), DetilAnakBUS.class);
				//startActivity(intent);
		    }
			
		}*/
	}

}
