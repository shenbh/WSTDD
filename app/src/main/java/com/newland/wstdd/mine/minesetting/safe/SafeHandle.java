package com.newland.wstdd.mine.minesetting.safe;

import com.newland.wstdd.mine.minesetting.beanresponse.SafeRes;
import com.newland.wstdd.mine.minesetting.beanresponse.VersionRes;

import android.R.integer;
import android.os.Handler;
import android.os.Message;

/**
 * 账户与安全
 *
 * @author Administrator 2015-12-4
 */
public class SafeHandle extends Handler {
    public static final int SAFE_INFO = 0; // 账户与安全
    private SafeActivity context;

    public SafeHandle(SafeActivity context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SAFE_INFO:
                // 只有当msg.obj对象为空，表示访问服务器成功
                if (msg.obj != null) {
                    if (msg.obj instanceof String) {
                        context.OnFailResultListener((String) msg.obj);
                    } else if (msg.obj instanceof SafeRes) {
                        context.OnHandleResultListener(msg.obj, SAFE_INFO);
                    }
                }
                break;
            default:
                break;
        }
        super.handleMessage(msg);
    }
}
