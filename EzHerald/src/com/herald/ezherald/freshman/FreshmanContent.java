package com.herald.ezherald.freshman;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.BaseFrameActivity;

public class FreshmanContent extends BaseFrameActivity {
	private FreshmanContentFragment mContentFrag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getIntent().getExtras();
		int type = bundle.getInt("type");
		mContentFrag = new FreshmanContentFragment();
		mContentFrag.setType(type);
		super.SetBaseFrameActivity(mContentFrag);
		super.onCreate(savedInstanceState);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// 返回上一层，因为已经被finish，需要重新intent
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent it = new Intent(this,FreshmanActivity.class);
			startActivity(it);
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			//覆盖这个case，实现按应用图标返回刚才的应用
			Intent it = new Intent(this,FreshmanActivity.class);
			startActivity(it);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
