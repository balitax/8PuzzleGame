package com.yinger.listeners;

import com.yinger.puzzlegame.R;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ButtonTouchListener implements OnTouchListener {

	private Context context;

	public ButtonTouchListener(Context context) {
		this.context = context;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {//手指按下去的时候触发，起来时不触发
			Animation animation = AnimationUtils.loadAnimation(context, R.anim.buttontouch);
			view.startAnimation(animation);
		}
		return false;//这里不能返回true，否则onclick事件就不会触发了
	}

}
