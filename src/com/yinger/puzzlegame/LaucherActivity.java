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
 * ��������
 * 
 * @author yinger
 * 
 */
public class LaucherActivity extends Activity {

	private static final String TAG = "LaucherActivity";
	
//	private MediaPlayer musicPlayer;// ** ���ֵĲ��� */
//	private boolean isMusicPlay;// ** ���ֲ��ű�ʶ�� */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ض����������� д��setContentView(R.layout.xxxx);֮ǰ����Ȼ����
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ȫ����ʾ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View mainView = getLayoutInflater().inflate(R.layout.activity_laucher, null);
		setContentView(mainView);
		
		GameService.initGameService(this);
//		musicPlayer = GameService.musicPlayer;
//		isMusicPlay = GameService.isMusicPlay;
//		musicPlayer = MediaPlayer.create(this, R.raw.canon);// ��������
//		musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// �������ֵ����������
//		musicPlayer.setLooping(true);// ��������ѭ������
		if (GameService.isMusicPlay)
			GameService.musicPlayer.start();
		
		// ����ʱ����һ������
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
				if (preferences.getInt("firstrun", 1) == 1) {// ����ǵ�һ�����У���ô����ʾsplash
					Log.d(TAG, "first run!");
					intent = new Intent(LaucherActivity.this, SplashActivity.class);
				} else {// ����ֱ�ӽ���������
					intent = new Intent(LaucherActivity.this, MainActivity.class);
				}
				startActivity(intent);
				// overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				LaucherActivity.this.finish();
			}
		});

	}

	//�ж���Ϸ�������ļ��Ƿ����
	private boolean isGameDataFileExist() {
		String[] fileList = fileList();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].equals(GameDataUtil.GAMEDATA_FILENAME)) {
				return true;
			}
		}
		return false;
	}
	
	/** ��Activity����Ծ��ʱ��ֹͣ���� */
	@Override
	protected void onPause() {
		if (GameService.musicPlayer.isPlaying()) {
			GameService.musicPlayer.pause();
		}
		super.onPause();
	}

	/** ��Activity�ٴδ��ڻ�Ծ״̬��ʱ�򣬿������� */
	@Override
	protected void onResume() {
		if (GameService.isMusicPlay) {
			GameService.musicPlayer.start();
		}
		super.onResume();
	}
}
