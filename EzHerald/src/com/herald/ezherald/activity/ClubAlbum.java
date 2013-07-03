package com.herald.ezherald.activity;

public class ClubAlbum {
	private String name;
	private String face_url;
	
	public ClubAlbum(String n, String url)
	{
		name = n;
		face_url = url;
	}
	
	public String getAlbumName()
	{
		return name;
	}
	
	public String getAlbumFaceUrl()
	{
		return face_url;
	}

}

