package com.herald.ezherald.academic;

import com.herald.ezherald.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class ListFootView {
	private Context context;
	private View view;
	private RelativeLayout footerView;
	private ProgressBar progressBar;
	private TextView moreView;
	
	public ListFootView(Context c)
	{
		context = c;
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.academic_list_foot_view, null);
		footerView =(RelativeLayout) view.findViewById(R.id.list_footview);
		moreView = (TextView) view.findViewById(R.id.text_view_more);
		progressBar = (ProgressBar) view.findViewById(R.id.footer_progress);
	}
	
	public void setOnClickListener(OnClickListener listener)
	{
		footerView.setOnClickListener(listener);	
	}
	
	public View getFootView()
	{
		return footerView;
	}
	
	public void startRequestData()
	{
		moreView.setText("加载中...");
		progressBar.setVisibility(View.VISIBLE);
		
	}
	
	public void endRequestData()
	{
		moreView.setText("获取更多");
		progressBar.setVisibility(View.GONE);
	}
	
	

}
