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
	public void onCreate(Bundle savedInstanceState)
	{
		context = this;
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.academic_detail);
//		Intent intent = getIntent();
//		String url = intent.getStringExtra("url");
//		try {
//			new RequestJwcDetail().execute(new URL("http://www.baidu.com/") );
//			
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.menu_acdemic_detail, menu);
		return super.onCreateOptionsMenu(menu);
		
		
	}
	
	@Override
	public boolean onOptionsItemSelected( MenuItem item)
	{
		switch(item.getItemId())
		{
		case android.R.id.home:
            // app icon in action bar clicked; go home
//            Intent intent = new Intent(this, AcademicActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
			finish();
            return true;
		case R.id.menu_academic_detail_share:
//			PopupWindow popWin;
//			View pwView = getLayoutInflater().inflate(R.layout.academic_detail_popwin_share, null);
//			popWin = new PopupWindow(pwView, 300, 210, true);
//			popWin.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.spinner_background));
//			//View clickView =  item.getActionView();
//			Display display = getWindowManager().getDefaultDisplay();
//			int posX = ( display.getWidth() - popWin.getWidth() ) /2;
//			int posY = ( display.getHeight() - popWin.getHeight() )/2 ;
//			popWin.showAsDropDown(pwView,posX, posY);
//			
//			Button btn_to_weibo = (Button) pwView.findViewById(R.id.academic_detail_shareto_weibo);
//			Button btn_to_renren = (Button) pwView.findViewById(R.id.academic_detail_shareto_renren);
//			btn_to_weibo.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Toast.makeText(context, "share to weibo", Toast.LENGTH_SHORT).show();
//				}
//				
//			});
//			
//			btn_to_renren.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Toast.makeText(context, "share to renren", Toast.LENGTH_SHORT).show();
//				}
//				
//			});
		      Intent intent=new Intent(Intent.ACTION_SEND);
		      
		      intent.setType("text/plain");
		      intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		      intent.putExtra(Intent.EXTRA_TEXT, "I would like to ‘herald campus’ this with you...");
		      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		      context.startActivity(Intent.createChooser(intent,"分享到"));
		      return true;
		
		}
		
		return false;
	}
	
	
	private class RequestJwcDetail extends AsyncTask<URL, Integer, JwcInfo>
	{

		@Override
		protected JwcInfo doInBackground(URL... params) {
			// TODO Auto-generated method stub
			URL url = params[0];
			int response = -1;
			InputStream in = null;
			URLConnection conn;
			
			try {
				conn = url.openConnection();
				if (!(conn instanceof HttpURLConnection) )
				{
					throw new IOException("NOT AN HTTP CONNECTION");
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
						String str = DataTypeTransition.InputStreamToString(in);
						JwcInfo info = new JwcInfo("","","",str);
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
		protected void onPostExecute(JwcInfo info)
		{
			if(info != null)
			{
				TextView type = (TextView) findViewById(R.id.academic_detail_type);
				TextView title = (TextView) findViewById(R.id.academic_detail_title);
				TextView date = (TextView) findViewById(R.id.academic_detail_date);
				TextView content = (TextView) findViewById(R.id.academic_detail_content);
				
				
				
				String txt = "第一条  为规范对考试违规行为的认定与处理，维护学校考试的公平、公正，" +
						"保证优良的学风和公平竞争的环境，根据国家有关规定，结合我校实际情况，特制定本规定。"+
						"第二条  本规定所称考试是指学校、院（系）、任课老师组织的与学生学业有关的各种形式的考试，" +
						"包括闭卷笔试（含实验、上机等考试）、半开卷笔试、开卷笔试、口试、笔试与口试相结合、撰写论文（设计）、" +
						"调研报告等方式。";
				JwcInfo jwcInfo = new JwcInfo("[教务管理]","重修通知","2013-6-25",txt);
				type.setText(jwcInfo.GetType());
				title.setText(jwcInfo.GetTitle());
				date.setText(jwcInfo.GetDate());
				content.setText(jwcInfo.GetIntro());
				
				//Toast.makeText(getApplicationContext(), info.GetIntro(), Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	

}
