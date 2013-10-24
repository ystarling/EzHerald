/**
 * 
 */
package com.herald.ezherald.library;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;

import com.herald.ezherald.R;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.LibAccountActivity;
import com.herald.ezherald.account.UserAccount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author BIG_SEA
 *
 */
public class LibraryFragmentMine extends SherlockFragment{
	
	/**
	 * 个人借阅界面
	 */
	 ListView mlistView;
	 @Override
	public void onResume() {
		// TODO Auto-generated method stub
			activity=getActivity();
			context=getActivity();
			UserAccount LibUser = Authenticate.getLibUser(activity);
			
			if(LibUser==null){
				
				ImageView image=(ImageView) activity.findViewById(R.id.libr_mine_NoBook2);
				image.setImageResource(R.drawable.libr_mine_nobook);
				TextView text=(TextView) activity.findViewById(R.id.libr_mine_NoBook);
				text.setText("点击登录");
				text.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(activity,LibAccountActivity.class);
						startActivity(intent);
					}
				});
			}else{
				LibraryFragmentMineThread th=new LibraryFragmentMineThread(view,getActivity(),context);
				th.start();
				ImageView image=(ImageView) activity.findViewById(R.id.libr_mine_NoBook2);
				TextView text=(TextView) activity.findViewById(R.id.libr_mine_NoBook);
				image.setImageResource(0);
				text.setText("");
			}
		super.onResume();
	}
	 
	Activity activity;
	Context context;
	View view;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group,Bundle save){
		view=inflater.inflate(R.layout.library_fragment_mine, null);
		Activity ac=getActivity();
		ac.setTitle("我的图书馆");

		
		activity=getActivity();
		context=getActivity();
		UserAccount LibUser = Authenticate.getLibUser(ac);
		
		if(LibUser==null){
			Intent intent=new Intent(ac,LibAccountActivity.class);
			startActivity(intent);
		}else{
			LibraryFragmentMineThread th=new LibraryFragmentMineThread(view,getActivity(),context);
			th.start();
		}
		return view;
	
	}
//	
//	public List<HashMap<String,Object>> get_value(){
//		
//		ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
//		HashMap<String,Object> map=new HashMap<String,Object>();
//		for(int i=0;i<=2;i++){
//		map.clear();
//		map.put("barcode", i+"条码号: "+"0000123");
//		map.put("author", "题名/责任者: "+"姜戎");
//		map.put("borrow_date", "借阅日期: "+"2013/1/1");
//		map.put("remand_date", "归回日期: "+"2013/1/2");
//		map.put("renew_num", "续借次数: "+"1");
//		map.put("collection", "馆藏地: "+"九龙湖");
//		map.put("attachment", "附件: "+"无");
//		
//		list.add(map);
//		}
//		return list;
//		
//	}
//	public class mAdapter extends BaseAdapter{
//		
//		private LayoutInflater inflater;
//		public mAdapter(Context c){
//			this.inflater=LayoutInflater.from(c);
//		}
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return get_value().size();
//		}
//
//		@Override
//		public Object getItem(int arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			ViewHolder holder;
//			if(convertView==null){
//				convertView=inflater.inflate(R.layout.library_mine_list_item, null);
//				holder=new ViewHolder();
//				holder.barCode=(TextView) convertView.findViewById(R.id.libr_mine_barcode);
//				holder.author=(TextView) convertView.findViewById(R.id.libr_mine_author);
//				holder.borrow_date=(TextView) convertView.findViewById(R.id.libr_mine_borrow_date);
//				holder.remand_date=(TextView) convertView.findViewById(R.id.libr_mine_remand_date);
//				holder.renew_num=(TextView) convertView.findViewById(R.id.libr_mine_renew_num);
//				holder.collection=(TextView) convertView.findViewById(R.id.libr_mine_collection);
//				holder.attachment=(TextView) convertView.findViewById(R.id.libr_mine_attachment);
//				holder.renew_btn=(Button) convertView.findViewById(R.id.libr_mine_renew_btn);
//				convertView.setTag(holder);
//			}
//			else{
//				holder=(ViewHolder) convertView.getTag();
//			}
//			
//			
//			holder.barCode.setText(get_value().get(position).get("barcode").toString());
//			holder.author.setText(get_value().get(position).get("author").toString());
//			holder.borrow_date.setText(get_value().get(position).get("borrow_date").toString());
//			holder.remand_date.setText(get_value().get(position).get("remand_date").toString());
//			holder.renew_num.setText(get_value().get(position).get("renew_num").toString());
//			holder.collection.setText(get_value().get(position).get("collection").toString());
//			holder.attachment.setText(get_value().get(position).get("attachment").toString());
//			holder.renew_btn.setText("续借");
//			
//			holder.renew_btn.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Toast th=Toast.makeText(getActivity(), "你猜", Toast.LENGTH_LONG);
//					th.show();
//				}
//				
//			});
//			
//			return convertView;
//		}
//		
//		
//	}
//	
//	public class ViewHolder{
//		public TextView barCode;
//		public TextView author;
//		public TextView borrow_date;
//		public TextView remand_date;
//		public TextView renew_num;
//		public TextView collection;
//		public TextView attachment;
//		public Button renew_btn;
//	}
	

}
