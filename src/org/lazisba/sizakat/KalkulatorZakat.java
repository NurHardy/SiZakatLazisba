package org.lazisba.sizakat;

import org.lazisba.sizakat.dialogs.zakat_DlgNisab;
import org.lazisba.sizakat.util.SiZakatGlobal;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Iyan
 *
 */
public class KalkulatorZakat extends ActionBarActivity implements
	zakat_DlgNisab.OnCompleteListener {

	public static final String ID_RETURN_ZAKAT_HARTA = "jumlahZakatHarta";
	public static final String ID_RETURN_ZAKAT_PROFESI = "jumlahZakatProfesi";
	public static final String ID_RETURN_ZAKAT_USAHA = "jumlahZakatUsaha";
	
	private static final int ID_ACTIVITY_ZAKAT_HARTA = 11;
	private static final int ID_ACTIVITY_ZAKAT_PROFESI = 12;
	private static final int ID_ACTIVITY_ZAKAT_USAHA = 13;
	
	private double nisabFinal = .0f;
	private TextView txtNisab;
	private TextView txtJmlZakatHarta;
	private TextView txtJmlZakatProfesi;
	private TextView txtJmlZakatUsaha;
	private TextView txtJmlZakatFinal;
	
	private double zHarta, zProfesi, zUsaha, zTotal;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kalkulator_zakat);
		
		txtNisab = (TextView) findViewById(R.id.zakat_txtNisab);
		RelativeLayout bNisab = (RelativeLayout) findViewById(R.id.zakat_boxNisab);
		bNisab.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	FragmentTransaction ft = KalkulatorZakat.this.getSupportFragmentManager().beginTransaction();
			    Fragment prev = KalkulatorZakat.this.getSupportFragmentManager().findFragmentByTag("dlgNisab");
			    if (prev != null) {
			        ft.remove(prev);
			    }
			    ft.addToBackStack(null);

			    // Create and show the dialog.
			    DialogFragment newFragment = new zakat_DlgNisab();
			    newFragment.show(ft, "dlgNisab");
		    }
		});
		
		RelativeLayout bZakatHarta = (RelativeLayout) findViewById(R.id.zakat_boxZakatHarta);
		bZakatHarta.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	Intent intent = new Intent(KalkulatorZakat.this, KalkulatorZakatHarta.class);
				startActivityForResult(intent, ID_ACTIVITY_ZAKAT_HARTA);
		    }
		});
		
		RelativeLayout bZakatProfesi = (RelativeLayout) findViewById(R.id.zakat_boxZakatProfesi);
		bZakatProfesi.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	Intent intent = new Intent(KalkulatorZakat.this, KalkulatorZakatProfesi.class);
		    	startActivityForResult(intent, ID_ACTIVITY_ZAKAT_PROFESI);
		    }
		});
		
		RelativeLayout bZakatUsaha = (RelativeLayout) findViewById(R.id.zakat_boxZakatUsaha);
		bZakatUsaha.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	Intent intent = new Intent(KalkulatorZakat.this, KalkulatorZakatUsaha.class);
		    	startActivityForResult(intent, ID_ACTIVITY_ZAKAT_USAHA);
		    }
		});
		
		RelativeLayout bZakatTotal = (RelativeLayout) findViewById(R.id.zakat_boxZakatTotal);
		bZakatTotal.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	final CharSequence[] items = {"Copy"};

				AlertDialog.Builder builder = new AlertDialog.Builder(KalkulatorZakat.this);
				builder.setTitle("Zakat Total");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
				    	if (item == 0) {
				    		SiZakatGlobal.copyToClipboard(
					    			KalkulatorZakat.this,
					    			String.valueOf(Math.round(KalkulatorZakat.this.zTotal)),
					    			"Zakat Total"
					    		);
				    	}
				    	
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
		    }
		});
		
		
		
		txtJmlZakatHarta	= (TextView) findViewById(R.id.zakat_txtZakatHarta);
		txtJmlZakatProfesi	= (TextView) findViewById(R.id.zakat_txtZakatProfesi);
		txtJmlZakatUsaha	= (TextView) findViewById(R.id.zakat_txtZakatUsaha);
		txtJmlZakatFinal	= (TextView) findViewById(R.id.zakat_txt_jmlzakat);
		this.registerForContextMenu(findViewById(R.id.zakat_boxZakatTotal));
		
		this.restoreSessionValues();
		this.updateLabels();
	}
	@Override
	public void onNisabComplete(double nisab) {
		nisabFinal = nisab;
		txtNisab.setText(SiZakatGlobal.rupiahFormat(nisabFinal));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kalkulator_zakat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_kalkulatorzakat_reset) {
		    new AlertDialog.Builder(this)
		        .setTitle("Konfirmasi")
		        .setMessage("Reset kalkulator zakat?")
		        .setNegativeButton(android.R.string.no, null)
		        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        	@Override
		            public void onClick(DialogInterface arg0, int arg1) {
		                KalkulatorZakat.this.resetSession();
		                KalkulatorZakat.this.restoreSessionValues();
		                KalkulatorZakat.this.updateLabels();
		        	}
		        }).create().show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == ID_ACTIVITY_ZAKAT_HARTA) {
				zHarta = data.getDoubleExtra(ID_RETURN_ZAKAT_HARTA, 0.0);
	        } else if (requestCode == ID_ACTIVITY_ZAKAT_PROFESI) {
				zProfesi = data.getDoubleExtra(ID_RETURN_ZAKAT_PROFESI, 0.0);
	        } else if (requestCode == ID_ACTIVITY_ZAKAT_USAHA) {
				zUsaha = data.getDoubleExtra(ID_RETURN_ZAKAT_USAHA, 0.0);
	        }
			this.updateLabels();
	    } // End if RESULT_OK
	} // End void
	
	private void updateLabels() {
		txtJmlZakatHarta.setText(SiZakatGlobal.rupiahFormat(zHarta));
		txtJmlZakatProfesi.setText(SiZakatGlobal.rupiahFormat(zProfesi));
		txtJmlZakatUsaha.setText(SiZakatGlobal.rupiahFormat(zUsaha));
		zTotal = zHarta + zProfesi + zUsaha;
		txtJmlZakatFinal.setText(SiZakatGlobal.rupiahFormat(zTotal));
	}
	
	/**
	 * Merestore sesi
	 */
	private void restoreSessionValues() {
		SharedPreferences settings = this.getSharedPreferences(SiZakatGlobal.PREFS_NAME, 0);
		double tmp = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_BESARNISAB, 0.0f);
		this.onNisabComplete(tmp);
		zHarta = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA, 0.0f);
		zProfesi = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI, 0.0f);
		zUsaha = settings.getFloat(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA, 0.0f);
	}
	
	/**
	 * Reset semua sesi kalkulator zakat
	 */
	public void resetSession() {
		// Save session
		SharedPreferences settings = getSharedPreferences(SiZakatGlobal.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + KalkulatorZakatHarta.PREFS_POSTFIX_EMAS);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + KalkulatorZakatHarta.PREFS_POSTFIX_ESTATE);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + KalkulatorZakatHarta.PREFS_POSTFIX_MOBIL);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + KalkulatorZakatHarta.PREFS_POSTFIX_SAHAM);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + KalkulatorZakatHarta.PREFS_POSTFIX_TUNAI);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATHARTA + KalkulatorZakatHarta.PREFS_POSTFIX_UTANG);
		
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + KalkulatorZakatProfesi.PREFS_POSTFIX_BIAYARUTIN);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + KalkulatorZakatProfesi.PREFS_POSTFIX_BIAYATAHUN);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + KalkulatorZakatProfesi.PREFS_POSTFIX_BONUS);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATPROFESI + KalkulatorZakatProfesi.PREFS_POSTFIX_GAJI);
		
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA + KalkulatorZakatUsaha.PREFS_POSTFIX_ASET);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA + KalkulatorZakatUsaha.PREFS_POSTFIX_KEPEMILIKAN);
		editor.remove(SiZakatGlobal.PREFS_ZAKAT_ZAKATUSAHA + KalkulatorZakatUsaha.PREFS_POSTFIX_UTANG);
		editor.commit();
	}
}
