package com.herald.ezherald.exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.herald.ezherald.R;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.TyxAccountActivity;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.api.APIAccount;
import com.herald.ezherald.api.APIAccountActivity;

/**
 * @author xie 显示跑操次数的信息
 */
public class FragmentB extends Fragment {
	private RunTimes runTimesInfo;
	private TextView txtTimes;
	private EditText edtAdjust;
	private Button btnAdjust;
	private Button btnUpdate;
	private TextView txtUpdateTime;
	private APIAccount user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group,
			Bundle saved) {
		return inflater.inflate(R.layout.exercise_frag_b, group, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		edtAdjust = (EditText) getActivity().findViewById(
				R.id.edtTxt_adjust);
		btnAdjust = (Button) getActivity().findViewById(R.id.btn_adjust);
		edtAdjust.setVisibility(View.INVISIBLE);
		btnAdjust.setVisibility(View.INVISIBLE);
		user = new APIAccount(getActivity());
		if (!user.isUUIDValid()) {
			Intent login = new Intent();
			login.setClass(getActivity(), APIAccountActivity.class);
			startActivity(login);
		} else {
			btnUpdate = (Button) getActivity().findViewById(R.id.btn_update);
			txtTimes = (TextView) getActivity().findViewById(R.id.txt_Times);
			txtUpdateTime = (TextView) getActivity().findViewById(
					R.id.txt_update_time);
			runTimesInfo = new RunTimes(getActivity(),this);
			
			txtTimes.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					//edtAdjust.setVisibility(View.VISIBLE);
					//btnAdjust.setVisibility(View.VISIBLE);
					return false;
				}

			});

			if (runTimesInfo.isSet()) {
				show();
			} else {
				//update();
				show();
			}

			btnAdjust.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						int adjust = Integer.parseInt(edtAdjust.getText()
								.toString());
						runTimesInfo.setAdjustTimes(adjust);
						runTimesInfo.save();
						show();
					} catch (Exception e) {
						Toast.makeText(getActivity(), "数据无效",
								Toast.LENGTH_SHORT).show();
					}
				}

			});

			btnUpdate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					btnUpdate.setText("正在更新...");
					update();
				}

			});
		}

	}

	/**
	 * 更新信息
	 */
	public void update() {
		runTimesInfo.update();
	}

	/**
	 * 显示信息
	 */
	public void show() {
		if (runTimesInfo.getTimes() == RunTimes.DEFAULT_TIMES) {
			txtTimes.setText("还没有数据");
		} else {
			if (runTimesInfo.getAdjustTimes() != RunTimes.DEFAULT_ADJUST_TIMES) {
				txtTimes.setText(""
						+ (runTimesInfo.getTimes() + +runTimesInfo
								.getAdjustTimes()));
				Log.w("msg",
						runTimesInfo.getTimes() + " + "
								+ runTimesInfo.getAdjustTimes());
			} else {
				txtTimes.setText(runTimesInfo.getTimes() + "");
			}
		}
		if (runTimesInfo.getUpdateTime() != RunTimes.DEFAULT_UPDATE_TIME) {
			txtUpdateTime.setText(String.format("更新于%s",
					runTimesInfo.getUpdateTime()));
		} else {
			txtUpdateTime.setText("");
		}
	}
	public void onSuccess(){
		btnUpdate.setText("更新");
		Activity act = getActivity();
		if(act != null){
			Toast.makeText(act, "更新成功", Toast.LENGTH_LONG).show();
			show();
		}
			
	}

	public void onFailed() {
		Log.w("err","failed herer!!!!!!!!!!!BBF");
		btnUpdate.setText("更新");
		Activity act = getActivity();
		if(act != null){
			Toast.makeText(act, "更新失败", Toast.LENGTH_LONG).show();
			show();
		}
			
	}

    @Override
    public void onResume() {
        super.onResume();
        //TODO check login
    }
}
