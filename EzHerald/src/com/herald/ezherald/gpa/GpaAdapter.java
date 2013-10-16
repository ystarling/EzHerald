package com.herald.ezherald.gpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.HttpClient;

import com.herald.ezherald.account.UserAccount;

import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class GpaAdapter extends BaseExpandableListAdapter {
	private GpaInfo gpaInfo;
	private Map<String,ArrayList<Record>> semester;
	private List<String> semesters;
	private Context context;
	private ProgressDialog progress;//�������ݵĽ�����
	private ProgressDialog progressDialog;//�������ݵĽ�����
	private UserAccount user;
	
 	public GpaAdapter(Context context) {
		this.context = context;
 		update(-1,null);//TODO 
	}
 	public GpaAdapter(Context context,ProgressDialog progressDialog, UserAccount user) {
		this.context = context;
 		update(-1,null);//TODO 
 		this.progressDialog = progressDialog;
 		this.user = user;
	} 
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return semester.get(semesters.get(groupPosition)).get(childPosition);
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
				ArrayList<Record> li = semester.get(rc.getSemester());
				li.get(li.indexOf(rc)).setSelected(isChecked);
				notifyDataSetChanged();//������ʾ
			}
			
		});
		return cb;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return semester.get(semesters.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return semesters.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return semesters.size();
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
		final int TEXT_SIZE = 20;
		TextView txt = new TextView(context);
		txt.setTextSize(TEXT_SIZE);
		txt.setText(semesters.get(groupPosition)+"ѧ��");
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
	public void update(int vercode,HttpClient client){
		if(gpaInfo == null || vercode == -1|| client == null){//��ʼ���ĵ���
			gpaInfo = new GpaInfo(context,this);
		}else{
			gpaInfo.update(vercode,client,user);
			gpaInfo.save();
		}
		//��ʼ��Ҫ��
		semester  = new HashMap<String,ArrayList<Record>>();//ѧ�ںͶ�Ӧ�ɼ�list��ӳ���
		semesters = new ArrayList<String>(); //���е�ѧ����Ϣ
		for(Record r: gpaInfo.getRecords()){//��ÿһ�����ѧ�ڼ��뵽map֮��
			if(semester.containsKey(r.getSemester())) {
				ArrayList<Record> list = semester.get(r.getSemester());
				list.add(r);
			} else {
				ArrayList<Record> list = new ArrayList<Record>();
				list.add(r);
				semesters.add(r.getSemester());//����ѧ����Ϣ
				semester.put(r.getSemester(),list);
			}
		}
		Collections.sort(semesters);
		
		notifyDataSetChanged();
		
	}

	public GpaInfo getGpaInfo() {
		return gpaInfo;
	}

	public void removeOptional() {
		// TODO Auto-generated method stub
		gpaInfo.removeOptional();//�������ݿ�ļ�¼
		/*
		for(Record r:gpaInfo.getRecords()){
			if(!r.getExtra().equals(""))
				r.setSelected(false);
		}
		*/
		notifyDataSetChanged();//������ʾ
	}
	public void updateFinished(boolean isSuccess){
		if(isSuccess){
			Toast.makeText(context, "���³ɹ�", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, "����ʧ�ܣ��������", Toast.LENGTH_SHORT).show();
		}
		progress.cancel();
		semester  = new HashMap<String,ArrayList<Record>>();//ѧ�ںͶ�Ӧ�ɼ�list��ӳ���
		semesters = new ArrayList<String>(); //���е�ѧ����Ϣ
		for(Record r: gpaInfo.getRecords()){//��ÿһ�����ѧ�ڼ��뵽map֮��
			if(semester.containsKey(r.getSemester())) {
				ArrayList<Record> list = semester.get(r.getSemester());
				list.add(r);
			} else {
				ArrayList<Record> list = new ArrayList<Record>();
				list.add(r);
				semesters.add(r.getSemester());//����ѧ����Ϣ
				semester.put(r.getSemester(),list);
			}
		}
		Collections.sort(semesters);
		
		notifyDataSetChanged();
		
	}
	public void onLoadFinished(){
		Toast.makeText(context, "��ȡ�������", Toast.LENGTH_SHORT).show();
		if(progressDialog!=null){
			progressDialog.cancel();
		}
	}

	public void onDealing(int i, int count) {
		// TODO Auto-generated method stub
		if(progress == null){//init
			progress = new ProgressDialog(context);
			progress.setTitle("���ڷ���");
			progress.setIndeterminate(false);//������������ȦȦ
			progress.setMax(count);
			progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progress.setCancelable(false);
			progress.setProgress(0);
			progress.show();
		}else{
			progress.setProgress(i);
		}
		
		
	}
	public void selectAll() {
		// TODO Auto-generated method stub
		gpaInfo.selectAll();
		for(Record r:gpaInfo.getRecords()){
			if(r.isSelected() == false)
				r.setSelected(true);
		}
		notifyDataSetChanged();
	}
}
