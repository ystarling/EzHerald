package com.herald.ezherald.activity;

import java.net.URL;

import android.widget.ImageView;
import android.widget.TextView;

public class ActiInfoDetail {

	private String clubIconURL;
	private String clubName;
	private String actiTitle;
	private String actiPubTime;
	private String actiDetail;
	private String actiPicURL;

	public ActiInfoDetail(String clname, String title, String pubtime,
			String detail, String iconurl, String picurl) {
		clubName = clname;
		actiTitle = title;
		actiPubTime = pubtime;
		actiDetail = detail;
		clubIconURL = iconurl;
		actiPicURL = picurl;
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

}
