package org.lazisba.sizakat;

import java.util.Calendar;

import org.lazisba.sizakat.dialogs.Login_DlgLogin;
import org.lazisba.sizakat.dialogs.Report_DlgMonth;
import org.lazisba.sizakat.fragments.FragmentLaporanKeuangan;
import org.lazisba.sizakat.fragments.FragmentSiZakat;
import org.lazisba.sizakat.util.SiZakatGlobal;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LazisbaHome extends ActionBarActivity implements
		ActionBar.TabListener, Login_DlgLogin.OnCompleteListener {

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
	private static View viewLaporan;
	private static View viewHome;
	
	private static LayoutInflater layoutInfaterLaporan;
	
	private Context parentCtx;
	private Menu mainMenu;
	
	private int currentMonth, currentYear;
	
	private static boolean loginShown = false;

	public final static String ID_ARG_CURRENTMONTH = "currentMonth";
	public final static String ID_ARG_CURRENTYEAR = "currentYear";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lazisba_home);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		
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
		/* for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					//.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setIcon(R.drawable.tab_ico_home)
					.setTabListener(this));
		} */
		actionBar.addTab(actionBar.newTab()
				.setIcon(R.drawable.tab_ico_home)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setIcon(R.drawable.ic_news)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setIcon(R.drawable.tab_ico_report)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setIcon(R.drawable.tab_ico_sizakat)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setIcon(R.drawable.tab_ico_about)
				.setTabListener(this));
		
		Calendar c = Calendar.getInstance();
		currentYear = c.get(Calendar.YEAR);
		currentMonth = c.get(Calendar.MONTH)+1;
		
		if (currentMonth == 1) {
			currentYear--; currentMonth = 12;
		}
		parentCtx = this;	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.mainMenu = menu;
		getMenuInflater().inflate(R.menu.lazisba_home, menu);
		if (!((SiZakatApp) this.getApplication()).loginState.isLoggedIn()) {
			MenuItem item = menu.findItem(R.id.home_menu_logout);
			item.setVisible(false);
		}
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
		} else if (id == R.id.home_menu_logout) {
			new AlertDialog.Builder(this)
	        .setTitle("Konfirmasi")
	        .setMessage("Disconnect akun?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        	@Override
	            public void onClick(DialogInterface arg0, int arg1) {
	        		((SiZakatApp) LazisbaHome.this.getApplication()).loginState.destroySession();
	        		
	        		SharedPreferences settings = LazisbaHome.this.getSharedPreferences(SiZakatGlobal.PREFS_NAME, 0);
         	        SharedPreferences.Editor editor = settings.edit();
         	        editor.remove(SiZakatGlobal.PREFS_UTOKEN);
         	        editor.remove(SiZakatGlobal.PREFS_UFULLNAME);
         	        editor.remove(SiZakatGlobal.PREFS_UID);
         	        editor.remove(SiZakatGlobal.PREFS_ULEVEL);
         	        editor.commit();
         	        
         	        LazisbaHome.this.updateLoginStatus();
	        	}
	        }).create().show();
			
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void updateLoginStatus() {
		/*FragmentSiZakat fragment =
    			(FragmentSiZakat) this.getSupportFragmentManager().findFragmentById(3131);
    	if (fragment != null)
    		fragment.updateLoginStatus();*/
    	
		Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + mViewPager.getCurrentItem());
	    // based on the current position you can then cast the page to the correct 
	    // class and call the method:
	    if (page != null) {
	         ((FragmentSiZakat)page).updateLoginStatus();   
	    } 
	    
    	MenuItem item = this.mainMenu.findItem(R.id.home_menu_logout);
    	if (!((SiZakatApp) this.getApplication()).loginState.isLoggedIn()) {
			item.setVisible(false);
		} else {
			item.setVisible(true);
		}
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
			} else if (position == 2) {
				FragmentLaporanKeuangan fragment =
						new FragmentLaporanKeuangan(currentMonth, currentYear);
				
				return fragment;
			} else if (position == 3) {
				FragmentSiZakat fragment = new FragmentSiZakat();
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
			//Locale l = Locale.getDefault();
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
            // =========== TAB HOME
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
            	final Button buttonCek = (Button) rootView.findViewById(R.id.frag_home_usertrans);
            	buttonCek.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	/*RequestParams params = new RequestParams();
                    	// Put Http parameter username with value of Email Edit View control
                        params.put("apiKey", "informatikaundip");
                        // Put Http parameter password with value of Password Edit Value control
                        params.put("password", "myp4ssw0rd");
                        
                    	RestfulUtility util =
                    			new RestfulUtility(PlaceholderFragment.this.getActivity());
                    	util.invokeWS(params);*/
                    	((LazisbaHome)getActivity()).jumpToPage(4);
                    }
                });
            	
        		
            // =========== TAB LAPORAN KEUANGAN
            } else if (i==3) {
            	layoutInfaterLaporan = inflater;
            	rootView = inflater.inflate(R.layout.fragment_laporankeuangan, container, false);
            	final TextView btnFilter = (TextView) rootView.findViewById(R.id.lblTitleReport);
            	btnFilter.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	FragmentTransaction ft = getFragmentManager().beginTransaction();
        			    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        			    if (prev != null) {
        			        ft.remove(prev);
        			    }
        			    ft.addToBackStack(null);

        			    // Create and show the dialog.
        			    DialogFragment newFragment = new Report_DlgMonth();
        			    Bundle bundle = new Bundle();
        			    bundle.putInt(LazisbaHome.ID_ARG_CURRENTMONTH, 4);
        			    bundle.putInt(LazisbaHome.ID_ARG_CURRENTYEAR, 2015);
        			    newFragment.setArguments(bundle);
        			    newFragment.show(ft, "dialog");
        			    
                    }
                });
            	viewLaporan = rootView;
            // =========== TAB BERITA TERBARU
            } else if (i==2) {
            	rootView = inflater.inflate(R.layout.fragment_news, container, false);
            	
            // =========== TAB COMPANY PROFILE
            } else if (i==5) {
            	rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            	final Button button = (Button) rootView.findViewById(R.id.frag_profile_viewmap);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	Intent intent = new Intent(PlaceholderFragment.this.getActivity().getBaseContext(), CompanyMap.class);
        				startActivity(intent);
                    }
                });

            // =========== TAB SIZAKAT
            } else if (i==4) {
            	
            }
            
            return rootView;
			//View rootView = inflater.inflate(R.layout.fragment_lazisba_home,
			//		container, false);
			//return rootView;
		} // end procedure
		
	} // end class PlaceholderFragment

	// Oncomplete untuk login
	@Override
	public void onCompleteLogin(int status) {
		updateLoginStatus();
	}
	
	public void changeCurrentReportTime(int cMonth, int cYear) {
		this.currentMonth = cMonth;
		this.currentYear = cYear;
	}
	
} // end class LazisbaHome
