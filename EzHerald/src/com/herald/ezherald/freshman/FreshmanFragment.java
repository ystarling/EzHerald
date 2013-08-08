package com.herald.ezherald.freshman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

public class FreshmanFragment extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle saved){
		return inflater.inflate(R.layout.freshman_main,group,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		WebView webview = (WebView)getActivity().findViewById(R.id.webView);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setUserAgentString("Android");
		webview.loadUrl("http://herald.seu.edu.cn/freshman_test/demo.html");
	}
}
