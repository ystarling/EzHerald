package com.herald.ezherald.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActiInfo {

	private final int COMMON = 0;
	private final int VOTE = 1;

	private int actiId;
	private boolean isVote;
	private String clubName;
	private int clubId;
	private String actiTitle;
	private String actiPubTime;
	private String startTime;
	private String endTime;
	private String actiIntro;
	private String clubIconName;
	private String actiPicName;
	private String iconUrl;
	private String actiPicUrl;
	private String actiDetailUrl;
	private String holdPlace;
	
	public ActiInfo(int id,boolean vote,String club_name,int club_id,String club_icon,
			String acti_title,String start_time,String end_time,String p,
			String release_time,String intro,String acti_pic)
	{
		actiId = id;
		isVote = vote;
		clubName = club_name;
		clubId = club_id;
		actiTitle = acti_title;
		actiPubTime = release_time;
		startTime = start_time;
		endTime = end_time;
		actiIntro = intro;
		clubIconName = club_icon;
		actiPicName = acti_pic;
		holdPlace = p;

//		iconUrl = "http://herald.seu.edu.cn/herald_league/Uploads/LeagueAvatar/m_s_avatar_address/"+
//					clubIconName;
		iconUrl = "http://static.dayandcarrot.net/temp/square.png";
//		actiPicUrl = "http://herald.seu.edu.cn/herald_league/Uploads/ActivityPost/m_s_post_add/" +
//				actiPicName;
		actiPicUrl = "http://static.dayandcarrot.net/temp/aoi_sora.jpg";
		actiDetailUrl = "http://herald.seu.edu.cn/herald_league_api/index.php" +
				"/command/select/selectoperate/refresh/activityid/"+actiId;
	}
	
	public int getId()
	{
		return actiId;
	}
	
	public int getClubId()
	{
		return clubId;
	}
	

	public boolean checkIsVote() {
		return isVote;
	}

	public String getClubName() {
		return clubName;
	}
	
	public String getClubIconName()
	{
		return clubIconName;
	}
	
	

	public String getActiTitle() {
		return actiTitle;
	}
	
	public String getStartTime()
	{
		return startTime;
	}
	
	public String getEndTime()
	{
		return endTime;
	}
	
	public String getPlace()
	{
		return holdPlace;
	}

	public String getActiPubTime() {
		return actiPubTime;
	}
	
	public String getActiPicName()
	{
		return actiPicName;
	}

	public String getActiIntro() {
		return actiIntro;
	}
	

	public String getClubIconURL() {
		return iconUrl;
	}

	public boolean checkHaveActiPic() {
		if(actiPicName!="")
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public String getActiPicURL() {
		return actiPicUrl;
	}
	
	public String getDetailUrl()
	{
		return actiDetailUrl;
	}

}
