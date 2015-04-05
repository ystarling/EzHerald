package com.herald.ezherald.mainframe;

import java.util.List;
import java.util.Map;
import com.herald.ezherald.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 主界面模块中ListView项目内容的Adapter (替代原来的GridView)
 * 
 * @author BorisHe
 * @since 20131018
 */
public class MainContentListItemAdapter extends BaseAdapter {

	private final String TAG = "MainContentListItemAdapter";

	private Context mContext;
	private List<Map<String, Object>> mGridItemList; // Grid项信息
	private LayoutInflater mGridContainer; // 视图容器

	private final String STR_UNDEF_ITEM_CONT1 = "点击这里";
	private final String STR_UNDEF_ITEM_CONT2 = "定义需要显示的模块";
	private final int IMG_UNDEF_ITEM_RESID = R.drawable.main_2ndmenu_ic_accsetting; // 图标

	/**
	 * Grid项目的自定义控件集
	 * 
	 * @author BorisHe
	 * 
	 */
	public final class GridItemView {
		public ImageView icon; // 模块图标
		public TextView content1; // 内容1
		public TextView content2; // 内容2
	}

	public MainContentListItemAdapter(Context c,
			List<Map<String, Object>> gridItems) {
		mContext = c;
		mGridContainer = LayoutInflater.from(mContext);
		mGridItemList = gridItems;
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
	 * 规定Id: 0 - 未设置; 1 - curriculum; 2 - activity; 3 - agenda; 4 - library; 5 -
	 * gpa; 6 - exercise; 7 - academic; 8 - freshman;9 - emptyclassroom
     * 10 - srtp
	 */
//	private final String[] ModuleNames = { "纳尼?第一次么?", "课表自习", "校园活动", "我的日程",
//			"图书查询", "绩点查询", "跑操查询", "教务信息", "校园指南" };

	@Override
	public long getItemId(int position) {

		String tag = (String) mGridItemList.get(position).get("title");
		if (tag.equals("curriculum"))
			return 1;
		else if (tag.equals("activity"))
			return 2;
		else if (tag.equals("agenda"))
			return 3;
		else if (tag.equals("library"))
			return 4;
		else if (tag.equals("gpa"))
			return 5;
		else if (tag.equals("exercise"))
			return 6;
		else if (tag.equals("academic"))
			return 7;
		else if (tag.equals("freshman"))
			return 8;
		else if (tag.equals("emptyclassroom"))
			return 9;
        else if(tag.equals("srtp"))
            return 10;
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 获得视图
		GridItemView gridItemView;
		if (convertView == null) {// if it's not recycled, initialize some
									// attributes
			gridItemView = new GridItemView();
			// 获取菜单项布局文件的视图
			convertView = mGridContainer.inflate(
					R.layout.main_frame_content_list_item, null);
			// 获取控件对象
			gridItemView.icon = (ImageView) convertView
					.findViewById(R.id.main_frame_content_listitem_icon);
			gridItemView.content1 = (TextView) convertView
					.findViewById(R.id.main_frame_content_listitem_content1);
			gridItemView.content2 = (TextView) convertView
					.findViewById(R.id.main_frame_content_listitem_content2);
			// 设置控件集到convertView
			convertView.setTag(gridItemView);
		} else {
			gridItemView = (GridItemView) convertView.getTag();
		}

		// 设置icon与contents
		if(getItemId(position) != 0){
			gridItemView.icon.setImageResource((Integer) mGridItemList
					.get(position).get("icon"));
		}
		
		String cont1txt = (String) mGridItemList.get(position).get("content1");
		if (getItemId(position) == 0) {
			// 未定义，替换文本内容
			cont1txt = STR_UNDEF_ITEM_CONT1;
		}

		gridItemView.content1.setText(cont1txt);
		
		
		String cont2txt = (String) mGridItemList.get(position).get("content2");
		if (getItemId(position) == 0) {
			// 未定义，替换文本内容
			cont2txt = STR_UNDEF_ITEM_CONT2;
		}
		gridItemView.content2.setText(cont2txt);
		if(cont2txt.isEmpty()){
			gridItemView.content2.setVisibility(View.GONE);
		} else {
			gridItemView.content2.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

}
