package com.herald.ezherald.curriculum;

import android.os.Bundle;
import cn.edu.seu.herald.ws.api.CurriculumService;
import cn.edu.seu.herald.ws.api.HeraldWebServicesFactory;
import cn.edu.seu.herald.ws.api.impl.HeraldWebServicesFactoryImpl;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;

public class CurriculumActivity extends SherlockActivity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.curriculum_activity_main);
	}

}
