package com.yinger.puzzlegame;

import com.yinger.util.GameService;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * ������Ϸ����
 * 
 * @author yinger
 * 
 */
public class AboutActivity extends Activity {

	private static final String TAG = "AboutActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ض����������� д��setContentView(R.layout.xxxx);֮ǰ����Ȼ����
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ȫ����ʾ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View mainView = getLayoutInflater().inflate(R.layout.activity_about, null);
		setContentView(mainView);

		if (GameService.isMusicPlay) {
			if (!GameService.musicPlayer.isPlaying()) {
				GameService.musicPlayer.start();
			}
		}
	}

	/** ��д���ؼ� */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// �����Ƿ��ؼ�
			AboutActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
