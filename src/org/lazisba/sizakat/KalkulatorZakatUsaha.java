package org.lazisba.sizakat;

import org.lazisba.sizakat.dialogs.zakatUsaha_DlgKepemilikan;
import org.lazisba.sizakat.util.SiZakatGlobal;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class KalkulatorZakatUsaha extends ActionBarActivity
		implements zakatUsaha_DlgKepemilikan.OnCompleteListener, TextWatcher {
	
	// Ada 3 field yang disimpan + 1 total nilai
	public static final String PREFS_POSTFIX_ASET		= "_aset";
	public static final String PREFS_POSTFIX_UTANG		= "_utang";
	public static final String PREFS_POSTFIX_KEPEMILIKAN	= "_kepemilikan";
	
	public static final String DLG_ID_SEEKKEPEMILIKAN	= "dlgSeekMilik";
	public static final String ARG_ID_SEEKVALUE			= "persenMilik";
	
	private double besarNisab = 0.0f;
	private ProgressBar seekKepemilikan;
	private EditText txtBoxKekayaan;
	private EditText txtBoxUtang;
	private TextView lblKepemilikan;
	
	private int persentaseMilik = 100;
	private TextView lblKenaZakat;
	private TextView lblJumlahZakat;
	
	private double jumlahKenaZakat;
	private double jumlahZakatUsaha;
	
	// =========== TextWatcher methods =============
	public void afterTextChanged(Editable s) {
		this.hitungZakatUsaha();
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	
	// =============================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.kalkulator_zakat, menu);
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kalkulator_zakat_usaha);
		
		seekKepemilikan = ((ProgressBar) findViewById(R.id.zakat_zakatUsaha_seek));
		txtBoxKekayaan = ((EditText) findViewById(R.id.zakat_zakatUsaha_txt_a));
		txtBoxUtang = ((EditText) findViewById(R.id.zakat_zakatUsaha_txt_b));
		
		lblKepemilikan = ((TextView) findViewById(R.id.zakat_zakatUsaha_lbl_komposisi));
		
		seekKepemilikan.setMax(100);
		
		lblKenaZakat	= ((TextView) findViewById(R.id.zakat_zakatUsaha_txt_d));
		lblJumlahZakat	= ((TextView) findViewById(R.id.zakat_zakatUsaha_txt_finalzakat));
		
		// Bind event onClick
		final Button buttonHitung = (Button) findViewById(R.id.zakat_zakatUsaha_btn_ok);
		buttonHitung.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	hitungZakatUsaha();
            }
        });
		
		LinearLayout bSeekMilik = (LinearLayout) findViewById(R.id.zakat_zakatUsaha_btnKepemilikan);
		bSeekMilik.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	showSeekbarDialog();
		    }
		});
		
		this.restoreSessionValues();
		final TextView txtBoxBesarNisab = (TextView) findViewById(R.id.zakat_zakatUsaha_nisab);
  	  	txtBoxBesarNisab.setText(String.format("Nisab = %s", SiZakatGlobal.rupiahFormat(besarNisab)));
  	  	this.hitungZakatUsaha();

  	  	txtBoxKekayaan.addTextChangedListener(this);
  	  	txtBoxUtang.addTextChangedListener(this);
	}
	
	private void showSeekbarDialog() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    Fragment prev = getSupportFragmentManager().findFragmentByTag(DLG_ID_SEEKKEPEMILIKAN);
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);

	    DialogFragment newFragment = new zakatUsaha_DlgKepemilikan();
	    
	    // Tambah bundle argument
	    Bundle args = new Bundle();
	    args.putInt(ARG_ID_SEEKVALUE, persentaseMilik);
	    newFragment.setArguments(args);
	    newFragment.show(ft, DLG_ID_SEEKKEPEMILIKAN);
	}
	private void updateLabel() {
		try {
			double jumlahKekayaan = 0.0f;
			
			// Hitung jumlah pemasukan
			String txtKekayaan = txtBoxKekayaan.getText().toString().trim();
			jumlahKekayaan += SiZakatGlobal.siZakatParseDouble(txtKekayaan);
			
			String txtUtang = txtBoxUtang.getText().toString().trim();
			if (!txtUtang.equalsIgnoreCase(""))
				jumlahKekayaan -= Double.parseDouble(txtUtang);
			
			lblKepemilikan.setText(String.format("%d%%", persentaseMilik));
			seekKepemilikan.setProgress(persentaseMilik);
			
			jumlahKenaZakat = ((double) persentaseMilik/100.0f) * jumlahKekayaan;
			
			lblKenaZakat.setText(String.format(SiZakatGlobal.rupiahFormat(jumlahKenaZakat)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void hitungZakatUsaha() {
		try {
			this.updateLabel();
			if (jumlahZakatUsaha < besarNisab)
				jumlahZakatUsaha = 0;
			else
				jumlahZakatUsaha = 0.025f * jumlahKenaZakat;
			lblJumlahZakat.setText(String.format(SiZakatGlobal.rupiahFormat(jumlahZakatUsaha)));
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Merestore sesi kalkulator
	 */
	private void restoreSessionValues() {
		SharedPreferences settings = this.getSharedPreferences(SiZakatGlobal.PREFS_NAME, 0);
		double tmp;
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA + PREFS_POSTFIX_ASET, 0.0f);
		txtBoxKekayaan.setText(String.valueOf(Math.round(tmp)));
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA + PREFS_POSTFIX_UTANG, 0.0f);
		txtBoxUtang.setText(String.valueOf(Math.round(tmp)));
		int persenMilik = settings.getInt(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA + PREFS_POSTFIX_KEPEMILIKAN, 100);
		if ((persenMilik >= 0) && (persenMilik <= 100))
			persentaseMilik = persenMilik;
		
		tmp = settings.getFloat(
				SiZakatGlobal.PREFS_ZAKAT_BESARNISAB,
				(float) SiZakatGlobal.HARGA_EMAS_DEFAULT * SiZakatGlobal.PENGALI_EMAS
			);
		besarNisab = tmp;
	}
	/**
	 * Menyimpan session (nilai-nilai) ke preference
	 */
	private void saveSessionValues() {
		// Save session
		SharedPreferences settings = this.getSharedPreferences(SiZakatGlobal.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		double tmp = 0.0f;
		
		String txtA;
		txtA = txtBoxKekayaan.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA + PREFS_POSTFIX_ASET, (float) tmp);
		
		txtA = txtBoxUtang.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA + PREFS_POSTFIX_UTANG, (float) tmp);
		
		editor.putInt(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA + PREFS_POSTFIX_KEPEMILIKAN, this.persentaseMilik);
		
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA, (float) jumlahZakatUsaha);
		editor.commit();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
	    return false;
	}

	@Override
	public void onDlgKepemilikanComplete(int returnValue) {
		persentaseMilik = returnValue;
		this.hitungZakatUsaha();
	}
	
	@Override
	public void onBackPressed() {
		this.hitungZakatUsaha();
		this.saveSessionValues();
	    
		Intent data = new Intent();
		data.putExtra(KalkulatorZakat.ID_RETURN_ZAKAT_USAHA, jumlahZakatUsaha);
		setResult(RESULT_OK, data);
		finish();
	}
}
