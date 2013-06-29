package com.herald.ezherald.academic;

public class JwcInfo {
	private String type;
	private String title;
	private String date;
	private String intro;

	public JwcInfo(String tp, String t, String d, String i) {
		type = tp;
		title = t;
		date = d;
		intro = i;
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

	public String GetIntro() {
		return intro;
	}

}
