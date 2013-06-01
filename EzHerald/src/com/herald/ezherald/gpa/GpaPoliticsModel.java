package com.herald.ezherald.gpa;

/**
 * @author xie
 */
public class GpaPoliticsModel {
	private static double userGpa = -1;//用户的gpa，未登陆时为-1
	public class politic{
		public String title;
		public String content;
		public String relation;
		public double requiredGpa;
		
		public politic(String title, String content, String relation,
				double requiredGpa) {
			this.title = title;
			this.content = content;
			this.relation = relation;
			this.requiredGpa = requiredGpa;
		}
		
	}
	public politic[] politics = new politic[]{
			new politic("title1","cccc",">",3.0),
			new politic("title2","ddd",">",3.0),
			new politic("title3","asddasd",">",3.0),
			new politic("title4","asss",">",3.0),
			new politic("title5","gggg",">",3.0),
			new politic("title6","hhhhh",">",3.0),
			new politic("title7","hhhhhh",">",3.0),
	};
	public static double getUserGpa() {
		return userGpa;
	}
	
	public static void setUserGpa(double userGpa) {
		GpaPoliticsModel.userGpa = userGpa;
	}
	/**
	 * @param n ，判断的项数
	 * @return Boolean
	 */
	public Boolean judge(int n){
		if(userGpa == -1 || n>politics.length){
			return false;
		}else{
			//判断用户的绩点是否满足要求
			if(politics[n].relation == ">" && userGpa >politics[n].requiredGpa)
				return true;
			if(politics[n].relation == "<" && userGpa <politics[n].requiredGpa)
				return true;
			if(politics[n].relation == ">=" && userGpa >=politics[n].requiredGpa)
				return true;
			if(politics[n].relation == "<=" && userGpa <=politics[n].requiredGpa)
				return true;
			return false;
		}
	}
}
