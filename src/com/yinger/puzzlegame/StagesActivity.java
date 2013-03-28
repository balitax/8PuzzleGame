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
	// private ImageSwitcher isInfo;//ʹ��imageswitcher������
	private ImageView isInfo;
	private TextView textResult;
	private int[] status = new int[imageIds.length];

	private final int STATUS_NONE = 0;// ������ִ������ֱ��Ӧstageinfos����������ͼƬ��index
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

	// ��� Home ͼ�꣬������Ϸ��ҳ
	class ImageViewHomeListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(StagesActivity.this, MainActivity.class);
			startActivity(intent);
			StagesActivity.this.finish();// ������Ҫ
			// overridePendingTransition(R.anim.fade, R.anim.hold);
		}
	}

	// MyGalleryAdapter�������ṩGallery����ʾ��ͼƬ
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

		// �÷����ķ��ص�View���Ǵ�����ÿ���б���
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
					ivEnterOnClick(position);// ����ؿ����ɽ�����Ϸ
				}
			});
			ViewGroup stageGroup = (ViewGroup) stageView.findViewById(R.id.stageGroup);
			if (isLocked(position)) {// ��ر����ˣ���û����ͨ��currentstage��position���жϵ�
				ImageView ivLock = new ImageView(StagesActivity.this);
				ivLock.setImageResource(R.drawable.puzzlelock);
				ivLock.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						ivPuzzleOnClick();
					}
				});
				stageGroup.addView(ivLock);
				// textResult.setText("���޼�¼");//
				status[position] = STATUS_LOCKED;// �����ﱣ��ÿ��stage��status
			} else {// û�б�����û���Ļ�����point����ʾ��Ϣ
				int point = gameData.getScoreList().get(position).getPoint();

				if (point < 0) {// ��û���������ô���ǵ����ȥ
					ImageView ivEnter = new ImageView(StagesActivity.this);
					ivEnter.setImageResource(R.drawable.button_question);
					ivEnter.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							ivEnterOnClick(position);
						}
					});
					stageGroup.addView(ivEnter);
					// textResult.setText("���޼�¼");//
					status[position] = STATUS_NONE;
				} else {// ���ݳɼ���ʾ���ǵ���Ŀ
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
						stageGroup.setBackgroundDrawable(null);// ��������²���ʾ��ֽ��ǩ
						// ����padding top
						stageGroup.setPadding(0, DensityUtil.dip2px(StagesActivity.this, 120), 0, 0);// Ϊ����ʾ��Ч��������ʹ�ý��º�stageͼƬ�غ�
						status[position] = STATUS_BEST;
					}
					// textResult.setText("��Ѽ�¼  ʱ�� " + Score.long2time(gameData.getScoreList().get(position).getTime())
					// + "  ���� " + gameData.getScoreList().get(position).getFoot());
				}
			}
			return stageView;
		}

		// �ж��Ƿ�����
		private boolean isLocked(int position) {
			return gameData.getCurrntStage() < (position);// ��ס��currentstage��stage���Ǵ�0��ʼ��positionҲ�����
		}

		// ��������ͼƬ����¼�
		protected void ivPuzzleOnClick() {
			Toast.makeText(StagesActivity.this, "���ѣ��Ȱ�ǰ��Ĺع��˰ɣ�", Toast.LENGTH_SHORT).show();
		}

		// �������ؿ�ͼƬ����¼�
		protected void ivEnterOnClick(int position) {
			if (isLocked(position)) {
				Toast.makeText(StagesActivity.this, "���ѣ��Ȱ�ǰ��Ĺع��˰ɣ�", Toast.LENGTH_SHORT).show();
				return;
			}
			// Toast.makeText(StagesActivity.this, "ѡ���� " + (position + 1) + " ��", Toast.LENGTH_SHORT).show();//����ʾ��ʾ��
			Intent intent = new Intent(StagesActivity.this, PuzzleActivity.class);
			intent.putExtra("stage", position);// ����ʵ���ϴ���bundle�е�---ע�����ﲻ��Ҫposition+1����Ϊpuzzle activity��ֻ����Ҫposition
			startActivity(intent);
			// overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			// StagesActivity.this.finish();//���ﲻҪ
			// �����Ч����Ҫ�˳���activity���ҳ�ȥ��Ҫ�����activity�������
			// ����ԭ�е�animation��ϵͳ�ģ����������ļ��������˵ģ����ﶼ���Ը�����Ҫ���и��ǣ�
		}
	}

	// ��galleryѡ�ط����仯ʱ�������¼�
	class GallerySelectedChangeListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			isInfo.setImageResource(stageinfos[status[position]]);// ͨ������õ�����ͼƬ
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			isInfo.setImageResource(R.drawable.stageinfo1);
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
