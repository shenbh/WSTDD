package com.newland.wstdd.mine.personalcenter;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.login.qqlogin.QQLoginActivity;
import com.newland.wstdd.mine.beanresponse.MinePersonInfoGetRes;
import com.newland.wstdd.mine.personalcenter.beanreq.BindReq;
import com.newland.wstdd.mine.personalcenter.beanreq.RemoveBindReq;
import com.newland.wstdd.mine.personalcenter.beanres.BindRes;
import com.newland.wstdd.mine.personalcenter.beanres.RemoveBindRes;
import com.newland.wstdd.mine.personalcenter.handle.BindRemoveBindHandle;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class RemoveBindActivity extends BaseFragmentActivity implements OnPostListenerInterface {
	private static final String TAG = "RemoveBindActivity";//收集异常日志tag
	/**
	 * 第三方登录
	 */
	// 微信登录
	private ImageView weixinImageView;// 测试 微信测试
	private IWXAPI api;
	private static final String WEIXIN_SCOPE = "snsapi_userinfo";// 用于请求用户信息的作用域
	private static final String WEIXIN_STATE = "login_state"; // 自定义
	// 广播
	private IntentFilter filter;// 定一个广播接收过滤器

	private int removeBind = -1;// 判断是 解除哪个绑定

	private int bind = -1;

	private TextView weixinStateTv, qqStateTv;// 绑定的状态
	private TextView weixinActionTv, qqActionTv;// 绑定的动作
	private MinePersonInfoGetRes minePersonInfoGetRes;// 个人的信息，主要是用来判断用的
	// 服务器 返回
	private RemoveBindRes removeBindRes;// 解绑
	private BindRes bindRes;// 绑定
	private BindRemoveBindHandle handler = new BindRemoveBindHandle(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		// 注册广播
		filter = new IntentFilter("GET_THIRD_MESS_BIND");// 注册第三方登录的广播
		registerReceiver(broadcastReceiver, filter);// 在对象broadcastReceiver中进行接收的相应处理

		setContentView(R.layout.activity_remove_bind);
		
		 /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		minePersonInfoGetRes = (MinePersonInfoGetRes) getIntent().getSerializableExtra("MinePersonInfoGetRes");
		setTitle();
		initView();
	}

	private void setTitle() {
		ImageView leftBtn = (ImageView) findViewById(R.id.head_left_iv);
		TextView centerTitle = (TextView) findViewById(R.id.head_center_title);
		TextView rightTv = (TextView) findViewById(R.id.head_right_tv);
		leftBtn.setVisibility(View.VISIBLE);
		centerTitle.setText("绑定账号");
		rightTv.setVisibility(View.GONE);
		rightTv.setTextColor(getResources().getColor(R.color.red));
		rightTv.setOnClickListener(this);
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("minePersonInfoGetRes", minePersonInfoGetRes);
				intent.putExtras(bundle);
				sendBroadcast(intent);
				finish();
			}
		});
	}

	@Override
	protected void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		weixinStateTv = (TextView) findViewById(R.id.remove_bind_weixin_state);
		weixinActionTv = (TextView) findViewById(R.id.remove_bind_weixin_action);

		qqStateTv = (TextView) findViewById(R.id.remove_bind_qq_state);
		qqActionTv = (TextView) findViewById(R.id.remove_bind_qq_action);
		initBind();// 初始化界面
		// 微信 绑定解除保定
		weixinActionTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if ("解绑".equals(weixinActionTv.getText().toString())) {
					removeBind = 1;
					removeBindReq("1");
				} else if ("绑定".equals(weixinActionTv.getText().toString())) {
					bind = 1;
					bindWeixinReq();

				}

			}

		});

		// qq 绑定解除保定
		qqActionTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("解绑".equals(qqActionTv.getText().toString())) {
					removeBind = 0;
					removeBindReq("0");
				} else if ("绑定".equals(qqActionTv.getText().toString())) {
					bind = 0;
					bindQQReq();
				}

			}

		});
	}

	// 初始化绑定
	@SuppressLint("NewApi")
	public void initBind() {
		// 初始化 界面
		if (minePersonInfoGetRes != null) {
			if (minePersonInfoGetRes.getWechat_union_id() != null && !"".equals(minePersonInfoGetRes.getWechat_union_id())) {
				weixinStateTv.setText("已绑定");
				weixinActionTv.setText("解绑");
				weixinActionTv.setBackground(getResources().getDrawable(R.drawable.textview_remove_bind_radius_shap));
			} else {
				weixinStateTv.setText("未绑定");
				weixinActionTv.setText("绑定");
				weixinActionTv.setBackground(getResources().getDrawable(R.drawable.textview_bind_remove_radius_shap));
			}

			if (minePersonInfoGetRes.getQq_open_id() != null && !"".equals(minePersonInfoGetRes.getQq_open_id())) {
				qqStateTv.setText("已绑定");
				qqActionTv.setText("解绑");
				qqActionTv.setBackground(getResources().getDrawable(R.drawable.textview_remove_bind_radius_shap));
			} else {
				qqStateTv.setText("未绑定");
				qqActionTv.setText("绑定");
				qqActionTv.setBackground(getResources().getDrawable(R.drawable.textview_bind_remove_radius_shap));
			}
		}

	}

	/**
	 * 接收到广播之后 在这里要做三件事情 将第三方的数据发送给服务器 openId
	 */
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (AppContext.getAppContext().getOpenId() != null && !"".equals(AppContext.getAppContext().getOpenId())) {
				String appString = AppContext.getAppContext().getOpenId();
				binds();

			} else {
				UiHelper.ShowOneToast(RemoveBindActivity.this, "异常原因获取第三方资料失败！");
				bind = -1;
			}

		}

	};

	// 绑定 qq
	private void bindQQReq() {
		// TODO Auto-generated method stub
		AppContext.getAppContext().setPlatForm("0");
		Intent intent = new Intent(this, QQLoginActivity.class);
		intent.putExtra("QQLOGIN", "bind");
		startActivity(intent);
	}

	// 绑定 微信
	private void bindWeixinReq() {
		// TODO Auto-generated method stub
		// 发送广播 通知 前台已经得到第三方的相关信息
		Intent intent = new Intent(this, WXEntryActivity.class);
		intent.putExtra("login", "bind");
		this.startActivity(intent);
		// 发送广播 结果没作用的感觉
		Intent intent0 = new Intent();// 切记
										// 这里的Action参数与IntentFilter添加的Action要一样才可以
		intent0.setAction("WEIXIN_TYPE");
		intent0.putExtra("login", "bind");
		this.sendBroadcast(intent0);// 发送广播了

		AppContext.getAppContext().setPlatForm("1");
		SendAuth.Req req;
		api = WXAPIFactory.createWXAPI(this, "wx1b84c30d9f380c89", false);
		api.registerApp("wx1b84c30d9f380c89");
		req = new SendAuth.Req();
		req.scope = WEIXIN_SCOPE;
		req.state = WEIXIN_STATE;
		api.sendReq(req);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("minePersonInfoGetRes", minePersonInfoGetRes);
			intent.putExtras(bundle);
			intent.setAction("BIND_REMOVE_BIND");
			sendBroadcast(intent);
			finish();
			return false;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		/** 收集异常日志 */
		LogManager.getManager(getApplicationContext()).unregisterActivity(this);
		/** 收集异常日志 */
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

	/**
	 * 服务器请求部分
	 */
	// 解除绑定请求
	public void removeBindReq(final String platform) {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					RemoveBindReq reqInfo = new RemoveBindReq();
					reqInfo.setPlatForm(platform);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<RemoveBindRes> ret = mgr.getRemoveBindInfo(reqInfo);// 泛型类，
					Message message = new Message();
					message.what = BindRemoveBindHandle.REMOVE_BIND;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						removeBindRes = (RemoveBindRes) ret.getObj();
						message.obj = removeBindRes;
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

	// 进行绑定
	private void binds() {
		// TODO Auto-generated method stub
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					BindReq reqInfo = new BindReq();
					reqInfo.setOpenId(AppContext.getAppContext().getOpenId());
					reqInfo.setPlatForm(AppContext.getAppContext().getPlatForm());
					reqInfo.setPhoneNum(AppContext.getAppContext().getMobilePhone());
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<BindRes> ret = mgr.getBindInfo(reqInfo);// 泛型类，
					Message message = new Message();
					message.what = BindRemoveBindHandle.BIND;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						bindRes = (BindRes) ret.getObj();
						message.obj = bindRes;
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

	/**
	 * 服务器返回部分
	 */
	@SuppressLint("NewApi")
	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		// TODO Auto-generated method stub
		try {
			switch (responseId) {
			case BindRemoveBindHandle.BIND:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				if (bind == 0) {
					minePersonInfoGetRes.setQq_open_id(AppContext.getAppContext().getOpenId());
					;
					qqStateTv.setText("已绑定");
					qqActionTv.setText("解绑");
					qqActionTv.setBackground(getResources().getDrawable(R.drawable.textview_remove_bind_radius_shap));
				} else if (bind == 1) {
					minePersonInfoGetRes.setWechat_union_id(AppContext.getAppContext().getOpenId());
					weixinStateTv.setText("已绑定");
					weixinActionTv.setText("解绑");
					weixinActionTv.setBackground(getResources().getDrawable(R.drawable.textview_remove_bind_radius_shap));
				}

				break;
			case BindRemoveBindHandle.REMOVE_BIND:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				if (removeBind == 0) {
					minePersonInfoGetRes.setQq_open_id(null);
					qqStateTv.setText("未绑定");
					qqActionTv.setText("绑定");
					qqActionTv.setBackground(getResources().getDrawable(R.drawable.textview_bind_remove_radius_shap));
				} else if (removeBind == 1) {
					minePersonInfoGetRes.setWechat_union_id(null);
					weixinStateTv.setText("未绑定");
					weixinActionTv.setText("绑定");
					weixinActionTv.setBackground(getResources().getDrawable(R.drawable.textview_bind_remove_radius_shap));
				}
				break;

			default:
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void OnFailResultListener(String mess) {
		// TODO Auto-generated method stub
		removeBind = -1;
		bind = -1;
		UiHelper.ShowOneToast(this, mess);
		if (progressDialog != null) {
			progressDialog.setContinueDialog(false);
			UiHelper.ShowOneToast(this, mess);
		}
	}

}
