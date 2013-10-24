package com.herald.ezherald.academic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class DataRequester {
	
	public static String request(String u)
	{
		List<JwcInfo> jwcList = new ArrayList<JwcInfo>();
		InputStream in = null;
		int response = -1;
		
		try {
			URL url = new URL(u);
			URLConnection conn;
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
					return str;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
