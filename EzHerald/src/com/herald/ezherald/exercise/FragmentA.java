package com.herald.ezherald.exercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.herald.ezherald.R;


public class FragmentA extends Fragment {
	private final RenrenInfo renren = new RenrenInfo(getActivity());
	private TextView txt_info ;
	private TextView txt_date ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		return inflater.inflate(R.layout.exercise_frag_a, group,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		txt_info = (TextView)getActivity().findViewById(R.id.txt_info);
		txt_date = (TextView)getActivity().findViewById(R.id.txt_date);
		if(renren.isSet()){
			show();
		}else{
			txt_info.setText("正在更新");
			txt_date.setText("");
			update();
		}
		Button btn_update = (Button)getActivity().findViewById(R.id.btn_update);
		btn_update.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				update();
			}
		});
	}
	private void update(){
		renren.update();
		show();
	}
	private void show(){
		txt_info.setText(renren.getInfo());
		txt_date.setText("更新于"+renren.getDate());
	}
}
