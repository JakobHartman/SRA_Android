package com.special;

import android.graphics.Bitmap;

public class ListItem {
	private int imageId;
	private String title;
	private String desc;
	private String nr;
	private String nrtxt;
    private Bitmap pictureTaken;
	
	public ListItem(int imageId, String title, String desc, String nr, String nrtxt,Bitmap pic) {
		this.imageId = imageId;
		this.title = title;
		this.desc = desc;
		this.nr = nr;
		this.nrtxt = nrtxt;
        this.pictureTaken = pic;
	}

    public Bitmap getPictureTaken() {
        return pictureTaken;
    }

    public void setPictureTaken(Bitmap pictureTaken) {
        this.pictureTaken = pictureTaken;
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
	public String getNr(){
		return nr;
	}
	public String getNrTxt(){
		return nrtxt;
	}
	@Override
	public String toString() {
		return title + "\n" + desc;
	}	
}