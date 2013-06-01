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
	public String[] title = new String[] {"Title"};
	public String[] content = new String[]{"content"};
	private double[] requiredGpa = new double[]{3.0};//政策所需的gpa
	private String[] relation = new String[]{">="};//政策所需的关系
	public String[] getRelation() {
		return relation;
	}
	public double[] getRequiredGpa() {
		return requiredGpa;
	}
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
