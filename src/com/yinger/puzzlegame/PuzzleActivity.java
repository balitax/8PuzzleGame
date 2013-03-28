package com.yinger.puzzlegame;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.yinger.beans.AppData;
import com.yinger.beans.GameData;
import com.yinger.beans.Score;
import com.yinger.beans.Stage;
import com.yinger.listeners.ButtonTouchListener;
import com.yinger.util.AppDataUtil;
import com.yinger.util.GameDataUtil;
import com.yinger.util.GameService;

public class PuzzleActivity extends Activity {

	private final String TAG = "PuzzleActivity";
	private int[] prizeIds = new int[] { R.drawable.prize1, R.drawable.prize2, R.drawable.prize3, R.drawable.prize4 };
	private int[] puzzlebgIds = new int[] { R.drawable.stage1bg, R.drawable.stage2bg, R.drawable.stage3bg,
			R.drawable.stage4bg };
	private int[] sounds = { R.raw.selected, R.raw.error, R.raw.clear, R.raw.aplause };
	private int[] musics = { R.raw.sky, R.raw.alphabets, R.raw.angrybird, R.raw.lufei };

	private int[][] numbers = new int[][] {
			{ R.drawable.task1_1_0, R.drawable.task1_1_1, R.drawable.task1_1_2, R.drawable.task1_1_3,
					R.drawable.task1_1_4, R.drawable.task1_1_5, R.drawable.task1_1_6, R.drawable.task1_1_7,
					R.drawable.task1_1_8 },
			{ R.drawable.task1_1_0, R.drawable.task1_1_1, R.drawable.task1_1_2, R.drawable.task1_1_3,
					R.drawable.task1_1_4, R.drawable.task1_1_5, R.drawable.task1_1_6, R.drawable.task1_1_7,
					R.drawable.task1_1_8 },
			{ R.drawable.task1_1_0, R.drawable.task1_1_1, R.drawable.task1_1_2, R.drawable.task1_1_3,
					R.drawable.task1_1_4, R.drawable.task1_1_5, R.drawable.task1_1_6, R.drawable.task1_1_7,
					R.drawable.task1_1_8 },
			{ R.drawable.task1_1_0, R.drawable.task1_1_1, R.drawable.task1_1_2, R.drawable.task1_1_3,
					R.drawable.task1_1_4, R.drawable.task1_1_5, R.drawable.task1_1_6, R.drawable.task1_1_7,
					R.drawable.task1_1_8 } };

	private int[] faces = new int[] { R.drawable.face1, R.drawable.face2, R.drawable.face3, R.drawable.face4,
			R.drawable.face5, R.drawable.face6, R.drawable.face7, R.drawable.face8, R.drawable.face9,
			R.drawable.face10, R.drawable.face11, R.drawable.face12, R.drawable.face13, R.drawable.face14,
			R.drawable.face15, R.drawable.face16, R.drawable.face17 };

	private static final String FINISHPATH = "123804765";// ���յ�path
	private AppData appData;// appdata
	private GameData gameData;// gamedata
	private int stage;// ��ǰ�Ĺؿ�
	private int face;// ��ǰface��listitems�е�index����0��ʼ
	private boolean isStarted = false;// �Ѿ���ʼ��
	private boolean isFinished = false;// �Ѿ������

	private Vibrator vibrator;// ** �ֻ����� */
	private MediaPlayer[] soundPlayer;// ** ��Ч�Ĳ��� */
	private boolean isSoundPlay = true;// ** ��Ч���ŵı�ʶ�� */
	private MediaPlayer musicPlayer;// ** ���ֵĲ��� */
	private boolean isMusicPlay = true;// ** ���ֲ��ű�ʶ�� */

	private View puzzleView;
	private ImageView ivBack;
	private ImageView ivReset;
	private ImageView ivControl;
	private ImageView ivConfig;
	private GridView gridgamebord;
	private Chronometer chronometer;
	private TextView tvfoot;

	private ArrayList<Map<String, Object>> gridviewItems;
	private SimpleAdapter gridviewAdapter;

