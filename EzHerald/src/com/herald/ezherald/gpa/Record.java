package com.herald.ezherald.gpa;

import android.util.Log;

/**
 * @author xie
 * �����������
 */
public class Record {
	private String  name;//�γ�����
	private String  score;//�ɼ� ��ʱ����ܲ�������
	private float   credit;//ѧ��
	private String  semester;//ѧ��
	private String  scoreType;//�ɼ����ʣ�����/����/...)
	private String  extra;//ѡ����Ϣ������/����/��Ȼ��ѧ/)
	private boolean isSelected;//�Ƿ��Ѿ�ѡ��
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
	public float getPoint(){//�ɳɼ����㼨��
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
		}catch(Exception e){//��������
			if("��".equals(score))
				point  = 4.5f;
			else if("��".equals(score))
				point  = 3.5f;
			else if("��".equals(score))
				point  = 2.5f;
			else if("����".equals(score))
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
		return String.format("%s-%s-%s-%s", name, credit,score,scoreType);
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
	public float getCredit() {
		return credit;
	}
	public void setCredit(float credit) {
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
