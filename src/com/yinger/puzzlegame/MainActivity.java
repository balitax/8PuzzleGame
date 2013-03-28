package com.yinger.puzzlegame;

import com.yinger.util.GameService;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ImageView ivQuickStart;
	ImageView ivGameRule;
	ImageView ivGameConfig;
	ImageView ivGameAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View mainView = getLayoutInflater().inflate(R.layout.activity_main, null);
		setContentView(mainView);
		if (GameService.isMusicPlay) {
			if (!GameService.musicPlayer.isPlaying()) {
				GameService.musicPlayer.start();
			}
		}
		ivQuickStart = (ImageView) mainView.findViewById(R.id.ivQuickStart);
		ivQuickStart.setOnClickListener(new ImageViewQuickStartListener());
		ivGameRule = (ImageView) mainView.findViewById(R.id.ivGameRule);
		ivGameRule.setOnClickListener(new ImageViewGameRuleListener());
		ivGameConfig = (ImageView) mainView.findViewById(R.id.ivGameConfig);
		ivGameConfig.setOnClickListener(new ImageViewGameConfigListener());
		ivGameAbout = (ImageView) mainView.findViewById(R.id.ivGameAbout);
		ivGameAbout.setOnClickListener(new ImageViewGameAboutListener());

	}

	// ��������ٿ�ʼ��������Ϸ������
	class ImageViewQuickStartListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// Toast tost =Toast.makeText(MainActivity.this, "QuickStart", Toast.LENGTH_SHORT);
			// tost.show();
			Intent intent = new Intent(MainActivity.this, StagesActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			// MainActivity.this.finish();//���ﲻҪ ������һ������Ҫ��������һ����Ҫ
			// overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
		}
	}

	// �������Ϸ���򡱽�����Ϸ������
	class ImageViewGameRuleListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Toast tost = Toast.makeText(MainActivity.this, "GameRule", Toast.LENGTH_SHORT);
			tost.show();
			// Intent intent = new Intent(MainActivity.this, MainActivity.class);
			// startActivity(intent);
		}
	}

	// �������Ϸ���á�������Ϸ������
	class ImageViewGameConfigListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Toast tost = Toast.makeText(MainActivity.this, "GameConfig", Toast.LENGTH_SHORT);
			tost.show();
			// Intent intent = new Intent(MainActivity.this, MainActivity.class);
			// startActivity(intent);
		}
	}

	// �����������Ϸ��������Ϸ������
	class ImageViewGameAboutListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// Toast tost = Toast.makeText(MainActivity.this, "GameAbout", Toast.LENGTH_SHORT);
			// tost.show();
			Intent intent = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
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
