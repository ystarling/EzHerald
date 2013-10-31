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
	 * 主界面ViewFlow的Adapter
	 */
	private final String TAG = "Ez:MainContentViewFlowItemAdapter";

	private Context mContext; // 运行上下文
	private List<Map<String, Object>> mListItems; // 菜单项信息
	public List<Map<String, Object>> getmListItems() {
		return mListItems;
	}

	public void setmListItems(List<Map<String, Object>> mListItems) {
		this.mListItems = mListItems;
	}

	private LayoutInflater mListContainer; // 视图容器

	public final class ListItemView {
		// 自定义控件集
		public ImageView image;
	}

	public MainContentFlowItemAdapter(Context context,
			List<Map<String, Object>> listItems) {
		/**
		 * 构造函数
		 */
		mContext = context;
		mListContainer = LayoutInflater.from(mContext); // 创建视图
		mListItems = listItems;
	}

	@Override
	public int getCount() {
		// 项目数量
		return mListItems.size();
		//return Integer.MAX_VALUE; 
		//返回很大的值使得getView中的position不断增大来实现循环
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
		// 获得视图
		Log.d(TAG, "getView");
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取菜单项布局文件的视图
			convertView = mListContainer.inflate(R.layout.main_frame_content_imageitem,
					null);
			// 获取控件对象
			listItemView.image = (ImageView) convertView
					.findViewById(R.id.main_frame_content_imageView);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图标
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
