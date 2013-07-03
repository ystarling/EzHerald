
package com.herald.ezherald.gpa;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.herald.ezherald.R;

public class FragmentB extends Fragment {
	private ExpandableListView elv;
	private TextView txtGpa;
	private Button btnUpdate,btnCalc;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved)
	{
		return inflater.inflate(R.layout.gpa_frag_b, group, false);
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		txtGpa = (TextView)getActivity().findViewById(R.id.txt_gpa);
		elv = (ExpandableListView)getActivity().findViewById(R.id.eList);
		final GpaAdapter adapter = new GpaAdapter(getActivity());
		elv.setAdapter(adapter);
		btnUpdate = (Button)getActivity().findViewById(R.id.btn_update);
		btnUpdate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				adapter.update();
			}
		});
		btnCalc = (Button)getActivity().findViewById(R.id.btn_calc);
		btnCalc.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtGpa.setText(String.format("所选绩点为:%.2f", adapter.getGpaInfo().calcAverage()));
			}
			
		});
		
	}
}
