package com.yinger.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏数据类
 * 
 * @author Administrator
 * 
 */
public class GameData {

	private int currntStage = 0;
	private List<Score> scoreList = new ArrayList<Score>();

	public int getCurrntStage() {
		return currntStage;
	}

	public void setCurrntStage(int currntStage) {
		this.currntStage = currntStage;
	}

	public List<Score> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<Score> scoreList) {
		this.scoreList = scoreList;
	}

}
