package com.herald.ezherald.freshman;
  
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class FreshmanFragment extends SherlockFragment {
	TextView tv_study,tv_life,tv_play,tv_faq;
	
	class Listener implements OnClickListener{
		private int type;
		public Listener(int type){
			this.type = type;
		}
		@Override
		public void onClick(View v) {
			Toast.makeText(getActivity(), "正在更新", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getActivity(),FreshmanContent.class);
			Bundle bundle = new Bundle();
			bundle.putInt("type", type);
			intent.putExtras(bundle);
			startActivity(intent);
			getActivity().finish();
		}
		
	}
	
	void initView(){
		DisplayMetrics metrics = new DisplayMetrics();
	    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		final int SIZE = metrics.widthPixels*30/100;
		Log.v("size!!",""+SIZE);
		tv_study = (TextView) getActivity().findViewById(R.id.text_study);
		tv_life  = (TextView) getActivity().findViewById(R.id.text_life);
		tv_play  = (TextView) getActivity().findViewById(R.id.text_play);
		tv_faq   = (TextView) getActivity().findViewById(R.id.text_faq);
		Drawable dr_play = getActivity().getResources().getDrawable(R.drawable.freshman_play);
		dr_play.setBounds(0,0,SIZE,SIZE);
		tv_play.setCompoundDrawables(null, dr_play,null, null);
		
		Drawable dr_life = getActivity().getResources().getDrawable(R.drawable.freshman_life);
		dr_life.setBounds(0,0,SIZE,SIZE);
		tv_life.setCompoundDrawables(null, dr_life,null, null);
		
		Drawable dr_study = getActivity().getResources().getDrawable(R.drawable.freshman_study);
		dr_study.setBounds(0,0,SIZE,SIZE);
		tv_study.setCompoundDrawables(null, dr_study,null, null);
		
		Drawable dr_faq = getActivity().getResources().getDrawable(R.drawable.freshman_faq);
		dr_faq.setBounds(0,0,SIZE,SIZE);
		tv_faq.setCompoundDrawables(null, dr_faq,null, null);
		
		
	}
	void setOnclick(){
		tv_study.setOnClickListener(new Listener(FreshmanInfo.STUDY));
		tv_life.setOnClickListener(new Listener(FreshmanInfo.LIFE));
		tv_play.setOnClickListener(new Listener(FreshmanInfo.PLAY));
		tv_faq.setOnClickListener(new Listener(FreshmanInfo.FAQ));
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		initView();
		setOnclick();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		//super.onCreate(savedInstanceState);
			return inflater.inflate(R.layout.freshman_main,group,false);
	}
	
}
