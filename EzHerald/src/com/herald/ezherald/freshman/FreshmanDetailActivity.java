package com.herald.ezherald.freshman;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.BaseFrameActivity;

public class FreshmanDetailActivity extends BaseFrameActivity {
	private FreshmanDetailFragment frag;
	private int type;
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getIntent().getExtras();
		String detail = bundle.getString("detail");
		type = bundle.getInt("type");
		frag = new FreshmanDetailFragment();
		frag.setDetail(detail);
		String title = bundle.getString("title");
		frag.setTitle(title);
		super.SetBaseFrameActivity(frag);
		super.onCreate(savedInstanceState);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// 返回上一层，因为已经被finish，需要重新intent
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(this,FreshmanContent.class);
			Bundle bundle = new Bundle();
			bundle.putInt("type", type);
			intent.putExtras(bundle);
			startActivity(intent);
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
			Intent intent = new Intent(this,FreshmanContent.class);
			Bundle bundle = new Bundle();
			bundle.putInt("type", type);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
