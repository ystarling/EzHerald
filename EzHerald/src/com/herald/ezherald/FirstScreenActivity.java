package com.herald.ezherald;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 第一屏
 * @author BorisHe
 *
 */
public class FirstScreenActivity extends Activity {
	//private ImageView mImageView;
	private TextView mTextView;
	private AlphaAnimation mAnimation;
	private long ANIM_DURATION = 1000; //延时（毫秒）
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_frame_first_screen);
		
		//mImageView = (ImageView)findViewById(R.id.first_screen_imageView1);
		mTextView = (TextView)findViewById(R.id.first_screen_textView1);
		
		setAnimation();
		
	}

	private void setAnimation() {
		// TODO Auto-generated method stub
		mAnimation = new AlphaAnimation(1.0f, 0.5f);
		mAnimation.setDuration(ANIM_DURATION);
		mAnimation.setAnimationListener(new MyAnimationListener());
		mTextView.setAnimation(mAnimation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_screen, menu);
		return true;
	}
	
	private class MyAnimationListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			Intent i = new Intent();
			i.setClass(getBaseContext(), MainActivity.class);
			startActivity(i);
			finish(); //关闭Activity跳转到主界面
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			
		}
		
	}

}
