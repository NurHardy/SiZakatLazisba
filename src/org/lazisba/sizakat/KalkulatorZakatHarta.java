package org.lazisba.sizakat;

import org.lazisba.sizakat.util.SiZakatGlobal;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class KalkulatorZakatHarta extends ActionBarActivity {

	private EditText txtBoxA;
	private EditText txtBoxB;
	private EditText txtBoxC;
	private EditText txtBoxD;
	private EditText txtBoxE;
	private TextView txtBoxF;
	private EditText txtBoxG;
	private TextView txtBoxH;
	private TextView txtBoxI;
	
	private double besarNisab = 0.0f;
	private double jmlZakat = 0.0f;
	
	private void hitungZakat() {
		double kenaZakat = .0f;
		try {
			double jumlahHarta = 0.0f;
			
			String txtA = txtBoxA.getText().toString().trim();
			if (!txtA.equalsIgnoreCase("")) jumlahHarta += Double.parseDouble(txtA);
			Log.d("A", txtA);
			
			String txtB = txtBoxB.getText().toString().trim();
			if (!txtB.equalsIgnoreCase("")) jumlahHarta += Double.parseDouble(txtB);
			Log.d("B", txtB);
			
			String txtC = txtBoxC.getText().toString().trim();
			if (!txtC.equalsIgnoreCase("")) jumlahHarta += Double.parseDouble(txtC);
			Log.d("C", txtC);
			
			String txtD = txtBoxD.getText().toString().trim();
			if (!txtD.equalsIgnoreCase("")) jumlahHarta += Double.parseDouble(txtD);
			Log.d("D", txtD);
			
			String txtE = txtBoxE.getText().toString().trim();
			if (!txtE.equalsIgnoreCase("")) jumlahHarta += Double.parseDouble(txtE);
			Log.d("E", txtE);
			
			Log.d("KalkulatorZakat","Outoput jumlah harta - PRE");
			txtBoxF.setText(String.format("Rp. %.2f", jumlahHarta));
			Log.d("KalkulatorZakat","Outoput jumlah harta - POST");
			
			double hutang = 0.0f;
			String txtG = txtBoxG.getText().toString().trim();
			if (!txtG.equalsIgnoreCase("")) hutang = Double.parseDouble(txtG.trim());
			Log.d("Hutang", txtG);
			
			double hartaBersih = jumlahHarta - hutang;
			txtBoxH.setText(String.format("Rp. %.2f", hartaBersih));
			
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
		
		final TextView txtBoxBesarNisab = (TextView) findViewById(R.id.zakat_zakatHarta_nisab);
  	  	txtBoxBesarNisab.setText(String.format("Nisab = Rp. %.2f", besarNisab));
  	  	
  	  	txtBoxA = ((EditText) findViewById(R.id.zakat_txtbox_a));
  	  	txtBoxB = ((EditText) findViewById(R.id.zakat_txtbox_b));
	  	txtBoxC = ((EditText) findViewById(R.id.zakat_txtbox_c));
	  	txtBoxD = ((EditText) findViewById(R.id.zakat_txtbox_d));
	  	txtBoxE = ((EditText) findViewById(R.id.zakat_txtbox_e));
	  	txtBoxF = ((TextView) findViewById(R.id.zakat_txtbox_f));
	  	txtBoxG = ((EditText) findViewById(R.id.zakat_txtbox_g));
	  	txtBoxH = ((TextView) findViewById(R.id.zakat_txtbox_h));
	  	txtBoxI = ((TextView) findViewById(R.id.zakat_txtbox_i));
		
	  	
		final Button buttonKas = (Button) findViewById(R.id.frg_news_tryagain);
    	buttonKas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	hitungZakat();
            }
        });
	}
	
	@Override
	public void onBackPressed() {
		Intent data = new Intent();
		data.putExtra("jumlaZakat", jmlZakat);
		// Activity finished ok, return the data
		setResult(RESULT_OK, data);
		finish();
		/*
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	                WelcomeActivity.super.onBackPressed();
	            }
	        }).create().show();*/
	}
}
