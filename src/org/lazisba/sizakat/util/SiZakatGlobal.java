package org.lazisba.sizakat.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class SiZakatGlobal {
	public static final int APP_VERSION = 1;
	public static final String APP_APIKEY = "informatikaundip";
	
	public static final String PREFS_NAME = "SiZakat_Lazisba_prefs";
	
	public static final String PREFS_UTOKEN = "siz_usertoken";
	public static final String PREFS_UID = "siz_userid";
	public static final String PREFS_UFULLNAME = "siz_userfullname";
	
	public static final String serviceUrl = "http://siz.lazisba.org/rest/api/"; //"http://nurhardyanto.web.id:80/rest/api/";
	
	public SiZakatGlobal() {
		// TODO Auto-generated constructor stub
	}

	public static final String getRESTUrl(String method, String verb) {
		return serviceUrl+"/"+method+"/"+verb;	
	}
	
	public static final String getRESTUrl(String method) {
		return getRESTUrl(method, "");	
	}
	
	public static final String rupiahFormat(Double nominal) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		symbols.setCurrencySymbol("Rp. ");
		symbols.setDecimalSeparator(',');
		
		DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(symbols);
		df.setGroupingSize(3);
		df.setMaximumFractionDigits(2);
		
		return df.format(nominal);
	}
}
