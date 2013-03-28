package com.yinger.puzzlegame;

import java.util.ArrayList;

import com.yinger.listeners.ButtonTouchListener;
import com.yinger.util.GameService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;

/**
 * 第一次启动时出现的滑动效果
 * 
 * @author yinger
 * 
 */
public class SplashActivity extends Activity {

	ViewPager viewPager;
	ArrayList<View> list;
	ImageView imageView;
	ImageView[] imageViews;
	ImageView ivEnter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = getLayoutInflater();
		list = new ArrayList<View>();
		list.add(inflater.inflate(R.layout.lauch1, null));
		list.add(inflater.inflate(R.layout.lauch2, null));
		View view3 = inflater.inflate(R.layout.lauch3, null);
		list.add(view3);

		ivEnter = (ImageView) view3.findViewById(R.id.ivEnter);
		ivEnter.setOnTouchListener(new ButtonTouchListener(this));
		ivEnter.setOnClickListener(new ImageViewEnterListener());
		imageViews = new ImageView[list.size()];
		ViewGroup main = (ViewGroup) inflater.inflate(R.layout.activity_splash, null);
		ViewGroup group = (ViewGroup) main.findViewById(R.id.viewGroup);// group是R.layou.main中的负责包裹小圆点的LinearLayout.
		viewPager = (ViewPager) main.findViewById(R.id.viewPager);

		for (int i = 0; i < list.size(); i++) {
			imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(10, 10));
			imageView.setPadding(30, 0, 30, 0);
			imageViews[i] = imageView;
			if (i == 0) {// 默认进入程序后第一张图片被选中
				imageViews[i].setBackgroundResource(R.drawable.guide_dot_white);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.guide_dot_black);
			}
			group.addView(imageView);
		}
		setContentView(main);
		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setOnPageChangeListener(new MyPagerListener());

		if (GameService.isMusicPlay) {
			if (!GameService.musicPlayer.isPlaying()) {
				GameService.musicPlayer.start();
			}
		}
	}

	// 点击“马上体验”进入游戏主界面
	class ImageViewEnterListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			SplashActivity.this.finish();
		}
	}

	// viewPager需要的adapter
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(list.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	// 监听viewpager的pagechange事件来控制界面下方的小圆点的显示
	class MyPagerListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.guide_dot_black);
				} else {
					imageViews[arg0].setBackgroundResource(R.drawable.guide_dot_white);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// destroy之前要将firstrun置为0
		SharedPreferences.Editor editor = getSharedPreferences("8puzzle", MODE_PRIVATE).edit();
		editor.putInt("firstrun", 0);
		editor.commit();
		super.onDestroy();
	}

	/** 当Activity不活跃的时候，停止音乐 */
	@Override
	protected void onPause() {
//		if (GameService.musicPlayer.isPlaying()) {
//			GameService.musicPlayer.pause();
//		}
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
