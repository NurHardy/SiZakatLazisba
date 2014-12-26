package org.lazisba.sizakat;

import java.util.Date;

public class RssItem {

	private final String title;
    private final String link;
    private final Date date_;
    
    public RssItem(String title, String link, Date tanggal) {
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

	public Date getDate() {
		return date_;
	}

}
