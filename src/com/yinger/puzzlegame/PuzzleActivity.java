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

	private static final String FINISHPATH = "123804765";// 最终的path
	private AppData appData;// appdata
	private GameData gameData;// gamedata
	private int stage;// 当前的关卡
	private int face;// 当前face在listitems中的index，从0开始
	private boolean isStarted = false;// 已经开始了
	private boolean isFinished = false;// 已经完成了

	private Vibrator vibrator;// ** 手机振动器 */
	private MediaPlayer[] soundPlayer;// ** 音效的播放 */
	private boolean isSoundPlay = true;// ** 音效播放的标识符 */
	private MediaPlayer musicPlayer;// ** 音乐的播放 */
	private boolean isMusicPlay = true;// ** 音乐播放标识符 */

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

	private int foot = 0;// 已经走的步数
	private boolean showTool = false;// 是否显示tool
	private Animation rotateAnimation;
	private Animation reverseAnimation;
	private Animation touchAnimation;
	private String path;// 路径
	private Random random;// 随机数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		puzzleView = getLayoutInflater().inflate(R.layout.activity_puzzle, null);
		// setContentView(R.layout.activity_puzzle);
		setContentView(puzzleView);
		if (GameService.musicPlayer.isPlaying()) {//暂停原有的背景音乐
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
		stage = getIntent().getExtras().getInt("stage");// 从0开始的
		Log.d(TAG, "stage is " + (stage));

		musicPlayer = MediaPlayer.create(this, musics[stage]);// 播放音乐
		musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置音乐的输出流类型
		musicPlayer.setLooping(true);// 设置音乐循环播放
		if (isMusicPlay)
			musicPlayer.start();

		vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);// 获得手机振动服务
		soundPlayer = new MediaPlayer[sounds.length];
		for (int i = 0; i < soundPlayer.length; i++) {// 初始化三个音效
			soundPlayer[i] = MediaPlayer.create(this, sounds[i]);
			soundPlayer[i].setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置音效的输出流类型
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
		// Typeface face = Typeface.createFromAsset(getAssets(), "fonts/fff.ttf");// luobo.ttf使用不了
		// tvfoot.setTypeface(face);

		chronometer = (Chronometer) findViewById(R.id.chronometer);
		// chronometer.setTypeface(face);

		gridgamebord = (GridView) findViewById(R.id.gridgamebord);
		initGamebord();

		rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotateconfig);
		reverseAnimation = AnimationUtils.loadAnimation(this, R.anim.reverseconfig);
		touchAnimation = AnimationUtils.loadAnimation(this, R.anim.buttontouch);

	}

	// 初始化游戏面板
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

		// gridview中的项被点击
		gridgamebord.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!isStarted) {// 还没有开始不处理，而且这个判断要放在isFinish的前面
					Toast.makeText(PuzzleActivity.this, "点击开始挑战吧!", Toast.LENGTH_SHORT).show();
					if (isSoundPlay) {
						soundPlayer[0].start();// selected
					}
					return;
				}
				if (isFinished) {// 如果完成了之后回到这个界面，要重新玩就要reset，这个时候必须要先点击开始还可以继续！
					Toast.makeText(PuzzleActivity.this, "游戏已经结束了，点击重置再来玩吧!", Toast.LENGTH_SHORT).show();
					if (isSoundPlay) {
						soundPlayer[0].start();// selected
					}
					return;
				}
				if (!changePosition(position)) {// 首先判断是否要交换位置
					Toast.makeText(PuzzleActivity.this, "它不可以移动哟!", Toast.LENGTH_SHORT).show();
					if (isSoundPlay) {
						soundPlayer[1].start();// error
					}
					return;// 不需要交换位置，直接返回
				}
				// 需要交换位置
				if (isSoundPlay) {
					soundPlayer[0].start();// selected
				}
				Collections.swap(gridviewItems, position, face);
				changePath(position, face);
				face = position;
				foot++;
				tvfoot.setText(foot + "");
				if (isFinished = validateFinish()) {// 然后判断是否完成了
					// isStarted = false;// 注意这一句！这里不符合逻辑，注释掉
					chronometer.stop();
					// Toast.makeText(PuzzleActivity.this, "Finish!", Toast.LENGTH_SHORT).show();//不显示提示了
					Stage s = appData.getStageList().get(stage);
					int score = Score.calculatePoint(chronometer.getText().toString(), tvfoot.getText().toString(),
							s.getTimelimit(), s.getFootlimit());
					showResultWindow(score);
					if (isSoundPlay) {
						soundPlayer[3].start();// aplause
					}
					saveResult(chronometer.getText().toString(), tvfoot.getText().toString(), score);
					vibrator.vibrate(1000);//振动一秒钟
				}
				// 没有完成就要更新界面，然后继续运行游戏
				gridviewItems.get(face).put("image", faces[getRandomFace()]);// 重新修改一下face
				gridviewAdapter = new SimpleAdapter(PuzzleActivity.this, gridviewItems, R.layout.gamebordcell,
						new String[] { "image" }, new int[] { R.id.gridviewcell });
				gridgamebord.setAdapter(gridviewAdapter);
				gridgamebord.invalidate();
			}
		});
	}

	// 如果成功完成本关卡，要保存这次游戏的数据！
	protected void saveResult(String time, String foot, int score) {
		System.out.println(stage + " current is " + gameData.getCurrntStage());
		if (stage == gameData.getCurrntStage()) {// 注意stage是从0开始算起的！ currentstage 也是从0开始算起的
			gameData.setCurrntStage(stage + 1);// 这里的意思就是过了当前这一关，即可解锁下一关
		}
		System.out.println("now current stage is " + gameData.getCurrntStage());
		// TODO:这里还要比较以前的成绩，只保存最好的成绩
		Score oldScore = gameData.getScoreList().get(stage);
		Score newScore = new Score(stage);
		newScore.setPoint(score);
		newScore.setFoot(Integer.parseInt(foot));
		newScore.setTime(Score.time2long(time));
		if (Score.compareResult(oldScore, newScore)) {
			System.out.println("new score");
			gameData.getScoreList().set(stage, newScore);
			try {
				// 这里appdata是null，而gamedata不为null，所以是重新写数据文件
				GameDataUtil
						.writeGameData(openFileOutput(GameDataUtil.GAMEDATA_FILENAME, MODE_PRIVATE), null, gameData);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	// 显示成绩的popupwindow
	protected void showResultWindow(int point) {
		View viewGroup = (View) getLayoutInflater().inflate(R.layout.popwindow, null);
		final PopupWindow window = new PopupWindow(viewGroup, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setFocusable(true);
		window.setOutsideTouchable(false);// 不允许外点击 点击PopuWindow外面并不会关闭PopuWindow
		// window.setOutsideTouchable(true);
		ImageView ivOk = (ImageView) viewGroup.findViewById(R.id.ivOk);

		ivOk.setOnClickListener(new View.OnClickListener() {// 给确定按钮添加一个关闭popupwindow的监听器
			public void onClick(View v) {
				window.dismiss();// 这里退出popupwindow，最好是能够直接跳到选关界面
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
			ivOk.setImageResource(prizeIds[stage]);// 通过反射得到这张图片
			ivOk.setPadding(40, 0, 0, 0);// 为了显示的美观，要设置padding
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
		// window.showAsDropDown(puzzleView, x, 0);//这样写会报错！
		window.showAtLocation(puzzleView, Gravity.CENTER, 0, 40);// 这句话很重要，一定要声明显示的位置
	}

	// 空白出显示的图片，随机的一个face
	private int getRandomFace() {
		random = new Random();
		return random.nextInt(faces.length);
	}

	// 检查是否完成了
	protected boolean validateFinish() {
		if (path.equals(FINISHPATH)) {
			return true;
		}
		return false;
	}

	// 判断是否要交换位置
	protected boolean changePosition(int position) {
		// 首先还要判断是否可以进行交换
		// 暂时没有想到好的办法
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

	// 改变字符串path
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

	// 点击 Back 图标，返回选关界面
	class ImageViewBackListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// 返回之前要进行一些垃圾处理操作！
			Intent intent = new Intent(PuzzleActivity.this, StagesActivity.class);
			startActivity(intent);
			PuzzleActivity.this.finish();// 销毁这个activity
		}
	}

	// 点击 Config 图标，返回游戏首页
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

	// 点击 control 图标，控制游戏的开始暂停
	class ImageViewControlListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			// view.startAnimation(touchAnimation);
			foot = 0;
			isStarted = true;
			isFinished = false;// 一定要重置这个！
			System.out.println(SystemClock.elapsedRealtime());// 642498697
			chronometer.setBase(SystemClock.elapsedRealtime());// 在参数后面加上一些数字，如果数字太小没有任何影响
			// chronometer.setBase(6);
			chronometer.start();
			ivControl.setVisibility(View.GONE);// 一旦开始便不可以停止！除非reset或者游戏结束或者返回上一个界面
		}
	}

	// 点击 reset 图标，游戏重新开始
	class ImageViewResetListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			// view.startAnimation(touchAnimation);
			foot = 0;
			tvfoot.setText(foot + "");
			chronometer.stop();
			chronometer.setBase(SystemClock.elapsedRealtime());// 这个方法调用之后定时器的确会重置为00:00
			ivControl.setVisibility(View.VISIBLE);
			isStarted = false;
			isFinished = false;
			initGamebord();
		}
	}

	/** 当Activity不活跃的时候，停止音乐 */
	@Override
	protected void onPause() {
		if (musicPlayer.isPlaying()) {
			musicPlayer.pause();
		}
		super.onPause();
	}

	/** 当Activity再次处于活跃状态的时候，开启音乐 */
	@Override
	protected void onResume() {
		if (isMusicPlay) {
			musicPlayer.start();
		}
		super.onResume();
	}

	/** 消毁时调用这个方法 */
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
