/**
 * 
 */
package com.newland.wstdd.mine.managerpage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.bean.TddActivity;
import com.newland.wstdd.common.common.ActivityType;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.common.UrlManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.BitmapImageUtil;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.widget.PengTextView;
import com.newland.wstdd.find.categorylist.detail.FindChairDetailActivity;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.applyList.ManagerApplyListActivity;
import com.newland.wstdd.mine.beanrequest.DeleteActivityReq;
import com.newland.wstdd.mine.beanresponse.DeleteActivityRes;
import com.newland.wstdd.mine.beanresponse.TddUserCertificate;
import com.newland.wstdd.mine.managerpage.activitycode.ActivityCodeActivity;
import com.newland.wstdd.mine.managerpage.beanrequest.OnTddRecommendReq;
import com.newland.wstdd.mine.managerpage.beanrequest.OpenActivityPeoplesReq;
import com.newland.wstdd.mine.managerpage.beanrequest.RegistrationCheckReq;
import com.newland.wstdd.mine.managerpage.beanrequest.RegistrationStateReq;
import com.newland.wstdd.mine.managerpage.beanresponse.OnTddRecommendRes;
import com.newland.wstdd.mine.managerpage.beanresponse.OpenActivityPeoplesRes;
import com.newland.wstdd.mine.managerpage.beanresponse.RegistrationCheckRes;
import com.newland.wstdd.mine.managerpage.beanresponse.RegistrationStateRes;
import com.newland.wstdd.mine.managerpage.coversetting.ModifyCoverReq;
import com.newland.wstdd.mine.managerpage.coversetting.ModifyCoverRes;
import com.newland.wstdd.mine.managerpage.handle.ManagerPagerSingleActivityHandle;
import com.newland.wstdd.mine.managerpage.handle.ManagerpageHandle;
import com.newland.wstdd.mine.managerpage.ilike.LikeListActivity;
import com.newland.wstdd.mine.managerpage.multitext.MultiTextActivity;
import com.newland.wstdd.mine.twocode.beanresponse.WeiXinCodeImgRes;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.netutils.MessageUtil;
import com.newland.wstdd.netutils.WBResponse;
import com.newland.wstdd.originate.beanresponse.SingleActivityRes;
import com.newland.wstdd.originate.origateactivity.OriginateChairActivity;
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
 * 管理页面
 * 
 * @author H81 2015-11-10
 * 
 */
/**
 * @author H81 2015-11-12
 * 
 */

public class ManagerPageActivity extends BaseFragmentActivity implements OnPostListenerInterface, OnCheckedChangeListener, IWXAPIEventHandler {
	private static final String TAG = "ManagerPageActivity";//收集异常日志tag
	/**
	 * 封面设置
	 */
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private Bitmap localBitmap;
	private Bitmap bitmap;
	private AppContext appContext = AppContext.getAppContext();
	private static final String PHOTO_FILE_NAME = "cover.png";
	private File tempFile;
	private List<String> filePathList;// 保存本地图片的地址
	private Uri uriImg;// 获取到了图片的Uri
	HttpMultipartPostManagerActivity post;//
	private String coverStringUrl;// 封面设置
	private ModifyCoverRes modifyCoverRes;
	/**
	 * 分享
	 */
	private PopupWindow popupWindow;// 分享窗口
	// 微信
	private static final String appid = "wx1b84c30d9f380c89";// 微信的appid
	private IWXAPI wxApi;// 微信的API
	// QQ
	private Tencent mTencent;
	private static final String APP_ID = "1104957952";

	// 注册广播
	private IntentFilter filter;// 定一个广播接收过滤器
	private IntentFilter filter2;// 用于接收处理“活动详情”界面的广播
	private IntentFilter cancelRegistfilter;// 定一个广播接收过滤器

	private TextView activity_managerpage_activityType_tv;// 活动类型
	private TextView activity_managerpage_activityTitle_tv;// 活动标题

	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_edit;// 活动编辑
	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_detail;// 活动预览
	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_signlist;// 报名名单
	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_share;// 分享
	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_sign;// 群发消息
	private com.newland.wstdd.common.widget.PengTextView mActivity_managerpage_activitycode;// 活动二维码
	private LinearLayout fouritem_ll;// 四个子项的LinearLayout

	// 报名
	private LinearLayout mActivity_managerpage_signup_ll;
	private TextView mActivity_managerpage_signup_content_tv;
	private TextView mActivity_managerpage_signup_tv;
	// 评论
	private LinearLayout mActivity_managerpage_comment_ll;
	private TextView mActivity_managerpage_comment_content_tv;
	private TextView mActivity_managerpage_comment_tv;
	// 点赞
	private LinearLayout mActivity_managerpage_like_ll;
	private TextView mActivity_managerpage_like_content_tv;
	private TextView mActivity_managerpage_like_tv;
	// 阅读
	private TextView mActivity_managerpage_read_content_tv;
	// 支付方式
	/*
	 * private RelativeLayout mActivity_managerpage_payment_rl; private
	 * LinearLayout mActivity_managerpage_payment_content_ll; private TextView
	 * mActivity_managerpage_payment_cash_tv;// 货到付款 private TextView
	 * mActivity_managerpage_payment_online_tv;// 在线支付 private ImageView
	 * mActivity_managerpage_payment_line_iv;// 下划线 private ImageView
	 * mActivity_managerpage_payment_extendable_iv;// 下拉箭头 // 配送方式 private
	 * RelativeLayout mActivity_managerpage_distributionmethod_rl; private
	 * LinearLayout mActivity_managerpage_distributionmethod_content_ll; private
	 * TextView mActivity_managerpage_distributionmethod_self_tv;// 自提 private
	 * TextView mActivity_managerpage_distributionmethod_safe_tv;// 当面交易 private
	 * TextView mActivity_managerpage_distributionmethod_mail_tv;// 邮寄 private
	 * ImageView mActivity_managerpage_distributionmethod_line_iv;// 下划线 private
	 * ImageView mActivity_managerpage_distributionmethod_extendable_iv;
	 */

	/*
	 * private com.newland.wstdd.common.widget.PengTextView
	 * mActivity_managerpage_share_penttv;// 分享 private
	 * com.newland.wstdd.common.widget.PengTextView
	 * mActivity_managerpage_invite_penttv;// 邀请参与 private
	 * com.newland.wstdd.common.widget.PengTextView
	 * mActivity_managerpage_cover_penttv;// 设置封面 private
	 * com.newland.wstdd.common.widget.PengTextView
	 * mActivity_managerpage_secretary_penttv;// 设置团秘 private
	 * com.newland.wstdd.common.widget.PengTextView
	 * mActivity_managerpage_originateagain_penttv;// 再次发起 private
	 * com.newland.wstdd.common.widget.PengTextView
	 * mActivity_managerpage_exportlist_penttv;// 导出名单 private
	 * com.newland.wstdd.common.widget.PengTextView
	 * mActivity_managerpage_mass_penttv;// 群发消息 private
	 * com.newland.wstdd.common.widget.PengTextView
	 * mActivity_managerpage_delete_penttv;// 删除活动
	 */
	private LinearLayout sendMessLayout;// 群发消息给报名人员
	private LinearLayout coverLayout;// 设置封面
	private ImageView coverImageView;// 活动封面
	private CheckBox mActivity_managerpage_isneedcheck_cb;// 报名需要审核
	private CheckBox mActivity_managerpage_ispublishsignnum_cb;// 公开报名人数
	private CheckBox mActivity_managerpage_isnotification_cb;// 团员报名通知
	private CheckBox mActivity_managerpage_isrecommend_cb;// 想不想上团大大热点推荐
	/** 活动状态（报名中） */
	private LinearLayout mActivity_managerpage_activitystate_ll;
	private TextView mActivity_managerpage_activitystate_tv;
	private ImageView mActivity_managerpage_activitystate_iv;
	private LinearLayout isTD_ll;// 是团大or团秘  如果是通知类型的话
	private LinearLayout botttom_ll;// 底部（复制活动、删除活动）
	private TextView deleteTextView;// 删除活动
	private TextView copyTextView;//复制活动
	private PopupWindow statePopupWindow;
	/** 活动状态（报名中） */
	/*
	 * // 公开范围 private LinearLayout mActivity_managerpage_publicrange_ll;
	 * private TextView mActivity_managerpage_publicrange_tv; private ImageView
	 * mActivity_managerpage_publicrange_iv;
	 */

