package org.lazisba.sizakat.dialogs;

import org.lazisba.sizakat.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class zakat_DlgNisab extends DialogFragment {

	private double hargaGramEmas = 400000.0f;
	private double besarNisab = 0;
	
	public static interface OnCompleteListener {
	    public abstract void onNisabComplete(double nisab);
	}

	private OnCompleteListener mListener;
	
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
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		besarNisab = hargaGramEmas * 85;
		
		AlertDialog.Builder b =  new  AlertDialog.Builder(getActivity())
	    .setTitle("Ubah nilai Nisab")
	    .setPositiveButton(android.R.string.ok,
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	dialogOke();
	            	dialog.dismiss();
	            }
	        }
	    )
	    .setNegativeButton(android.R.string.cancel,
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	                dialog.dismiss();
	            }
	        }
	    );

	    LayoutInflater i = getActivity().getLayoutInflater();

	    View v = i.inflate(R.layout.kalkulator_zakat_nisab,null);
	    final EditText txtBoxHargaEmas = (EditText) v.findViewById(R.id.zakat_txtbox_nisab);
		final TextView txtBoxBesarNisab = (TextView) v.findViewById(R.id.zakat_txtbox_nisabfinal);
		
  	  	txtBoxBesarNisab.setText(String.format("Rp. %.2f", besarNisab));
		txtBoxHargaEmas.addTextChangedListener(new TextWatcher() {

          public void afterTextChanged(Editable s) {
        	  hargaGramEmas = 0.0f;
        	  besarNisab = 0.0f;
        	  try {
            	  String txtEmas = txtBoxHargaEmas.getText().toString();
            	  if (!txtEmas.equalsIgnoreCase("")) {
            		  hargaGramEmas = Double.parseDouble(txtEmas);
            	  }
            	  besarNisab = 85 * hargaGramEmas;
        	  } catch (Exception e) {
        		  hargaGramEmas = 0.0f;
            	  besarNisab = 0.0f;
        	  }
        	  txtBoxBesarNisab.setText(String.format("Rp. %.2f", besarNisab));
          }

          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          public void onTextChanged(CharSequence s, int start, int before, int count) {}
       });
	    
	    b.setView(v);
	    return b.create();
	}
	public zakat_DlgNisab() {
		// TODO Auto-generated constructor stub
	}

	private void dialogOke() {
		if (besarNisab == 0) {
			
		} else this.mListener.onNisabComplete(besarNisab);
	}
}
