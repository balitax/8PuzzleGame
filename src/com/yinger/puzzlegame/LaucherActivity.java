package com.yinger.puzzlegame;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yinger.beans.AppData;
import com.yinger.util.AppDataUtil;
import com.yinger.util.GameDataUtil;
import com.yinger.util.GameService;

/**
 * 启动界面
 * 
 * @author yinger
 * 
 */
public class LaucherActivity extends Activity {

	private static final String TAG = "LaucherActivity";
	
//	private MediaPlayer musicPlayer;// ** 音乐的播放 */
//	private boolean isMusicPlay;// ** 音乐播放标识符 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏顶部程序名称 写在setContentView(R.layout.xxxx);之前，不然报错
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View mainView = getLayoutInflater().inflate(R.layout.activity_laucher, null);
		setContentView(mainView);
		
		GameService.initGameService(this);
//		musicPlayer = GameService.musicPlayer;
//		isMusicPlay = GameService.isMusicPlay;
//		musicPlayer = MediaPlayer.create(this, R.raw.canon);// 播放音乐
//		musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置音乐的输出流类型
//		musicPlayer.setLooping(true);// 设置音乐循环播放
		if (GameService.isMusicPlay)
			GameService.musicPlayer.start();
		
		// 进入时加载一次数据
		Log.d(TAG, "load app data");
		AppData appdata = AppDataUtil.loadAppData(getResources().getXml(R.xml.puzzle));
		try {
			if (!isGameDataFileExist()) {
				Log.d(TAG, "write game data");
				GameDataUtil.writeGameData(openFileOutput(GameDataUtil.GAMEDATA_FILENAME, MODE_PRIVATE), appdata, null);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "game data file exist");
		final SharedPreferences preferences = getSharedPreferences("8puzzle", MODE_PRIVATE);
		mainView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent;
				if (preferences.getInt("firstrun", 1) == 1) {// 如果是第一次运行，那么就显示splash
					Log.d(TAG, "first run!");
					intent = new Intent(LaucherActivity.this, SplashActivity.class);
				} else {// 否则直接进入主界面
					intent = new Intent(LaucherActivity.this, MainActivity.class);
				}
				startActivity(intent);
				// overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				LaucherActivity.this.finish();
			}
		});

	}

	//判断游戏的数据文件是否存在
	private boolean isGameDataFileExist() {
		String[] fileList = fileList();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].equals(GameDataUtil.GAMEDATA_FILENAME)) {
				return true;
			}
		}
		return false;
	}
	
	/** 当Activity不活跃的时候，停止音乐 */
	@Override
	protected void onPause() {
		if (GameService.musicPlayer.isPlaying()) {
			GameService.musicPlayer.pause();
		}
		super.onPause();
	}

	/** 当Activity再次处于活跃状态的时候，开启音乐 */
	@Override
	protected void onResume() {
		if (GameService.isMusicPlay) {
			GameService.musicPlayer.start();
		}
		super.onResume();
	}
}
