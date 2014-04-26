package com.herald.ezherald.bookingOffice;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.herald.ezherald.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class BookingFragment extends SherlockFragment {
       BookingDBAdapter mDB = null;
       private JSONArray mJsonArray = null;
       private final String infoUrl = "http://function1994.sinaapp.com/index.php";
	
	    
        @Override
        public void onCreate(Bundle savedInstanceState){
        	super.onCreate(savedInstanceState);
            mDB = new BookingDBAdapter(getActivity());
            mDB.open();
            int i;
            for(i=0;i<mJsonArray.length();i++){
                try {
                    JSONObject jsonObj = mJsonArray.getJSONObject(i);
                    mDB.insert(jsonObj.getInt("id"),
                            jsonObj.getString("caption"),
                            jsonObj.getString("poster"),
                            jsonObj.getInt("number"),
                            jsonObj.getString("activity_time"),
                            jsonObj.getString("deadline")
                    );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
        
        @Override 
        public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        	return inflater.inflate(R.layout.booking_fragment,container,false);
        }
    private JSONArray getInfo(){
        JSONArray infoRet = null;
        GetBookingListJsonInfoViaUrl getInfoViaUrl = new GetBookingListJsonInfoViaUrl(infoRet);
        try {
            getInfoViaUrl.execute(new URL(infoUrl));
        }
        catch (MalformedURLException e){
            e.printStackTrace();
            return infoRet;
        }

        return infoRet;
    }
    private class GetBookingListJsonInfoViaUrl extends AsyncTask<URL,Integer,JSONArray> {
        public JSONArray  mJsonList = null;
        public GetBookingListJsonInfoViaUrl(JSONArray jsonList){
            mJsonList = jsonList;
        }
        @Override
        protected void onPostExecute(JSONArray jsonList){
            Log.v("BookingListAdapter", "getBookingListJsonInfo request complete!");
            try {
                mJsonList = new JSONArray(jsonList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected JSONArray doInBackground(URL... params) {
            Log.v("BookingListAdapter","getBookingListJsonInfo request start!");
            JSONArray jsonArray = null;
            try {
                URLConnection conn =  params[0].openConnection();
                if(!(conn instanceof HttpURLConnection)){
                    throw new IOException("GetBookingListJsonInfo is not a http connect");
                }
                else{
                    HttpURLConnection httpConn = (HttpURLConnection) conn;
                    httpConn.setAllowUserInteraction(false);
                    httpConn.setInstanceFollowRedirects(true);
                    httpConn.setConnectTimeout(10 * 1000);
                    httpConn.setReadTimeout(60 * 1000);
                    httpConn.setRequestMethod("GET");
                    httpConn.connect();
                    int response = httpConn.getResponseCode();
                    if(response== java.net.HttpURLConnection.HTTP_OK){
                        InputStream in = httpConn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder buffer = new StringBuilder();
                        String line;
                        try {
                            while ((line = reader.readLine()) != null) {
                                buffer.append(line + "\n");
                            }
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                        finally {
                            try{
                                in.close();
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }

                        try {
                            jsonArray = new JSONArray(buffer.toString());
                            mJsonList = jsonArray;
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            return jsonArray;
                        }


                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return jsonArray;
            }
            return  jsonArray;
        }
    }
}
