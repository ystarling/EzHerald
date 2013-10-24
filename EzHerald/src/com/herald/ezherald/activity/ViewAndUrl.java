package com.herald.ezherald.activity;

import java.net.URL;

import android.view.View;

public class ViewAndUrl
{
	public View view;
	public URL url;
	public int flag;
	public int id;
	public ViewAndUrl(View v,URL u,int f,int i)
	{
		view= v;
		url = u;
		flag = f;
		id = i;
	}
}
