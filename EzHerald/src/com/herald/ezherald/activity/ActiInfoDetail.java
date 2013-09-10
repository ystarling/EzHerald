package com.herald.ezherald.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private String iconName;
	private String iconUrl;
	
	private String actiPicUrl;
	private String actiPicName;
	
	private boolean isVote;
	
	private boolean haveVote;
	
	private int lastCommentId;
	private int commentNum;
	private String commentUrl;
	private List<Comment> commentList;
	
	private Map<String ,Integer> voteResult;


	public ActiInfoDetail()
	{
		commentList = new ArrayList<Comment>();
		voteResult = new HashMap<String,Integer>();
	}
	
	public void setHaveVote(boolean v)
	{
		haveVote = v;
	}
	
	public void setIconName(String name)
	{
		iconName = name;
		iconUrl = "http://herald.seu.edu.cn/herald_league/Uploads/LeagueAvatar/m_s_avatar_address/"+name;
	}
	
	public void setActiPicName(String name)
	{
		actiPicName = name;
		actiPicUrl = "http://herald.seu.edu.cn/herald_league/Uploads/ActivityPost/m_s_post_add/" + name;
	}
	
	public void setVoteResult(Map<String,Integer> map)
	{
		voteResult = map;
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
	
	
	public List<Comment> getCommentList()
	{
		return commentList;
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
