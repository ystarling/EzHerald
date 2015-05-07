package com.herald.ezherald.academic;

import java.util.ArrayList;
import java.util.List;

public class JwcInfo {
	private String type;
	private String title;
	private String date;
	private String href;
	private int id;
	private String con;
	private List<Link> appendixs;

	public JwcInfo(String tp, String t, String d, int jid,String hre) {
		type = tp;
		title = t;
		date = d;
		id = jid;
		href=hre;
		appendixs = new ArrayList<Link>();
	}
	
	public void setContent(String conn)
	{
		this.con = conn;
	}
	
	public void setAppendix(List<Link> links)
	{
		this.appendixs = links;
	}
	

	public String GetType() {
		return type;
	}

	public String GetTitle() {
		return title;
	}

	public String GetDate() {
		return date;
	}

	public String GetHref(){return href;}

	public int GetId() {
		return id;
	}
	
	public String getContent()
	{
		return this.con;
	}
	
	public List<Link> getAppendixs()
	{
		return this.appendixs;
	}
	

}
