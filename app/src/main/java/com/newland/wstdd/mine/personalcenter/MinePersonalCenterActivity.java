/**
 * 
 */
package com.newland.wstdd.mine.personalcenter;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.common.UrlManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.EditTextUtil;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.updownloadimg.ImageDownLoad;
import com.newland.wstdd.common.widget.RelativeLayoutView;
import com.newland.wstdd.common.widget.RelativeLayoutView.KeyBordStateListener;
import com.newland.wstdd.common.widget.RoundImageView;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.login.beanresponse.HeadImgRes;
import com.newland.wstdd.mine.beanrequest.MinePersonInfoGetReq;
import com.newland.wstdd.mine.beanresponse.MinePersonInfoGetRes;
import com.newland.wstdd.mine.beanresponse.TddUserCertificate;
import com.newland.wstdd.mine.personalcenter.bean.MineEditPersonReq;
import com.newland.wstdd.mine.personalcenter.bean.MineEditPersonRes;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.netutils.MessageUtil;
import com.newland.wstdd.netutils.WBResponse;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 我的-个人信息
 * 
 * @author H81 2015-11-10
 * 
 */
public class MinePersonalCenterActivity extends BaseFragmentActivity implements OnPostListenerInterface, KeyBordStateListener

{
	private static final String TAG = "MinePersonalCenterActivity";//收集异常日志tag
	private RelativeLayoutView adjustLayoutView;// 调整软键盘
	private ScrollView adjustScrollview;// 滚动
	private View adjustView;// 显示隐藏 控件 让 其他控件可以显示出来
	// 状态栏的高度
	private int statusBarHeight;
	// 软键盘的高度
	private int keyboardHeight;
	// 软键盘的显示状态
	private boolean isShowKeyboard;
	/**
	 * 头像上传
	 */
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private Uri uriImg;// 获取到了图片的Uri
	private HttpMultipartPostPersonCenter post;
	private File tempFile;
	private List<String> filePathList;// 保存本地图片的地址
	private Bitmap localBitmap;
	private Bitmap bitmap;
	String headImgUrlString = null;// 头像地址
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	// 头像
	private TextView mActivity_mine_personalcenter_icon_tv;
	private ImageView mActivity_mine_personalcenter_icon_extendable_iv;
	private RoundImageView mActivity_mine_personalcenter_icon_iv;
	private RelativeLayout mActivity_mine_personalcenter_icon_rl;
	// 昵称
	private ImageView mActivity_mine_personalcenter_nickname_extendable_iv;
	private EditText mActivity_mine_personalcenter_nickname_content_edt;
	private LinearLayout mActivity_mine_personalcenter_nickname_ll;

	private TextView mActivity_mine_personalcenter_telnum_tv;// 手机
	// 我的二维码
	private ImageView mActivity_mine_personalcenter_qrcode_extendable_iv;
	private ImageView mActivity_mine_personalcenter_qrcode_iv;
	private RelativeLayout mActivity_mine_personalcenter_qrcode_rl;
	// 绑定账号
	private ImageView mActivity_mine_personalcenter_bindonaccount_extendable_iv;
	private RelativeLayout mActivity_mine_personalcenter_bindonaccount_rl;
	// 身份证
	private ImageView mActivity_mine_personalcenter_bindonaccount_qzone_iv;
	// private ImageView mActivity_mine_personalcenter_bindonaccount_xinlang_iv;
	private ImageView mActivity_mine_personalcenter_bindonaccount_weixin_iv;
	// 身份证
	private ImageView mActivity_mine_personalcenter_idcard_extendable_iv;
	private EditText mActivity_mine_personalcenter_idcard_content_edt;
	private LinearLayout mActivity_mine_personalcenter_idcard_ll;
	// 性别
	private ImageView mActivity_mine_personalcenter_sex_extendable_iv;
	private TextView mActivity_mine_personalcenter_sex_content_tv;
	private LinearLayout mActivity_mine_personalcenter_sex_ll;
	// 邮箱
	private ImageView mActivity_mine_personalcenter_mailbox_extendable_iv;
	private EditText mActivity_mine_personalcenter_mailbox_content_edt;
	private LinearLayout mActivity_mine_personalcenter_mailbox_ll;

