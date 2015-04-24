package org.lazisba.sizakat.dialogs;

import org.lazisba.sizakat.LazisbaHome;
import org.lazisba.sizakat.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class Report_DlgMonth extends DialogFragment {

	private int selMonth, selYear;
	
	public static interface OnCompleteListener {
	    public abstract void onComplete(int sM, int sY);
	}

	private OnCompleteListener mListener;

	
	// make sure the Activity implemented it
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	    try {
	        this.mListener = (OnCompleteListener) this.getTargetFragment();
	    }
	    catch (final ClassCastException e) {
	        throw new ClassCastException(this.getTargetFragment().toString() + " must implement OnCompleteListener");
	    }
	}
	
	//@Override
	//public void onCreate() {
	//	this.getDialog().setCanceledOnTouchOutside(true);
	//}
	public Report_DlgMonth() {
		// TODO Auto-generated constructor stub
	}

	private void dialogOke() {
		this.mListener.onComplete(selMonth, selYear);
	}
	
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
	    .setTitle("Laporan Keuangan")
	    .setPositiveButton(android.R.string.ok,
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	dialogOke();
	            	dialog.dismiss();
	            }
	        }
	    )
	    .setIcon(R.drawable.icon_report_32)
	    .setNegativeButton(android.R.string.cancel,
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	                dialog.dismiss();
	            }
	        }
	    );
		
		// Process arguments
		Bundle dlgArgs = this.getArguments();
		if (dlgArgs != null) {
			selMonth = dlgArgs.getInt(LazisbaHome.ID_ARG_CURRENTMONTH, 1);
			selYear = dlgArgs.getInt(LazisbaHome.ID_ARG_CURRENTYEAR, 2014);
		} else {
			selMonth = 1; selYear = 2014;
		}
		
		
		// Generate user interface
		View v = inflater.inflate(R.layout.fragment_dlg_report_filter, null);
		
		//=== Spinner Bulan ===========================
        Spinner spinnerMonth = (Spinner) v.findViewById(R.id.frg_dlgreport_bulan);
    	// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<CharSequence> adapterBulan = ArrayAdapter.createFromResource(this.getActivity(),
    	        R.array.app_str_months, android.R.layout.simple_spinner_item);
    	// Specify the layout to use when the list of choices appears
    	adapterBulan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	spinnerMonth.setAdapter(adapterBulan);
        
    	spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	    	selMonth = pos+1;
    	    }
    	    public void onNothingSelected(AdapterView<?> parent) {
    	    }
    	});
    	spinnerMonth.setSelection(selMonth-1);
    	Log.d("Spinner Bulan : ", String.valueOf(selMonth));
    	
    	//=== Spinner Tahun ===========================
        Spinner spinnerYear = (Spinner) v.findViewById(R.id.frg_dlgreport_tahun);
    	// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<CharSequence> adapterTahun =
    			new ArrayAdapter<CharSequence>(this.getActivity(), android.R.layout.simple_spinner_item);
    	
    	int c;
    	for (c=2014;c<2019;c++) {
    		adapterTahun.add(String.valueOf(c));
    	}
    	// Specify the layout to use when the list of choices appears
    	adapterTahun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	// Apply the adapter to the spinner
    	spinnerYear.setAdapter(adapterTahun);
        
    	spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	    	selYear = 2014+(pos);
    	    }
    	    public void onNothingSelected(AdapterView<?> parent) {
    	    }
    	});
    	spinnerYear.setSelection(((selYear-2014)>=0 ? selYear-2014 : 2014));
    	Log.d("Spinner Tahun : ", String.valueOf(selYear));
		b.setView(v);
		
	    return b.create();
	}
}
