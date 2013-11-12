package com.herald.ezherald.radio;

import android.os.Bundle;

import com.herald.ezherald.BaseFrameActivity;

public class RadioActivity extends BaseFrameActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SetBaseFrameActivity(new RadioFragment());
		super.onCreate(savedInstanceState);
	}
}
