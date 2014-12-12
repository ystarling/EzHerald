package com.herald.ezherald.exercise;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.herald.ezherald.R;



/**
 * @author xie
 * 显示体育系人人早操播报的信息
 *
 */
public class FragmentA extends Fragment {
	private RenrenInfo renren; 
	private TextView txt_info ;
	private TextView txt_date ;
	private Button btn_update;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
            return inflater.inflate(R.layout.exercise_frag_a, group,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		renren = new RenrenInfo(getActivity(),this);
		txt_info = (TextView)getActivity().findViewById(R.id.txt_info);
		txt_info.setMovementMethod(ScrollingMovementMethod.getInstance());//实现多行滚动
		txt_date = (TextView)getActivity().findViewById(R.id.txt_date);
		btn_update = (Button)getActivity().findViewById(R.id.btn_update);
		if(renren.isSet()){
			show();
		}else{
			txt_info.setText("首次使用，正在更新数据");
			txt_date.setText("");
			update();
		}
		btn_update = (Button)getActivity().findViewById(R.id.btn_update);
		btn_update.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				update();
				
			}
		});
	}
	/**
	 * 更新信息
	 */
	private void update(){
		btn_update.setText("正在更新...");
		renren.update();
	}
	/**
	 * 将信息显示
	 */
	public void show(){
		txt_info.setText("  "+renren.getInfo());
		if (renren.getDate() != null) {
			txt_date.setText("更新于" + renren.getDate());
		}
		
	}
	//信息更新成功及失败的显示
	public void onSuccess(){
		Context context = getActivity();
		if(context != null){
			txt_info.setText("  "+renren.getInfo());
		txt_date.setText("更新于" + renren.getDate());
		Toast.makeText(getActivity(),"人人信息更新成功", Toast.LENGTH_LONG).show();
		btn_update.setText("更新");
		}
		
	}
	public void onFailed(){
		Context context = getActivity();
		if(context != null){
			Toast.makeText(context,"人人信息更新失败", Toast.LENGTH_LONG).show();
			btn_update.setText("更新");
		}
			
	}
}

