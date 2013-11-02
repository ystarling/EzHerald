package com.herald.ezherald.curriculum;


import java.util.List;

import com.herald.ezherald.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;



public class CurriWidgetProvider extends AppWidgetProvider {
	
	private final String ACTION_NEXT_ATT = "com.herald.ezherald.action.NEXT_ATT";
	private RemoteViews remoteView = null;
	private int count = 0;
	private String TAG = "APP_WIDGET";
	
	private String CURR_PREF = "curriculum";
	private String WIDGET_PERIOD = "curri_num";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		Log.i(TAG, "---- onUpdate ----");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		remoteView = updateRemoteView(context);
		
		for(int i=0; i<appWidgetIds.length;++i)
		{
			int id = appWidgetIds[i];
			
			appWidgetManager.updateAppWidget(id, remoteView);
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.i(TAG, "---- onReceive ----");
		super.onReceive(context, intent);
		
		if(intent.getAction().equals(ACTION_NEXT_ATT))
		{
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

			if(null == remoteView)
			{
				remoteView = new RemoteViews(context.getPackageName(), R.layout.curri_widget);
			}
			
			String widget_str ;
			int num = context.getSharedPreferences(CURR_PREF, 0).getInt(WIDGET_PERIOD, -1);
			List<Attendance> atts = new CurriDataGrabber(context).getAttsByWeekday();
			if(atts.size() == 0)
			{
				widget_str = "今天没课~";
			}
			else{
				num+=1;
				Attendance att = atts.get(num%atts.size());
				context.getSharedPreferences(CURR_PREF, 0).edit().putInt(WIDGET_PERIOD, num).commit();
				widget_str = att.getAttPeriod()+ ","+att.getAttCourseName()+","+att.getAttPlace();
			}
			
			remoteView.setTextViewText(R.id.curr_widget_tv, widget_str);

			appWidgetManager.updateAppWidget(new ComponentName(context, CurriWidgetProvider.class), remoteView);
		}
		
	}
	
	private RemoteViews updateRemoteView(Context context)
	{
		if(null == remoteView)
		{
			remoteView = new RemoteViews(context.getPackageName(), R.layout.curri_widget);
		}
		List<Attendance> atts = new CurriDataGrabber(context).getNextAtts();
		SharedPreferences pref = context.getSharedPreferences(CURR_PREF, 0);
		String widget_str ;
		if(atts.size()==0)
		{
			pref.edit().putInt(WIDGET_PERIOD, -1).commit();
			widget_str = "下节没课了";
		}
		else
		{
			Attendance att = atts.get(0);
			widget_str = "下节课:"+att.getAttPeriod()+","+att.getAttCourseName()+","+att.getAttPlace();
			List<Attendance> allAtt = new CurriDataGrabber(context).getAttsByWeekday();
			int index = -1;
			for(int i=0; i<allAtt.size();++i)
			{
				if(allAtt.get(i).equals(att))
				{
					index = i;
					break;
				}
			}
			pref.edit().putInt(WIDGET_PERIOD, index).commit();

		}

		remoteView.setTextViewText(R.id.curr_widget_tv, widget_str);
		Intent intent = new Intent(ACTION_NEXT_ATT);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		remoteView.setOnClickPendingIntent(R.id.curri_widget_btn, pendingIntent);
		
		return remoteView;
	}
	
    // 每次删除该类型的窗口小部件(AppWidget)时都会触发 ，同时发送ACTION_APPWIDGET_DELETED广播，
    //     该广播可被onReceive()方法接受到.
   @Override
   public void onDeleted(Context context, int[] appWidgetIds)
   {
       super.onDeleted(context, appWidgetIds);
       Log.i(TAG, "--- onDeleted ---");
   }
   // 最后删除该类型的窗口小部件(AppWidget)时触发   
   @Override
   public void onDisabled(Context context)
   {
       super.onDisabled(context);
       Log.i(TAG, "--- onDisabled ---");
   }

   //第一次添加该类型的窗口小部件窗口小部件(AppWidget)时触发
   @Override
   public void onEnabled(Context context)
   {
       super.onEnabled(context);
       Log.i(TAG, "--- onEnabled ---");
   }

}
