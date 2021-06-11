package com.newland.wstdd.find.categorylist.detail;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.bean.TddActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.smsphone.CallPhoneUtil;
import com.newland.wstdd.common.tools.EditTextUtil;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.tools.UiToUiHelper;
import com.newland.wstdd.common.updownloadimg.ImageDownLoad;
import com.newland.wstdd.common.widget.PengTextView;
import com.newland.wstdd.find.categorylist.detail.bean.CollectReq;
import com.newland.wstdd.find.categorylist.detail.bean.CollectRes;
import com.newland.wstdd.find.categorylist.detail.bean.IsLikeAndCollectReq;
import com.newland.wstdd.find.categorylist.detail.bean.IsLikeAndCollectRes;
import com.newland.wstdd.find.categorylist.detail.bean.LikeReq;
import com.newland.wstdd.find.categorylist.detail.bean.LikeRes;
import com.newland.wstdd.find.categorylist.detail.handle.SingleActivityDetailHandle;
import com.newland.wstdd.find.categorylist.registrationedit.editregistration.EditRegistrationEditActivity;
import com.newland.wstdd.find.categorylist.registrationedit.registration.RegistrationSubmitActivity;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.servicecenter.MineServiceCenterActivity;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.originate.beanrequest.SingleActivityReq;
import com.newland.wstdd.originate.beanresponse.SingleActivityRes;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 发现-- 8类数据（如：讲座报名、团购报名）
 *
 * @author H81 2015-11-6
 */
public class FindChairDetailActivity extends BaseFragmentActivity implements OnClickListener, OnPostListenerInterface, IWXAPIEventHandler {
    private static final String TAG = "FindChairDetailActivity";//收集异常日志tag
    /**
     * 分享相关的
     */

    // 微信
    private static final String appid = "wx1b84c30d9f380c89";// 微信的appid
    private IWXAPI wxApi;// 微信的API
    // QQ
    private Tencent mTencent;
    private static final String APP_ID = "1104957952";
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
    private RelativeLayout mLayout;
    private TextView mActivity_find_apply_title_tv;
    private TextView mActivity_find_apply_originatename_tv;
    private TextView mActivity_find_apply_originatetime_tv;
    private ImageView mActivity_find_apply_img_iv;
    private com.newland.wstdd.common.widget.PengTextView mActivity_find_chairtime_ptv;//时间
    private com.newland.wstdd.common.widget.PengTextView mActivity_find_chairaddress_ptv;//地点
    private TextView mActivity_find_chairsigncount_tv;//人数
    private com.newland.wstdd.common.widget.PengTextView mActivity_find_chairsign_limitcount_ptv;//限定人数

    private TextView mActivity_find_chairdetail_detail_tv;
    private ImageView[] mActivity_find_chairdetail_detail_iv = new ImageView[7];
    private TextView mActivity_find_chairdetail_detail_readingquantity_tv;
    private com.newland.wstdd.common.widget.PengTextView mActivity_call_td;
    private com.newland.wstdd.common.widget.PengTextView mActivity_find_collect;
    private com.newland.wstdd.common.widget.PengTextView mActivity_find_like;
    private Button mActivity_find_register_btn;// 我要报名
    ImageButton rightBtn;//返回

    int maxImgWidth;
    int maxImgHeight;

    // 活动是否点赞收藏
    private IsLikeAndCollectRes isLikeAndCollectRes;
    private IsLikeAndCollectHandle isLikeAndCollectHandle = new IsLikeAndCollectHandle(this);
    private String isCollectString;// 是否收藏
    private String isLikeString;// 是否点赞

    // 收藏
    private CollectRes collectRes;
    private String collectType = "1";// 操作（0 收藏 1 取消）
    private CollectHandle collecthandler = new CollectHandle(this);
    private String whichQuest;// 是什么请求 ：收藏/点赞/团大
    // 点赞
    private LikeRes likeRes;
    private String likeType = "1";// 操作（0 点赞 1 取消）
    private LikeHandle likehandler = new LikeHandle(this);

    /**
     * --------------接收数据 -------------
     */
    // 8类数据，单一对象
    TddActivity tddActivity;

    /** --------------接收数据 ------------- */

