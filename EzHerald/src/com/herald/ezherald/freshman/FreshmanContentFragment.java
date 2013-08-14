package com.herald.ezherald.freshman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class FreshmanContentFragment extends SherlockFragment {
	private int type;
	private ListView listView;
	private ListAdapter adapter;
	public void setType(int type){
		this.type = type;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		return inflater.inflate(R.layout.freshman_content,group,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		adapter = new FreshmanListViewAdapter(type,getActivity());
		listView = (ListView)getActivity().findViewById(R.id.list);
		listView.setAdapter(adapter);
	}


}
