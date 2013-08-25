package com.herald.ezherald.library;

import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;

public class LibraryActivityNews extends SherlockActivity{
	
	public void onCreate(Bundle saveInstanceSate){
		super.onCreate(saveInstanceSate);
		setContentView(R.layout.library_activity_news);
		setTitle("ͼ�������");
		
		TextView text_title=(TextView)findViewById(R.id.libr_news_title);
		text_title.setText("ͼ��ݰ칫����Ƹ�ڹ���ѧ������");
		TextView text_subtitle=(TextView)findViewById(R.id.libr_news_subtitle);
		text_subtitle.setText("����ʱ�䣺"+"2013-08-23"+"    �Ķ�����: "+"10");
		
		TextView text_content=(TextView)findViewById(R.id.libr_news_content);
		text_content.setText(get_text_content());
	}
	
	public String get_text_content(){
		String libr_str="������Ҫ��" +"ͼ��ݰ칫������Ƹ�ڹ���ѧ������һ������ҪЭ������ϰ칫�Ҵ����ճ�����������������ʱ����ĸ����������Ҫ�����£�1�����븴ӡ����ع������˽�������ִ���ͼ���������ʹ�ã���������Photoshop������һ�����������ף������Ű�������Ը��������Ҫ��ʱѧϰ�������ʹ�á����д����Ե�Э���������������ӡˢ��ҳ�ȵ���ƹ�����   2��ÿ�ܹ���ʱ�䲻����8Сʱ��ÿ�¹���ʱ�䲻����34Сʱ��3���Ȱ�ͼ��ݹ���������ͼ��ݵĸ���涨������������ϡ�����ݰ칫�ҵĸ�����ڡ���������������4��ӦƸ�߳�Ӧ��д�ڹ���ѧ�������⣨ͼ�����ҳ�ڹ���ѧ��������أ�����Ӧ׼�������������Ƽ��š����������Ʒһ�ݡ�ӦƸ�ߣ��뽫����������springrain@seu.edu.cn,��ǰ��������ͼ���A302Ͷ������������ϡ������ֹ���ڣ�9��30�գ���ϵ�ˣ��칫�� ����ʦ������ʦ��";
		return libr_str;
	}
}
