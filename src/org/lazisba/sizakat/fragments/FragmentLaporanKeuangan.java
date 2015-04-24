package org.lazisba.sizakat.fragments;

import java.util.Calendar;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lazisba.sizakat.LazisbaHome;
import org.lazisba.sizakat.R;
import org.lazisba.sizakat.dialogs.Report_DlgMonth;
import org.lazisba.sizakat.util.SiZakatGlobal;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentLaporanKeuangan extends Fragment implements Report_DlgMonth.OnCompleteListener {
	private static final String ID_DLG_REPORTFILTER = "dlg_reportFilter";
	
	private View view;
	private int currentMonth, currentYear;
	private Activity parentCtx;
	
	public FragmentLaporanKeuangan() {
		Calendar c = Calendar.getInstance();
		currentYear = c.get(Calendar.YEAR);
		currentMonth = c.get(Calendar.MONTH)+1;
		
		if (currentMonth == 1) {
			currentYear--; currentMonth = 12;
		}
	}
	public FragmentLaporanKeuangan(int cMonth, int cYear) {
		this.currentMonth = cMonth;
		this.currentYear = cYear;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
		
        parentCtx = this.getActivity();
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_laporankeuangan, container, false);
            
        	final TextView btnFilter = (TextView) view.findViewById(R.id.lblTitleReport);
        	btnFilter.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	FragmentTransaction ft = getFragmentManager().beginTransaction();
    			    Fragment prev = getFragmentManager().findFragmentByTag(ID_DLG_REPORTFILTER);
    			    if (prev != null) {
    			        ft.remove(prev);
    			    }
    			    ft.addToBackStack(null);

    			    // Create and show the dialog.
    			    DialogFragment newFragment = new Report_DlgMonth();
    			    newFragment.setTargetFragment(FragmentLaporanKeuangan.this, 0);
    			    Bundle bundle = new Bundle();
    			    bundle.putInt(LazisbaHome.ID_ARG_CURRENTMONTH, currentMonth);
    			    bundle.putInt(LazisbaHome.ID_ARG_CURRENTYEAR, currentYear);
    			    newFragment.setArguments(bundle);
    			    newFragment.show(ft, ID_DLG_REPORTFILTER);
                }
            });
        	
        	onComplete(this.currentMonth, this.currentYear);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        return view;
    }
	
	// Oncomplete untuk laporan keuangan
	@Override
	public void onComplete(int sM, int sY) {
		currentMonth = sM;
		currentYear = sY;
		
		((LazisbaHome) parentCtx).changeCurrentReportTime(currentMonth, currentYear);
		final LayoutInflater layoutInfaterLaporan = parentCtx.getLayoutInflater();
		final AsyncHttpClient client = new AsyncHttpClient();
		
    	// ======= Set up progress bar
    	final ProgressDialog prgDialog;
    	prgDialog = new ProgressDialog(parentCtx);
        prgDialog.setMessage("Memuat data laporan...");
        prgDialog.setCancelable(true);
        prgDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (client != null) {
					client.cancelRequests(parentCtx, true);
					Toast.makeText(parentCtx, "Request dibatalkan...", Toast.LENGTH_SHORT).show();
				}
			}
        });
        
    	// Put Http parameter username with value of Email Edit View control
        RequestParams params = new RequestParams();
        params.put("apiKey", "informatikaundip");
        params.put("report_month", sM);
        params.put("report_year", sY);
        
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        
        client.get(SiZakatGlobal.getRESTUrl("laporan_bulan"),params ,new AsyncHttpResponseHandler() {
            
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // Hide Progress Dialog
                prgDialog.dismiss();
                Toast.makeText(parentCtx, "Terjadi kesalahan... Silakan coba lagi.", Toast.LENGTH_LONG).show();
            }
			@Override
			// When the response returned by REST has Http response code '200'
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				prgDialog.dismiss();
				String strJSON = new String(response);
				Log.v("LaporanBulan: JSON", strJSON);
				try {
				   // JSON Object
				   JSONObject obj = new JSONObject(strJSON);
				   // When the JSON response has status boolean value assigned with true
				   if(!obj.has("error")){
					   ((TextView) view.findViewById(R.id.lblTitleReport)).setText(obj.getString("label"));
					   JSONObject reportBody = obj.getJSONObject("data");
					   JSONObject reportPemasukan = reportBody.getJSONObject("pemasukan");
					   JSONObject reportPenyaluran = reportBody.getJSONObject("penyaluran");
					   
					   ((TextView) view.findViewById(R.id.txtKas)).setText(reportBody.getString("kas"));
					   ((TextView) view.findViewById(R.id.txtPenerimaan)).setText(reportPemasukan.getString("total"));
					   ((TextView) view.findViewById(R.id.txtPengeluaran)).setText(reportPenyaluran.getString("total"));
					   ((TextView) view.findViewById(R.id.txtPengeluaran1)).setText(reportBody.getString("saldo"));
					   
					   	// Detil akun penerimaan
		            	final LinearLayout listDetilPenerimaan =
		            			(LinearLayout) view.findViewById(R.id.laporanKeuanganListDetilPenerimaan);
		            	final JSONArray akunPenerimaan = reportPemasukan.getJSONArray("data");
		            	int cc;
		            	TextView tmp1, tmp2;
		            	listDetilPenerimaan.removeAllViewsInLayout();
		            	for (cc=0;cc<reportPemasukan.getInt("count");cc++) {
		            		JSONObject jsonObject = akunPenerimaan.getJSONObject(cc);
		            		View newItem = layoutInfaterLaporan.inflate(R.layout.listitem_detil_laporankeuangan, null);
		            		tmp1 = (TextView) newItem.findViewById(R.id.listitem_laporankeu_txtTitle);
		            		tmp2 = (TextView) newItem.findViewById(R.id.listitem_laporankeu_txtNilai);
		            		tmp1.setText(jsonObject.getString("label"));
		            		tmp2.setText(jsonObject.getString("nominal"));
		            		listDetilPenerimaan.addView(newItem);
		            	}
		            	
		            	// Detil akun pengeluaran
		            	final LinearLayout listDetilPengeluaran =
		            			(LinearLayout) view.findViewById(R.id.laporanKeuanganListDetilPengeluaran);
		            	final JSONArray akunPengeluaran = reportPenyaluran.getJSONArray("data");
		            	listDetilPengeluaran.removeAllViewsInLayout();
		            	for (cc=0;cc<reportPenyaluran.getInt("count");cc++) {
		            		JSONObject jsonObject = akunPengeluaran.getJSONObject(cc);
		            		View newItem = layoutInfaterLaporan.inflate(R.layout.listitem_detil_laporankeuangan, null);
		            		tmp1 = (TextView) newItem.findViewById(R.id.listitem_laporankeu_txtTitle);
		            		tmp2 = (TextView) newItem.findViewById(R.id.listitem_laporankeu_txtNilai);
		            		tmp1.setText(jsonObject.getString("label"));
		            		tmp2.setText(jsonObject.getString("nominal"));
		            		listDetilPengeluaran.addView(newItem);
		            	}
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
}
