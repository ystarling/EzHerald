package com.herald.ezherald.mainframe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.MainActivity;
import com.herald.ezherald.R;
import com.herald.ezherald.academic.AcademicActivity;
import com.herald.ezherald.academic.AcademicDataGrabber;
import com.herald.ezherald.activity.ActiActivity;
import com.herald.ezherald.agenda.AgendaActivity;
import com.herald.ezherald.curriculum.CurriDataGrabber;
import com.herald.ezherald.curriculum.CurriculumActivity;
import com.herald.ezherald.exercise.ExerciseActivity;
import com.herald.ezherald.exercise.ExerciseGrabber;
import com.herald.ezherald.freshman.FreshmanActivity;
import com.herald.ezherald.freshman.FreshmanGrabber;
import com.herald.ezherald.gpa.GPAActivity;
import com.herald.ezherald.gpa.GpaGrabber;
import com.herald.ezherald.library.LibraryActivity;
import com.herald.ezherald.library.LibraryContentGrabber;
import com.herald.ezherald.settingframe.MainContentModulePrefActivity;
import com.tendcloud.tenddata.TCAgent;

/**
 * �ϴ�ͼƬ: 
 * http://121.248.63.105/EzHerald/pictureupload/ 
 * �ϴ�����:
 * http://121.248.63.105/EzHerald/updateupload/ 
 * ͼƬjson��ʾ��������:
 * http://121.248.63.105/EzHerald/picturejson/ 
 * ����json��ʾ����һ��:
 * http://121.248.63.105/EzHerald/updatejson/
 * 
 * @author BorisHe
 * 
 */

/*
 * @author �β�ΰ
 * 
 * @since 20130514
 * 
 * @updated 20130630
 * 
 * ������������ݵ�Fragement ������ģ��ɲ��ձ�Fragement�Ķ����������
 */
public class MainContentFragment extends SherlockFragment {
	// private GridView mGridView; // GridView
	private ListView mListView; // ListView�����GridView��

	private ViewFlow mViewFlow; // ViewFlow
	private CircleFlowIndicator mCircIndic;

	private InfoHandler mInfoHandler = new InfoHandler();

	private List<Map<String, Object>> mGridItems;
	private List<Map<String, Object>> mImageItems;

	private final String PREF_NAME = "com.herald.ezherald_preferences";
	private final String KEY_NAME = "module_choice";

	private final String MAPKEY_TITLE = "title";
	private final String MAPKEY_CONT1 = "content1";
	private final String MAPKEY_CONT2 = "content2";
	private final String MAPKEY_ICON = "icon";

	private final String MAPKEY_IMAGE = "image";
	private final String MAPKEY_TYPE = "type";

	private final int MAX_BANNER_SIZE = 5;

	private ArrayList<String> mContentTitles = new ArrayList<String>();
	

	// ////////////Temporarily used local variables///////////////////
	String mContentCont1[] = { "������", "������", "������", "������", "������", "������", "������",
			"������" };
	String mContentCont2[] = { "������", "������", "������", "������", "������", "������", "������",
			"������" };

	private boolean mContentIsDestroyed = false;
	// /////��������������������������������������������������������////////
	private static final int[] image_ids = { R.drawable.main_frame_pic0,
			R.drawable.main_frame_pic1, R.drawable.main_frame_pic2,
			R.drawable.main_frame_pic3, R.drawable.main_frame_pic4 };

	private int[] color_ids // ������ListView�ı���ɫ
	= { R.drawable.main_content_listview_round_shape_blue,
			R.drawable.main_content_listview_round_shape_green,
			R.drawable.main_content_listview_round_shape_red,
			R.drawable.main_content_listview_round_shape_navy,
			R.drawable.main_content_listview_round_shape_purple,
			R.drawable.main_content_listview_round_shape_orange,
			R.drawable.main_content_listview_round_shape_yellow};

