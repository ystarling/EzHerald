package com.herald.ezherald.bookingOffice;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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


       private final String infoUrl = "http://function1994.sinaapp.com/index.php";
	
	    public BookingFragment(){

        }

        @Override
        public void onCreate(Bundle savedInstanceState){

            GetBookingListJsonInfoViaUrl getInfoViaUrl = new GetBookingListJsonInfoViaUrl(getActivity());
            try {
                getInfoViaUrl.execute(new URL(infoUrl));

            }
            catch (MalformedURLException e){
                e.printStackTrace();

            }

            super.onCreate(savedInstanceState);
        }
        
        @Override 
        public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        	return inflater.inflate(R.layout.booking_fragment,container,false);
        }

        private class GetBookingListJsonInfoViaUrl extends AsyncTask<URL,Integer,JSONArray> {
            public Context mContext;
            public GetBookingListJsonInfoViaUrl(Context context){
                mContext = context;
            }

             @Override
             protected void onPostExecute(JSONArray jsonList){

                Log.v("BookingListAdapter", "getBookingListJsonInfo request complete!");
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
                            // mJsonList = jsonArray;
                                BookingDBAdapter dbAdapter = new BookingDBAdapter(mContext);
                                dbAdapter.mDB = dbAdapter.mDBHelper.getWritableDatabase();
                                int i;
                                for(i=0;i<jsonArray.length();i++){
                                    try {
                                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                                        if( !dbAdapter.isExists(jsonObj.getString("id"))) {
                                            Long Size = dbAdapter.insert(jsonObj.getString("id"),
                                                    jsonObj.getString("caption"),
                                                    jsonObj.getString("poster"),
                                                    jsonObj.getInt("number"),
                                                    jsonObj.getString("activity_time"),
                                                    jsonObj.getString("deadline")
                                            );
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                dbAdapter.mDB.close();
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
