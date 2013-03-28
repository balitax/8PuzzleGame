package com.yinger.puzzlegame;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yinger.beans.GameData;
import com.yinger.beans.Score;
import com.yinger.util.DensityUtil;
import com.yinger.util.GameDataUtil;
import com.yinger.util.GameService;

public class StagesActivity extends Activity {

	private final String TAG = "StagesActivity";
	private int[] imageIds = new int[] { R.drawable.stage1, R.drawable.stage2, R.drawable.stage3, R.drawable.stage4 };
	private int[] prizeIds = new int[] { R.drawable.prize1, R.drawable.prize2, R.drawable.prize3, R.drawable.prize4 };
	private int[] stageinfos = new int[] { R.drawable.stageinfo1, R.drawable.stageinfo2, R.drawable.stageinfo3,
			R.drawable.stageinfo4, R.drawable.stageinfo5, R.drawable.stageinfo6 };
	private GameData gameData;
	// private ImageSwitcher isInfo;//使用imageswitcher出错了
	private ImageView isInfo;
	private TextView textResult;
	private int[] status = new int[imageIds.length];

	private final int STATUS_NONE = 0;// 这个数字代表它分别对应stageinfos数组中那张图片的index
	private final int STATUS_ONE = 1;
	private final int STATUS_TWO = 2;
	private final int STATUS_THREE = 3;
	private final int STATUS_BEST = 4;
	private final int STATUS_LOCKED = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewGroup main = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_stages, null);
		setContentView(main);
		if (GameService.isMusicPlay) {
			if (!GameService.musicPlayer.isPlaying()) {
				GameService.musicPlayer.start();
			}
		}
		try {
			System.out.println("activity --- loading gamedata");
			gameData = GameDataUtil.loadGameData(openFileInput(GameDataUtil.GAMEDATA_FILENAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println(gameData.getCurrntStage());
		for (Score score : gameData.getScoreList()) {
			System.out.println(score.getStageid() + " " + score.getPoint() + " " + score.getFoot() + " "
					+ score.getTime());
		}

		ImageView ivHome = (ImageView) findViewById(R.id.ivHome);
		ivHome.setOnClickListener(new ImageViewHomeListener());

		isInfo = (ImageView) findViewById(R.id.isInfo);
		// isInfo.setImageResource(R.drawable.stageinfo);
		// isInfo.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		// isInfo.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

		Gallery gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new MyGalleryAdapter());
		gallery.setOnItemSelectedListener(new GallerySelectedChangeListener());
	}

	// 点击 Home 图标，返回游戏首页
	class ImageViewHomeListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(StagesActivity.this, MainActivity.class);
			startActivity(intent);
			StagesActivity.this.finish();// 这里需要
			// overridePendingTransition(R.anim.fade, R.anim.hold);
		}
	}

	// MyGalleryAdapter对象负责提供Gallery所显示的图片
	class MyGalleryAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageIds.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// 该方法的返回的View就是代表了每个列表项
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			System.out.println("get view " + position);
			System.out.println("status :" + status);
			View stageView = getLayoutInflater().inflate(R.layout.stage, null);
			// textResult = (TextView) stageView.findViewById(R.id.textResult);//
			ImageView ivStage = (ImageView) stageView.findViewById(R.id.ivStage);
			ivStage.setImageResource(imageIds[position]);
			ivStage.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ivEnterOnClick(position);// 点击关卡即可进入游戏
				}
			});
			ViewGroup stageGroup = (ViewGroup) stageView.findViewById(R.id.stageGroup);
			if (isLocked(position)) {// 这关被锁了，锁没锁是通过currentstage和position来判断的
				ImageView ivLock = new ImageView(StagesActivity.this);
				ivLock.setImageResource(R.drawable.puzzlelock);
				ivLock.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						ivPuzzleOnClick();
					}
				});
				stageGroup.addView(ivLock);
				// textResult.setText("暂无记录");//
				status[position] = STATUS_LOCKED;// 在这里保存每个stage的status
			} else {// 没有被锁，没锁的话根据point来显示信息
				int point = gameData.getScoreList().get(position).getPoint();

				if (point < 0) {// 还没有玩过，那么就是点击进去
					ImageView ivEnter = new ImageView(StagesActivity.this);
					ivEnter.setImageResource(R.drawable.button_question);
					ivEnter.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							ivEnterOnClick(position);
						}
					});
					stageGroup.addView(ivEnter);
					// textResult.setText("暂无记录");//
					status[position] = STATUS_NONE;
				} else {// 根据成绩显示星星的数目
					if (point == Score.GOOD) {// 1
						ImageView ivstar1 = new ImageView(StagesActivity.this);
						ivstar1.setImageResource(R.drawable.star);
						stageGroup.addView(ivstar1);
						status[position] = STATUS_ONE;
					} else if (point == Score.GREAT) {// 2
						ImageView ivstar1 = new ImageView(StagesActivity.this);
						ivstar1.setImageResource(R.drawable.star);
						stageGroup.addView(ivstar1);
						ImageView ivstar2 = new ImageView(StagesActivity.this);
						ivstar2.setImageResource(R.drawable.star);
						ivstar2.setPadding(10, 0, 0, 0);
						stageGroup.addView(ivstar2);
						status[position] = STATUS_TWO;
					} else if (point == Score.EXCELLENT) {// 3
						ImageView ivstar1 = new ImageView(StagesActivity.this);
						ivstar1.setImageResource(R.drawable.star);
						stageGroup.addView(ivstar1);
						ImageView ivstar2 = new ImageView(StagesActivity.this);
						ivstar2.setImageResource(R.drawable.star);
						ivstar2.setPadding(10, 0, 0, 0);
						stageGroup.addView(ivstar2);
						ImageView ivstar3 = new ImageView(StagesActivity.this);
						ivstar3.setImageResource(R.drawable.star);
						ivstar3.setPadding(10, 0, 0, 0);
						stageGroup.addView(ivstar3);
						status[position] = STATUS_THREE;
					} else if (point == Score.BEST) {// prize
						ImageView ivPrize = new ImageView(StagesActivity.this);
						ivPrize.setImageResource(prizeIds[position]);
						stageGroup.addView(ivPrize);
						stageGroup.setBackgroundDrawable(null);// 这种情况下不显示折纸标签
						// 增加padding top
						stageGroup.setPadding(0, DensityUtil.dip2px(StagesActivity.this, 120), 0, 0);// 为了显示的效果，这里使得奖章和stage图片重合
						status[position] = STATUS_BEST;
					}
					// textResult.setText("最佳记录  时间 " + Score.long2time(gameData.getScoreList().get(position).getTime())
					// + "  步数 " + gameData.getScoreList().get(position).getFoot());
				}
			}
			return stageView;
		}

		// 判断是否上锁
		private boolean isLocked(int position) {
			return gameData.getCurrntStage() < (position);// 记住，currentstage和stage都是从0开始，position也是如此
		}

		// 处理锁关图片点击事件
		protected void ivPuzzleOnClick() {
			Toast.makeText(StagesActivity.this, "朋友，先把前面的关过了吧！", Toast.LENGTH_SHORT).show();
		}

		// 处理进入关卡图片点击事件
		protected void ivEnterOnClick(int position) {
			if (isLocked(position)) {
				Toast.makeText(StagesActivity.this, "朋友，先把前面的关过了吧！", Toast.LENGTH_SHORT).show();
				return;
			}
			// Toast.makeText(StagesActivity.this, "选择了 " + (position + 1) + " 关", Toast.LENGTH_SHORT).show();//不显示提示了
			Intent intent = new Intent(StagesActivity.this, PuzzleActivity.class);
			intent.putExtra("stage", position);// 数据实际上存在bundle中的---注意这里不需要position+1，因为puzzle activity中只是需要position
			startActivity(intent);
			// overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			// StagesActivity.this.finish();//这里不要
			// 这个的效果是要退出的activity向右出去，要进入的activity向左进入
			// 不管原有的animation是系统的，还是配置文件中配置了的，这里都可以根据需要进行覆盖！
		}
	}

	// 当gallery选关发生变化时触发的事件
	class GallerySelectedChangeListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			isInfo.setImageResource(stageinfos[status[position]]);// 通过反射得到这张图片
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			isInfo.setImageResource(R.drawable.stageinfo1);
		}
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
