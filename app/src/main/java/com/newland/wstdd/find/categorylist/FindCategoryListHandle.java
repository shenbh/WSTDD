package com.newland.wstdd.find.categorylist;

import android.os.Handler;
import android.os.Message;

import com.newland.wstdd.find.hotlist.bean.FindCategoryRes;
import com.newland.wstdd.originate.beanresponse.SingleActivityRes;

/**
 * 发现列表的处理
 *
 * @author Administrator
 */
public class FindCategoryListHandle extends Handler {
    public static final int FIND_LIST = 0;//发现热门活动
    public static final int SINGLE_ACTIVITY = 30;//发现热门活动
    private ShowFindListViewActivity context;

    public FindCategoryListHandle(ShowFindListViewActivity context) {
        this.context = context;
    }


    public void handleMessage(Message msg) {
        switch (msg.what) {
            case FIND_LIST:
                // 只有当msg.obj对象为空，表示访问服务器成功
                if (msg.obj != null) {
                    if (msg.obj instanceof String) {
                        context.OnFailResultListener((String) msg.obj);
                    } else if (msg.obj instanceof FindCategoryRes) {
                        context.OnHandleResultListener(msg.obj, FIND_LIST);
                    }
                }
                break;
            case SINGLE_ACTIVITY:
                // 只有当msg.obj对象为空，表示访问服务器成功
                if (msg.obj != null) {
                    if (msg.obj instanceof String) {
                        context.OnFailResultListener((String) msg.obj);
                    } else if (msg.obj instanceof SingleActivityRes) {
                        context.OnHandleResultListener(msg.obj, SINGLE_ACTIVITY);
                    }
                }
                break;
        }
        super.handleMessage(msg);
    }

}
