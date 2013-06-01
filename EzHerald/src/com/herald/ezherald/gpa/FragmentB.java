package com.herald.ezherald.gpa;


import com.herald.ezherald.R;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentB extends Fragment {
	Button button;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved)
	{
		return inflater.inflate(R.layout.frag_b, group, false);
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		/*
		button = (Button) getActivity().findViewById(R.id.button2);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Fragment B");
				builder.setMessage("What would you like to do?");
				builder.setPositiveButton("Nothing", null);
				builder.setNegativeButton("Leave me alone!", null);
				builder.show();
			}
		});*/
	}
}
