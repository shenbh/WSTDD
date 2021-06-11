package com.newland.wstdd.mine.servicecenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.smsphone.CallPhoneUtil;

/**
 * 服务中心界面
 *
 * @author Administrator 2015-12-9
 */
public class MineServiceCenterActivity extends BaseFragmentActivity implements OnClickListener {
    private static final String TAG = "MineServiceCenterActivity";//收集异常日志tag
    private ImageView mService_headimg_iv;//头像
    private TextView mService_kefu_tv;//客服 我是团太太
    private TextView mService_time_tv;//当班时间
    private com.newland.wstdd.common.view.RoundAngleImageView mService_twocode_iv;//二维码图片

    /*private ImageView mService_twocode_iv;//二维码图片
     */    private TextView mService_weixin_tv;//微信号
    private TextView mService_weixintwocode_tv;//微信扫描二维码
    private TextView mService_qq_tv;//qq
    private com.newland.wstdd.common.widget.PengTextView mService_tel_ptv;//tel
    private com.newland.wstdd.common.widget.PengTextView mService_phone_ptv;//phone
    private TextView mService_email_tv;//email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        setContentView(R.layout.activity_mine_servicecenter);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        setTitle();
        initView();
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
        ImageView leftBtn = (ImageView) findViewById(R.id.head_left_iv);
        TextView centerTitle = (TextView) findViewById(R.id.head_center_title);
        TextView rightTv = (TextView) findViewById(R.id.head_right_tv);
        ImageButton rightBtn = (ImageButton) findViewById(R.id.head_right_btn);
        leftBtn.setVisibility(View.VISIBLE);
        centerTitle.setText("服务中心");
        // rightTv.setText("发布");
        rightTv.setVisibility(View.GONE);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(getResources().getDrawable(R.drawable.feedback_red));
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
    }

    @Override
    protected void processMessage(Message msg) {
    }

    @Override
    public void initView() {
        mService_headimg_iv = (ImageView) findViewById(R.id.service_headimg_iv);
        mService_kefu_tv = (TextView) findViewById(R.id.service_kefu_tv);
        mService_time_tv = (TextView) findViewById(R.id.service_time_tv);
        mService_twocode_iv = (com.newland.wstdd.common.view.RoundAngleImageView) findViewById(R.id.service_twocode_iv);
        /*mService_twocode_iv = (ImageView) findViewById(R.id.service_twocode_iv);*/
        mService_weixin_tv = (TextView) findViewById(R.id.service_weixin_tv);
        mService_weixintwocode_tv = (TextView) findViewById(R.id.service_weixintwocode_tv);
        mService_qq_tv = (TextView) findViewById(R.id.service_qq_tv);
        mService_tel_ptv = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.service_tel_ptv);
        mService_phone_ptv = (com.newland.wstdd.common.widget.PengTextView) findViewById(R.id.service_phone_ptv);
        mService_tel_ptv.setOnClickListener(this);
        mService_phone_ptv.setOnClickListener(this);
        mService_email_tv = (TextView) findViewById(R.id.service_email_tv);
        if (null != AppContext.getAppContext()) {
            mService_headimg_iv.getLayoutParams().width = AppContext.getAppContext().getScreenWidth() / 4 + 10;
            mService_headimg_iv.getLayoutParams().height = AppContext.getAppContext().getScreenWidth() / 4 + 10;

            mService_twocode_iv.getLayoutParams().height = AppContext.getAppContext().getScreenWidth() / 3 + 20;
            mService_twocode_iv.getLayoutParams().width = AppContext.getAppContext().getScreenWidth() / 3 + 20;
        }
        mService_twocode_iv.setImageDrawable(getResources().getDrawable(R.drawable.tuantaitai_weixincode));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_iv:
                finish();
                break;
            case R.id.head_right_btn:

                break;
            case R.id.service_tel_ptv:
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("是否拨打400-100-8060").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        CallPhoneUtil.callPhone("400-100-8060", MineServiceCenterActivity.this);
                    }
                }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;
            case R.id.service_phone_ptv:
                AlertDialog dialog1 = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("是否拨打13959198060").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog1, int which) {
                        CallPhoneUtil.callPhone("400-100-8060", MineServiceCenterActivity.this);
                    }
                }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                    }
                }).show();
                break;
            default:
                break;
        }
    }


}
