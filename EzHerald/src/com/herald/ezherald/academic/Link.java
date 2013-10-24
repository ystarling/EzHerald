package com.herald.ezherald.academic;

class Link
{
	String title;
	String url;
	public Link(String name, String u)
	{
		title = name;
		url = u;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getUrl()
	{
		return this.url;
	}
}
