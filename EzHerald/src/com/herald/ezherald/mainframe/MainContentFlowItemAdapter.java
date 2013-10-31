package com.herald.ezherald.mainframe;

import java.util.List;
import java.util.Map;

import com.herald.ezherald.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MainContentFlowItemAdapter extends BaseAdapter{
	/**
	 * ������ViewFlow��Adapter
	 */
	private final String TAG = "Ez:MainContentViewFlowItemAdapter";

	private Context mContext; // ����������
	private List<Map<String, Object>> mListItems; // �˵�����Ϣ
	public List<Map<String, Object>> getmListItems() {
		return mListItems;
	}

	public void setmListItems(List<Map<String, Object>> mListItems) {
		this.mListItems = mListItems;
	}

	private LayoutInflater mListContainer; // ��ͼ����

	public final class ListItemView {
		// �Զ���ؼ���
		public ImageView image;
	}

	public MainContentFlowItemAdapter(Context context,
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
		//return Integer.MAX_VALUE; 
		//���غܴ��ֵʹ��getView�е�position����������ʵ��ѭ��
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
		Log.d(TAG, "getView");
		// �Զ�����ͼ
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// ��ȡ�˵�����ļ�����ͼ
			convertView = mListContainer.inflate(R.layout.main_frame_content_imageitem,
					null);
			// ��ȡ�ؼ�����
			listItemView.image = (ImageView) convertView
					.findViewById(R.id.main_frame_content_imageView);
			// ���ÿؼ�����convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// �������ֺ�ͼ��
		int type = (Integer)mListItems.get(position % mListItems.size()).get("type");
		if(type == 0){
			listItemView.image.setImageResource((Integer) mListItems.get(position % mListItems.size())
					.get("image"));
		}
		else if(type == 1){
			listItemView.image.setImageBitmap((Bitmap)mListItems.get(position % mListItems.size())
					.get("image"));
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			listItemView.image.setLayoutParams(lp);
		}
		else{
			Log.e("MainContentFlowItemAdapter", "Wrong resource type: " + type);
		}
		
		return convertView;
	}
	
}
