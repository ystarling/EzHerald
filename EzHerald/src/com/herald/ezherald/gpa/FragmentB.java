package com.herald.ezherald.gpa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.herald.ezherald.R;

public class FragmentB extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved)
	{
		return inflater.inflate(R.layout.gpa_frag_b, group, false);
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		ExpandableListView elv = (ExpandableListView)getActivity().findViewById(R.id.eList);
		final GpaAdapter adapter = new GpaAdapter(getActivity());
		elv.setAdapter(adapter);
		Button update = (Button)getActivity().findViewById(R.id.btn_update);
		update.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				adapter.update();
			}
		});
	}
}
