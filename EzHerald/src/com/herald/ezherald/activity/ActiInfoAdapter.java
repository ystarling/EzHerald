package com.herald.ezherald.activity;

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
import android.widget.TextView;
import android.widget.Toast;

public class ActiInfoAdapter extends BaseAdapter {

	Activity context;
	List<ActiInfo> actiInfoList;

	public ActiInfoAdapter(Activity c) {
		context = c;
		actiInfoList = new LinkedList<ActiInfo>();
	}

	public void setActiInfoList(ActiInfo[] actiArr) {
		actiInfoList.clear();
		for (int loop = 0; loop < actiArr.length; ++loop) {
			actiInfoList.add(actiArr[loop]);
		}
	}

	public void setActiInfoList(List<ActiInfo> actiList) {
		actiInfoList.clear();
		actiInfoList.addAll(actiList);
	}

	public void addActiInfoList(ActiInfo[] actiArr) {
		for (int loop = 0; loop < actiArr.length; ++loop) {
			actiInfoList.add(actiArr[loop]);
		}
	}

	public void addActiInfoList(List<ActiInfo> actiList) {
		actiInfoList.addAll(actiList);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return actiInfoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return actiInfoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(
				R.layout.acti_list_item, null);
		ActiInfoHolder actiInfoHolder = new ActiInfoHolder();
		actiInfoHolder.clubName = (TextView) convertView
				.findViewById(R.id.acti_listitem_club_name);
		actiInfoHolder.actiTitle = (TextView) convertView
				.findViewById(R.id.acti_listitem_acti_title);
		actiInfoHolder.actiPubTime = (TextView) convertView
				.findViewById(R.id.acti_listitem_acti_pubtime);
		actiInfoHolder.actiIntro = (TextView) convertView
				.findViewById(R.id.acti_listitem_acti_intro);
		actiInfoHolder.clubIcon = (ImageView) convertView
				.findViewById(R.id.acti_listitem_club_icon);
		actiInfoHolder.actiPic = (ImageView) convertView
				.findViewById(R.id.acti_listitem_acti_pic);
		actiInfoHolder.actiShareBtn = (ImageButton) convertView
				.findViewById(R.id.acti_listitem_share);

		ActiInfo actiInfo = actiInfoList.get(position);
		actiInfoHolder.clubName.setText(actiInfo.getClubName());
		actiInfoHolder.actiTitle.setText(actiInfo.getActiTitle());
		actiInfoHolder.actiPubTime.setText(actiInfo.getActiPubTime());
		actiInfoHolder.actiIntro.setText(actiInfo.getActiIntro());
		actiInfoHolder.clubIcon.setImageResource(R.drawable.ic_launcher);
		actiInfoHolder.actiPic.setImageResource(R.drawable.ic_launcher);

		actiInfoHolder.actiShareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_SEND);

				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "∑÷œÌ");
				intent.putExtra(Intent.EXTRA_TEXT,
						"I would like to °Æherald campus°Ø this with you...");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(Intent.createChooser(intent, "∑÷œÌµΩ"));

			}

		});

		actiInfoHolder.clubIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "club icon clicked", Toast.LENGTH_SHORT)
						.show();
			}

		});

		return convertView;
	}

	private class ActiInfoHolder {
		public ImageView clubIcon;
		public TextView clubName;
		public TextView actiTitle;
		public TextView actiPubTime;
		public TextView actiIntro;
		public ImageView actiPic;
		public ImageButton actiShareBtn;

	}

}
