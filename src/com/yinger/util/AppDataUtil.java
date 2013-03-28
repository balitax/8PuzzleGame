package com.yinger.util;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.yinger.beans.AppData;
import com.yinger.beans.Stage;

/**
 * 应用数据处理类
 * 
 * @author yinger
 * 
 */
public class AppDataUtil {

	private static AppData appData;

	private static final String TAG = "AppDataUtil";

	// 从puzzle.xml文件中加载应用数据
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
			while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {// 还未到文档结束
				String tagName = xrp.getName();
				if (xrp.getEventType() == XmlResourceParser.START_TAG) {// 标签开始
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
				} else if (xrp.getEventType() == XmlPullParser.END_TAG) {// 标签结束
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
