package com.herald.ezherald.mainframe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;
/*
 * @author 何博伟
 * @since 20130514
 * 
 * 主界面呈现内容的Fragement
 * 其他各模块可参照本Fragement的定义呈现内容
 * 务必extends SherlockFragment以实现碎片切换
 */
public class MainContentFragment extends SherlockFragment {
	String text = null;
	
	public MainContentFragment()
	{
		text = "Default";
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#setArguments(android.os.Bundle)
	 */
	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
		text = args.getString("text");
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_frame_content, null);
		return v;
	}
	
	
}
