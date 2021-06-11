package com.newland.wstdd.originate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.find.handle.SingleActivityHandle;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.originate.beanrequest.SingleActivityReq;
import com.newland.wstdd.originate.beanresponse.SingleActivityRes;

/**
 * 单个活动获取详细信息
 *
 * @author Administrator
 */
public class SingleActivityActivity extends BaseFragmentActivity implements OnPostListenerInterface {
    private static final String TAG = "SingleActivityActivity";//收集异常日志tag
    Intent intent;
    String activityId;// 活动的id
    SingleActivityRes singleActivityRes;// 服务器的返回信息
    private AppContext appContext;
    SingleActivityHandle handler = new SingleActivityHandle(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        appContext = AppContext.getAppContext();
        setContentView(R.layout.activity_single_activity);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        intent = getIntent();
        activityId = intent.getStringExtra("activityId");
        refresh();// 请求服务器

    }

    @Override
    protected void onDestroy() {
        /** 收集异常日志 */
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /** 收集异常日志 */
        super.onDestroy();
    }

    @Override
    public void OnHandleResultListener(Object obj, int responseId) {
        switch (responseId) {
            // 单个活动详情
            case SingleActivityHandle.SINGLE_ACTIVITY:
                if (progressDialog != null) {
                    progressDialog.setContinueDialog(false);
                }
                singleActivityRes = (SingleActivityRes) obj;
                if (singleActivityRes != null) {
                    // UiHelper.ShowOneToast(this, "获取单个活动信息成功");
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
    protected void processMessage(Message msg) {

    }

    @Override
    public void refresh() {
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {
                    // 需要发送一个request的对象进行请求
                    SingleActivityReq reqInfo = new SingleActivityReq();
                    reqInfo.setActivityId(activityId);

                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<SingleActivityRes> ret = mgr.getSingleActivityInfo(reqInfo);// 泛型类，
                    Message message = new Message();
                    message.what = SingleActivityHandle.SINGLE_ACTIVITY;// 设置死
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

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

}
