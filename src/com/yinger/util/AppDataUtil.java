package com.yinger.util;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.yinger.beans.AppData;
import com.yinger.beans.Stage;

/**
 * Ӧ�����ݴ�����
 * 
 * @author yinger
 * 
 */
public class AppDataUtil {

	private static AppData appData;

	private static final String TAG = "AppDataUtil";

	// ��puzzle.xml�ļ��м���Ӧ������
	public static AppData loadAppData(XmlResourceParser xrp) {
		if (appData != null) {
			return appData;
		}
		if (xrp == null) {
			return null;
		}
		Log.d(TAG, "real load app data");
		appData = new AppData();
		Stage stage = null;
		try {
			while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {// ��δ���ĵ�����
				String tagName = xrp.getName();
				if (xrp.getEventType() == XmlResourceParser.START_TAG) {// ��ǩ��ʼ
					if (tagName.equalsIgnoreCase("version")) {
						appData.setVersion(xrp.nextText());
					} else if (tagName.equalsIgnoreCase("author")) {
						appData.setAuthor(xrp.nextText());
					} else if (tagName.equalsIgnoreCase("blog")) {
						appData.setBlog(xrp.nextText());
					} else if (tagName.equalsIgnoreCase("stage")) {
						stage = new Stage();
					} else if (tagName.equalsIgnoreCase("id")) {
						stage.setId(Integer.parseInt(xrp.nextText()));
					} else if (tagName.equalsIgnoreCase("name")) {
						stage.setName(xrp.nextText());
					} else if (tagName.equalsIgnoreCase("path")) {
						stage.setPath(xrp.nextText());
					} else if (tagName.equalsIgnoreCase("shortpath")) {
						stage.setShotpath(xrp.nextText());
					} else if (tagName.equalsIgnoreCase("timelimit")) {
						stage.setTimelimit(Long.parseLong(xrp.nextText()));
					} else if (tagName.equalsIgnoreCase("footlimit")) {
						stage.setFootlimit(Integer.parseInt(xrp.nextText()));
					}
				} else if (xrp.getEventType() == XmlPullParser.END_TAG) {// ��ǩ����
					if (tagName.equalsIgnoreCase("stage")) {
						appData.getStageList().add(stage);
					}
				}
				xrp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, "stage size=" + appData.getStageList().size());
		return appData;
	}

}
