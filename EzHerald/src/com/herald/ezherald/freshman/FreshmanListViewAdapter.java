package com.herald.ezherald.freshman;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class FreshmanListViewAdapter implements ListAdapter {

	public FreshmanInfo data;
	private int type;
	/*
	private String[][] titles = { 
			{ "ѡ��", "ѧ��", "ƽ���ɼ�����(GPA)", "����", },
			{ "һ��ͨ", "�˶�", "����", "ʳ��", "����", "����", }, 
			{ "����", "����" },
			{ "˵�õ�API��??" }, 
	};
	private String[][] info = {
			{
				"ѡ��һ��������1�㿪�ŵġ�ѡ�ε�ʱ�������˶�����������ϵͳ��ǳ��������һ��Ҫ�ۼ��ֿ졣����һ����ѡ��ϵͳ��ֱ�ӱ�ȥѡҪ�޹�10ѧ�ֵ�ͨѡ�Ρ���Ϊ��������࣬��Ȼ��ѧ��;���ѧ�ࡣ",
				"�����ÿ��ѧ���3��ѧ�ڣ��＾��ѧ���Ƕ�ѧ��4�ܣ���һ�����ݾ��Ǿ�ѵ��Ȼ��ڶ���ѧ�ڣ���ѧ�ڣ�16���Ͽ�+2�ܿ��ԡ�",
				"���������ܵĳɼ������֣��Ӵ�һһֱ���۵����ı�ҵ���������������ֽ�ѧ��ǳ���Ҫ�� GPA���㷽����Ժϵ���в�ͬ.",
				"��Ϊ�Ļ����ʽ������Ϳ�����ѧ�������Ļ����ʽ�����Ҫ��ˢ����ˢ��8�Σ���ƪ���鱨�棬�ſ��ԴﵽҪ�󡣿�����ѧ����ÿ��ͦ��ᷢ��Ҫ�����д���ģ��������ͨ�����Ի��0.3����0.5��ѧ�֡�ÿ��ѧ�������ù�2��������ѧ��ѧ�֣�һ�����ͨ����������ѧ��������srtp��Ŀ���μӾ������ù�������ѧѧ��",
			},
			{
				"��֪ͨ����ܵ�һ��һ��ͨ��ѧУ��ĺܶ����鶼��Ҫ�����Է����ܲٴ򿨡�ҽԺ���������й��ͼ��ݽ��š��ϻ������������ԡ�ϴ�衢����ˮ��������һ��ͨһ��Ҫ���ܺã�һ��ͨ���˺�����Ҫ�������",
				"��԰÷԰�����˶�������԰�������Ա��д�Ƭ���򳡡����򳡺����򳡣���̨�»��н�����÷԰�����򳡡� \n�ܲ٣�ÿ��ѧ�ڱ������45����٣���������������ҿƵġ�",
				"1.bras:��ѧУ���õ���bras����bras�����Ǻܻ���ġ���Ҫ���������ģ�http://nic.seu.edu.cn/�������룬bras�����֣�һ����@a��ֻ���Ϲ�������20G�������/�£����ѹ��������ȫ�����ٿ����Ϲ�����վ�������ٶ���ʱ��������һ����@b�����Է��ʹ�����վ������ֻ��4G�������/�£�24Сʱ����������\n2.У԰�����У԰���ͬ����Ҫ������������վ��ͨ�Լ����˺ţ���Сʱ�Ʒѣ�0.6Ԫ/Сʱ��ÿ��23���������������24�㣩�����ٱȽϿ졣\n3.���ţ���������Ҫ��������ֻ����룬��ͬ�ײ��в�ͬʱ����һ����100Сʱ���ң�ƽ���۸���У԰������ƣ�����24Сʱ������������δ�������ֻ�����������Ǹ������ѡ��",
				"��������ʳ�÷���԰��÷԰����԰����԰���ѧ¥�����һ�¿ξͻ��źܳ��Ķ��飬���ǷǷ���һ�㲻���гԵġ�÷԰ʳ�õ�Ʒ��Ҫ��Զ�һ�㣬����Ҳ����һЩ����������ץ��֮���С�ԡ����ϻ��ṩ��ҹ��ʮһ�㡣��԰��Ҫ���о������ᣬ������ԱȽ�Զ��ż��������һ��Ҳ�ǲ����ѡ��ʳ���ﶼ�п��⡢������̲衢����ʲô�ġ���ʵ�������Ƕ����˵ĵڶ�ʳ�á������������ſڣ�����������ж��������С�ԡ�ҹ��",
				"������������Ҫ��÷԰����԰�������������ǲ���ģ����˼䣬�ж��������䣬����������ԡ����ˮ��ÿ��24Сʱ��������ˮ��ϴ��Ҫˢ������ÿ�����ᶼ�пյ���",
				"ѧ������ʱ������Ҫ�������������ѧ���������ҵ��Լ�����Ȥ������������ḻ��ѧ������������󽻼�Ȧ��������ʵ���������Ӷ�Ϊ�պ�Ĺ����������������Щѧ����֯����ø����Լ�����Ȥ������ѡ��μӣ��ḻ��ѧ���",
			},
			{ 
				"У��������ֱ����������¥У����ѧ����У��һ�������ſڣ�����ʦû�������������ѧ������ˢ���ϳ���Ʊ��4Ԫ/�ˣ���һ��ͨ�Ͽ�ȡ��", 
				"÷԰ʳ���Ա����չ����У�������ѧ¥���л��ϳ��С���԰ʳ���Ա�Ҳ��һ��С���С���ʵͬѧ���ճ�������Ʒ��ʳ��Щ���л������������㡣ˮ��ÿ�����ж��У��չ������Ա߻���һ��ˮ�����С���ӡ��÷԰����԰���С���԰����һ������������������У�ѧУ�������У�",
			},
			{ 
					"˵�õ�API��??" 
			}, 
	};
	*/
	private String[][] titles;
	private String[][] info;
	private Context context;
	private static final float LARGE = 23,SMALL=18; //�ֺ�
	private static final int PADDING = 10;
	public FreshmanListViewAdapter(int type,Context context) {
		this.type = type;
		this.context = context;
		titles = new String[4][];
		info = new String[4][];
		for(int i=0;i<4;i++){
			titles[i] = (String[]) data.getTitles().get(i).toArray();
			info[i] = (String[]) data.getContent().get(i).toArray();
		}
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles[type].length+info[type].length;
		//return data.getContent().get(type).size()+data.getTitles().get(type).size();
	}

	@Override
	public Object getItem(int id) {
		return null;
		
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public int getItemViewType(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView v = new TextView(context);
		if(position%2==0){//����
			v.setText(titles[type][position/2]);
			v.setTextSize(LARGE);
			v.setTextColor(Color.BLUE);
			v.setGravity(Gravity.CENTER);
		}else{
			v.setText(info[type][position/2]);
			v.setTextSize(SMALL);
			v.setPadding(PADDING, PADDING, PADDING, 0);//left top right bottom
		}
		return v;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
