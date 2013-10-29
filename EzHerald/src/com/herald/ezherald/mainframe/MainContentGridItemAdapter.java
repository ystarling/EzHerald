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
 * ������ģ����GridView��Ŀ���ݵ�Adapter
 * @author BorisHe
 * @since 20130630
 * @updated 20130630
 */
public class MainContentGridItemAdapter extends BaseAdapter {

	private final String TAG = "Ez:MainContentGridItemAdapter";
	
	private Context mContext;
	private List< Map<String, Object> > mGridItemList; // Grid����Ϣ
	private LayoutInflater mGridContainer; //��ͼ����
	private Random mRandom;
	
	private final String STR_UNDEF_ITEM_CONT1 = "�������";
	private final String STR_UNDEF_ITEM_CONT2 = "������Ҫ��ʾ��ģ��";
	
	/**
	 * Grid��Ŀ���Զ���ؼ���
	 * @author BorisHe
	 *
	 */
	public final class GridItemView{
		public TextView title;		//ģ������
		public TextView content1;	//����1
		public TextView content2;	//����2
		public TextView bigText;   //�޴����
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
	 * �涨Id:
	 * 0 - δ����;
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
			"����?��һ��ô?",
			"�α��ѯ", "У԰�", "�ҵ��ճ�", 
			"ͼ���ѯ", "�����ѯ", "�ܲٲ�ѯ",
			"������Ϣ", "У԰ָ��"
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
		// �����ͼ
		GridItemView gridItemView;
		if(convertView == null){// if it's not recycled, initialize some attributes
			gridItemView = new GridItemView();
			// ��ȡ�˵�����ļ�����ͼ
			convertView = mGridContainer.inflate(R.layout.main_frame_content_grid_item, null);
			// ��ȡ�ؼ�����
			gridItemView.title = (TextView)convertView
					.findViewById(R.id.main_frame_content_griditem_title);
			gridItemView.content1 = (TextView)convertView
					.findViewById(R.id.main_frame_content_griditem_content1);
			gridItemView.content2 = (TextView)convertView
					.findViewById(R.id.main_frame_content_griditem_content2);
			gridItemView.bigText = (TextView)convertView
					.findViewById(R.id.main_frame_content_griditem_bigtext);
			// ���ÿؼ�����convertView
			convertView.setTag(gridItemView);
		}	else{
			gridItemView = (GridItemView)convertView.getTag();
		}
		
		// ����title��contents
		String titleText = ModuleNames[(int) getItemId(position)];
		gridItemView.title.setText(titleText);
		//gridItemView.title.setTextColor(getRandomLightColor());
		
		
		String cont1txt = (String) mGridItemList.get(position).get("content1");
		if(getItemId(position)==0){
			//δ���壬�滻�ı�����
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
			//δ���壬�滻�ı�����
			cont2txt = STR_UNDEF_ITEM_CONT2;
		}
		/*if(cont2txt.length() > 15)
		{
			cont2txt = cont2txt.substring(0, 12);
			cont2txt += "...";
		}*/ //ֱ��Layout��������android:ellipsize
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
	 * ������������ʺϵ��ֺŴ�С
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
		//ʹ��HSV�������ĵ�ɫ
		int h = mRandom.nextInt(360);
		float s = 0.17f;
		float v = 0.95f;
		float hsv[] = {h,s,v};
		return Color.HSVToColor(hsv);
	}
}
