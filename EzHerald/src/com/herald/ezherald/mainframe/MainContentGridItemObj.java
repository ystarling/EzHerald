package com.herald.ezherald.mainframe;

/**
 * �������ģ��ժҪ��Ϣ����
 * @author BorisHe
 *
 */
public class MainContentGridItemObj {
	private String content1; //����1���ֺŽϴ�
	private String content2; //����2���ֺŽ�С
	
	
	public String getContent1() {
		return content1;
	}
	public void setContent1(String content1) {
		this.content1 = content1;
	}
	public String getContent2() {
		return content2;
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
