package org.lazisba.sizakat.fragments;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lazisba.sizakat.CompanyMap;
import org.lazisba.sizakat.Donasiku;
import org.lazisba.sizakat.KalkulatorZakat;
import org.lazisba.sizakat.LazisbaHome;
import org.lazisba.sizakat.ListPesertaBus;
import org.lazisba.sizakat.Profilku;
import org.lazisba.sizakat.R;
import org.lazisba.sizakat.RowItem;
import org.lazisba.sizakat.SiZakatApp;
import org.lazisba.sizakat.LazisbaHome.PlaceholderFragment;
import org.lazisba.sizakat.dialogs.Login_DlgLogin;
import org.lazisba.sizakat.util.SiZakatGlobal;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentSiZakat extends Fragment {

	private View view;
	private Activity parentCtx;

	private SiZakatApp currentApp;
	private int currentUserLevel = 0;
	
	
	public FragmentSiZakat() {
		
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
		
        currentApp = ((SiZakatApp) getActivity().getApplication());
        parentCtx = this.getActivity();
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_activity, container, false);
	    	
			updateLoginStatus();
	    	Log.d("Tab Sizakat: ", "OnCreateView");
	    	final Button buttonKalk = (Button) view.findViewById(R.id.sizakat_kalkzakat);
	    	buttonKalk.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Intent intent = new Intent(getActivity().getBaseContext(), KalkulatorZakat.class);
					startActivity(intent);
	            }
	        });
	    	final Button buttonProfilku = (Button) view.findViewById(R.id.sizakat_profilku);
	    	buttonProfilku.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	loadUserProfileData();
	            }
	        });
	    	final Button buttonViewSIZ = (Button) view.findViewById(R.id.sizakat_opensizonline);
	    	buttonViewSIZ.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Uri uri = Uri.parse("http://siz.lazisba.org/login.php");
	                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	                startActivity(intent);
	            }
	        });
	    	final Button buttonLogin = (Button) view.findViewById(R.id.sizakat_btn_login);
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
	    	final Button btnDonasiku = (Button) view.findViewById(R.id.sizakat_donasiku);
	    	btnDonasiku.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Intent intent = new Intent(getActivity().getBaseContext(), Donasiku.class);
					startActivity(intent);
	            }
	        });
	    	final Button buttonListBus = (Button) view.findViewById(R.id.sizakat_daftarbus);
	    	buttonListBus.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	final Context parentCtx = getActivity();
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
	    	        		((SiZakatApp) getActivity().getApplication()).loginState.getToken());
	    	        params.put(SiZakatGlobal.PREFS_UID,
	    	        		((SiZakatApp) getActivity().getApplication()).loginState.getUid());
	                
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
	                       		Log.v("JSON Response:", strJSON);
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
	                               Intent intent = new Intent(getActivity().getBaseContext(), ListPesertaBus.class);
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
		} else {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        return view;
	}
	
	public void loadUserProfileData() {
		final AsyncHttpClient client = new AsyncHttpClient();
		
    	// ======= Set up progress bar
    	final ProgressDialog prgDialog;
    	prgDialog = new ProgressDialog(parentCtx);
        prgDialog.setMessage("Memuat data profil...");
        prgDialog.setCancelable(true);
        prgDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (client != null) {
					client.cancelRequests(parentCtx, true);
				}
			}
        });
        
    	// Put Http parameter username with value of Email Edit View control
        RequestParams params = new RequestParams();
        params.put("apiKey", SiZakatGlobal.APP_APIKEY);
        params.put("appVer", SiZakatGlobal.APP_VERSION);
        params.put(SiZakatGlobal.PREFS_UTOKEN,
        		((SiZakatApp) getActivity().getApplication()).loginState.getToken());
        params.put(SiZakatGlobal.PREFS_UID,
        		((SiZakatApp) getActivity().getApplication()).loginState.getUid());
        
        prgDialog.show();
        
        // Make RESTful webservice call using AsyncHttpClient object
        client.get(SiZakatGlobal.getRESTUrl("profilku"),params ,new AsyncHttpResponseHandler() {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                prgDialog.dismiss();
                Toast.makeText(parentCtx, "Terjadi kesalahan... Silakan coba lagi.", Toast.LENGTH_LONG).show();
            }
			@Override
			// When the response returned by REST has Http response code '200'
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				prgDialog.dismiss();
				String strJSON = new String(response);
				try {
				   // JSON Object
				   JSONObject obj = new JSONObject(strJSON);
				   Log.v("JSON Response:", strJSON);
				   // When the JSON response has status boolean value assigned with true
				   if(!obj.has("error")){
					    JSONObject dataBody = obj.getJSONObject("data");
					   
					    Intent intent = new Intent(parentCtx, Profilku.class);
					    
					    intent.putExtra(Profilku.ID_ARG_FULLNAME, dataBody.getString("nama"));
					    intent.putExtra(Profilku.ID_ARG_EMAIL, dataBody.getString("email"));
					    intent.putExtra(Profilku.ID_ARG_TEMPATLHR, dataBody.getString("tempat_lahir"));
					    intent.putExtra(Profilku.ID_ARG_TGLLAHIR, dataBody.getString("tanggal_lahir"));
					    intent.putExtra(Profilku.ID_ARG_KOTA, dataBody.getString("kota"));
					    intent.putExtra(Profilku.ID_ARG_ALAMAT, dataBody.getString("alamat"));
					    intent.putExtra(Profilku.ID_ARG_KONTAK, dataBody.getString("hp"));
					    intent.putExtra(Profilku.ID_ARG_PEKERJAAN, dataBody.getString("pekerjaan"));
       				    startActivity(intent);
					   
				   }
				   // Else display error message
				   else{
					       //errorMsg.setText(obj.getString("error_msg"));
					   Toast.makeText(parentCtx, obj.getString("error"), Toast.LENGTH_LONG).show();
				   }
				} catch (JSONException e) {
					Toast.makeText(parentCtx, "Respons error... Silakan coba lagi.", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				} catch (Exception e) {
					Toast.makeText(parentCtx, "Internal error.", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			} // end onSuccess
        }); // end get
	}
	
	public void updateLoginStatus() {
		// == If logged in...
    	if ((currentApp.loginState.isLoggedIn()) && (view != null)) {
    		currentUserLevel = currentApp.loginState.getUserLevel();
    		
    		final ViewGroup sizLogin = (ViewGroup) view.findViewById(R.id.sizakat_cnt_loggedin);
    		final ViewGroup sizLogoff = (ViewGroup) view.findViewById(R.id.sizakat_cnt_notloggedin);
    		
    		sizLogin.setVisibility(View.VISIBLE);
    		sizLogoff.setVisibility(View.GONE);
    		((TextView) view.findViewById(R.id.sizakat_loginstat_in))
    			.setText("Login sebagai "+
    					currentApp.loginState.getFullname());
    		
    		final Button sizListBUS = (Button) view.findViewById(R.id.sizakat_daftarbus);
    		// Jika user adalah donatur BUS atau Admin
    		if ((currentUserLevel == 3) || (currentUserLevel == 99)) {
    			// Tampilkan menu list BUS
    			sizListBUS.setVisibility(View.VISIBLE);
    		} else {
    			sizListBUS.setVisibility(View.GONE);
    		}
    	}
	}
}
