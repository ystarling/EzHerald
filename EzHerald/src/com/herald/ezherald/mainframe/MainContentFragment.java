package com.herald.ezherald.mainframe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.AcademicActivity;
import com.herald.ezherald.activity.ActiActivity;
import com.herald.ezherald.agenda.AgendaActivity;
import com.herald.ezherald.curriculum.CurriculumActivity;
import com.herald.ezherald.exercise.ExerciseActivity;
import com.herald.ezherald.freshman.FreshmanActivity;
import com.herald.ezherald.gpa.GPAActivity;
import com.herald.ezherald.library.LibraryActivity;
import com.herald.ezherald.settingframe.MainContentModulePrefActivity;

/*
 * @author 何博伟
 * @since 20130514
 * @updated 20130630
 * 
 * 主界面呈现内容的Fragement
 * 其他各模块可参照本Fragement的定义呈现内容
 */
public class MainContentFragment extends SherlockFragment {
	private GridView mGridView;  //GridView
	private ViewFlow mViewFlow;  //ViewFlow
	private CircleFlowIndicator mCircIndic;
	
	
	private List<Map<String, Object>> mGridItems;
	private List<Map<String, Object>> mImageItems;
	
	private final String PREF_NAME = "com.herald.ezherald_preferences";
	private final String KEY_NAME = "module_choice";
	private final int MAX_BANNER_SIZE = 5;
	
	
	private ArrayList<String> mContentTitles = new ArrayList<String>();
	
	
	//////////////Temporarily used local variables///////////////////
	String mContentCont1 [] = {"关于2013年暑假放假的通知", "教学楼管理规定", "新社团活动", "DDD", "放假啦", "2013年6月全国大学英语四六级考试“多提多卷”注意事项发布会", "GGG", "HHH"};
	String mContentCont2 [] = {"SSSSSSSSSSSSSSXXXX", "TTT", "UUU", "VVV", "尼玛真不容易啊终于放假了啊！！！" ,"东南大学教务处", "YYY", "ZZZ"};
	///////Should be removed after we have a SharedPreference////////
	private static final int[] image_ids =
		{R.drawable.main_frame_pic0, R.drawable.main_frame_pic1,
		R.drawable.main_frame_pic2, R.drawable.main_frame_pic3,
		R.drawable.main_frame_pic4
		};
	//TODO:使用时应当先用本地存储内容，然后Async通过网络更新本地存储内容，再更新视图
	///////Should be removed after we have a SharedPreference////////

	public MainContentFragment() {
	}
	
	public ViewFlow getViewFlow(){
		return mViewFlow;
	}
	
	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
		args.getString("text");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

	}

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
		
		//mActionBar = getSherlockActivity().getSupportActionBar();
		
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
	 * 刷新ViewFlow
	 */
	public void refreshViewFlowImage(){
		if(mViewFlow == null){
			Log.e("MainContentFrag", "Fail to refresh. mViewFlow = null");
		}
		
		//是否这样使用?
		mViewFlow.refreshDrawableState();
		mViewFlow.setAdapter(new MainContentFlowItemAdapter(getActivity(), mImageItems));
		Log.d("MainContentFrag", "ViewFlow refreshed..");
	}
	
	/**
	 * 获得偏好设置
	 */
	private void getPrefItems() {
		// 删除旧的东西
		mContentTitles.clear();
		
		// 获得偏好设置
		SharedPreferences appPrefs =
				getSherlockActivity().getSharedPreferences(
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
				i.setClass(getActivity(), MainContentModulePrefActivity.class);
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
	 * key: image 图片的ID或者直接的Bitmap对象
	 * key: type  0-图片资源ID, 1-Bitmap对象
	 */
	private List<Map<String, Object>> getImageItems(){
		List<Map<String, Object>> imgItems = new ArrayList<Map<String, Object>>();
		for(int i=0; i<image_ids.length; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", image_ids[i]);
			map.put("type", 0);
			imgItems.add(map);
		}
		//Toast.makeText(getActivity(), ""+ imgItems.size(), Toast.LENGTH_SHORT).show();
		return imgItems;
	}
	
	/**
	 * 更新某一项的图片对象
	 * @param index
	 * @param bitmap
	 */
	public void updateImageItem(int index, Bitmap bitmap){
		if(mImageItems == null || mImageItems.size() < index-1){
			Log.e("MainContentFrag.", "Invalid image item position");
			return;
		}
		mImageItems.get(index).put("type", 1);
		mImageItems.get(index).put("image", bitmap);
		Log.d("MainContentFrag", "Image updated :\n" + mImageItems.get(index).toString());
	}

	@Override
	public void onResume() {
		Log.d("MainContentFrag", "OnResume");
		//更新内容
		super.onResume();
		refreshInfo();
	}

	/*
	 * 更新内容
	 */
	public void refreshInfo() {
		//Toast.makeText(getActivity(), "Refreshing...", Toast.LENGTH_SHORT).show();
		getPrefItems();
		//同步获取各模块的更新项目
		mGridItems = getGridItems();
		mGridView.setAdapter(new MainContentGridItemAdapter(getActivity(), mGridItems));
		
		refreshImageFromDb();
	}
	
	/**
	 * 从数据库先获得banner数据
	 * 如果有的话，替换掉静态的
	 */
	public void refreshImageFromDb(){
		ArrayList<Bitmap> retList = new ArrayList<Bitmap>();
		MainFrameDbAdapter dbAdapter = new MainFrameDbAdapter(getSherlockActivity());
		dbAdapter.open();
		Cursor cs = dbAdapter.getAllImages();
		if(cs != null && cs.moveToFirst()){
			int count = 0;
			do{
				byte[] inBytes = cs.getBlob(1); 
				retList.add(BitmapFactory.decodeByteArray(inBytes, 0, inBytes.length));
				count ++;
			}while(count < MAX_BANNER_SIZE && cs.moveToNext());		
		} else {
			Log.w("MainActivity", "db record does not exist");
		}
		for(int i=0; i<retList.size(); i++){
			updateImageItem(i, retList.get(i));
		}
		refreshViewFlowImage();
	}
	

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		mViewFlow.onConfigurationChanged(newConfig);
	}

}
