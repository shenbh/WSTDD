package com.newland.wstdd.originate.origateactivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.common.UrlManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.selectphoto.PhotoUpImageItem;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.widget.PengTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 选择删除封面的图片
 * 
 * @author Administrator
 * 
 */
public class SelectedCoverActivity extends Activity {
	private static final String TAG = "SelectedCoverActivity";//收集异常日志tag
	ImageView returnImageView;// 返回的图表 发送广播 把arraypist传回去就好 点击按下返回键的是也是一样的道理

	PengTextView setCoverIncon;// 设置封面的图标
	PhotoUpImageItem temPhotoUpImageItem;// 每一张图片对应的对象
	// 从本地获取图片的设置到ImageView上的相关设置
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	// 图片数量的变化
	private TextView positionChangeTextView;// 1/9 2/9
	private TextView deleTextView;// 删除按钮
	private TextView setCoverTextView;// 设置为封面
	// 设置滑动到当前图片上的position
	private int tempPosition;// 临时页面对应 的位置
	List<View> advPics;
	private AdvAdapter adapter;
	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ViewPager advPager = null;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;
	// 用来接收从编辑活动界面传过来的 arraylist 跟 position位置
	private ArrayList<PhotoUpImageItem> arrayList = new ArrayList<PhotoUpImageItem>();// 用来接收第一个界面传过来的已选的图片
	private int initPosition;// 初始化的位置

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		setContentView(R.layout.activity_selected_cover);

		
		 /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		arrayList = (ArrayList<PhotoUpImageItem>) getIntent().getSerializableExtra("selectImageList");
		initPosition = getIntent().getIntExtra("position", 0);

		setCoverIncon = (PengTextView) findViewById(R.id.activity_selected_cover_iv);// 设置封面的图标
		returnImageView = (ImageView) findViewById(R.id.select_cover_return_icon);

