package com.newland.wstdd.find.hotlist;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.bean.TddActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.view.CustomListViews.IXListViewListener;
import com.newland.wstdd.find.categorylist.detail.FindChairDetailActivity;
import com.newland.wstdd.find.hotlist.bean.FindCategoryReq;
import com.newland.wstdd.find.hotlist.bean.FindCategoryRes;
import com.newland.wstdd.find.hotlist.bean.HotListInfo;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.managerpage.ManagerPageActivity;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.originate.beanrequest.SingleActivityReq;
import com.newland.wstdd.originate.beanresponse.SingleActivityRes;

/**
 * 发现-热门全部数据的列表界面
 *
 * @author Administrator
 */
public class HotListListViewActivity extends BaseFragmentActivity implements IXListViewListener, OnPostListenerInterface {
    private static final String TAG = "HotListListViewActivity";//收集异常日志tag
    private Intent intent;
    private TextView titleTextView;
    private String title;// 标题
    // private String mess;// 跳转过来的信息是哪一个 比如讲座比如团购
    // ListView控件
    private com.newland.wstdd.common.view.CustomListViews findCategoryListViews;
    private HotListListAdapter hotListListAdapter;
    private List<HotListInfo> listViewDatas = new ArrayList<HotListInfo>();

    // 服务器返回信息
    private FindCategoryRes findCategoryRes;
    private HotListHandle handler = new HotListHandle(this);

    private List<TddActivity> acList = new ArrayList<TddActivity>();// 返回的列表对象

    TddActivity tddActivity;
    private int page_index = 0;// 当前页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        setContentView(R.layout.activity_show_find_list_view1);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        setTitle();
        title = "全部热门列表";
        initView(title);// 初始化控件


