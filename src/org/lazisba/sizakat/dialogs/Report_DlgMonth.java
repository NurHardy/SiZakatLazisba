package org.lazisba.sizakat.dialogs;

import org.lazisba.sizakat.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	        this.mListener = (OnCompleteListener)activity;
	    }
	    catch (final ClassCastException e) {
	        throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
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
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dlg_report_filter, container, false);
        
        //=== Spinner Bulan ===========================
        Spinner spinnerMonth = (Spinner) v.findViewById(R.id.frg_dlgreport_bulan);
    	// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<CharSequence> adapterBulan = ArrayAdapter.createFromResource(this.getActivity().getBaseContext(),
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
    	
    	//=== Spinner Bulan ===========================
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
        spinnerYear.setSelection(0);
    	spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	    	selYear = 2013+(pos);
    	    }
    	    public void onNothingSelected(AdapterView<?> parent) {
    	    }
    	});
    	
        Button button = (Button)v.findViewById(R.id.frag_laporanKauangan_filter);
        //button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Report_DlgMonth.this.getDialog().dismiss();
				dialogOke();
            }
        });

        return v;
    }
	
}
