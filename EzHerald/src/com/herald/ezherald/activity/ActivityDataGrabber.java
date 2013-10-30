package com.herald.ezherald.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.herald.ezherald.academic.DataRequester;
import com.herald.ezherald.mainframe.MainContentGridItemObj;
import com.herald.ezherald.mainframe.MainContentInfoGrabber;

public class ActivityDataGrabber implements MainContentInfoGrabber {
	
	private final String refresh_url = "http://herald.seu.edu.cn/herald_league_api/index.php?" +
			"/command/select/selectoperate/getactivity"; 
	
	private final String noActivityHint = "NOACTIVITYCANGET";

	@Override
	public MainContentGridItemObj GrabInformationObject() {
		// TODO Auto-generated method stub
		
		String str = DataRequester.request(refresh_url);
		MainContentGridItemObj obj = new MainContentGridItemObj();
		if(null == str)
		{
			obj.setContent1("加载不了呢~");
			obj.setContent2("同学同学网络正常不...");
			return obj;
		}
		
		
		if(str == noActivityHint )
		{
			obj.setContent1("没有活动哦~");
			obj.setContent2("赶快提醒您身边的社团组织到先声网上发布活动吧~");
		}
		else
		{
			try {
				
				JSONArray jsonArr = new JSONArray(str);
				JSONObject jsonObject = jsonArr.getJSONObject(0);
				JSONObject league_obj = jsonObject.getJSONObject("league_info");
				String acti_title = jsonObject.getString("name");
				String league_name = league_obj.getString("league_name");
				
				obj.setContent1(league_name+"发布了新活动:");
				obj.setContent2(acti_title);
	
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				obj.setContent1("加载活动出错了 ==");
				obj.setContent2("");
				
			}
			catch (Exception e)
			{
				obj.setContent1("加载出错啦");
				obj.setContent2("是因为网络连接不好么");
			}
			
		}
		return obj;
		
	}

}
