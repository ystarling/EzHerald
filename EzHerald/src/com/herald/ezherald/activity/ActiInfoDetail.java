package com.herald.ezherald.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiInfoDetail {

	private int actiId;
	private int clubId;
	private String clubName;
	private String actiTitle;
	private String actiPubTime;
	private String startTime;
	private String endTime;
	private String actiDetail;
	
	private String iconName;
	private String iconUrl;
	
	private String actiPicUrl;
	private String actiPicName;
	
	private boolean isVote;
	
	private boolean haveVote;
	
	private int lastCommentId;
	private int commentNum;
	private String commentUrl;
	
	private Map<String ,Integer> voteResult;


	public ActiInfoDetail()
	{
		voteResult = new HashMap<String,Integer>();
	}
	
	public void setHaveVote(boolean v)
	{
		haveVote = v;
	}
	
	public void setIconName(String name)
	{
		iconName = name;
		iconUrl = "http://herald.seu.edu.cn/herald_league/Uploads/LeagueAvatar/"+name+"_100.jpg";
	}
	
	public void setActiPicName(String name)
	{
		actiPicName = name;
		actiPicUrl = "http://herald.seu.edu.cn/herald_league/Uploads/ActivityPost/"+"m_" + name;
	}
	
	public void setVoteResult(Map<String,Integer> map)
	{
		voteResult = map;
	}
	
	public void setActiTitle(String title)
	{
		this.actiTitle = title;
	}
	
	public Map<String,Integer> getVoteResult()
	{
		return voteResult;
	}
	
	public void setActiId(int id)
	{
		actiId = id;
	}
	
	public void setClubId(int id)
	{
		clubId = id;
	}
	
	public void setIsVote(boolean vote)
	{
		isVote = vote;
	}
	
	public void setClubName(String name)
	{
		clubName = name;
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
	

	public String getClubName() {
		return clubName;
	}
	
	public String getClubIconUrl()
	{
		return iconUrl;
	}
	
	public int getClubId()
	{
		return clubId;
	}
	
	public boolean getHaveVote()
	{
		return this.haveVote;
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
	
	public String getActiPicName()
	{
		return actiPicName;
	}
	

	
	public int getCommentNum()
	{
		return commentNum;
	}
	
	public int getLastCommentId()
	{
//		return commentList.get(commentList.size()-1).getCommentId();
		return lastCommentId;
	}
	
	public boolean checkIsVote()
	{
		return isVote;
	}



}
