package com.herald.ezherald.academic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.herald.ezherald.R;

public class AcademicDetailActivity extends SherlockActivity {

	Context context;
	private final String DETAIL_URL = "http://herald.seu.edu.cn/herald_web_service/jwc/detaile/%d/";
	private ProgressDialog progressDialog;
	
	private String infoTitle;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = this;
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("请稍候 ... ");
		
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.academic_detail);
		 Intent intent = getIntent();
		 int id = intent.getIntExtra("id", -1);
		 String url = String.format(DETAIL_URL, id);
		 progressDialog.show();
		 try {
			 new RequestJwcDetail().execute(new URL(url) );
		
		 } catch (MalformedURLException e) {
		 // TODO Auto-generated catch block
			 e.printStackTrace();
			 progressDialog.cancel();
		 }

		ActionBar actionbar = this.getSupportActionBar();
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
			Intent intent = new Intent(Intent.ACTION_SEND);

			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			intent.putExtra(Intent.EXTRA_TEXT,
					"教务处发布了新的通知："+ infoTitle);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(Intent.createChooser(intent, "分享到"));
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
						JSONArray jsonArr = new JSONArray(str);
						int id = jsonArr.getInt(0);
						String type = jsonArr.getString(1);
						String title = jsonArr.getString(2);
						String date = jsonArr.getString(3);
						String con = jsonArr.getString(4);
						JSONArray apps = jsonArr.getJSONArray(5);
						List<Link> links = new ArrayList<Link>();
						for(int i=0; i<apps.length();++i)
						{
							JSONArray app = apps.getJSONArray(i);
							Link link = new Link(app.getString(0), app.getString(1));
							links.add(link);
						}
						JwcInfo info = new JwcInfo(type, title, date, id);
						info.setContent(con);
						info.setAppendix(links);
						return info;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(JwcInfo info) {
			if (info != null) {
				LinearLayout layout = (LinearLayout) findViewById(R.id.academic_detail);
				TextView type = (TextView) findViewById(R.id.academic_detail_type);
				TextView title = (TextView) findViewById(R.id.academic_detail_title);
				TextView date = (TextView) findViewById(R.id.academic_detail_date);
				TextView content = (TextView) findViewById(R.id.academic_detail_content);

				type.setText(info.GetType());
				title.setText(info.GetTitle());
				date.setText(info.GetDate());
				content.setText(info.getContent());
				
				infoTitle = info.GetTitle();
				
				List<Link> links = info.getAppendixs();
				String linkText = "<a href=\"%s\"><u>%s</u></a>";
				for(Link link: links)
				{
					String realLinkText = String.format(linkText, link.getUrl(), link.getTitle());
					TextView tv = new TextView(context);
					tv.setTextColor(Color.BLUE);
//					tv.setAutoLinkMask(Linkify.ALL);
					tv.setText(Html.fromHtml(realLinkText));
//					tv.setText(link.getTitle()+link.getUrl());
					tv.setMovementMethod(LinkMovementMethod.getInstance());
					layout.addView(tv);
				}

				// Toast.makeText(getApplicationContext(), info.GetIntro(),
				// Toast.LENGTH_SHORT).show();
				progressDialog.cancel();
			}

		}

	}

}
