package com.herald.ezherald.gpa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.herald.ezherald.R;

public class FragmentA extends Fragment {
	Button button;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved)
	{
		return inflater.inflate(R.layout.gpa_frag_a, group, false);
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		ExpandableListView eLV = (ExpandableListView) getActivity().findViewById(R.id.eListView);
		eLV.setAdapter(new GpaPolitcsAdapter(getActivity()));
	}
}
