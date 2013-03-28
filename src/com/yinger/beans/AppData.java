package com.yinger.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *  应用数据类
 * @author yinger
 *
 */
public class AppData {

	private String version;
	private String author;
	private String blog;
	private List<Stage> stageList = new ArrayList<Stage>();

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBlog() {
		return blog;
	}

	public void setBlog(String blog) {
		this.blog = blog;
	}

	public List<Stage> getStageList() {
		return stageList;
	}

	public void setStageList(List<Stage> stageList) {
		this.stageList = stageList;
	}

}
