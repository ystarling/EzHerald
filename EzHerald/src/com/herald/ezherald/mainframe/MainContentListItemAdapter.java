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
 * ������ģ����ListView��Ŀ���ݵ�Adapter (���ԭ����GridView)
 * 
 * @author BorisHe
 * @since 20131018
 */
public class MainContentListItemAdapter extends BaseAdapter {

	private final String TAG = "MainContentListItemAdapter";

	private Context mContext;
	private List<Map<String, Object>> mGridItemList; // Grid����Ϣ
	private LayoutInflater mGridContainer; // ��ͼ����

	private final String STR_UNDEF_ITEM_CONT1 = "�������";
	private final String STR_UNDEF_ITEM_CONT2 = "������Ҫ��ʾ��ģ��";
	private final int IMG_UNDEF_ITEM_RESID = R.drawable.main_2ndmenu_ic_accsetting; // ͼ��

	/**
	 * Grid��Ŀ���Զ���ؼ���
	 * 
	 * @author BorisHe
	 * 
	 */
	public final class GridItemView {
		public ImageView icon; // ģ��ͼ��
		public TextView content1; // ����1
		public TextView content2; // ����2
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
	 * �涨Id: 0 - δ����; 1 - curriculum; 2 - activity; 3 - agenda; 4 - library; 5 -
	 * gpa; 6 - exercise; 7 - academic; 8 - freshman;
	 */
//	private final String[] ModuleNames = { "����?��һ��ô?", "�α���ϰ", "У԰�", "�ҵ��ճ�",
//			"ͼ���ѯ", "�����ѯ", "�ܲٲ�ѯ", "������Ϣ", "У԰ָ��" };

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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// �����ͼ
		GridItemView gridItemView;
		if (convertView == null) {// if it's not recycled, initialize some
									// attributes
			gridItemView = new GridItemView();
			// ��ȡ�˵�����ļ�����ͼ
			convertView = mGridContainer.inflate(
					R.layout.main_frame_content_list_item, null);
			// ��ȡ�ؼ�����
			gridItemView.icon = (ImageView) convertView
					.findViewById(R.id.main_frame_content_listitem_icon);
			gridItemView.content1 = (TextView) convertView
					.findViewById(R.id.main_frame_content_listitem_content1);
			gridItemView.content2 = (TextView) convertView
					.findViewById(R.id.main_frame_content_listitem_content2);
			// ���ÿؼ�����convertView
			convertView.setTag(gridItemView);
		} else {
			gridItemView = (GridItemView) convertView.getTag();
		}

		// ����icon��contents
		if(getItemId(position) != 0){
			gridItemView.icon.setImageResource((Integer) mGridItemList
					.get(position).get("icon"));
		}
		
		String cont1txt = (String) mGridItemList.get(position).get("content1");
		if (getItemId(position) == 0) {
			// δ���壬�滻�ı�����
			cont1txt = STR_UNDEF_ITEM_CONT1;
		}

		gridItemView.content1.setText(cont1txt);
		
		
		String cont2txt = (String) mGridItemList.get(position).get("content2");
		if (getItemId(position) == 0) {
			// δ���壬�滻�ı�����
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
