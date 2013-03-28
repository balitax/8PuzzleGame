package com.yinger.beans;

/**
 * 关卡类
 * 
 * @author yinger
 * 
 */
public class Stage {

	private int id;
	private String name;
	private float stagetime;//这个暂时没有用
	private long timelimit;
	private int footlimit;
	private String path;
	private String shotpath;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getStagetime() {
		return stagetime;
	}

	public void setStagetime(float stagetime) {
		this.stagetime = stagetime;
	}

	public long getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(long timelimit) {
		this.timelimit = timelimit;
	}

	public int getFootlimit() {
		return footlimit;
	}

	public void setFootlimit(int footlimit) {
		this.footlimit = footlimit;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getShotpath() {
		return shotpath;
	}

	public void setShotpath(String shotpath) {
		this.shotpath = shotpath;
	}

}
