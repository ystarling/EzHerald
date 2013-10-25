package com.herald.ezherald.gpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.HttpClient;

import com.herald.ezherald.account.UserAccount;

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
	private ProgressDialog progress;//处理数据的进度条
	private ProgressDialog progressDialog;//更新数据的进度条
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
				notifyDataSetChanged();//更新显示
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
		txt.setText(semesters.get(groupPosition)+"学期");
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
		if(gpaInfo == null || vercode == -1|| client == null){//初始化的调用
			gpaInfo = new GpaInfo(context,this);
		}else{
			gpaInfo.update(vercode,client,user);
			gpaInfo.save();
		}
		//初始化要用
		semester  = new HashMap<String,ArrayList<Record>>();//学期和对应成绩list的映射表
		semesters = new ArrayList<String>(); //所有的学期信息
		for(Record r: gpaInfo.getRecords()){//将每一项根据学期加入到map之中
			if(semester.containsKey(r.getSemester())) {
				ArrayList<Record> list = semester.get(r.getSemester());
				list.add(r);
			} else {
				ArrayList<Record> list = new ArrayList<Record>();
				list.add(r);
				semesters.add(r.getSemester());//更新学期信息
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
		gpaInfo.removeOptional();//更新数据库的记录
		/*
		for(Record r:gpaInfo.getRecords()){
			if(!r.getExtra().equals(""))
				r.setSelected(false);
		}
		*/
		notifyDataSetChanged();//更新显示
	}
	public void updateFinished(boolean isSuccess){
		if(isSuccess){
			Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, "更新失败，检查网络", Toast.LENGTH_SHORT).show();
		}
		progress.cancel();
		semester  = new HashMap<String,ArrayList<Record>>();//学期和对应成绩list的映射表
		semesters = new ArrayList<String>(); //所有的学期信息
		for(Record r: gpaInfo.getRecords()){//将每一项根据学期加入到map之中
			if(semester.containsKey(r.getSemester())) {
				ArrayList<Record> list = semester.get(r.getSemester());
				list.add(r);
			} else {
				ArrayList<Record> list = new ArrayList<Record>();
				list.add(r);
				semesters.add(r.getSemester());//更新学期信息
				semester.put(r.getSemester(),list);
			}
		}
		Collections.sort(semesters);
		
		notifyDataSetChanged();
		
	}
	public void onLoadFinished(){
		Toast.makeText(context, "获取数据完成", Toast.LENGTH_SHORT).show();
		if(progressDialog!=null){
			progressDialog.cancel();
		}
	}

	public void onDealing(int i, int count) {
		// TODO Auto-generated method stub
		if(progress == null){//init
			progress = new ProgressDialog(context);
			progress.setTitle("正在分析");
			progress.setIndeterminate(false);//进度条而不是圈圈
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
		//for(Record r:gpaInfo.getRecords()){
		//	if(r.isSelected() == false)
		//		r.setSelected(true);
		//}
		notifyDataSetChanged();
	}
}
