package com.herald.ezherald.exercise;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class ExerciseChat extends View {
	private int width,height,radius;
	private Paint paint;
	private RectF rect;
	private RunTimes runtime;
	public ExerciseChat(Context context, AttributeSet attrs) {
		super(context, attrs);
		runtime = new RunTimes(context);
		paint = new Paint();
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		width = getWidth();
		height = getHeight();
		radius = Math.min(width, height)/2;
		radius*=0.65;
		
		rect = new RectF(width/2-radius, radius*0.3f, width/2+radius,2.3f*radius);
		paint.setTextSize(radius*0.4f);
		canvas.drawText(String.format("%d%%", runtime.getTimes()*100/45), width/2*0.9f, height/2-radius*0.1f,paint);
		
		float startAngle = -90, sweepAngle = ((float)runtime.getTimes())/45*360;
		if(sweepAngle>=360) {
			sweepAngle = 360;
		}
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(radius*0.6f);
		paint.setColor(Color.RED);
		canvas.drawArc(rect, startAngle, sweepAngle, false, paint);
		
		paint.setColor(Color.GRAY);
		canvas.drawArc(rect, sweepAngle-90, 360-sweepAngle, false, paint);
		
		paint.setColor(Color.WHITE);
		canvas.drawArc(rect, startAngle, 1, false, paint);
		canvas.drawArc(rect, sweepAngle-90, 1, false, paint);
		
		
		super.onDraw(canvas);
	}
	

}
