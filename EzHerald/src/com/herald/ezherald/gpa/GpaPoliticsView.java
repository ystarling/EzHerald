package com.herald.ezherald.gpa;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class GpaPoliticsView extends View {
	String content;
	boolean state;
	Paint paint = new Paint();
	public GpaPoliticsView(Context context,String content,boolean state) {
		super(context);
		this.content = content;
		this.state = state;
	}
	@Override
	protected void onDraw(Canvas canvas){
		 paint.setTextSize(25);
		 canvas.drawText(content, 0, 0, paint);
		 if(state == true){
			 this.setBackgroundColor(Color.GREEN);
		 }else{
			 this.setBackgroundColor(Color.RED);
		 }
	}

}
