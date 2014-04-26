package com.herald.ezherald.bookingOffice;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;




import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by medition on 2014/4/19.
 */
public class GetImageViaUrl extends AsyncTask<ViewUrlInfo,Integer,Bitmap> {
    int id;
    ImageView view;
    Context mContext;
    ProgressDialog downDialog;

    public GetImageViaUrl(Context context){
        mContext = context;
    }

    @Override
    protected Bitmap doInBackground(ViewUrlInfo...params){
        InputStream in = null;
        Bitmap picture = null;
        id = params[0].mId;
        int response = -1;
        URL url = params[0].mUrl;
        view = (ImageView)params[0].mView;

        Log.v("Booking Icon","request url : "+url.toString());
        try {
              URLConnection conn = url.openConnection();
              if(!(conn instanceof HttpURLConnection)){
                  // throw new IOException("Not a http connection!");
              }
              else{
                  HttpURLConnection httpConn = (HttpURLConnection) conn;

                  httpConn.setAllowUserInteraction(false);
                  httpConn.setInstanceFollowRedirects(true);
                 // httpConn.setReadTimeout(60 * 1000);   设置读取超时
                  httpConn.setConnectTimeout(10 * 1000);
                  httpConn.setRequestMethod("GET");

                  httpConn.connect();
                  response = httpConn.getResponseCode();

                  if(response==HttpURLConnection.HTTP_OK){
                      in = httpConn.getInputStream();
                      picture = BitmapFactory.decodeStream(in);
                      in.close();
                      return picture;
                  }

              }
        }
        catch (IOException e){
            Log.v("Booking icon","http connect exception!");
        }

        return null;

    }
    @Override
    protected void onPreExecute(){
        downDialog = ProgressDialog.show(mContext,"友情提示","正在下载");
        //Toast.makeText(mContext,"正在下载",Toast.LENGTH_SHORT);
    }
    @SuppressLint("NewApi")
    @Override
    protected void onPostExecute(Bitmap bitmap){
        //Toast.makeText(mContext,"完成下载",Toast.LENGTH_SHORT);

         Log.v("Booking Icon","Request complete!");
         if(bitmap==null){
            view.setVisibility(view.GONE);
             Log.v("Booking Icon","bitmap is null");
            downDialog.dismiss();
         }
        else{
            Log.v("Booking Icon","bitmap is not null");
            downDialog.dismiss();
            view.setVisibility(view.VISIBLE);
            view.setImageBitmap(bitmap);

         }
    }
}