    /**
     * 注册广播，等报名结束之后，更新界面时候用的   或者编辑报名的时候使用
     */
    private IntentFilter filter;// 定一个广播接收过滤器
    private IntentFilter cancelRegistfilter;// 定一个广播接收过滤器-------------取消报名
    //服务器返回
    SingleActivityRes singleActivityRes;//服务器的返回信息
    SingleActivityDetailHandle handler = new SingleActivityDetailHandle(this);

    @Override
    protected void processMessage(Message msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮

        getIntentData();//获取单个活动的对象 tddactivity
        setContentView(R.layout.activity_find_chairdetail);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        initView();
        /**
         * 分享
         */
        // QQ
        final Context ctxContext = this.getApplicationContext();
        mTencent = Tencent.createInstance(APP_ID, ctxContext);
        mHandler = new Handler();
        // weixin
        wxApi = WXAPIFactory.createWXAPI(this, appid);
        wxApi.registerApp(appid);
        // initData();
        // initFragment();
        // 注册广播
        filter = new IntentFilter("Refresh_FindChairDetailActivity");//用来接收从这个界面出去之后回来之后的tddactivity
        registerReceiver(broadcastReceiver, filter);// 在对象broadcastReceiver中进行接收的相应处理

        // 取消报名
        cancelRegistfilter = new IntentFilter("ManagerPageActivityRefresh_Cancel_Regist");// 取消报名
        registerReceiver(cancelRegistBroadcastReceiver, cancelRegistfilter);// 在对象broadcastReceiver中进行接收的相应处理

        setTitle();//设置标题栏
        whichQuest = "isLikeAndCollect";
        refresh();
    }

    /**
     * 单个活动查询   用来判断   活动报名人数   是否已经报名 等等
     */
    private void singleActivitySearch() {
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {
                    // 需要发送一个request的对象进行请求
                    SingleActivityReq reqInfo = new SingleActivityReq();
                    reqInfo.setActivityId(tddActivity.getActivityId());

                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<SingleActivityRes> ret = mgr.getSingleActivityInfo(reqInfo);// 泛型类，																
                    Message message = new Message();
                    message.what = SingleActivityDetailHandle.SINGLE_ACTIVITY;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        singleActivityRes = (SingleActivityRes) ret.getObj();
                        message.obj = singleActivityRes;
                    } else {
                        message.obj = ret.getMsg();
                    }
                    handler.sendMessage(message);
                }

