package com.newland.wstdd.mine.minesetting.about;

import java.io.File;
import java.io.IOException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.FileHelper;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.minesetting.beanrequest.VersionReq;
import com.newland.wstdd.mine.minesetting.beanresponse.VersionRes;
import com.newland.wstdd.mine.minesetting.handle.VersionInfoHandle;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;

/**
 * 关于我是团大大
 * 
 * @author Administrator
 * 
 */
public class AboutTddActivity extends BaseFragmentActivity implements OnPostListenerInterface, OnClickListener {
	private static final String TAG = "AboutTddActivity";//收集异常日志tag
	// 服务器返回的信息
	VersionRes versionRes;
	VersionInfoHandle handler = new VersionInfoHandle(this);
	// 当前系统的版本号
	String strVersionCode;// 当前系统的版本号
	private ProgressDialog mypDialog;
	HttpHandler<File> httpHandler;
	FinalHttp fh = new FinalHttp();

	private ImageView headimg_iv;// 我是团大大头像
	private TextView name_tv;// 我是团大大名字和版本
	private LinearLayout protocol_ll;// 服务协议
	private LinearLayout officialstation_ll;// 官方网站
	private TextView officialstation_tv;// 官方网站网址
	private LinearLayout weixin_ll;// 微信号
	private TextView weixin_tv;// 微信号内容
	private LinearLayout weibo_ll;// 新浪微博
	private TextView weibo_tv;// 新浪微博@我是团大大
	private LinearLayout versions_ll;// 版本更新
	private TextView versions_tv;// 当前版本

	// 控制整体布局的三个layout
	private RelativeLayout relativelayout1;
	private LinearLayout linearlayout2;
	private RelativeLayout relativelayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		setContentView(R.layout.activity_mine_setting_about);
		
