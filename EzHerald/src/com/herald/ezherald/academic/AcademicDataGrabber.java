package com.herald.ezherald.academic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.herald.ezherald.mainframe.MainContentGridItemObj;
import com.herald.ezherald.mainframe.MainContentInfoGrabber;

public class AcademicDataGrabber implements MainContentInfoGrabber {
	

	private final static String JWC_URL = "http://herald.seu.edu.cn/herald_web_service/jwc/0";
	
	@Override
	public MainContentGridItemObj GrabInformationObject() {
		// TODO Auto-generated method stub
		String str = DataRequester.request(JWC_URL);
		if( null != str)
		{
			List<JwcInfo> list = new ArrayList<JwcInfo>();
			JSONArray jsonArr;
			try {
				jsonArr = new JSONArray(str);
				for (int i=0; i<jsonArr.length(); ++i)
				{
					JSONArray jsonItem = (JSONArray) jsonArr.get(i);
					int id = Integer.parseInt(jsonItem.getString(0));
					String type = jsonItem.getString(1);
					String title = jsonItem.getString(2);
					String date = jsonItem.getString(3);
					list.add(new JwcInfo(type, title, date, id));
				}
				JwcInfo item = list.get(0);
				MainContentGridItemObj obj = new MainContentGridItemObj();
				obj.setContent1(item.GetTitle());
				obj.setContent2(item.GetDate());
				return obj;
			}catch (JSONException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
			}
			
		}
		
		return null;
	}


}
