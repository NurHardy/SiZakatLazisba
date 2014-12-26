package org.lazisba.sizakat;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lazisba.sizakat.dialogs.Login_DlgLogin;
import org.lazisba.sizakat.dialogs.Report_DlgMonth;
import org.lazisba.sizakat.util.SiZakatGlobal;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LazisbaHome extends ActionBarActivity implements
		ActionBar.TabListener, Report_DlgMonth.OnCompleteListener,
		Login_DlgLogin.OnCompleteListener {

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
	private static boolean loginShown = false;

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
		} else if (id == R.id.home_menu_logout) {
			((SiZakatApp) this.getApplication()).loginState.destroySession();
			
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
            	rootView = inflater.inflate(R.layout.fragment_laporankeuangan, container, false);
            	
            	final Button btnFilter = (Button) rootView.findViewById(R.id.frag_laporanKauangan_filter);
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
            	rootView = inflater.inflate(R.layout.fragment_activity, container, false);
            	//((TextView) rootView.findViewById(R.id.fragment_label)).setText("Menu #"+i+" aktif!");
            	
            	if (((SiZakatApp) PlaceholderFragment.this.getActivity().getApplication()).loginState.isLoggedIn()) {
            		final ViewGroup sizLogin = (ViewGroup) rootView.findViewById(R.id.sizakat_cnt_loggedin);
            		final ViewGroup sizLogoff = (ViewGroup) rootView.findViewById(R.id.sizakat_cnt_notloggedin);
            		
            		sizLogin.setVisibility(View.VISIBLE);
            		sizLogoff.setVisibility(View.GONE);
            		((TextView) rootView.findViewById(R.id.sizakat_txt_logininfo))
            			.setText("Login sebagai "+
            				((SiZakatApp) PlaceholderFragment.this.getActivity().getApplication()).loginState.getFullname());
            	}
            	Log.d("Tab Sizakat: ", "OnCreateView");
            	final Button buttonKalk = (Button) rootView.findViewById(R.id.sizakat_kalkzakat);
            	buttonKalk.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	Intent intent = new Intent(PlaceholderFragment.this.getActivity().getBaseContext(), KalkulatorZakat.class);
        				startActivity(intent);
                    }
                });
            	final Button buttonViewSIZ = (Button) rootView.findViewById(R.id.sizakat_opensizonline);
            	buttonViewSIZ.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	Uri uri = Uri.parse("http://siz.lazisba.org/login.php");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            	final Button buttonLogin = (Button) rootView.findViewById(R.id.sizakat_btn_login);
            	buttonLogin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	FragmentTransaction ft = getFragmentManager().beginTransaction();
                	    Fragment prev = getFragmentManager().findFragmentByTag("sizakat_login");
                	    if (prev != null) {
                	        ft.remove(prev);
                	    }
                	    ft.addToBackStack(null);

                	    // Create and show the dialog.
                	    DialogFragment newFragment = new Login_DlgLogin();
                	    newFragment.show(ft, "sizakat_login");
                    }
                });
            	final Button btnDonasiku = (Button) rootView.findViewById(R.id.sizakat_donasiku);
            	btnDonasiku.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	Intent intent = new Intent(PlaceholderFragment.this.getActivity().getBaseContext(), Donasiku.class);
        				startActivity(intent);
                    }
                });
            	final Button buttonListBus = (Button) rootView.findViewById(R.id.sizakat_daftarbus);
            	buttonListBus.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	final Context parentCtx = PlaceholderFragment.this.getActivity();
                    	// ======= Set up progress bar
                    	final ProgressDialog prgDialog;
                    	prgDialog = new ProgressDialog(parentCtx);
                        prgDialog.setMessage("Memuat data...");
                        prgDialog.setCancelable(false);
                        
                    	// Put Http parameter username with value of Email Edit View control
                        RequestParams params = new RequestParams();
                        params.put("apiKey", SiZakatGlobal.APP_APIKEY);
            	        params.put("appVer", SiZakatGlobal.APP_VERSION);
            	        params.put(SiZakatGlobal.PREFS_UTOKEN,
            	        		((SiZakatApp) PlaceholderFragment.this.getActivity().getApplication()).loginState.getToken());
            	        params.put(SiZakatGlobal.PREFS_UID,
            	        		((SiZakatApp) PlaceholderFragment.this.getActivity().getApplication()).loginState.getUid());
                        
                        prgDialog.show();
                        // Make RESTful webservice call using AsyncHttpClient object
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get(SiZakatGlobal.getRESTUrl("peserta_bus"),params ,new AsyncHttpResponseHandler() {
                            
                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                                // Hide Progress Dialog
                                prgDialog.hide();
                                Toast.makeText(parentCtx, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                            }
               			@Override
               			// When the response returned by REST has Http response code '200'
               			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
               				// TODO Auto-generated method stub
               				prgDialog.hide();
                               try {
                               		String strJSON = new String(response);
                                        // JSON Object
                                   JSONObject obj = new JSONObject(strJSON);
                                   // When the JSON response has status boolean value assigned with true
                                   if(!obj.has("error")){
                                	   ArrayList<RowItem> daftarAnak = new ArrayList<RowItem>();
                                	   JSONArray jsonArrayAnak = obj.getJSONArray("data");
                                	   
                                	   int counter;
                                	   for (counter=0; counter < jsonArrayAnak.length(); counter++) {
                                		   JSONObject anak = jsonArrayAnak.getJSONObject(counter);
                                		   daftarAnak.add(new RowItem(1, anak.getString("nama"), anak.getString("sekolah")));
                                	   }
                                	   
                                       //Toast.makeText(parentCtx, obj.getString("result"), Toast.LENGTH_LONG).show();
                                       Intent intent = new Intent(PlaceholderFragment.this.getActivity().getBaseContext(), ListPesertaBus.class);
                                       intent.putParcelableArrayListExtra("LAZISBA_LIST_BUS", daftarAnak);
                                       startActivity(intent);
                                   }
                                   // Else display error message
                                   else{
                                       //errorMsg.setText(obj.getString("error_msg"));
                                       Toast.makeText(parentCtx, obj.getString("error"), Toast.LENGTH_LONG).show();
                                   }
                               } catch (JSONException e) {
                                   // TODO Auto-generated catch block
                                   Toast.makeText(parentCtx, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                                   e.printStackTrace();
                               }
               			}
                        }); // end get
                    } // end onClick
                });
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
		// TODO Auto-generated method stub
		
	}
	
	// Oncomplete untuk laporan keuangan
	@Override
	public void onComplete(int sM, int sY) {
		// TODO Auto-generated method stub
		final Context parentCtx = this;
    	// ======= Set up progress bar
    	final ProgressDialog prgDialog;
    	prgDialog = new ProgressDialog(parentCtx);
        prgDialog.setMessage("Memuat data laporan...");
        prgDialog.setCancelable(false);
        
    	// Put Http parameter username with value of Email Edit View control
        RequestParams params = new RequestParams();
        params.put("apiKey", "informatikaundip");
        params.put("report_month", sM);
        params.put("report_year", sY);
        
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(SiZakatGlobal.getRESTUrl("laporan_bulan"),params ,new AsyncHttpResponseHandler() {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // Hide Progress Dialog
                prgDialog.hide();
                Toast.makeText(parentCtx, "Terjadi kesalahan... Silakan coba lagi.", Toast.LENGTH_LONG).show();
            }
		@Override
		// When the response returned by REST has Http response code '200'
		public void onSuccess(int statusCode, Header[] headers, byte[] response) {
			// TODO Auto-generated method stub
			prgDialog.hide();
			String strJSON = new String(response);
			Log.v("LaporanBulan: JSON", strJSON);
			try {
			   // JSON Object
			   JSONObject obj = new JSONObject(strJSON);
			   // When the JSON response has status boolean value assigned with true
			   if(!obj.has("error")){
				   ((TextView) viewLaporan.findViewById(R.id.lblTitleReport)).setText(obj.getString("label"));
				   JSONObject reportBody = obj.getJSONObject("data");
				   JSONObject reportPemasukan = reportBody.getJSONObject("pemasukan");
				   JSONObject reportPenyaluran = reportBody.getJSONObject("penyaluran");
				   
				   ((TextView) viewLaporan.findViewById(R.id.txtKas)).setText(reportBody.getString("kas"));
				   ((TextView) viewLaporan.findViewById(R.id.txtPenerimaan)).setText(reportPemasukan.getString("total"));
				   ((TextView) viewLaporan.findViewById(R.id.txtPengeluaran)).setText(reportPenyaluran.getString("total"));
				   ((TextView) viewLaporan.findViewById(R.id.txtPengeluaran1)).setText(reportBody.getString("saldo"));
			   }
			   // Else display error message
			   else{
				       //errorMsg.setText(obj.getString("error_msg"));
				   Toast.makeText(parentCtx, obj.getString("error"), Toast.LENGTH_LONG).show();
			   }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			   Toast.makeText(parentCtx, "Respons error... Silakan coba lagi.", Toast.LENGTH_LONG).show();
			   e.printStackTrace();
			}
		}
        }); // end get
	}
} // end class LazisbaHome
