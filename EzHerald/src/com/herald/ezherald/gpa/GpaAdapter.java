package com.herald.ezherald.gpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class GpaAdapter extends BaseExpandableListAdapter {
	private GpaInfo gpaInfo;
	private Map<String,ArrayList<Record>> sem;
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
		final Record rc = (Record)getChild(groupPosition,childPosition);
		cb.setText(rc.toString());
		cb.setChecked(rc.isSelected());
//		cb.setFocusable(false);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				gpaInfo.selectionChanged(rc, isChecked);
				ArrayList<Record> li = sem.get(rc.getSemester());
				li.get(li.indexOf(rc)).setSelected(isChecked);
				notifyDataSetChanged();//������ʾ
			}
			
		});
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
		final int PADDING_LEFT = 62;
		final int TEXT_SIZE=28;
		TextView txt = new TextView(context);
		txt.setTextSize(TEXT_SIZE);
		txt.setText(sems.get(groupPosition)+"ѧ��");
		txt.setPadding(PADDING_LEFT, 0, 0, 0);
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
		
		sem  = new HashMap<String,ArrayList<Record>>();//ѧ�ںͶ�Ӧ�ɼ�list��ӳ���
		sems = new ArrayList<String>(); //���е�ѧ����Ϣ
		for(Record r: gpaInfo.getRecords()){//��ÿһ�����ѧ�ڼ��뵽map֮��
			if(sem.containsKey(r.getSemester())) {
				ArrayList<Record> list = sem.get(r.getSemester());
				list.add(r);
			} else {
				ArrayList<Record> list = new ArrayList<Record>();
				list.add(r);
				sems.add(r.getSemester());//����ѧ����Ϣ
				sem.put(r.getSemester(),list);
			}
		}
		Collections.sort(sems);
		
		notifyDataSetChanged();
	}

	public GpaInfo getGpaInfo() {
		return gpaInfo;
	}

	public void removeOptional() {
		// TODO Auto-generated method stub
		gpaInfo.removeOptional();
		for(Record r:gpaInfo.getRecords()){
			if(r.getExtra() != null )
				r.setSelected(false);
		}
		Iterator<Entry<String, ArrayList<Record>>> iter = sem.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<String, ArrayList<Record>> entry  = (Entry<String, ArrayList<Record>>) iter.next();
			ArrayList<Record> rec = entry.getValue();
			for(Record r:rec){
				if(r.getExtra() != null)
					r.setSelected(false);
			}
		}
		notifyDataSetChanged();
	}

}
