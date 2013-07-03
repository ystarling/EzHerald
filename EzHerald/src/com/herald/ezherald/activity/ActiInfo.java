package com.herald.ezherald.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActiInfo {

	private final int COMMON = 0;
	private final int VOTE = 1;

	private int type;
	private String clubName;
	private String actiTitle;
	private String actiPubTime;
	private String actiIntro;
	private String clubIconUrl;
	private boolean haveActiPic;
	private String actiPicUrl;
	private String actiDetailUrl;
	

	public ActiInfo(int tp, String clName, String title, String pubTime,
			String intro, String iconurl, boolean havPic) {
		type = tp;
		clubName = clName;
		actiTitle = title;
		actiPubTime = pubTime;
		actiIntro = intro;
		clubIconUrl = iconurl;
		haveActiPic = havPic;
		
	}
	
	
	

	public void setDetailUrl(String url) {
		actiDetailUrl = url;
	}

	public int getType() {
		return type;
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

	public String getActiIntro() {
		return actiIntro;
	}

	public String getClubIconURL() {
		return clubIconUrl;
	}

	public boolean getHaveActiPic() {
		return haveActiPic;
	}

	public String getActiPicURL() {
		return actiPicUrl;
	}

}
