package com.yinger.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.yinger.puzzlegame.R;

public class GameService {
	
	public static MediaPlayer musicPlayer;// ** ���ֵĲ��� */
	
	public static boolean isMusicPlay = true;
	public static boolean isSoundPlay = true;
	
	public static void initGameService(Context context){
		musicPlayer = MediaPlayer.create(context, R.raw.canon);
		musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// �������ֵ����������
		musicPlayer.setLooping(true);// ��������ѭ������
	}
	
}

