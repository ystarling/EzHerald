package com.herald.ezherald.gpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class GpaAdapter extends BaseExpandableListAdapter {
	private GpaInfo gpaInfo;
	private Map<String,List<Record>> sem;
	private List<String> sems;
	private Context context;
 	public GpaAdapter(Context context) {
		this.context = context;
		
 		update();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return sem.get(sems.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View contentView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		CheckBox cb = new CheckBox(context);
		Record rc = (Record)getChild(groupPosition,childPosition);
		cb.setText(rc.toString());
		cb.setSelected(rc.isSelected());
		return cb;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return sem.get(sems.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return sems.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return sems.size();
	}

	@Override
	public long getGroupId(int roupPosition) {
		// TODO Auto-generated method stub
		return roupPosition; 
	} 
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View coverView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView txt = new TextView(context);
		txt.setText(sems.get(groupPosition)+"ѧ��");
		return txt;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	public void update(){
		if(gpaInfo == null){
			gpaInfo = new GpaInfo(context);
		}else{
			gpaInfo.update();
			gpaInfo.save();
		}
		
		sem  = new HashMap<String,List<Record>>();//ѧ�ںͶ�Ӧ�ɼ�list��ӳ���
		sems = new ArrayList<String>(); //���е�ѧ����Ϣ
		for(Record r: gpaInfo.getRecords()){//��ÿһ�����ѧ�ڼ��뵽map֮��
			if(sem.containsKey(r.getSemester())) {
				List<Record> list = sem.get(r.getSemester());
				list.add(r);
			} else {
				List<Record> list = sem.get(r.getSemester());
				list.add(r);
				sems.add(r.getSemester());//����ѧ����Ϣ
				sem.put(r.getSemester(),list);
			}
		}
		Collections.sort(sems);
		
		notifyDataSetChanged();
	}
}
