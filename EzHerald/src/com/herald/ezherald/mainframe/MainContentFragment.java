package com.herald.ezherald.mainframe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.MainActivity;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.AcademicActivity;
import com.herald.ezherald.activity.ActiActivity;
import com.herald.ezherald.agenda.AgendaActivity;
import com.herald.ezherald.curriculum.CurriculumActivity;
import com.herald.ezherald.exercise.ExerciseActivity;
import com.herald.ezherald.freshman.FreshmanActivity;
import com.herald.ezherald.gpa.GPAActivity;
import com.herald.ezherald.library.LibraryActivity;
import com.herald.ezherald.settingframe.MainContentModulePref;

/*
 * @author 何博伟
 * @since 20130514
 * @updated 20130630
 * 
 * 主界面呈现内容的Fragement
 * 其他各模块可参照本Fragement的定义呈现内容
 */
public class MainContentFragment extends SherlockFragment {
	private String text = null;
	private GridView mGridView;  //GridView
	private ViewFlow mViewFlow;  //ViewFlow
	private CircleFlowIndicator mCircIndic;
	
	private List<Map<String, Object>> mGridItems;
	private List<Map<String, Object>> mImageItems;
	
	private final String PREF_NAME = "com.herald.ezherald_preferences";
	private final String KEY_NAME = "module_choice";
	
	
	private ArrayList<String> mContentTitles = new ArrayList<String>();
	
	
	//////////////Temporarily used local variables///////////////////
	String mContentCont1 [] = {"AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH"};
	String mContentCont2 [] = {"SSS", "TTT", "UUU", "VVV", "WWW" ,"XXX", "YYY", "ZZZ"};
	///////Should be removed after we have a SharedPreference////////
	private static final int[] image_ids =
		{R.drawable.main_frame_pic0, R.drawable.main_frame_pic1,
		R.drawable.main_frame_pic2, R.drawable.main_frame_pic3,
		R.drawable.main_frame_pic4
		};
	//TODO:使用时应当先用本地存储内容，然后Async通过网络更新本地存储内容，再更新视图
	///////Should be removed after we have a SharedPreference////////

	public MainContentFragment() {
		text = "Default";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#setArguments(android.os.Bundle)
	 */
	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
		text = args.getString("text");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

	}

	/*
	 * 	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//视图
		View v = inflater.inflate(R.layout.main_frame_content, null);
		
		return v;
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		
		getPrefItems();
		
		mGridView = (GridView)getActivity().findViewById(R.id.main_frame_content_gridView);
		mGridItems = getGridItems();
		mGridView.setAdapter(new MainContentGridItemAdapter(getActivity(), mGridItems));
		mGridView.setOnItemClickListener(new MyOnItemClickListener());
		
		mViewFlow = (ViewFlow)getActivity().findViewById(R.id.main_frame_viewflow);
		mImageItems = getImageItems();
		mViewFlow.setAdapter(new MainContentFlowItemAdapter(getActivity(), mImageItems));
		
		mCircIndic = (CircleFlowIndicator)getActivity().findViewById(R.id.main_frame_viewflow_indic);
		
		mViewFlow.setFlowIndicator(mCircIndic);
		
		mViewFlow.setTimeSpan(5000); 
		mViewFlow.startAutoFlowTimer();
	}
	

	/**
	 * 获得偏好设置
	 */
	private void getPrefItems() {
		// 删除旧的东西
		mContentTitles.clear();
		
		// 获得偏好设置
		SharedPreferences appPrefs =
				getActivity().getSharedPreferences(
						PREF_NAME
						, Context.MODE_PRIVATE);
		Set<String> result_set = appPrefs.getStringSet(KEY_NAME, null);
		if(null != result_set && result_set.size()>0){
			for(String result : result_set){
				//Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
				mContentTitles.add(result);
			}
		}
		else
		{
			mContentTitles.add("尚未设置");
			Toast.makeText(getActivity(), "NULL， use default", Toast.LENGTH_SHORT).show();
		}
		
	}



	private class MyOnItemClickListener implements AdapterView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			//TODO: 项目点击的响应（进入相关模块）
			Toast.makeText(getActivity(), "pos=" + position + "\nid=" + id, Toast.LENGTH_SHORT).show();
			Intent i = new Intent();
			switch ((int)id) {
			case 0:
				i.setClass(getActivity(), MainContentModulePref.class);
				break;
			case 1:
				i.setClass(getActivity(), CurriculumActivity.class);
				break;
			case 2:
				i.setClass(getActivity(), ActiActivity.class);
				break;
			case 3:
				i.setClass(getActivity(), AgendaActivity.class);
				break;
			case 4:
				i.setClass(getActivity(), LibraryActivity.class);
				break;
			case 5:
				i.setClass(getActivity(), GPAActivity.class);
				break;
			case 6:
				i.setClass(getActivity(), ExerciseActivity.class);
				break;
			case 7:
				i.setClass(getActivity(), AcademicActivity.class);
				break;
			case 8:
				i.setClass(getActivity(), FreshmanActivity.class);
				break;
			}
			if (i != null) {
				startActivity(i);
				if(id != 0)
					getActivity().finish();
			}
		}
		
	}
	
	/**
	 * 初始化菜单项信息
	 */
	private List<Map<String, Object>> getGridItems(){
		List<Map<String, Object>> gridItems = new ArrayList<Map<String, Object>>();
		for(int i=0; i<mContentTitles.size(); i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", mContentTitles.get(i));
			map.put("content1", mContentCont1[i]);
			map.put("content2", mContentCont2[i]);
			gridItems.add(map);
		}
		return gridItems;
	}
	
	/**
	 * 初始化图片信息
	 */
	private List<Map<String, Object>> getImageItems(){
		List<Map<String, Object>> imgItems = new ArrayList<Map<String, Object>>();
		for(int i=0; i<image_ids.length; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", image_ids[i]);
			imgItems.add(map);
		}
		Toast.makeText(getActivity(), ""+ imgItems.size(), Toast.LENGTH_SHORT).show();
		return imgItems;
	}

	@Override
	public void onResume() {
		Log.d("MainContentFrag", "OnResume");
		//更新内容
		getPrefItems();
		mGridItems = getGridItems();
		mGridView.setAdapter(new MainContentGridItemAdapter(getActivity(), mGridItems));
		super.onResume();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		mViewFlow.onConfigurationChanged(newConfig);
	}
	
	
}