		 /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		initView();
		setTitle();
		getLocalVersion();// 获取程序内清单文件中的版本号
	}

	@Override
	protected void onDestroy() {
		/** 收集异常日志 */
		LogManager.getManager(getApplicationContext()).unregisterActivity(this);
		/** 收集异常日志 */
		super.onDestroy();
	}
	/**
	 * 设置标题
	 */
	private void setTitle() {
		ImageView left_btn = (ImageView) findViewById(R.id.head_left_iv);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("关于");
		if (left_btn != null) {
			left_btn.setVisibility(View.VISIBLE);
			left_btn.setImageDrawable(getResources().getDrawable(R.drawable.left_expandsion));
			left_btn.setOnClickListener(this);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {
			right_tv.setVisibility(View.GONE);
		}
	}

	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		try {
			switch (responseId) {
			case VersionInfoHandle.VERSION_INFO:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				versionRes = (VersionRes) obj;
				if (versionRes != null) {
					String versionNum = versionRes.getTddAppVersion().getVersionNo();
					String installUrl = versionRes.getTddAppVersion().getInstallUrl();
					if (versionNum.equals(strVersionCode)) {
						UiHelper.ShowOneToast(this, "当前已是最新版本");
					} else {
						if (installUrl == null) {
							return;
						}
						showDownLoadDialog(installUrl);
					}
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// 服务器返回失败
	@Override
	public void OnFailResultListener(String mess) {
		if (progressDialog != null) {
			progressDialog.setContinueDialog(false);
			UiHelper.ShowOneToast(this, mess);
		}

	}

	@Override
	protected void processMessage(Message msg) {

	}

	@Override
	public void refresh() {

	}

	/**
	 * 更新版本问题
	 */
	private void refreshVersion() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					VersionReq versionReq = new VersionReq();
					versionReq.setPlatform("1");// 所属平台 0 ios 1 安卓
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<VersionRes> ret = mgr.getVersionInfo(versionReq);// 泛型类，
					Message message = new Message();
					message.what = VersionInfoHandle.VERSION_INFO;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						versionRes = (VersionRes) ret.getObj();
						message.obj = versionRes;
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
	 * 版本更新提示对话框
	 */
	private void showDownLoadDialog(final String updateUrl) {
		try {

			AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("有最新版本,是否更新？").setPositiveButton("立即去更新", new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					// doDownLoadWork(updateUrl);
					// 调用系统默认浏览器 去下载
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(updateUrl);
					intent.setData(content_url);
					startActivity(intent);

				}
			}).setNegativeButton("稍后再说", new android.content.DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

				}
			}).show();
			dialog.setCanceledOnTouchOutside(false);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	private void doDownLoadWork(String strurl) {
		try {

			mypDialog = new ProgressDialog(this);
			// 实例化
			mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// 设置进度条风格，风格为长形，有刻度的
			mypDialog.setMessage("正在下载");
			mypDialog.setProgress(59);
			mypDialog.setButton("取消下载", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					httpHandler.stop();
					mypDialog.dismiss();

				}

			});
			// 设置ProgressDialog 进度条进度
			// 设置ProgressDialog 的一个Button
			mypDialog.setIndeterminate(false);
			// 设置ProgressDialog 的进度条是否不明确
			mypDialog.setCancelable(false);
			// 设置ProgressDialog 是否可以按退回按键取消
			mypDialog.show();
			// 让ProgressDialog显示

			// 调用download方法开始下载
			File file = new File(AppContext.getAppContext().CASH_DIR_APK);
			if (!file.exists()) {
				try {
					FileHelper.createDir(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String fileName = strurl.substring(strurl.lastIndexOf('/') + 1);
			httpHandler = fh.download(strurl, new AjaxParams(), AppContext.getAppContext().CASH_DIR_APK + fileName, false, new AjaxCallBack() {

				public void onLoading(long count, long current) {
					System.out.println("下载进度：" + current + "/" + count);
					mypDialog.setMax((int) count);
					mypDialog.setProgress((int) current);
				}

				public void onFailure(Throwable t, int errorNo, String strMsg) {
					if (StringUtil.isNotEmpty(strMsg))
						strMsg = "连接失败";
					// System.out.println("下载进度："+errorNo+"/"+strMsg);
					if (errorNo == 0) {
						Toast.makeText(getApplicationContext(), "用户取消下载", 1500).show();
						httpHandler.stop();
					} else {
						Toast.makeText(getApplicationContext(), "错误提示：" + errorNo + "/" + strMsg, 1500).show();
					}

					mypDialog.dismiss();

				}

				@Override
				public void onSuccess(Object t) {
					if (t instanceof File) {
						File f = (File) t;
						Toast.makeText(getApplicationContext(), "下载完成", 1000).show();
						mypDialog.dismiss();
						installApk(((File) t).getAbsoluteFile().toString());
					}
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void installApk(String fileName) {
		// String str = "/yike_160.apk";
		// String fileName = AppContext.getAppContext().CASH_DIR_APK + str;

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
		this.startActivity(intent);
	}

	// 获取本地程序的版本ID
	public void getLocalVersion() {
		PackageManager packageManager = getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		strVersionCode = packageInfo.versionName + "";// 当前操作系统的版本号
		AppContext.getAppContext().setVersionCode(strVersionCode);
	}

	@Override
	public void initView() {
		relativelayout1 = (RelativeLayout) findViewById(R.id.relativelayout1);
		linearlayout2 = (LinearLayout) findViewById(R.id.linearlayout2);
		relativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
		if (null != AppContext.getAppContext()) {
			relativelayout1.getLayoutParams().height = AppContext.getAppContext().getScreenHeight() / 3 - 30;
			linearlayout2.getLayoutParams().height = AppContext.getAppContext().getScreenHeight() / 3;
			relativelayout.getLayoutParams().height = AppContext.getAppContext().getScreenHeight() / 3;
		}
		headimg_iv = (ImageView) findViewById(R.id.headimg_iv);
		name_tv = (TextView) findViewById(R.id.name_tv);
		protocol_ll = (LinearLayout) findViewById(R.id.protocol_ll);
		officialstation_ll = (LinearLayout) findViewById(R.id.officialstation_ll);
		officialstation_tv = (TextView) findViewById(R.id.officialstation_tv);
		weixin_ll = (LinearLayout) findViewById(R.id.weixin_ll);
		weixin_tv = (TextView) findViewById(R.id.weixin_tv);
		weibo_ll = (LinearLayout) findViewById(R.id.weibo_ll);
		weibo_tv = (TextView) findViewById(R.id.weibo_tv);
		weibo_tv.setText("@我是团大大");
		versions_ll = (LinearLayout) findViewById(R.id.versions_ll);
		versions_tv = (TextView) findViewById(R.id.versions_tv);

		versions_tv.setText("当前版本v"+AppContext.getAppContext().getVersionCode());
		
		protocol_ll.setOnClickListener(this);
		officialstation_ll.setOnClickListener(this);
		weixin_ll.setOnClickListener(this);
		weibo_ll.setOnClickListener(this);
		versions_ll.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.head_left_iv:// 返回
			finish();
			break;
		case R.id.protocol_ll:// 服务协议
			Intent intent = new Intent(AboutTddActivity.this, ProtocolActivity.class);
			startActivity(intent);
			break;
		case R.id.officialstation_ll:// 官方网站

			break;
		case R.id.weixin_ll:// 微信号

			break;
		case R.id.weibo_ll:// 新浪微博

			break;
		case R.id.versions_ll:// 版本更新

			refreshVersion();// 更新版本
			break;
		default:
			break;
		}

	}
}