	public ViewFlow getViewFlow() {
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
		// ��ͼ
		View v = inflater.inflate(R.layout.main_frame_content, null);

		mContentIsDestroyed = false;
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		

		getPrefItems();

		// mGridView = (GridView) getActivity().findViewById(
		// R.id.main_frame_content_gridView);

		mGridItems = getGridItems();

		//int margin = 5;
		// mGridView.setPadding(margin, 0, margin, 0); // have the margin on the
		// sides as well
		//
		// mGridView.setAdapter(new MainContentGridItemAdapter(getActivity(),
		// mGridItems));
		// mGridView.setOnItemClickListener(new MyOnItemClickListener());

		// /////////
		mListView = (ListView) getActivity().findViewById(
				R.id.main_frame_content_listView);
		mListView.setAdapter(new MainContentListItemAdapter(getActivity(),
				mGridItems));
		mListView.setOnItemClickListener(new MyOnItemClickListener());

		List<Integer> preferredColors = getPreferredColors();
		Random random = new Random();
		int random_index = random.nextInt(preferredColors.size());
		mListView.setBackgroundResource(preferredColors.get(random_index));

		// /////////

		mViewFlow = (ViewFlow) getActivity().findViewById(
				R.id.main_frame_viewflow);
		mImageItems = getImageItems();
		mViewFlow.setAdapter(new MainContentFlowItemAdapter(getActivity(),
				mImageItems));

		mCircIndic = (CircleFlowIndicator) getActivity().findViewById(
				R.id.main_frame_viewflow_indic);

		mViewFlow.setFlowIndicator(mCircIndic);

		mViewFlow.setTimeSpan(5000);
		mViewFlow.startAutoFlowTimer();

		// mInfoHandler = new InfoHandler();
		refreshViewFlowImage();
	}
	
	/**
	 * ����ѡ���л�ÿ��������������ɫ
	 * ��ת����resource id
	 * @return
	 */
	private List<Integer> getPreferredColors() {
		String pref_name = getActivity().getResources().getString(R.string.main_frame_preferences);
		String pref_key = getActivity().getResources().getString(R.string.main_frame_list_color_pref_key);
		SharedPreferences prefs = getActivity().getSharedPreferences(pref_name, Context.MODE_PRIVATE);
		Set<String> prefColorNames = SharedPreferencesHandler.getStringSet(prefs, pref_key, null);
		List<Integer> result = new ArrayList<Integer>();
		if(prefColorNames == null ||prefColorNames.isEmpty()){
			for(int resid : color_ids){
				result.add(resid);
			}
		} else {
			for(String colorName: prefColorNames){
				if(colorName.equals("blue")){
					result.add(R.drawable.main_content_listview_round_shape_blue);
				} else if (colorName.equals("green")){
					result.add(R.drawable.main_content_listview_round_shape_green);
				} else if (colorName.equals("red")){
					result.add(R.drawable.main_content_listview_round_shape_red);
				} else if (colorName.equals("navy")){
					result.add(R.drawable.main_content_listview_round_shape_navy);
				} else if (colorName.equals("purple")){
					result.add(R.drawable.main_content_listview_round_shape_purple);
				} else if (colorName.equals("orange")){
					result.add(R.drawable.main_content_listview_round_shape_orange);
				} else {
					result.add(R.drawable.main_content_listview_round_shape_yellow);
				}
			}
		}
		
		return result;
	}

	/**
	 * ˢ��ViewFlow
	 */
	public void refreshViewFlowImage() {
		if (mViewFlow == null) {
			Log.e("MainContentFrag", "Fail to refresh. mViewFlow = null");
		}

		// �Ƿ�����ʹ��?
		// //mViewFlow.refreshDrawableState();
		mViewFlow.setAdapter(new MainContentFlowItemAdapter(getActivity(),
				mImageItems));
		mViewFlow.refreshDrawableState();
		Log.d("MainContentFrag", "ViewFlow refreshed..");
	}

