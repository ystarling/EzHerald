package com.herald.ezherald.gpa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.IDCardAccountActivity;
import com.herald.ezherald.account.UserAccount;
import com.herald.ezherald.api.APIAccount;


public class GPAActivity extends BaseFrameActivity {
	Fragment content;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		// TODO Auto-generated method stub
        APIAccount apiAccount = new APIAccount(this);
        if(!apiAccount.isUUIDValid()){
            Intent login = new Intent();
            login.putExtra("remoteCall", true);
            login.setClass(this,IDCardAccountActivity.class);
            startActivity(login);
            GpaDbModel model = new GpaDbModel(this);
            model.open();
            model.clear();
            model.close();//删除旧用户的数据
            //mContentFrag = new FailFragment();
            content = new FailFragment();
            super.SetBaseFrameActivity(content);
        }
//		UserAccount user = Authenticate.getIDcardUser(this);
//		if(null == user){//TODO CHANGE CONDITION
//			Intent login = new Intent();
//			login.putExtra("remoteCall", true);
//			login.setClass(this,IDCardAccountActivity.class);
//			startActivity(login);
//			GpaDbModel model = new GpaDbModel(this);
//			model.open();
//			model.clear();
//			model.close();//删除旧用户的数据
//			//mContentFrag = new FailFragment();
//			content = new FailFragment();
//			super.SetBaseFrameActivity(content);
//}
    else{
			content  = new  GpaFragment();
			SetBaseFrameActivity(content);
		}
		super.onCreate(savedInstanceState);

	}
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		
		return super.onCreateOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * 上侧Title位置的按钮点击相应
		 */
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//UserAccount user = Authenticate.getIDcardUser(this);
        APIAccount apiAccount = new APIAccount(this);
		if(!apiAccount.isUUIDValid()){
			mContentFrag = new FailFragment();
			super.SetBaseFrameActivity(mContentFrag);
		}else{
			content = new GpaFragment();
			super.SetBaseFrameActivity(content);
			InitBaseFrame();
		}
		super.onResume();
	}

}
