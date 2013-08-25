package com.herald.ezherald.library;

import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.herald.ezherald.R;

public class LibraryActivityNews extends SherlockActivity{
	
	public void onCreate(Bundle saveInstanceSate){
		super.onCreate(saveInstanceSate);
		setContentView(R.layout.library_activity_news);
		setTitle("图书馆新闻");
		
		TextView text_title=(TextView)findViewById(R.id.libr_news_title);
		text_title.setText("图书馆办公室招聘勤工助学本科生");
		TextView text_subtitle=(TextView)findViewById(R.id.libr_news_subtitle);
		text_subtitle.setText("发布时间："+"2013-08-23"+"    阅读次数: "+"10");
		
		TextView text_content=(TextView)findViewById(R.id.libr_news_content);
		text_content.setText(get_text_content());
	}
	
	public String get_text_content(){
		String libr_str="因工作需要，" +"图书馆办公室现招聘勤工助学本科生一名，主要协助、配合办公室处理日常行政工作，参与临时分配的各项工作。具体要求如下：1、参与复印室相关工作。了解各种文字处理、图像处理软件的使用，熟练掌握Photoshop，具有一定的美术功底，具有排版能力。愿意因工作需要随时学习新软件的使用。能有创造性的协助完成宣传海报、印刷单页等的设计工作。   2、每周工作时间不少于8小时，每月工作时间不超过34小时。3、热爱图书馆工作，遵守图书馆的各项规定。乐于主动配合、参与馆办公室的各项对内、对外宣传工作。4、应聘者除应填写勤工助学申请表格外（图书馆主页勤工助学版面可下载），还应准备简历、自我推荐信、个人设计作品一份。应聘者，请将简历发送至springrain@seu.edu.cn,或前往李文正图书馆A302投递所需各项资料。申请截止日期：9月30日，联系人：办公室 范老师、杨老师。";
		return libr_str;
	}
}