                ;
            }.start();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        /*tddActivity = (TddActivity) intent.getSerializableExtra("tddActivity");*/
        singleActivityRes = (SingleActivityRes) intent.getSerializableExtra("singleActivityRes");
        tddActivity = singleActivityRes.getTddActivity();
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
        rightBtn.setImageDrawable(getResources().getDrawable(R.drawable.share));
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


        mActivity_find_apply_title_tv = (TextView) findViewById(R.id.activity_find_apply_title_tv);
        mActivity_find_apply_originatename_tv = (TextView) findViewById(R.id.activity_find_apply_originatename_tv);
        mActivity_find_apply_originatetime_tv = (TextView) findViewById(R.id.activity_find_apply_originatetime_tv);
        mActivity_find_apply_img_iv = (ImageView) findViewById(R.id.activity_find_apply_img_iv);
        mActivity_find_chairtime_ptv = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_find_chairtime_ptv);
        mActivity_find_chairaddress_ptv = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_find_chairaddress_ptv);
        mActivity_find_chairsigncount_tv = (TextView) findViewById(R.id.activity_find_chairsigncount_tv);
        mActivity_find_chairsign_limitcount_ptv = (PengTextView) findViewById(R.id.activity_find_chairsign_limitcount_ptv);
        mActivity_find_chairdetail_detail_tv = (TextView) findViewById(R.id.activity_find_chairdetail_detail_tv);
        mActivity_find_chairdetail_detail_iv[0] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv1);
        mActivity_find_chairdetail_detail_iv[1] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv2);
        mActivity_find_chairdetail_detail_iv[2] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv3);
        mActivity_find_chairdetail_detail_iv[3] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv4);
        mActivity_find_chairdetail_detail_iv[4] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv5);
        mActivity_find_chairdetail_detail_iv[5] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv6);
        mActivity_find_chairdetail_detail_iv[6] = (ImageView) findViewById(R.id.activity_find_chairdetail_detail_iv7);
        mActivity_find_chairdetail_detail_readingquantity_tv = (TextView) findViewById(R.id.activity_find_chairdetail_detail_readingquantity_tv);
        mActivity_call_td = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_call_phone);
        mActivity_find_collect = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_find_collect);
        mActivity_find_like = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_find_like);

        mActivity_find_register_btn = (Button) findViewById(R.id.activity_find_register_btn);

        mActivity_call_td.setOnClickListener(this);
        mActivity_find_collect.setOnClickListener(this);
        mActivity_find_like.setOnClickListener(this);
        mActivity_find_register_btn.setOnClickListener(this);


        initActivityData();
        initDetailData();
    }

    /**
     * 初始化活动信息
     */
    private void initActivityData() {
        mActivity_find_apply_title_tv.setText(StringUtil.noNull(tddActivity.getActivityTitle()));
        mActivity_find_apply_originatename_tv.setText(StringUtil.noNull(tddActivity.getSponsor()));
        mActivity_find_apply_originatetime_tv.setText(StringUtil.noNull(tddActivity.getFriendActivityTime()));
        if (StringUtil.isNotEmpty(tddActivity.getImage1())) {

            ImageDownLoad.getDownLoadImg(tddActivity.getImage1(), mActivity_find_apply_img_iv);
        }
        mActivity_find_chairtime_ptv.setText(StringUtil.noNull(tddActivity.getActivityTime()));
        mActivity_find_chairaddress_ptv.setText(StringUtil.noNull(tddActivity.getActivityAddress()));
        mActivity_find_chairsigncount_tv.setText(StringUtil.noNull(tddActivity.getSignCount() + ""));
        if (tddActivity.getLimitPerson() != 0) {
            mActivity_find_chairsign_limitcount_ptv.setText(StringUtil.noNull("(限定" + tddActivity.getLimitPerson()) + "人)");
        } else {
            mActivity_find_chairsign_limitcount_ptv.setText("(人数不限)");
        }
    }

    /**
     * 初始化 点赞、收藏按钮
     */
    private void initIsLikeAndCollectData() {
        mActivity_call_td.setTextColor(getResources().getColor(R.color.textgray));
        mActivity_call_td.setDrawableTop(getResources().getDrawable(R.drawable.detailphone));
        mActivity_call_td.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_call_td.getDrawableTop(), null, null);
        mActivity_call_td.invalidate();
        if ("1".equals(isCollectString)) {// 1 已点赞 0未点赞
            mActivity_find_collect.setTextColor(getResources().getColor(R.color.red));
            mActivity_find_collect.setDrawableTop(getResources().getDrawable(R.drawable.detailcollect_red));
            mActivity_find_collect.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_find_collect.getDrawableTop(), null, null);
            mActivity_find_collect.invalidate();
        } else if ("0".equals(isCollectString)) {
            mActivity_find_collect.setTextColor(getResources().getColor(R.color.textgray));
            mActivity_find_collect.setDrawableTop(getResources().getDrawable(R.drawable.detailcollect));
            mActivity_find_collect.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_find_collect.getDrawableTop(), null, null);
        }
        if ("1".equals(isLikeString)) {// 1 已收藏 0未收藏
            mActivity_find_like.setTextColor(getResources().getColor(R.color.red));
            mActivity_find_like.setDrawableTop(getResources().getDrawable(R.drawable.detaillike_red));
            mActivity_find_like.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_find_like.getDrawableTop(), null, null);
        } else if ("0".equals(isLikeString)) {
            mActivity_find_like.setTextColor(getResources().getColor(R.color.textgray));
            mActivity_find_like.setDrawableTop(getResources().getDrawable(R.drawable.detaillike));
            mActivity_find_like.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_find_like.getDrawableTop(), null, null);
            mActivity_find_like.invalidate();
        }
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
        initViewData();
        String[] imgs = null;
        if (StringUtil.isNotEmpty(tddActivity.getImage())) {
            imgs = getImageList(tddActivity.getImage());
        }
        mActivity_find_chairdetail_detail_tv.setText(StringUtil.noNull(tddActivity.getActivityTitle()));
        mActivity_find_chairdetail_detail_readingquantity_tv.setText("阅读 " + tddActivity.getViewCount());

        int i = 0;
        try {
            if (imgs != null && imgs.length > 0) {
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


    }

    private void initViewData() {
        if (singleActivityRes != null) {
            //如果已经报名的话，需要跳转到编辑报名名单的界面
            if ("Sign".equals(singleActivityRes.getUserSignstate())) {
                mActivity_find_register_btn.setText("已报名");
                mActivity_find_register_btn.setBackgroundColor(getResources().getColor(R.color.textgray));
            }
//			UiHelper.ShowOneToast(this, "获取单个活动信息成功");
        }
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
     */
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
        } else {
            initPopupWindow();
        }
    }

    protected void initPopupWindow() {
        TextView weixinShareTv, weixinSNSShareTv, qqShareTv;//微信 微信朋友圈  qq分享
        TextView cancelTextView;//取消  就让框消失
        View popupWindow_view = this.getLayoutInflater().inflate(R.layout.activity_find_chairdetail_popwindow, null, false);
		/*TextView public_type_tv = (TextView) popupWindow_view.findViewById(R.id.public_type_tv);// 发布类型
		TextView public_title_tv = (TextView) popupWindow_view.findViewById(R.id.public_title_tv);// 发布主题
		public_type_tv.setText(StringUtil.intType2Str(tddActivity.getActivityType()));
		public_title_tv.setText(mActivity_find_apply_title_tv.getText().toString());*/
        popupWindow = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow_view.setFocusable(true); // 这个很重要  
        popupWindow_view.setFocusableInTouchMode(true);
        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow_view.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    popupWindow.dismiss();
                    popupWindow = null;
                    return true;
                }
                return false;
            }
        });
