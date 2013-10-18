package com.herald.ezherald.mainframe;

/**
 * 主界面各模块摘要信息的类
 * @author BorisHe
 *
 */
public class MainContentGridItemObj {
	private String content1 = null; //内容1，字号较大
	private String content2 = null; //内容2，字号较小
	
	public String getContent1() {
		if(content1 != null)
			return content1;
		else
			return "";
	}
	public void setContent1(String content1) {
		this.content1 = content1;
	}
	public String getContent2() {
		if(content2 != null)
			return content2;
		else
			return "";
	}
	public void setContent2(String content2) {
		this.content2 = content2;
	}
	
	public boolean hasValidInfo()
	{
		if(content1 == null || content2 == null)
			return false;
		return true;
	}
}
