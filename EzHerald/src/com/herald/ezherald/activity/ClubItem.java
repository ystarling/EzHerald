package com.herald.ezherald.activity;

public class ClubItem {
	
	private String clubIconUrl;
	private String clubName;
	private boolean haveFocus;
	
	public ClubItem(String url, String name, boolean focus)
	{
		clubIconUrl = url;
		clubName = name;
		haveFocus = focus;
	}
	
	public String getClubIconUrl()
	{
		return clubIconUrl;
	}
	
	public String getClubName()
	{
		return clubName;
	}
	
	public boolean checkFocus()
	{
		return haveFocus;
	}
	

}
