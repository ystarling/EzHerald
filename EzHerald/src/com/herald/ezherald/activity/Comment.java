package com.herald.ezherald.activity;

public class Comment {
	public int commentId;
	public String content;
	public String name;
	public String date;
	
	public Comment(int id,String con, String n, String d)
	{
		commentId = id;
		content = con;
		name = n;
		date = d;
	}
}