	/** 服务器返回的信息 */
	MinePersonInfoGetRes minePersonInfoGetRes;
	private MineEditPersonRes mineEditPersonRes;
	MinePersonInfoGetHandle handler = new MinePersonInfoGetHandle(this);
	String myQucodeString;// 二维码url
	String nikeNameString;// 昵称
	String headImgUrl;// 头像url

	/** 服务器返回的信息 */

	// 注册广播 首先就需要有这个广播过滤器
	private IntentFilter filter;// 定一个广播接收过滤器

	@Override
	protected void processMessage(Message msg) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_mine_personalcenter);
		
		  /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		statusBarHeight = getStatusBarHeight(getApplicationContext());
		// 注册广播 更新绑定的情况
		filter = new IntentFilter("BIND_REMOVE_BIND");// 用来接收从这个界面出去之后回来之后的tddactivity
		registerReceiver(broadcastReceiver, filter);// 在对象broadcastReceiver中进行接收的相应处理
		setTitle();
		initView();
		refresh();

	}

	private void setTitle() {
		ImageView leftBtn = (ImageView) findViewById(R.id.head_left_iv);
		TextView centerTitle = (TextView) findViewById(R.id.head_center_title);
		TextView rightTv = (TextView) findViewById(R.id.head_right_tv);
		leftBtn.setVisibility(View.VISIBLE);
		centerTitle.setText("个人信息");
		rightTv.setVisibility(View.VISIBLE);
		rightTv.setText("保存");
		rightTv.setTextColor(getResources().getColor(R.color.red));
		rightTv.setOnClickListener(this);
		leftBtn.setOnClickListener(this);
	}

	public void initView() {
		// 调整软键盘
		adjustView = findViewById(R.id.adjust_view);
		adjustLayoutView = (RelativeLayoutView) findViewById(R.id.adjust_ll_layout);
		adjustLayoutView.setKeyBordStateListener(this);

		adjustScrollview = (ScrollView) findViewById(R.id.adjust_scrollview);
		mActivity_mine_personalcenter_icon_rl = (RelativeLayout) findViewById(R.id.activity_mine_personalcenter_icon_rl);
		mActivity_mine_personalcenter_icon_tv = (TextView) findViewById(R.id.activity_mine_personalcenter_icon_tv);
		mActivity_mine_personalcenter_icon_extendable_iv = (ImageView) findViewById(R.id.activity_mine_personalcenter_icon_extendable_iv);
		mActivity_mine_personalcenter_icon_iv = (RoundImageView) findViewById(R.id.activity_mine_personalcenter_icon_iv);
		mActivity_mine_personalcenter_nickname_ll = (LinearLayout) findViewById(R.id.activity_mine_personalcenter_nickname_ll);
		mActivity_mine_personalcenter_nickname_extendable_iv = (ImageView) findViewById(R.id.activity_mine_personalcenter_nickname_extendable_iv);
		mActivity_mine_personalcenter_nickname_content_edt = (EditText) findViewById(R.id.activity_mine_personalcenter_nickname_content_edt);
		mActivity_mine_personalcenter_telnum_tv = (TextView) findViewById(R.id.activity_mine_personalcenter_telnum_tv);
		mActivity_mine_personalcenter_qrcode_rl = (RelativeLayout) findViewById(R.id.activity_mine_personalcenter_qrcode_rl);
		mActivity_mine_personalcenter_qrcode_extendable_iv = (ImageView) findViewById(R.id.activity_mine_personalcenter_qrcode_extendable_iv);
		mActivity_mine_personalcenter_qrcode_iv = (ImageView) findViewById(R.id.activity_mine_personalcenter_qrcode_iv);
		mActivity_mine_personalcenter_bindonaccount_rl = (RelativeLayout) findViewById(R.id.activity_mine_personalcenter_bindonaccount_rl);
		mActivity_mine_personalcenter_bindonaccount_extendable_iv = (ImageView) findViewById(R.id.activity_mine_personalcenter_bindonaccount_extendable_iv);
		mActivity_mine_personalcenter_bindonaccount_qzone_iv = (ImageView) findViewById(R.id.activity_mine_personalcenter_bindonaccount_azone_iv);
		// mActivity_mine_personalcenter_bindonaccount_xinlang_iv = (ImageView)
		// findViewById(R.id.activity_mine_personalcenter_bindonaccount_xinlang_iv);
		mActivity_mine_personalcenter_bindonaccount_weixin_iv = (ImageView) findViewById(R.id.activity_mine_personalcenter_bindonaccount_weixin_iv);
		mActivity_mine_personalcenter_idcard_ll = (LinearLayout) findViewById(R.id.activity_mine_personalcenter_idcard_ll);
		mActivity_mine_personalcenter_idcard_extendable_iv = (ImageView) findViewById(R.id.activity_mine_personalcenter_idcard_extendable_iv);
		mActivity_mine_personalcenter_idcard_content_edt = (EditText) findViewById(R.id.activity_mine_personalcenter_idcard_content_edt);
		mActivity_mine_personalcenter_sex_ll = (LinearLayout) findViewById(R.id.activity_mine_personalcenter_sex_ll);
		mActivity_mine_personalcenter_sex_extendable_iv = (ImageView) findViewById(R.id.activity_mine_personalcenter_sex_extendable_iv);
		mActivity_mine_personalcenter_sex_content_tv = (TextView) findViewById(R.id.activity_mine_personalcenter_sex_content_tv);
		mActivity_mine_personalcenter_mailbox_ll = (LinearLayout) findViewById(R.id.activity_mine_personalcenter_mailbox_ll);
		mActivity_mine_personalcenter_mailbox_extendable_iv = (ImageView) findViewById(R.id.activity_mine_personalcenter_mailbox_extendable_iv);
		mActivity_mine_personalcenter_mailbox_content_edt = (EditText) findViewById(R.id.activity_mine_personalcenter_mailbox_content_edt);
		mActivity_mine_personalcenter_icon_rl.setOnClickListener(this);
		mActivity_mine_personalcenter_nickname_ll.setOnClickListener(this);
		mActivity_mine_personalcenter_qrcode_rl.setOnClickListener(this);
		mActivity_mine_personalcenter_bindonaccount_rl.setOnClickListener(this);
		mActivity_mine_personalcenter_idcard_ll.setOnClickListener(this);
		mActivity_mine_personalcenter_sex_ll.setOnClickListener(this);
		mActivity_mine_personalcenter_mailbox_content_edt.setOnClickListener(this);
		mActivity_mine_personalcenter_bindonaccount_qzone_iv.setImageDrawable(getResources().getDrawable(R.drawable.qzone));
		mActivity_mine_personalcenter_bindonaccount_weixin_iv.setImageDrawable(getResources().getDrawable(R.drawable.wechat));

		setData();
	}

	private void setData() {
		mActivity_mine_personalcenter_nickname_content_edt.setText(AppContext.getAppContext().getNickName());
		if (AppContext.getAppContext().getHeadImgUrl() != null && !"".equals(AppContext.getAppContext().getHeadImgUrl())) {
			
			ImageLoader.getInstance().displayImage(AppContext.getAppContext().getHeadImgUrl(), mActivity_mine_personalcenter_icon_iv,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					if(localBitmap!=null){
						mActivity_mine_personalcenter_icon_iv.setImageBitmap(localBitmap);
					}else {
						mActivity_mine_personalcenter_icon_iv.setImageDrawable(getResources().getDrawable(R.drawable.default_head_icon));
					}
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					localBitmap = arg2;
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					if(localBitmap!=null){
						mActivity_mine_personalcenter_icon_iv.setImageBitmap(localBitmap);
					}else {
						mActivity_mine_personalcenter_icon_iv.setImageDrawable(getResources().getDrawable(R.drawable.default_head_icon));
					}
				}
			});
