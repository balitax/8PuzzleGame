package com.yinger.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.yinger.puzzlegame.R;

public class GameService {
	
	public static MediaPlayer musicPlayer;// ** 音乐的播放 */
	
	public static boolean isMusicPlay = true;
	public static boolean isSoundPlay = true;
	
	public static void initGameService(Context context){
		musicPlayer = MediaPlayer.create(context, R.raw.canon);
		musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置音乐的输出流类型
		musicPlayer.setLooping(true);// 设置音乐循环播放
	}
	
}

