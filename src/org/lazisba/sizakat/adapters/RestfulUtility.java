package org.lazisba.sizakat.adapters;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class RestfulUtility {

	ProgressDialog prgDialog;
	Context parentCtx;
	public RestfulUtility(Context ctx) {
		parentCtx = ctx;
		// TODO Auto-generated constructor stub
		prgDialog = new ProgressDialog(parentCtx);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        
	}

	public void invokeWS(RequestParams params){
        // Show Progress Dialog
		prgDialog.show();
         // Make RESTful webservice call using AsyncHttpClient object
         AsyncHttpClient client = new AsyncHttpClient();
         client.get("http://192.168.1.11:80/rest/api/example",params ,new AsyncHttpResponseHandler() {
             
         
             // When the response returned by REST has Http response code other than '200'
             @Override
             public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                 // Hide Progress Dialog
                 prgDialog.hide();
                 // When Http response code is '404'
                 if(statusCode == 404){
                     Toast.makeText(parentCtx, "Requested resource not found", Toast.LENGTH_LONG).show();
                 }
                 // When Http response code is '500'
                 else if(statusCode == 500){
                     Toast.makeText(parentCtx, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                 }
                 // When Http response code other than 404, 500
                 else{
                     Toast.makeText(parentCtx, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                 }
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
                        if(obj.getString("error") == null){
                            Toast.makeText(parentCtx, obj.getString("result"), Toast.LENGTH_LONG).show();
                            // Navigate to Home screen
                            //navigatetoHomeActivity();
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
         
    } // end method invokeWS
} // end class RestfulUtility