	/**
	 * ���ƫ������
	 */
	private void getPrefItems() {
		// ɾ���ɵĶ���
		mContentTitles.clear();

		// ���ƫ������
		SharedPreferences appPrefs = getSherlockActivity()
				.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		Set<String> result_set;
		// try {
		// result_set = appPrefs.getStringSet(KEY_NAME, null);
		// } catch (NoSuchMethodError e) {
		result_set = SharedPreferencesHandler.getStringSet(appPrefs, KEY_NAME,
				null);
		// }
		if (null != result_set && result_set.size() > 0) {
			for (String result : result_set) {
				// Toast.makeText(getActivity(), result,
				// Toast.LENGTH_SHORT).show();
				mContentTitles.add(result);
			}
		} else {
			mContentTitles.add("��δ����");
			//Toast.makeText(getActivity(), "NULL�� use default",
			//		Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * ���̸߳���ģ������ ���Ŀǰû��ʵ������ӿڣ���ô������һ��null�Ľ�ȥ
	 * 
	 * @param moduleName
	 *            ģ��Ӣ������
	 */
	private void fetchInfoObjForName(String moduleName, int index) {
		// MainContentGridItemObj obj = null;
		MainContentInfoGrabber grabber = null;
		try {
			if (moduleName.equals("curriculum")) {
				grabber = new CurriDataGrabber(getActivity());
			} else if (moduleName.equals("academic")) {
				grabber = new AcademicDataGrabber();
			} else if (moduleName.equals("freshman")) {
				grabber = new FreshmanGrabber();
			} else if (moduleName.equals("gpa")) {
				grabber = new GpaGrabber(getActivity());
			} else if (moduleName.equals("exercise")) {
				grabber = new ExerciseGrabber(getActivity());
			} else if (moduleName.equals("library")) {
				grabber = new LibraryContentGrabber(getActivity());
			}
			// else if ....f
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("MainContentFragment", "���³���..");
		}
		if (grabber != null) {
			new Thread(new InfoRunnable(grabber, index)).start();
		}
		// mContentInfoObjs.add(obj);
	}

	/**
	 * ����ģ�����Ƶõ�ͼ�����Դid
	 * 
	 * @param moduleName
	 * @return
	 */
	private int fetchIconForName(String moduleName) {
		int retId = R.drawable.main_2ndmenu_ic_setting;
		if (moduleName.equals("curriculum")) {
			retId = R.drawable.main_menu_ic_curriculum;
		} else if (moduleName.equals("academic")) {
			retId = R.drawable.main_menu_ic_academic;
		} else if (moduleName.equals("freshman")) {
			retId = R.drawable.main_menu_ic_freshman;
		} else if (moduleName.equals("gpa")) {
			retId = R.drawable.main_menu_ic_gpa;
		} else if (moduleName.equals("exercise")) {
			retId = R.drawable.main_menu_ic_exercise;
		} else if (moduleName.equals("library")) {
			retId = R.drawable.main_menu_ic_library;
		} else if (moduleName.equals("activity")) {
			retId = R.drawable.main_menu_ic_activity;
		}
		return retId;
	}

	/*
	 * ���Item��ת������ģ��
	 */
	private class MyOnItemClickListener implements
			AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			String clickTarget = "Unknown";
			switch ((int) id) {
			case 0:
				i.setClass(getActivity(), MainContentModulePrefActivity.class);
				MainActivity mainActivity = (MainActivity) getActivity();
				mainActivity.needRefreshContent = true;
				clickTarget = "Main";
				break;
			case 1:
				i.setClass(getActivity(), CurriculumActivity.class);
				clickTarget = "Curr";
				break;
			case 2:
				i.setClass(getActivity(), ActiActivity.class);
				clickTarget = "Acti";
				break;
			case 3:
				i.setClass(getActivity(), AgendaActivity.class);
				clickTarget = "Agen";
				break;
			case 4:
				i.setClass(getActivity(), LibraryActivity.class);
				clickTarget = "Libr";
				break;
			case 5:
				i.setClass(getActivity(), GPAActivity.class);
				clickTarget = "GPA";
				break;
			case 6:
				i.setClass(getActivity(), ExerciseActivity.class);
				clickTarget = "Exer";
				break;
			case 7:
				i.setClass(getActivity(), AcademicActivity.class);
				clickTarget = "Acad";
				break;
			case 8:
				i.setClass(getActivity(), FreshmanActivity.class);
				clickTarget = "Fres";
				break;
			}
			TCAgent.onEvent(getActivity(), "������ListView���", clickTarget);
			
			if (i != null) {
				startActivity(i);
				if (id != 0)
					getActivity().finish();
			}
		}

	}

	/**
	 * ��ʼ���˵�����Ϣ
	 */
	private List<Map<String, Object>> getGridItems() {
		// Update content info
		int index = 0;
		for (String title : mContentTitles) {
			fetchInfoObjForName(title, index++);
		}

		List<Map<String, Object>> gridItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mContentTitles.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(MAPKEY_TITLE, mContentTitles.get(i)); // tag of a title
			map.put(MAPKEY_CONT1, mContentCont1[i]);
			map.put(MAPKEY_CONT2, mContentCont2[i]);
			map.put(MAPKEY_ICON, fetchIconForName(mContentTitles.get(i))); // for
																			// listview
			gridItems.add(map);
		}

