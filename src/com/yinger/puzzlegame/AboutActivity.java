package com.yinger.puzzlegame;

import com.yinger.util.GameService;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 关于游戏界面
 * 
 * @author yinger
 * 
 */
public class AboutActivity extends Activity {

	private static final String TAG = "AboutActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏顶部程序名称 写在setContentView(R.layout.xxxx);之前，不然报错
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View mainView = getLayoutInflater().inflate(R.layout.activity_about, null);
		setContentView(mainView);

		if (GameService.isMusicPlay) {
			if (!GameService.musicPlayer.isPlaying()) {
				GameService.musicPlayer.start();
			}
		}
	}

	/** 重写返回键 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 假如是返回键
			AboutActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
