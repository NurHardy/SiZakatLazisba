package org.lazisba.sizakat.util;

public class SiZakatLoginState {

	private String uToken = null;
	private String uFullname = null;
	
	private int uId = 0;
	private int uLevel = 0;
	
	
	public SiZakatLoginState() {
		// TODO Auto-generated constructor stub
	}

	public Boolean isLoggedIn() {
		return ((uToken != null) && (uId != 0));
	}
	
	public void saveSession(int newUId, String newUToken, String userFullname) {
		uId = newUId; uToken = newUToken; uFullname = userFullname;
	}
	
	public void destroySession() {
		uId = 0; uToken = null;
	}
	
	public int getUid() {return uId;}
	public String getToken() {return uToken;}
	public String getFullname() {return uFullname;}
}
