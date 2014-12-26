package org.lazisba.sizakat;

import org.lazisba.sizakat.LazisbaHome.PlaceholderFragment;
import org.lazisba.sizakat.dialogs.zakat_DlgNisab;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class KalkulatorZakat extends ActionBarActivity implements zakat_DlgNisab.OnCompleteListener {

	private double nisabFinal = .0f;
	private TextView txtNisab;
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
				startActivity(intent);
		    }
		});
		
		RelativeLayout bZakatProfesi = (RelativeLayout) findViewById(R.id.zakat_boxZakatProfesi);
		bZakatProfesi.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	Intent intent = new Intent(KalkulatorZakat.this, KalkulatorZakatProfesi.class);
				startActivity(intent);
		    }
		});
		
		RelativeLayout bZakatUsaha = (RelativeLayout) findViewById(R.id.zakat_boxZakatUsaha);
		bZakatUsaha.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	Intent intent = new Intent(KalkulatorZakat.this, KalkulatorZakatUsaha.class);
				startActivity(intent);
		    }
		});
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onNisabComplete(double nisab) {
		// TODO Auto-generated method stub
		nisabFinal = nisab;
		txtNisab.setText(String.format("Rp. %.2f", nisabFinal));
	}
}
