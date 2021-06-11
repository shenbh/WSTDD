package com.newland.wstdd.find.categorylist.registrationedit.editregistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.EditTextUtil;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.widget.PengTextView;
import com.newland.wstdd.find.categorylist.registrationedit.beaninfo.AdultInfo;
import com.newland.wstdd.find.categorylist.registrationedit.beaninfo.MainSignAttr;
import com.newland.wstdd.find.categorylist.registrationedit.beanrequest.CancelRegistrationReq;
import com.newland.wstdd.find.categorylist.registrationedit.beanrequest.GetEditRegistrationReq;
import com.newland.wstdd.find.categorylist.registrationedit.beanrequest.SubmitRegistrationReq;
import com.newland.wstdd.find.categorylist.registrationedit.beanresponse.CancelRegistrationRes;
import com.newland.wstdd.find.categorylist.registrationedit.beanresponse.EditRegistrationRes;
import com.newland.wstdd.find.categorylist.registrationedit.beanresponse.GetEditRegistrationRes;
import com.newland.wstdd.find.categorylist.registrationedit.beanresponse.SubmitRegistrationRes;
import com.newland.wstdd.find.categorylist.registrationedit.handle.EditRegistrationHandle;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.originate.beanresponse.SingleActivityRes;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 报名信息编辑
 *
 * @author Administrator
 */
public class EditRegistrationEditActivity extends BaseFragmentActivity implements OnPostListenerInterface, IWXAPIEventHandler {
    private static final String TAG = "EditRegistrationEditActivity";//收集异常日志tag
    Intent intent;
    private String mainSignAttrs;// 必填信息项目 是一个以逗号隔开的字符串
    SingleActivityRes singleActivityRes;// 得到前面一个界面传递过来的活动对象 是为了获取必填项
    // 随行人员 ListView相关信息
    EditSxRegistrationEditListViews sxListViews;
    EditSxRegistrationEditAdapter sxAdapter;
    List<EditSxRegistrationEditAdapterData> sxAdapterDatas = new ArrayList<EditSxRegistrationEditAdapterData>();
    // 本人信息
    private List<MainSignAttr> mineAdapterDatas = new ArrayList<MainSignAttr>();
    EditRegistrationEditAdapter mineEditAdapter;// 我自己的报名信息的适配器
    EditSxRegistrationEditListViews mineEditListViews;
    // private List<String> mineAdapterDatas = new ArrayList<String>();
    // 添加随行人员
    TextView addTextView;// 添加随行人员 带有监听事件
    TextView registrationActivityIcon, registrationActivityTitle;// 活动报名中的活动类型图标
    // 跟 活动标题
    //服务器返回的信息
    EditRegistrationRes submitRegistrationRes;
    EditRegistrationHandle handler = new EditRegistrationHandle(this);

    // 取消报名
    private TextView cancelTextView;//取消报名
    CancelRegistrationRes cancelRegistrationRes;
    EditRegistrationHandle cancelHandle = new EditRegistrationHandle(this);
    // 获取报名信息
    GetEditRegistrationRes getEditRegistrationRes;// 获取报名信息 服务器返回
    EditRegistrationHandle getEditRegistrationHandle = new EditRegistrationHandle(this);
    /**
     * 分享相关的
     */
    private PopupWindow popupWindow;// 分享窗口
    // 微信
    private static final String appid = "wx1b84c30d9f380c89";// 微信的appid
    private IWXAPI wxApi;// 微信的API
    // QQ
    private Tencent mTencent;
    private static final String APP_ID = "1104957952";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        setContentView(R.layout.activity_registration_edit);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        intent = getIntent();