		returnImageView.setOnClickListener(new MyClick());
		setCoverIncon.setOnClickListener(new MyClick());
		imageLoader = ImageLoader.getInstance();
		// 使用DisplayImageOption.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.album_default_loading_pic) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.album_default_loading_pic) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.album_default_loading_pic) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.bitmapConfig(Config.ARGB_8888).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build(); // 创建配置过的DisplayImageOption对象
		initViewPager();
	}

	@Override
	protected void onDestroy() {
		/** 收集异常日志 */
		LogManager.getManager(getApplicationContext()).unregisterActivity(this);
		/** 收集异常日志 */
		super.onDestroy();
	}
	private void initViewPager() {
		advPager = (ViewPager) findViewById(R.id.adv_pager);
		deleTextView = (TextView) findViewById(R.id.activity_select_cover_delete);
		deleTextView.setOnClickListener(new MyClick());
		positionChangeTextView = (TextView) findViewById(R.id.activity_select_cover_positionchange);
		positionChangeTextView.setText((1) + "/" + arrayList.size());
		advPics = new ArrayList<View>();

		for (int i = 0; i < arrayList.size(); i++) {
			ImageView img = new ImageView(this);
			if (arrayList.get(i).getImagePath() == null) {
				((ImageView) advPics.get(i)).setImageDrawable(getResources().getDrawable(R.drawable.two_code));
			} else {
				File file = new File(arrayList.get(i).getImagePath());
				if (file.exists()) {
					imageLoader.displayImage("file://" + arrayList.get(i).getImagePath(), img, options);
				} else {
					file.delete();
					imageLoader.displayImage(UrlManager.uploadToUrlServer + arrayList.get(i).getImagePath(), img, options);
				}

			}
			advPics.add(img);
		}

		/* imageLoader.displayImage("file://"+arrayList.get(0).getImagePath(),
		 ((ImageView)advPics.get(0)),
		 options);*/
		imageViews = new ImageView[advPics.size()];
		adapter = new AdvAdapter(advPics);
		advPager.setAdapter(adapter);
		advPager.setOnPageChangeListener(new GuidePageChangeListener());
		advPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					isContinue = false;
					break;
				case MotionEvent.ACTION_UP:
					isContinue = true;
					break;
				default:
					isContinue = true;
					break;
				}
				return false;
			}
		});

		// 判断首次显示的那一页是否为封面
		advPager.setCurrentItem(initPosition);
		if (arrayList.get(initPosition).isCover()) {
			setCoverIncon.setTextColor(getResources().getColor(R.color.white));
			setCoverIncon.setDrawableLeft(getResources().getDrawable(R.drawable.set_cover_select_icon));
			setCoverIncon.setCompoundDrawablesWithIntrinsicBounds(setCoverIncon.getDrawableLeft(), null, null, null);
		}
		positionChangeTextView.setText((initPosition + 1) + "/" + arrayList.size());

	}

	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			what.getAndSet(position);
			tempPosition = position;
			positionChangeTextView.setText((position + 1) + "/" + arrayList.size());

			if (arrayList.get(position).getImagePath() == null) {
				((ImageView) advPics.get(position)).setImageDrawable(getResources().getDrawable(R.drawable.two_code));
			} else {
				File file = new File(arrayList.get(position).getImagePath());
				if (file.exists()) {
					imageLoader.displayImage("file://" + arrayList.get(position).getImagePath(), ((ImageView) advPics.get(position)), options);
				} else {
					imageLoader.displayImage(UrlManager.uploadToUrlServer + arrayList.get(position).getImagePath(), ((ImageView) advPics.get(position)), options);
				}

			}

			if (arrayList.get(tempPosition).isCover()) {
				setCoverIncon.setTextColor(getResources().getColor(R.color.white));
				setCoverIncon.setDrawableLeft(getResources().getDrawable(R.drawable.set_cover_select_icon));
				setCoverIncon.setCompoundDrawablesWithIntrinsicBounds(setCoverIncon.getDrawableLeft(), null, null, null);
			} else {
				setCoverIncon.setTextColor(getResources().getColor(R.color.white));
				setCoverIncon.setDrawableLeft(getResources().getDrawable(R.drawable.set_cover_normal_icon));
				setCoverIncon.setCompoundDrawablesWithIntrinsicBounds(setCoverIncon.getDrawableLeft(), null, null, null);
			}

		}

	}

	private final class AdvAdapter extends PagerAdapter {
		private List<View> views = null;

		public AdvAdapter(List<View> views) {
			this.views = views;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// super.destroyItem(container, position, object);
			((ViewPager) container).removeView(views.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			return views.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
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

		public List<View> getViews() {
			return views;
		}

		public void setViews(List<View> views) {
			this.views = views;
		}

	}

	public class MyClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.activity_select_cover_delete:
				int index = tempPosition;
				int size = arrayList.size();
				if (tempPosition == arrayList.size()) {
					--tempPosition;
				}
				if (arrayList.size() > 0) {
					temPhotoUpImageItem = (PhotoUpImageItem) arrayList.get(tempPosition);
					showIsDeleteDialog(temPhotoUpImageItem, tempPosition);
				}else {
					UiHelper.ShowOneToast(SelectedCoverActivity.this, "无图可删，请增加图片");
					sendBroad();
				}

				break;
			case R.id.activity_selected_cover_iv:
				// 如果原来就已经是封面了，那么就消除掉
				if (!((PhotoUpImageItem) arrayList.get(tempPosition)).isCover()) {
					for (int i = 0; i < arrayList.size(); i++) {
						if (arrayList.get(i).isCover()) {
							arrayList.get(i).setCover(false);//
							setCoverIncon.setTextColor(getResources().getColor(R.color.white));
							setCoverIncon.setDrawableLeft(getResources().getDrawable(R.drawable.set_cover_normal_icon));
							setCoverIncon.setCompoundDrawablesWithIntrinsicBounds(setCoverIncon.getDrawableLeft(), null, null, null);
						}
					}

					((PhotoUpImageItem) arrayList.get(tempPosition)).setCover(true);
					setCoverIncon.setTextColor(getResources().getColor(R.color.white));
					setCoverIncon.setDrawableLeft(getResources().getDrawable(R.drawable.set_cover_select_icon));
					setCoverIncon.setCompoundDrawablesWithIntrinsicBounds(setCoverIncon.getDrawableLeft(), null, null, null);
				}

				break;
			case R.id.select_cover_return_icon:
				// 返回的时候发送广播给

				Intent intent0 = new Intent();// 切记
												// 这里的Action参数与IntentFilter添加的Action要一样才可以
				intent0.setAction("ORIGUNATE_CHAIR_PHOTO");
				intent0.putExtra("selectIma", arrayList);
				intent0.putExtra("type", "cover");// 从封面的界面返回的
				sendBroadcast(intent0);// 发送广播了
				finish();
				break;
			default:
				break;
			}
		}

	}

	private void showIsDeleteDialog(PhotoUpImageItem photoUpImageItem, final int position) {
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("是否删除所选图片").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// 首先需要先在除去之前先改变下设置封面的状态
				if (0 < position) {
					if (arrayList.get(position - 1).isCover()) {
						setCoverIncon.setTextColor(getResources().getColor(R.color.white));
						setCoverIncon.setDrawableLeft(getResources().getDrawable(R.drawable.set_cover_select_icon));
						setCoverIncon.setCompoundDrawablesWithIntrinsicBounds(setCoverIncon.getDrawableLeft(), null, null, null);
					} else {
						setCoverIncon.setTextColor(getResources().getColor(R.color.white));
						setCoverIncon.setDrawableLeft(getResources().getDrawable(R.drawable.set_cover_normal_icon));
						setCoverIncon.setCompoundDrawablesWithIntrinsicBounds(setCoverIncon.getDrawableLeft(), null, null, null);
					}
				}
				/*
				 * else { if (arrayList.get(1).isCover()) {
				 * setCoverIncon.setTextColor
				 * (getResources().getColor(R.color.white));
				 * setCoverIncon.setDrawableLeft
				 * (getResources().getDrawable(R.drawable
				 * .set_cover_select_icon));
				 * setCoverIncon.setCompoundDrawablesWithIntrinsicBounds
				 * (setCoverIncon.getDrawableLeft(), null, null, null); } else {
				 * setCoverIncon
				 * .setTextColor(getResources().getColor(R.color.white));
				 * setCoverIncon
				 * .setDrawableLeft(getResources().getDrawable(R.drawable
				 * .set_cover_normal_icon));
				 * setCoverIncon.setCompoundDrawablesWithIntrinsicBounds
				 * (setCoverIncon.getDrawableLeft(), null, null, null); } }
				 */

				// 如果是删除最后一个item的话，需要添加 添加图片
				arrayList.remove(temPhotoUpImageItem);
				int index = advPager.getCurrentItem();
				advPager.setAdapter(null);
				advPics.remove(index);
				advPager.setAdapter(adapter);
				advPager.postInvalidate();
				advPager.invalidate();
				adapter.setViews(advPics);
				adapter.notifyDataSetChanged();
				if (0 < index) {
					advPager.setCurrentItem(index - 1);
					positionChangeTextView.setText((index) + "/" + arrayList.size());

				} else {
					if (arrayList.size() == 0) {
						advPager.setCurrentItem(0);
						positionChangeTextView.setText((0) + "/" + arrayList.size());
					}else {
						advPager.setCurrentItem(index+1);
						positionChangeTextView.setText((index+1)+"/"+arrayList.size());
					}
				}
				// 设置封面的标志也需要修改，也就是
			}
		}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();
		dialog.setCanceledOnTouchOutside(false);
	}

	private void sendBroad(){
		Intent intent0 = new Intent();// 切记
		// 这里的Action参数与IntentFilter添加的Action要一样才可以
		intent0.setAction("ORIGUNATE_CHAIR_PHOTO");
		intent0.putExtra("selectIma", arrayList);
		intent0.putExtra("type", "cover");// 从封面的界面返回的
		sendBroadcast(intent0);// 发送广播了
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			sendBroad();
			return true;
		}
		return false;
	}
}