package com.herald.ezherald.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;
import android.widget.TextView;

public class ActiInfoDetail {

	private int actiId;
	private int clubId;
	private String clubName;
	private String actiTitle;
	private String actiPubTime;
	private String startTime;
	private String endTime;
	private String actiDetail;
	
	private String actiPicUrl;
	private String actiPicName;
	
	private int lastCommentId;
	private int commentNum;
	private String commentUrl;
	private List<Comment> commentList;


	public ActiInfoDetail()
	{
		commentList = new ArrayList<Comment>();
	}
	
	public void setActiId(int id)
	{
		actiId = id;
	}
	
	public void setClubId(int id)
	{
		clubId = id;
	}
	
	
	public void setActiPicName(String name)
	{
		actiPicName = name;
		actiPicUrl = "http://herald.seu.edu.cn/herald_league/Uploads" +
				"/ActivityPost/m_m_"+actiPicName;
	}
	
	public void setCommentUrl(String url)
	{
		commentUrl = url;
	}
	
	public void setLastCommentId(int id)
	{
		lastCommentId = id;
	}
	
	public void setCommentNum(int num)
	{
		commentNum = num;
	}
	
	public void setActiIntro(String intro)
	{
		actiDetail = intro;
	}
	
	public void setCommentList(List<Comment> cl)
	{
		commentList.addAll(cl);
	}
	
	public void addCommentList(List<Comment> cl)
	{
		commentList.addAll(cl);
	}

	public String getClubName() {
		return clubName;
	}

	public String getActiTitle() {
		return actiTitle;
	}

	public String getActiPubTime() {
		return actiPubTime;
	}

	public String getActiDetail() {
		return actiDetail;
	}

	public int getActiId()
	{
		return actiId;
	}

	public String getActiPicUrl() {
		return actiPicUrl;
	}
	
	public List<Comment> getCommentList()
	{
		return commentList;
	}
	
	public int getCommentNum()
	{
		return commentNum;
	}



}
