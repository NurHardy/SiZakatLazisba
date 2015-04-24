package org.lazisba.sizakat;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lazisba.sizakat.util.SiZakatGlobal;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Profilku extends ActionBarActivity {
	public static final String ID_ARG_FULLNAME	= "idArgFullName";
	public static final String ID_ARG_EMAIL		= "idArgEmail";
	public static final String ID_ARG_TGLLAHIR	= "idArgTglLhr";
	public static final String ID_ARG_TEMPATLHR	= "idArgTmpLhr";
	public static final String ID_ARG_KOTA		= "idArgKota";
	public static final String ID_ARG_ALAMAT	= "idArgAlamat";
	public static final String ID_ARG_KONTAK	= "idArgKontak";
	public static final String ID_ARG_PEKERJAAN	= "idArgPekerjaan";
	
	private String accFullName;
	private String accEmail;
	private String accTempatLahir;
	private String accTglLahir;
	private String accKota;
	private String accAlamat;
	private String accKontak;
	private String accPekerjaan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profilku);
		
		Intent activityIntent = this.getIntent();
		
		accFullName = activityIntent.getStringExtra(ID_ARG_FULLNAME);
		((EditText) this.findViewById(R.id.profilku_txtNama)).setText(accFullName);
		
		accEmail = activityIntent.getStringExtra(ID_ARG_EMAIL);
		((EditText) this.findViewById(R.id.profilku_txtEmail)).setText(accEmail);
		
		accTempatLahir = activityIntent.getStringExtra(ID_ARG_TEMPATLHR);
		((EditText) this.findViewById(R.id.profilku_txtTL)).setText(accTempatLahir);
		
		accTglLahir = activityIntent.getStringExtra(ID_ARG_TGLLAHIR);
		((EditText) this.findViewById(R.id.profilku_txtTanggalLahir)).setText(accTglLahir);
		
		accKota = activityIntent.getStringExtra(ID_ARG_KOTA);
		((EditText) this.findViewById(R.id.profilku_txtKota)).setText(accKota);
		
		accAlamat = activityIntent.getStringExtra(ID_ARG_ALAMAT);
		((EditText) this.findViewById(R.id.profilku_txtAlamat)).setText(accAlamat);
		
		accKontak = activityIntent.getStringExtra(ID_ARG_KONTAK);
		((EditText) this.findViewById(R.id.profilku_txtContact)).setText(accKontak);
		
		accPekerjaan = activityIntent.getStringExtra(ID_ARG_PEKERJAAN);
		((EditText) this.findViewById(R.id.profilku_txtPekerjaan)).setText(accPekerjaan);
		
		final Button btnSubmit = (Button) findViewById(R.id.profilku_btnSubmit);
		btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	saveProfileData();
            }
        });
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void saveProfileData() {
		final AsyncHttpClient client = new AsyncHttpClient();
		
    	// ======= Set up progress bar
    	final ProgressDialog prgDialog;
    	prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Menyimpan data...");
        prgDialog.setCancelable(true);
        prgDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (client != null) {
					client.cancelRequests(Profilku.this, true);
				}
			}
        });
        
        // Fill the variables
        accFullName		= ((EditText) this.findViewById(R.id.profilku_txtNama)).getText().toString();
		accEmail		= ((EditText) this.findViewById(R.id.profilku_txtEmail)).getText().toString();
		accTempatLahir	= ((EditText) this.findViewById(R.id.profilku_txtTL)).getText().toString();
		accTglLahir		= ((EditText) this.findViewById(R.id.profilku_txtTanggalLahir)).getText().toString();
		accKota			= ((EditText) this.findViewById(R.id.profilku_txtKota)).getText().toString();
		accAlamat		= ((EditText) this.findViewById(R.id.profilku_txtAlamat)).getText().toString();
		accKontak		= ((EditText) this.findViewById(R.id.profilku_txtContact)).getText().toString();
		accPekerjaan	= ((EditText) this.findViewById(R.id.profilku_txtPekerjaan)).getText().toString();
        
    	// Put Http parameter username with value of Email Edit View control
        RequestParams params = new RequestParams();
        params.put("apiKey"			, "informatikaundip");
        params.put("user_fullname"	, accFullName);
        params.put("user_email"		, accEmail);
        params.put("user_ttl"		, accTempatLahir);
        params.put("user_tgl"		, accTglLahir);
        params.put("user_kota"		, accKota);
        params.put("user_alamat"	, accAlamat);
        params.put("user_kontak"	, accKontak);
        params.put("user_pekerjaan"	, accPekerjaan);
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        
        final Profilku view = this;
        client.post(SiZakatGlobal.getRESTUrl("profilku", "simpan"),params ,new AsyncHttpResponseHandler() {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // Hide Progress Dialog
                prgDialog.dismiss();
                Toast.makeText(Profilku.this, "Terjadi kesalahan... Silakan coba lagi.", Toast.LENGTH_LONG).show();
            }
			@Override
			// When the response returned by REST has Http response code '200'
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				prgDialog.dismiss();
				String strJSON = new String(response);
				Log.v("SaveProfile: JSON", strJSON);
				try {
				   // JSON Object
				   JSONObject obj = new JSONObject(strJSON);
				   // When the JSON response has status boolean value assigned with true
				   if(!obj.has("error")){
					   AlertDialog notif = new AlertDialog.Builder(Profilku.this)
		                .setTitle("Sukses")
		                .setMessage("Data profil Anda berhasil disimpan!")
		                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int which) { 
		                    	dialog.dismiss();
		                    	Profilku.this.onBackPressed();
		                    }
		                 })
		                .setIcon(R.drawable.icon_check_32)
		                .show();
				   } else { // Else display error message
					   Toast.makeText(Profilku.this, obj.getString("error"), Toast.LENGTH_LONG).show();
				   }
				} catch (JSONException e) {
					Toast.makeText(Profilku.this, "Respons error... Silakan coba lagi.", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				} catch (Exception e) {
					Toast.makeText(Profilku.this, "Internal error.", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			} // end onSuccess
        }); // end get
	} // End method
}