//		popupWindow_view.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View v, MotionEvent event) {
//				if (popupWindow != null && popupWindow.isShowing()) {
//					popupWindow.dismiss();
//					popupWindow = null;
//				}
//				return false;
//			}
//		});

        weixinShareTv = (TextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_weixin);
        weixinSNSShareTv = (TextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_weixin_sns);
        qqShareTv = (TextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_qq);
        cancelTextView = (TextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_cancel);
        cancelTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //微信分享
        weixinShareTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                weixin(v);
            }
        });
        //微信朋友圈分享
        weixinSNSShareTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                weixinfriend(v);
            }
        });
        //qq分享
        qqShareTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickShareToQQ();
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

                popupWindow = null;
            }
        });

        // 显示窗口
        popupWindow.showAtLocation(this.findViewById(R.id.layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

    }

    //监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_iv:// 返回
                sendBroadToManagerPageActivity();
                finish();
                break;
            case R.id.head_right_btn://分享
                getPopWindow();
                popupWindow.showAtLocation(FindChairDetailActivity.this.findViewById(R.id.layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            // case R.id.ll_detail:// 详情
            // mIv_detail.setVisibility(View.VISIBLE);
            // mViewPager.setCurrentItem(0);
            // break;
            // case R.id.ll_discuss:// 评论
            // mIv_discuss.setVisibility(View.VISIBLE);
            // mViewPager.setCurrentItem(1);
            // break;

            case R.id.activity_find_collect:// 收藏
                if (null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin()) || "false".equals(AppContext.getAppContext().getIsLogin())) {
//				UiHelper.ShowOneToast(FindChairDetailActivity.this, "该操作需要登录后进行！");
                    UiToUiHelper.showLogin(FindChairDetailActivity.this);
                } else if ("true".equals(AppContext.getAppContext().getIsLogin())) {

                    whichQuest = "collect";
                    refresh();
                }
                break;
            case R.id.activity_find_like:// 点赞
                if (null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin()) || "false".equals(AppContext.getAppContext().getIsLogin())) {
//				UiHelper.ShowOneToast(FindChairDetailActivity.this, "该操作需要登录后进行！");
                    UiToUiHelper.showLogin(FindChairDetailActivity.this);
                } else if ("true".equals(AppContext.getAppContext().getIsLogin())) {
//			UiHelper.ShowOneToast(FindChairDetailActivity.this, "点赞");
                    whichQuest = "like";
                    refresh();
                }
                break;
            case R.id.activity_find_register_btn:// 我要报名
                if (null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin()) || "false".equals(AppContext.getAppContext().getIsLogin())) {
//				UiHelper.ShowOneToast(FindChairDetailActivity.this, "该操作需要登录后进行！");
                    UiToUiHelper.showLogin(FindChairDetailActivity.this);
                } else if ("true".equals(AppContext.getAppContext().getIsLogin())) {
                    if ("已报名".equals(mActivity_find_register_btn.getText().toString())) {
                        //如果是已经报名的话，需要进行的操作是倒编辑报名的界面
                        Intent intent = new Intent(FindChairDetailActivity.this, EditRegistrationEditActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("singleActivity", singleActivityRes);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(FindChairDetailActivity.this, RegistrationSubmitActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("tddActivity", tddActivity);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }


                }
                break;
            case R.id.activity_call_phone://拨打电话
                if (singleActivityRes.getTddActivity() != null && singleActivityRes.getTddActivity().getUserMobilePhone() != null && !"".equals(singleActivityRes.getTddActivity().getUserMobilePhone())) {
                    AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("是否拨打" + singleActivityRes.getTddActivity().getUserMobilePhone()).setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            CallPhoneUtil.callPhone("singleActivityRes.getTddActivity().getUserMobilePhone()", FindChairDetailActivity.this);
                        }
                    }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                } else {
                    UiHelper.ShowOneToast(this, "错误号码");
                }


                break;
            default:
                break;
        }
    }

    //给团大大拨打电话
    private void callPhone() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");//动作名称
        intent.addCategory("android.intent.category.DEFAULT");//类别   注意这里不是set而是add，但是默认可省
