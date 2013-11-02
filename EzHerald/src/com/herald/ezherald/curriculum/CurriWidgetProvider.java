package com.herald.ezherald.curriculum;


import com.herald.ezherald.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;



public class CurriWidgetProvider extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		for(int i=0; i<appWidgetIds.length;++i)
		{
			int id = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.curri_widget);
			views.setTextViewText(R.id.curr_widget_tv, " widget id:"+id);
			
			appWidgetManager.updateAppWidget(id, views);
		}
	}

}
