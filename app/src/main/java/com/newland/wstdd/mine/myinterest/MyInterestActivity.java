package com.newland.wstdd.mine.myinterest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.myinterest.DragGridView.OnChanageListener;
import com.newland.wstdd.mine.myinterest.beanrequest.MyInterestTagsReq;
import com.newland.wstdd.mine.myinterest.beanrequest.TddUserTagQuery;
import com.newland.wstdd.mine.myinterest.beanresponse.MyInterestTagsRes;
import com.newland.wstdd.mine.myinterest.handle.MyInterestHandle;
import com.newland.wstdd.mine.myinterest.selectinterest.SelectInterestActivity;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;

/**
 * 我的兴趣 1.编辑状态----有打X的状态就是点击可以删除 2.完成状态----去除打X 3.可以添加更多
 *
 * @author Administrator
 */
public class MyInterestActivity extends BaseFragmentActivity implements OnClickListener, OnPostListenerInterface {
    private static final String TAG = "MyInterestActivity";//收集异常日志tag
    private List<String> tagNamesList = new ArrayList<String>();// 暂时保存兴趣标签
    private boolean isShowEdit = false;// 点击编辑的时候显示出编辑的界面 就是显示出X
    // 点击完成的时候就是提交，同时呢X消失
    boolean isCanDeltet = false;// 用来区分长按跟短按
    // 要是长按的话，就不用弹出是否删除的对话框isCanDelete的false
    // 相反就是true
    // 数据
    private List<TddUserTagQuery> myTags = new ArrayList<TddUserTagQuery>();// 感兴趣标签对象
    private List<TddUserTagQuery> allTags = new ArrayList<TddUserTagQuery>();// 所有感兴趣的标签

