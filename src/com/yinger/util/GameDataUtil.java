package com.yinger.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

import com.yinger.beans.AppData;
import com.yinger.beans.GameData;
import com.yinger.beans.Score;

/**
 * ��Ϸ���ݴ�����
 * 
 * @author Administrator
 * 
 */
public class GameDataUtil {

	private static final String TAG = "GameDataUtil";
	public static final String GAMEDATA_FILENAME = "gamedata.xml";

	private static GameData gameData;

	// д��gamedata
	public static void writeGameData(FileOutputStream fos, AppData appdata, GameData gameData) {
		Log.d(TAG, "write game data");
		if (gameData == null) {// ��˵����û��gamedata�����ݴ��ڣ������ǵ�һ�δ�������ļ�
			Log.d(TAG, "gamedata null");
			gameData = new GameData();
			for (int i = 0; i < appdata.getStageList().size(); i++) {// ����appdata��ָ����stage��Ŀ��������ͬ������score
				gameData.getScoreList().add(new Score(appdata.getStageList().get(i).getId()));
			}
		}
		// ����gamedata֮�󣬽���������д�뵽�ļ���
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(fos, "utf-8");
			serializer.startTag(null, "gamedata");

			serializer.startTag(null, "currentStage");
			serializer.text(gameData.getCurrntStage() + "");
			serializer.endTag(null, "currentStage");
			for (Score score : gameData.getScoreList()) {
				serializer.startTag(null, "score");

				serializer.startTag(null, "stageid");
				serializer.text(score.getStageid() + "");
				serializer.endTag(null, "stageid");

				serializer.startTag(null, "point");
				serializer.text(score.getPoint() + "");
				serializer.endTag(null, "point");

				serializer.startTag(null, "time");
				serializer.text(score.getTime() + "");
				serializer.endTag(null, "time");

				serializer.startTag(null, "foot");
				serializer.text(score.getFoot() + "");
				serializer.endTag(null, "foot");

				serializer.endTag(null, "score");
			}
			serializer.endTag(null, "gamedata");
			serializer.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ������Ϸ����
	public static GameData loadGameData(FileInputStream fis) {
//		if (gameData != null) {//gamedata��appdata��ͬ��gamedata���ǻ�仯������ÿ�ζ�Ҫ���м��أ�
//			return gameData;
//		}
//		if (fis == null) {
//			return null;
//		}
		Log.d(TAG, "real load game data");
		gameData = new GameData();
		Score score = null;
		try {
			XmlPullParser xmlParser = XmlPullParserFactory.newInstance().newPullParser();
			xmlParser.setInput(fis, "utf-8");
			while (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {
				String tagName = xmlParser.getName();
				if (xmlParser.getEventType() == XmlPullParser.START_TAG) {
					if (tagName.equals("currentStage")) {
						gameData.setCurrntStage(Integer.parseInt(xmlParser.nextText()));
					} else if (tagName.equals("score")) {
						score = new Score();
					} else if (tagName.equals("stageid")) {
						score.setStageid(Integer.parseInt(xmlParser.nextText()));
					} else if (tagName.equals("point")) {
						score.setPoint(Integer.parseInt(xmlParser.nextText()));
					} else if (tagName.equals("time")) {
						score.setTime(Long.parseLong(xmlParser.nextText()));
					} else if (tagName.equals("foot")) {
						score.setFoot(Integer.parseInt(xmlParser.nextText()));
					}
				} else if (xmlParser.getEventType() == XmlPullParser.END_TAG) {
					gameData.getScoreList().add(score);
				}
				xmlParser.next();
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gameData;
	}

}
