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

public class FragmentC extends Fragment {
	private TextView txt_rate,txt_remainDays,txt_notice,txt_updateTime;
	private Button btn_update;
	private RunTimes runTimes;
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		return inflater.inflate(R.layout.exercise_frag_c, group,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		txt_rate = (TextView) getActivity().findViewById(R.id.txt_rate);
		txt_remainDays = (TextView)getActivity().findViewById(R.id.txt_remain_days);
		txt_notice = (TextView)getActivity().findViewById(R.id.txt_notice);
		txt_updateTime = (TextView)getActivity().findViewById(R.id.txt_update_time);
		btn_update = (Button)getActivity().findViewById(R.id.btn_update);
		runTimes = new RunTimes(getActivity());
		btn_update.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				update();
				show();
			}
			
		});
		if(runTimes.isSet()){
			show();
		}else{
			update();
			show();
		}
	}
	private void show(){
		
		txt_rate.setText( String.format("你的次数超过了%d的同学", (int)(runTimes.getRate()*100)) );
		txt_remainDays.setText(String.format("本学期还剩%d天,建议你每周至少跑%d次",runTimes.getRemainDays(),runTimes.getAdviceTime()));
		txt_notice.setText("平均打卡时间："+runTimes.getAverageRunTime());
		txt_updateTime.setText("更新于"+runTimes.getUpdateTime());
		
	}
	
	private void update(){
		runTimes.update();
	}
	
	
}
