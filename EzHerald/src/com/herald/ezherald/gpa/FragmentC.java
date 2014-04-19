package com.herald.ezherald.gpa;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.herald.ezherald.R;
import com.herald.ezherald.account.Authenticate;
import com.herald.ezherald.account.UserAccount;

public class FragmentC extends Fragment {
	private CircleChat circleChat;
	private UserAccount user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group,
			Bundle saved) {
		return inflater.inflate(R.layout.gpa_frag_c, group, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		user = Authenticate.getIDcardUser(getActivity()) ;
			circleChat = (CircleChat) getActivity().findViewById(
					R.id.circleChat);
			if( !circleChat.hasData() ) {
				Toast.makeText(getActivity(), "请先更新数据", Toast.LENGTH_LONG).show();
			}
			circleChat.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_UP) {// 弹起时触发，否则影响滑动
						ArrayList<Record> records = circleChat.onTouch(
								event.getX(), event.getY());
						if (records == null) {// 第一次运行可能没有数据
							return true;
						}
						AlertDialog.Builder builderSingle = new AlertDialog.Builder(
								getActivity());
						builderSingle.setIcon(R.drawable.ic_launcher);
						builderSingle.setTitle(String.format("共%d门", records.size()));
						final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
								getActivity(),
								android.R.layout.simple_list_item_1);
						for (Record r : records) {
							arrayAdapter.add(r.getName() + "   " + r.getScore());
						}
						builderSingle.setNegativeButton("关闭",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});

						builderSingle.setAdapter(arrayAdapter, null);
						builderSingle.show();

					}
					return true;
				}

			});
		}
	

}
