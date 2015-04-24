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

/**
 * @author Iyan
 * 
 * Description Activity kalkulator zakat harta (zakat maal)
 */
public class KalkulatorZakatHarta extends ActionBarActivity implements TextWatcher {

	// Ada 6 field yang disimpan + 1 total nilai
	public static final String PREFS_POSTFIX_TUNAI		= "_tunai";
	public static final String PREFS_POSTFIX_SAHAM		= "_saham";
	public static final String PREFS_POSTFIX_ESTATE	= "_estate";
	public static final String PREFS_POSTFIX_EMAS		= "_emas";
	public static final String PREFS_POSTFIX_MOBIL		= "_mobil";
	public static final String PREFS_POSTFIX_UTANG		= "_utang";
	
	private EditText txtBoxA;
	private EditText txtBoxB;
	private EditText txtBoxC;
	private EditText txtBoxD;
	private EditText txtBoxE;
	private TextView txtBoxF;
	private EditText txtBoxG;
	private TextView txtBoxH;
	//private TextView txtBoxI;
	
	private double besarNisab = 0.0f;
	private double jmlZakat = 0.0f;
	
	// =========== TextWatcher methods =============
	public void afterTextChanged(Editable s) {
		this.hitungZakat();
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	
	
	// =============================================
	private void hitungZakat() {
		double kenaZakat = .0f;
		try {
			double jumlahHarta = 0.0f;
			
			String txtA = txtBoxA.getText().toString();
			jumlahHarta += SiZakatGlobal.siZakatParseDouble(txtA);
			
			String txtB = txtBoxB.getText().toString();
			jumlahHarta += SiZakatGlobal.siZakatParseDouble(txtB);
			
			String txtC = txtBoxC.getText().toString();
			jumlahHarta += SiZakatGlobal.siZakatParseDouble(txtC);
			
			String txtD = txtBoxD.getText().toString();
			jumlahHarta += SiZakatGlobal.siZakatParseDouble(txtD);
			
			String txtE = txtBoxE.getText().toString();
			jumlahHarta += SiZakatGlobal.siZakatParseDouble(txtE);
			
			txtBoxF.setText(SiZakatGlobal.rupiahFormat(jumlahHarta));
			
			double hutang = 0.0f;
			String txtG = txtBoxG.getText().toString();
			hutang = SiZakatGlobal.siZakatParseDouble(txtG);
			
			double hartaBersih = jumlahHarta - hutang;
			txtBoxH.setText(SiZakatGlobal.rupiahFormat(hartaBersih));
			
			kenaZakat = (2.5f / 100.0f) * hartaBersih;
			
			if (hartaBersih <= besarNisab) {
				kenaZakat = 0.0f;
			}
		} catch (Exception e) {
			
		}
		jmlZakat = kenaZakat;
		((TextView) findViewById(R.id.zakat_txtbox_i)).setText(String.format(SiZakatGlobal.rupiahFormat(kenaZakat)));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kalkulator_zakat_harta);
		
		// Element inits
  	  	txtBoxA = ((EditText) findViewById(R.id.zakat_txtbox_a));
  	  	txtBoxB = ((EditText) findViewById(R.id.zakat_txtbox_b));
	  	txtBoxC = ((EditText) findViewById(R.id.zakat_txtbox_c));
	  	txtBoxD = ((EditText) findViewById(R.id.zakat_txtbox_d));
	  	txtBoxE = ((EditText) findViewById(R.id.zakat_txtbox_e));
	  	txtBoxF = ((TextView) findViewById(R.id.zakat_txtbox_f));
	  	txtBoxG = ((EditText) findViewById(R.id.zakat_txtbox_g));
	  	txtBoxH = ((TextView) findViewById(R.id.zakat_txtbox_h));
	  	//txtBoxI = ((TextView) findViewById(R.id.zakat_txtbox_i));
		
		final Button buttonKas = (Button) findViewById(R.id.frg_news_tryagain);
    	buttonKas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	hitungZakat();
            }
        });
    
    	this.restoreSessionValues();
    	final TextView txtBoxBesarNisab = (TextView) findViewById(R.id.zakat_zakatHarta_nisab);
  	  	txtBoxBesarNisab.setText(String.format("Nisab = %s", SiZakatGlobal.rupiahFormat(besarNisab)));
  	  	
    	this.hitungZakat();
    	
    	txtBoxA.addTextChangedListener(this);
    	txtBoxB.addTextChangedListener(this);
    	txtBoxC.addTextChangedListener(this);
    	txtBoxD.addTextChangedListener(this);
    	txtBoxE.addTextChangedListener(this);
    	txtBoxG.addTextChangedListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.kalkulator_zakat, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		//super.onDestroy();
		this.hitungZakat();
		this.saveSessionValues();
	    
		Intent data = new Intent();
		data.putExtra(KalkulatorZakat.ID_RETURN_ZAKAT_HARTA, jmlZakat);
		setResult(RESULT_OK, data);
		finish();
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
		txtA = txtBoxA.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_TUNAI, (float) tmp);
		
		txtA = txtBoxB.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_SAHAM, (float) tmp);
		
		txtA = txtBoxC.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_ESTATE, (float) tmp);
		
		txtA = txtBoxD.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_EMAS, (float) tmp);
		
		txtA = txtBoxE.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_MOBIL, (float) tmp);
		
		txtA = txtBoxG.getText().toString();
		tmp = SiZakatGlobal.siZakatParseDouble(txtA);
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_UTANG, (float) tmp);
		
		editor.putFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA, (float) jmlZakat);
		editor.commit();
	}
	
	private void restoreSessionValues() {
		SharedPreferences settings = this.getSharedPreferences(SiZakatGlobal.PREFS_NAME, 0);
		double tmp;
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_TUNAI, 0.0f);
		txtBoxA.setText(String.valueOf(Math.round(tmp)));
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_SAHAM, 0.0f);
		txtBoxB.setText(String.valueOf(Math.round(tmp)));
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_ESTATE, 0.0f);
		txtBoxC.setText(String.valueOf(Math.round(tmp)));
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_EMAS, 0.0f);
		txtBoxD.setText(String.valueOf(Math.round(tmp)));
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_MOBIL, 0.0f);
		txtBoxE.setText(String.valueOf(Math.round(tmp)));
		tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + PREFS_POSTFIX_UTANG, 0.0f);
		txtBoxG.setText(String.valueOf(Math.round(tmp)));
		tmp = settings.getFloat(
				SiZakatGlobal.PREFS_ZAKAT_BESARNISAB,
				(float) SiZakatGlobal.HARGA_EMAS_DEFAULT * SiZakatGlobal.PENGALI_EMAS
			);
		besarNisab = tmp;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		} else if (item.getItemId() == R.id.menu_kalkulatorzakat_reset) {
			
			return true;
		}
	    return false;
	}
}
