package com.herald.ezherald.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.herald.ezherald.R;


class CommentListAdapter extends BaseAdapter
{
	List<Comment> comList;
	Context context;
	
	
	public CommentListAdapter(Context c)
	{
		comList = new ArrayList<Comment>();
		context = c;
	}
	
	public void setCommentList(Comment [] comArr)
	{
		comList.clear();
		for (int loop = 0; loop < comArr.length; ++ loop)
		{
			comList.add(comArr[loop]);
		}
	}
	
	public void addCommentList(Comment [] comArr)
	{
		for (int loop = 0; loop < comArr.length; ++ loop)
		{
			comList.add(comArr[loop]);
		}
	}
	
	public void setCommentList(List<Comment> cl)
	{
		comList.clear();
		comList.addAll(cl);
	}
	
	public void addCommentList(List<Comment> cl)
	{
		comList.addAll(cl);
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return comList.get(position);
		
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.acti_club_detail_comment_list_item, null);
		Comment comment = comList.get(position);
		TextView tvCommenter = (TextView) convertView.findViewById(R.id.acti_club_detail_comment_comenter);
		TextView tvDate=  (TextView) convertView.findViewById(R.id.acti_club_detail_comment_date);
		TextView tvContent = (TextView) convertView.findViewById(R.id.acti_club_detail_comment_content);
		tvCommenter.setText(comment.name);
		tvDate.setText(comment.date);
		tvContent.setText(comment.content);
		
		return convertView;
	}
	
}

