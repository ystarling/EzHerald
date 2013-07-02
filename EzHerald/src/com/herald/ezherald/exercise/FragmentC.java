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
		final String noData = "��û������";
		
		if (runTimes.getRate() != RunTimes.DEFAULT_RATE) {
			txt_rate.setText(String.format("��Ĵ���������%d%%��ͬѧ",
					(int) (runTimes.getRate() * 100)));
		}else{
			txt_rate.setText(noData);
		}
		
		if (runTimes.getRemainDays() != RunTimes.DEFAULT_REMAIN_DAYS && runTimes.getAdviceTime()!=RunTimes.DEFAULT_ADVICE_TIME) {
			txt_remainDays.setText(String.format("��ѧ�ڻ�ʣ%d��,������ÿ��������%d��",
					runTimes.getRemainDays(), runTimes.getAdviceTime()));
		}else{
			txt_remainDays.setText(noData);
		}
		if (runTimes.getAverageRunTime() != RunTimes.DEFAULT_AVERAGE_RUN_TIME) {
			txt_notice.setText("ƽ����ʱ�䣺" + runTimes.getAverageRunTime());
		}else{
			txt_notice.setText(noData);
		}
		if (runTimes.getUpdateTime() != RunTimes.DEFAULT_UPDATE_TIME) {
			txt_updateTime.setText("������" + runTimes.getUpdateTime());
		}else{
			txt_updateTime.setText("");
		}
		
	}
	
	private void update(){
		runTimes.update();
	}
	
	
}