	private int foot = 0;// �Ѿ��ߵĲ���
	private boolean showTool = false;// �Ƿ���ʾtool
	private Animation rotateAnimation;
	private Animation reverseAnimation;
	private Animation touchAnimation;
	private String path;// ·��
	private Random random;// �����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		puzzleView = getLayoutInflater().inflate(R.layout.activity_puzzle, null);
		// setContentView(R.layout.activity_puzzle);
		setContentView(puzzleView);
		if (GameService.musicPlayer.isPlaying()) {//��ͣԭ�еı�������
			GameService.musicPlayer.pause();
		}
		random = new Random();
		appData = AppDataUtil.loadAppData(null);
		try {
			gameData = GameDataUtil.loadGameData(openFileInput(GameDataUtil.GAMEDATA_FILENAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "app data is " + (appData == null));
		stage = getIntent().getExtras().getInt("stage");// ��0��ʼ��
		Log.d(TAG, "stage is " + (stage));

		musicPlayer = MediaPlayer.create(this, musics[stage]);// ��������
		musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// �������ֵ����������
		musicPlayer.setLooping(true);// ��������ѭ������
		if (isMusicPlay)
			musicPlayer.start();

		vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);// ����ֻ��񶯷���
		soundPlayer = new MediaPlayer[sounds.length];
		for (int i = 0; i < soundPlayer.length; i++) {// ��ʼ��������Ч
			soundPlayer[i] = MediaPlayer.create(this, sounds[i]);
			soundPlayer[i].setAudioStreamType(AudioManager.STREAM_MUSIC);// ������Ч�����������
		}

		ImageView ivPuzzleBg = (ImageView) findViewById(R.id.ivPuzzleBg);
		ivPuzzleBg.setImageResource(puzzlebgIds[stage]);

		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivBack.setOnClickListener(new ImageViewBackListener());

		ivReset = (ImageView) findViewById(R.id.ivReset);
		ivReset.setOnTouchListener(new ButtonTouchListener(this));
		ivReset.setOnClickListener(new ImageViewResetListener());

		ivControl = (ImageView) findViewById(R.id.ivControl);
		ivControl.setOnClickListener(new ImageViewControlListener());
		ivControl.setOnTouchListener(new ButtonTouchListener(this));
		// ivConfig = (ImageView) findViewById(R.id.ivConfig);
		// ivConfig.setOnClickListener(new ImageViewConfigListener());

		tvfoot = (TextView) findViewById(R.id.tvfoot);
		// Typeface face = Typeface.createFromAsset(getAssets(), "fonts/fff.ttf");// luobo.ttfʹ�ò���
		// tvfoot.setTypeface(face);

		chronometer = (Chronometer) findViewById(R.id.chronometer);
		// chronometer.setTypeface(face);

		gridgamebord = (GridView) findViewById(R.id.gridgamebord);
		initGamebord();

		rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotateconfig);
		reverseAnimation = AnimationUtils.loadAnimation(this, R.anim.reverseconfig);
		touchAnimation = AnimationUtils.loadAnimation(this, R.anim.buttontouch);

	}

	// ��ʼ����Ϸ���
	private void initGamebord() {
		gridviewItems = new ArrayList<Map<String, Object>>();
		path = appData.getStageList().get(stage).getPath();
		Log.d(TAG, path);
		try {
			for (int i = 0; i < path.length(); i++) {
				Map<String, Object> gridItem = new HashMap<String, Object>();
				int index = path.charAt(i) - '0';
				if (index == 0) {
					face = i;
					Log.d(TAG, "face=" + face);
					System.out.println("face=" + face);
					gridItem.put("image", faces[getRandomFace()]);
				} else {
					gridItem.put("image", numbers[stage][index]);
				}
				gridviewItems.add(gridItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		gridviewAdapter = new SimpleAdapter(PuzzleActivity.this, gridviewItems, R.layout.gamebordcell,
				new String[] { "image" }, new int[] { R.id.gridviewcell });
		gridgamebord.setAdapter(gridviewAdapter);

		// gridview�е�����
		gridgamebord.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!isStarted) {// ��û�п�ʼ��������������ж�Ҫ����isFinish��ǰ��
					Toast.makeText(PuzzleActivity.this, "�����ʼ��ս��!", Toast.LENGTH_SHORT).show();
					if (isSoundPlay) {
						soundPlayer[0].start();// selected
					}
					return;
				}
				if (isFinished) {// ��������֮��ص�������棬Ҫ�������Ҫreset�����ʱ�����Ҫ�ȵ����ʼ�����Լ�����
					Toast.makeText(PuzzleActivity.this, "��Ϸ�Ѿ������ˣ���������������!", Toast.LENGTH_SHORT).show();
					if (isSoundPlay) {
						soundPlayer[0].start();// selected
					}
					return;
				}
				if (!changePosition(position)) {// �����ж��Ƿ�Ҫ����λ��
					Toast.makeText(PuzzleActivity.this, "���������ƶ�Ӵ!", Toast.LENGTH_SHORT).show();
					if (isSoundPlay) {
						soundPlayer[1].start();// error
					}
					return;// ����Ҫ����λ�ã�ֱ�ӷ���
				}
				// ��Ҫ����λ��
				if (isSoundPlay) {
					soundPlayer[0].start();// selected
				}
				Collections.swap(gridviewItems, position, face);
				changePath(position, face);
				face = position;
				foot++;
				tvfoot.setText(foot + "");
				if (isFinished = validateFinish()) {// Ȼ���ж��Ƿ������
					// isStarted = false;// ע����һ�䣡���ﲻ�����߼���ע�͵�
					chronometer.stop();
					// Toast.makeText(PuzzleActivity.this, "Finish!", Toast.LENGTH_SHORT).show();//����ʾ��ʾ��
					Stage s = appData.getStageList().get(stage);
					int score = Score.calculatePoint(chronometer.getText().toString(), tvfoot.getText().toString(),
							s.getTimelimit(), s.getFootlimit());
					showResultWindow(score);
					if (isSoundPlay) {
						soundPlayer[3].start();// aplause
					}
					saveResult(chronometer.getText().toString(), tvfoot.getText().toString(), score);
					vibrator.vibrate(1000);//��һ����
				}
				// û����ɾ�Ҫ���½��棬Ȼ�����������Ϸ
				gridviewItems.get(face).put("image", faces[getRandomFace()]);// �����޸�һ��face
				gridviewAdapter = new SimpleAdapter(PuzzleActivity.this, gridviewItems, R.layout.gamebordcell,
						new String[] { "image" }, new int[] { R.id.gridviewcell });
				gridgamebord.setAdapter(gridviewAdapter);
				gridgamebord.invalidate();
			}
		});
	}

	// ����ɹ���ɱ��ؿ���Ҫ���������Ϸ�����ݣ�
	protected void saveResult(String time, String foot, int score) {
		System.out.println(stage + " current is " + gameData.getCurrntStage());
		if (stage == gameData.getCurrntStage()) {// ע��stage�Ǵ�0��ʼ����ģ� currentstage Ҳ�Ǵ�0��ʼ�����
			gameData.setCurrntStage(stage + 1);// �������˼���ǹ��˵�ǰ��һ�أ����ɽ�����һ��
		}
		System.out.println("now current stage is " + gameData.getCurrntStage());
		// TODO:���ﻹҪ�Ƚ���ǰ�ĳɼ���ֻ������õĳɼ�
		Score oldScore = gameData.getScoreList().get(stage);
		Score newScore = new Score(stage);
		newScore.setPoint(score);
		newScore.setFoot(Integer.parseInt(foot));
		newScore.setTime(Score.time2long(time));
		if (Score.compareResult(oldScore, newScore)) {
			System.out.println("new score");
			gameData.getScoreList().set(stage, newScore);
			try {
				// ����appdata��null����gamedata��Ϊnull������������д�����ļ�
				GameDataUtil
						.writeGameData(openFileOutput(GameDataUtil.GAMEDATA_FILENAME, MODE_PRIVATE), null, gameData);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	// ��ʾ�ɼ���popupwindow
	protected void showResultWindow(int point) {
		View viewGroup = (View) getLayoutInflater().inflate(R.layout.popwindow, null);
		final PopupWindow window = new PopupWindow(viewGroup, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setFocusable(true);
		window.setOutsideTouchable(false);// ���������� ���PopuWindow���沢����ر�PopuWindow
		// window.setOutsideTouchable(true);
		ImageView ivOk = (ImageView) viewGroup.findViewById(R.id.ivOk);

		ivOk.setOnClickListener(new View.OnClickListener() {// ��ȷ����ť���һ���ر�popupwindow�ļ�����
			public void onClick(View v) {
				window.dismiss();// �����˳�popupwindow��������ܹ�ֱ������ѡ�ؽ���
			}
		});
		TextView tvTime = (TextView) viewGroup.findViewById(R.id.tvResultTime);
		tvTime.setText(chronometer.getText());

		TextView tvFoot = (TextView) viewGroup.findViewById(R.id.tvResultFoot);
		tvFoot.setText(tvfoot.getText());

		TableRow trResult = (TableRow) viewGroup.findViewById(R.id.trResult);
		ViewGroup resultViewGroup = (ViewGroup) viewGroup.findViewById(R.id.resultViewGroup);

		if (point == Score.BEST) {
			trResult.setVisibility(View.GONE);
			ivOk.setImageResource(prizeIds[stage]);// ͨ������õ�����ͼƬ
			ivOk.setPadding(40, 0, 0, 0);// Ϊ����ʾ�����ۣ�Ҫ����padding
		} else if (point == Score.GOOD) {// 1
			ImageView ivstar1 = new ImageView(PuzzleActivity.this);
			ivstar1.setImageResource(R.drawable.smallstar);
			resultViewGroup.addView(ivstar1);
		} else if (point == Score.GREAT) {// 2
			ImageView ivstar1 = new ImageView(PuzzleActivity.this);
			ivstar1.setImageResource(R.drawable.smallstar);
			resultViewGroup.addView(ivstar1);
			ImageView ivstar2 = new ImageView(PuzzleActivity.this);
			ivstar2.setImageResource(R.drawable.smallstar);
			ivstar2.setPadding(10, 0, 0, 0);
			resultViewGroup.addView(ivstar2);
		} else if (point == Score.EXCELLENT) {// 3
			ImageView ivstar1 = new ImageView(PuzzleActivity.this);
			ivstar1.setImageResource(R.drawable.smallstar);
			resultViewGroup.addView(ivstar1);
			ImageView ivstar2 = new ImageView(PuzzleActivity.this);
			ivstar2.setImageResource(R.drawable.smallstar);
			ivstar2.setPadding(10, 0, 0, 0);
			resultViewGroup.addView(ivstar2);
			ImageView ivstar3 = new ImageView(PuzzleActivity.this);
			ivstar3.setImageResource(R.drawable.smallstar);
			ivstar3.setPadding(10, 0, 0, 0);
			resultViewGroup.addView(ivstar3);
		}

		// WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// int x = windowManager.getDefaultDisplay().getWidth() / 2 - window.getWidth() / 2;
		// int y = windowManager.getDefaultDisplay().getHeight() / 2 - window.getHeight() / 2;
		// System.out.println(x + "   " + y);
		// window.showAsDropDown(puzzleView, x, 0);//����д�ᱨ��
		window.showAtLocation(puzzleView, Gravity.CENTER, 0, 40);// ��仰����Ҫ��һ��Ҫ������ʾ��λ��
	}

	// �հ׳���ʾ��ͼƬ�������һ��face
	private int getRandomFace() {
		random = new Random();
		return random.nextInt(faces.length);
	}

	// ����Ƿ������
	protected boolean validateFinish() {
		if (path.equals(FINISHPATH)) {
			return true;
		}
		return false;
	}

	// �ж��Ƿ�Ҫ����λ��
	protected boolean changePosition(int position) {
		// ���Ȼ�Ҫ�ж��Ƿ���Խ��н���
		// ��ʱû���뵽�õİ취
		boolean changeFlag = false;
		Log.d(TAG, "position=" + position + "  face=" + face);
		if (face == 0 && (position == 1 || position == 3)) {
			changeFlag = true;
		} else if (face == 1 && (position == 0 || position == 2 || position == 4)) {
			changeFlag = true;
		} else if (face == 2 && (position == 1 || position == 5)) {
			changeFlag = true;
		} else if (face == 3 && (position == 0 || position == 4 || position == 6)) {
			changeFlag = true;
		} else if (face == 4 && (position == 1 || position == 3 || position == 5 || position == 7)) {
			changeFlag = true;
		} else if (face == 5 && (position == 2 || position == 4 || position == 8)) {
			changeFlag = true;
		} else if (face == 6 && (position == 3 || position == 7)) {
			changeFlag = true;
		} else if (face == 7 && (position == 4 || position == 6 || position == 8)) {
			changeFlag = true;
		} else if (face == 8 && (position == 5 || position == 7)) {
			changeFlag = true;
		}
		return changeFlag;
	}

	// �ı��ַ���path
	private void changePath(int position, int face2) {
		char[] chars = path.toCharArray();
		char[] newChars = new char[chars.length];
		for (int i = 0; i < chars.length; i++) {
			if (i == position) {
				newChars[i] = chars[face2];
			} else if (i == face2) {
				newChars[i] = chars[position];
			} else {
				newChars[i] = chars[i];
			}
		}
		path = new String(newChars);
	}

	// ��� Back ͼ�꣬����ѡ�ؽ���
	class ImageViewBackListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// ����֮ǰҪ����һЩ�������������
			Intent intent = new Intent(PuzzleActivity.this, StagesActivity.class);
			startActivity(intent);
			PuzzleActivity.this.finish();// �������activity
		}
	}

	// ��� Config ͼ�꣬������Ϸ��ҳ
	class ImageViewConfigListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (!showTool) {
				ivConfig.startAnimation(rotateAnimation);
				showTool = true;
			} else {
				ivConfig.startAnimation(reverseAnimation);
				showTool = false;
			}
			musicPlayer.stop();
		}
	}

	// ��� control ͼ�꣬������Ϸ�Ŀ�ʼ��ͣ
	class ImageViewControlListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			// view.startAnimation(touchAnimation);
			foot = 0;
			isStarted = true;
			isFinished = false;// һ��Ҫ���������
			System.out.println(SystemClock.elapsedRealtime());// 642498697
			chronometer.setBase(SystemClock.elapsedRealtime());// �ڲ����������һЩ���֣��������̫Сû���κ�Ӱ��
			// chronometer.setBase(6);
			chronometer.start();
			ivControl.setVisibility(View.GONE);// һ����ʼ�㲻����ֹͣ������reset������Ϸ�������߷�����һ������
		}
	}

	// ��� reset ͼ�꣬��Ϸ���¿�ʼ
	class ImageViewResetListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			// view.startAnimation(touchAnimation);
			foot = 0;
			tvfoot.setText(foot + "");
			chronometer.stop();
			chronometer.setBase(SystemClock.elapsedRealtime());// �����������֮��ʱ����ȷ������Ϊ00:00
			ivControl.setVisibility(View.VISIBLE);
			isStarted = false;
			isFinished = false;
			initGamebord();
		}
	}

	/** ��Activity����Ծ��ʱ��ֹͣ���� */
	@Override
	protected void onPause() {
		if (musicPlayer.isPlaying()) {
			musicPlayer.pause();
		}
		super.onPause();
	}

	/** ��Activity�ٴδ��ڻ�Ծ״̬��ʱ�򣬿������� */
	@Override
	protected void onResume() {
		if (isMusicPlay) {
			musicPlayer.start();
		}
		super.onResume();
	}

	/** ����ʱ����������� */
	@Override
	protected void onDestroy() {
		for (MediaPlayer p : soundPlayer) {
			if (p.isPlaying()) {
				p.stop();
			}
			p.release();
		}
		musicPlayer.stop();
		musicPlayer.release();
//		if (GameService.isMusicPlay) {
//			GameService.musicPlayer.start();
//		}
		super.onDestroy();
	}

}
