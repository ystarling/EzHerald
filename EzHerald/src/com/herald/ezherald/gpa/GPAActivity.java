package com.herald.ezherald.gpa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.BaseFrameActivity;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.IDCardAccountActivity;
import com.herald.ezherald.account.UserAccount;



public class GPAActivity extends BaseFrameActivity {
	Fragment content;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		// TODO Auto-generated method stub
		UserAccount user = Authenticate.getIDcardUser(this);
		if(null == user){
			Intent login = new Intent();
			login.putExtra("remoteCall", true);
			login.setClass(this,IDCardAccountActivity.class);
			startActivity(login);
			GpaDbModel model = new GpaDbModel(this);
			model.open();
			model.clear();
			model.close();//ɾ�����û�������
			//mContentFrag = new FailFragment();
			content = new FailFragment();
			super.SetBaseFrameActivity(content);
		}else{
			//if(content == null ) { 
				content  = new  GpaFragment();
			//}
			
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
		 * �ϲ�Titleλ�õİ�ť�����Ӧ
		 */
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		UserAccount user = Authenticate.getIDcardUser(this);
		if(null == user){
			mContentFrag = new FailFragment();
			super.SetBaseFrameActivity(mContentFrag);
		}else{
			//if(content == null) {

				content = new GpaFragment();
			//}
			SetBaseFrameActivity(content);
			InitBaseFrame();
		}
		super.onResume();
	}

}
