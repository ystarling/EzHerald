package com.herald.ezherald.exercise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.herald.ezherald.R;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.TyxAccountActivity;
import com.herald.ezherald.account.UserAccount;

public class FragmentC extends Fragment {
	private TextView txt_rate, txt_remainDays, txt_notice, txt_updateTime;
	private Button btn_update;
	private RunTimes runTimes;
	private UserAccount user;

	public View onCreateView(LayoutInflater inflater, ViewGroup group,
			Bundle saved) {
		return inflater.inflate(R.layout.exercise_frag_c, group, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		user = Authenticate.getTyxUser(getActivity());
		if (null == user ) {
			Intent login = new Intent();
			login.setClass(getActivity(), TyxAccountActivity.class);
			startActivity(login);
			//getActivity().finish();
		} else {
			txt_rate = (TextView) getActivity().findViewById(R.id.txt_rate);
			txt_remainDays = (TextView) getActivity().findViewById(
					R.id.txt_remain_days);
			txt_notice = (TextView) getActivity().findViewById(R.id.txt_notice);
			txt_updateTime = (TextView) getActivity().findViewById(
					R.id.txt_update_time);
			btn_update = (Button) getActivity().findViewById(R.id.btn_update);
			runTimes = new RunTimes(getActivity(),this);
			btn_update.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					btn_update.setText("正在更新");
					update(user);
					
				}

			});
			//update(user);
			show();
			/*
			if (runTimes.isSet()) {
				show();
			} else {
				
			}
			*/
		}

	}

	private void show() {
		final String noData = "还没有数据";
		/*
		if (runTimes.getRate() != RunTimes.DEFAULT_RATE) {
			txt_rate.setText(String.format("你的次数击败了\n%d%%的同学",
					(int) (runTimes.getRate() * 100)));// TODO
														// 1-runTimes.getRate()?
		} else {
			txt_rate.setText(noData);
		}
		*/
		if (runTimes.getRemainDays() > 0
				&& runTimes.getAdviceTime() != RunTimes.DEFAULT_ADVICE_TIME) {
			txt_remainDays.setText(String.format("本学期还剩%d天上课\n\n建议你每周至少跑%d次",
					runTimes.getRemainDays(), runTimes.getAdviceTime()));
			if (runTimes.getAdviceTime() <= 0) {
				txt_remainDays.setText("已经跑够了，要挑战满勤么？");
			} else if (runTimes.getAdviceTime() < 3)
				txt_remainDays.setTextColor(Color.GREEN);
			else if (runTimes.getAdviceTime() <= 4)
				txt_remainDays.setTextColor(Color.BLUE);
			else
				txt_remainDays.setTextColor(Color.RED);
		} else if (runTimes.getRemainDays() < 0) {
			txt_remainDays.setText("跑操已经结束或无信息");
		} else {
			txt_remainDays.setText(noData);
		}
		/*
		if (runTimes.getAverageRunTime() != RunTimes.DEFAULT_AVERAGE_RUN_TIME) {
			txt_notice.setText("平均打卡时间：" + runTimes.getAverageRunTime());
		} else {
			txt_notice.setText(noData);
		}
		*/
		if (runTimes.getUpdateTime() != RunTimes.DEFAULT_UPDATE_TIME) {
			txt_updateTime.setText("更新于" + runTimes.getUpdateTime());
		} else {
			txt_updateTime.setText("");
		}

	}

	private void update(UserAccount user) {
		runTimes.update(user);
	}

	public void onFailed() {
		// TODO Auto-generated method stub
		Log.w("err","failed herer!!!!!!!!!!!CCF");
		btn_update.setText("更新");
		Activity act = getActivity();
		if(act != null){
			Toast.makeText(act, "更新失败", Toast.LENGTH_LONG).show();
			show();
		}
		
	}

	public void onSuccess() {
		// TODO Auto-generated method stub
		Log.w("err","failed herer!!!!!!!!!!!CCS");
		btn_update.setText("更新");
		Activity act = getActivity();
		if(act != null){
			Toast.makeText(act, "更新成功", Toast.LENGTH_LONG).show();
			show();
		}
			
	}
	
}
