package com.newland.wstdd.login.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.SharePreferenceUtil.SharePreferenceUtil;
import com.newland.wstdd.common.activity.HomeActivity;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.EditTextUtil;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.login.beanrequest.CheckCodeReq;
import com.newland.wstdd.login.beanrequest.LoginBindReq;
import com.newland.wstdd.login.beanrequest.RegistFirstReq;
import com.newland.wstdd.login.beanrequest.ThirdLoginReq;
import com.newland.wstdd.login.beanresponse.CheckCodeRes;
import com.newland.wstdd.login.beanresponse.LoginBindRes;
import com.newland.wstdd.login.beanresponse.LoginRes;
import com.newland.wstdd.login.beanresponse.RegistFirstRes;
import com.newland.wstdd.login.beanresponse.ThirdLoginRes;
import com.newland.wstdd.login.handle.BindRegistFragmentHandle;
import com.newland.wstdd.login.handle.CheckCodeHandle;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;

/**
 * 第三方登录（微信、qq）
 *
 * @author Administrator 2015-12-9
 */
public class LoginBindActivity extends BaseFragmentActivity implements OnPostListenerInterface {
    private static final String TAG = "LoginBindActivity";//收集异常日志tag
    int recLen = 0;// 计时大小 0--59
    private boolean checkcodeJudge = true;// 验证码倒计时 判断

    private TextView confirmTextView;// 获取验证码

    private EditText phoneEditText, confirmEditText; // 输入手机跟验证码

