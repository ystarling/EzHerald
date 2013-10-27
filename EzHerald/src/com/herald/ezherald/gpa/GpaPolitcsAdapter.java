package com.herald.ezherald.gpa;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * @author xie
 * 
 */
public class GpaPolitcsAdapter extends BaseExpandableListAdapter {
	private Context context;
	private final float titleTextSize = 28;
	private final float contentTextSize = 25;
	private GpaPoliticsModel gpaPoliticsModel = new GpaPoliticsModel();

	GpaPolitcsAdapter(Context context) {
		this.context = context;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return gpaPoliticsModel.politics[groupPosition].content;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View contentView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView tv = new TextView(context);
		tv.setBackgroundColor(color.black);
		tv.setText(gpaPoliticsModel.politics[groupPosition].content);
		tv.setTextSize(titleTextSize);
		return tv;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return gpaPoliticsModel.politics[groupPosition].title;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return gpaPoliticsModel.politics.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View coverView, ViewGroup parent) {
		// TODO Auto-generated method stub

		TextView tv = new TextView(context);
		tv.setText(gpaPoliticsModel.politics[groupPosition].title);
		tv.setPadding(60, 0, 0, 0);
		tv.setTextSize(titleTextSize);
		if (gpaPoliticsModel.judge(groupPosition)) {// TODO Î´µÇÂ½²»±äÉ«
			tv.setBackgroundColor(Color.GREEN);
		} else {
			tv.setBackgroundColor(Color.RED);
		}
		return tv;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
