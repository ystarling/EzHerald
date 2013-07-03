package com.herald.ezherald.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;
import android.widget.TextView;

public class ActiInfoDetail {

	private String clubIconURL;
	private String clubName;
	private String actiTitle;
	private String actiPubTime;
	private String actiDetail;
	private String actiPicURL;
	private int commentNum;
	
	private List<Comment> commentList;

	public ActiInfoDetail(String clname, String title, String pubtime,
			String detail, String iconurl, String picurl,int cm) {
		clubName = clname;
		actiTitle = title;
		actiPubTime = pubtime;
		actiDetail = detail;
		clubIconURL = iconurl;
		actiPicURL = picurl;
		commentNum = cm;
		commentList = new ArrayList<Comment>();
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

	public String getClubIconUrl() {
		return clubIconURL;
	}

	public String getActiPicUrl() {
		return actiPicURL;
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
