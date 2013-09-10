package com.herald.ezherald.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import com.herald.ezherald.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActiInfoAdapter extends BaseAdapter {

	Activity context;
	List<ActiInfo> actiInfoList;
	ActiDBAdapter DBAdapter;
	

	public ActiInfoAdapter(Activity c) {
		context = c;
		actiInfoList = new LinkedList<ActiInfo>();
		DBAdapter = new ActiDBAdapter(context);
//		DBAdapter.open();
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
	
	public int getLastActiId()
	{
		return actiInfoList.get(actiInfoList.size()-1).getId();
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
		ActiInfoHolder actiInfoHolder = actiInfoHolder = new ActiInfoHolder(); ;
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
		actiInfoHolder.outIcon = (LinearLayout) convertView
				.findViewById(R.id.acti_acti_list_outer_clubicon);
		actiInfoHolder.time = (TextView) convertView.findViewById(R.id.acti_listitem_acti_time);
		actiInfoHolder.place = (TextView) convertView.findViewById(R.id.acti_listitem_acti_place);

		ActiInfo actiInfo = actiInfoList.get(position);
		actiInfoHolder.clubName.setText(actiInfo.getClubName());
		actiInfoHolder.actiTitle.setText(actiInfo.getActiTitle());
		actiInfoHolder.actiPubTime.setText(actiInfo.getActiPubTime());
		actiInfoHolder.actiIntro.setText(actiInfo.getActiIntro());
		actiInfoHolder.time.setText(actiInfo.getStartTime()+"至"+actiInfo.getEndTime());
		actiInfoHolder.place.setText(actiInfo.getPlace());
		
		//actiInfoHolder.clubIcon.setImageResource(R.drawable.ic_launcher);
		//actiInfoHolder.actiPic.setImageResource(R.drawable.ic_launcher);
		
		int acti_id = actiInfo.getId();
		DBAdapter.open();
		if(DBAdapter.checkHaveIcon(acti_id))
		{
			Bitmap bitmap = DBAdapter.getClubIconByActi(acti_id);
			actiInfoHolder.clubIcon.setImageBitmap(bitmap);
			Log.v("DB ICON", "have icon");
		}
		else
		{
			try {
				new RequestImage().execute(new ViewAndUrl(actiInfoHolder.clubIcon,new URL(actiInfo.getClubIconURL()),
						0,acti_id));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.v("DB ICON", "no icon");
		}
		
		
		
		if(actiInfo.checkHaveActiPic())
		{
			if(DBAdapter.checkHaveActiPic(acti_id))
			{
				Bitmap bitmap = DBAdapter.getActiPicByActi(acti_id);
				actiInfoHolder.actiPic.setImageBitmap(bitmap);
				Log.v("DB PIC", "have pic");
			}
			else{
				try {
					new RequestImage().execute(new ViewAndUrl(actiInfoHolder.actiPic,new URL(actiInfo.getActiPicURL()),
							1,acti_id));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.v("DB PIC", "no pic");
			}
			
		}
		DBAdapter.close();
		final String name = actiInfo.getClubName();
		final String title = actiInfo.getActiTitle();

		actiInfoHolder.actiShareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_SEND);

				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				intent.putExtra(Intent.EXTRA_TEXT,
						name +" 发布了新活动《"+title+"》");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(Intent.createChooser(intent, "分享到"));

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
		public TextView time;
		public TextView place;
		public TextView actiPubTime;
		public TextView actiIntro;
		public ImageView actiPic;
		public ImageButton actiShareBtn;
		public LinearLayout outIcon;

	}
	
	private class RequestImage extends AsyncTask<ViewAndUrl,Integer,Bitmap>
	{
		ImageView img;
		int flag;
		int id;
		@Override
		protected Bitmap doInBackground(ViewAndUrl... params) {
			// TODO Auto-generated method stub
			InputStream in = null;
			Bitmap bitmap = null;
			int response = -1;
			img = (ImageView) params[0].view;
			URL url = params[0].url;
			flag= params[0].flag;
			id = params[0].id;
			try {
				URLConnection conn = url.openConnection();
				if(!(conn instanceof HttpURLConnection) )
				{
					throw new IOException("Not a HTTP Connection");
				}
				else
				{
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					httpConn.setAllowUserInteraction(false);
					httpConn.setInstanceFollowRedirects(true);
					httpConn.setRequestMethod("GET");
					httpConn.connect();
					response = httpConn.getResponseCode();
					if (response == HttpURLConnection.HTTP_OK)
					{
						in = httpConn.getInputStream();
						bitmap = BitmapFactory.decodeStream(in);
						in.close();
						return bitmap;
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Bitmap result)
		{
			Log.v("REQUEST", "request completed");
			if(result != null)
			{
				DBAdapter.open();
				if(flag == 0)
				{
					DBAdapter.updateClubIconByActi(id, result);
					Log.v("Save Icon", "complete save icon");
				}
				else
				{
					DBAdapter.updateActiPicByActi(id, result);
					Log.v("Save Icon", "complete save pic");
				}
				img.setVisibility(View.VISIBLE);
				img.setImageBitmap(result);
				DBAdapter.close();
				//img.setLayoutParams(new LayoutParams(40, 40));
				//
				//actiInfoHolder.clubIcon.setImageResource(R.drawable.ic_launcher);
			}
			
		}
		
	}
	
	private class ViewAndUrl
	{
		public View view;
		public URL url;
		public int flag;
		public int id;
		public ViewAndUrl(View v,URL u,int f,int i)
		{
			view= v;
			url = u;
			flag = f;
			id = i;
		}
	}

}


