package com.herald.ezherald.activity;



import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;

public class VoteDetailActivity extends SherlockActivity {

	public VoteDetailActivity() {
		// TODO Auto-generated constructor stub
	}
	
	@Override 
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acti_vote_detail);
		
		LinearLayout voteLayout = (LinearLayout) this.findViewById(R.id.acti_vote_list);
		TextView tv = new TextView(this);
		tv.setText("dongtai");
		voteLayout.addView(tv);
		
		Bitmap bitmap = Bitmap.createBitmap(30, 100, Config.ARGB_8888);
		Canvas cv = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.FILL);
		cv.drawRect(0, 0, 100, 50, paint);
		cv.save();
		cv.restore();
		
		ImageView iv = new ImageView(this);
//		iv.draw(cv);
		iv.setImageBitmap(bitmap);
		
		voteLayout.addView(iv);
		
		
		
	}
		
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