        IntentFilter intentFilter = new IntentFilter("StatusChange");
        registerReceiver(broadcastReceiver, intentFilter);
        //报名人数变化的广播
        IntentFilter intentFilter2 = new IntentFilter("SignedNumChange");
        registerReceiver(signedNumChangeReceiver, intentFilter2);
    }

    private void setTitle() {
        ImageView leftBtn = (ImageView) findViewById(R.id.head_left_iv);
        TextView centerTitle = (TextView) findViewById(R.id.head_center_title);
        TextView rightTv = (TextView) findViewById(R.id.head_right_tv);
        ImageButton rightBtn = (ImageButton) findViewById(R.id.head_right_btn);
        leftBtn.setVisibility(View.VISIBLE);
        centerTitle.setText("讲座");
        rightTv.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);
        // rightBtn.setImageDrawable(getResources().getDrawable(R.drawable.find));
        leftBtn.setOnClickListener(this);
    }

    private void initView(String string) {
        titleTextView = (TextView) findViewById(R.id.head_center_title);
        titleTextView.setText(string);
        findCategoryListViews = (com.newland.wstdd.common.view.CustomListViews) findViewById(R.id.category_listview);
        findCategoryListViews.setPullLoadEnable(true);
        findCategoryListViews.setXListViewListener(this);

        // test();

        findCategoryListViews.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    // TODO
                    tddActivity = acList.get(arg2 - 1);
                    singleActivitySearch(tddActivity.getActivityId());
                    /*
                     * Intent intent = new Intent(HotListListViewActivity.this,
                     * FindChairDetailActivity.class);// 跳转到详情 Bundle bundle =
                     * new Bundle(); bundle.putSerializable("tddActivity",
                     * tddActivity); intent.putExtras(bundle);
                     * startActivity(intent);
                     */
                } catch (Exception e) {
                    return;
                }
            }
        });
        refresh();
    }

    /**
     * 单个活动查询 用来判断 活动报名人数 是否已经报名 等等
     */
    SingleActivityRes singleActivityRes;

    private void singleActivitySearch(final String activityString) {
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {
                    // 需要发送一个request的对象进行请求
                    SingleActivityReq reqInfo = new SingleActivityReq();
                    reqInfo.setActivityId(activityString);

                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<SingleActivityRes> ret = mgr.getSingleActivityInfo(reqInfo);// 泛型类，
                    Message message = new Message();
                    message.what = HotListHandle.SINGLE_ACTIVITY;// 设置死
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
    protected void processMessage(Message msg) {

    }

    /**
     * 热门列表数据
     */
    @Override
    public void refresh() {
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {
                    // 需要发送一个request的对象进行请求
                    FindCategoryReq reqInfo = new FindCategoryReq();
                    reqInfo.setCity("全国");
                    reqInfo.setActivityType("0");
                    reqInfo.setCity("全国");
                    // activityType 为""表示全部
                    reqInfo.setActivityType("");
                    reqInfo.setPage(page_index + "");
                    reqInfo.setProvince("全国");
                    reqInfo.setRow(AppContext.getAppContext().pageRow);
                    reqInfo.setSearchText("娱乐");
                    reqInfo.setProvince("全国");
                    reqInfo.setSearchText("");
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<FindCategoryRes> ret = mgr.getFindCategoeyInfo(reqInfo);// 泛型类，
                    Message message = new Message();
                    message.what = HotListHandle.HOT_ALLLIST;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        if (page_index == 0) {
                            acList.clear();
                            findCategoryRes = (FindCategoryRes) ret.getObj();
                            acList = findCategoryRes.getAcList();
                        } else {
                            findCategoryRes = (FindCategoryRes) ret.getObj();
                            if (findCategoryRes != null && findCategoryRes.getAcList().size() > 0) {
                                for (int i = 0, size = findCategoryRes.getAcList().size(); i < size; i++) {
                                    TddActivity tddActivity = findCategoryRes.getAcList().get(i);
                                    acList.add(tddActivity);
                                }
                            }
                        }
                        message.obj = findCategoryRes;
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
    public void onListViewRefresh() {

        page_index = 0;
        refresh();// 网络刷新
        onLoad();

    }

    @Override
    public void onListViewLoadMore() {
        page_index++;
        refresh();
        onLoad();
    }

    private void onLoad() {
        findCategoryListViews.stopRefresh();
        findCategoryListViews.stopLoadMore();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_iv:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void OnHandleResultListener(Object obj, int responseId) {
        try {
            switch (responseId) {
                case HotListHandle.HOT_ALLLIST:
                    if (progressDialog != null) {
                        progressDialog.setContinueDialog(false);
                    }
                    findCategoryRes = (FindCategoryRes) obj;
                    List<HotListInfo> list = new ArrayList<HotListInfo>();
                    if (findCategoryRes != null) {
                        /**
                         * 获取到了数据
                         */
                        // UiHelper.ShowOneToast(this, "热门列表数据获取成功");
                        // TODO
                        if (findCategoryRes.getAcList().size() > 0) {
                            list.clear();
                            for (int i = 0, size = findCategoryRes.getAcList().size(); i < size; i++) {
                                HotListInfo hotListInfo = new HotListInfo();
                                hotListInfo.setActivityType(findCategoryRes.getAcList().get(i).getActivityType());
                                hotListInfo.setImgUrl(findCategoryRes.getAcList().get(i).getImage1());
                                hotListInfo.setActivityTitle(findCategoryRes.getAcList().get(i).getActivityTitle());
                                hotListInfo.setFriendActivityTime(findCategoryRes.getAcList().get(i).getFriendActivityTime());
                                hotListInfo.setActivityAddress(findCategoryRes.getAcList().get(i).getActivityAddress());
                                hotListInfo.setSignCount(findCategoryRes.getAcList().get(i).getSignCount());
                                hotListInfo.setNeedPay(findCategoryRes.getAcList().get(i).getNeedPay());
                                hotListInfo.setStatus(findCategoryRes.getAcList().get(i).getStatus());
                                hotListInfo.setActivityId(findCategoryRes.getAcList().get(i).getActivityId());
                                list.add(hotListInfo);
                            }
                            if (page_index == 0) {
                                listViewDatas.clear();
                                listViewDatas.addAll(list);
                                hotListListAdapter = new HotListListAdapter(this, listViewDatas);
                                findCategoryListViews.setAdapter(hotListListAdapter);
                                hotListListAdapter.notifyDataSetChanged();
                            } else {
                                listViewDatas.addAll(list);
                                hotListListAdapter.notifyDataSetChanged();
                            }
                            if (list.size() < Integer.valueOf(AppContext.getAppContext().pageRow)) {
                                findCategoryListViews.setPullLoadEnable(false);
                            } else {
                                findCategoryListViews.setPullLoadEnable(true);
                            }
                        }
                    }
                    break;
                case HotListHandle.SINGLE_ACTIVITY:
                    if (progressDialog != null) {
                        progressDialog.setContinueDialog(false);
                    }
                    singleActivityRes = (SingleActivityRes) obj;
                    if (singleActivityRes != null) {
                        if ("true".equals(singleActivityRes.getIsLeader())) {// 团大
                            Intent intent = new Intent(this, ManagerPageActivity.class);// 跳转到管理
                            Bundle bundle = new Bundle();
                            singleActivityRes.getTddActivity().setSignRole(1);
                            bundle.putSerializable("singleActivityRes", singleActivityRes);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (singleActivityRes.getSignRole() == 2) {// 团秘
                            Intent intent = new Intent(this, ManagerPageActivity.class);// 跳转到管理
                            Bundle bundle = new Bundle();
                            singleActivityRes.getTddActivity().setSignRole(2);
                            bundle.putSerializable("singleActivityRes", singleActivityRes);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (singleActivityRes.getSignRole() == 9) {// 团员
                            Intent intent = new Intent(this, FindChairDetailActivity.class);// 跳转到详情
                            Bundle bundle = new Bundle();
                            singleActivityRes.getTddActivity().setSignRole(9);
                            bundle.putSerializable("singleActivityRes", singleActivityRes);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (singleActivityRes.getSignRole() == 0) {// 没参加
                            Intent intent = new Intent(this, FindChairDetailActivity.class);// 跳转到详情
                            Bundle bundle = new Bundle();
                            singleActivityRes.getTddActivity().setSignRole(0);
                            bundle.putSerializable("singleActivityRes", singleActivityRes);
                            intent.putExtras(bundle);
                            startActivity(intent);
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

    @Override
    public void OnFailResultListener(String mess) {
        if (progressDialog != null) {
            progressDialog.setContinueDialog(false);
            UiHelper.ShowOneToast(this, mess);
        }
    }

    @Override
    public void initView() {

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        updateStatus();
        updateSignNum();
    }

    @Override
    protected void onDestroy() {
        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /**收集异常日志*/
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(signedNumChangeReceiver);
        super.onDestroy();
    }

    /**
     * 更新报名状态
     */
    private void updateStatus() {
        if (listViewDatas != null && listViewDatas.size() > 0) {
            for (int j = 0, size = listViewDatas.size(); j < size; j++) {
                if (activityIdString != null && activityIdString.equals(listViewDatas.get(j).getActivityId())) {
                    if ("关闭报名".equals(statusString)) {
                        listViewDatas.get(j).setStatus(2);//状态  0.未发布 1.报名中 2.关闭报名 4.已撤销
                        break;
                    } else if ("开启报名".equals(statusString)) {
                        listViewDatas.get(j).setStatus(1);
                        break;
                    }
                }
            }
            if (hotListListAdapter != null) {
                hotListListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 更新报名人数
     */
    private void updateSignNum() {
        for (int i = 0, size = listViewDatas.size(); i < size; i++) {
            if (activityIdString2 != null && activityIdString2.equals(listViewDatas.get(i).getActivityId())) {
                if ("add".equals(signNumTypeString)) {//增加
                    listViewDatas.get(i).setSignCount(listViewDatas.get(i).getSignCount() + 1);
                    break;
                } else if ("del".equals(signNumTypeString)) {
                    listViewDatas.get(i).setSignCount(listViewDatas.get(i).getSignCount() - 1);
                    break;
                }
            }
        }
        if (hotListListAdapter != null) {
            hotListListAdapter.setHotFragmentListInfo(listViewDatas);
            hotListListAdapter.notifyDataSetChanged();
        }
    }

    //广播返回
    private String activityIdString;//活动id
    private String statusString;//报名状态
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            activityIdString = intent.getStringExtra("activityId");
            statusString = intent.getStringExtra("status");
        }
    };

    //报名人数变化的广播
    private String activityIdString2;//活动id
    private String signNumTypeString;//报名人数增加还是减少
    BroadcastReceiver signedNumChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            activityIdString2 = intent.getStringExtra("activityId");
            signNumTypeString = intent.getStringExtra("signNumType");
        }
    };
}
