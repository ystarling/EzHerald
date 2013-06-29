package com.herald.ezherald.activity;

import java.util.ArrayList;
import java.util.List;

import com.herald.ezherald.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ClubListAdapter extends BaseAdapter {
	
	private List<ClubItem> clubList ;
	Context context;
	
	public ClubListAdapter(Context c)
	{
		context = c;
		clubList = new ArrayList<ClubItem>();
		clubList.add(new ClubItem("","跆拳道",true));
		clubList.add(new ClubItem("","学生会",false));
	}
	
	public void setClubList(ClubItem [] clubArr)
	{
		clubList.clear();
		for (int loop = 0; loop < clubArr.length; ++loop)
		{
			clubList.add(clubArr[loop]);
		}
	}
	
	public void setClubList(List<ClubItem> clubList)
	{
		clubList.clear();
		clubList.addAll(clubList);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return clubList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return clubList.get(arg0) ;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		convertView = LayoutInflater.from(context).inflate(R.layout.acti_club_list_item, null);
		ClubHolder holder = new ClubHolder();
		holder.clubIcon = (ImageView) convertView.findViewById(R.id.acti_club_list_icon);
		holder.clubName = (TextView) convertView.findViewById(R.id.acti_club_list_name);
		holder.focusBtn = (Button) convertView.findViewById(R.id.acti_club_list_focus_btn);
		ClubItem club = clubList.get(position);
		holder.clubName.setText(club.getClubName());
		holder.clubIcon.setImageResource(R.drawable.ic_launcher);
		if (club.checkFocus())
		{
			holder.focusBtn.setText("已关注");
			holder.focusBtn.setBackgroundColor(Color.GRAY);
		}
		else
		{
			holder.focusBtn.setText("关注");
			holder.focusBtn.setBackgroundColor(Color.rgb(100, 100, 255));
		}
		holder.focusBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
			}
			
		});
		
		
		Log.v("ClubListAdapter", "is here");
		
		return convertView;
	}
	
	
	private class ClubHolder{
		public ImageView clubIcon;
		public TextView clubName;
		public Button focusBtn;
	}

}
