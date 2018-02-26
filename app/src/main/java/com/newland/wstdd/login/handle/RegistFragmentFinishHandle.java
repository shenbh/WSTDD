package com.newland.wstdd.login.handle;

import android.R.integer;
import android.os.Handler;
import android.os.Message;

import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.login.beanresponse.LoginRes;
import com.newland.wstdd.login.beanresponse.RegistFirstRes;
import com.newland.wstdd.login.beanresponse.RegistSecondRes;
import com.newland.wstdd.login.beanresponse.ThirdLoginRes;
import com.newland.wstdd.login.login.LoginFragment;
import com.newland.wstdd.login.regist.RegistFinishActivity;
import com.newland.wstdd.login.regist.RegistFragment;

public class RegistFragmentFinishHandle extends Handler {
	public static final int REGIST_SECOND = 0;//第一次注册请求
	public static final int THIRD_LOGIN = 1;//第三方登录
	private RegistFinishActivity context;
	public RegistFragmentFinishHandle(RegistFinishActivity registFinishActivity) {
		this.context = registFinishActivity;
	}

	@SuppressWarnings("static-access")
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case REGIST_SECOND:
			// 只有当msg.obj对象为空，表示访问服务器成功
			if (msg.obj != null) {
				if (msg.obj instanceof String) {
					context.OnFailResultListener((String)msg.obj);
				} else if (msg.obj instanceof RegistSecondRes) {
					context.OnHandleResultListener(msg.obj, REGIST_SECOND);
				}
			}
			break;
		case THIRD_LOGIN:
			// 只有当msg.obj对象为空，表示访问服务器成功
			if (msg.obj != null) {
				if (msg.obj instanceof String) {
					context.OnFailResultListener((String) msg.obj);
				} else if (msg.obj instanceof ThirdLoginRes) {
//					com.newland.wstdd.common.tools.UiHelper.ShowOneToast(context.getActivity(), "请求成功了");
					context.OnHandleResultListener(msg.obj, THIRD_LOGIN);
				}
			}
			break;
		}
		super.handleMessage(msg);
	}

}
