package com.herald.ezherald.mainframe;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.herald.ezherald.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 主界面模块中GridView项目内容的Adapter
 * @author BorisHe
 * @since 20130630
 * @updated 20130630
 */
public class MainContentGridItemAdapter extends BaseAdapter {

	private final String TAG = "Ez:MainContentGridItemAdapter";
	
	private Context mContext;
	private List< Map<String, Object> > mGridItemList; // Grid项信息
	private LayoutInflater mGridContainer; //视图容器
	private Random mRandom;
	
	private final String STR_UNDEF_ITEM_CONT1 = "点击这里";
	private final String STR_UNDEF_ITEM_CONT2 = "定义需要显示的模块";
	
	/**
	 * Grid项目的自定义控件集
	 * @author BorisHe
	 *
	 */
	public final class GridItemView{
		public TextView title;		//模块名字
		public TextView content1;	//内容1
		public TextView content2;	//内容2
		public TextView bigText;   //巨大的字
	}
	
	public MainContentGridItemAdapter(Context c, List<Map<String, Object>> gridItems){
		mContext = c;
		mGridContainer = LayoutInflater.from(mContext);
		mGridItemList = gridItems;
		mRandom = new Random();
	}
	
	@Override
	public int getCount() {
		return mGridItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mGridItemList.get(position);
	}

	/**
	 * 规定Id:
	 * 0 - 未设置;
	 * 1 - curriculum;
	 * 2 - activity;
	 * 3 - agenda;
	 * 4 - library;
	 * 5 - gpa;
	 * 6 - exercise;
	 * 7 - academic;
	 * 8 - freshman;
	 */
	private final String[] ModuleNames = {
			"纳尼?第一次么?",
			"课表查询", "校园活动", "我的日程", 
			"图书查询", "绩点查询", "跑操查询",
			"教务信息", "校园指南"
	};
	
	@Override
	public long getItemId(int position) {
		
		String tag = (String) mGridItemList.get(position).get("title");
		if(tag.equals("curriculum"))
			return 1;
		else if(tag.equals("activity"))
			return 2;
		else if(tag.equals("agenda"))
			return 3;
		else if(tag.equals("library"))
			return 4;
		else if(tag.equals("gpa"))
			return 5;
		else if(tag.equals("exercise"))
			return 6;
		else if(tag.equals("academic"))
			return 7;
		else if(tag.equals("freshman"))
			return 8;
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 获得视图
		GridItemView gridItemView;
		if(convertView == null){// if it's not recycled, initialize some attributes
			gridItemView = new GridItemView();
			// 获取菜单项布局文件的视图
			convertView = mGridContainer.inflate(R.layout.main_frame_content_grid_item, null);
			// 获取控件对象
			gridItemView.title = (TextView)convertView
					.findViewById(R.id.main_frame_content_griditem_title);
			gridItemView.content1 = (TextView)convertView
					.findViewById(R.id.main_frame_content_griditem_content1);
			gridItemView.content2 = (TextView)convertView
					.findViewById(R.id.main_frame_content_griditem_content2);
			gridItemView.bigText = (TextView)convertView
					.findViewById(R.id.main_frame_content_griditem_bigtext);
			// 设置控件集到convertView
			convertView.setTag(gridItemView);
		}	else{
			gridItemView = (GridItemView)convertView.getTag();
		}
		
		// 设置title与contents
		String titleText = ModuleNames[(int) getItemId(position)];
		gridItemView.title.setText(titleText);
		//gridItemView.title.setTextColor(getRandomLightColor());
		
		
		String cont1txt = (String) mGridItemList.get(position).get("content1");
		if(getItemId(position)==0){
			//未定义，替换文本内容
			cont1txt = STR_UNDEF_ITEM_CONT1;
		}
		/*if(cont1txt.length() > 20)
		{
			cont1txt = cont1txt.substring(0, 17);
			cont1txt += "...";
		}*/
		gridItemView.content1.setText(cont1txt);
		int cont1size = cont1txt.length();
		gridItemView.content1.setTextSize(getFittedTextSize(cont1size));
		gridItemView.content1.bringToFront();
		
		
		
		String cont2txt = (String) mGridItemList.get(position).get("content2");
		if(getItemId(position)==0){
			//未定义，替换文本内容
			cont2txt = STR_UNDEF_ITEM_CONT2;
		}
		/*if(cont2txt.length() > 15)
		{
			cont2txt = cont2txt.substring(0, 12);
			cont2txt += "...";
		}*/ //直接Layout里面设置android:ellipsize
		int cont2size = cont2txt.length();
		gridItemView.content2.setText(cont2txt);
		gridItemView.content2.bringToFront();
		/*float cont2txtsize = (float) (getFittedTextSize(cont2size) / 1.5);
		if(cont2txtsize > 20)
			cont2txtsize /= 1.2;
		gridItemView.content2.setTextSize(cont2txtsize);*/
		
		gridItemView.bigText.setText(titleText.subSequence(0, 1));
		
		return convertView;
	}
	
	/**
	 * 根据字数获得适合的字号大小
	 * @param txtLength
	 * @return
	 */
	private int getFittedTextSize(int txtLength){
		
		/*if (txtLength <= 3)
			return 36;
		else if(txtLength <= 5)
			return 28;
		else if(txtLength < 10)
			return 24;
		else if(txtLength < 15)
			return 22;
		else
			return 20;*/
		double result = -8.328 * Math.log(txtLength) + 43.571;
		return (int)result;
	}
	
	private int getRandomLightColor(){
		//使用HSV获得随机的淡色
		int h = mRandom.nextInt(360);
		float s = 0.17f;
		float v = 0.95f;
		float hsv[] = {h,s,v};
		return Color.HSVToColor(hsv);
	}
}
