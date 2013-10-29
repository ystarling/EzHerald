package com.herald.ezherald.freshman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class FreshmanDetailFragment extends SherlockFragment{
	private TextView txt_Detail;
	private String detail;
	public void setDetail(String str) { 
		detail = str;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		//super.onCreate(savedInstanceState);
			return inflater.inflate(R.layout.freshman_detail,group,false);
	}
	private void initView() {
		txt_Detail = (TextView)getActivity().findViewById(R.id.txt_detail);
		txt_Detail.setText(detail);
	}
}
