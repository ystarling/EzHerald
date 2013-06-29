package com.herald.ezherald.exercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.herald.ezherald.R;



/**
 * @author xie
 * 显示跑操次数的信息
 */
public class FragmentB extends Fragment {
	private RunTimesInfo runTimesInfo; 
	private TextView txtTimes;
	private EditText edtAdjust;
	private Button btnAdjust;
	private Button btnUpdate;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		return inflater.inflate(R.layout.exercise_frag_b, group,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		runTimesInfo= new RunTimesInfo(getActivity());
		if(runTimesInfo.isSet()){
			show();
		}else{
			update();
			show();
		}
		edtAdjust = (EditText)getActivity().findViewById(R.id.edtTxt_adjust);
		btnAdjust = (Button)getActivity().findViewById(R.id.btn_adjust);
		btnUpdate = (Button)getActivity().findViewById(R.id.btn_update);
		
		btnAdjust.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				try {
					int adjust = Integer.parseInt(edtAdjust.getText()
							.toString());
					runTimesInfo.setAdjustTimes(adjust);
					runTimesInfo.save();
					show();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "数据无效", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		btnUpdate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				update();
				show();
			}
			
		});
	}
	/**
	 * 更新信息
	 */
	public void update(){
		runTimesInfo.update();
	}
	/**
	 * 显示信息
	 */
	public void show(){
		txtTimes = (TextView)getActivity().findViewById(R.id.txt_Times);
		edtAdjust = (EditText)getActivity().findViewById(R.id.edtTxt_adjust);
		
		if (runTimesInfo.getAdjustTimes() != 0) {
			txtTimes.setText(runTimesInfo.getTimes() + " + "
					+ runTimesInfo.getAdjustTimes());
			Log.w("msg", runTimesInfo.getTimes() + " + "
					+ runTimesInfo.getAdjustTimes());
		}else{
			txtTimes.setText( runTimesInfo.getTimes()+"");
		}
	}
}
