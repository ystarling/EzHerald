package com.herald.ezherald.gpa;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleChat extends View {
	private Paint paint;
	private RectF rect;
	private ArrayList<Record> records,sorces[];
	private float theta[];
	private float radius;
	private float sum;
	private float startAngle;
	private final int TOTAL = 5;
	private final String[] txt= {"  ","<2.0","2.0~3.0","3.0~4.0",">4.0"};
	private boolean drawed;
	
	public CircleChat(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint  = new Paint();
		records = (ArrayList<Record>) new GpaInfo(context).getRecords();
		sorces = new ArrayList[TOTAL];
		
		
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		for(int i=0;i<TOTAL;i++)
			sorces[i] = new ArrayList<Record>();
		radius = getWidth()>getHeight()?getHeight()/2:getWidth()/2;
		rect = new RectF(0,0,2*radius,2*radius);
		sum = 0;
		//int colors[] = {Color.LTGRAY,Color.parseColor("#FF9900"),Color.CYAN,Color.parseColor("#BDFF1B"),Color.parseColor("#B70094")};
		int colors[] = {Color.rgb(188, 200, 224),Color.rgb(147, 169, 208),Color.rgb(79, 129, 188),Color.rgb(69, 115, 167),Color.rgb(56, 97, 143)};
		for(Record r:records){
			sum += r.getCredit();
			if(r.getPoint()>0){
				sorces[(int)(r.getPoint())].add(r);
			}else{
				sorces[0].add(r);
			}
		}
		
		theta = new float[TOTAL];
		for(int i=0;i<TOTAL;i++){
			float ssum=0;
			for(Record r:sorces[i]){
				ssum += r.getCredit();
			}
			theta[i] = 360*ssum/sum;
		}
		
		float start = (float) (Math.abs(Math.random()*360)),ac = 0;
		if(drawed == false) {
			startAngle  = start;
			drawed = true;
		}
		
		for(int i=0;i<TOTAL;i++){
			paint.setColor(colors[i]);
			ac = theta[i];
			canvas.drawArc(rect, start, ac, true, paint);
			
			if(ac>0){
				paint.setTextSize(42);
				paint.setTextAlign(Align.CENTER);
				paint.setColor(Color.WHITE);
				canvas.drawText(txt[i],radius+radius*(0.65f)*(float)Math.cos((start+ac/2)/180*Math.PI),radius+radius*(0.65f)*(float)Math.sin((start+ac/2)/180*Math.PI),paint);
			}
			start=(start+ac)%360;
		}
		
	}
	/**
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @return 点对应的课程
	 */
	public ArrayList<Record> onTouch(float x,float y){
		x -= radius;
		y -= radius;
		double ag;
		if(x>0 && y>0)
			ag = Math.atan(y/x);
		else if(x>0 && y<0 )
			ag = Math.atan(y/x)+2*Math.PI;
		else if(x<0 && y>0 )
			ag = Math.PI +Math.atan(y/x);
		else //if(x<0 && y<0 )
			ag = Math.atan(y/x)+Math.PI;
		ag *= 180/Math.PI;//弧度化为角度
		ag -= startAngle;
		if(ag<0)
			ag+=360f;
		float su =0; 
		int i;
		for(i=0;i<TOTAL;i++){
			su += theta[i];
			if(su > ag){
				break;
			}
		}
		if(i>=0 && i<TOTAL)
			return sorces[i];
		else
			return null;
	}
	
	public boolean hasData() {
		return records !=null && !records.isEmpty();
	}
	
}