	// 服务器返回的相关操作
	OpenActivityPeoplesRes openActivityPeoplesRes;// 设置是否公开活动人员
	OnTddRecommendRes onTddRecommendRes;// 设置是否上团大大热搜
	RegistrationStateRes registrationStateRes;// 设置报名状态
	RegistrationCheckRes registrationCheckRes;// 报名是否需要通过审核
	DeleteActivityRes deleteActivityRes;// 删除活动
	ManagerpageHandle handler = new ManagerpageHandle(this);
	// 请求角色的网络请求，用于显示不同的界面布局
	SingleActivityRes singleActivityRes = new SingleActivityRes();// 是团大、团秘、团员
	ManagerPagerSingleActivityHandle singleActivityHandle = new ManagerPagerSingleActivityHandle(this);
	private int signRole = 9;// 报名用户角色 1.团长 2.团秘 9.团员
	private String isLeader = "false";// 是否是团大
	private TddActivity tddActivity;// 活动对象

	@Override
	protected void processMessage(Message msg) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		appContext = AppContext.getAppContext();
		setContentView(R.layout.activity_managerpage);
		
		 /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		// 分享
		// QQ
		final Context ctxContext = this.getApplicationContext();
		mTencent = Tencent.createInstance(APP_ID, ctxContext);
		mHandler = new Handler();

		// weixin
		wxApi = WXAPIFactory.createWXAPI(this, appid);
		wxApi.registerApp(appid);
		getIntentData();

		setTitle();// 设置标题栏
		initView();// 初始化控件
		showHideView(singleActivityRes.getTddActivity().getActivityType());// 根据活动类型
																			// 展示隐藏相关的控件
		//
		// 注册广播--编辑活动之后
		filter = new IntentFilter("RECEIVE_TDDACTIVITY");// 用来接收从这个界面出去之后回来之后的tddactivity
		registerReceiver(broadcastReceiver, filter);// 在对象broadcastReceiver中进行接收的相应处理
		
		filter2 = new IntentFilter("ManagerPageActivityRefresh");
		registerReceiver(broadcastReceiver2, filter2);
		// 取消报名
		cancelRegistfilter = new IntentFilter("ManagerPageActivityRefresh_Cancel_Regist");// 取消报名
		registerReceiver(cancelRegistBroadcastReceiver, cancelRegistfilter);// 在对象broadcastReceiver中进行接收的相应处理
		// 获取服务器的相关测试
		/*
		 * openActivityPeoples();// 是否公开活动的报名人数 onTddRecommend();// 是否上团大大热搜
		 * setRegistrationState();// 设置报名的状态 setRegistrationCheck();// 是否需要审核
		 */
		
