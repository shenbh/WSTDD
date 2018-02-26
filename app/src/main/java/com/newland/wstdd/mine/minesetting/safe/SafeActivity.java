package com.newland.wstdd.mine.minesetting.safe;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.minesetting.beanrequest.SafeReq;
import com.newland.wstdd.mine.minesetting.beanresponse.SafeRes;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;

/**
 * 账户与安全
 * 
 * @author Administrator 2015-12-4
 */
public class SafeActivity extends BaseFragmentActivity implements OnClickListener, OnPostListenerInterface {
	private static final String TAG = "SafeActivity";//收集异常日志tag
	private EditText oldpsw_edt;// 原密码
	private EditText newpsw_edt;// 新密码
	private EditText confirmnewpsw_edt;// 确认新密码

	private SafeRes safeRes;
	private SafeHandle handle = new SafeHandle(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_setting_safe);

		/**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		initView();
		setTitle();
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
			center_tv.setText("账户与安全");
		if (left_btn != null) {
			left_btn.setVisibility(View.VISIBLE);
			left_btn.setImageDrawable(getResources().getDrawable(R.drawable.left_expandsion));
			left_btn.setOnClickListener(this);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {
			right_tv.setText("提交");
			right_tv.setTextColor(getResources().getColor(R.color.red));
			right_tv.setVisibility(View.VISIBLE);
			right_tv.setOnClickListener(this);
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
					SafeReq safeReq = new SafeReq();
					safeReq.setPwd(newpsw_edt.getText().toString());
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<SafeRes> retMsg = mgr.getSafeInfo(safeReq, AppContext.getAppContext().getUserId());
					Message message = new Message();
					message.what = SafeHandle.SAFE_INFO;
					// 访问服务器成功1 否则访问服务器失败
					if (retMsg.getCode() == 1) {
						if (retMsg.getObj() != null) {
							safeRes = (SafeRes) retMsg.getObj();
							message.obj = safeRes;
						} else {
							safeRes = new SafeRes();
							message.obj = safeRes;
						}
					} else {
						message.obj = retMsg.getMsg();
					}
					handle.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void initView() {
		oldpsw_edt = (EditText) findViewById(R.id.oldpsw_edt);
		newpsw_edt = (EditText) findViewById(R.id.newpsw_edt);
		confirmnewpsw_edt = (EditText) findViewById(R.id.confirmnewpsw_edt);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.head_left_iv:
			finish();
			break;
		case R.id.head_right_tv:
			judge();
			break;
		default:
			break;
		}
	}

	/**
	 * 判断
	 * 
	 */
	private void judge() {
		if (StringUtil.isNotEmpty(oldpsw_edt.getText().toString()) || StringUtil.isNotEmpty(newpsw_edt.getText().toString()) || StringUtil.isNotEmpty(confirmnewpsw_edt.getText().toString())) {
			if (null != AppContext.getAppContext() && oldpsw_edt.getText().toString().equals(AppContext.getAppContext().getUserPwd())) {
				if (oldpsw_edt.getText().toString().equals(newpsw_edt.getText().toString())) {
					UiHelper.ShowOneToast(this, "新密码不能与旧密码一样");
				} else if (newpsw_edt.getText().toString().length() < 6) {
					UiHelper.ShowOneToast(this, "密码长度至少6位");
				} else if (newpsw_edt.getText().toString().length() > 20) {
					UiHelper.ShowOneToast(this, "密码长度不能超过20位");
				} else if (newpsw_edt.getText().toString().equals(confirmnewpsw_edt.getText().toString())) {
					refresh();
				} else {
					UiHelper.ShowOneToast(this, "新密码不一致，请重新输入");
				}
			} else {
				UiHelper.ShowOneToast(this, "原密码错误");
			}
		} else {
			UiHelper.ShowOneToast(this, "请填写完整");
		}
	}

	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		try {
			switch (responseId) {
			case SafeHandle.SAFE_INFO:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				safeRes = (SafeRes) obj;
				if (safeRes != null) {
					UiHelper.ShowOneToast(this, "修改密码成功");
					// 保存密码到本地
					AppContext.getAppContext().setUserPwd(newpsw_edt.getText().toString());
					finish();
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
		if (progressDialog != null) {
			progressDialog.setContinueDialog(false);
			UiHelper.ShowOneToast(this, mess);
		}
	}
}
