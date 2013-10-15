package com.herald.ezherald.exercise;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * @author xie
 * 体育系人人的早操播报消息
 */
public class RenrenInfo{
	public static final boolean DEBUG = false;//TODO　just for debug,must be removed before release 
	private static final String URI = "/v2/feed/list?feedType=UPDATE_STATUS&userId=601258593"; 
	private static final String HOST = "api.renren.com";
	private static final String MAC_KEY = "36dbee8f9b4845fb8e8e3046ff6cf10a";
	private static final String MAC_TOKEN = "241511|2.eZgOrWm5Cr8XcwAIZJKYcxrY7Gp2fUGd.0.1381732793425";
	private static final String URL = "http://api.renren.com/v2/feed/list?feedType=UPDATE_STATUS&userId=601258593";
	private final int SUCCESS = 1,FAILED = 0;
	private String info;
	private String date;
	private String message;//联网读取到的信息
	private SharedPreferences pref;
	private FragmentA father;
	public Context context;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what){
			case SUCCESS:
				message = (String)msg.obj;
				onSuccess();
				break;
			case FAILED:
				onFailed();
			}
		}
	};
	/**
	 * @param activity 调用者的activity
	 * 构造时会从sharedPreference尝试读数据
	 */
	public RenrenInfo(Context context,FragmentA father){
		this.context = context;
		this.father = father;
		try {
			pref = context.getSharedPreferences("Renren", 0);
			setInfo(pref.getString("info", null));
			setDate(pref.getString("date", null));
		} catch (Exception e) {
			setInfo(null);
			setDate(null);
		}
	}
	protected void onSuccess() {
		// TODO Auto-generated method stub
		try {
			//String today = android.text.format.DateFormat.format("yyyy-m-d",new Date()).toString();
			//Date  date = new Date();
			Calendar calendar = Calendar.getInstance();
			String today = String.format("%d-%d-%d", calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
			JSONObject json = new JSONObject(message);
			JSONArray array = json.getJSONArray("response");
			setInfo("");
			for(int i=0;i<array.length();i++){
				JSONObject object = (JSONObject) array.opt(i);
				String date = object.getString("createTime");
				date = date.split(" ")[0];
				if(date.equals(today)){
					String info = object.getString("content");
					setInfo(getInfo()+info+"\n\n\n");
					setDate(date);
					save();
				}else{
					break;
				}
			}
			if(getInfo()==null||getInfo().equals("")){//没有找到今天的，显示最新一条
				String info = "\n\n今天没有跑操消息\n\n";
				setInfo(info);
				setDate(today);
				save();
			}
			father.onSuccess();
			DateFormat fmt = SimpleDateFormat.getDateTimeInstance(); 
			setDate(fmt.format(new Date()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		save();
	}
	protected void onFailed() {
		// TODO Auto-generated method stub
		//Toast.makeText(activity, "人人信息更新失败", Toast.LENGTH_SHORT).show();
		father.onFailed();
	}
	/**
	 * 更新数据
	 */
	public void update(){
		if(DEBUG){//一些测试数据
			setInfo("今天正常跑操。20 ~ 30℃。多云转晴。没起床的各位亲们赶紧起来跑操吧。");
			DateFormat fmt = SimpleDateFormat.getDateTimeInstance(); 
			setDate(fmt.format(new Date()));
			save();
			return ;
		}else{
			new Thread(){
				@Override
	    		public void run() {
					try {
						Log.w("update","updating renren");
						HttpClient client = new DefaultHttpClient();
						HttpGet get = new HttpGet(URL);
						Log.v("URL",URL);
						
						long timesnap = System.currentTimeMillis()/1000;
						String randStr = getRandString(8);
						
						StringBuffer signatureBaseStringBuffer = new StringBuffer();
						signatureBaseStringBuffer.append( String.valueOf ( timesnap ) ).append('\n');
						signatureBaseStringBuffer.append(randStr).append('\n');
						signatureBaseStringBuffer.append("GET\n");
						signatureBaseStringBuffer.append(URI).append('\n');
						signatureBaseStringBuffer.append(HOST).append('\n');
						signatureBaseStringBuffer.append("80\n");
						signatureBaseStringBuffer.append("\n");
						
						String macString = sign(MAC_KEY, signatureBaseStringBuffer.toString());
						Log.v("signature",signatureBaseStringBuffer.toString());
						Log.v("macstr",macString);
						
						StringBuffer authenBuffer = new StringBuffer();
						authenBuffer.append("MAC ");
						authenBuffer.append("id=\"").append(MAC_TOKEN).append("\",");
						authenBuffer.append("ts=\"").append(String.valueOf ( timesnap )).append("\",");
						authenBuffer.append("nonce=\"").append(randStr).append("\",");
						authenBuffer.append("mac=\"").append(macString).append("\"");
						
						Log.v("head",authenBuffer.toString());
						
						get.addHeader("Authorization", authenBuffer.toString());
						

						HttpResponse response = client.execute(get);
						if (response.getStatusLine().getStatusCode() !=  200) {
							throw new Exception("state is not 200");
						}
						String message = EntityUtils.toString(response.getEntity());
						Message msg = handler.obtainMessage(SUCCESS,
								message);
						handler.sendMessage(msg);
					} catch (Exception e) {
						
						e.printStackTrace();
						handler.obtainMessage(FAILED).sendToTarget();
					}
				}
			}.start();
		}
		
	}
	
	/**
	 * 保存数据到sharedPreference;
	 */
	public void save(){
		Editor editor = pref.edit();
		editor.putString("info", getInfo());
		editor.putString("date", getDate());
		editor.commit();
	}
	
	/**
	 * @return boolean 数据是否为空
	 */
	public boolean isSet(){
		if(info == null || date == null)
			return false;
		return true;
	}
	
	private String getRandString(int len){
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer sb = new StringBuffer();
		Random rand = new Random();
		int id;
		for(int i=0;i<len;i++){
			id = rand.nextInt(base.length());
			sb.append(base.charAt(id));
		}
		return sb.toString();
	}
	
	private String sign(String key, String signatureBaseString) {
        try {
        	SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] text = signatureBaseString.getBytes("UTF-8");
            byte[] signatureBytes = mac.doFinal(text);
            signatureBytes = Base64.encodeBase64(signatureBytes);
            String signature = new String(signatureBytes, "UTF-8");
            return signature;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException e) {
            throw new IllegalStateException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}