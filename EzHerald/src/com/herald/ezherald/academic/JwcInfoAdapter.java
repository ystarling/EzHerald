package com.herald.ezherald.academic;

import java.util.LinkedList;
import java.util.List;

import com.herald.ezherald.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class JwcInfoAdapter extends BaseAdapter {
	Activity context;
	List<JwcInfo> jwcInfoList;
	JwcInfoAdapter(Activity c)
	{
		context = c;
		jwcInfoList = new LinkedList();
	}
	
	public void setJwcInfoList(JwcInfo [] jwcArr)
	{
		jwcInfoList.clear();
		for(int loop = 0; loop < jwcArr.length; ++loop)
		{
			jwcInfoList.add(jwcArr[loop]);
		}
	}
	 
	public void setJwcInfoList(List<JwcInfo> jwcList)
	{
		jwcInfoList.clear();
		jwcInfoList.addAll(jwcList);
	}
	
	public void addJwcInfoList(JwcInfo [] jwcArr)
	{
		for(int loop=0; loop < jwcArr.length; ++loop)
		{
			jwcInfoList.add(jwcArr[loop]);
		}
	}
	
	public void addJwcInfoList(List<JwcInfo> jwcList)
	{
		jwcInfoList.addAll(jwcList);
	}
	
	public void foreAddJwcInfoList(JwcInfo [] jwcArr)
	{
		for(int loop = jwcArr.length; loop >= 0; --loop)
		{
			jwcInfoList.add(0, jwcArr[loop]);
		}
	}
	
	public void foreAddJwcInfoList(List<JwcInfo> jwcList)
	{
		jwcList.addAll(jwcInfoList);
		jwcInfoList = jwcList ;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jwcInfoList.size();
	}
	

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return jwcInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final JwcInfoHolder holder = new JwcInfoHolder();
		convertView =  LayoutInflater.from(context).inflate(R.layout.academic_info_item, null);
		holder.type = (TextView) convertView.findViewById(R.id.academic_info_item_type);
		holder.title = (TextView) convertView.findViewById(R.id.academic_info_item_title);
		holder.btn_share = (ImageView) convertView.findViewById(R.id.academic_list_btn_share);
		holder.date = (TextView) convertView.findViewById(R.id.academic_info_item_date);
		holder.intro = (TextView) convertView.findViewById(R.id.academic_info_item_intro);
		
		JwcInfo jwcInfo = jwcInfoList.get(position);
		if(jwcInfo != null)
		{
			holder.type.setText(jwcInfo.GetType());
			holder.title.setText(jwcInfo.GetTitle());
			holder.date.setText(jwcInfo.GetDate());
			holder.intro.setText(jwcInfo.GetIntro());
			holder.btn_share.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//Toast.makeText(context, "share clicked", Toast.LENGTH_SHORT).show();
//					PopupWindow popWin;
//					View pwView = LayoutInflater.from(context).inflate(R.layout.academic_popwin_share, null);
//					popWin = new PopupWindow(pwView, 235, 100, true);
//					popWin.setBackgroundDrawable(context.getResources().getDrawable(android.R.drawable.spinner_background));
//					popWin.showAsDropDown(holder.btn_share,0,0);
//					
//					ImageButton btn_to_weibo = (ImageButton) pwView.findViewById(R.id.academic_share_weibo);
//					ImageButton btn_to_renren = (ImageButton) pwView.findViewById(R.id.academic_share_renren);
//					btn_to_weibo.setOnClickListener(new OnClickListener(){
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							Toast.makeText(context, "share to weibo", Toast.LENGTH_SHORT).show();
//						}
//						
//					});
//					btn_to_renren.setOnClickListener(new OnClickListener(){
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							Toast.makeText(context, "share to renren", Toast.LENGTH_SHORT).show();
//						}
//						
//					});
				      Intent intent=new Intent(Intent.ACTION_SEND);
				      
				      intent.setType("text/plain");
				      intent.putExtra(Intent.EXTRA_SUBJECT, "∑÷œÌ");
				      intent.putExtra(Intent.EXTRA_TEXT, "I would like to °Æherald campus°Ø this with you...");
				      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				      context.startActivity(Intent.createChooser(intent,"∑÷œÌµΩ"));
					
					
				}
				
			});
		}
		
		return convertView;
	}
	
	private class JwcInfoHolder
	{
		public TextView type;
		public TextView title;
		public ImageView btn_share;
		public TextView date;
		public TextView intro;
	}

}
