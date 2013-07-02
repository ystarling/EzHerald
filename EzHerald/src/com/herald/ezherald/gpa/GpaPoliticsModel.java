package com.herald.ezherald.gpa;

/**
 * @author xie
 */
public class GpaPoliticsModel {
	private static double userGpa = -1;// �û���gpa��δ��½ʱΪ-1

	public class politic {
		public politic(String title, String content, String relation,
				double requiredGpa) {
			super();
			this.title = title;
			this.content = content;
			this.relation = relation;
			this.requiredGpa = requiredGpa;
		}

		public String title;
		public String content;
		public String relation;
		public double requiredGpa;
	}

	public politic[] politics = new politic[] {
			new politic("title1", "cccc", ">", 3.0),
			new politic("title2", "ddd", ">", 3.0),
			new politic("title3", "asddasd", ">", 3.0),
			new politic("title4", "asss", ">", 3.0),
			new politic("title5", "gggg", ">", 3.0),
			new politic("title6", "hhhhh", ">", 3.0),
			new politic("title7", "hhhhhh", ">", 3.0), };

	public static double getUserGpa() {
		return userGpa;
	}

	public static void setUserGpa(double userGpa) {
		GpaPoliticsModel.userGpa = userGpa;
	}

	/**
	 * @param n
	 *            ���жϵ�����
	 * @return Boolean
	 */
	public Boolean judge(int n) {
		if (userGpa == -1 || n > politics.length) {
			return false;
		} else {
			// �ж��û��ļ����Ƿ�����Ҫ��
			if (politics[n].relation == ">"
					&& userGpa > politics[n].requiredGpa)
				return true;
			if (politics[n].relation == "<"
					&& userGpa < politics[n].requiredGpa)
				return true;
			if (politics[n].relation == ">="
					&& userGpa >= politics[n].requiredGpa)
				return true;
			if (politics[n].relation == "<="
					&& userGpa <= politics[n].requiredGpa)
				return true;
			return false;
		}
	}
}
