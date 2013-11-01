package com.herald.ezherald.freshman;

import android.app.AlertDialog.Builder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class FreshmanDetailFragment extends SherlockFragment {
	private TextView txtDetail, txtTitle;
	private String detail, title;

	public void setDetail(String str) {
		detail = str;
	}

	public void setTitle(String str) {
		title = str;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group,
			Bundle saved) {
		// super.onCreate(savedInstanceState);
		
		return inflater.inflate(R.layout.freshman_detail, group, false);

		
	}

	private void initView() {
		txtDetail = (TextView) getActivity().findViewById(R.id.txt_detail);
		txtDetail.setText(detail);
		txtTitle = (TextView) getActivity().findViewById(R.id.txt_titile);
		txtTitle.setText(title);
		txtTitle.getPaint().setFakeBoldText(true);//¼Ó´Ö
	}
}