		return gridItems;
	}

	/**
	 * ��ʼ��ͼƬ��Ϣ key: image ͼƬ��ID����ֱ�ӵ�Bitmap���� key: type 0-ͼƬ��ԴID, 1-Bitmap����
	 */
	private List<Map<String, Object>> getImageItems() {
		List<Map<String, Object>> imgItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < image_ids.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", image_ids[i]);
			map.put("type", 0);
			imgItems.add(map);
		}
		// Toast.makeText(getActivity(), ""+ imgItems.size(),
		// Toast.LENGTH_SHORT).show();
		return imgItems;
	}

	/**
	 * ����ĳһ���ͼƬ����
	 * 
	 * @param index
	 * @param bitmap
	 */
	public void updateImageItem(int index, Bitmap bitmap) {
		if (mImageItems == null || mImageItems.size() < index - 1) {
			Log.e("MainContentFrag.", "Invalid image item position");
			return;
		}
		mImageItems.get(index).put(MAPKEY_TYPE, 1);
		mImageItems.get(index).put(MAPKEY_IMAGE, bitmap);
		Log.d("MainContentFrag", "Image updated :\n"
				+ mImageItems.get(index).toString());
	}

	@Override
	public void onResume() {
		Log.d("MainContentFrag", "OnResume");
		// ��������
		super.onResume();
		refreshInfo();
		MainActivity mainActivity = (MainActivity) getActivity();
		if (mainActivity.needRefreshContent) {
			Log.d("MainContentFragment", "Refreshing info");

			mainActivity.needRefreshContent = false;
		}
	}

	/*
	 * ��������
	 */
	public void refreshInfo() {
		// Toast.makeText(getActivity(), "Refreshing...",
		// Toast.LENGTH_SHORT).show();
		getPrefItems();
		// ͬ����ȡ��ģ��ĸ�����Ŀ
		mGridItems = getGridItems();
		// mGridView.setAdapter(new MainContentGridItemAdapter(getActivity(),
		// mGridItems));
		mListView.setAdapter(new MainContentListItemAdapter(getActivity(),
				mGridItems));

		refreshImageFromDb();
	}

	/**
	 * �����ݿ��Ȼ��banner���� ����еĻ����滻����̬��
	 */
	public void refreshImageFromDb() {
		ArrayList<Bitmap> retList = new ArrayList<Bitmap>();
		MainFrameDbAdapter dbAdapter = new MainFrameDbAdapter(
				getSherlockActivity());
		dbAdapter.open();
		Cursor cs = dbAdapter.getAllImages();
		if (cs != null && cs.moveToFirst()) {
			int count = 0;
			do {
				byte[] inBytes = cs.getBlob(1);
				retList.add(BitmapFactory.decodeByteArray(inBytes, 0,
						inBytes.length));
				count++;
			} while (count < MAX_BANNER_SIZE && cs.moveToNext());
		} else {
			Log.w("MainActivity", "db record does not exist");
		}
		for (int i = 0; i < retList.size(); i++) {
			updateImageItem(i, retList.get(i));
		}
		refreshViewFlowImage();
		dbAdapter.close();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		mViewFlow.onConfigurationChanged(newConfig);
	}

	/**
	 * ��ƫ���и��¸�ģ����Ϣ������ ֱ�Ӹ���mContentCont
	 */
	public void loadGridContentsFromPref() {
		SharedPreferences prefs = getActivity().getPreferences(
				Context.MODE_PRIVATE);
		// prefs.get
	}

	private class InfoHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if (mContentIsDestroyed)
				return;

			int retVal = msg.arg1;
			if (retVal == 200) {
				// ���½���
				int index = msg.arg2;
				mGridItems.get(index).put(MAPKEY_CONT1, mContentCont1[index]);
				mGridItems.get(index).put(MAPKEY_CONT2, mContentCont2[index]);
			}

			// mGridView.setAdapter(new
			// MainContentGridItemAdapter(getActivity(),
			// mGridItems));
			mListView.setAdapter(new MainContentListItemAdapter(getActivity(),
					mGridItems));
		}

	}

	private class InfoRunnable implements Runnable {
		private MainContentInfoGrabber g = null;
		private int i; // index

		public InfoRunnable(MainContentInfoGrabber grabber, int index) {
			g = grabber;
			i = index;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			MainContentGridItemObj obj = g.GrabInformationObject();
			if (obj == null) {
				Log.e("MainContentFrag:InfoRunnable",
						"Not a valid MainContentGridItemObj");
				return;
			}

			mContentCont1[i] = obj.getContent1();
			mContentCont2[i] = obj.getContent2();

			Message msg = new Message();
			msg.arg1 = 200;
			msg.arg2 = i; // index
			Looper.prepare();

			mInfoHandler.sendMessage(msg);
		}
	}

	@Override
	public void onDestroy() {
		mContentIsDestroyed = true;
		mInfoHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

}
