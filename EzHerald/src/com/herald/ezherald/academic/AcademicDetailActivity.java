package com.herald.ezherald.academic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;

public class AcademicDetailActivity extends SherlockActivity {

	Context context;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.academic_detail);
		// Intent intent = getIntent();
		// String url = intent.getStringExtra("url");
		// try {
		// new RequestJwcDetail().execute(new URL("http://www.baidu.com/") );
		//
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		setContentView(R.layout.academic_webview_test);
		WebView wv = (WebView) findViewById(R.id.academic_webview);
		WebSettings websetting = wv.getSettings();
		websetting.setBuiltInZoomControls(true);
		wv.loadUrl("http://www.baidu.com");

		ActionBar actionbar = this.getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowTitleEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.menu_acdemic_detail, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			// Intent intent = new Intent(this, AcademicActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
			finish();
			return true;
		case R.id.menu_academic_detail_share:
			// PopupWindow popWin;
			// View pwView =
			// getLayoutInflater().inflate(R.layout.academic_detail_popwin_share,
			// null);
			// popWin = new PopupWindow(pwView, 300, 210, true);
			// popWin.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.spinner_background));
			// //View clickView = item.getActionView();
			// Display display = getWindowManager().getDefaultDisplay();
			// int posX = ( display.getWidth() - popWin.getWidth() ) /2;
			// int posY = ( display.getHeight() - popWin.getHeight() )/2 ;
			// popWin.showAsDropDown(pwView,posX, posY);
			//
			// Button btn_to_weibo = (Button)
			// pwView.findViewById(R.id.academic_detail_shareto_weibo);
			// Button btn_to_renren = (Button)
			// pwView.findViewById(R.id.academic_detail_shareto_renren);
			// btn_to_weibo.setOnClickListener(new OnClickListener(){
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// Toast.makeText(context, "share to weibo",
			// Toast.LENGTH_SHORT).show();
			// }
			//
			// });
			//
			// btn_to_renren.setOnClickListener(new OnClickListener(){
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// Toast.makeText(context, "share to renren",
			// Toast.LENGTH_SHORT).show();
			// }
			//
			// });
			Intent intent = new Intent(Intent.ACTION_SEND);

			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "����");
			intent.putExtra(Intent.EXTRA_TEXT,
					"I would like to ��herald campus�� this with you...");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(Intent.createChooser(intent, "����"));
			return true;

		}

		return false;
	}

	private class RequestJwcDetail extends AsyncTask<URL, Integer, JwcInfo> {

		@Override
		protected JwcInfo doInBackground(URL... params) {
			// TODO Auto-generated method stub
			URL url = params[0];
			int response = -1;
			InputStream in = null;
			URLConnection conn;

			try {
				conn = url.openConnection();
				if (!(conn instanceof HttpURLConnection)) {
					throw new IOException("NOT AN HTTP CONNECTION");
				} else {
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					httpConn.setAllowUserInteraction(false);
					httpConn.setInstanceFollowRedirects(true);
					httpConn.setRequestMethod("GET");
					httpConn.connect();
					response = httpConn.getResponseCode();
					if (response == HttpURLConnection.HTTP_OK) {
						in = httpConn.getInputStream();
						String str = DataTypeTransition.InputStreamToString(in);
						JwcInfo info = new JwcInfo("", "", "", str);
						return info;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(JwcInfo info) {
			if (info != null) {
				TextView type = (TextView) findViewById(R.id.academic_detail_type);
				TextView title = (TextView) findViewById(R.id.academic_detail_title);
				TextView date = (TextView) findViewById(R.id.academic_detail_date);
				TextView content = (TextView) findViewById(R.id.academic_detail_content);

				String txt = "��һ��  Ϊ�淶�Կ���Υ����Ϊ���϶��봦��ά��ѧУ���ԵĹ�ƽ��������"
						+ "��֤������ѧ��͹�ƽ�����Ļ��������ݹ����йع涨�������Уʵ����������ƶ����涨��"
						+ "�ڶ���  ���涨���ƿ�����ָѧУ��Ժ��ϵ�����ο���ʦ��֯����ѧ��ѧҵ�йصĸ�����ʽ�Ŀ��ԣ�"
						+ "�����վ���ԣ���ʵ�顢�ϻ��ȿ��ԣ����뿪����ԡ�������ԡ����ԡ�������������ϡ�׫д���ģ���ƣ���"
						+ "���б���ȷ�ʽ��";
				JwcInfo jwcInfo = new JwcInfo("[�������]", "����֪ͨ", "2013-6-25",
						txt);
				type.setText(jwcInfo.GetType());
				title.setText(jwcInfo.GetTitle());
				date.setText(jwcInfo.GetDate());
				content.setText(jwcInfo.GetIntro());

				// Toast.makeText(getApplicationContext(), info.GetIntro(),
				// Toast.LENGTH_SHORT).show();
			}

		}

	}

}