//			ImageDownLoad.getDownLoadSmallImg(AppContext.getAppContext().getHeadImgUrl(), mActivity_mine_personalcenter_icon_iv);
			
		} else {
			mActivity_mine_personalcenter_icon_iv.setImageResource(R.drawable.default_head_icon);
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.head_left_iv:
			this.finish();
			break;
		case R.id.head_right_tv:// 保存
			// 先进行头像上传 然后再提交编辑的信息
			if (!"".equals(uriImg) && uriImg != null) {
				upload();
			} else {
				if (judgeDataFormat()) {
					editPersonalInformation();
				}
			}

			break;
		case R.id.activity_mine_personalcenter_icon_rl:// 头像
			// super.refreshDialog();
			showDownLoadDialog();// 显示 头像
			break;
		case R.id.activity_mine_personalcenter_nickname_ll:// 昵称

			break;
		case R.id.activity_mine_personalcenter_qrcode_rl:// 我的二维码
			intent = new Intent(this, MineMyQRCodeActivity.class);
			intent.putExtra("nikeNameString", nikeNameString);
			intent.putExtra("headImgUrl", headImgUrl);
			intent.putExtra("myQucodeString", myQucodeString);
			startActivity(intent);
			break;
		case R.id.activity_mine_personalcenter_bindonaccount_rl:// 绑定账号
			intent = new Intent(this, RemoveBindActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("MinePersonInfoGetRes", minePersonInfoGetRes);
			intent.putExtras(bundle);
			startActivity(intent);

			break;
		case R.id.activity_mine_personalcenter_idcard_ll:// 身份证
			// UiHelper.ShowOneToast(this, "未开发");
			break;
		case R.id.activity_mine_personalcenter_sex_ll:// 性别
			getStatePopWind();// 性别popwindow
			statePopupWindow.showAsDropDown(v);
			break;
		case R.id.activity_mine_personalcenter_mailbox_content_edt:// 邮箱

			mActivity_mine_personalcenter_mailbox_content_edt.requestFocus();
			break;
		default:
			break;
		}
	}

	private boolean judgeDataFormat() {

		boolean flag = true;
		if (mActivity_mine_personalcenter_idcard_content_edt.getText().toString() != null && !"".equals(mActivity_mine_personalcenter_idcard_content_edt.getText().toString())) {
			if (!StringUtil.isPid(mActivity_mine_personalcenter_idcard_content_edt.getText().toString())) {
				flag = false;
				UiHelper.ShowOneToast(this, "身份证格式不对");
			} else {
				flag = true;
			}
		}
		if (mActivity_mine_personalcenter_mailbox_content_edt.getText().toString() != null && !"".equals(mActivity_mine_personalcenter_mailbox_content_edt.getText().toString())) {
			if (!EditTextUtil.checkEmail(mActivity_mine_personalcenter_mailbox_content_edt.getText().toString())) {
				flag = false;
				UiHelper.ShowOneToast(this, "邮箱格式不对");
			} else {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 个人信息编辑
	 * 
	 */
	private void editPersonalInformation() {
		super.refreshDialog();
		progressDialog.setCancelable(false);
		try {
			new Thread() {
				public void run() {
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					MineEditPersonReq reqInfo = new MineEditPersonReq();

					TddUserCertificate tddUserCertificate = new TddUserCertificate();
					// 8个元素
					tddUserCertificate.setCerStatus(1);// 状态 1.未认证 30.初步认证
														// 90.身份认证
					tddUserCertificate.setEmail(mActivity_mine_personalcenter_mailbox_content_edt.getText().toString());
					tddUserCertificate.setHeadimgurl(AppContext.getAppContext().getHeadImgUrl());
					tddUserCertificate.setMobilePhone(StringUtil.noNull(mActivity_mine_personalcenter_telnum_tv.getText().toString()));
					if (StringUtil.isNotEmpty(mActivity_mine_personalcenter_nickname_content_edt.getText().toString())) {
						tddUserCertificate.setNickName(mActivity_mine_personalcenter_nickname_content_edt.getText().toString());
					} else {
						tddUserCertificate.setNickName(AppContext.getAppContext().getNickName());
					}
					tddUserCertificate.setSex(StringUtil.getIntSex(mActivity_mine_personalcenter_sex_content_tv.getText().toString()));
					tddUserCertificate.setUserId(AppContext.getAppContext().getUserId());
					if (StringUtil.isNotEmpty(mActivity_mine_personalcenter_idcard_content_edt.getText().toString())) {
						if (StringUtil.isPid(mActivity_mine_personalcenter_idcard_content_edt.getText().toString())) {
							tddUserCertificate.setIdentity(mActivity_mine_personalcenter_idcard_content_edt.getText().toString());

							reqInfo.setTddUserCertificate(tddUserCertificate);
							RetMsg<MineEditPersonRes> ret = mgr.getMineEditPersonInfo(reqInfo);// 泛型类，
							Message message = new Message();
							message.what = MinePersonInfoGetHandle.PERSON_EDIT_INFO;// 设置死
							// 访问服务器成功 1 否则访问服务器失败
							if (ret.getCode() == 1) {
								mineEditPersonRes = (MineEditPersonRes) ret.getObj();
								message.obj = mineEditPersonRes;
							} else {
								message.obj = ret.getMsg();
							}
							handler.sendMessage(message);
						} else {
							UiHelper.ShowOneToast(MinePersonalCenterActivity.this, "身份证格式有误");
						}
					} else {
						tddUserCertificate.setIdentity(mActivity_mine_personalcenter_idcard_content_edt.getText().toString());

						reqInfo.setTddUserCertificate(tddUserCertificate);
						RetMsg<MineEditPersonRes> ret = mgr.getMineEditPersonInfo(reqInfo);// 泛型类，
						Message message = new Message();
						message.what = MinePersonInfoGetHandle.PERSON_EDIT_INFO;// 设置死
						// 访问服务器成功 1 否则访问服务器失败
						if (ret.getCode() == 1) {
							mineEditPersonRes = (MineEditPersonRes) ret.getObj();
							message.obj = mineEditPersonRes;
						} else {
							message.obj = ret.getMsg();
						}
						handler.sendMessage(message);
					}

				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/** 个人信息获取 */
	@Override
	public void refresh() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					MinePersonInfoGetReq reqInfo = new MinePersonInfoGetReq();
					reqInfo.setUserId(AppContext.getAppContext().getUserId());
					RetMsg<MinePersonInfoGetRes> ret = mgr.getMinePersonInfoGetInfoMsg(reqInfo);// 泛型类，
					Message message = new Message();
					message.what = MinePersonInfoGetHandle.PERSON_INFO_GET;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						minePersonInfoGetRes = (MinePersonInfoGetRes) ret.getObj();
						message.obj = minePersonInfoGetRes;
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

	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		try {
			switch (responseId) {
			case MinePersonInfoGetHandle.PERSON_INFO_GET:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				minePersonInfoGetRes = (MinePersonInfoGetRes) obj;
				if (minePersonInfoGetRes != null) {
					// UiHelper.ShowOneToast(MinePersonalCenterActivity.this,
					// "发现界面请求成功");

					// 个人信息
					TddUserCertificate tddUserCertificate = minePersonInfoGetRes.getTddUserCertificate();
					if (tddUserCertificate != null) {
						settddUserCertificate(tddUserCertificate);
					}
					myQucodeString = minePersonInfoGetRes.getMyQucode();// 二维码

					// 是否绑定qq、微信、新浪微博
					String qqOpenIdString = minePersonInfoGetRes.getQq_open_id();
					String weChatUnionIdString = minePersonInfoGetRes.getWechat_union_id();
					String weiboOpenIdString = minePersonInfoGetRes.getWeibo_open_id();
					if (StringUtil.isNotEmpty(qqOpenIdString)) {// qq绑定
						mActivity_mine_personalcenter_bindonaccount_qzone_iv.setImageDrawable(getResources().getDrawable(R.drawable.qzonebound));
					} else {
						mActivity_mine_personalcenter_bindonaccount_qzone_iv.setImageDrawable(getResources().getDrawable(R.drawable.qzone));
					}
					if (StringUtil.isNotEmpty(weChatUnionIdString)) {// 微信绑定
						mActivity_mine_personalcenter_bindonaccount_weixin_iv.setImageDrawable(getResources().getDrawable(R.drawable.wechatbound));
					} else {
						mActivity_mine_personalcenter_bindonaccount_weixin_iv.setImageDrawable(getResources().getDrawable(R.drawable.wechat));
					}
					// if (StringUtil.isNotEmpty(weiboOpenIdString)) {// 微博绑定
					// mActivity_mine_personalcenter_bindonaccount_xinlang_iv.setImageDrawable(getResources().getDrawable(R.drawable.weibobound));
					// } else {
					// mActivity_mine_personalcenter_bindonaccount_xinlang_iv.setImageDrawable(getResources().getDrawable(R.drawable.weibo));
					// }
					initBindOrRemove();

				}
				break;
			// 编辑个人信息
			case MinePersonInfoGetHandle.PERSON_EDIT_INFO:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
					finish();
				}
				mineEditPersonRes = (MineEditPersonRes) obj;
				if (mineEditPersonRes != null) {
//					UiHelper.ShowOneToast(this, mineEditPersonRes.getGetResMess());
				}
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			onKeyBoardHide();
		}

	}

	/**
	 * 设置个人消息
	 * 
	 * @param tddUserCertificate
	 */
	private void settddUserCertificate(TddUserCertificate tddUserCertificate) {
		if (tddUserCertificate != null) {
			// 头像
			if (StringUtil.isNotEmpty(tddUserCertificate.getHeadimgurl())) {
				ImageDownLoad.getDownLoadCircleImg(tddUserCertificate.getHeadimgurl(), mActivity_mine_personalcenter_icon_iv);

				headImgUrl = tddUserCertificate.getHeadimgurl();// 我的二维码头像
			} else {
				mActivity_mine_personalcenter_icon_iv.setImageDrawable(getResources().getDrawable(R.drawable.defaultheadimg));
			}
			// 昵称
			if (StringUtil.isNotEmpty(tddUserCertificate.getNickName())) {
				mActivity_mine_personalcenter_nickname_content_edt.setText(tddUserCertificate.getNickName());

				nikeNameString = tddUserCertificate.getNickName();
			} else {
				mActivity_mine_personalcenter_nickname_content_edt.setText("");
			}
			// 手机号
			if (StringUtil.isNotEmpty(tddUserCertificate.getMobilePhone())) {
				AppContext.getAppContext().setMobilePhone(tddUserCertificate.getMobilePhone());
				mActivity_mine_personalcenter_telnum_tv.setText(tddUserCertificate.getMobilePhone());
			} else {
				mActivity_mine_personalcenter_telnum_tv.setText("");
			}
			// 身份证
			if (StringUtil.isNotEmpty(tddUserCertificate.getIdentity())) {
				mActivity_mine_personalcenter_idcard_content_edt.setText(tddUserCertificate.getIdentity());
				mActivity_mine_personalcenter_idcard_content_edt.setTextColor(getResources().getColor(R.color.textgray));
			} else {
				mActivity_mine_personalcenter_idcard_content_edt.setText("");
				mActivity_mine_personalcenter_idcard_content_edt.setTextColor(getResources().getColor(R.color.lightgray));
			}
			// 性别
			if (tddUserCertificate.getSex() instanceof Integer) {
				mActivity_mine_personalcenter_sex_content_tv.setText(StringUtil.getSex(tddUserCertificate.getSex()));// Int（1是男性，2是女性，0是未知）',
				if (tddUserCertificate.getSex() != 1 || tddUserCertificate.getSex() != 2) {
					mActivity_mine_personalcenter_sex_content_tv.setTextColor(getResources().getColor(R.color.lightgray));
				} else {
					mActivity_mine_personalcenter_sex_content_tv.setTextColor(getResources().getColor(R.color.textgray));
				}
			} else {
				mActivity_mine_personalcenter_sex_content_tv.setText("未知");// Int（1是男性，2是女性，0是未知）',
				mActivity_mine_personalcenter_sex_content_tv.setTextColor(getResources().getColor(R.color.lightgray));
			}
			// 邮箱
			if (StringUtil.isNotEmpty(tddUserCertificate.getEmail())) {
				mActivity_mine_personalcenter_mailbox_content_edt.setText(tddUserCertificate.getEmail());
				mActivity_mine_personalcenter_mailbox_content_edt.setTextColor(getResources().getColor(R.color.textgray));
			} else {
				mActivity_mine_personalcenter_mailbox_content_edt.setText("");
				mActivity_mine_personalcenter_mailbox_content_edt.setTextColor(getResources().getColor(R.color.lightgray));
			}
		}
	}

	@Override
	public void OnFailResultListener(String mess) {
		if (progressDialog != null) {
			progressDialog.setContinueDialog(false);
//			UiHelper.ShowOneToast(this, mess);
		}
		onKeyBoardHide();
	}

	/**
	 * 获取活动状态popwindow
	 */
	private PopupWindow statePopupWindow;

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
		View popupWindow_view1 = this.getLayoutInflater().inflate(R.layout.activity_mine_personal_sexpopwindow, null, false);
		statePopupWindow = new PopupWindow(popupWindow_view1, AppContext.getAppContext().getScreenWidth(), LayoutParams.WRAP_CONTENT, true);
		statePopupWindow.setOutsideTouchable(true);// 点击区域外，关闭
		statePopupWindow.setTouchable(true);
		popupWindow_view1.setFocusable(true); // 这个很重要  
		popupWindow_view1.setFocusableInTouchMode(true); 
		popupWindow_view1.setOnKeyListener(new OnKeyListener() {  
		    @Override  
		    public boolean onKey(View v, int keyCode, KeyEvent event) {  
		        if (keyCode == KeyEvent.KEYCODE_BACK) {  
		        	
		        	statePopupWindow.dismiss();  
		        	statePopupWindow = null;  
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
				mActivity_mine_personalcenter_sex_content_tv.setText("男");
				mActivity_mine_personalcenter_sex_content_tv.setTextColor(getResources().getColor(R.color.textgray));
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
				mActivity_mine_personalcenter_sex_content_tv.setText("女");
				mActivity_mine_personalcenter_sex_content_tv.setTextColor(getResources().getColor(R.color.textgray));
				if (statePopupWindow != null && statePopupWindow.isShowing()) {
					statePopupWindow.dismiss();
					statePopupWindow = null;
				}
			}
		});
		if (mActivity_mine_personalcenter_sex_content_tv.getText().toString().equals("女")) {
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
			}
		});
		// statePopupWindow.setFocusable(true);

		statePopupWindow.update();
		// 显示窗口 位置
		statePopupWindow.showAtLocation(this.findViewById(R.id.adjust_ll_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
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
			if ((MinePersonInfoGetRes) bundle.getSerializable("minePersonInfoGetRes") != null) {
				minePersonInfoGetRes = (MinePersonInfoGetRes) bundle.getSerializable("minePersonInfoGetRes");
				initBindOrRemove();
			}

		}
	};

	@Override
	protected void onDestroy() {
		 /**收集异常日志*/
    	LogManager.getManager(getApplicationContext()).unregisterActivity(this);
    	 /**收集异常日志*/
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

	public void initBindOrRemove() {
		// 是否绑定qq、微信、新浪微博
		String qqOpenIdString = minePersonInfoGetRes.getQq_open_id();
		String weChatUnionIdString = minePersonInfoGetRes.getWechat_union_id();
		String weiboOpenIdString = minePersonInfoGetRes.getWeibo_open_id();
		if (StringUtil.isNotEmpty(qqOpenIdString)) {// qq绑定
			mActivity_mine_personalcenter_bindonaccount_qzone_iv.setImageDrawable(getResources().getDrawable(R.drawable.qzonebound));
		} else {
			mActivity_mine_personalcenter_bindonaccount_qzone_iv.setImageDrawable(getResources().getDrawable(R.drawable.qzone));
		}
		if (StringUtil.isNotEmpty(weChatUnionIdString)) {// 微信绑定
			mActivity_mine_personalcenter_bindonaccount_weixin_iv.setImageDrawable(getResources().getDrawable(R.drawable.wechatbound));
		} else {
			mActivity_mine_personalcenter_bindonaccount_weixin_iv.setImageDrawable(getResources().getDrawable(R.drawable.wechat));
		}
		// if (StringUtil.isNotEmpty(weiboOpenIdString)) {// 微博绑定
		// mActivity_mine_personalcenter_bindonaccount_xinlang_iv.setImageDrawable(getResources().getDrawable(R.drawable.weibobound));
		// } else {
		// mActivity_mine_personalcenter_bindonaccount_xinlang_iv.setImageDrawable(getResources().getDrawable(R.drawable.weibo));
		// }
	}

	/**
	 * 头像选择对话框
	 */
	private void showDownLoadDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("选择获取头像方式").setPositiveButton("本地相册", new android.content.DialogInterface.OnClickListener() {

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

	// 剪切之后得到的图片
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				uriImg = uri;
				crop(uri);
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
				uriImg = Uri.fromFile(tempFile);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(MinePersonalCenterActivity.this, "未找到存储卡，无法存储照片！", 0).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				if (data != null) {
					bitmap = data.getParcelableExtra("data");
					this.mActivity_mine_personalcenter_icon_iv.setImageBitmap(UiHelper.CircleImageView(bitmap, 2));
					if (tempFile != null && tempFile.exists()) {
						tempFile.delete();
					}
					// boolean delete = tempFile.delete();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 剪切图片
	 * 
	 * @function:
	 * @author:Jerry
	 * @date:2013-12-30
	 * @param uri
	 */
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
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
				setTitle("paht ok,path:" + path);
			}
		}
		// 构造方法1、2的参数
		filePathList = new ArrayList<String>();
		String aa = uriImg.toString();
		filePathList.add(getRealFilePath(this, uriImg));
		File file = new File(filePathList.get(0));
		boolean fi = file.exists();
		filePathList.add(getRealFilePath(this, uriImg));
		post = new HttpMultipartPostPersonCenter(MinePersonalCenterActivity.this, filePathList);
		post.execute();
	}

	// 根据Uri获取到图片的路径
	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri, new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	// 上传头像完之后的操作
	public void handleHeadImg(String imgMess) {
		WBResponse response = MessageUtil.JsonStrToWBResponse(imgMess, HeadImgRes.class);
		if(response!=null){
			
	
		String msgString = response.getMsg();
		HeadImgRes headImgRes = (HeadImgRes) response.getRespBody();
		if (headImgRes != null) {
			if (headImgRes.getFileUrls().size() > 0) {
				headImgUrlString = UrlManager.uploadToUrlServer + headImgRes.getFileUrls().get(0);
				AppContext.getAppContext().setHeadImgUrl(headImgUrlString);
			}

		}

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.setContinueDialog(false);
		}
		if (judgeDataFormat()) {
			editPersonalInformation();
		}else {
			// TODO Auto-generated method stub
			if(localBitmap!=null){
				mActivity_mine_personalcenter_icon_iv.setImageBitmap(localBitmap);
			}else {
				mActivity_mine_personalcenter_icon_iv.setImageDrawable(getResources().getDrawable(R.drawable.default_head_icon));
			}
		}
	}
}

	@Override
	public void stateChange(int state) {
		// TODO Auto-generated method stub
		switch (state) {
		case RelativeLayoutView.KEYBORAD_HIDE:
			// mActivity_mine_personalcenter_icon_rl.setVisibility(View.VISIBLE);
			// adjustView.setVisibility(View.GONE);
			// adjustScrollview.scrollTo(0, 0);
			// adjustScrollview.smoothScrollTo(0, 0);
			break;
		case RelativeLayoutView.KEYBORAD_SHOW:
			adjustView.setVisibility(View.VISIBLE);
			// adjustScrollview.scrollTo(0, keyboardHeight/3+300);
			// adjustScrollview.smoothScrollTo(0, keyboardHeight/3+300);
			// 使用post的方法
			adjustScrollview.post(new Runnable() {
				@Override
				public void run() {
					adjustScrollview.scrollTo(0, 200);
				}
			});
			break;
		}
	}

	// 获取状态栏高度
	public static int getStatusBarHeight(Context context) {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	protected void onKeyBoardHide() {
		// TODO Auto-generated method stub

		adjustLayoutView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			/**
			 * the result is pixels
			 */
			@Override
			public void onGlobalLayout() {

				Rect r = new Rect();
				// (0, 50 - 720, 1280)
				Rect aaRect = new Rect();
				adjustLayoutView.getWindowVisibleDisplayFrame(r);
				int height = AppContext.getAppContext().getScreenHeight();
				int screenHeight = adjustLayoutView.getRootView().getHeight();
				int heightDiff = screenHeight - (r.bottom - r.top);
				// 在不显示软键盘时，heightDiff等于状态栏的高度
				// 在显示软键盘时，heightDiff会变大，等于软键盘加状态栏的高度。
				// 所以heightDiff大于状态栏高度时表示软键盘出现了，
				// 这时可算出软键盘的高度，即heightDiff减去状态栏的高度
				if (keyboardHeight == 0 && heightDiff > statusBarHeight) {
					keyboardHeight = heightDiff - statusBarHeight;
				}
				if (isShowKeyboard) {
					// 如果软键盘是弹出的状态，并且heightDiff小于等于状态栏高度，
					// 说明这时软键盘已经收起
					if (heightDiff <= statusBarHeight) {
						isShowKeyboard = false;
						adjustView.setVisibility(View.GONE);

					}
				} else {
					// 如果软键盘是收起的状态，并且heightDiff大于状态栏高度，
					// 说明这时软键盘已经弹出
					if (heightDiff > statusBarHeight) {
						isShowKeyboard = true;
						adjustView.setVisibility(View.VISIBLE);
						adjustScrollview.post(new Runnable() {
							@Override
							public void run() {
								adjustScrollview.scrollTo(0, 200);
							}
						});
					}
				}

				// boolean visible = heightDiff > screenHeight / 3;
			}
		});
	}

}
