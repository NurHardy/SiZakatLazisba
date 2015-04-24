package org.lazisba.sizakat;

import org.lazisba.sizakat.util.SiZakatGlobal;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class KalkulatorZakatProfesi extends ActionBarActivity implements TextWatcher {
	// Ada 4 field yang disimpan + 1 total nilai
	public static final String PREFS_POSTFIX_GAJI		= "_gaji";
	public static final String PREFS_POSTFIX_BONUS		= "_bonus";
	public static final String PREFS_POSTFIX_BIAYARUTIN	= "_bearutin";
	public static final String PREFS_POSTFIX_BIAYATAHUN	= "_beatahun";
	
	private double besarNisab = 0.0f;
	private EditText txtBoxGaji;
	private EditText txtBoxBonus;
	private TextView lblPendapatanTahun;
	private EditText txtBoxPengeluaranRutin;
	private EditText txtBoxPengeluaranTahunan;
	private TextView lblPenghasilanKenaZakat;
	private TextView lblJumlahZakat;
	
	private double jumlahKenaZakat;
	private double jumlahZakatProfesi;
	
	// =========== TextWatcher methods =============
	public void afterTextChanged(Editable s) {
		this.hitungZakatProfesi();
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	
	
	// =============================================
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kalkulator_zakat_profesi);
  	  	
		txtBoxGaji	= ((EditText) findViewById(R.id.zakat_zakatProfesi_txt_a));
		txtBoxBonus	= ((EditText) findViewById(R.id.zakat_zakatProfesi_txt_b));
		lblPendapatanTahun	= ((TextView) findViewById(R.id.zakat_zakatProfesi_txt_c));
	
		txtBoxPengeluaranRutin		= ((EditText) findViewById(R.id.zakat_zakatProfesi_txt_d));
		txtBoxPengeluaranTahunan	= ((EditText) findViewById(R.id.zakat_zakatProfesi_txt_e));
		lblPenghasilanKenaZakat		= ((TextView) findViewById(R.id.zakat_zakatProfesi_txt_f));
	
		lblJumlahZakat	= ((TextView) findViewById(R.id.zakat_zakatProfesi_txt_finalzakat));
		
		// Bind event onClick
		final Button buttonHitung = (Button) findViewById(R.id.zakat_zakatProfesi_btn_ok);
		buttonHitung.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	hitungZakatProfesi();
            }
        });
		
		this.restoreSessionValues();
    	final TextView txtBoxBesarNisab = (TextView) findViewById(R.id.zakat_zakatProfesi_besarnisab);
  	  	txtBoxBesarNisab.setText(String.format("Nisab = %s", SiZakatGlobal.rupiahFormat(besarNisab)));
  	  	
    	this.hitungZakatProfesi();
    	
    	txtBoxGaji.addTextChangedListener(this);
    	txtBoxBonus.addTextChangedListener(this);
    	txtBoxPengeluaranRutin.addTextChangedListener(this);
    	txtBoxPengeluaranTahunan.addTextChangedListener(this);
	}
	
	/**
	 * Merestore sesi kalkulator
	 */
	private void restoreSessionValues() {
		SharedPreferences settings = this.getSharedPreferences(SiZakatGlobal.PREFS_NAME, 0);
		double tmp;
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + PREFS_POSTFIX_GAJI, 0.0f);
		txtBoxGaji.setText(String.valueOf(Math.round(tmp)));
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + PREFS_POSTFIX_BONUS, 0.0f);
		txtBoxBonus.setText(String.valueOf(Math.round(tmp)));
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + PREFS_POSTFIX_BIAYARUTIN, 0.0f);
		txtBoxPengeluaranRutin.setText(String.valueOf(Math.round(tmp)));
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + PREFS_POSTFIX_BIAYATAHUN, 0.0f);
		txtBoxPengeluaranTahunan.setText(String.valueOf(Math.round(tmp)));
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
		txtA = txtBoxGaji.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + PREFS_POSTFIX_GAJI, (float) tmp);
		
		txtA = txtBoxBonus.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + PREFS_POSTFIX_BONUS, (float) tmp);
		
		txtA = txtBoxPengeluaranRutin.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + PREFS_POSTFIX_BIAYARUTIN, (float) tmp);
		
		txtA = txtBoxPengeluaranTahunan.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + PREFS_POSTFIX_BIAYATAHUN, (float) tmp);
		
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI, (float) jumlahZakatProfesi);
		editor.commit();
	}

	/**
	 * Hitung zakat profesi dan tampilkan hasilnya
	 */
	private void hitungZakatProfesi() {
		try {
			double jumlahPemasukan = 0.0f;
			double jumlahPengeluaran = 0.0f;
			
			// Hitung jumlah pemasukan
			String txtGaji = txtBoxGaji.getText().toString().trim();
			if (!txtGaji.equalsIgnoreCase(""))
				jumlahPemasukan += (Double.parseDouble(txtGaji)*12);
			
			String txtBonus = txtBoxBonus.getText().toString().trim();
			if (!txtBonus.equalsIgnoreCase(""))
				jumlahPemasukan += Double.parseDouble(txtBonus);
			
			lblPendapatanTahun.setText(String.format(SiZakatGlobal.rupiahFormat(jumlahPemasukan)));
			
			// Hitung jumlah pengeluaran
			String txtRutin = txtBoxPengeluaranRutin.getText().toString().trim();
			if (!txtRutin.equalsIgnoreCase(""))
				jumlahPengeluaran += (Double.parseDouble(txtRutin)*12);
			
			String txtTahunan = txtBoxPengeluaranTahunan.getText().toString().trim();
			if (!txtTahunan.equalsIgnoreCase(""))
				jumlahPengeluaran += Double.parseDouble(txtTahunan);

			// Hitung jumlah zakat profesi
			jumlahKenaZakat = jumlahPemasukan - jumlahPengeluaran;
			
			lblPenghasilanKenaZakat.setText(String.format(SiZakatGlobal.rupiahFormat(jumlahKenaZakat)));
			if (jumlahKenaZakat < besarNisab)
				jumlahZakatProfesi = 0;
			else
				jumlahZakatProfesi = 0.025f * jumlahKenaZakat;
			
			lblJumlahZakat.setText(String.format(SiZakatGlobal.rupiahFormat(jumlahZakatProfesi)));
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.kalkulator_zakat, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		this.hitungZakatProfesi();
		this.saveSessionValues();
	    
		Intent data = new Intent();
		data.putExtra(KalkulatorZakat.ID_RETURN_ZAKAT_PROFESI, jumlahZakatProfesi);
		setResult(RESULT_OK, data);
		finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
	    return false;
	}
}