		// 报名人数变化的广播
		IntentFilter intentFilter2 = new IntentFilter("SignedNumChange");
		registerReceiver(signedNumChangeReceiver, intentFilter2);
		
	}


	
	/**
	 * 接收传递过来的tddActivity数据
	 * 
	 */
	private void getIntentData() {
		singleActivityRes = (SingleActivityRes) getIntent().getSerializableExtra("singleActivityRes");
		tddActivity = singleActivityRes.getTddActivity();

		/*
		 * tddActivity = (TddActivity)
		 * getIntent().getSerializableExtra("tddActivity");
		 */
		// judgeRole(tddActivity.getActivityId());
	}

	/**
	 * 单个活动查询(判断角色、更新阅读数等)
	 * 
	 * @param activityId
	 * @return signRole 报名用户角色 1.团长 2.团秘 9.团员 Int,若userSignstate显示不参加，则null
	 */
	/*
	 * private void judgeRole(final String activityId){ super.refreshDialog();
	 * try { new Thread(){ public void run() { //需要发送一个request对象进行请求
	 * SingleActivityReq singleActivityReq = new SingleActivityReq();
	 * singleActivityReq.setActivityId(activityId); BaseMessageMgr mgr = new
	 * HandleNetMessageMgr(); RetMsg<SingleActivityRes> retMsg =
	 * mgr.getSingleActivityInfo(singleActivityReq); Message message = new
	 * Message(); message.what =
	 * ManagerPagerSingleActivityHandle.SINGLE_ACTIVITY; //访问服务器成功1，否则访问服务器失败 if
	 * (retMsg.getCode() == 1) { singleActivityRes = (SingleActivityRes)
	 * retMsg.getObj(); message.obj = singleActivityRes; }else { message.obj =
	 * retMsg.getMsg(); } singleActivityHandle.sendMessage(message); };
	 * }.start(); } catch (Exception e) { // TODO: handle exception }
	 * 
	 * }
	 */
	private void setTitle() {
		ImageView left_btn = (ImageView) findViewById(R.id.head_left_iv);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		// TextView right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("活动管理");
		if (left_btn != null) {
			left_btn.setVisibility(View.VISIBLE);
			left_btn.setOnClickListener(this);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		// if (right_tv != null) {
		// right_tv.setVisibility(View.VISIBLE);
		// right_tv.setText("签到");
		// right_tv.setTextColor(getResources().getColor(R.color.red));
		// right_tv.setOnClickListener(this);
		// }
	}

	public void initView() {
		coverImageView = (ImageView) findViewById(R.id.activity_manager_cover_iv);// 封面
		coverLayout = (LinearLayout) findViewById(R.id.activity_manager_cover_ll);
		coverLayout.setOnClickListener(this);
		deleteTextView = (TextView) findViewById(R.id.activity_managerpage_delete_activity_tv);
		deleteTextView.setOnClickListener(this);
		copyTextView = (TextView) findViewById(R.id.activity_manager_copyactivity_tv);
		activity_managerpage_activityType_tv = (TextView) findViewById(R.id.activity_managerpage_activityType_tv);
		activity_managerpage_activityTitle_tv = (TextView) findViewById(R.id.activity_managerpage_activityTitle_tv);
		mActivity_managerpage_edit = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_edit);
		mActivity_managerpage_detail = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_detail);
		mActivity_managerpage_signlist = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_signlist);
		mActivity_managerpage_share = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_share);
		mActivity_managerpage_sign = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_sign);
		mActivity_managerpage_activitycode = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.activity_managerpage_activitycode);
		mActivity_managerpage_signup_ll = (LinearLayout) findViewById(R.id.activity_managerpage_signup_ll);
		mActivity_managerpage_signup_content_tv = (TextView) findViewById(R.id.activity_managerpage_signup_content_tv);
		mActivity_managerpage_signup_tv = (TextView) findViewById(R.id.activity_managerpage_signup_tv);
		mActivity_managerpage_comment_ll = (LinearLayout) findViewById(R.id.activity_managerpage_comment_ll);
		mActivity_managerpage_comment_content_tv = (TextView) findViewById(R.id.activity_managerpage_comment_content_tv);
		mActivity_managerpage_comment_tv = (TextView) findViewById(R.id.activity_managerpage_comment_tv);
		mActivity_managerpage_like_ll = (LinearLayout) findViewById(R.id.activity_managerpage_like_ll);
		mActivity_managerpage_like_content_tv = (TextView) findViewById(R.id.activity_managerpage_like_content_tv);
		mActivity_managerpage_like_tv = (TextView) findViewById(R.id.activity_managerpage_like_tv);
		mActivity_managerpage_read_content_tv = (TextView) findViewById(R.id.activity_managerpage_read_content_tv);
		mActivity_managerpage_activitystate_ll = (LinearLayout) findViewById(R.id.activity_managerpage_activitystate_ll);
		mActivity_managerpage_activitystate_tv = (TextView) findViewById(R.id.activity_managerpage_activitystate_tv);
		mActivity_managerpage_activitystate_iv = (ImageView) findViewById(R.id.activity_managerpage_activitystate_iv);
		mActivity_managerpage_isneedcheck_cb = (CheckBox) findViewById(R.id.activity_managerpage_isneedcheck_cb);
		mActivity_managerpage_ispublishsignnum_cb = (CheckBox) findViewById(R.id.activity_managerpage_ispublishsignnum_cb);
		mActivity_managerpage_isnotification_cb = (CheckBox) findViewById(R.id.activity_managerpage_isnotification_cb);
		mActivity_managerpage_isrecommend_cb = (CheckBox) findViewById(R.id.activity_managerpage_isrecommend_cb);
		fouritem_ll = (LinearLayout) findViewById(R.id.fouritem_ll);
		isTD_ll = (LinearLayout) findViewById(R.id.isTD_ll);
		botttom_ll = (LinearLayout) findViewById(R.id.botttom_ll);
		fouritem_ll.getLayoutParams().height = AppContext.getAppContext().getScreenWidth() / 4;
		sendMessLayout = (LinearLayout) findViewById(R.id.activity_manager_sendmess_ll);// 群发消息给报名人员
		sendMessLayout.setOnClickListener(this);
		initData();
		setClickListener();

		mActivity_managerpage_isneedcheck_cb.setOnCheckedChangeListener(this);
		mActivity_managerpage_ispublishsignnum_cb.setOnCheckedChangeListener(this);
		mActivity_managerpage_isnotification_cb.setOnCheckedChangeListener(this);
		mActivity_managerpage_isrecommend_cb.setOnCheckedChangeListener(this);

		initViewData();
	}

	/**
	 * 初始化界面数据
	 */
	private void initData() {
		activity_managerpage_activityType_tv.setText(StringUtil.intType2Str(tddActivity.getActivityType()));
		activity_managerpage_activityTitle_tv.setText(tddActivity.getActivityTitle());
		mActivity_managerpage_signup_content_tv.setText(tddActivity.getSignCount() + "");// 报名人数
		mActivity_managerpage_comment_content_tv.setText(tddActivity.getCommentCount() + "");// 评论
		mActivity_managerpage_like_content_tv.setText(tddActivity.getLikeCount() + "");// 点赞数量
		mActivity_managerpage_read_content_tv.setText(tddActivity.getViewCount() + "");// 阅读人数

		if (tddActivity.getStatus() == 1) {// status： 2 关闭报名，1开启报名
			mActivity_managerpage_activitystate_tv.setText("开启报名");
		} else if (tddActivity.getStatus() == 2) {
			mActivity_managerpage_activitystate_tv.setText("关闭报名");
		}
		if (tddActivity.getNeedAudit() == 1) {// 报名需要审核1.需要 2.不需要
			mActivity_managerpage_isneedcheck_cb.setSelected(true);
		} else if (tddActivity.getNeedAudit() == 2) {
			mActivity_managerpage_isneedcheck_cb.setSelected(false);
		} else if (tddActivity.getNeedPublicSigninfo() == 1) {// 是否公开报名情况
																// 1.公开2不公开
			mActivity_managerpage_ispublishsignnum_cb.setSelected(true);
		} else if (tddActivity.getNeedPublicSigninfo() == 2) {
			mActivity_managerpage_ispublishsignnum_cb.setSelected(false);
		}
		// TODO 缺少团员报名通知接口

		else if (StringUtil.isNotEmpty(tddActivity.getActivityId()) && StringUtil.isNotEmpty(tddActivity.getShareScope() + "") && StringUtil.isNotEmpty(tddActivity.getPublicWeight() + "")) {// 想不想上团大大热点推荐
			// 要上
			mActivity_managerpage_isrecommend_cb.setSelected(true);
		} else {
			mActivity_managerpage_isrecommend_cb.setSelected(false);
		}
	}

	/**
	 * 根据活动类型做出相应的控件的隐藏
	 * @param activityType
	 */
	private void showHideView(int activityType) {
		// TODO Auto-generated method stub
		if(ActivityType.typeNotification==activityType){
			isTD_ll.setVisibility(View.GONE);
			copyTextView.setVisibility(View.GONE);
		}
		
	}

	private void setClickListener() {
		mActivity_managerpage_edit.setOnClickListener(this);
		mActivity_managerpage_detail.setOnClickListener(this);
		mActivity_managerpage_signlist.setOnClickListener(this);
		mActivity_managerpage_share.setOnClickListener(this);
		mActivity_managerpage_sign.setOnClickListener(this);
		mActivity_managerpage_activitycode.setOnClickListener(this);
		/*
		 * mActivity_managerpage_signup_ll.setOnClickListener(this);
		 * mActivity_managerpage_comment_ll.setOnClickListener(this);
		 */
		mActivity_managerpage_like_ll.setOnClickListener(this);
		mActivity_managerpage_activitystate_ll.setOnClickListener(this);
		mActivity_managerpage_activitystate_tv.setOnClickListener(this);
		mActivity_managerpage_activitystate_iv.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		updateSignNum();
	}
	
	/**更新报名人数
	 * 
	 */
	private void updateSignNum() {
		int signedNum = 0;//报名人数
		if (activityIdString2 != null&&activityIdString2.equals(tddActivity.getActivityId())) {
			if ("add".equals(signNumTypeString)) {// 增加
				signedNum = tddActivity.getSignCount() + 1;
			} else  if ("del".equals(signNumTypeString) ){
				signedNum = tddActivity.getSignCount() - 1;
			}
			mActivity_managerpage_signup_content_tv.setText(signedNum + "");// 报名人数
		}
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.activity_managerpage_edit:// 活动编辑

			intent = new Intent(this, OriginateChairActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("tddActivity", tddActivity);
			bundle1.putString("activity_action", "edit");
			bundle1.putInt("activity_type", tddActivity.getActivityType());
			intent.putExtras(bundle1);
			startActivity(intent);
			break;
		case R.id.activity_managerpage_detail:// 活动详情
			intent = new Intent(this, FindChairDetailActivity.class);// 跳转到详情
			Bundle bundle2 = new Bundle();
			if (singleActivityRes != null) {
				bundle2.putSerializable("singleActivityRes", singleActivityRes);
			}
			intent.putExtras(bundle2);
			startActivity(intent);
			break;
		// TODO 报名名单
		case R.id.activity_managerpage_signlist:// 报名名单
			/*
			 * intent = new Intent(this, MineRegistrationListActivity.class);
			 * startActivity(intent);
			 */
			intent = new Intent(this, ManagerApplyListActivity.class);
			intent.putExtra("activityId", tddActivity.getActivityId());
			intent.putExtra("signRole", signRole);
			intent.putExtra("isLeader", isLeader);
			startActivity(intent);
			break;
		case R.id.activity_managerpage_share:// 分享
			getPopWindow();

			break;
		case R.id.activity_managerpage_sign:// 扫描签到
			UiHelper.ShowOneToast(this, "扫描签到");

			break;
		case R.id.activity_managerpage_activitycode:// 活动二维码
			intent = new Intent(this, ActivityCodeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("tddActivity", tddActivity);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.activity_managerpage_delete_activity_tv:// 删除活动的请求
			isDeleteCheck();

			break;
		/* *//**
		 * 报名
		 */
		/*
		 * case R.id.activity_managerpage_signup_ll:
		 * 
		 * break;
		 *//**
		 * 评论
		 */
		/*
		 * case R.id.activity_managerpage_comment_ll: intent = new Intent(this,
		 * MineRegistrationListActivity.class); startActivity(intent); break;
		 *//**
		 * 点赞
		 */
		case R.id.activity_managerpage_like_ll:
			intent = new Intent(this, LikeListActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("tddactivity", tddActivity);
			intent.putExtras(bundle3);
			startActivity(intent);
			break;
		/**
		 * 支付方式
		 */
		/*
		 * case R.id.activity_managerpage_payment_rl:
		 * UiHelper.ShowOneToast(this, "未开发"); if
		 * (mActivity_managerpage_payment_content_ll.getVisibility() ==
		 * View.VISIBLE) {
		 * mActivity_managerpage_payment_line_iv.setVisibility(View.GONE);
		 * mActivity_managerpage_payment_content_ll.setVisibility(View.GONE);
		 * mActivity_managerpage_payment_extendable_iv
		 * .setImageDrawable(getResources
		 * ().getDrawable(R.drawable.right_expansion)); } else if
		 * (mActivity_managerpage_payment_content_ll.getVisibility() ==
		 * View.GONE) {
		 * mActivity_managerpage_payment_line_iv.setVisibility(View.VISIBLE);
		 * mActivity_managerpage_payment_content_ll.setVisibility(View.VISIBLE);
		 * mActivity_managerpage_payment_extendable_iv
		 * .setImageDrawable(getResources
		 * ().getDrawable(R.drawable.down_expansion)); } break; case
		 * R.id.activity_managerpage_payment_cash_tv:
		 * setPaymentColor(R.id.activity_managerpage_payment_cash_tv); break;
		 * case R.id.activity_managerpage_payment_online_tv:
		 * setPaymentColor(R.id.activity_managerpage_payment_online_tv); break;
		 *//**
		 * 配送方式
		 */
		/*
		 * case R.id.activity_managerpage_distributionmethod_rl: if
		 * (mActivity_managerpage_distributionmethod_content_ll.getVisibility()
		 * == View.GONE) {
		 * mActivity_managerpage_distributionmethod_content_ll.setVisibility
		 * (View.VISIBLE);
		 * mActivity_managerpage_distributionmethod_line_iv.setVisibility
		 * (View.VISIBLE);
		 * mActivity_managerpage_distributionmethod_extendable_iv
		 * .setImageDrawable
		 * (getResources().getDrawable(R.drawable.down_expansion)); } else if
		 * (mActivity_managerpage_distributionmethod_content_ll.getVisibility()
		 * == View.VISIBLE) {
		 * mActivity_managerpage_distributionmethod_content_ll
		 * .setVisibility(View.GONE);
		 * mActivity_managerpage_distributionmethod_line_iv
		 * .setVisibility(View.GONE);
		 * mActivity_managerpage_distributionmethod_extendable_iv
		 * .setImageDrawable
		 * (getResources().getDrawable(R.drawable.right_expansion)); } break;
		 * case R.id.activity_managerpage_distributionmethod_self_tv:
		 * setDistributionMethodColor
		 * (R.id.activity_managerpage_distributionmethod_self_tv); break; case
		 * R.id.activity_managerpage_distributionmethod_safe_tv:
		 * setDistributionMethodColor
		 * (R.id.activity_managerpage_distributionmethod_safe_tv); break; case
		 * R.id.activity_managerpage_distributionmethod_mail_tv:
		 * setDistributionMethodColor
		 * (R.id.activity_managerpage_distributionmethod_mail_tv); break; case
		 * R.id.activity_managerpage_share_penttv:// 分享
		 * UiHelper.ShowOneToast(this, "未开发"); break; case
		 * R.id.activity_managerpage_invite_penttv:// 邀请参与
		 * UiHelper.ShowOneToast(this, "未开发"); break; case
		 * R.id.activity_managerpage_cover_penttv:// 设置封面
		 * UiHelper.ShowOneToast(this, "未开发"); break; case
		 * R.id.activity_managerpage_secretary_penttv:// 设置团秘
		 * UiHelper.ShowOneToast(this, "未开发"); break; case
		 * R.id.activity_managerpage_originateagain_penttv:// 再次发起
		 * UiHelper.ShowOneToast(this, "未开发"); break; case
		 * R.id.activity_managerpage_exportlist_penttv:// 导出名单
		 * UiHelper.ShowOneToast(this, "未开发"); break; case
		 * R.id.activity_managerpage_mass_penttv:// 群发消息
		 * UiHelper.ShowOneToast(this, "未开发"); break; case
		 * R.id.activity_managerpage_delete_penttv:// 删除活动
		 * UiHelper.ShowOneToast(this, "未开发"); break;
		 */
		/**
		 * 活动状态
		 */
		case R.id.activity_managerpage_activitystate_ll:
		case R.id.activity_managerpage_activitystate_tv:
		case R.id.activity_managerpage_activitystate_iv:
			getStatePopWind();// 活动状态的popwindow
			statePopupWindow.showAtLocation(this.findViewById(R.id.layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			break;
		/**
		 * 公开范围
		 */
		/*
		 * case R.id.activity_managerpage_publicrange_ll:
		 * UiHelper.ShowOneToast(this, "未开发"); break; case
		 * R.id.activity_managerpage_publicrange_tv: UiHelper.ShowOneToast(this,
		 * "未开发"); break; case R.id.activity_managerpage_publicrange_iv:
		 * UiHelper.ShowOneToast(this, "未开发"); break;
		 */
		case R.id.head_left_iv:// 返回
			finish();
			break;
		case R.id.activity_manager_sendmess_ll:// 群发消息
			intent = new Intent(this, MultiTextActivity.class);
			intent.putExtra("activityId", tddActivity.getActivityId());
			intent.putExtra("nickName", tddActivity.getNickName());
			startActivity(intent);

			break;
		case R.id.activity_manager_cover_ll:// 设置封面
			showDownLoadDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取活动状态popwindow
	 */
	private void getStatePopWind() {
		if (statePopupWindow != null) {
			statePopupWindow.dismiss();
			return;
		} else {
			initStatePopupWindow();
		}
	}

	/**
	 * 初始化活动状态的popwindow
	 */
	TextView closeApplyTextView, openApplyTextView;// 开启、关闭报名

	private void initStatePopupWindow() {
		// TODO
		View popupWindow_view1 = this.getLayoutInflater().inflate(R.layout.activity_manager_activitystate_popwindow, null, false);
		statePopupWindow = new PopupWindow(popupWindow_view1, AppContext.getAppContext().getScreenWidth(), LayoutParams.WRAP_CONTENT, true);
		statePopupWindow.setOutsideTouchable(true);// 点击区域外，关闭
		statePopupWindow.setTouchable(true);
		popupWindow_view1.setFocusable(true); // 这个很重要
		popupWindow_view1.setFocusableInTouchMode(true);
		popupWindow_view1.setOnKeyListener(new OnKeyListener() {
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
		popupWindow_view1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (statePopupWindow != null && statePopupWindow.isShowing()) {
					statePopupWindow.dismiss();
					statePopupWindow = null;
				}
				return false;
			}
		});
		closeApplyTextView = (TextView) popupWindow_view1.findViewById(R.id.close_apply_tv);
		openApplyTextView = (TextView) popupWindow_view1.findViewById(R.id.open_apply_tv);
		closeApplyTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openApplyTextView.setTextColor(getResources().getColor(R.color.grey_text));
				closeApplyTextView.setTextColor(getResources().getColor(R.color.black));
				closeApplyTextView.setTextSize(18);
				openApplyTextView.setTextSize(14);
				mActivity_managerpage_activitystate_tv.setText("关闭报名");
				mActivity_managerpage_activitystate_tv.setTextColor(getResources().getColor(R.color.textgray));
				if (statePopupWindow != null && statePopupWindow.isShowing()) {
					statePopupWindow.dismiss();
					statePopupWindow = null;
				}
			}
		});
		openApplyTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openApplyTextView.setTextColor(getResources().getColor(R.color.black));
				closeApplyTextView.setTextColor(getResources().getColor(R.color.grey_text));
				closeApplyTextView.setTextSize(14);
				openApplyTextView.setTextSize(18);
				mActivity_managerpage_activitystate_tv.setText("开启报名");
				mActivity_managerpage_activitystate_tv.setTextColor(getResources().getColor(R.color.textgray));
				if (statePopupWindow != null && statePopupWindow.isShowing()) {
					statePopupWindow.dismiss();
					statePopupWindow = null;
				}
			}
		});
		if (mActivity_managerpage_activitystate_tv.getText().toString().equals("开启报名")) {
			openApplyTextView.setTextColor(getResources().getColor(R.color.black));
			closeApplyTextView.setTextColor(getResources().getColor(R.color.grey_text));
			closeApplyTextView.setTextSize(14);
			openApplyTextView.setTextSize(18);
		} else {
			openApplyTextView.setTextColor(getResources().getColor(R.color.grey_text));
			closeApplyTextView.setTextColor(getResources().getColor(R.color.black));
			closeApplyTextView.setTextSize(18);
			openApplyTextView.setTextSize(14);
		}
		// 设置透明度
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.3f;
		getWindow().setAttributes(lp);
		statePopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
				setRegistrationState();

				// 发送广播通知报名状态已经更改
				Intent intent = new Intent();
				intent.setAction("StatusChange");
				intent.putExtra("activityId", tddActivity.getActivityId());
				intent.putExtra("status", mActivity_managerpage_activitystate_tv.getText().toString());
				sendBroadcast(intent);
			}
		});
		// statePopupWindow.setFocusable(true);

		statePopupWindow.update();
		// 显示窗口 位置
		statePopupWindow.showAtLocation(this.findViewById(R.id.layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	@Override
	public void refresh() {

	}

	/**
	 * 设置支付方式字体和边框颜色
	 * 
	 * @param id
	 *            控件id
	 */
	/*
	 * private void setPaymentColor(int id) { switch (id) { case
	 * R.id.activity_managerpage_payment_cash_tv:// 货到付款
	 * mActivity_managerpage_payment_cash_tv
	 * .setBackgroundDrawable(getResources()
	 * .getDrawable(R.drawable.text_rectangleredboderstyle));
	 * mActivity_managerpage_payment_cash_tv
	 * .setTextColor(getResources().getColor(R.color.red));
	 * mActivity_managerpage_payment_online_tv
	 * .setBackgroundDrawable(getResources
	 * ().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	 * mActivity_managerpage_payment_online_tv
	 * .setTextColor(getResources().getColor(R.color.black)); break; case
	 * R.id.activity_managerpage_payment_online_tv:// 在线支付
	 * mActivity_managerpage_payment_cash_tv
	 * .setBackgroundDrawable(getResources()
	 * .getDrawable(R.drawable.text_rectanglegrayboderstyle));
	 * mActivity_managerpage_payment_cash_tv
	 * .setTextColor(getResources().getColor(R.color.black));
	 * mActivity_managerpage_payment_online_tv
	 * .setBackgroundDrawable(getResources
	 * ().getDrawable(R.drawable.text_rectangleredboderstyle));
	 * mActivity_managerpage_payment_online_tv
	 * .setTextColor(getResources().getColor(R.color.red)); break; default:
	 * break; } }
	 */

	/**
	 * 设置配送方式字体和边框的颜色
	 * 
	 * @param id
	 */
	/*
	 * private void setDistributionMethodColor(int id) { switch (id) { case
	 * R.id.activity_managerpage_distributionmethod_self_tv:// 货到付款
	 * mActivity_managerpage_distributionmethod_self_tv
	 * .setBackgroundDrawable(getResources
	 * ().getDrawable(R.drawable.text_rectangleredboderstyle));
	 * mActivity_managerpage_distributionmethod_self_tv
	 * .setTextColor(getResources().getColor(R.color.red));
	 * mActivity_managerpage_distributionmethod_safe_tv
	 * .setBackgroundDrawable(getResources
	 * ().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	 * mActivity_managerpage_distributionmethod_safe_tv
	 * .setTextColor(getResources().getColor(R.color.black));
	 * mActivity_managerpage_distributionmethod_mail_tv
	 * .setBackgroundDrawable(getResources
	 * ().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	 * mActivity_managerpage_distributionmethod_mail_tv
	 * .setTextColor(getResources().getColor(R.color.black)); break; case
	 * R.id.activity_managerpage_distributionmethod_safe_tv:// 在线支付
	 * mActivity_managerpage_distributionmethod_self_tv
	 * .setBackgroundDrawable(getResources
	 * ().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	 * mActivity_managerpage_distributionmethod_self_tv
	 * .setTextColor(getResources().getColor(R.color.black));
	 * mActivity_managerpage_distributionmethod_safe_tv
	 * .setBackgroundDrawable(getResources
	 * ().getDrawable(R.drawable.text_rectangleredboderstyle));
	 * mActivity_managerpage_distributionmethod_safe_tv
	 * .setTextColor(getResources().getColor(R.color.red));
	 * mActivity_managerpage_distributionmethod_mail_tv
	 * .setBackgroundDrawable(getResources
	 * ().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	 * mActivity_managerpage_distributionmethod_mail_tv
	 * .setTextColor(getResources().getColor(R.color.black)); break; case
	 * R.id.activity_managerpage_distributionmethod_mail_tv:
	 * mActivity_managerpage_distributionmethod_self_tv
	 * .setBackgroundDrawable(getResources
	 * ().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	 * mActivity_managerpage_distributionmethod_self_tv
	 * .setTextColor(getResources().getColor(R.color.black));
	 * mActivity_managerpage_distributionmethod_safe_tv
	 * .setBackgroundDrawable(getResources
	 * ().getDrawable(R.drawable.text_rectanglegrayboderstyle));
	 * mActivity_managerpage_distributionmethod_safe_tv
	 * .setTextColor(getResources().getColor(R.color.black));
	 * mActivity_managerpage_distributionmethod_mail_tv
	 * .setBackgroundDrawable(getResources
	 * ().getDrawable(R.drawable.text_rectangleredboderstyle));
	 * mActivity_managerpage_distributionmethod_mail_tv
	 * .setTextColor(getResources().getColor(R.color.red)); break; default:
	 * break; } }
	 */

	/***
	 * 跟服务器的相关操作
	 */
	// 是否公开后动的报名人数
	private void openActivityPeoples() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					OpenActivityPeoplesReq reqInfo = new OpenActivityPeoplesReq();
					TddUserCertificate tddUserCertificate = new TddUserCertificate();

					if (mActivity_managerpage_ispublishsignnum_cb.isChecked()) {
						tddActivity.setNeedPublicSigninfo(1);// 公开
					} else {
						tddActivity.setNeedPublicSigninfo(2);// 关闭
					}
					tddUserCertificate.setUserId(tddActivity.getActivityId());
					tddUserCertificate.setUserName(tddActivity.getUserName());
					tddUserCertificate.setHeadimgurl(tddActivity.getHeadimgurl());
					tddUserCertificate.setNickName(tddActivity.getNickName());
					tddUserCertificate.setMobilePhone(tddActivity.getUserMobilePhone());
					// tddUserCertificate.setSex(tddActivity.gets);
					// tddUserCertificate.setEmail(tddActivity.get);
					// tddUserCertificate.setIdentity(identity);
					// tddUserCertificate.setCerStatus(cerStatus);

					reqInfo.setTddActivity(tddActivity);
					reqInfo.setTddUserCertificate(tddUserCertificate);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<OpenActivityPeoplesRes> ret = mgr.getOpenActivityPeoplesInfo(reqInfo, tddActivity.getActivityId());// 泛型类，
					Message message = new Message();
					message.what = ManagerpageHandle.OPENACTIVITY_PEOPLES;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						openActivityPeoplesRes = (OpenActivityPeoplesRes) ret.getObj();
						message.obj = openActivityPeoplesRes;
					} else {
						message.obj = ret.getMsg();
					}
					handler.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 设置是否上团大大热搜
	private void onTddRecommend() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					OnTddRecommendReq reqInfo = new OnTddRecommendReq();
					TddUserCertificate tddUserCertificate = new TddUserCertificate();

					if (mActivity_managerpage_isrecommend_cb.isChecked()) {
						tddActivity.setNeedAudit(1);
					} else {
						tddActivity.setNeedAudit(2);
					}
					tddUserCertificate.setUserId(tddActivity.getActivityId());
					tddUserCertificate.setUserName(tddActivity.getUserName());
					tddUserCertificate.setHeadimgurl(tddActivity.getHeadimgurl());
					tddUserCertificate.setNickName(tddActivity.getNickName());
					tddUserCertificate.setMobilePhone(tddActivity.getUserMobilePhone());
					reqInfo.setTddActivity(tddActivity);
					reqInfo.setTddUserCertificate(tddUserCertificate);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<OnTddRecommendRes> ret = mgr.getOnTddRecommendInfo(reqInfo, tddActivity.getActivityId());// 泛型类，
					Message message = new Message();
					message.what = ManagerpageHandle.ONTDD_RECOMMENT;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						onTddRecommendRes = (OnTddRecommendRes) ret.getObj();
						message.obj = onTddRecommendRes;
					} else {
						message.obj = ret.getMsg();
					}
					handler.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 删除活动
	private void deleteActivityRequest() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					DeleteActivityReq reqInfo = new DeleteActivityReq();
					reqInfo.setReason("没有理由，就是要删。");
					reqInfo.setStatus("-1");
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<DeleteActivityRes> ret = mgr.getDeleteActivityInfo(reqInfo, tddActivity.getActivityId());// 泛型类，
					Message message = new Message();
					message.what = ManagerpageHandle.DELETE_ACTIVITY;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						deleteActivityRes = (DeleteActivityRes) ret.getObj();
						message.obj = deleteActivityRes;
					} else {
						message.obj = ret.getMsg();
					}
					handler.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 设置报名的状态 关闭报名 开启报名
	private void setRegistrationState() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					RegistrationStateReq reqInfo = new RegistrationStateReq();
					TddUserCertificate tddUserCertificate = new TddUserCertificate();

					tddUserCertificate.setUserId(tddActivity.getActivityId());
					tddUserCertificate.setUserName(tddActivity.getUserName());
					tddUserCertificate.setHeadimgurl(tddActivity.getHeadimgurl());
					tddUserCertificate.setNickName(tddActivity.getNickName());
					tddUserCertificate.setMobilePhone(tddActivity.getUserMobilePhone());
					if (mActivity_managerpage_activitystate_tv != null) {
						if (mActivity_managerpage_activitystate_tv.getText().toString().equals("开启报名")) {
							tddActivity.setStatus(1);
						} else {
							tddActivity.setStatus(2);
						}
					}
					reqInfo.setTddActivity(tddActivity);
					reqInfo.setTddUserCertificate(tddUserCertificate);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<RegistrationStateRes> ret = mgr.getRegistrationStateInfo(reqInfo, tddActivity.getActivityId());// 泛型类，
					Message message = new Message();
					message.what = ManagerpageHandle.REGISTRATION_STATE;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						registrationStateRes = (RegistrationStateRes) ret.getObj();
						message.obj = registrationStateRes;
					} else {
						message.obj = ret.getMsg();
					}
					handler.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 是否需要审核
	private void setRegistrationCheck() {
		super.refreshDialog();
		// try {
		new Thread() {
			public void run() {
				// 需要发送一个request的对象进行请求
				RegistrationCheckReq reqInfo = new RegistrationCheckReq();
				TddUserCertificate tddUserCertificate = new TddUserCertificate();
				// 报名需要审核1.需要 2.不需要
				if (mActivity_managerpage_isneedcheck_cb.isChecked()) {
					tddActivity.setNeedAudit(1);
				} else {
					tddActivity.setNeedAudit(2);
				}
				tddUserCertificate.setUserId(tddActivity.getActivityId());
				tddUserCertificate.setUserName(tddActivity.getUserName());
				tddUserCertificate.setHeadimgurl(tddActivity.getHeadimgurl());
				tddUserCertificate.setNickName(tddActivity.getNickName());
				tddUserCertificate.setMobilePhone(tddActivity.getUserMobilePhone());
				reqInfo.setTddActivity(tddActivity);
				reqInfo.setTddUserCertificate(tddUserCertificate);
				BaseMessageMgr mgr = new HandleNetMessageMgr();
				RetMsg<RegistrationCheckRes> ret = mgr.getRegistrationCheckInfo(reqInfo, tddActivity.getActivityId());// 泛型类，
				Message message = new Message();
				message.what = ManagerpageHandle.REGISTRATION_CHECK;// 设置死
				// 访问服务器成功 1 否则访问服务器失败
				if (ret.getCode() == 1) {
					registrationCheckRes = (RegistrationCheckRes) ret.getObj();
					message.obj = registrationCheckRes;
				} else {
					message.obj = ret.getMsg();
				}
				handler.sendMessage(message);
			};
		}.start();
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.activity_managerpage_isneedcheck_cb:
			System.out.println("报名需要审核---" + isChecked);
			setRegistrationCheck();
			break;
		case R.id.activity_managerpage_ispublishsignnum_cb:
			System.out.println("公开报名人数---" + isChecked);
			openActivityPeoples();
			break;
		case R.id.activity_managerpage_isnotification_cb:
			System.out.println("团员报名通知----" + isChecked);
			break;
		case R.id.activity_managerpage_isrecommend_cb:
			System.out.println("想不想上团大大热点推荐----" + isChecked);
			onTddRecommend();
			break;
		default:
			break;
		}
	}

	// 服务器返回的处理
	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		try {

			switch (responseId) {
			// 是否公开活动报名人数
			case ManagerpageHandle.OPENACTIVITY_PEOPLES:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				openActivityPeoplesRes = (OpenActivityPeoplesRes) obj;
				if (openActivityPeoplesRes != null) {
					tddActivity = openActivityPeoplesRes.getTddActivity();
				}

				break;
			// 是否上团大大热搜
			case ManagerpageHandle.ONTDD_RECOMMENT:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				onTddRecommendRes = (OnTddRecommendRes) obj;
				if (onTddRecommendRes != null) {
					tddActivity = onTddRecommendRes.getTddActivity();
				}
				break;
			// 是否需要审核
			case ManagerpageHandle.REGISTRATION_CHECK:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				registrationCheckRes = (RegistrationCheckRes) obj;
				if (registrationCheckRes != null) {
					tddActivity = registrationCheckRes.getTddActivity();
				}
				break;
			case ManagerpageHandle.REGISTRATION_STATE:// 当前报名状态
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				registrationStateRes = (RegistrationStateRes) obj;
				if (registrationStateRes != null) {
					tddActivity = registrationStateRes.getTddActivity();
				}
				break;
			/*
			 * case
			 * ManagerPagerSingleActivityHandle.SINGLE_ACTIVITY://单个活动接口获取SignRole
			 * if (progressDialog != null) {
			 * progressDialog.setContinueDialog(false); } //
			 * UiHelper.ShowOneToast(this, "单个活动接口请求成功"); singleActivityRes =
			 * (SingleActivityRes) obj; if (singleActivityRes != null) {
			 * signRole = singleActivityRes.getSignRole(); isLeader =
			 * singleActivityRes.getIsLeader(); if(signRole == 2){
			 * isTD_ll.setVisibility(View.GONE);
			 * botttom_ll.setVisibility(View.GONE);
			 * mActivity_managerpage_detail.
			 * setDrawableTop(getResources().getDrawable
			 * (R.drawable.manager_activitydetail_gray));
			 * mActivity_managerpage_detail
			 * .setCompoundDrawablesWithIntrinsicBounds(null,
			 * mActivity_managerpage_detail.getDrawableTop(), null, null);
			 * mActivity_managerpage_detail
			 * .setTextColor(getResources().getColor(R.color.textgray));
			 * mActivity_managerpage_detail.setCompoundDrawablePadding(3);
			 * 
			 * mActivity_managerpage_detail.setClickable(false);
			 * 
			 * mActivity_managerpage_edit.setDrawableTop(getResources().getDrawable
			 * (R.drawable.manager_activityedit_gray));
			 * mActivity_managerpage_edit
			 * .setCompoundDrawablesWithIntrinsicBounds(null,
			 * mActivity_managerpage_edit.getDrawableTop(), null, null);
			 * mActivity_managerpage_edit
			 * .setTextColor(getResources().getColor(R.color.textgray));
			 * mActivity_managerpage_edit.setCompoundDrawablePadding(3);
			 * mActivity_managerpage_edit.setClickable(false);
			 * 
			 * setData(); }else if (isLeader.equals("true")) {
			 * isTD_ll.setVisibility(View.VISIBLE);
			 * botttom_ll.setVisibility(View.VISIBLE);
			 * 
			 * mActivity_managerpage_detail.setDrawableTop(getResources().
			 * getDrawable(R.drawable.manager_activitydetail));
			 * mActivity_managerpage_detail
			 * .setCompoundDrawablesWithIntrinsicBounds(null,
			 * mActivity_managerpage_detail.getDrawableTop(), null, null);
			 * mActivity_managerpage_detail.setClickable(true);
			 * mActivity_managerpage_detail.setCompoundDrawablePadding(3);
			 * 
			 * mActivity_managerpage_edit.setDrawableTop(getResources().getDrawable
			 * (R.drawable.manager_activityedit));
			 * mActivity_managerpage_edit.setCompoundDrawablesWithIntrinsicBounds
			 * (null, mActivity_managerpage_edit.getDrawableTop(), null, null);
			 * mActivity_managerpage_edit.setClickable(true);
			 * mActivity_managerpage_edit.setCompoundDrawablePadding(3);
			 * 
			 * setData(); }
			 * 
			 * //更新阅读数、报名数等数据 updateData(singleActivityRes); break; }
			 */

			case ManagerpageHandle.DELETE_ACTIVITY:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				deleteActivityRes = (DeleteActivityRes) obj;
				if (deleteActivityRes != null) {
					UiHelper.ShowOneToast(this, "删除成功");
					appContext.setMyAcNum(Integer.valueOf(appContext.getMyAcNum()) - 1 + "");
					Intent intent0 = new Intent();// 切记//
													// 这里的Action参数与IntentFilter添加的Action要一样才可以
					intent0.setAction("Refresh_MyActivityListActivity");
					sendBroadcast(intent0);// 发送广播了
					finish();
				}
				break;
			//设置封面
			case ManagerpageHandle.MODIFY_COVER:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				modifyCoverRes = (ModifyCoverRes) obj;
				if(modifyCoverRes!=null){
					singleActivityRes.setTddActivity(tddActivity);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 初始化布局、数值等数据
	 */
	private void initViewData() {
		if (singleActivityRes != null) {
			if (tddActivity != null) {
				// 这是封面
				ImageLoader.getInstance().displayImage(UrlManager.uploadToUrlServer + tddActivity.getImage1(), coverImageView, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}


					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						// TODO Auto-generated method stub
						localBitmap = arg2;
						coverImageView.setImageBitmap(localBitmap);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						// TODO Auto-generated method stub
						if (localBitmap != null) {
							coverImageView.setImageBitmap(localBitmap);
						} else {
							coverImageView.setImageDrawable(getResources().getDrawable(R.drawable.defaultphoto));
						}
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						if (localBitmap != null) {
							coverImageView.setImageBitmap(localBitmap);
						} else {
							coverImageView.setImageDrawable(getResources().getDrawable(R.drawable.defaultphoto));
						}
					}
				});
			}
			signRole = singleActivityRes.getSignRole();
			isLeader = singleActivityRes.getIsLeader();
			if (signRole == 2) {
				isTD_ll.setVisibility(View.GONE);
				botttom_ll.setVisibility(View.GONE);
				mActivity_managerpage_detail.setDrawableTop(getResources().getDrawable(R.drawable.manager_activitydetail_gray));
				mActivity_managerpage_detail.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_detail.getDrawableTop(), null, null);
				mActivity_managerpage_detail.setTextColor(getResources().getColor(R.color.textgray));
				mActivity_managerpage_detail.setCompoundDrawablePadding(3);

				mActivity_managerpage_detail.setClickable(false);

				mActivity_managerpage_edit.setDrawableTop(getResources().getDrawable(R.drawable.manager_activityedit_gray));
				mActivity_managerpage_edit.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_edit.getDrawableTop(), null, null);
				mActivity_managerpage_edit.setTextColor(getResources().getColor(R.color.textgray));
				mActivity_managerpage_edit.setCompoundDrawablePadding(3);
				mActivity_managerpage_edit.setClickable(false);

				setData();
			} else if (isLeader.equals("true")) {
				isTD_ll.setVisibility(View.VISIBLE);
				botttom_ll.setVisibility(View.VISIBLE);

				mActivity_managerpage_detail.setDrawableTop(getResources().getDrawable(R.drawable.manager_activitydetail));
				mActivity_managerpage_detail.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_detail.getDrawableTop(), null, null);
				mActivity_managerpage_detail.setClickable(true);
				mActivity_managerpage_detail.setCompoundDrawablePadding(3);

				mActivity_managerpage_edit.setDrawableTop(getResources().getDrawable(R.drawable.manager_activityedit));
				mActivity_managerpage_edit.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_edit.getDrawableTop(), null, null);
				mActivity_managerpage_edit.setClickable(true);
				mActivity_managerpage_edit.setCompoundDrawablePadding(3);

				setData();
			}

			// 更新阅读数、报名数等数据
			updateData(singleActivityRes);
		}
	}

	private void setData() {
		mActivity_managerpage_signlist.setDrawableTop(getResources().getDrawable(R.drawable.manager_signlist));
		mActivity_managerpage_signlist.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_signlist.getDrawableTop(), null, null);
		mActivity_managerpage_signlist.setClickable(true);
		mActivity_managerpage_signlist.setCompoundDrawablePadding(3);

		mActivity_managerpage_share.setDrawableTop(getResources().getDrawable(R.drawable.manager_share));
		mActivity_managerpage_share.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_share.getDrawableTop(), null, null);
		mActivity_managerpage_share.setClickable(true);
		mActivity_managerpage_share.setCompoundDrawablePadding(3);

		mActivity_managerpage_sign.setDrawableTop(getResources().getDrawable(R.drawable.manager_groupsend));
		mActivity_managerpage_sign.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_sign.getDrawableTop(), null, null);
		mActivity_managerpage_sign.setClickable(true);
		mActivity_managerpage_sign.setCompoundDrawablePadding(3);

		mActivity_managerpage_activitycode.setDrawableTop(getResources().getDrawable(R.drawable.manager_activitycode));
		mActivity_managerpage_activitycode.setCompoundDrawablesWithIntrinsicBounds(null, mActivity_managerpage_activitycode.getDrawableTop(), null, null);
		mActivity_managerpage_activitycode.setClickable(true);
		mActivity_managerpage_activitycode.setCompoundDrawablePadding(3);
	}

	/**
	 * 更新报名数、阅读数、评论数、点赞数
	 * 
	 * @param singleActivityRes2
	 * 
	 */
	private void updateData(SingleActivityRes singleActivityRes2) {
		if (singleActivityRes2.getTddActivity().getSignCount() != 0) {
			mActivity_managerpage_signup_content_tv.setText(singleActivityRes2.getTddActivity().getSignCount() + "");
		} else {
			mActivity_managerpage_signup_content_tv.setText("0");
		}
		if (singleActivityRes2.getTddActivity().getCommentCount() != 0) {
			mActivity_managerpage_comment_content_tv.setText(singleActivityRes2.getTddActivity().getCommentCount() + "");
		} else {
			mActivity_managerpage_comment_content_tv.setText("0");
		}
		if (singleActivityRes2.getTddActivity().getLikeCount() != 0) {
			mActivity_managerpage_like_content_tv.setText(singleActivityRes2.getTddActivity().getLikeCount() + "");
		} else {
			mActivity_managerpage_like_content_tv.setText("0");
		}
		if (singleActivityRes2.getTddActivity().getViewCount() != 0) {
			mActivity_managerpage_read_content_tv.setText(singleActivityRes2.getTddActivity().getViewCount() + "");
		} else {
			mActivity_managerpage_read_content_tv.setText("0");
		}

	}

	@Override
	public void OnFailResultListener(String mess) {
		if (progressDialog != null) {
			progressDialog.setContinueDialog(false);
			UiHelper.ShowOneToast(this, mess);
			coverImageView.setImageBitmap(localBitmap);
		}
	}

	/**
	 * 分享的相关操作
	 */
	public void friend(View v) {
		share(0);
	}

	public void friendline(View v) {
		share(1);
	}

	private void share(int flag) {
		downloadWeiXinImg(flag);

	}

	// 微信分享需要 先去下载封面的图片，然后才会分享出去
	private void downloadWeiXinImg(final int flag) {
		if (tddActivity.getShareContent() != null && tddActivity.getShareImg() != null && tddActivity.getShareUrl() != null) {

			ImageLoader.getInstance().loadImage(tddActivity.getShareImg(), new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String arg0, View arg1) {

				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// 下载失败
					WXWebpageObject webpage = new WXWebpageObject();
					webpage.webpageUrl = tddActivity.getShareUrl();
					WXMediaMessage msg = new WXMediaMessage(webpage);
					msg.title = tddActivity.getActivityTitle();
					msg.description = tddActivity.getActivityDescription();
					// 根据ImgUrl下载下来一张图片，弄出bitmap格式
					// 这里替换一张自己工程里的图片资源
					Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
					msg.setThumbImage(thumb);
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("webpage");
					req.message = msg;
					req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
					boolean fla = wxApi.sendReq(req);
					System.out.println("fla=" + fla);
				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
					// 表示下载成功了
					WXWebpageObject webpage = new WXWebpageObject();
					webpage.webpageUrl = tddActivity.getShareUrl();
					WXMediaMessage msg = new WXMediaMessage(webpage);
					msg.title = tddActivity.getActivityTitle();
					msg.description = tddActivity.getActivityDescription();
					// 根据ImgUrl下载下来一张图片，弄出bitmap格式
					// 这里替换一张自己工程里的图片资源
					Bitmap thumb = bitmap;
					msg.setThumbImage(thumb);
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("webpage");
					req.message = msg;
					req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
					boolean fla = wxApi.sendReq(req);
					System.out.println("fla=" + fla);
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {

				}
			});
		} else {
			UiHelper.ShowOneToast(this, "第三方分享的内容不能为空！！！");
			finish();
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp arg0) {
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
		mTencent.shareToQQ(this, params, new BaseUiListener() {
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
			doComplete(arg0);
		}
	}

	private Handler mHandler;

	// qq分享的结果处理
	private void showResult(final String base, final String msg) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// finish();//结束

				UiHelper.ShowOneToast(ManagerPageActivity.this, msg);
				// popupWindow.dismiss();
				// finish();
			}
		});
	}

	/**
	 * 设置窗口
	 */
	private void getPopWindow() {
		if (popupWindow != null) {
			popupWindow.dismiss();
			return;
		} else {
			initPopupWindow();
		}
	}

	/**
	 * 分享窗口初始化
	 */
	private void initPopupWindow() {
		View popupWindow_view = this.getLayoutInflater().inflate(R.layout.activity_originate_chair_popwindow, null, false);
		popupWindow = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setOutsideTouchable(true);// 点击区域外，关闭
		popupWindow.setTouchable(true);
		popupWindow_view.setFocusable(true); // 这个很重要
		popupWindow_view.setFocusableInTouchMode(true);
		// ColorDrawable dw = new ColorDrawable(0x00000000);
		// statePopupWindow.setBackgroundDrawable(dw);
		popupWindow_view.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						popupWindow = null;
					}
					return true;
				}
				return false;
			}
		});
		ImageView deleteImageView = (ImageView) popupWindow_view.findViewById(R.id.originate_chair_popwindow_image_delete);
		deleteImageView.setVisibility(View.GONE);
		Button cancelImageView;
		cancelImageView = (Button) popupWindow_view.findViewById(R.id.originate_chair_popwindow_delete);
		cancelImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
			}
		});
		// 隐藏一些需要隐藏的控件
		ImageView imageView1;
		TextView textView1, textView2, textView3;
		LinearLayout linearLayout1;
		imageView1 = (ImageView) popupWindow_view.findViewById(R.id.imageView1);
		textView1 = (TextView) popupWindow_view.findViewById(R.id.public_title_tv);
		textView2 = (TextView) popupWindow_view.findViewById(R.id.public_type_tv);
		textView3 = (TextView) popupWindow_view.findViewById(R.id.textView3);
		linearLayout1 = (LinearLayout) popupWindow_view.findViewById(R.id.linearLayout1);
		imageView1.setVisibility(View.GONE);
		textView1.setVisibility(View.GONE);
		textView2.setVisibility(View.GONE);
		textView3.setVisibility(View.GONE);
		linearLayout1.setVisibility(View.GONE);
		PengTextView weixinFriend, weixinZone, qqFriend;// 弹出窗口三个控件
		weixinFriend = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_weixin);
		weixinZone = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_weixin_zone);
		qqFriend = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_qq);
		// 微信好友分享
		weixinFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				friend(v);
			}
		});
		// 微信朋友圈分享
		weixinZone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				friendline(v);
			}
		});
		// qq好友分享
		qqFriend.setOnClickListener(new OnClickListener() {

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

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
		// 显示窗口 位置
		popupWindow.showAtLocation(this.findViewById(R.id.layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);//
	}

	/**
	 * 设置一个广播，用来接收activity
	 */
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			if ((TddActivity) bundle.getSerializable("tddactivity") != null)
				tddActivity = (TddActivity) bundle.getSerializable("tddactivity");
			singleActivityRes.setTddActivity(tddActivity);
			initData();

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (statePopupWindow != null && statePopupWindow.isShowing()) {
				statePopupWindow.dismiss();
				statePopupWindow = null;
			}
			finish();
			return true;
		}
		return false;
	}

	@Override
	public void onDestroy() {
		/** 收集异常日志 */
		LogManager.getManager(getApplicationContext()).unregisterActivity(this);
    	 /**收集异常日志*/
		unregisterReceiver(broadcastReceiver);
		unregisterReceiver(broadcastReceiver2);
		unregisterReceiver(signedNumChangeReceiver);
		super.onDestroy();
	}

	/**
	 * 设置广播，用于接收处理“活动详情”发送的广播
	 * 
	 */
	BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			/*
			 * Bundle bundle = intent.getExtras();
			 * judgeRole((String)bundle.getSerializable("activityId"));
			 */
			// judgeRole(tddActivity.getActivityId());
		}
	};
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
			} else {
				singleActivityRes.setUserSignstate("noSign");
			}

		}
	};

	/**
	 * 是否删除活动提示
	 */
	private void isDeleteCheck() {
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("是否删除此活动？").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				deleteActivityRequest();// 删除活动的请求
			}
		}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();
	}

	/**
	 * 封面相关的设置
	 */

	/**
	 * 头像选择对话框
	 */
	private void showDownLoadDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("选择获取二维码方式")
				.setPositiveButton("本地相册", new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(Intent.ACTION_PICK);
						intent.setType("image/*");
						startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

					}
				}).setNegativeButton("拍照上传", new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						// 判断存储卡是否可以用，可用进行存储
						if (hasSdcard()) {
							if (Environment.getExternalStorageDirectory()!=null) {
								System.out.println("不空");
							}
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
						}
						startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

					}
				}).show();
		dialog.setCanceledOnTouchOutside(false);
	}

	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				uriImg = uri;
				String pathString = BitmapImageUtil.getRealFilePath(this, uriImg);
				bitmap = BitmapImageUtil.getBitmapFromLocal(pathString);
				coverImageView.setImageBitmap(bitmap);
				if (bitmap != null) {
					// 进行上传的操作
					upload();
				}
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
				if (tempFile.exists()) {
					uriImg = Uri.fromFile(tempFile);
					String pathString = BitmapImageUtil.getRealFilePath(this, uriImg);
					bitmap = BitmapImageUtil.getBitmapFromLocal(pathString);
					coverImageView.setImageBitmap(bitmap);
					;
					if (bitmap != null) {
						// 进行上传的操作

						upload();
					}
				} else {
					if (tempFile != null && tempFile.exists()) {
						tempFile.delete();
					}
					UiHelper.ShowOneToast(this, "未执行拍照操作!");
					coverImageView.setImageBitmap(localBitmap);
				}
			} else {
				Toast.makeText(this, "未找到存储卡，无法存储照片！", 0).show();
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/*
	 * 上传图片
	 */
	public void upload() {

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir = Environment.getExternalStorageDirectory();
			// 得到一个路径，内容是sdcard的文件夹路径和名字
			String path = sdcardDir.getPath() + "/cardImages";
			File path1 = new File(path);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();
				this.setTitle("paht ok,path:" + path);
			}
		}
		// 构造方法1、2的参数
		filePathList = new ArrayList<String>();
		String aa = uriImg.toString();
		filePathList.add(BitmapImageUtil.getRealFilePath(this, uriImg));

		post = new HttpMultipartPostManagerActivity(this, filePathList);
		post.execute();
	}

	// 上传头像完之后的操作
	public void handleHeadImg(String imgMess) {
		WBResponse response = MessageUtil.JsonStrToWBResponse(imgMess, WeiXinCodeImgRes.class);
		if (response != null) {
			String msgString = response.getMsg();
			WeiXinCodeImgRes headImgRes = (WeiXinCodeImgRes) response.getRespBody();
			if (headImgRes != null && headImgRes.getFileUrls().size() > 0) {
				coverStringUrl = headImgRes.getFileUrls().get(0);
				if (bitmap != null) {
					localBitmap = bitmap;
				}
				if (tempFile != null && tempFile.exists()) {
					tempFile.delete();
				}
				modifyCover();
			}
		}
	}

	private void modifyCover() {
		// TODO Auto-generated method stub
		super.refreshDialog();
		progressDialog.setCancelable(false);
		try {
			new Thread() {
				public void run() {

					// 需要发送一个request的对象进行请求
					ModifyCoverReq reqInfo = new ModifyCoverReq();
					tddActivity.setImage1(coverStringUrl);
					reqInfo.setTddActivity(tddActivity);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<ModifyCoverRes> ret = mgr.getModifyCoverInfo(reqInfo);// 泛型类，
					Message message = new Message();
					message.what = ManagerpageHandle.MODIFY_COVER;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						modifyCoverRes = (ModifyCoverRes) ret.getObj();
						message.obj = modifyCoverRes;
					} else {
						message.obj = ret.getMsg();
					}
					handler.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	// 报名人数变化的广播
		private String activityIdString2;// 活动id
		private String signNumTypeString;// 报名人数增加还是减少
		BroadcastReceiver signedNumChangeReceiver = new BroadcastReceiver() {


			@Override
			public void onReceive(Context context, Intent intent) {
				activityIdString2 = intent.getStringExtra("activityId");
				signNumTypeString = intent.getStringExtra("signNumType");
			}
		};

}
