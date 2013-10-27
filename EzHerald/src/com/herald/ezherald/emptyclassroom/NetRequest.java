package com.herald.ezherald.emptyclassroom;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpException;

import com.herald.ezherald.academic.DataTypeTransition;

public class NetRequest {
	
	private int timeout;
	private String method;
	
	public NetRequest()
	{
		timeout = 5000;
		method = "GET";
	}
	
	public NetRequest setTimeout(int t)
	{
		timeout = t;
		return this;
	}
	
	public NetRequest setMethod(String method)
	{
		this.method = method;
		return this;
	}
	
	public String request(String url_str) throws IOException, HttpException
	{
		URL url = new URL(url_str);
		int response = -1;
		InputStream in = null;
		URLConnection conn;
		conn = url.openConnection();
		if (!(conn instanceof HttpURLConnection)) {
			throw new IOException("NOT AN HTTP CONNECTION");
		}
		HttpURLConnection httpConn = (HttpURLConnection) conn;
		httpConn.setAllowUserInteraction(false);
		httpConn.setInstanceFollowRedirects(true);
		httpConn.setRequestMethod(this.method);
		httpConn.setConnectTimeout(this.timeout);
		httpConn.connect();
		response = httpConn.getResponseCode();
		if (response == HttpURLConnection.HTTP_OK) {
			in = httpConn.getInputStream();
			String str = DataTypeTransition.InputStreamToString(in);
			return str;
			
		}
		else{
			throw new HttpException("«Î«Û ß∞‹");
		}
		
	}

}