        Bundle bundle = intent.getExtras();
        singleActivityRes = (SingleActivityRes) bundle.getSerializable("singleActivity");
        initTitle();// 初始化标题
        initView();// 初始化控件
        getEditRegistrationReq();// 获取当前活动的报名信息
        /**
         * 分享
         */
        // QQ
        final Context ctxContext = this.getApplicationContext();
        mTencent = Tencent.createInstance(APP_ID, ctxContext);
        mHandler = new Handler();
        // weixin
        wxApi = WXAPIFactory.createWXAPI(this, appid);
        wxApi.registerApp(appid);

    }

    @Override
    protected void onDestroy() {
        /** 收集异常日志 */
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /** 收集异常日志 */
        super.onDestroy();
    }
    /**-----------------------服务器请求的相关操作-----------------*/
    /**
     * 请求服务器信息 提交报名
     */
    private void refreshSumit() {
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {
                    // 需要发送一个request的对象进行请求
                    SubmitRegistrationReq reqInfo = new SubmitRegistrationReq();
                    mineAdapterDatas = mineEditAdapter.getRegistrationData();
                    // 主报名信息
                    reqInfo.setMainSignAttr(mineAdapterDatas);
                    // 随行人员报名信息
                    List<AdultInfo> adultInfos = new ArrayList<AdultInfo>();
                    for (int i = 0; i < sxAdapterDatas.size(); i++) {
                        // 添加到adultInfos
                        AdultInfo adultInfo = new AdultInfo();
                        adultInfo.setAdultPersonType(2);
                        adultInfo.setAdultUserName(sxAdapter.getRegistrationData().get(i).getName());
                        List<MainSignAttr> mainSignAttrs = new ArrayList<MainSignAttr>();
                        List<Map<String, String>> values = sxAdapter.getRegistrationData().get(i).getMap();
                        for (Map<String, String> map : values) {
                            for (Entry<String, String> entry : map.entrySet()) {
                                Object key = entry.getKey();
                                Object val = entry.getValue();
                                MainSignAttr mainSignAttr = new MainSignAttr();
                                if (!"".equals((String) key) && key != null) {
                                    mainSignAttr.setName((String) key);
                                }
                                if (!"".equals((String) val) && val != null) {
                                    mainSignAttr.setValue((String) val);
                                }
                                if (!"".equals(mainSignAttr.getName()) && !"".equals(mainSignAttr.getValue()) && mainSignAttr.getValue() != null && mainSignAttr.getName() != null) {
                                    mainSignAttrs.add(mainSignAttr);
                                } else {
                                    break;
                                }

                            }
                        }
                        if (mainSignAttrs.size() > 0) {
                            adultInfo.setAdultSignAttr(mainSignAttrs);
                            adultInfos.add(adultInfo);
                        }

                    }
                    reqInfo.setAdultInfos(adultInfos);
                    // 人员类型
                    reqInfo.setPersonType(1);
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<SubmitRegistrationRes> ret = mgr.getOkEditRegistrationInfo(reqInfo, singleActivityRes.getSignId());// 泛型类，
                    Message message = new Message();
                    message.what = EditRegistrationHandle.OK_EDIT_REGISTRATION;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        submitRegistrationRes = new EditRegistrationRes();
                        submitRegistrationRes.setGetResMess(StringUtil.noNull(ret.getMsg()));
                        message.obj = submitRegistrationRes;
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
     * 取消报名
     */
    private void refreshCancelReg() {
        super.refreshDialog();
        try {
            new Thread() {
                public void run() {
                    // 需要发送一个request的对象进行请求
                    CancelRegistrationReq reqInfo = new CancelRegistrationReq();
                    reqInfo.setActivityId(singleActivityRes.getTddActivity().getActivityId());
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<CancelRegistrationRes> ret = mgr.getCancelRegistrationInfo(reqInfo);// 泛型类，
                    Message message = new Message();
                    message.what = EditRegistrationHandle.CANCEL_REGISTRATION;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        cancelRegistrationRes = new CancelRegistrationRes();
                        cancelRegistrationRes.setGetResMess(StringUtil.noNull(ret.getMsg()));
                        message.obj = cancelRegistrationRes;
                    } else {
                        message.obj = ret.getMsg();
                    }
                    cancelHandle.sendMessage(message);
                }

                ;
            }.start();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    // 从服务器 ------ 获取报名 信息 编辑
    private void getEditRegistrationReq() {
        // TODO Auto-generated method stub
        try {
            new Thread() {
                public void run() {
                    // 需要发送一个request的对象进行请求
                    GetEditRegistrationReq reqInfo = new GetEditRegistrationReq();
                    reqInfo.setSignId(singleActivityRes.getSignId());
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<GetEditRegistrationRes> ret = mgr.getEditRegistrationInfo(reqInfo);// 泛型类，
                    Message message = new Message();
                    message.what = EditRegistrationHandle.GET_EDIT_REGISTRATION;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        if (ret.getObj() != null) {
                            getEditRegistrationRes = (GetEditRegistrationRes) ret.getObj();
                            message.obj = getEditRegistrationRes;
                        } else {
                            getEditRegistrationRes = new GetEditRegistrationRes();
                            message.obj = getEditRegistrationRes;
                        }
                    } else {
                        message.obj = ret.getMsg();
                    }
                    getEditRegistrationHandle.sendMessage(message);
                }

                ;
            }.start();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 设置标题
     */
    private void initTitle() {
        ImageView leftBtn = (ImageView) findViewById(R.id.head_left_iv);
        TextView centerTitle = (TextView) findViewById(R.id.head_center_title);
        centerTitle.setText("编辑报名信息");
        TextView rightTv = (TextView) findViewById(R.id.head_right_tv);
        ImageButton rightBtn = (ImageButton) findViewById(R.id.head_right_btn);
        leftBtn.setVisibility(View.VISIBLE);
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setTextColor(getResources().getColor(R.color.text_red));
        rightTv.setText("提交");
        rightBtn.setVisibility(View.GONE);
        rightBtn.setImageDrawable(getResources().getDrawable(R.drawable.find));
        leftBtn.setOnClickListener(this);
        rightTv.setOnClickListener(this);
    }

    private void test() {
        // TODO Auto-generated method stub
        for (int i = 0; i < 2; i++) {
            EditSxRegistrationEditAdapterData data = new EditSxRegistrationEditAdapterData();
            data.setName("李山川");
            data.setPhone("18750736798");
            List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
            Map<String, String> map1 = new HashMap<String, String>();
            map1.put("姓名", "李山川" + i);
            Map<String, String> map3 = new HashMap<String, String>();
            map3.put("手机", "18750736798");
            maplist.add(map1);
            maplist.add(map3);
            data.setMap(maplist);
            data.setShowRl1(true);
            data.setShowRl2(false);
            data.setShowListView(false);
            data.setInParent(i);
            sxAdapterDatas.add(data);
        }
        /**
         * 这里只是用来测试的，下面的语句需要在真实数据进行填写
         */
        sxAdapter.setRegistrationData(sxAdapterDatas);
        sxListViews.setAdapter(sxAdapter);
        sxAdapter.notifyDataSetChanged();
    }

    public void initView() {
        // TODO Auto-generated method stub
        registrationActivityIcon = (TextView) findViewById(R.id.activity_mine_personalcenter_icon_tv);
        registrationActivityTitle = (TextView) findViewById(R.id.activity_mine_personalcenter_title_tv);
        registrationActivityIcon.setText(StringUtil.intType2Str(singleActivityRes.getTddActivity().getActivityType()));
        registrationActivityTitle.setText(singleActivityRes.getTddActivity().getActivityTitle());
        sxListViews = (EditSxRegistrationEditListViews) findViewById(R.id.registration_sx_listview);
        mineEditListViews = (EditSxRegistrationEditListViews) findViewById(R.id.registration_listview);
        sxAdapter = new EditSxRegistrationEditAdapter(this, sxAdapterDatas);
        sxListViews.setAdapter(sxAdapter);
        mineEditAdapter = new EditRegistrationEditAdapter(this, mineAdapterDatas);
        cancelTextView = (TextView) findViewById(R.id.edit_registration_cancel_registration);
        cancelTextView.setOnClickListener(this);
        addTextView = (TextView) findViewById(R.id.edit_registration_addsx_registration);
        addTextView.setOnClickListener(this);
    }

    // 监听事件
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            //添加随行人员
            case R.id.edit_registration_addsx_registration:
                // 进行判断，只有添加完最后一个才可以进行下一个的判断
                if (sxAdapterDatas.size() > 0) {
                    if (sxAdapterDatas.get(sxAdapterDatas.size() - 1).getName() == null || sxAdapterDatas.get(sxAdapterDatas.size() - 1).getPhone() == null
                            || "".equals(sxAdapterDatas.get(sxAdapterDatas.size() - 1).getName()) || "".equals(sxAdapterDatas.get(sxAdapterDatas.size() - 1).getPhone())) {
                        UiHelper.ShowOneToast(this, "随行人员信息未完成，无法继续添加");
                    } else {

                        EditSxRegistrationEditAdapterData data = new EditSxRegistrationEditAdapterData();
                        data.setName(null);
                        data.setPhone(null);
                        List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
                        List<Map<String, String>> tempInputMaplist = new ArrayList<Map<String, String>>();
                        List<Map<String, String>> tempLastMaplist = new ArrayList<Map<String, String>>();
                        if (singleActivityRes.getTddActivity() != null) {
                            mainSignAttrs = singleActivityRes.getTddActivity().getSignAttr();
                            if (mainSignAttrs != null && !"".equals(mainSignAttrs)) {
                                String[] strs = mainSignAttrs.split(",");
                                // 把String数组输入list
                                for (String substr : strs) {
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put(substr, null);
                                    maplist.add(map);
                                }
                                for (String substr : strs) {
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put(substr, null);
                                    tempInputMaplist.add(map);
                                }
                                for (String substr : strs) {
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put(substr, null);
                                    tempLastMaplist.add(map);
                                }
                            }

                        } else {
                            UiHelper.ShowOneToast(EditRegistrationEditActivity.this, "活动信息为空,无法添加随行人员");
                        }
                        data.setMap(maplist);
                        data.setInputTempList(tempInputMaplist);
                        data.setLastTempList(tempLastMaplist);
                        data.setShowRl1(false);
                        data.setShowRl2(true);
                        data.setShowListView(true);
                        data.setInParent(sxAdapterDatas.size());
                        sxAdapterDatas.add(data);
                        sxAdapter.setRegistrationData(sxAdapterDatas);
                        sxAdapter.notifyDataSetChanged();
                    }
                } else {
                    // 首次添加随行人员的时候，列表是为0开始的
                    EditSxRegistrationEditAdapterData data = new EditSxRegistrationEditAdapterData();
                    data.setName(null);
                    data.setPhone(null);
                    List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
                    List<Map<String, String>> tempInputMaplist = new ArrayList<Map<String, String>>();
                    List<Map<String, String>> tempLastMaplist = new ArrayList<Map<String, String>>();
                    if (singleActivityRes.getTddActivity() != null) {
                        mainSignAttrs = singleActivityRes.getTddActivity().getSignAttr();
                        if (mainSignAttrs != null && !"".equals(mainSignAttrs)) {
                            String[] strs = mainSignAttrs.split(",");
                            // 把String数组输入list
                            for (String substr : strs) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put(substr, null);
                                maplist.add(map);
                            }
                            for (String substr : strs) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put(substr, null);
                                tempInputMaplist.add(map);
                            }
                            for (String substr : strs) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put(substr, null);
                                tempLastMaplist.add(map);
                            }
                        }
                    } else {
                        UiHelper.ShowOneToast(EditRegistrationEditActivity.this, "活动信息为空,无法添加随行人员");
                    }
                    data.setMap(maplist);
                    data.setInputTempList(tempInputMaplist);
                    data.setLastTempList(tempLastMaplist);
                    data.setShowRl1(false);
                    data.setShowRl2(true);
                    data.setShowListView(true);
                    data.setInParent(0);// 首次添加随行人员的时候，


                    sxAdapterDatas.add(data);
                    sxAdapter.setRegistrationData(sxAdapterDatas);
                    sxAdapter.notifyDataSetChanged();

                }
                break;
            case R.id.head_right_tv://提交报名

                boolean isEmpty = false;
                mineAdapterDatas = mineEditAdapter.getRegistrationData();
                for (int i = 0; i < mineAdapterDatas.size(); i++) {
                    if ("".equals(mineAdapterDatas.get(i).getValue())) {
                        UiHelper.ShowOneToast(this, "不能有空信息");
                        isEmpty = true;
                        break;
                    } else if ("手机".equals(mineAdapterDatas.get(i).getName())) {
                        if (!EditTextUtil.checkMobileNumber(mineAdapterDatas.get(i).getValue())) {
                            UiHelper.ShowOneToast(this, "主报名人手机输入有误");
                            isEmpty = true;
                            break;
                        }
                    } else if ("邮箱".equals(mineAdapterDatas.get(i).getName())) {
                        if (!EditTextUtil.checkEmail(mineAdapterDatas.get(i).getValue())) {
                            UiHelper.ShowOneToast(this, "主报名人邮箱输入有误");
                            isEmpty = true;
                            break;
                        }
                    } else if ("身份证".equals(mineAdapterDatas.get(i).getName())) {
                        if (!EditTextUtil.checkChinaIDCard(mineAdapterDatas.get(i).getValue())) {
                            UiHelper.ShowOneToast(this, "主报名人身份证输入有误");
                            isEmpty = true;
                            break;
                        }
                    } else if ("性别".equals(mineAdapterDatas.get(i).getName())) {
                        if (!"男".equals(mineAdapterDatas.get(i).getValue()) && !"女".equals(mineAdapterDatas.get(i).getValue())) {
                            UiHelper.ShowOneToast(this, "主报名人性别只能为男或女");
                            isEmpty = true;
                            break;
                        }
                    } else {
                        continue;
                    }

                }
                if (!isEmpty) {
                    refreshSumit();
//				submitCheck();
                }

                break;
            case R.id.edit_registration_cancel_registration:
                isCancelDeleteCheck();

                break;
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

    //服务器返回信息处理
    @Override
    public void OnHandleResultListener(Object obj, int responseId) {
        try {
            if (progressDialog != null) {
                progressDialog.setContinueDialog(false);
            }
            switch (responseId) {
                // 获取报名信息 编辑  初始化列表
                case EditRegistrationHandle.GET_EDIT_REGISTRATION://0
                    getEditRegistrationRes = (GetEditRegistrationRes) obj;
                    if (getEditRegistrationRes != null) {
                        if (getEditRegistrationRes.getJoinPerson() != null && getEditRegistrationRes.getJoinPerson().size() > 0) {
                            for (int i = 0; i < getEditRegistrationRes.getJoinPerson().size(); i++) {
                                if (getEditRegistrationRes.getJoinPerson().get(i).getPersonType() == 1) {
                                    String tempMainSign = getEditRegistrationRes.getJoinPerson().get(i).getSignAttr();
                                    if (tempMainSign != null && !"".equals(tempMainSign)) {

                                        JSONObject jsonObj = new JSONObject(tempMainSign);
                                        @SuppressWarnings("unchecked")
                                        Iterator<String> nameItr = jsonObj.keys();
                                        String name;
                                        mineAdapterDatas.clear();
                                        while (nameItr.hasNext()) {
                                            name = nameItr.next();
                                            MainSignAttr mainSignAttr = new MainSignAttr();
                                            mainSignAttr.setName(name);
                                            mainSignAttr.setValue(jsonObj.getString(name));
                                            mineAdapterDatas.add(mainSignAttr);
                                        }
                                        Collections.reverse(mineAdapterDatas);// 返回排列下因为json转换成list集合的时候会从后面进行

                                        mineEditAdapter.setRegistrationData(mineAdapterDatas);
                                        mineEditListViews.setAdapter(mineEditAdapter);
                                        mineEditAdapter.notifyDataSetChanged();

                                    }
                                    // 如果没有主要报名人的情况
                                    else {
                                        MainSignAttr mainSignAttr = new MainSignAttr();
                                        mainSignAttr.setName("姓名");
                                        mainSignAttr.setValue(singleActivityRes.getTddActivity().getUserName());
                                        MainSignAttr mainSignAttr1 = new MainSignAttr();
                                        mainSignAttr1.setName("手机");
                                        mainSignAttr1.setValue(singleActivityRes.getTddActivity().getUserMobilePhone());
                                        mineAdapterDatas.add(mainSignAttr);
                                        mineAdapterDatas.add(mainSignAttr1);
                                        mineEditAdapter.setRegistrationData(mineAdapterDatas);
                                        mineEditListViews.setAdapter(mineEditAdapter);
                                        mineEditAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            sxAdapterDatas.clear();
                            //初始化 随行人员的列表
                            for (int i = 0; i < getEditRegistrationRes.getJoinPerson().size(); i++) {
                                if (getEditRegistrationRes.getJoinPerson().get(i).getPersonType() == 2) {
                                    String tempMainSign = getEditRegistrationRes.getJoinPerson().get(i).getSignAttr();
                                    if (tempMainSign != null && !"".equals(tempMainSign)) {
                                        JSONObject jsonObj = new JSONObject(tempMainSign);
                                        @SuppressWarnings("unchecked")
                                        Iterator<String> nameItr = jsonObj.keys();
                                        String name;
                                        EditSxRegistrationEditAdapterData data = new EditSxRegistrationEditAdapterData();
                                        List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
                                        List<Map<String, String>> tempInputMaplist = new ArrayList<Map<String, String>>();
                                        List<Map<String, String>> tempLastMaplist = new ArrayList<Map<String, String>>();
                                        while (nameItr.hasNext()) {

                                            name = nameItr.next();
                                            if ("姓名".equals(name)) {
                                                data.setName(jsonObj.getString(name));
                                            }
                                            if ("手机".equals(name)) {
                                                data.setPhone(jsonObj.getString(name));
                                            }
                                            //第一个hashmap
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put(name, jsonObj.getString(name));
                                            maplist.add(map);
                                            //第二个hashmap
                                            Map<String, String> map1 = new HashMap<String, String>();
                                            map1.put(name, jsonObj.getString(name));
                                            tempInputMaplist.add(map1);
                                            //第三个
                                            Map<String, String> map2 = new HashMap<String, String>();
                                            map2.put(name, jsonObj.getString(name));
                                            tempLastMaplist.add(map2);
                                        }
                                        Collections.reverse(maplist);
                                        Collections.reverse(tempInputMaplist);
                                        Collections.reverse(tempLastMaplist);
                                        data.setMap(maplist);
                                        data.setInputTempList(tempInputMaplist);
                                        data.setLastTempList(tempLastMaplist);
                                        data.setShowRl1(true);
                                        data.setShowRl2(false);
                                        data.setShowListView(false);
                                        data.setInParent(sxAdapterDatas.size());
                                        sxAdapterDatas.add(data);
//							Collections.reverse(sxAdapterDatas);// 返回排列下因为json转换成list集合的时候会从后面进行
                                        sxAdapter.setRegistrationData(sxAdapterDatas);
                                        sxListViews.setAdapter(sxAdapter);
                                        sxAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                        }
                    }
                    break;
                //提交编辑的报名信息
                case EditRegistrationHandle.OK_EDIT_REGISTRATION://1
                    submitRegistrationRes = (EditRegistrationRes) obj;
                    if (submitRegistrationRes != null) {
                        String mess = null;
                        mess = submitRegistrationRes.getGetResMess();
                        /**
                         * 返回成功之后进行分享
                         */

                        getPopWindow();
                    }
                    break;
                //取消报名
                case EditRegistrationHandle.CANCEL_REGISTRATION:
                    cancelRegistrationRes = (CancelRegistrationRes) obj;
                    if (cancelRegistrationRes != null) {
                        UiHelper.ShowOneToast(this, "取消了报名!");
                        if (null != AppContext.getAppContext())
                            AppContext.getAppContext().setMySignAcNum(Integer.valueOf(AppContext.getAppContext().getMySignAcNum()) - 1 + "");
                        Intent intent = new Intent();
                        intent.setAction("Refresh_FindChairDetailActivity");
                        intent.putExtra("registration_state", "我要报名");
                        sendBroadcast(intent);
                        //刷新一下    列表activity
                        Intent intent0 = new Intent();// 切记// 这里的Action参数与IntentFilter添加的Action要一样才可以												
                        intent0.setAction("Refresh_MyActivityListActivity");
                        sendBroadcast(intent0);// 发送广播了
                        //取消报名
                        Intent intent1 = new Intent();
                        intent1.setAction("ManagerPageActivityRefresh_Cancel_Regist");
                        intent1.putExtra("SignState", "noSign");
                        sendBroadcast(intent1);
                        finish();

                        sendSignedDelAddBroadCast();
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


    /**
     * 分享相关的操作
     */

    private void getPopWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            return;
        } else {
            initPopupWindow();
        }
    }

    /**
     * 分享窗口初始化
     */
    private void initPopupWindow() {
        View popupWindow_view = this.getLayoutInflater().inflate(R.layout.popwindow_registration_shares, null, false);
        TextView public_type_tv = (TextView) popupWindow_view.findViewById(R.id.public_type_tv);// 发布类型
        TextView public_title_tv = (TextView) popupWindow_view.findViewById(R.id.public_title_tv);// 发布主题
        public_type_tv.setText(StringUtil.intType2Str(singleActivityRes.getTddActivity().getActivityType()));
        public_title_tv.setText(registrationActivityTitle.getText().toString());
        popupWindow = new PopupWindow(popupWindow_view, AppContext.getAppContext().getScreenWidth() * 4 / 5, LayoutParams.WRAP_CONTENT, true);
        ImageView cancelImageView = (ImageView) popupWindow_view.findViewById(R.id.originate_chair_popwindow_delete);
        cancelImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
                Intent intent = new Intent();
                intent.setAction("Refresh_FindChairDetailActivity");
                intent.putExtra("registration_state", "已报名");
                sendBroadcast(intent);
                finish();

            }
        });
        popupWindow.setOutsideTouchable(false);
        PengTextView weixinFriend, weixinZone, qqFriend;// 弹出窗口三个控件
        weixinFriend = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_weixin);
        weixinZone = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_weixin_zone);
        qqFriend = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_qq);
        // 微信好友分享
        weixinFriend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                friend(v);
            }
        });

        // 微信朋友圈分享
        weixinZone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                friendline(v);
            }
        });

        // qq好友分享
        qqFriend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onClickShareToQQ();
            }
        });

        // 设置透明度
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        // 显示窗口 位置
        popupWindow.showAtLocation(this.findViewById(R.id.layout), Gravity.CENTER, 0, 0);//
        popupWindow.setOutsideTouchable(false);

    }

    /**
     * 分享相关的方法
     *
     * @param v
     */
    public void friend(View v) {
        share(0);
    }

    public void friendline(View v) {
        share(1);
    }

    private void share(int flag) {
        downloadWeiXinImg(flag);

    }

    // 微信分享需要 先去下载封面的图片，然后才会分享出去
    private void downloadWeiXinImg(final int flag) {
        // TODO Auto-generated method stub
        if (singleActivityRes.getTddActivity().getShareContent() != null && singleActivityRes.getTddActivity().getShareImg() != null && singleActivityRes.getTddActivity().getShareUrl() != null) {

            ImageLoader.getInstance().loadImage(singleActivityRes.getTddActivity().getShareImg(), new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String arg0, View arg1) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    // TODO Auto-generated method stub
                    // 下载失败
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = singleActivityRes.getTddActivity().getShareUrl();
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = singleActivityRes.getTddActivity().getActivityTitle();
                    msg.description = singleActivityRes.getTddActivity().getActivityDescription();
                    // 根据ImgUrl下载下来一张图片，弄出bitmap格式
                    // 这里替换一张自己工程里的图片资源
                    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                    msg.setThumbImage(thumb);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                    boolean fla = wxApi.sendReq(req);
                    System.out.println("fla=" + fla);
                }

                @Override
                public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
                    // TODO Auto-generated method stub
                    // 表示下载成功了
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = singleActivityRes.getTddActivity().getShareUrl();
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = singleActivityRes.getTddActivity().getActivityTitle();
                    msg.description = singleActivityRes.getTddActivity().getActivityDescription();
                    // 根据ImgUrl下载下来一张图片，弄出bitmap格式
                    // 这里替换一张自己工程里的图片资源
                    Bitmap thumb = bitmap;
                    msg.setThumbImage(thumb);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                    boolean fla = wxApi.sendReq(req);
                    System.out.println("fla=" + fla);
                }

                @Override
                public void onLoadingCancelled(String arg0, View arg1) {
                    // TODO Auto-generated method stub

                }
            });
        } else {
            UiHelper.ShowOneToast(EditRegistrationEditActivity.this, "第三方分享的内容不能为空!");
            Intent intent = new Intent();
            intent.setAction("Refresh_FindChairDetailActivity");
            intent.putExtra("registration_state", "已报名");
            sendBroadcast(intent);
            finish();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResp(BaseResp arg0) {
        // TODO Auto-generated method stub
        finish();
    }

    private void onClickShareToQQ() {
        Bundle b = getShareBundle();
        if (b != null) {
            shareParams = b;
            Thread thread = new Thread(shareThread);
            thread.start();
        }
    }

    private Bundle getShareBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("title", singleActivityRes.getTddActivity().getActivityTitle());
        bundle.putString("imageUrl", singleActivityRes.getTddActivity().getShareImg());
        bundle.putString("targetUrl", singleActivityRes.getTddActivity().getShareUrl());
        bundle.putString("summary", singleActivityRes.getTddActivity().getActivityDescription());
        bundle.putString("site", "1104957952");
        bundle.putString("appName", "我是TDD");
        return bundle;
    }

    Bundle shareParams = null;

    Handler shareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };

    // 线程类，该类使用匿名内部类的方式进行声明
    Runnable shareThread = new Runnable() {

        public void run() {
            doShareToQQ(shareParams);
            Message msg = shareHandler.obtainMessage();

            // 将Message对象加入到消息队列当中
            shareHandler.sendMessage(msg);

        }
    };

    private void doShareToQQ(Bundle params) {
        mTencent.shareToQQ(EditRegistrationEditActivity.this, params, new BaseUiListener() {
            protected void doComplete(JSONObject values) {
                showResult("shareToQQ:", "分享成功");
            }

            @Override
            public void onError(UiError e) {
                showResult("shareToQQ:", "分享失败未安装登陆第三方");
            }

            @Override
            public void onCancel() {
                showResult("shareToQQ", "取消分享");
            }
        });
    }

    private class BaseUiListener implements IUiListener {

        // @Override
        // public void onComplete(JSONObject response) {
        // // mBaseMessageText.setText("onComplete:");
        // // mMessageText.setText(response.toString());
        // doComplete(response);
        // }

        protected void doComplete(Object values) {

        }

        @Override
        public void onError(UiError e) {
            showResult("onError:", "分享失败未安装登陆第三方");
        }

        @Override
        public void onCancel() {
            showResult("onCancel", "分享取消");
        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            doComplete(arg0);
        }
    }

    private Handler mHandler;

    // qq分享的结果处理
    private void showResult(final String base, final String msg) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                UiHelper.ShowOneToast(EditRegistrationEditActivity.this, msg);
//				popupWindow.dismiss();
//				finish();
            }
        });
    }

    /**
     * 提交验证
     */
    private void submitCheck() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("是否提交？").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                refreshSumit();
            }
        }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    /**
     * 是否取消报名提示
     */
    private void isCancelDeleteCheck() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("是否取消报名？").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                refreshCancelReg();// 取消报名
            }
        }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    /**
     * 发送减少报名人数广播
     */
    private void sendSignedDelAddBroadCast() {
        Intent intent = new Intent();
        intent.setAction("SignedNumChange");
        intent.putExtra("activityId", singleActivityRes.getTddActivity().getActivityId());
        intent.putExtra("signNumType", "del");
        sendBroadcast(intent);
    }


}
