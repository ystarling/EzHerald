package com.herald.ezherald.mainframe;

/**
 * �������ģ��ժҪ��Ϣ����
 * @author BorisHe
 *
 */
public class MainContentGridItemObj {
	private String content1 = null; //����1���ֺŽϴ�
	private String content2 = null; //����2���ֺŽ�С
	
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
