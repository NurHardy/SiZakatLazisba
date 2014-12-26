package org.lazisba.sizakat.dialogs;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.lazisba.sizakat.R;
import org.lazisba.sizakat.SiZakatApp;
import org.lazisba.sizakat.util.SiZakatGlobal;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Login_DlgLogin extends DialogFragment {
	public static interface OnCompleteListener {
	    public abstract void onCompleteLogin(int status);
	}

	private OnCompleteListener mListener;
	private Activity parentActivity;
	
	// make sure the Activity implemented it
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	    try {
	        this.mListener = (OnCompleteListener)activity;
	        parentActivity = activity;
	    }
	    catch (final ClassCastException e) {
	        throw new ClassCastException(activity.toString() + ": Must implement Listener...");
	    }
	}
	
	//@Override
	//public void onCreate() {
	//	this.getDialog().setCanceledOnTouchOutside(true);
	//}
	public Login_DlgLogin() {
		// TODO Auto-generated constructor stub
	}

	private void dialogOke(int hasilStatus) {
		this.mListener.onCompleteLogin(hasilStatus);
	}
	/*
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.fragment_dlg_loginform, null))
	    // Add action buttons
	           .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // sign in the user ...
	            	   String uName = ((TextView) v.findViewById(R.id.login_username)).getText().toString();
	            	   String uPass = ((TextView) v.findViewById(R.id.login_password)).getText().toString();
	            	   authenticate(uName, uPass);
	               }
	           })
	           .setNegativeButton(R.string.app_str_tryagain, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   Login_DlgLogin.this.getDialog().cancel();
	            	   dialogOke(0);
	               }
	           });      
	    return builder.create();
	}*/
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_dlg_loginform, container, false);
       
        Button buttonDo = (Button)v.findViewById(R.id.login_btn_dologin);
        //button.setVisibility(View.VISIBLE);
        buttonDo.setOnClickListener(new OnClickListener() {
			public void onClick(View btn) {
				String uName = ((TextView) v.findViewById(R.id.login_username)).getText().toString();
         	    String uPass = ((TextView) v.findViewById(R.id.login_password)).getText().toString();
         	    authenticate(uName, uPass);
            }
        });

        Button buttonDismiss = (Button)v.findViewById(R.id.login_btn_dismiss);
        //button.setVisibility(View.VISIBLE);
        buttonDismiss.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Login_DlgLogin.this.getDialog().cancel();
				dialogOke(0);
            }
        });
        return v;
    }
	
	private void authenticate(String uName, String uPass) {
		final Context parentCtx = parentActivity;
    	// ======= Set up progress bar
    	final ProgressDialog prgDialog;
    	prgDialog = new ProgressDialog(parentCtx);
        prgDialog.setMessage("Logging in...");
        prgDialog.setCancelable(false);
        
    	// Put Http parameter username with value of Email Edit View control
        RequestParams params = new RequestParams();
        params.put("apiKey", "informatikaundip");
        params.put("appVer", 1);
        params.put("siz_username", uName);
        params.put("siz_passkey", uPass);
        
        prgDialog.show();
        // Make RESTful webservices call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(SiZakatGlobal.getRESTUrl("request_token"),params ,new AsyncHttpResponseHandler() {
            
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
				String strJSON = new String(response);
				Log.v("DlgLogin", strJSON);
				try {
               		
                        // JSON Object
                   JSONObject obj = new JSONObject(strJSON);
                   // When the JSON response has status boolean value assigned with true
                   if(!obj.has("error")){
                	   Login_DlgLogin.this.getDialog().dismiss();
                	   JSONObject userData;
                	   userData = obj.getJSONObject("data");
                	   Toast.makeText(parentCtx, userData.getString("user_fullname"), Toast.LENGTH_LONG).show();
                	   
                	   String uToken	= userData.getString("user_token");
                	   String uFullname	= userData.getString("user_fullname");
                	   int uId		= userData.getInt("user_id");
                	   ((SiZakatApp) Login_DlgLogin.this.getActivity().getApplication()).
                	   		loginState.saveSession(uId, uToken, uFullname);
                	   
                	   SharedPreferences settings = Login_DlgLogin.this.getActivity().getSharedPreferences(SiZakatGlobal.PREFS_NAME, 0);
            	       SharedPreferences.Editor editor = settings.edit();
            	       editor.putString(SiZakatGlobal.PREFS_UTOKEN, uToken);
            	       editor.putString(SiZakatGlobal.PREFS_UFULLNAME, uFullname);
            	       editor.putInt(SiZakatGlobal.PREFS_UID, uId);

            	       // Commit the edits!
            	       editor.commit();
                	      
                	   dialogOke(1);
                   }
                   // Else display error message
                   else{
                       //errorMsg.setText(obj.getString("error_msg"));
                       Toast.makeText(parentCtx, obj.getString("error"), Toast.LENGTH_LONG).show();
                   }
               } catch (JSONException e) {
                   // TODO Auto-generated catch block
                   Toast.makeText(parentCtx, "Error Occured!", Toast.LENGTH_LONG).show();
                   e.printStackTrace();
               }
			}
        }); // end get
	}
}
