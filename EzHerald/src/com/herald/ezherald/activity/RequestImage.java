package com.herald.ezherald.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;



public class RequestImage extends AsyncTask<ViewAndUrl,Integer,Bitmap>
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
		Log.v("ICON", "request url: "+url.toString());
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
			if(flag == 0)
			{
				Log.v("Save Icon", "complete save icon");
			}
			else
			{
				Log.v("Save Icon", "complete save pic");
			}
			img.setVisibility(View.VISIBLE);
			img.setImageBitmap(result);
			//img.setLayoutParams(new LayoutParams(40, 40));
			//
			//actiInfoHolder.clubIcon.setImageResource(R.drawable.ic_launcher);
		}
		else{
			img.setVisibility(View.GONE);
		}
		
	}
	
}
