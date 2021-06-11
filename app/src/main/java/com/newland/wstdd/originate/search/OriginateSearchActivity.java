/**
 *
 */
package com.newland.wstdd.originate.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.SharePreferenceUtil.SharePreferenceUtil;
import com.newland.wstdd.common.SharePreferenceUtil.SharedPreferencesRefreshUtil;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;

/**
 * 发起--搜索界面
 *
 * @author H81 2015-11-2
 *
 */
public class OriginateSearchActivity extends BaseFragmentActivity implements OnPostListenerInterface {
    private static final String TAG = "OriginateSearchActivity";//收集异常日志tag
    private EditText moriginate_search_edt;// 搜索框
    private ImageView moriginate_search_iv;
    private TextView moriginate_search_tv;// 搜索

    private FragmentManager fragmentManager;

    SharePreferenceUtil sharePreferenceUtil;// 用于获取标签信息
    // 保存搜索记录
    SharedPreferencesRefreshUtil sharedPreferencesRefreshUtil;
    // 用于保存搜索记录
    List<HashMap<String, String>> datasList = new ArrayList<HashMap<String, String>>();
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        setContentView(R.layout.activity_originate_search);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        sharePreferenceUtil = new SharePreferenceUtil(this);
        sharedPreferencesRefreshUtil = new SharedPreferencesRefreshUtil(getApplicationContext());
        datasList = sharedPreferencesRefreshUtil.getSearchInfo(getApplicationContext(), "searchhistory");

        if (datasList.size() > 0) {
            index = datasList.size();
        } else {
            index = 0;
        }
        fragmentManager = getSupportFragmentManager();
        initView();
        AppContext.getAppContext().replaceFragment(fragmentManager, R.id.search_fragment, new OriginateSearchHistoryFragment(OriginateSearchActivity.this));
    }

    @Override
    protected void onDestroy() {
        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /**收集异常日志*/
        super.onDestroy();
    }

    public void initView() {
        moriginate_search_edt = (EditText) findViewById(R.id.originate_search_edt);
        moriginate_search_edt.setFocusable(true);
        moriginate_search_edt.requestFocus();
        moriginate_search_iv = (ImageView) findViewById(R.id.originagte_search_iv);
        moriginate_search_tv = (TextView) findViewById(R.id.originate_search_tv);
        moriginate_search_iv.setOnClickListener(this);
        moriginate_search_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.originate_search_tv:

            case R.id.originagte_search_iv:
                HashMap<String, String> map = new HashMap<String, String>();
                if (StringUtil.isNotEmpty(moriginate_search_edt.getText().toString())) {
                    index++;
                    map.put("historyitem" + index, moriginate_search_edt.getText().toString());
                    datasList.add(map);
                    sharedPreferencesRefreshUtil.saveSearchComment("historyindex", index + "");
                    sharedPreferencesRefreshUtil.saveSearchInfo(getApplicationContext(), "searchhistory", datasList);
                    AppContext.getAppContext().replaceFragment(fragmentManager, R.id.search_fragment,
                            new OriginateSearchResultFragment(OriginateSearchActivity.this, moriginate_search_edt.getText().toString()));
                } else {
                    UiHelper.ShowOneToast(this, "关键字不能为空!");
                }
            default:
                break;
        }

    }

    @Override
    protected void processMessage(Message msg) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void OnHandleResultListener(Object obj, int responseId) {

    }

    @Override
    public void OnFailResultListener(String mess) {
        if (progressDialog != null) {
            progressDialog.setContinueDialog(false);
            UiHelper.ShowOneToast(this, mess);
        }

    }

}