    private DragAdapter imageAdapter;// 兴趣界面的item
    private DragGridView dragGridView = null; // 取得组件
    private Handler handler = new MyInterestHandle(this);
    // 控件
    private TextView title;// 顶部的标题
    private TextView topRightTextView;//
    private LinearLayout addInterestLayout;// 添加
    private Button addButton;
    private LinearLayout reLayout;// 长按的监听事件
    private TextView interestNameTextView;// 表示点击的时候，那个item中的name
    private TextView isDeleteTextView;// 表示点击的时候，那个item中的X的符号
    // 点击之后获取的两个值 一个是运动的名称 是否显示删除 在列表中的位置
    String interestName = null;
    String isDeleteInterest = null;
    IntentFilter filter;
    // 服务器返回的内容
    private MyInterestTagsRes myInterestTagsRes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        setContentView(R.layout.activity_my_interest);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        imageAdapter = new DragAdapter(this, allTags);
        initView();// 初始化控件
        refresh();
    }

    // 初始化控件
    public void initView() {
        // TODO Auto-generated method stub
        try {

            filter = new IntentFilter("ShopFragment");
            registerReceiver(broadcastReceiver, filter);
            RelativeLayout includeLayout = (RelativeLayout) findViewById(R.id.interest_top_rr);

            title = (TextView) includeLayout.findViewById(R.id.head_center_title);
            title.setText("我的兴趣");
            topRightTextView = (TextView) includeLayout.findViewById(R.id.head_right_btns);
            topRightTextView.setVisibility(View.VISIBLE);
            // topRightTextView.setTextColor(Color.red(R.color.text_red));
            // 谨记这样写是严重的错误 会造成控件被隐藏
            topRightTextView.setTextColor(getResources().getColor(R.color.text_red));
            topRightTextView.setText("编辑");

            this.dragGridView = (DragGridView) findViewById(R.id.fragment_shop_myGridView);
            this.dragGridView.setAdapter(imageAdapter);

            addButton = (Button) findViewById(R.id.add_bt);
            addButton.setOnClickListener(this);
            addInterestLayout = (LinearLayout) findViewById(R.id.add_interest_ll);
            addInterestLayout.setOnClickListener(this);
            reLayout = (LinearLayout) findViewById(R.id.fragment_shop_re);// gridview的布局
            dragGridView.setOnChangeListener(new OnChanageListener() {

                @Override
                public void onChange(int from, int to) {

                }

                @Override
                public void updatePosition(int from, int to) {
                    // TODO Auto-generated method stub
                    try {
                        isCanDeltet = true;
                        imageAdapter.notifyDataSetChanged();
                        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                        dragGridView.setLayoutParams(params);
                        dragGridView.setStretchMode(GridView.NO_STRETCH);
                        dragGridView.setColumnWidth((int) (AppContext.getAppContext().getScreenWidth() / 4.5));
                        dragGridView.setHorizontalSpacing(AppContext.getAppContext().getScreenWidth() / 50);
                        dragGridView.setVerticalSpacing(AppContext.getAppContext().getScreenWidth() / 500);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }

            });

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void changeEditState(Boolean isEditState) {

        // TODO Auto-generated method stub
        // 设置可以编辑的状态
        if (isEditState) {// 刪除

            if (!isShowEdit) {
                isShowEdit = true;
                dragGridView.setDrag(true);
            }
        } else {

            if (isShowEdit) {

                isShowEdit = false;
                dragGridView.setDrag(false);
            } else {
                interestName = "";

            }
        }

        imageAdapter.setIsShowDelete(isShowEdit);
    }

    /**
     * 上传PLU设置的信息到服务器上去
     */
    public synchronized void updatePluToServer() {
    }

    /**
     * 获取商店水果信息
     */
    public synchronized void getShopFragmentInfo() {
        // TODO Auto-generated method stub
        new Thread() {
            public void run() {
            }

            ;
        }.start();

    }

    /**
     * 接收到广播之后 在这里要做三件事情 1.更新数据到界面上去 2.更新pluinfoList的值 3.上传到服务器
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    protected void processMessage(Message msg) {
        // TODO Auto-generated method stub

    }

    // 确认是否要被删除

//	private void deleteTipDialog(final int p, boolean isShowDelete2) {
//
//		Dialog dialog = new AlertDialog.Builder(this)
//
//		.setTitle("") // 创建标题
//
//				.setMessage("您确认要删除吗？") // 表示对话框中的内容
//
//				.setIcon(R.drawable.app_icon_del) // 设置LOGO
//
//				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//
//						imageAdapter.delete(p);
//
//						imageAdapter.setIsShowDelete(isShowEdit);
//
//						imageAdapter.notifyDataSetChanged();
//
//					}
//
//				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//
//						// 不做任何操作就是消失的意思
//
//					}
//
//				}).create(); // 创建了一个对话框
//
//		dialog.setCanceledOnTouchOutside(false);
//
//		dialog.show(); // 显示对话框
//
//	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_right_btns:

                if ("编辑".equals(topRightTextView.getText().toString())) {
                    isCanDeltet = true;
                    changeEditState(true);
                    topRightTextView.setText("完成");
                } else {
                    topRightTextView.setText("编辑");
                    isCanDeltet = false;
                    changeEditState(false);
                    updateMyInterest();

                }
                break;
            case R.id.add_bt:
                Intent intent = new Intent(MyInterestActivity.this, SelectInterestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("myTags", (Serializable) myTags);
                bundle.putSerializable("allTags", (Serializable) allTags);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                break;
            case R.id.add_interest_ll:
                Intent intent1 = new Intent(MyInterestActivity.this, SelectInterestActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("myTags", (Serializable) myTags);
                bundle1.putSerializable("allTags", (Serializable) allTags);
                intent1.putExtras(bundle1);
                startActivityForResult(intent1, 1);
                break;
            default:
                break;
        }
        super.onClick(v);
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // String result = data.getExtras().getString("result");//得到新Activity
        // 关闭后返回的数据
        // 数据是使用Intent返回
        switch (requestCode) {
            case 1:
                if (resultCode == 01) {
                    List<TddUserTagQuery> infos = new ArrayList<TddUserTagQuery>();
                    Bundle bundle = data.getExtras();
                    infos = (List<TddUserTagQuery>) bundle.getSerializable("result");
                    int len = allTags.size();
                    if (infos != null && !infos.isEmpty()) {
                        myTags.clear();
                        for (int i = 0; i < infos.size(); i++) {
                            myTags.add(infos.get(i));

                        }
                    } else {
                        myTags.clear();
                    }
                    imageAdapter.setListData(myTags);
                    imageAdapter.notifyDataSetChanged();
                }
                break;

            default:
                break;
        }

    }

    //访问服务器   更新兴趣标签
    private void updateMyInterest() {
        // TODO Auto-generated method stub
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {

                    // 需要发送一个request的对象进行请求
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    MyInterestTagsReq myInterestTagsReq = new MyInterestTagsReq();
                    RetMsg<MyInterestTagsRes> ret = null;
                    if (myTags != null && !myTags.isEmpty()) {
                        myInterestTagsReq.setMyTags(myTags);
                        ret = mgr.getMyInterestUpdateInfo(myInterestTagsReq);// 泛型类，
                    } else {
                        ret = mgr.getMyInterestUpdateInfo(myInterestTagsReq);// 泛型类，
                    }
                    Message message = new Message();
                    message.what = MyInterestHandle.MY_INTEREST;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        myInterestTagsRes = (MyInterestTagsRes) ret.getObj();
                        message.obj = myInterestTagsRes;
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

    // 异步任务访问服务器
    @Override
    public void refresh() {
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {

                    // 需要发送一个request的对象进行请求
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    MyInterestTagsReq myInterestTagsReq = new MyInterestTagsReq();
                    RetMsg<MyInterestTagsRes> ret = null;
                    if (myTags != null && !myTags.isEmpty()) {
                        myInterestTagsReq.setMyTags(myTags);
                        ret = mgr.getMyInterestInfo(myInterestTagsReq);// 泛型类，
                    } else {
                        ret = mgr.getMyInterestInfo(myInterestTagsReq);// 泛型类，
                    }
                    Message message = new Message();
                    message.what = MyInterestHandle.MY_INTEREST;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        myInterestTagsRes = (MyInterestTagsRes) ret.getObj();
                        message.obj = myInterestTagsRes;
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

    // 分享服务器的内容
    @Override
    public void OnHandleResultListener(Object obj, int responseId) {
        switch (responseId) {
            case MyInterestHandle.MY_INTEREST:
                if (progressDialog != null) {
                    progressDialog.setContinueDialog(false);
                }
                try {
                    myInterestTagsRes = (MyInterestTagsRes) obj;
                    if (myInterestTagsRes != null) {
                        myTags.clear();
                        allTags.clear();
                        myTags = myInterestTagsRes.getMyTags();
                        allTags = myInterestTagsRes.getAllTags();
                        for (int i = 0; i < myTags.size(); i++) {
                            myTags.get(i).setUserId(AppContext.getAppContext().getUserId());

                        }
                        for (int i = 0; i < allTags.size(); i++) {
                            allTags.get(i).setUserId(AppContext.getAppContext().getUserId());

                        }
                        tagNamesList.clear();
                        for (int i = 0; i < myTags.size(); i++) {
                            tagNamesList.add(myTags.get(i).getTagName());
                        }

                        AppContext.getAppContext().setTags(tagNamesList);
                        if (AppContext.getAppContext().getSearchTags().size() == 0) {
                            AppContext.getAppContext().setSearchTags(tagNamesList);
                        }
                        StringUtil.appContextTagsListToString(tagNamesList);//以字符串的形式

                        // 更新界面上的信息
                        imageAdapter.setListData(myTags);
                        imageAdapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }

    }

    // 服务器接收失败的请求
    @Override
    public void OnFailResultListener(String mess) {
        if (progressDialog != null) {
            progressDialog.setContinueDialog(false);
            UiHelper.ShowOneToast(this, mess);
        }
    }

    @Override
    protected void onDestroy() {
        /** 收集异常日志 */
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /** 收集异常日志 */
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
