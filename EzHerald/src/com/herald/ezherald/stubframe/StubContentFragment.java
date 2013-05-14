package com.herald.ezherald.stubframe;

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
 * 测试用的一个Framgment，测试菜单中点击后Fragment切换
 * 没啥功能，空白一片
 */

public class StubContentFragment extends SherlockFragment {
	String text = null;
	
	public StubContentFragment()
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
		View v = inflater.inflate(R.layout.stub_frame_content, null);
		return v;
	}
	
	
}
