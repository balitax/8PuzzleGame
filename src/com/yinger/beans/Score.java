package com.yinger.beans;

/**
 * ��Ϸ�ĳɼ�
 * 
 * @author yinger
 * 
 */
public class Score {

	// �ɼ����ĸ��ȼ�
	public static final int NONE = -1;// ��û����
	public static final int GOOD = 0;// 1
	public static final int GREAT = 1;// 2
	public static final int EXCELLENT = 2;// 3
	public static final int BEST = 3;// ����

	private int stageid;// ��Ӧ��stage id
	private long time = 0L;// ���õ�ʱ��
	private int foot = 0;// ���õĲ���
	private int point = -1;// �÷�

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

	// ����score
	public static int calculatePoint(String time, String foot, long timelimit, int footlimit) {
		System.out.println(time + "  " + foot + "  " + timelimit + "  " + footlimit);
		int score = Score.GOOD;// Ĭ��ֵ������ʱ��Ͳ����������˷�Χ
		long floattime = time2long(time);
		int intfoot = Integer.parseInt(foot);
		if (floattime <= timelimit && intfoot <= footlimit) {// ʱ�� �� �����������Ʒ�Χ֮��
			score = Score.BEST;
		}
		if (intfoot <= footlimit && floattime > timelimit) {// ������ ʱ�䳬�˷�Χ ��ͬʱ�䲽�����ٵ÷ָ��ߣ�
			score = Score.EXCELLENT;
		}
		if (intfoot > footlimit && floattime <= timelimit) {// ��ʱ�� �������˷�Χ
			score = Score.GREAT;
		}
		return score;
	}

	// �� hh:mm:ss ת���� long ֵ
	public static long time2long(String time) {
		long t = 0L;
		String[] timeStrings = time.split(":");
		int len = timeStrings.length - 1;
		System.out.println("len = " + len);// ��ȷ����hh:mm:ss����mm:ss
		for (int i = 0; i <= len; i++) {
			t += Math.pow(60, len - i) * Long.parseLong(timeStrings[i]);
		}
		System.out.println("time is " + time + "  = long " + t);
		return t;
	}

	// ��long ת����  hh:mm:ss ֵ
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

	// �Ƚ��ϳɼ����³ɼ�������³ɼ����ã�����true
	public static boolean compareResult(Score oldScore, Score newScore) {
		if (newScore.point > oldScore.point) {// �³ɼ��÷ָ���
			return true;
		}
		if (newScore.foot < oldScore.foot) {// �³ɼ����õĲ������� �ڷ�����ͬ�������
			return true;
		}
		if (newScore.time < oldScore.time) {// �³ɼ����õ�ʱ����� �ڲ�����ͬ�������
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(long2time(3600));
	}

}
