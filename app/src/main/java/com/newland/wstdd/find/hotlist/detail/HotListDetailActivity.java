package com.newland.wstdd.find.hotlist.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.bean.TddActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.updownloadimg.ImageDownLoad;
import com.newland.wstdd.find.categorylist.registrationedit.registration.RegistrationSubmitActivity;

/**
 * 发现-热门列表具体详情界面
 * 
 * @author H81 2015-11-19
 * 
 */
// TODO 数据是重新从网络上获取的，并非是传递过来的
public class HotListDetailActivity extends BaseFragmentActivity implements OnClickListener, OnPostListenerInterface {
	private static final String TAG = "HotListDetailActivity";//收集异常日志tag
	// // 详情
	// private LinearLayout mHor_lin;
	// private LinearLayout mLl_detail;
	// private TextView mTv_detail;
	// private ImageView mIv_detail;
	private PopupWindow popupWindow;
	// // 评论
	// private LinearLayout mLl_discuss;
	// private TextView mTv_discuss;
	// private ImageView mIv_discuss;
	// private ViewPager mViewPager;
	// private List<BaseFragment> listFragments;
	// private BaseFragment currentFragment;// 当前选中的Fragment
	// FindChairDetailFragment findChairDetailFragment;// 发现-讲座详情-详情
	// FindChairDetailFragment findChairDetailFragment1;// 发现-讲座详情-详情

	private FrameLayout mLayout;
	private TextView mActivity_find_apply_title_tv;
	private TextView mActivity_find_apply_originatename_tv;
	private TextView mActivity_find_apply_originatetime_tv;
	private ImageView mActivity_find_apply_img_iv;
	private com.newland.wstdd.common.widget.PengTextView mActivity_find_chairtime_ptv;
	private com.newland.wstdd.common.widget.PengTextView mActivity_find_chairaddress_ptv;
	private TextView mActivity_find_chairsigncount_tv;
	private TextView mActivity_find_chairdetail_detail_tv;
	private ImageView[] mActivity_find_chairdetail_detail_iv = new ImageView[7];
	private TextView mActivity_find_chairdetail_detail_readingquantity_tv;
	private com.newland.wstdd.common.widget.PengTextView mActivity_find_td;
	private com.newland.wstdd.common.widget.PengTextView mActivity_find_collect;
	private com.newland.wstdd.common.widget.PengTextView mActivity_find_like;
	private Button mActivity_find_register_btn;// 我要报名
	ImageButton rightBtn;

	int maxImgWidth;
	int maxImgHeight;

	/** --------------接收数据 ------------- */
	TddActivity tddActivity;

	/** --------------接收数据 ------------- */
	@Override
	protected void processMessage(Message msg) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		getIntentData();
		setContentView(R.layout.activity_find_chairdetail);
		
		 /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
		
