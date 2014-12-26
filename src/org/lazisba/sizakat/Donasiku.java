package org.lazisba.sizakat;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lazisba.sizakat.adapters.DonasikuTransactionAdapter;
import org.lazisba.sizakat.dialogs.Login_DlgLogin;
import org.lazisba.sizakat.util.SiZakatGlobal;
import org.lazisba.sizakat.util.TransactionItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Donasiku extends ActionBarActivity {

	private View progressView;
	private View offlineNotif;
	private TextView connStatus;
	private ListView listDonasi;
	
	private ArrayList<TransactionItem> listDonasiku = new ArrayList<TransactionItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donasiku);
		
		offlineNotif  = (ViewGroup) findViewById(R.id.donasiku_offlinenotif);
		progressView = findViewById(R.id.donasiku_loading);
		connStatus = (TextView) findViewById(R.id.donasiku_offline);
		listDonasi = (ListView) findViewById(R.id.donasiku_listDonasi);
		loadData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.donasiku, menu);
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
	//=========== Progress utility ==
	private void showError(String strErr) {
		listDonasi.setVisibility(View.GONE);
		progressView.setVisibility(View.GONE);
		
		connStatus.setText(strErr);
		offlineNotif.setVisibility(View.VISIBLE);
	}
	private void showList() {
		listDonasi.setVisibility(View.VISIBLE);
		progressView.setVisibility(View.GONE);
		offlineNotif.setVisibility(View.GONE);
	}
	// =================== LOAD DATA FUNCTION ====
	private void loadData() {
		if (((SiZakatApp) this.getApplication()).loginState.isLoggedIn()) {
			// Show loading...
			listDonasi.setVisibility(View.GONE);
			offlineNotif.setVisibility(View.GONE);
			progressView.setVisibility(View.VISIBLE);
			
			// Put Http parameter username with value of Email Edit View control
	        RequestParams params = new RequestParams();
	        params.put("apiKey", SiZakatGlobal.APP_APIKEY);
	        params.put("appVer", SiZakatGlobal.APP_VERSION);
	        params.put(SiZakatGlobal.PREFS_UTOKEN, ((SiZakatApp) this.getApplication()).loginState.getToken());
	        params.put(SiZakatGlobal.PREFS_UID, ((SiZakatApp) this.getApplication()).loginState.getUid());
	        
	        
	        // Make RESTful webservices call using AsyncHttpClient object
	        AsyncHttpClient client = new AsyncHttpClient();
	        client.get(SiZakatGlobal.getRESTUrl("donasiku"),params ,new AsyncHttpResponseHandler() {
	            
	            @Override
	            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
	                // Hide Progress Dialog
	            	showError("Gagal mengambil data.");
	            }
				@Override
				// When the response returned by REST has Http response code '200'
				public void onSuccess(int statusCode, Header[] headers, byte[] response) {
					// TODO Auto-generated method stub
					String strJSON = new String(response);
					Log.v("Donasiku", strJSON);
					try {
	                   // JSON Object
	                   JSONObject obj = new JSONObject(strJSON);
	                   // When the JSON response has status boolean value assigned with true
	                   if(!obj.has("error")){
	                	   JSONArray userTrx = obj.getJSONArray("data");
	                	   int i;
	                	   for (i=0; i< userTrx.length(); i++) {
	                		   JSONObject trxItem = (JSONObject) userTrx.get(i);
	                		   listDonasiku.add(new TransactionItem(
	                				   trxItem.getString("jenis"),
	                				   trxItem.getString("nominal"),
	                				   trxItem.getString("tanggal")));
	                		   
	                		   DonasikuTransactionAdapter adapter =
	                				   new DonasikuTransactionAdapter(Donasiku.this, listDonasiku);
	                		   listDonasi.setAdapter(adapter);
	                	   }
	                	   
	            	       showList();
	                   }
	                   // Else display error message
	                   else{
	                       showError(obj.getString("error"));
	                   }
	               } catch (JSONException e) {
	                   // TODO Auto-generated catch block
	            	   showError("Terjadi error");
	                   e.printStackTrace();
	               }
				}
	        }); // end get
		} else {
			showError("Anda belum login...");
		} // end if isn't logged in
    	
	} // end method loadData
}
