package com.herald.ezherald.freshman;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class FreshmanContentFragment extends SherlockFragment {
	private int type;
	private ListView listView;
	private FreshmanListViewAdapter adapter;
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
		listView = (ListView)getActivity().findViewById(R.id.lv_content);
		listView.setAdapter(adapter);
		/*
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				adapter.data.update();
				return false;
			}
		});
		*/
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Log.v("click",""+id);
				String detail = adapter.getInfo()[type][(int) id];
				Bundle bundle = new Bundle();
				bundle.putString("detail", detail);
				bundle.putInt("type",type);
				Intent intent = new Intent(getActivity(),FreshmanDetail.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});
	}


}
