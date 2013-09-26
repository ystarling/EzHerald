package com.herald.ezherald.freshman;
  
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

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
			Intent intent = new Intent(getActivity(),FreshmanContent.class);
			Bundle bundle = new Bundle();
			bundle.putInt("type", type);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	}
	
	void initView(){
		tv_study = (TextView) getActivity().findViewById(R.id.text_study);
		tv_life  = (TextView) getActivity().findViewById(R.id.text_life);
		tv_play  = (TextView) getActivity().findViewById(R.id.text_play);
		tv_faq   = (TextView) getActivity().findViewById(R.id.text_faq);
		
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
