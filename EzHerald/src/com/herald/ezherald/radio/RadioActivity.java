package com.herald.ezherald.radio;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.herald.ezherald.BaseFrameActivity;

public class RadioActivity extends BaseFrameActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SetBaseFrameActivity(new RadioFragment());
		super.onCreate(savedInstanceState);
	}
}
