package com.newland.wstdd.common.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.fragment.MainFragment;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.tools.Util;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.minesetting.beanrequest.VersionReq;
import com.newland.wstdd.mine.minesetting.beanresponse.VersionRes;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.originate.handle.OriginateFragmentHandle;

/**
 * 这是存放fragment的FragmentActivity
 * 放mainfragment
 *
 * @author Administrator
 */
public class HomeActivity extends BaseFragmentActivity implements OnPostListenerInterface {
    private static final String TAG = "HomeActivity";
    private long exitTime = 0;// 退出的时间
    private MainFragment mainFragment;

    String strVersionCode;// 当前系统的版本号
    CheckVersionHandle handler = new CheckVersionHandle(this);

    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction = getSupportFragmentManager()
            .beginTransaction();
//	private LoginRes loginRes;//登录获取到的所有信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        AppManager.getAppManager().addActivity(this);//添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        setContentView(R.layout.activity_home);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        getLocalVersion();
        refreshVersion();
//		Intent intent=getIntent();
//		Bundle bundle=intent.getExtras();
//		loginRes=(LoginRes) bundle.getSerializable("loginres");
        if (AppContext.getAppContext().getUserId() != null && !"".endsWith(AppContext.getAppContext().getUserId())) {
            AppContext.getAppContext().setIsLogin("true");
        }
        mainFragment = (MainFragment) fm.findFragmentById(R.id.mainfragments);
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            fragmentTransaction.add(R.id.fragmentmain, mainFragment);
            fragmentTransaction.commit();
            fm.executePendingTransactions();
        }

    }

    @Override
    protected void onDestroy() {
        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /**收集异常日志*/
        super.onDestroy();
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

    /**
     * 版本更新提示对话框
     */
    private void showDownLoadDialog(final String updateUrl) {
        try {
			
			/*LinearLayout layout = new LinearLayout(this); 
			TextView tv = new TextView(this);  
			tv.setText("确定退出吗？");  
			LinearLayout.LayoutParams pm = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); 
			layout.setLayoutParams(pm);
			layout.setOrientation(LinearLayout.VERTICAL);
//			pm.gravity = Gravity.CENTER;
//			tv.setLayoutParams(pm);
			layout.addView(tv, pm);  
			tv.setGravity(Gravity.CENTER);
			layout.setGravity(Gravity.CENTER);*/

            AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").
//					setView(layout)
        setMessage("有最新版本,是否更新？")
                    .setPositiveButton("立即去更新", new android.content.DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

//					doDownLoadWork(updateUrl);
                            //调用系统默认浏览器   去下载
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                com.newland.wstdd.common.tools.UiHelper.ShowOneToast(getApplicationContext(), "再按一次退出程序");
                exitTime = System.currentTimeMillis();

            } else {

                AppManager.getAppManager().finishAllActivity();
                AppManager.getAppManager().finishActivity(HomeActivity.class);

            }


            return false;
        }
        return false;
    }


    @Override
    protected void processMessage(Message msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

    /**
     * 更新版本问题
     */
    VersionRes versionRes;

    private void refreshVersion() {
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {
                    // 需要发送一个request的对象进行请求
                    VersionReq versionReq = new VersionReq();
                    versionReq.setPlatform("1");//所属平台 0 ios 1 安卓
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<VersionRes> ret = mgr.getVersionInfo(versionReq);// 泛型类，
                    Message message = new Message();
                    message.what = OriginateFragmentHandle.VERSION_INFO;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        versionRes = (VersionRes) ret.getObj();
                        message.obj = versionRes;
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

    @Override
    public void OnHandleResultListener(Object obj, int responseId) {
        if (progressDialog != null) {
            progressDialog.setContinueDialog(false);
        }
        switch (responseId) {
            case OriginateFragmentHandle.VERSION_INFO:
                versionRes = (VersionRes) obj;
                if (versionRes != null) {
                    String versionNum = versionRes.getTddAppVersion().getVersionNo();
                    String installUrl = versionRes.getTddAppVersion().getInstallUrl();
                    if (versionNum.equals(strVersionCode)) {
                        if (progressDialog != null) {
                            progressDialog.setContinueDialog(false);
//						UiHelper.ShowOneToast(this, "当前已是最新版本");
                        }
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

    }

    @Override
    public void OnFailResultListener(String mess) {
        if (progressDialog != null) {
            progressDialog.setContinueDialog(false);
            UiHelper.ShowOneToast(this, mess);
        }

    }
}