//		intent.setData(Uri.parse("tel:" + "18094007487"));//数据   传给对方，根据"tell"+号码的数据格式匹配好，变成一个Uri对象发送出去（发送给系统）
        if (singleActivityRes.getTddActivity() != null && EditTextUtil.checkMobileNumber(singleActivityRes.getTddActivity().getUserMobilePhone())) {
            intent.setData(Uri.parse("tel:" + singleActivityRes.getTddActivity().getUserMobilePhone()));//数据   传给对方，根据"tell"+号码的数据格式匹配好，变成一个Uri对象发送出去（发送给系统）
            startActivity(intent);// 通过意图去调用系统的打电话的功能，方法内部会自动为Intent添加类别：android.intent.category.DEFAULT
        } else {
            UiHelper.ShowOneToast(this, "电话号码格式错误");
        }
    }

    @Override
    public void refresh() {
        // targetId 收藏目标活动Id type 操作（0 收藏 1 取消） target_title 收藏目标活动标题
        super.refreshDialog();
//		try {
        new Thread() {
            public void run() {
                // 需要发送一个request的对象进行请求
                if ("isLikeAndCollect".equals(whichQuest)) {// 活动是否点赞收藏
                    IsLikeAndCollectReq isLikeAndCollectReq = new IsLikeAndCollectReq();
                    isLikeAndCollectReq.setTargetId(tddActivity.getActivityId());
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<IsLikeAndCollectRes> ret = mgr.getIsLikeAndCollectInfo(isLikeAndCollectReq);
                    Message message = new Message();
                    message.what = IsLikeAndCollectHandle.ISLIKEANDCOLLECT;
                    // 访问服务器成功1否则访问服务器失败
                    if (ret.getCode() == 1) {
                        isLikeAndCollectRes = (IsLikeAndCollectRes) ret.getObj();
                        message.obj = isLikeAndCollectRes;
                    } else {
                        message.obj = ret.getMsg();
                    }
                    isLikeAndCollectHandle.sendMessage(message);
                } else if ("collect".equals(whichQuest)) {// 收藏请求
                    CollectReq collectReq = new CollectReq();
                    // collectReq.setTargetId(targetId);
                    // collectReq.setTarget_title(target_title);
                    collectReq.setTargetId(tddActivity.getActivityId());
                    collectReq.setTarget_title(tddActivity.getActivityTitle());
                    // if (collectType.equals("0")) {
                    // collectType = "1";
                    // } else if (collectType.equals("1")) {
                    // collectType = "0";
                    // }
                    collectType = "0";
                    collectReq.setType(collectType);

                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<CollectRes> ret = mgr.getCollectInfo(collectReq);// 泛型类，
                    Message message = new Message();
                    message.what = CollectHandle.COLLECT;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        collectRes = (CollectRes) ret.getObj();
                        message.obj = collectRes;
                    } else {
                        message.obj = ret.getMsg();
                    }
                    collecthandler.sendMessage(message);
                } else if ("like".equals(whichQuest)) {// 点赞请求
                    LikeReq likeReq = new LikeReq();
                    // collectReq.setTargetId(targetId);
                    // collectReq.setTarget_title(target_title);
                    likeReq.setTargetId(tddActivity.getActivityId());
                    // if (likeType.equals("0")) {
                    // likeType = "1";
                    // } else if (likeType.equals("1")) {
                    // likeType = "0";
                    // }
                    likeType = "0";
                    likeReq.setType(likeType);

                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<LikeRes> ret = mgr.getLikeInfo(likeReq);// 泛型类，
                    Message message = new Message();
                    message.what = LikeHandle.LIKE;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        likeRes = (LikeRes) ret.getObj();
                        message.obj = likeRes;
                    } else {
                        message.obj = ret.getMsg();
                    }
                    likehandler.sendMessage(message);
                }
            }
        }.start();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
    }

    @SuppressLint("NewApi")
    @Override
    public void OnHandleResultListener(Object obj, int responseId) {
        switch (responseId) {
            case IsLikeAndCollectHandle.ISLIKEANDCOLLECT:
                if (progressDialog != null) {
                    progressDialog.setContinueDialog(false);
                }
                isLikeAndCollectRes = (IsLikeAndCollectRes) obj;
                if (isLikeAndCollectRes != null) {
                    /**
                     * 获取到了数据
                     */
//				UiHelper.ShowOneToast(this, "获取数据成功");
                    isCollectString = isLikeAndCollectRes.getIsCollect();
                    isLikeString = isLikeAndCollectRes.getIsLike();
                    initIsLikeAndCollectData();
                }
                break;
            case CollectHandle.COLLECT:
                if (progressDialog != null) {
                    progressDialog.setContinueDialog(false);
                }
                collectRes = (CollectRes) obj;
                if (collectRes != null) {
                    /**
                     * 获取到了数据
                     */
//				UiHelper.ShowOneToast(this, "获取数据成功");
                    if ("0".equals(collectRes.getBack())) {// 0 收藏成功 1收藏取消
                        mActivity_find_collect.setTextColor(getResources().getColor(R.color.red));
                        mActivity_find_collect.setDrawableTop(getResources().getDrawable(R.drawable.detailcollect_red));
                        mActivity_find_collect.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_find_collect.getDrawableTop(), null, null);
                    } else if ("1".equals(collectRes.getBack())) {
                        mActivity_find_collect.setTextColor(getResources().getColor(R.color.textgray));
                        mActivity_find_collect.setDrawableTop(getResources().getDrawable(R.drawable.detailcollect));
                        mActivity_find_collect.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_find_collect.getDrawableTop(), null, null);
                    }
                    mActivity_find_collect.invalidate();
                }
                break;
            case LikeHandle.LIKE:
                if (progressDialog != null) {
                    progressDialog.setContinueDialog(false);
                }
                likeRes = (LikeRes) obj;
                if (likeRes != null) {
                    /**
                     * 获取到了数据
                     */
//				UiHelper.ShowOneToast(this, "获取数据成功");
                    if ("0".equals(likeRes.getBack())) {// 0 点赞成功 1点赞取消
                        mActivity_find_like.setTextColor(getResources().getColor(R.color.red));
                        mActivity_find_like.setDrawableTop(getResources().getDrawable(R.drawable.detaillike_red));
                        mActivity_find_like.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_find_like.getDrawableTop(), null, null);

                    } else if ("1".equals(likeRes.getBack())) {
                        mActivity_find_like.setTextColor(getResources().getColor(R.color.textgray));
                        mActivity_find_like.setDrawableTop(getResources().getDrawable(R.drawable.detaillike));
                        mActivity_find_like.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_find_like.getDrawableTop(), null, null);
                    }
                    mActivity_find_like.invalidate();
                }
                break;

            case SingleActivityDetailHandle.SINGLE_ACTIVITY:
                if (progressDialog != null) {
                    progressDialog.setContinueDialog(false);
                }
                singleActivityRes = (SingleActivityRes) obj;
                if (singleActivityRes != null) {
                    //如果已经报名的话，需要跳转到编辑报名名单的界面
                    if ("Sign".equals(singleActivityRes.getUserSignstate())) {
                        mActivity_find_register_btn.setText("已报名");
                        mActivity_find_register_btn.setBackgroundColor(getResources().getColor(R.color.textgray));
                        mActivity_find_chairsigncount_tv.setText(StringUtil.noNull(singleActivityRes.getTddActivity().getSignCount() + ""));
                    } else {
                        mActivity_find_chairsigncount_tv.setText(StringUtil.noNull(singleActivityRes.getTddActivity().getSignCount() + ""));
                    }
//				UiHelper.ShowOneToast(this, "获取单个活动信息成功");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void OnFailResultListener(String mess) {
        if (progressDialog != null) {
            progressDialog.setContinueDialog(false);
            UiHelper.ShowOneToast(this, mess);
        }
    }


    @Override
    public void onDestroy() {
        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /**收集异常日志*/

        unregisterReceiver(broadcastReceiver);
        // 回收 Bitmap
        try {
            unregisterReceiver(broadcastReceiver);
            unregisterReceiver(cancelRegistBroadcastReceiver);

            mActivity_find_apply_img_iv.getDrawingCache().recycle();
            for (int i = 0; i < mActivity_find_chairdetail_detail_iv.length; i++) {
                mActivity_find_chairdetail_detail_iv[i].getDrawingCache().recycle();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        super.onDestroy();
    }

    /**
     * 设置一个广播，用来接收activity
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @SuppressWarnings("unchecked")
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if ("已报名".equals(intent.getStringExtra("registration_state"))) {
                mActivity_find_register_btn.setText("已报名");
                mActivity_find_register_btn.setBackgroundColor(getResources().getColor(R.color.textgray));
            } else if ("我要报名".equals(intent.getStringExtra("registration_state"))) {
                mActivity_find_register_btn.setText("我要报名");
                mActivity_find_register_btn.setBackgroundColor(getResources().getColor(R.color.text_red));
            }
//			if(!"Sign".equals(singleActivityRes.getUserSignstate())){
//				mActivity_find_chairsigncount_tv.setText(StringUtil.noNull((tddActivity.getSignCount()+1)+""));
//			}
            /*	singleActivitySearch();//单个活动查询   用来判断   活动报名人数   是否已经报名 等等*/
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            } else {
                sendBroadToManagerPageActivity();
                finish();
            }

            return true;
        }
        return false;
    }

    /**
     * 分享相关的方法
     *
     * @param v
     */
    public void weixin(View v) {
        //微信好友分享
        share(0);
    }

    public void weixinfriend(View v) {
        //微信朋友圈分享
        share(1);
    }

    private void share(int flag) {
        downloadWeiXinImg(flag);

    }

    // 微信分享需要 先去下载封面的图片，然后才会分享出去
    private void downloadWeiXinImg(final int flag) {
        // TODO Auto-generated method stub
        if (tddActivity.getShareContent() != null
                && tddActivity.getShareImg() != null
                && tddActivity.getShareUrl() != null) {

            ImageLoader.getInstance().loadImage(tddActivity.getShareImg(),
                    new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String arg0, View arg1) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onLoadingFailed(String arg0, View arg1,
                                                    FailReason arg2) {
                            // TODO Auto-generated method stub
                            // 下载失败
                            WXWebpageObject webpage = new WXWebpageObject();
                            webpage.webpageUrl = tddActivity.getShareUrl();
                            WXMediaMessage msg = new WXMediaMessage(webpage);
                            msg.title = tddActivity.getActivityTitle();
                            msg.description = tddActivity
                                    .getActivityDescription();
                            // 根据ImgUrl下载下来一张图片，弄出bitmap格式
                            // 这里替换一张自己工程里的图片资源
                            Bitmap thumb = BitmapFactory.decodeResource(
                                    getResources(), R.drawable.ic_launcher);
                            msg.setThumbImage(thumb);
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("webpage");
                            req.message = msg;
                            req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                            boolean fla = wxApi.sendReq(req);
                            System.out.println("fla=" + fla);
                        }

                        @Override
                        public void onLoadingComplete(String arg0, View arg1,
                                                      Bitmap bitmap) {
                            // TODO Auto-generated method stub
                            // 表示下载成功了
                            WXWebpageObject webpage = new WXWebpageObject();
                            webpage.webpageUrl = tddActivity.getShareUrl();
                            WXMediaMessage msg = new WXMediaMessage(webpage);
                            msg.title = tddActivity.getActivityTitle();
                            msg.description = tddActivity
                                    .getActivityDescription();
                            // 根据ImgUrl下载下来一张图片，弄出bitmap格式
                            // 这里替换一张自己工程里的图片资源
                            Bitmap thumb = bitmap;
                            msg.setThumbImage(thumb);
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("webpage");
                            req.message = msg;
                            req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
                                    : SendMessageToWX.Req.WXSceneTimeline;
                            boolean fla = wxApi.sendReq(req);
                            System.out.println("fla=" + fla);
                        }

                        @Override
                        public void onLoadingCancelled(String arg0, View arg1) {
                            // TODO Auto-generated method stub

                        }
                    });
        } else {
            UiHelper.ShowOneToast(FindChairDetailActivity.this, "第三方分享的内容不能为空！！！");
            finish();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResp(BaseResp arg0) {
        // TODO Auto-generated method stub
        finish();
    }

    private void onClickShareToQQ() {
        Bundle b = getShareBundle();
        if (b != null) {
            shareParams = b;
            Thread thread = new Thread(shareThread);
            thread.start();
        }
    }

    private Bundle getShareBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("title", tddActivity.getActivityTitle());
        bundle.putString("imageUrl", tddActivity.getShareImg());
        bundle.putString("targetUrl", tddActivity.getShareUrl());
        bundle.putString("summary", tddActivity.getActivityDescription());
        bundle.putString("site", "1104957952");
        bundle.putString("appName", "我是TDD");
        return bundle;
    }

    Bundle shareParams = null;

    Handler shareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };

    // 线程类，该类使用匿名内部类的方式进行声明
    Runnable shareThread = new Runnable() {

        public void run() {
            doShareToQQ(shareParams);
            Message msg = shareHandler.obtainMessage();

            // 将Message对象加入到消息队列当中
            shareHandler.sendMessage(msg);

        }
    };

    private void doShareToQQ(Bundle params) {
        mTencent.shareToQQ(FindChairDetailActivity.this, params,
                new BaseUiListener() {
                    protected void doComplete(JSONObject values) {
                        showResult("shareToQQ:", "分享成功");
                    }

                    @Override
                    public void onError(UiError e) {
                        showResult("shareToQQ:", "分享失败未安装登陆第三方");
                    }

                    @Override
                    public void onCancel() {
                        showResult("shareToQQ", "分享取消");
                    }
                });
    }

    private class BaseUiListener implements IUiListener {

        // @Override
        // public void onComplete(JSONObject response) {
        // // mBaseMessageText.setText("onComplete:");
        // // mMessageText.setText(response.toString());
        // doComplete(response);
        // }

        protected void doComplete(Object values) {

        }

        @Override
        public void onError(UiError e) {
            showResult("onError:", "分享失败未安装登陆第三方");
        }

        @Override
        public void onCancel() {
            showResult("onCancel", "分享取消");
        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            doComplete(arg0);
        }
    }

    private Handler mHandler;

    // qq分享的结果处理
    private void showResult(final String base, final String msg) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                UiHelper.ShowOneToast(FindChairDetailActivity.this, msg);
//				popupWindow.dismiss();
//				finish();
            }
        });
    }

    public void sendBroadToManagerPageActivity() {
        Intent intent = new Intent();
        intent.setAction("ManagerPageActivityRefresh");
	/*	Bundle bundle  = new Bundle();
		if(singleActivityRes!=null&&singleActivityRes.getTddActivity()!=null)
		bundle.putSerializable("activityId", singleActivityRes.getTddActivity().getActivityId());
		intent.putExtras(bundle);*/
        sendBroadcast(intent);
    }

    /**
     * 设置一个广播，用来接收activity
     */
    BroadcastReceiver cancelRegistBroadcastReceiver = new BroadcastReceiver() {

        @SuppressWarnings("unchecked")
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String actionString = intent.getStringExtra("SignState");
            if ("Sign".equals(actionString)) {
                singleActivityRes.setUserSignstate("Sign");
                singleActivitySearch();
            } else {
                singleActivityRes.setUserSignstate("noSign");
                singleActivitySearch();
            }
        }
    };


}
