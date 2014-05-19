package com.herald.ezherald.gpa;

import android.util.Log;

import org.json.JSONObject;

/**
 * @author xie
 * 绩点各项数据
 */
public class Record {
	private String  name;//课程名称
	private String  score;//成绩 有时候可能不是数字
	private double   credit;//学分
	private String  semester;//学期
	private String  scoreType;//成绩性质（首修/重修/...)
	private String  extra;//选修信息（人文/艺术/自然科学/)
	private boolean isSelected;//是否已经选中
	public Record(String name, String score, float credit, String semester,
			String scoreType, String extra, boolean isSelected) {
		super();
		this.name = name;
		this.score = score;
		this.credit = credit;
		this.semester = semester;
		this.scoreType = scoreType;
		this.extra = extra;
		this.isSelected = isSelected;
	}

    public Record(JSONObject json) {
        try{
            setCredit(json.getDouble("credit"));
            setExtra(json.getString("extra"));
            setName(json.getString("name"));
            setScore(json.getString("score"));
            setScoreType(json.getString("score_type"));
            setSelected(false);
            setSemester(json.getString("semester"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public float getPoint(){//由成绩计算绩点
		float point=0;
		try{
			float RealScore = Float.parseFloat(score);
			int intPart = (int)(RealScore-50)/10;
			int pointPart = (int)(RealScore)%10;
			if(pointPart <= 2)
				point = intPart;
			else if(pointPart <=5)	
				point = intPart+0.5f;
			else 
				point = intPart+0.8f;
			if(point > 4.8)
				point = 4.8f;
			if(point < 1.0)
				point = 0;
			return point;
		}catch(Exception e){//不是数字
			if("优".equals(score))
				point  = 4.5f;
			else if("良".equals(score))
				point  = 3.5f;
			else if("中".equals(score))
				point  = 2.5f;
			else if("及格".equals(score))
				point  = 1.5f;
			else{
				point = 0;
				Log.w("error","undefined score");
			}
		}
		return point;
		
	}
	
	@Override
	public String toString() {
		return String.format("%s  学分:%s   成绩:%s   %s", name, credit,score,scoreType);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getScoreType() {
		return scoreType;
	}
	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
}
