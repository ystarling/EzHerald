package com.herald.ezherald.gpa;

import android.content.Context;
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
	
	private GpaPoliticsModel gpaPoliticsModel = new GpaPoliticsModel();
	GpaPolitcsAdapter(Context context){
		this.context = context;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return gpaPoliticsModel.content[groupPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition	, int childPosition, boolean isLastChild, View contentView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView tv = new TextView(context);
		tv.setText(gpaPoliticsModel.content[groupPosition]);
		return tv;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return gpaPoliticsModel.title;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return gpaPoliticsModel.title.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View coverView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return new GpaPoliticsView(context,gpaPoliticsModel.title[groupPosition],gpaPoliticsModel.judge(groupPosition));
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
