package com.herald.ezherald.radio;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.IDCardAccountActivity;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.gpa.FailFragment;
import com.herald.ezherald.gpa.GpaDbModel;

public class RadioActivity extends BaseFrameActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//UserAccount user = Authenticate.getIDcardUser(this);
		//if(null == user) {
			//turnToLogin();
		//} else {
			SetBaseFrameActivity(new RadioFragment());
			super.onCreate(savedInstanceState);
		//}
	}
}