    private Button bindLogin;// 绑定登录
    // 服务器返回
    private CheckCodeRes checkCodeRes;// 服务器返回的信息
    private CheckCodeHandle handler = new CheckCodeHandle(this);
    // 进行注册
    private RegistFirstRes registFirstRes;// 从服务器返回的信息
    private BindRegistFragmentHandle handler1 = new BindRegistFragmentHandle(this);
    // 进行绑定
    private LoginBindRes loginBindRes;// 从服务器返回的信息
    // 第三方登录
    private ThirdLoginRes thirdLoginRes;// 第三方登录的请求

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        setContentView(R.layout.activity_login_bind);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        initTitle();// 标题栏
        initView();
    }

    @Override
    protected void onDestroy() {
        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /**收集异常日志*/
        super.onDestroy();
    }

    private void initTitle() {
        // TODO Auto-generated method stub
        TextView topTitle = (TextView) findViewById(R.id.head_center_title);
        topTitle.setText("绑定");
    }

    public void initView() {
        confirmTextView = (TextView) findViewById(R.id.bindlogin_getconfirm);
        confirmTextView.setOnClickListener(this);// 获取验证码

        phoneEditText = (EditText) findViewById(R.id.bindlogin_phone);
        confirmEditText = (EditText) findViewById(R.id.bindlogin_confirm);

        bindLogin = (Button) findViewById(R.id.bind_login_bt);
        bindLogin.setOnClickListener(this);
    }

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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bindlogin_getconfirm:
                UiHelper.ShowOneToast(LoginBindActivity.this, "60s后重新获取");
                if (!EditTextUtil.checkMobileNumber(phoneEditText.getText().toString())) {
                    UiHelper.ShowOneToast(LoginBindActivity.this, "手机号格式不对或者有误");
                } else {
                    // 获取验证码
                    confirmTextView.setClickable(false);
                    recLen = 60;
                    checkcodeJudge = true;
                    new Thread(new MyThread()).start();// start thread
                    // 获取短信验证码
                    postPhoneNumber();// 短信验证
                }
                break;

            case R.id.bind_login_bt:
                String phone = phoneEditText.getText().toString();// 手机号码
                String veriCode = confirmEditText.getText().toString();
                if (!EditTextUtil.checkMobileNumber(phone)) {
                    UiHelper.ShowOneToast(this, "手机号格式不对或者有误");
                } else if ("".equals(veriCode)) {
                    UiHelper.ShowOneToast(this, "验证码不能为空");
                } else {
                    if (checkCodeRes != null) {
                        RegistFirstReq();// 发送到服务器上去，验证第一次是否成功
                    } else {
                        UiHelper.ShowOneToast(this, "请输入正确的验证码！");
                    }

                }
                // RegistFirstReq();

                break;
            default:
                break;
        }
    }

    // 发送手机获取验证码
    private void postPhoneNumber() {
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {
                    String phone = phoneEditText.getText().toString();// 手机号码
                    // 需要发送一个request的对象进行请求
                    CheckCodeReq reqInfo = new CheckCodeReq();
                    reqInfo.setPhoneNum(phone);
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<CheckCodeRes> ret = mgr.getCheckCodeIndo(reqInfo);// 泛型类，
                    Message message = new Message();
                    message.what = CheckCodeHandle.CHECK_CODE;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        if (ret.getObj() != null) {
                            checkCodeRes = (CheckCodeRes) ret.getObj();
                            message.obj = checkCodeRes;
                        } else {
                            checkCodeRes = new CheckCodeRes();
                            message.obj = checkCodeRes;
                        }
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

    // 第三方登录的时候 未绑定的时候进行的第一次的请求

    private void RegistFirstReq() {
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {
                    String phone = phoneEditText.getText().toString();// 手机号码
                    String veriCode = confirmEditText.getText().toString();
                    if (!EditTextUtil.checkMobileNumber(phone)) {
                        UiHelper.ShowOneToast(LoginBindActivity.this, "手机号格式不对或者有误");
                    } else {

                        // 需要发送一个request的对象进行请求
                        RegistFirstReq reqInfo = new RegistFirstReq();
                        reqInfo.setPhoneNum(phone);
                        reqInfo.setVeriCode(veriCode);
                        reqInfo.setPlatForm(AppContext.getAppContext().getPlatForm());
                        reqInfo.setOpenId(AppContext.getAppContext().getOpenId());
                        BaseMessageMgr mgr = new HandleNetMessageMgr();
                        RetMsg<RegistFirstRes> ret = mgr.getRegistInfo(reqInfo, checkCodeRes.getSessionId());// 泛型类，
                        Message message = new Message();
                        message.what = BindRegistFragmentHandle.BIND_REGIST_FIRST;// 设置死
                        // 访问服务器成功 1 否则访问服务器失败
                        if (ret.getCode() == 1) {
                            registFirstRes = (RegistFirstRes) ret.getObj();
                            message.obj = registFirstRes;
                        } else {
                            message.obj = ret.getMsg();
                        }
                        handler1.sendMessage(message);
                    }
                }

                ;
            }.start();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    // 发送请求绑定

    private void requestBind() {
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {
                    String phone = phoneEditText.getText().toString();// 手机号码
                    String veriCode = confirmEditText.getText().toString();

                    // 需要发送一个request的对象进行请求
                    LoginBindReq reqInfo = new LoginBindReq();
                    reqInfo.setPhoneNum(phone);
                    reqInfo.setPlatForm(AppContext.getAppContext().getPlatForm());
                    reqInfo.setOpenId(AppContext.getAppContext().getOpenId());
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<LoginBindRes> ret = mgr.getReqLoginBindInfo(reqInfo);// 泛型类，
                    Message message = new Message();
                    message.what = CheckCodeHandle.BING_LOGIN;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        loginBindRes = new LoginBindRes();
                        loginBindRes.setLoginBindMess(StringUtil.noNull(ret.getMsg()));
                        message.obj = loginBindRes;
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

    /**
     * 利用绑定过的进行请求登录
     */
    private void getThirdLoginResMess() {
        super.refreshDialog();

        try {
            new Thread() {
                public void run() {
                    // 需要发送一个request的对象进行请求
                    ThirdLoginReq reqInfo = new ThirdLoginReq();
                    reqInfo.setPlatForm(AppContext.getAppContext().getPlatForm());
                    reqInfo.setOpenId(AppContext.getAppContext().getOpenId());
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<ThirdLoginRes> ret = mgr.getThirdLoginInfo(reqInfo);// 泛型类，
                    Message message = new Message();
                    message.what = CheckCodeHandle.LOGIN_BIND;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        thirdLoginRes = (ThirdLoginRes) ret.getObj();
                        message.obj = thirdLoginRes;
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

    /**
     * 处理服务器的数据
     */
    @Override
    public void OnHandleResultListener(Object obj, int responseId) {
        try {
            switch (responseId) {
                // 短信验证码
                case CheckCodeHandle.CHECK_CODE:
                    if (progressDialog != null) {
                        progressDialog.setContinueDialog(false);
                    }
                    UiHelper.ShowOneToast(this, "短信验证码已发");
                    break;
                // 绑定之后第一次注册请求
                case BindRegistFragmentHandle.BIND_REGIST_FIRST:
                    if (progressDialog != null) {
                        progressDialog.setContinueDialog(false);
                    }
                    try {
                        registFirstRes = (RegistFirstRes) obj;
                        if (registFirstRes != null) {
                            String userIdString = registFirstRes.getUserId();// 用户id
                            AppContext.getAppContext().setUserId(userIdString);
                            String headImgUrlString = registFirstRes.getHeadImgUrl();// 头像地址
                            String nickNameString = registFirstRes.getNickName();// 昵称
                            Bundle bundle = new Bundle();
                            bundle.putString("userIdString", userIdString);
                            bundle.putString("headImgUrlString", headImgUrlString);
                            bundle.putString("nickNameString", nickNameString);
                            // 如果密码不为空的话,那么就是已经注册过的用户，这样就直接弹出是否绑定，要是绑定
                            if (registFirstRes.getPwd() != null && !"".equals(registFirstRes.getPwd())) {
                                if (!userIdString.equals("") && userIdString != null) {
                                    showDownLoadDialog();

                                } else {
                                    UiHelper.ShowOneToast(this, "无法获取用户ID,需重新发送验证码！");
                                }
                            } else {
                                // 这里表示的是，绑定的账号还没有进行注册过，需要进行注册
                                UiHelper.showRegistFinishActivity(this, bundle);

                            }

                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    break;
                // 绑定成功之后 的处理 已经有账户了
                case CheckCodeHandle.BING_LOGIN:
                    if (progressDialog != null) {
                        progressDialog.setContinueDialog(false);
                    }
                    loginBindRes = (LoginBindRes) obj;
                    if (loginBindRes != null) {
                        // 已经绑定成功了，可以自动进行根据第三方进行登录了
                        getThirdLoginResMess();
                    }
                    UiHelper.ShowOneToast(this, loginBindRes.getLoginBindMess());
                    break;

                // 绑定之后直接登录 这个是已经有账号了，绑定下就可以直接登录的 要是没有账号的，就需要去注册
                case CheckCodeHandle.LOGIN_BIND:
                    if (progressDialog != null) {
                        progressDialog.setContinueDialog(false);
                    }
                    thirdLoginRes = (ThirdLoginRes) obj;
                    // UiHelper.ShowOneToast(this, "注册完成");
                    if (thirdLoginRes != null) {
                        if ("true".equals(thirdLoginRes.getIfBind())) {
                            (new SharePreferenceUtil(LoginBindActivity.this)).savesAppcntextInfo("", "", "", "", "", "", "", "", "", "", "", "", "", "", "0", "0", "0", "", "", "true", "");

                            LoginRes loginResInfo = new LoginRes();
                            loginResInfo.setHeadImgUrl(thirdLoginRes.getHeadImgUrl());
                            loginResInfo.setHomeAds(thirdLoginRes.getHomeAds());
                            loginResInfo.setMyAcNum(thirdLoginRes.getMyAcNum());
                            loginResInfo.setMyFavAcNum(thirdLoginRes.getMyFavAcNum());
                            loginResInfo.setMySignAcNum(thirdLoginRes.getMySignAcNum());
                            loginResInfo.setNickName(thirdLoginRes.getNickName());
                            loginResInfo.setTags(thirdLoginRes.getTags());
                            loginResInfo.setUserId(thirdLoginRes.getUserId());

                            if (null != AppContext.getAppContext()) {
                                AppContext.getAppContext().setUserId(loginResInfo.getUserId());
                                AppContext.getAppContext().setHeadImgUrl(loginResInfo.getHeadImgUrl());
                                AppContext.getAppContext().setMyAcNum(loginResInfo.getMyAcNum());
                                AppContext.getAppContext().setMyFavAcNum(loginResInfo.getMyFavAcNum());
                                AppContext.getAppContext().setMySignAcNum(loginResInfo.getMySignAcNum());
                                AppContext.getAppContext().setTags(loginResInfo.getTags());
                                StringUtil.appContextTagsListToString(loginResInfo.getTags());// 以字符串的形式
                                AppContext.getAppContext().setHomeAds(loginResInfo.getHomeAds());
                                AppContext.getAppContext().setNickName(loginResInfo.getNickName());
                                AppContext.getAppContext().setIsLogin("true");
                            }
                            Intent intent = new Intent(this, HomeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("loginres", loginResInfo);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        // 该账号还没有绑定的用户，需要进行绑定或者登入
                        else if ("false".equals(thirdLoginRes.getIfBind())) {
                            // 跳转到绑定界面
                            UiHelper.ShowOneToast(this, "绑定失败！");
                            finish();
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

    /**
     * 弹出对话看表示手机号已经注册过了，是否要进行绑定
     */
    private void showDownLoadDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("是否使用当前手机号进行绑定").setPositiveButton("确认绑定", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestBind();// 发送请求绑定 说明账号已经注册过
            }

        }).setNegativeButton("更换手机号", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        }).show();
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 获取短信验证码的相关操作
     */
    final Handler handlers = new Handler() { // handle
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    recLen--;
                    confirmTextView.setText(recLen + "秒");

                    break;
                case 0:
                    confirmTextView.setClickable(true);
                    confirmTextView.setText("重新获取验证码");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public class MyThread implements Runnable { // thread
        @Override
        public void run() {
            while (checkcodeJudge) {
                try {
                    if (recLen == 0) {
                        checkcodeJudge = false;

                        Message message = new Message();
                        message.what = 0;
                        handlers.sendMessage(message);
                    } else {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = new Message();
                        message.what = 1;
                        handlers.sendMessage(message);
                    }

                } catch (Exception e) {
                }
            }
        }
    }
}
