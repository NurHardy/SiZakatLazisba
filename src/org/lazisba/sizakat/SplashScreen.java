package org.lazisba.sizakat;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.lazisba.sizakat.dialogs.Login_DlgLogin;
import org.lazisba.sizakat.util.SiZakatGlobal;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreen extends FragmentActivity implements Login_DlgLogin.OnCompleteListener {

	private static int SPLASH_TIME_OUT = 1500;
	private static ViewGroup progressBox;
	private static TextView loadingStatusText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash_screen);
		
		progressBox = (ViewGroup) findViewById(R.id.splash_loadingContent);
		loadingStatusText = (TextView) findViewById(R.id.splash_txtLoading);
		new Handler().postDelayed(new Runnable() {
			 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	progressBox.setVisibility(View.VISIBLE);
            	SharedPreferences settings = getSharedPreferences(SiZakatGlobal.PREFS_NAME, 0);
            	if (settings.contains(SiZakatGlobal.PREFS_UTOKEN) &&
            			settings.contains(SiZakatGlobal.PREFS_UID) &&
            			settings.contains(SiZakatGlobal.PREFS_UFULLNAME) &&
            			settings.contains(SiZakatGlobal.PREFS_ULEVEL)) {
            		
            		String userToken = settings.getString(SiZakatGlobal.PREFS_UTOKEN, "token");
            		String userFullname = settings.getString(SiZakatGlobal.PREFS_UFULLNAME, "Unnamed");
                    int userId = settings.getInt(SiZakatGlobal.PREFS_UID, 0);
                    int userLevel = settings.getInt(SiZakatGlobal.PREFS_ULEVEL, 0);
                    ((SiZakatApp) SplashScreen.this.getApplication()).
                    	loginState.saveSession(userId, userToken, userFullname, userLevel);
                    
                    Log.d("Auth","Pref found! : "+userToken+"|"+userFullname);
                    //Toast.makeText(SplashScreen.this, userToken, Toast.LENGTH_LONG).show();
                    checkToken();
            	} else {
            		showLoginForm();
            	}

            } // end void run
        }, SPLASH_TIME_OUT);
	}
	
	// Oncomplete untuk login
	@Override
	public void onCompleteLogin(int status) {
		// TODO Auto-generated method stub
		finishSplashScreen();
	}
	
	private void showLoginForm() {
		FragmentTransaction ft = SplashScreen.this.getSupportFragmentManager().beginTransaction();
	    Fragment prev = getSupportFragmentManager().findFragmentByTag("sizakat_login");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);

	    // Create and show the dialog.
	    DialogFragment newFragment = new Login_DlgLogin();
	    newFragment.show(ft, "sizakat_login");
	}
	
	private void finishSplashScreen() {
		Intent intent = new Intent(SplashScreen.this.getBaseContext(), LazisbaHome.class);
        
        startActivity(intent);
        finish();
	}
	
	private void checkToken() {
		// Put Http parameter username with value of Email Edit View control
		loadingStatusText.setText("Authenticating...");
		
        RequestParams params = new RequestParams();
        params.put("apiKey", SiZakatGlobal.APP_APIKEY);
        params.put("appVer", SiZakatGlobal.APP_VERSION);
        params.put(SiZakatGlobal.PREFS_UTOKEN,
        		((SiZakatApp) SplashScreen.this.getApplication()).loginState.getToken());
        params.put(SiZakatGlobal.PREFS_UID,
        		((SiZakatApp) SplashScreen.this.getApplication()).loginState.getUid());
        
        Log.d("CekToken","Saved token:|"+((SiZakatApp) SplashScreen.this.getApplication()).loginState.getToken());
        // Make RESTful webservices call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(SiZakatGlobal.getRESTUrl("check_token"),params ,new AsyncHttpResponseHandler() {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            	Toast.makeText(SplashScreen.this, "Autentikasi gagal.", Toast.LENGTH_LONG).show();
            	((SiZakatApp) SplashScreen.this.getApplication()).loginState.destroySession();
                finishSplashScreen();
            }
			@Override
			// When the response returned by REST has Http response code '200'
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				// TODO Auto-generated method stub
				String strJSON = new String(response);
				Log.v("TokenCheck", strJSON);
				try {
                   // JSON Object
                   JSONObject obj = new JSONObject(strJSON);
                   if (obj.get("result").equals("ok")) {
                	   finishSplashScreen();
                   } else {
                	   showLoginForm();
                	   return;
                   }
			    } catch (JSONException e) {
				   Toast.makeText(SplashScreen.this, "Terjadi kesalahan saat autentifikasi...", Toast.LENGTH_LONG).show();
				   ((SiZakatApp) SplashScreen.this.getApplication()).loginState.destroySession();
			       e.printStackTrace();
			    }
				finishSplashScreen();
			}
        }); // end get
	}

}
