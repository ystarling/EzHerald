package com.herald.ezherald.freshman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class FreshmanFaqFragment extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		return inflater.inflate(R.layout.freshman_main,group,false);
	}
 
}
