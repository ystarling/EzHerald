package com.herald.ezherald.gpa;

/**
 * @author xie
 */
public class GpaPoliticsModel {
	private static double userGpa = -1;//用户的gpa，未登陆时为-1
	public static double getUserGpa() {
		return userGpa;
	}
	public static void setUserGpa(double userGpa) {
		GpaPoliticsModel.userGpa = userGpa;
	}
	public final String[] title = new String[] {"Title1","Title2"};
	public final String[][] content = 
			new String[][]{
				{"content1"},
				{"Content2Content2Content2Content2Content2Content2Content2Content2Content2Content2Content2Content2"},
			};
	private final double[] requiredGpa = new double[]{3.0,3.0};//政策所需的gpa
	private final String[] relation = new String[]{">=",">="};//政策所需的关系
	/**
	 * @param n ，判断的项数
	 * @return Boolean
	 */
	public Boolean judge(int n){
		if(userGpa == -1 || n>relation.length){
			return false;
		}else{
			//判断用户的绩点是否满足要求
			if(relation[n] == ">" && userGpa >requiredGpa[n])
				return true;
			if(relation[n] == "<" && userGpa<requiredGpa[n])
				return true;
			if(relation[n] == ">=" && userGpa>=requiredGpa[n])
				return true;
			if(relation[n] == "<=" && userGpa<=requiredGpa[n])
				return true;
			return false;
		}
	}
}
