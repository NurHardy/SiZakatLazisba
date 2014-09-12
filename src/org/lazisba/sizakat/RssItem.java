package org.lazisba.sizakat;

public class RssItem {

	private final String title;
    private final String link;
    private final String date_;
    
    public RssItem(String title, String link, String tanggal) {
        this.title = title;
        this.link = link;
        this.date_ = tanggal;
    }
 
    public String getTitle() {
        return title;
    }
 
    public String getLink() {
        return link;
    }

	public String getDate() {
		return date_;
	}

}
