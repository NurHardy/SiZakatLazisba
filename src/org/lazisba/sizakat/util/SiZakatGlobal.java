package org.lazisba.sizakat.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * @author Iyan
 *
 */
public final class SiZakatGlobal {
	// == REST CONSTANTS ==
	public static final int APP_VERSION = 1;
	public static final String APP_APIKEY			= "informatikaundip";
	
	public static final String PREFS_NAME			= "SiZakat_Lazisba_prefs";
	
	public static final String PREFS_UTOKEN 		= "siz_usertoken";
	public static final String PREFS_UID			= "siz_userid";
	public static final String PREFS_UFULLNAME		= "siz_userfullname";
	public static final String PREFS_ULEVEL			= "siz_userlevel";
	
	public static final String serviceUrl = "http://siz.lazisba.org/rest/api/"; //"http://nurhardyanto.web.id:80/rest/api/";
	
	// == Seputar Emas
	public static final int PENGALI_EMAS = 85;
	public static final double HARGA_EMAS_DEFAULT = 498002.995f; // Harga emas 19 April 2015, 12:20
	
	// == ZAKAT PREFS ==
	public static final String PREFS_ZAKAT_HARGAEMAS = "siz_zakat_hargaemas";
	public static final String PREFS_ZAKAT_BESARNISAB = "siz_zakat_nisab";
	
	public static final String PREFS_ZAKAT_ZAKATHARTA = "siz_zakat_zakatharta";
	public static final String PREFS_ZAKAT_ZAKATPROFESI = "siz_zakat_zakatprofesi";
	public static final String PREFS_ZAKAT_ZAKATUSAHA = "siz_zakat_zakatusaha";
	
	// == METHODS ======
	public SiZakatGlobal() {
		// TODO Auto-generated constructor stub
	}

	public static final String getRESTUrl(String method, String verb) {
		return serviceUrl+"/"+method+"/"+verb;	
	}
	
	public static final String getRESTUrl(String method) {
		return getRESTUrl(method, "");	
	}
	
	/**
	 * @param nominal Nilai yang akan diubah
	 * @return String yang sudah terformat rupiah
	 */
	public static final String rupiahFormat(Double nominal) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		//symbols.setCurrencySymbol("Rp. ");
		symbols.setDecimalSeparator(',');

		DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(symbols);
		df.setGroupingSize(3);
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		
		return "Rp. "+ df.format(nominal);
	}
	
	public static final double siZakatParseDouble(String notasi) {
		if (!notasi.trim().equalsIgnoreCase(""))
			return (Double.parseDouble(notasi));
		return 0;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static final void copyToClipboard(Activity activity, String textToCopy, String textId) {
		try {
			int sdk = android.os.Build.VERSION.SDK_INT;
			if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
			    clipboard.setText(textToCopy);
			} else {
			    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE); 
			    android.content.ClipData clip =
			    		android.content.ClipData.newPlainText(
			    				textId,
			    				textToCopy);
			    clipboard.setPrimaryClip(clip);
			}
			Toast.makeText(activity.getApplicationContext(), "Text copied.", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(activity.getApplicationContext(), "Text copy failed.", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}
