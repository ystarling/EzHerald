package com.herald.ezherald.emptyclassroom;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpException;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

import com.herald.ezherald.mainframe.MainContentGridItemObj;
import com.herald.ezherald.mainframe.MainContentInfoGrabber;
import com.tendcloud.tenddata.m;

public class EmptyClassroomInfoGrabber implements MainContentInfoGrabber {
	Context mContext;
	
	public EmptyClassroomInfoGrabber(Context context){
		mContext = context;
	}
	
	private static int[] mTimeOfClasses = {
		8 * 60 + 45,  //8:45
		9 * 60 + 35,  //9:35
		10 * 60 + 30, //...
		11 * 60 + 25,
		12 * 60 + 15,
		14 * 60 + 45,
		15 * 60 + 35,
		16 * 60 + 30,
		17 * 60 + 25,
		18 * 60 + 15,
		19 * 60 + 15,
		20 * 60 + 5,
		20 * 60 + 55
	}; //每节课的时间段

	@Override
	public MainContentGridItemObj GrabInformationObject() {
		String currTimeInClassSpan = getCurrentTimeInClassSpan();
		MainContentGridItemObj obj = new MainContentGridItemObj();
		if(currTimeInClassSpan == null){
			obj.setContent1("大家都不上课啦~");
			obj.setContent2("太晚了,早点休息吧");
		} else {
			Integer toPeroidInt = Integer.parseInt(currTimeInClassSpan) + 2; //自习好歹要两节课吧！
			if(toPeroidInt > 13)
				toPeroidInt = 13;
			SharedPreferences prefs = mContext.getSharedPreferences("ec_campus", Context.MODE_PRIVATE);
			String selectedCampus = prefs.getString("campus", "jlh");
			obj.setContent1("抱歉抱歉...");
			obj.setContent2("服务器暂时链接不上呢");
			
			String url = String.format("http://herald.seu.edu.cn/queryEmptyClassrooms/query/%s/today/%s/%s/",
					selectedCampus, currTimeInClassSpan, toPeroidInt.toString());
			String responseStr = null;
			try {
				responseStr = new NetRequest().request(url);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (HttpException e) {
				e.printStackTrace();
			}
			
			if(responseStr != null){
				//有返回信息
				try {
					List<String> roomList = DataParser.strToList(responseStr);
					
					int roomSize = roomList.size();
					String roomString = "";
					int endCount = roomSize<3 ? roomSize : 3;
					Random random = new Random();
					for(int i=0; i<endCount; i++){
						int index = random.nextInt(roomList.size());
						roomString += roomList.get(index);
						roomList.remove(index);
						if(i + 1 < endCount){
							roomString += ",";
						}
					}
					roomString += ("等" + roomList.size() + "间教室可自习");
					if(prefs.contains("campus")){
						obj.setContent1("学霸你好! ");
						obj.setContent2(roomString);
					} else {
						obj.setContent1("进入空闲教室模块选下校区啦~");
						obj.setContent2("当前湖区共" + roomList.size() + "间教室可自习");
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		return obj;
	}
	
	/**
	 * 获得当前是第几节课
	 * 如果时间太早，返回第一节
	 * 时间太晚，返回null
	 * @return
	 */
	public static String getCurrentTimeInClassSpan(){
		String retStr = null;
		Time time = new Time();
		time.setToNow();
		int hourMinuteInteger = time.hour * 60 + time.minute;
		for(int i=0; i<mTimeOfClasses.length; i++){
			int t = mTimeOfClasses[i];
			if(hourMinuteInteger >= t)
				continue;
			retStr = Integer.valueOf(i+1).toString(); //返回值从1开始
			break;
		}
						
		return retStr;
	}

}