		initView();
		// initData();
		// initFragment();
		setTitle();
	}

	@Override
	protected void onDestroy() {
		 /**收集异常日志*/
    	LogManager.getManager(getApplicationContext()).unregisterActivity(this);
    	 /**收集异常日志*/
    	super.onDestroy();
	}
	private void getIntentData() {
		Intent intent = getIntent();
		tddActivity = (TddActivity) intent.getSerializableExtra("tddActivity");
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		ImageView leftBtn = (ImageView) findViewById(R.id.head_left_iv);
		TextView centerTitle = (TextView) findViewById(R.id.head_center_title);
		TextView rightTv = (TextView) findViewById(R.id.head_right_tv);
		rightBtn = (ImageButton) findViewById(R.id.head_right_btn);
		leftBtn.setVisibility(View.VISIBLE);
		centerTitle.setText(StringUtil.intType2Str(tddActivity.getActivityType()) + "报名");
		// rightTv.setText("发布");
		rightTv.setVisibility(View.GONE);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setImageDrawable(getResources().getDrawable(R.drawable.find));
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
	}

	public void initView() {
		// mHor_lin = (LinearLayout) findViewById(R.id.hor_lin);
		// mLl_detail = (LinearLayout) findViewById(R.id.ll_detail);
		// mTv_detail = (TextView) findViewById(R.id.tv_detail);
		// mIv_detail = (ImageView) findViewById(R.id.iv_detail);
		// mLl_discuss = (LinearLayout) findViewById(R.id.ll_discuss);
		// mTv_discuss = (TextView) findViewById(R.id.tv_discuss);
		// mIv_discuss = (ImageView) findViewById(R.id.iv_discuss);
		// mViewPager = (ViewPager) findViewById(R.id.mViewPager);

		mLayout = (FrameLayout) findViewById(R.id.layout);

		mActivity_find_apply_title_tv = (TextView) findViewById(R.id.activity_find_apply_title_tv);
		mActivity_find_apply_originatename_tv = (TextView) findViewById(R.id.activity_find_apply_originatename_tv);
		mActivity_find_apply_originatetime_tv = (TextView) findViewById(R.id.activity_find_apply_originatetime_tv);
		mActivity_find_apply_img_iv = (ImageView) findViewById(R.id.activity_find_apply_img_iv);
		mActivity_find_chairtime_ptv = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_find_chairtime_ptv);
		mActivity_find_chairaddress_ptv = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_find_chairaddress_ptv);
		mActivity_find_chairsigncount_tv = (TextView) findViewById(R.id.activity_find_chairsigncount_tv);

		mActivity_find_chairdetail_detail_tv = (TextView) findViewById(R.id.activity_find_chairdetail_detail_tv);
		mActivity_find_chairdetail_detail_iv[0] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv1);
		mActivity_find_chairdetail_detail_iv[1] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv2);
		mActivity_find_chairdetail_detail_iv[2] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv3);
		mActivity_find_chairdetail_detail_iv[3] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv4);
		mActivity_find_chairdetail_detail_iv[4] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv5);
		mActivity_find_chairdetail_detail_iv[5] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv6);
		mActivity_find_chairdetail_detail_iv[6] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv7);
		mActivity_find_chairdetail_detail_readingquantity_tv = (TextView) findViewById(R.id.activity_find_chairdetail_detail_readingquantity_tv);
		mActivity_find_td = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_call_phone);
		mActivity_find_collect = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_find_collect);
		mActivity_find_like = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_find_like);

		mActivity_find_register_btn = (Button) findViewById(R.id.activity_find_register_btn);
		mActivity_find_collect.setOnClickListener(this);
		mActivity_find_like.setOnClickListener(this);
		mActivity_find_register_btn.setOnClickListener(this);
		maxImgWidth = AppContext.getAppContext().getScreenWidth() - 20;
		maxImgHeight = AppContext.getAppContext().getScreenHeight();
		for (int i = 0; i < mActivity_find_chairdetail_detail_iv.length; i++) {

			mActivity_find_chairdetail_detail_iv[i].getLayoutParams().height = 200;

			mActivity_find_chairdetail_detail_iv[i].getLayoutParams().width = 200;

			mActivity_find_chairdetail_detail_iv[i].setMaxWidth(maxImgWidth);
			mActivity_find_chairdetail_detail_iv[i].setMaxHeight(maxImgHeight);
		}
		initActivityData();
		initDetailData();
	}

	/**
	 * 初始化活动信息
	 * 
	 */
	private void initActivityData() {
		mActivity_find_apply_title_tv.setText(StringUtil.noNull(tddActivity.getActivityTitle()));
		mActivity_find_apply_originatename_tv.setText(StringUtil.noNull(tddActivity.getSponsor()));
		mActivity_find_apply_originatetime_tv.setText(StringUtil.noNull(tddActivity.getFriendActivityTime()));
		if (StringUtil.isNotEmpty(tddActivity.getImage1())) {
			mActivity_find_apply_img_iv.getLayoutParams().width = 200;
			mActivity_find_apply_img_iv.getLayoutParams().height = 200;
			ImageDownLoad.getDownLoadImg(tddActivity.getImage1(), mActivity_find_apply_img_iv);
		}
		mActivity_find_chairtime_ptv.setText(StringUtil.noNull(tddActivity.getActivityTime()));
		mActivity_find_chairaddress_ptv.setText(StringUtil.noNull(tddActivity.getActivityAddress()));
		mActivity_find_chairsigncount_tv.setText(StringUtil.noNull(tddActivity.getSignCount()));
	}

	/**
	 * 传入图片地址字符串（用“,”隔开）,转成ArrayList<String>输出
	 * 
	 * @param imgUrls
	 * @return
	 */
	private String[] getImageList(String imgUrls) {
		String[] strs = imgUrls.split(",");
		return strs;
	}

	/**
	 * 初始化详情信息
	 */
	private void initDetailData() {
		String[] imgs = null;
		if (StringUtil.isNotEmpty(tddActivity.getImage())) {
			imgs = getImageList(tddActivity.getImage());
		}
		mActivity_find_chairdetail_detail_tv.setText(StringUtil.noNull(tddActivity.getActivityTitle()));
		int i = 0;
		try {
			if (imgs != null) {
				for (; i < mActivity_find_chairdetail_detail_iv.length; i++) {
					if (StringUtil.isNotEmpty(imgs[i])) {
						ImageDownLoad.getDownLoadImg(imgs[i], mActivity_find_chairdetail_detail_iv[i]);
						mActivity_find_chairdetail_detail_iv[i].setVisibility(View.VISIBLE);
					}
				}
			}
		} catch (Exception e) {
			return;
		} finally {
			for (; i < mActivity_find_chairdetail_detail_iv.length; i++) {
				mActivity_find_chairdetail_detail_iv[i].setVisibility(View.GONE);
			}
		}

		// ImageDownLoad.getDownLoadImg(tddActivity.getImage1(),
		// mActivity_find_chairdetail_detail_iv5);
		// ImageDownLoad.getDownLoadImg(tddActivity.getImage1(),
		// mActivity_find_chairdetail_detail_iv6);
		// ImageDownLoad.getDownLoadImg(tddActivity.getImage1(),
		// mActivity_find_chairdetail_detail_iv7);
		mActivity_find_chairdetail_detail_readingquantity_tv.setText("阅读 " + StringUtil.noNull(tddActivity.getViewCount()));
	}

	/**
	 * 初始化viewpager颜色
	 */
	// private void initData() {
	// mIv_detail.setVisibility(View.VISIBLE);
	// mIv_discuss.setVisibility(View.INVISIBLE);
	//
	// mTv_detail.setTextColor(this.getResources().getColor(R.color.originate_darkgreen));
	// mTv_discuss.setTextColor(this.getResources().getColor(R.color.black));
	// }

	/**
	 * 初始化Fragment
	 */
	// private void initFragment() {
	// listFragments = new ArrayList<BaseFragment>();
	// findChairDetailFragment =
	// FindChairDetailFragment.newInstance(FindChairDetailActivity.this);
	// findChairDetailFragment1 =
	// FindChairDetailFragment.newInstance(FindChairDetailActivity.this);
	//
	// listFragments.add(findChairDetailFragment);
	// listFragments.add(findChairDetailFragment1);
	//
	// BaseFragmentPagerAdapter mAdapetr = new
	// BaseFragmentPagerAdapter(getSupportFragmentManager(), listFragments);
	// mViewPager.setAdapter(mAdapetr);
	// mViewPager.setOnPageChangeListener(pageListener);
	// mViewPager.setOffscreenPageLimit(2);
	// currentFragment = findChairDetailFragment;
	// }

	/**
	 * ViewPager切换监听方法
	 * */
	// public OnPageChangeListener pageListener = new OnPageChangeListener() {
	//
	// public void onPageScrollStateChanged(int arg0) {
	// }
	//
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// }
	//
	// public void onPageSelected(int position) {
	// clearPress();
	// mViewPager.setCurrentItem(position);
	// currentFragment = listFragments.get(position);
	// switch (position) {
	// case 0:
	// mIv_detail.setVisibility(View.VISIBLE);
	// mTv_detail.setTextColor(FindChairDetailActivity.this.getResources().getColor(R.color.originate_darkgreen));
	// UiHelper.ShowOneToast(FindChairDetailActivity.this, "0");
	//
	// break;
	// case 1:
	// mIv_discuss.setVisibility(View.VISIBLE);
	// mTv_discuss.setTextColor(FindChairDetailActivity.this.getResources().getColor(R.color.originate_darkgreen));
	// UiHelper.ShowOneToast(FindChairDetailActivity.this, "1");
	// break;
	// default:
	// break;
	// }
	// selectTab(position);
	// }
	// };

	// private void selectTab(int tab_postion) {
	// for (int i = 0; i < mHor_lin.getChildCount(); i++) {
	// View checkView = mHor_lin.getChildAt(tab_postion);
	// int k = checkView.getMeasuredWidth();
	// int l = checkView.getLeft();
	// }
	// }

	// private void clearPress() {
	//
	// mIv_detail.setVisibility(View.INVISIBLE);
	// mIv_discuss.setVisibility(View.INVISIBLE);
	//
	// mTv_detail.setTextColor(this.getResources().getColor(R.color.black));
	// mTv_discuss.setTextColor(this.getResources().getColor(R.color.black));
	// }

	private void getPopWindow() {

		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopupWindow();
		}
	}

	protected void initPopupWindow() {
		View popupWindow_view = this.getLayoutInflater().inflate(R.layout.activity_find_chairdetail_popwindow, null, false);

		popupWindow = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow_view.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});

		// 设置透明度
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.3f;
		getWindow().setAttributes(lp);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);

			}
		});
		// 显示窗口
		popupWindow.showAtLocation(this.findViewById(R.id.layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_left_iv:// 返回
			finish();
			break;
		case R.id.head_right_btn:
			getPopWindow();
			popupWindow.showAsDropDown(v);
			break;
		// case R.id.ll_detail:// 详情
		// mIv_detail.setVisibility(View.VISIBLE);
		// mViewPager.setCurrentItem(0);
		// break;
		// case R.id.ll_discuss:// 评论
		// mIv_discuss.setVisibility(View.VISIBLE);
		// mViewPager.setCurrentItem(1);
		// break;
		case R.id.activity_find_register_btn:// 我要报名
			Intent intent = new Intent(HotListDetailActivity.this, RegistrationSubmitActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("tddActivity", tddActivity);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void refresh() {
	}

	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
	}

	@Override
	public void OnFailResultListener(String mess) {
		if (progressDialog != null) {
			progressDialog.setContinueDialog(false);
			UiHelper.ShowOneToast(this, mess);
		}
	}
}
