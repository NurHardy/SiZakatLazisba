package org.lazisba.sizakat;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class KalkulatorZakat extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kalkulator_zakat);
		
		final EditText txtBoxHargaEmas = (EditText) findViewById(R.id.zakat_txtbox_nisab);
		final EditText txtBoxBesarNisab = (EditText) findViewById(R.id.zakat_txtbox_nisabfinal);
		
		txtBoxHargaEmas.addTextChangedListener(new TextWatcher() {

          public void afterTextChanged(Editable s) {
        	  double hargaEmasGram = 0.0f;
        	  double besarNisab = 0.0f;
        	  hargaEmasGram = Double.parseDouble(txtBoxHargaEmas.getText().toString());
        	  
        	  besarNisab = 85 * hargaEmasGram;
        	  txtBoxBesarNisab.setText(String.format("Rp. %.2f", besarNisab));
          }

          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
}
