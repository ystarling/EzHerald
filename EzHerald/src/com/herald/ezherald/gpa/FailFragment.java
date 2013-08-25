package com.herald.ezherald.gpa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class FailFragment extends SherlockFragment {
	private Button btn_fresh;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved)
	{
		View v =inflater.inflate(R.layout.gpa_fragment_failed, group, false);
		btn_fresh = (Button)v.findViewById(R.id.button_resfresh);
		return v;
	}
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		btn_fresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = getActivity().getIntent();
				getActivity().finish();
				startActivity(it);
			}
		});
	}
}
