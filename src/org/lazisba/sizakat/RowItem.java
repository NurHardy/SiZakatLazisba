package org.lazisba.sizakat;

import android.os.Parcel;
import android.os.Parcelable;

public class RowItem implements Parcelable {
    private int imageId;
    private String title;
    private String desc;
     
    public RowItem(int imageId, String title, String desc) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
    }
    public RowItem(Parcel in) {
        this.title = in.readString();
        this.desc = in.readString();
        this.imageId = in.readInt();
    }
    
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(title);
		out.writeString(desc);
		out.writeInt(imageId);
	}
	
	public static final Parcelable.Creator<RowItem> CREATOR
	    = new Parcelable.Creator<RowItem>() {
		public RowItem createFromParcel(Parcel in) {
		    return new RowItem(in);
		}
		
		public RowItem[] newArray(int size) {
		    return new RowItem[size];
		}
	};

}
