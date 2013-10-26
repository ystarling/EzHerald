package com.herald.ezherald.mainframe;

import java.util.List;
import java.util.Map;

import com.herald.ezherald.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondMenuListItemAdapter extends BaseAdapter {
	private static final int VIEW_TYPE_SIZE = 2;

	private static final int VIEW_TYPE_ACCOUNT = 0;

	private static final int VIEW_TYPE_GENERAL = 1;
	
	private static final String HIGHLIGHT_COLOR  = "#eb3c4b";
	private static final String NORMAL_COLOR = "#777777";

	/**
	 * 右侧菜单列表项的Adapter
	 */
	private final String TAG = "Ez:SecondMenuListItemAdapter";

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
		public ImageView icon;
		public TextView title;
	}

	public final class ListAccountItemView {
		// 账户登陆状态的控件集
		public ImageView icon;
		public TextView title;
		public TextView idCardText; // 一卡通状态
		public TextView tyxText; // 体育系状态
		public TextView libText; // 图书馆状态
	}

	public SecondMenuListItemAdapter(Context context,
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
	public int getItemViewType(int position) {
		int type;
		switch (position) {
		case 0:
			type = VIEW_TYPE_ACCOUNT;
			break;
		default:
			type = VIEW_TYPE_GENERAL;
			break;
		}
		return type;
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE_SIZE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 获得视图
		// Log.d(TAG, "getView");
		// 自定义视图
		ListItemView listItemView = null;
		ListAccountItemView accountItemView = null;
		int type = getItemViewType(position);
		if (convertView == null) {

			// 获取菜单项布局文件的视图
			if (type == VIEW_TYPE_GENERAL) {
				listItemView = new ListItemView();
				// 除了第0行账户信息之外的行
				convertView = mListContainer.inflate(
						R.layout.main_frame_menu_item, null);
				// 获取控件对象
				listItemView.icon = (ImageView) convertView
						.findViewById(R.id.main_frame_menuitem_icon);
				listItemView.title = (TextView) convertView
						.findViewById(R.id.main_frame_menuitem_title);
				// 设置控件集到convertView
				convertView.setTag(listItemView);
			} else {
				// 账户信息那一行
				accountItemView = new ListAccountItemView();
				convertView = mListContainer.inflate(
						R.layout.main_frame_2ndmenu_account_item, null);

				accountItemView.icon = (ImageView) convertView
						.findViewById(R.id.main_frame_menuitem_accicon);
				accountItemView.title = (TextView) convertView
						.findViewById(R.id.main_frame_menuitem_acctitle);
				accountItemView.idCardText = (TextView) convertView
						.findViewById(R.id.main_frame_menuitem_idCardNum);
				accountItemView.libText = (TextView) convertView
						.findViewById(R.id.main_frame_menuitem_LibIdNum);
				accountItemView.tyxText = (TextView) convertView
						.findViewById(R.id.main_frame_menuitem_TyxIdNum);
				convertView.setTag(accountItemView);
			}

		} else {
			if (type == VIEW_TYPE_GENERAL)
				listItemView = (ListItemView) convertView.getTag();
			else{
				try{
					//莫名其妙的问题？
					accountItemView = (ListAccountItemView) convertView.getTag();
				} catch (ClassCastException e){
					listItemView = (ListItemView) convertView.getTag();
				}
			}
		}

		// 设置文字和图标

		if (type == VIEW_TYPE_GENERAL) {
			listItemView.icon.setImageResource((Integer) mListItems.get(
					position).get("icon"));
			listItemView.title.setText((String) mListItems.get(position).get(
					"title"));
		} else {
			accountItemView.icon.setImageResource((Integer) mListItems.get(
					position).get("icon"));
			accountItemView.title.setText((String) mListItems.get(position)
					.get("title"));

			String idCardState = (String) mListItems.get(position).get(
					"idCardState");
			String tyxState = (String) mListItems.get(position)
					.get("tyxState");
			String libState = (String) mListItems.get(position)
					.get("libState");

			accountItemView.idCardText.setText(idCardState);
			accountItemView.tyxText.setText(tyxState);
			accountItemView.libText.setText(libState);
			//字的颜色
			if(idCardState.equals(SecondMenuFragment.TEXT_IDCARD_IS_LOGIN)){
				accountItemView.idCardText.setTextColor(Color.parseColor(HIGHLIGHT_COLOR));
			} else {
				accountItemView.idCardText.setTextColor(Color.parseColor(NORMAL_COLOR));
			}
			if(tyxState.equals(SecondMenuFragment.TEXT_TYX_IS_LOGIN)){
				accountItemView.tyxText.setTextColor(Color.parseColor(HIGHLIGHT_COLOR));
			} else {
				accountItemView.tyxText.setTextColor(Color.parseColor(NORMAL_COLOR));
			}
			if(libState.equals(SecondMenuFragment.TEXT_LIB_IS_LOGIN)){
				accountItemView.libText.setTextColor(Color.parseColor(HIGHLIGHT_COLOR));
			} else {
				accountItemView.libText.setTextColor(Color.parseColor(NORMAL_COLOR));
			}

		}

		return convertView;
	}

}
