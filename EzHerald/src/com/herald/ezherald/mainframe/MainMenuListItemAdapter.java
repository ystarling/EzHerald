package com.herald.ezherald.mainframe;

import java.util.List;
import java.util.Map;

import com.herald.ezherald.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuListItemAdapter extends BaseAdapter {
	/**
	 * ���˵��б����Adapter
	 */
	private final String TAG = "Ez:MainMenuListItemAdapter";

	private Context mContext; // ����������
	private List<Map<String, Object>> mListItems; // �˵�����Ϣ
	private LayoutInflater mListContainer; // ��ͼ����

	public final class ListItemView {
		// �Զ���ؼ���
		public ImageView icon;
		public TextView title;
	}

	public MainMenuListItemAdapter(Context context,
			List<Map<String, Object>> listItems) {
		/**
		 * ���캯��
		 */
		mContext = context;
		mListContainer = LayoutInflater.from(mContext); // ������ͼ
		mListItems = listItems;
	}

	@Override
	public int getCount() {
		// ��Ŀ����
		return mListItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mListItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// �����ͼ
		//Log.d(TAG, "getView");
		// �Զ�����ͼ
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// ��ȡ�˵�����ļ�����ͼ
			convertView = mListContainer.inflate(R.layout.main_frame_menu_item,
					null);
			// ��ȡ�ؼ�����
			listItemView.icon = (ImageView) convertView
					.findViewById(R.id.main_frame_menuitem_icon);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.main_frame_menuitem_title);
			// ���ÿؼ�����convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// �������ֺ�ͼ��

		listItemView.icon.setImageResource((Integer) mListItems.get(position)
				.get("icon"));
		listItemView.title.setText((String) mListItems.get(position).get(
				"title"));
		Boolean selected = (Boolean)mListItems.get(position).get("selected");
		if(selected!= null && selected.equals(true)){
			//convertView.setBackgroundColor(Color.parseColor("#10ff0000"));
			listItemView.title.setTextColor(Color.parseColor("#2c9bff"));
		}
		
		return convertView;
	}

}
