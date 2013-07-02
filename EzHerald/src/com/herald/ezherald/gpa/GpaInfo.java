package com.herald.ezherald.gpa;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class GpaInfo {
	public static final boolean DEBUG = true;
	private GpaDbModel gpaDbModel;
	private List<Record> records;
	public List<Record> getRecords() {
		return records;
	}
	public GpaInfo(Context context){
		try {
			gpaDbModel = new GpaDbModel(context);
			gpaDbModel.open();
			records = gpaDbModel.all();
		} catch (Exception e) {
			// TODO: handle exception
			records = null;
		}
		gpaDbModel.close();
	}
	/**
	 * @return 算出的所有绩点
	 */
	public float calcAverage(){
		float totalGrade=0,totalCredit=0;
		for(Record r:records){
			if(r.isSelected() && r.getPoint() > 0 ) {
				totalGrade  +=r.getPoint() * r.getCredit();
				totalCredit +=r.getCredit();
			}
		}
		return totalGrade/totalCredit;
	}
	public void update() {
		if( DEBUG ) {
			records = new ArrayList<Record>();
			records.add(new Record("高数","78",5.0f,"12-13-2","首修",null,false));
			records.add(new Record("大物","良",4.0f,"12-13-1","首修",null,false));
			records.add(new Record("物理实验","通过",2.0f,"12-13-1","首修",null,true));
			records.add(new Record("艺术鉴赏","98",5.0f,"12-13-3","首修","人文类",false));
			records.add(new Record("高数","78",5.0f,"12-13-2","首修",null,false));
			records.add(new Record("c++","100",2.5f,"12-13-2","首修",null,true));
		} else {
			//TODO update gpa info
		}
	}
	public void save(){
		gpaDbModel.open();
		gpaDbModel.update(records);
		gpaDbModel.close();
	}
	public void selectionChanged(Record r,boolean newState){
		gpaDbModel.open();
		gpaDbModel.changeSelection(r.getName(),newState);
		gpaDbModel.close();
		//TODO 
	}
}
