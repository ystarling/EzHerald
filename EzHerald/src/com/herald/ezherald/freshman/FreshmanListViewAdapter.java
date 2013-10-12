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
			{ "选课", "学期", "平均成绩点数(GPA)", "讲座", },
			{ "一卡通", "运动", "上网", "食堂", "宿舍", "社团", }, 
			{ "出行", "超市" },
			{ "说好的API呢??" }, 
	};
	private String[][] info = {
			{
				"选课一般是下午1点开放的。选课的时候所有人都在抢，所以系统会非常卡，大家一定要眼疾手快。尽量一进到选课系统就直接奔去选要修够10学分的通选课。分为人文社科类，自然科学类和经济学类。",
				"东大的每个学年分3个学期，秋季开学先是短学期4周，大一的内容就是军训。然后第二个学期，长学期，16周上课+2周考试。",
				"绩点是你总的成绩的体现，从大一一直积累到大四毕业。绩点对于申请各种奖学金非常重要。 GPA计算方法各院系略有不同.",
				"分为文化素质讲座，和课外研学讲座。文化素质讲座，要求刷卡，刷够8次，交篇读书报告，才可以达到要求。课外研学讲座每次挺后会发表，要求填表并写论文，论文审核通过可以获得0.3或者0.5个学分。每个学生必须拿够2个课外研学的学分，一般就是通过听课外研学讲座、做srtp项目、参加竞赛来拿够课外研学学分",
			},
			{
				"随通知书会受到一张一卡通，学校里的很多事情都需要它，吃饭、跑操打卡、医院看病、超市购物、图书馆进门、上机、报名、考试、洗澡、打热水……所以一卡通一定要保管好，一卡通的账号密码要记清楚。",
				"桃园梅园都有运动场，桃园体育场旁边有大片篮球场、手球场和排球场，看台下还有健身房。梅园有网球场。 \n跑操：每个学期必须出够45次早操，次数不够体育会挂科的。",
				"1.bras:在学校里，最常用的是bras，用bras上网是很划算的。需要到网络中心（http://nic.seu.edu.cn/）上申请，bras有两种，一个是@a，只能上国内网，20G免费流量/月（用搜狗浏览器的全网加速可以上国外网站，不过速度有时较慢）。一个是@b，可以访问国外网站，但是只有4G免费流量/月（24小时不断网）。\n2.校园宽带：校园宽带同样需要到网络中心网站开通自己的账号，按小时计费，0.6元/小时，每晚23点断网（周五周六24点），网速比较快。\n3.电信（天翼）：需要办理电信手机号码，不同套餐有不同时长，一般在100小时左右，平均价格与校园宽带相似，但是24小时不断网，对于未办理本地手机号码的新生是个不错的选择。",
				"九龙湖的食堂分桃园、梅园、橘园。桃园离教学楼最近，一下课就会排很长的队伍，但是非饭点一般不会有吃的。梅园食堂的品种要相对多一点，边上也会有一些窗口买煎饼手抓饼之类的小吃。晚上会提供宵夜到十一点。橘园主要是研究生宿舍，所以相对比较远。偶尔俩体验一下也是不错的选择。食堂里都有烤肉、面包、奶茶、饮料什么的。其实还有咱们东大人的第二食堂————东门门口，晚上这里会有东大人最爱的小吃、夜宵",
				"本科生宿舍主要是梅园和桃园。宿舍条件还是不错的，四人间，有独立卫生间，卫生间有淋浴，热水器每天24小时可以用热水（洗澡要刷卡），每个宿舍都有空调。",
				"学生课余时间活动的主要场所。在这里，大学生不但能找到自己的兴趣、体验新事物、丰富大学生活，而且能扩大交际圈、提高社会实践能力，从而为日后的工作打基础。对于这些学生组织，最好根据自己的兴趣和能力选择参加，丰富大学生活。",
			},
			{ 
				"校车：可以直接坐到四牌楼校区，学生等校车一律在西门口，在老师没有坐满的情况下学生方可刷卡上车，票价4元/人，从一卡通上扣取。", 
				"梅园食堂旁边有苏果超市，再往教学楼走有华诚超市。桃园食堂旁边也有一个小超市。其实同学们日常生活用品零食这些超市基本都可以满足。水果每个超市都有，苏果超市旁边还有一个水果超市。打印店梅园、桃园都有。桃园还有一个理发店和中行自助银行（学校卡是中行）",
			},
			{ 
					"说好的API呢??" 
			}, 
	};
	*/
	private String[][] titles;
	private String[][] info;
	private Context context;
	private static final float LARGE = 23,SMALL=18; //字号
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
		if(position%2==0){//标题
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
