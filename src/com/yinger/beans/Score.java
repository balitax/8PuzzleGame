package com.yinger.beans;

/**
 * 游戏的成绩
 * 
 * @author yinger
 * 
 */
public class Score {

	// 成绩的四个等级
	public static final int NONE = -1;// 还没有玩
	public static final int GOOD = 0;// 1
	public static final int GREAT = 1;// 2
	public static final int EXCELLENT = 2;// 3
	public static final int BEST = 3;// 徽章

	private int stageid;// 对应的stage id
	private long time = 0L;// 所用的时间
	private int foot = 0;// 所用的步数
	private int point = -1;// 得分

	public Score() {
	}

	public Score(int stageid) {
		this.stageid = stageid;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getStageid() {
		return stageid;
	}

	public void setStageid(int stageid) {
		this.stageid = stageid;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getFoot() {
		return foot;
	}

	public void setFoot(int foot) {
		this.foot = foot;
	}

	// 计算score
	public static int calculatePoint(String time, String foot, long timelimit, int footlimit) {
		System.out.println(time + "  " + foot + "  " + timelimit + "  " + footlimit);
		int score = Score.GOOD;// 默认值，但是时间和步数都超出了范围
		long floattime = time2long(time);
		int intfoot = Integer.parseInt(foot);
		if (floattime <= timelimit && intfoot <= footlimit) {// 时间 和 步数都在限制范围之内
			score = Score.BEST;
		}
		if (intfoot <= footlimit && floattime > timelimit) {// 步数少 时间超了范围 相同时间步数更少得分更高！
			score = Score.EXCELLENT;
		}
		if (intfoot > footlimit && floattime <= timelimit) {// 用时少 步数超了范围
			score = Score.GREAT;
		}
		return score;
	}

	// 将 hh:mm:ss 转换成 long 值
	public static long time2long(String time) {
		long t = 0L;
		String[] timeStrings = time.split(":");
		int len = timeStrings.length - 1;
		System.out.println("len = " + len);// 不确定是hh:mm:ss还是mm:ss
		for (int i = 0; i <= len; i++) {
			t += Math.pow(60, len - i) * Long.parseLong(timeStrings[i]);
		}
		System.out.println("time is " + time + "  = long " + t);
		return t;
	}

	// 将long 转换成  hh:mm:ss 值
	public static String long2time(long t) {
		StringBuffer time = new StringBuffer();
		long h = t / 3600;
		time.append(h == 0 ? "" : h + ":");
		int m = (int) ((t % 3600) / 60);
		time.append(m == 0 ? "00:" : m < 9 ? "0" + m + ":" : m + ":");
		int s = (int) (t - 3600 * h - 60 * m);
		time.append(s == 0 ? "00" : s < 9 ? "0" + s + ":" : s + ":");
		System.out.println("time is " + time + "  = long " + t);
		return time.toString();
	}

	// 比较老成绩和新成绩，如果新成绩更好，返回true
	public static boolean compareResult(Score oldScore, Score newScore) {
		if (newScore.point > oldScore.point) {// 新成绩得分更高
			return true;
		}
		if (newScore.foot < oldScore.foot) {// 新成绩所用的步数更少 在分数相同的情况下
			return true;
		}
		if (newScore.time < oldScore.time) {// 新成绩所用的时间更少 在步数相同的情况下
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(long2time(3600));
	}

}
