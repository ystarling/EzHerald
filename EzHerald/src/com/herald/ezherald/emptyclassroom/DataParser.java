package com.herald.ezherald.emptyclassroom;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataParser {
	
	
	static public List<String> strToList(String str) throws JSONException
	{
		List<String> arr = new ArrayList<String>();
		JSONArray jsonArr = new JSONArray(str);
		for(int i=0;i<jsonArr.length();++i)
		{
			arr.add(jsonArr.getString(i));
		}
		return arr;
	}
	
	
	static public List<String> strOfAllToList(String str) throws JSONException
	{
		List<String> arr = new ArrayList<String>();
		JSONObject jsonObj = new JSONObject(str);
		List<JSONArray> jl = new ArrayList<JSONArray>();
		jl.add(jsonObj.getJSONArray("SPL"));
		jl.add(jsonObj.getJSONArray("JLH"));
		jl.add(jsonObj.getJSONArray("DJQ"));
		for(int i=0;i<jl.size();i++)
		{
			JSONArray jsonArr = jl.get(i);
			for(int j=0;j<jsonArr.length();++i)
			{
				arr.add(jsonArr.getString(j));
			}
		}
		return arr;
		
	}
	

	
	

}
