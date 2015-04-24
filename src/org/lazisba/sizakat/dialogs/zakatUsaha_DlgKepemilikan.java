package org.lazisba.sizakat.dialogs;

import org.lazisba.sizakat.KalkulatorZakatUsaha;
import org.lazisba.sizakat.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class zakatUsaha_DlgKepemilikan extends DialogFragment {

	private int returnValue = 100;
	
	public static interface OnCompleteListener {
	    public abstract void onDlgKepemilikanComplete(int returnValue);
	}

	private OnCompleteListener mListener;
	private SeekBar seekKepemilikan;
	private TextView lblKepemilikan;

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

	private void dialogOke() {
		this.mListener.onDlgKepemilikanComplete(returnValue);
	}
	
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
	    .setTitle("Kepemilikan Usaha")
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
		
		View v = inflater.inflate(R.layout.fragment_dlg_zusaha_milik, null);
        
        seekKepemilikan = ((SeekBar) v.findViewById(R.id.frg_zusaha_seekmilik));
		lblKepemilikan = ((TextView) v.findViewById(R.id.frg_zusaha_txtpersen));
		
		seekKepemilikan.setMax(100);
		
		returnValue = this.getArguments().getInt(KalkulatorZakatUsaha.ARG_ID_SEEKVALUE, 100);
		seekKepemilikan.setProgress(returnValue);
		updateProgressLabel();
		
		seekKepemilikan.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				returnValue = progress;
				updateProgressLabel();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { /* nothing */}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
		});
		
		b.setView(v);
		
	    return b.create();
	}
	
	private void updateProgressLabel() {
		lblKepemilikan.setText(String.format("%d%%", returnValue));
	}
}
