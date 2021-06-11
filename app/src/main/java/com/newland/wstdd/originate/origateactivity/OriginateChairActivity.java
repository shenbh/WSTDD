/**
 *
 */
package com.newland.wstdd.originate.origateactivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.bean.TddActivity;
import com.newland.wstdd.common.common.ActivityType;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.fileupload.FormFile;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.selectphoto.AlbumsActivity;
import com.newland.wstdd.common.selectphoto.PhotoUpImageItem;
import com.newland.wstdd.common.tools.DateTimePickDialogUtil;
import com.newland.wstdd.common.tools.DateUtil;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.tools.UiToUiHelper;
import com.newland.wstdd.common.widget.PengTextView;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.login.beanresponse.HeadImgRes;
import com.newland.wstdd.mine.beanresponse.TddUserCertificate;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.netutils.MessageUtil;
import com.newland.wstdd.netutils.WBResponse;
import com.newland.wstdd.originate.beanrequest.SelectMustItemInfo;
import com.newland.wstdd.originate.handle.SingleActivityPublishHandle;
import com.newland.wstdd.originate.origateactivity.beanrequest.SingleActivityPublishReq;
import com.newland.wstdd.originate.origateactivity.beanresponse.SingleActivityPublishRes;
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
 * 发起--讲座
 *
 * @author H81 2015-11-5
 *
 */
public class OriginateChairActivity extends BaseFragmentActivity implements OnPostListenerInterface, IWXAPIEventHandler {
    private static final String TAG = "OriginateChairActivity";//收集异常日志tag
    TextView addPhotoIcon;// 没有图片的时候添加图片的标志
    private TddActivity editTddActivity;// 这里表示的是从服务器返回的一个活动的id，不管是编辑还是发布都会有这个id
    private Intent intent;// 接收首页传递过来的值
    // 活动的类型id
    private int activityType;// 活动的类型
    // 活动的动作
    private String activityAction;// 是发起还是编辑
    // 图片上传的相关信息
    private List<String> filePathList;// 本地图片的列表
    private List<String> imgUrl = new ArrayList<String>();// 服务器返回的图片列表
    private List<FormFile> formFiles;
    private HttpMultipartPostOriginate post;
    // 活动图片选择的相关操作
    private GridView selected_images_gridv;// 活动图片的数据
    private ArrayList<PhotoUpImageItem> arrayList = new ArrayList<PhotoUpImageItem>();// 活动图片的选择
    private SelectedImagesAdapter adapter;// 活动图片的适配器
    // 必选项
    private ScrollView mustScrollView;// 必选项容器
    private GridView selectGridView;// 必选项的网格
    private ArrayList<SelectMustItemInfo> selectItemList = new ArrayList<SelectMustItemInfo>();// 必选项的数据
    private SelectedMustAdapter selectItemAdapter;// 必选项的适配器
    private ImageView mustImageView;// 必选项的图表指向
    private View must_line;// 必选项下划线
    // 必填项的选择
    private EditText mOriginate_originateactivity_title_edt;// 开讲主题
    private TextView mOriginate_originateactivity_starttime_tv;// 开始时间
    private TextView mOriginate_originateactivity_starttime_tv_hint;// 开始时间  提示的
    private EditText mOriginate_originateactivity_address_edt;// 地址
    private EditText mOriginate_originateactivity_limitperson_edt;// 人数限制
    private TextView mOriginate_originateactivity_endtime_tv;// 报名截止
    private TextView mOriginate_originateactivity_endtime_tv_hint;// 报名截止 提示的
    private TextView mOriginate_originateactivity_mustselect_tv;// 必填项
    private EditText mOriginate_originateactivity_describe_edt;// 填写描述，让更多的人参与
    private PengTextView mOriginate_chair_remain_tv;// 输入*/2500 字

    private IntentFilter filter;// 定一个广播接收过滤器
    private LinearLayout mOriginate_chair_theme_ll;// 讲座主题ll
    private LinearLayout mOriginate_chair_starttime_ll;// 开讲时间ll
    private LinearLayout mOriginate_chair_starttime_ll1;// 开讲时间ll
    private LinearLayout mOriginate_chair_location_ll;// 开讲地点ll
    private LinearLayout mOriginate_chair_endtime_ll;// 报名截止ll
    private LinearLayout mOriginate_chair_endtime_ll1;// 报名截止ll
    private LinearLayout mOriginate_chair_numnolimit_ll;// 人数不限ll 设置一个监听事件
    private LinearLayout mOriginate_chair_mustselect_ll;// 必选项
    private RelativeLayout mOriginate_chair_nonempty_rl;// 高级内容
    private LinearLayout shortCutLayout;
    private String initStartDateTime = ""; // 日期时间控件初始化开始时间
    private int inputNum = 2500;// 限制输入的字数

    // 服务器相关的
    private SingleActivityPublishRes singleActivityPublishRes;// 服务器返回的信息
    private SingleActivityPublishHandle handler = new SingleActivityPublishHandle(this);
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

    // private TddActivity editTddActivity;//从活动编辑界面发送过来

    @Override
    protected void processMessage(Message msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        setContentView(R.layout.activity_originate_chair);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        initStartDateTime = DateUtil.getDateNowString2();// 获取当前时间格式为 2013年9月3日
        // 获取到activity_type
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        activityType = bundle.getInt("activity_type");// 默认为0
        activityAction = bundle.getString("activity_action");
        editTddActivity = (TddActivity) bundle.getSerializable("tddActivity");

        // 注册广播
        filter = new IntentFilter("ORIGUNATE_CHAIR_PHOTO");
        registerReceiver(broadcastReceiver, filter);// 在对象broadcastReceiver中进行接收的相应处理
        // 分享
        // QQ
        final Context ctxContext = this.getApplicationContext();
        mTencent = Tencent.createInstance(APP_ID, ctxContext);
        mHandler = new Handler();
        // weixin
        wxApi = WXAPIFactory.createWXAPI(this, appid);
        wxApi.registerApp(appid);
        initView();// 控件的绑定 包括初始化必选项 图片
        setTitle();// 设置标题    同时根据活动类型进行相应的初始化   比如  讲座  旅游   等等

        if (editTddActivity == null) {
            initMustSelect();// 初始化必选的五项
        }
        initMust();// 添加必选项中的“+”这个选项
        init();// 图片选择初始化
        setclickListener();

    }


    // 初始化必选的五项
    private void initMustSelect() {
        // TODO Auto-generated method stub
        // 初始化必选的两个
        if (selectItemList != null && !selectItemList.isEmpty()) {
            selectItemList.clear();
        } else {
            SelectMustItemInfo mustItem1 = new SelectMustItemInfo();
            mustItem1.setSelectItem("姓名");
            mustItem1.setSelect(true);
            selectItemList.add(mustItem1);
            SelectMustItemInfo mustItem2 = new SelectMustItemInfo();
            mustItem2.setSelectItem("手机");
            mustItem2.setSelect(true);
            selectItemList.add(mustItem2);
            SelectMustItemInfo mustItem3 = new SelectMustItemInfo();
            mustItem3.setSelectItem("身份证");
            mustItem3.setSelect(false);
            selectItemList.add(mustItem3);
            SelectMustItemInfo mustItem4 = new SelectMustItemInfo();
            mustItem4.setSelectItem("邮箱");
            mustItem4.setSelect(false);
            selectItemList.add(mustItem4);
            SelectMustItemInfo mustItem5 = new SelectMustItemInfo();
            mustItem5.setSelectItem("性别");
            mustItem5.setSelect(false);
            selectItemList.add(mustItem5);

            if (editTddActivity != null) {
                mOriginate_originateactivity_mustselect_tv.setText(editTddActivity.getSignAttr().replace(",", "/"));
            } else {
                mOriginate_originateactivity_mustselect_tv.setText("姓名/手机");
            }

        }
    }

    private void setTitle() {
        ImageView leftBtn = (ImageView) findViewById(R.id.head_left_iv);
        TextView centerTitle = (TextView) findViewById(R.id.head_center_title);
        TextView rightTv = (TextView) findViewById(R.id.head_right_tv);// 发布的操作
        leftBtn.setVisibility(View.VISIBLE);
        if ("publish".equals(activityAction)) {
            rightTv.setText("发布");
        }
        if ("edit".equals(activityAction)) {
            rightTv.setText("保存");
        }
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        /**
         * 运动   通知   旅游   聚会   活动  会议
         */
        //0.高级 1.讲座 2.通知 3.会议 4.运动 5.招聘 6.旅游 7.众筹 8.赛事 9.聚会 10.投票
        //31.高级快捷活动 32.一句话 33.一张图片 34.一段语音  50.团购 99.微店',
        if (activityType == ActivityType.typeChair) {
            centerTitle.setText("发起讲座");
            mOriginate_originateactivity_starttime_tv_hint.setHint("开讲时间");
        } else if (activityType == ActivityType.typeSporting) {
            centerTitle.setText("发起运动");
            mOriginate_originateactivity_title_edt.setHint("运动内容");
            mOriginate_originateactivity_address_edt.setHint("运动地点");
            mOriginate_chair_endtime_ll.setVisibility(View.GONE);//报名截止隐藏掉
            mOriginate_chair_mustselect_ll.setVisibility(View.GONE);//必选项隐藏掉
        } else if (activityType == ActivityType.typeNotification) {
            centerTitle.setText("发起通知");
            mOriginate_originateactivity_title_edt.setHint("通知主题");
            mOriginate_originateactivity_endtime_tv_hint.setHint("通知有效期");
            mOriginate_chair_starttime_ll.setVisibility(View.GONE);//开始时间 隐藏
            mOriginate_chair_location_ll.setVisibility(View.GONE);//地点隐藏
            mOriginate_chair_numnolimit_ll.setVisibility(View.GONE);//人数限制隐藏
            mOriginate_chair_mustselect_ll.setVisibility(View.GONE);//必选项隐藏

        } else if (activityType == ActivityType.typeTravel) {
            centerTitle.setText("发起旅游");
            mOriginate_originateactivity_title_edt.setHint("旅游主题");
            mOriginate_chair_location_ll.setVisibility(View.GONE);//地址隐藏
        } else if (activityType == ActivityType.typeParty) {
            centerTitle.setText("发起聚会");
            mOriginate_originateactivity_title_edt.setHint("聚会主题,如ktv、party、联谊...");
            mOriginate_originateactivity_address_edt.setHint("聚会地点");
        } else if (activityType == ActivityType.typeActivity) {
            centerTitle.setText("发起活动");
            mOriginate_originateactivity_title_edt.setHint("活动主题");
            mOriginate_originateactivity_address_edt.setHint("活动地点");
        } else if (activityType == ActivityType.typeMeeting) {
            centerTitle.setText("发起会议");
            mOriginate_originateactivity_title_edt.setHint("会议主题");
            mOriginate_originateactivity_address_edt.setHint("地址");
            mOriginate_chair_endtime_ll.setVisibility(View.GONE);//报名截止隐藏掉
            mOriginate_chair_numnolimit_ll.setVisibility(View.GONE);//限制选择隐藏掉
        } else if (activityType == ActivityType.typeShortCut) {
            centerTitle.setText("快捷发布");
            shortCutLayout.setVisibility(View.GONE);
        }
        //提示隐藏的文字信息


    }

    public void initView() {
        addPhotoIcon = (TextView) findViewById(R.id.originate_chair_addphoto_icon);
        addPhotoIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                addPhotoIcon.setVisibility(View.GONE);
                Intent intent = new Intent(OriginateChairActivity.this, AlbumsActivity.class);
                arrayList.remove(arrayList.size() - 1);
                intent.putExtra("selectImageList", arrayList);
                intent.putExtra("activityType", "OriginateChairActivity");
                startActivity(intent);
            }
        });
        mOriginate_originateactivity_title_edt = (EditText) findViewById(R.id.originate_originateactivity_title_edt);// 主题
        mOriginate_originateactivity_starttime_tv = (TextView) findViewById(R.id.originate_chair_starttime_tv);// 开始时间
        mOriginate_originateactivity_starttime_tv_hint = (TextView) findViewById(R.id.originate_chair_starttime_tv1);//开始时间的提示
        mOriginate_originateactivity_address_edt = (EditText) findViewById(R.id.originate_originateactivity_address_edt);// 地址
        mOriginate_originateactivity_endtime_tv = (TextView) findViewById(R.id.originate_chair_endtime_tv);// 报名截止
        mOriginate_originateactivity_endtime_tv_hint = (TextView) findViewById(R.id.originate_chair_endtime_tv1);
        mOriginate_originateactivity_limitperson_edt = (EditText) findViewById(R.id.originate_originateactivity_limitperson_edt);// 人数限制
        mOriginate_originateactivity_mustselect_tv = (TextView) findViewById(R.id.originate_originateactivity_signattr_tv);// 必填项
        mOriginate_originateactivity_describe_edt = (EditText) findViewById(R.id.originate_chair_describe_edt);// 描述

        //解决edittext和Scrollview滑动条冲突问题
        mOriginate_originateactivity_describe_edt.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mustScrollView = (ScrollView) findViewById(R.id.activity_originate_chair_mustscrollview);// 必选项的容器
        mustImageView = (ImageView) findViewById(R.id.activity_originate_chair_mustselect_icon);
        selected_images_gridv = (GridView) findViewById(R.id.selected_images_gridv);
        selected_images_gridv.setVisibility(View.GONE);
        must_line = (View) findViewById(R.id.must_line);
        must_line.setVisibility(View.GONE);
        selectGridView = (GridView) findViewById(R.id.selected_must_gridv);
        mOriginate_chair_theme_ll = (LinearLayout) findViewById(R.id.originate_chair_theme_ll);
        mOriginate_chair_starttime_ll = (LinearLayout) findViewById(R.id.originate_chair_starttime_ll);
        mOriginate_chair_starttime_ll1 = (LinearLayout) findViewById(R.id.originate_chair_starttime_ll1);
        mOriginate_chair_location_ll = (LinearLayout) findViewById(R.id.originate_chair_location_ll);
        mOriginate_chair_endtime_ll = (LinearLayout) findViewById(R.id.originate_chair_endtime_ll);
        mOriginate_chair_endtime_ll1 = (LinearLayout) findViewById(R.id.originate_chair_endtime_ll1);
        mOriginate_chair_numnolimit_ll = (LinearLayout) findViewById(R.id.originate_chair_numnolimit_ll);
        mOriginate_chair_mustselect_ll = (LinearLayout) findViewById(R.id.originate_chair_mustselect_ll);
        mOriginate_chair_mustselect_ll.setOnClickListener(this);// 设置监听事件
        // 显示隐藏必选项
        mOriginate_chair_remain_tv = (PengTextView) findViewById(R.id.originate_chair_remain_tv);
        mOriginate_originateactivity_starttime_tv = (TextView) findViewById(R.id.originate_chair_starttime_tv);
        shortCutLayout = (LinearLayout) findViewById(R.id.activity_originate_ll);//快捷发布
        mOriginate_chair_starttime_ll.setOnClickListener(this);
        mOriginate_chair_starttime_ll1.setOnClickListener(this);
        mOriginate_originateactivity_endtime_tv = (TextView) findViewById(R.id.originate_chair_endtime_tv);
        mOriginate_chair_endtime_ll.setOnClickListener(this);
        mOriginate_chair_endtime_ll1.setOnClickListener(this);
        // 限制输入的个数
        mOriginate_originateactivity_describe_edt.addTextChangedListener(new TextWatcher() {
            private boolean isOutOfBounds = false;
            int end;

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > inputNum) {
                    isOutOfBounds = true;
                } else {
                    mOriginate_chair_remain_tv.setText(s.length() + "/2500");
                    isOutOfBounds = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (isOutOfBounds) {
                    UiHelper.ShowOneToast(OriginateChairActivity.this, "字符超过了");
                    if (s.length() > inputNum) {
                        s.delete(inputNum, s.length());
                        end = inputNum;
                    } else if (s.length() > 20 && s.length() <= inputNum) {
                        s.delete(20, s.length());
                        end = 20;
                    }
                    end = s.length();
                    mOriginate_originateactivity_describe_edt.setSelection(end);// 设置光标在最后
                    mOriginate_chair_remain_tv.setText(s.length() + "/2500");
                }
            }
        });

        /**
         * 若果是编辑活动的话，就将传过来的值，填写到界面上去
         */
        if (editTddActivity != null) {
            mOriginate_originateactivity_title_edt.setText(editTddActivity.getActivityTitle());
            mOriginate_originateactivity_starttime_tv.setText(editTddActivity.getActivityTime().substring(0, editTddActivity.getActivityTime().lastIndexOf(":")));
            mOriginate_originateactivity_address_edt.setText(editTddActivity.getActivityAddress());
            mOriginate_originateactivity_endtime_tv.setText(editTddActivity.getOffTime().substring(0, editTddActivity.getOffTime().lastIndexOf(":")));
            mOriginate_originateactivity_limitperson_edt.setText(editTddActivity.getLimitPerson() + "");
            mOriginate_originateactivity_mustselect_tv.setText(editTddActivity.getSignAttr().replace(",", "/"));
            // 显示必选项
            String[] strs = editTddActivity.getSignAttr().split(",");
            for (String substr : strs) {
                SelectMustItemInfo selectMustItemInfo = new SelectMustItemInfo();
                selectMustItemInfo.setSelect(true);
                selectMustItemInfo.setSelectItem(substr);
                selectItemList.add(selectMustItemInfo);
            }

            mOriginate_originateactivity_describe_edt.setText(editTddActivity.getActivityDescription());// 描述

            // 显示图片 首先根据
            PhotoUpImageItem photoUpImageItem1 = new PhotoUpImageItem();
            photoUpImageItem1.setCover(true);
            photoUpImageItem1.setImageId(editTddActivity.getImage1());
            photoUpImageItem1.setImagePath(editTddActivity.getImage1());
            photoUpImageItem1.setSelected(true);
            arrayList.add(photoUpImageItem1);
            // 这里需要注意 11，22 11
            if (editTddActivity.getImage() != null && !"".equals(editTddActivity.getImage())) {
                String[] imgstr = editTddActivity.getImage().split(",");
                for (String substr : imgstr) {
                    PhotoUpImageItem photoUpImageItem = new PhotoUpImageItem();
                    photoUpImageItem.setCover(false);
                    photoUpImageItem.setImageId(substr);
                    photoUpImageItem.setImagePath(substr);
                    photoUpImageItem.setSelected(true);
                    arrayList.add(photoUpImageItem);
                }
            }

            if (arrayList.size() > 0) {
                addPhotoIcon.setVisibility(View.GONE);
                selected_images_gridv.setVisibility(View.VISIBLE);
            } else {
                addPhotoIcon.setVisibility(View.VISIBLE);
                selected_images_gridv.setVisibility(View.GONE);
            }
        }

    }

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
        View popupWindow_view = this.getLayoutInflater().inflate(R.layout.activity_originate_chair_popwindow, null, false);
        TextView public_type_tv = (TextView) popupWindow_view.findViewById(R.id.public_type_tv);// 发布类型
        TextView public_title_tv = (TextView) popupWindow_view.findViewById(R.id.public_title_tv);// 发布主题
        public_title_tv.setText(mOriginate_originateactivity_title_edt.getText().toString());
        public_title_tv.setText(StringUtil.intType2Str(activityType));
        popupWindow = new PopupWindow(popupWindow_view, AppContext.getAppContext().getScreenWidth() * 4 / 5, LayoutParams.WRAP_CONTENT, true);
        Button cancelButton = (Button) popupWindow_view.findViewById(R.id.originate_chair_popwindow_delete);
        View originate_chair_popwindow_delete_line = (View) popupWindow_view.findViewById(R.id.originate_chair_popwindow_delete_line);
        cancelButton.setVisibility(View.GONE);
        originate_chair_popwindow_delete_line.setVisibility(View.GONE);
        ImageView cancelImageView = (ImageView) popupWindow_view.findViewById(R.id.originate_chair_popwindow_image_delete);
        cancelImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
                finish();
            }
        });
        popupWindow.setOutsideTouchable(false);
        // popupWindow.setAnimationStyle(R.style.pop_anim);
        // popupWindow_view.setOnTouchListener(new OnTouchListener() {
        //
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // if (popupWindow != null && popupWindow.isShowing()) {
        // popupWindow.dismiss();
        // popupWindow = null;
        // }
        // return false;
        // }
        // });

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

    // 发布活动
    @Override
    public void refresh() {
        try {
            new Thread() {
                public void run() {
                    // 必填项
                    StringBuffer signAttr = new StringBuffer();
                    for (int i = 0, size = selectItemList.size(); i < size; i++) {
                        if (selectItemList.get(i).isSelect()) {
                            signAttr.append(selectItemList.get(i).getSelectItem());
                            signAttr.append(",");
                        }
                    }
                    if (signAttr != null && !"".equals(signAttr)) {
                        signAttr.deleteCharAt(signAttr.lastIndexOf(","));

                    }
                    // 需要发送一个request的对象进行请求
                    SingleActivityPublishReq reqInfo = new SingleActivityPublishReq();
                    if (editTddActivity == null) {
                        editTddActivity = new TddActivity();
                    }
                    editTddActivity.setActivityTitle(mOriginate_originateactivity_title_edt.getText().toString());// 活动主题
                    editTddActivity.setActivityTime(mOriginate_originateactivity_starttime_tv.getText().toString());// 活动时间
                    editTddActivity.setActivityAddress(mOriginate_originateactivity_address_edt.getText().toString());// 活动地址
                    editTddActivity.setOffTime(mOriginate_originateactivity_endtime_tv.getText().toString());// 报名截止
                    if (StringUtil.isNotEmpty(mOriginate_originateactivity_limitperson_edt.getText().toString())) {
                        editTddActivity.setLimitPerson(Integer.valueOf(mOriginate_originateactivity_limitperson_edt.getText().toString()));// 限制人数
                    } else {
                        editTddActivity.setLimitPerson(0);// 限制人数
                    }

                    editTddActivity.setSignAttr(signAttr.toString());// 报名属性（用户必填项）
                    // tddActivity.setSignAttr("姓名,手机");// 报名属性（用户必填项）
                    editTddActivity.setActivityDescription(mOriginate_originateactivity_describe_edt.getText().toString());// 活动内容
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).isCover()) {
                            editTddActivity.setImage1(arrayList.get(i).getImagePath());
                            break;// 设置完封面就可以退出循环了
                        } else {
                            editTddActivity.setImage1(arrayList.get(0).getImagePath());
                        }
                    }

                    // 必须的地点 省份 跟 城市
                    editTddActivity.setProvince("全国");
                    editTddActivity.setCity("全国");
                    // 封装图片
                    StringBuffer imgAttr = new StringBuffer();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (!"".equals(arrayList.get(i).getImageId())) {
                            imgAttr.append(arrayList.get(i).getImagePath());
                            if (i != arrayList.size() - 1) {
                                imgAttr.append(",");
                            }

                        }
                    }

                    // for (int j = 0; j < imgUrl.size; j++) {
                    //
                    // }
                    editTddActivity.setImage(imgAttr.toString());
                    editTddActivity.setActivityType(activityType);
                    TddUserCertificate tddUserCertificate = new TddUserCertificate();
                    tddUserCertificate.setUserId(AppContext.getAppContext().getUserId());
                    tddUserCertificate.setNickName(AppContext.getAppContext().getNickName());
                    tddUserCertificate.setUserName(AppContext.getAppContext().getUserName());
//					tddUserCertificate.setCerStatus(1);
//					tddUserCertificate.setSex(1);
                    tddUserCertificate.setHeadimgurl(AppContext.getAppContext().getHeadImgUrl());
                    tddUserCertificate.setMobilePhone(AppContext.getAppContext().getMobilePhone());
                    tddUserCertificate.setEmail(AppContext.getAppContext().getEmail());
                    tddUserCertificate.setIdentity(AppContext.getAppContext().getIdentity());
                    reqInfo.setTddActivity(editTddActivity);
                    reqInfo.setTddUserCertificate(tddUserCertificate);
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<SingleActivityPublishRes> ret = null;
                    if ("publish".equals(activityAction)) {
                        ret = mgr.getSingleActivityPublishInfo(reqInfo, activityAction, "");// 泛型类，
                    } else if ("edit".equals(activityAction)) {
                        ret = mgr.getSingleActivityPublishInfo(reqInfo, activityAction, editTddActivity.getActivityId());// 泛型类，
                    }

                    Message message = new Message();
                    message.what = SingleActivityPublishHandle.SINGLE_ACTIVITY_PUBLISH;// 设置死
                    // 访问服务器成功 1 否则访问服务器失败
                    if (ret.getCode() == 1) {
                        singleActivityPublishRes = (SingleActivityPublishRes) ret.getObj();
                        message.obj = singleActivityPublishRes;
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

    // 必选项的的尾部添加操作
    private void initMust() {

        // 选择必选项的操作
        if (selectItemList != null) {
            // if(selectItemList.size()<7){
            SelectMustItemInfo mustItem = new SelectMustItemInfo();
            mustItem.setSelectItem("+");
            mustItem.setSelect(false);
            selectItemList.add(mustItem);
            // }

        } else {
            selectItemList = new ArrayList<SelectMustItemInfo>();
            SelectMustItemInfo mustItem = new SelectMustItemInfo();
            mustItem.setSelectItem("+");
            mustItem.setSelect(false);
            selectItemList.add(mustItem);
        }

        selectItemAdapter = new SelectedMustAdapter(OriginateChairActivity.this, selectItemList);
        selectGridView.setAdapter(selectItemAdapter);
        selectItemAdapter.notifyDataSetChanged();
    }

    // 选择图片的相关操作
    @SuppressWarnings("unchecked")
    private void init() {
        // arrayList = (ArrayList<PhotoUpImageItem>)
        // getIntent().getSerializableExtra("selectIma");
        if (arrayList != null) {
            if (arrayList.size() < 7) {// 如果
                PhotoUpImageItem lastItem = new PhotoUpImageItem();
                lastItem.setImageId("");
                lastItem.setImagePath("");
                lastItem.setSelected(true);
                arrayList.add(lastItem);
            }

        } else {
            arrayList = new ArrayList<PhotoUpImageItem>();
            PhotoUpImageItem lastItem = new PhotoUpImageItem();
            lastItem.setImageId("");
            lastItem.setImagePath("");
            lastItem.setSelected(true);
            arrayList.add(lastItem);
        }

        adapter = new SelectedImagesAdapter(OriginateChairActivity.this, arrayList);
        selected_images_gridv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    /**
     * 监听事件
     */
    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 开始时间
            case R.id.originate_chair_starttime_ll:
                DateTimePickDialogUtil startTimePicKDialog = new DateTimePickDialogUtil(OriginateChairActivity.this, initStartDateTime);
                startTimePicKDialog.dateTimePicKDialog(mOriginate_originateactivity_starttime_tv);
                break;
            case R.id.originate_chair_starttime_ll1:
                DateTimePickDialogUtil startTimePicKDialog1 = new DateTimePickDialogUtil(OriginateChairActivity.this, initStartDateTime);
                startTimePicKDialog1.dateTimePicKDialog(mOriginate_originateactivity_starttime_tv);
                break;
            // 结束时间
            case R.id.originate_chair_endtime_ll:
                DateTimePickDialogUtil endTimePicKDialog = new DateTimePickDialogUtil(OriginateChairActivity.this, initStartDateTime);
                endTimePicKDialog.dateTimePicKDialog(mOriginate_originateactivity_endtime_tv);
                break;
            case R.id.originate_chair_endtime_ll1:
                DateTimePickDialogUtil endTimePicKDialog1 = new DateTimePickDialogUtil(OriginateChairActivity.this, initStartDateTime);
                endTimePicKDialog1.dateTimePicKDialog(mOriginate_originateactivity_endtime_tv);
                break;
            case R.id.head_left_iv:// 返回
                finish();
                break;
            // 必须选项 显示出必选项的gridview
            case R.id.originate_chair_mustselect_ll:
                if (mustScrollView.getVisibility() == View.VISIBLE) {
                    mustImageView.setImageDrawable(getResources().getDrawable(R.drawable.originate_chair_right_expand_icon));
                    mustScrollView.setVisibility(View.GONE);
                    must_line.setVisibility(View.GONE);
                } else {
                    mustScrollView.setVisibility(View.VISIBLE);
                    mustImageView.setImageDrawable(getResources().getDrawable(R.drawable.originate_chair_down_expand_icon));
                    must_line.setVisibility(View.VISIBLE);
                }
                selectItemAdapter.setArrayList(selectItemList);
                selectItemAdapter.notifyDataSetChanged();
                break;

            // 发布活动
            case R.id.head_right_tv:
                // 发布的时候需要进行判断日期的开始日期是否小于结束日期
                if (null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin()) || "false".equals(AppContext.getAppContext().getIsLogin())) {
                    // UiHelper.ShowOneToast(OriginateChairActivity.this,
                    // "该操作需要登录后进行！");
                    UiToUiHelper.showLogin(OriginateChairActivity.this);
                } else if ("true".equals(AppContext.getAppContext().getIsLogin())) {
                    String startTimeString = mOriginate_originateactivity_starttime_tv.getText().toString();
                    String endTimeString = mOriginate_originateactivity_endtime_tv.getText().toString();
                    //根据不同的activity需要进行的判断相关的字符串
                    if (judgeStringOk(startTimeString, endTimeString)) {

                        upload();
                    }
                }
                break;
            default:
                break;
        }
    }


    private void setclickListener() {
        // 必须选项
        selectGridView.setOnItemClickListener(new OnItemClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // 这里的arraylist是包含空图片的// 点击的是最后一个且为空
                if (position == selectItemList.size() - 1 && "+".equals(selectItemList.get(position).getSelectItem())) {
                    // 判断是否为添加标志 弹出对话框添加
                    final EditText et = new EditText(OriginateChairActivity.this);

                    et.setBackground(null);
                    AlertDialog dialog = new AlertDialog.Builder(OriginateChairActivity.this).setTitle("温馨提示").setMessage("请输入新增选项").setView(et)
                            .setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String input = et.getText().toString();
                                    // TODO 比对内容是否已有
                                    boolean isSame = false;
                                    if (input.trim().equals("")) {
                                        Toast.makeText(getApplicationContext(), "内容不能为空！", Toast.LENGTH_LONG).show();
                                    } else {
                                        for (int i = 0; i < selectItemList.size(); i++) {
                                            isSame = input.equals(selectItemList.get(i).getSelectItem());
                                            if (isSame) {
                                                Toast.makeText(getApplicationContext(), "内容不能相同！" + input, Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                        }
                                        if (!isSame) {
                                            // 添加进了选项
                                            SelectMustItemInfo tempInfo = new SelectMustItemInfo();
                                            tempInfo.setSelect(true);
                                            tempInfo.setSelectItem(input);
                                            selectItemList.set(selectItemList.size() - 1, tempInfo);
                                            StringBuffer signAttr = new StringBuffer();
                                            for (int i = 0, size = selectItemList.size(); i < size; i++) {
                                                if (selectItemList.get(i).isSelect()) {
                                                    signAttr.append(selectItemList.get(i).getSelectItem());
                                                    signAttr.append("/");
                                                }
                                            }
                                            signAttr.deleteCharAt(signAttr.lastIndexOf("/"));
                                            mOriginate_originateactivity_mustselect_tv.setText(signAttr.toString());
                                            initMust();// 添加“+”项
                                        }
                                    }
                                }
                            }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    dialog.setCanceledOnTouchOutside(false);

                } else {
                    // 点击其它的
                    if (position != 0 && position != 1) {
                        if (selectItemList.get(position).isSelect()) {
                            selectItemList.get(position).setSelect(false);
                        } else {
                            selectItemList.get(position).setSelect(true);
                        }
                        selectItemAdapter.setArrayList(selectItemList);
                        selectItemAdapter.notifyDataSetChanged();
                        StringBuffer signAttr = new StringBuffer();
                        for (int i = 0, size = selectItemList.size(); i < size; i++) {
                            if (selectItemList.get(i).isSelect()) {
                                signAttr.append(selectItemList.get(i).getSelectItem());
                                signAttr.append("/");
                            }
                        }
                        signAttr.deleteCharAt(signAttr.lastIndexOf("/"));
                        mOriginate_originateactivity_mustselect_tv.setText(signAttr.toString());
                    }
                }
                selectItemAdapter.setArrayList(selectItemList);
                selectItemAdapter.notifyDataSetChanged();
            }

        });

        // 相册选项
        selected_images_gridv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 这里的arraylist是包含空图片的
                if (position == arrayList.size() - 1 && "".equals(arrayList.get(position).getImageId())) {// 点击的是最后一个且为空
                    // 判断是否为添加标志
                    Intent intent = new Intent(OriginateChairActivity.this, AlbumsActivity.class);
                    arrayList.remove(arrayList.size() - 1);
                    intent.putExtra("selectImageList", arrayList);
                    intent.putExtra("activityType", "OriginateChairActivity");
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(OriginateChairActivity.this, SelectedCoverActivity.class);
                    if ("".equals(arrayList.get(arrayList.size() - 1).getImageId())) {
                        arrayList.remove(arrayList.size() - 1);
                    }

                    intent.putExtra("selectImageList", arrayList);
                    intent.putExtra("position", position);// 点击哪一个就跳转到哪一个，然后在设置封面界面上显示出当前的界面
                    startActivity(intent);

                    // 删除
                    // PhotoUpImageItem photoUpImageItem = (PhotoUpImageItem)
                    // parent
                    // .getItemAtPosition(position);
                    // showIsDeleteDialog(photoUpImageItem, position);
                }
            }

        });
    }

    @SuppressWarnings("unused")
    private void showIsDeleteDialog(final PhotoUpImageItem photoUpImageItem, final int position) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("是否删除所选图片").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // 如果是删除最后一个item的话，需要添加 添加图片
                arrayList.remove(photoUpImageItem);
                adapter.setArrayList(arrayList);
                adapter.notifyDataSetChanged();
                /**
                 * 如果全部为充满的话 arraylist是为8张 且没有空图片 如果没有全部充满的话 arraylist是比如6张
                 * 实际是5张的，包括一张空白的
                 */
                // 添加空图片
                if (arrayList.size() == 6 && !"".equals(arrayList.get(arrayList.size() - 1).getImageId())) {// 删除最后一张
                    PhotoUpImageItem lastItem = new PhotoUpImageItem();
                    lastItem.setImageId("");
                    lastItem.setImagePath(null);
                    lastItem.setSelected(true);
                    arrayList.add(lastItem);
                }

            }
        }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 接收到广播之后 在这里要做三件事情 1.更新数据到界面上去 2.更新pluinfoList的值 3.上传到服务器
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @SuppressWarnings("unchecked")
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            ArrayList<PhotoUpImageItem> tempArrayList = new ArrayList<PhotoUpImageItem>();

            // 这里注意需要区分已有的网络下载下来的跟相册选择的
            /**
             * 第一种：发布活动的时候，所选的图片都是来自相册的 arrayList=(ArrayList<PhotoUpImageItem>)
             * intent.getSerializableExtra("selectIma"); 第二种：有网络的也有本地的
             *
             *
             * 另一种是设置封面界面的时候传过来的，需要用接
             */
            // 对选择图片的那张 标签图片进行隐藏跟显示处理

            if ("cover".equals(intent.getStringExtra("type"))) {
                arrayList = (ArrayList<PhotoUpImageItem>) intent.getSerializableExtra("selectIma");
            } else {
                if (editTddActivity != null) {
                    tempArrayList = (ArrayList<PhotoUpImageItem>) intent.getSerializableExtra("selectIma");

                    for (int i = 0; i < tempArrayList.size(); i++) {
                        File file = new File(tempArrayList.get(i).getImagePath());
                        if (!file.exists()) {
                            tempArrayList.remove(tempArrayList.get(i));
                            file.delete();
                            i -= 1;
                        }
                    }
                    // 去除已经存在的
                    for (int j = 0; j < arrayList.size(); j++) {
                        File file = new File(arrayList.get(j).getImagePath());
                        if (file.exists()) {
                            arrayList.remove(arrayList.get(j));
                            j -= 1;
                        } else {
                            file.delete();
                        }
                    }
                    for (int i = 0; i < tempArrayList.size(); i++) {
                        PhotoUpImageItem photoUpImageItem = new PhotoUpImageItem();
                        photoUpImageItem.setCover(false);
                        photoUpImageItem.setImageId(tempArrayList.get(i).getImageId());
                        photoUpImageItem.setImagePath(tempArrayList.get(i).getImagePath());
                        photoUpImageItem.setSelected(tempArrayList.get(i).isSelected());
                        arrayList.add(photoUpImageItem);
                    }
                    tempArrayList.clear();

                } else {
                    arrayList = (ArrayList<PhotoUpImageItem>) intent.getSerializableExtra("selectIma");
                }
            }
            if (arrayList.size() > 0) {
                addPhotoIcon.setVisibility(View.GONE);
                selected_images_gridv.setVisibility(View.VISIBLE);
            } else {
                addPhotoIcon.setVisibility(View.VISIBLE);
                selected_images_gridv.setVisibility(View.GONE);
            }
            // arrayList=(ArrayList<PhotoUpImageItem>)
            // intent.getSerializableExtra("selectIma");
            init();

        }
    };

    @Override
    public void onDestroy() {
        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /**收集异常日志*/

        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    // 服务器返回结果
    @Override
    public void OnHandleResultListener(Object obj, int responseId) {
        try {
            switch (responseId) {
                case SingleActivityPublishHandle.SINGLE_ACTIVITY_PUBLISH:
                    if (progressDialog != null) {
                        progressDialog.setContinueDialog(false);
                    }
                    singleActivityPublishRes = (SingleActivityPublishRes) obj;
                    if (singleActivityPublishRes != null) {
                        editTddActivity = singleActivityPublishRes.getTddActivity();
                        if (null != AppContext.getAppContext() && "publish".equals(activityAction)) {
                            AppContext.getAppContext().setMyAcNum(Integer.valueOf(AppContext.getAppContext().getMyAcNum()) + 1 + "");
                            getPopWindow();
                        } else {

                            Intent intent0 = new Intent();// 切记
                            // 这里的Action参数与IntentFilter添加的Action要一样才可以
                            intent0.setAction("RECEIVE_TDDACTIVITY");
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("tddactivity", editTddActivity);
                            intent0.putExtras(bundle);
                            sendBroadcast(intent0);// 发送广播了
                            finish();
                            UiHelper.ShowOneToast(OriginateChairActivity.this, "编辑成功!");
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

    // 服务器失败结果
    @Override
    public void OnFailResultListener(String mess) {
        if (progressDialog != null) {
            progressDialog.setContinueDialog(false);
            UiHelper.ShowOneToast(OriginateChairActivity.this, mess);
        }

    }

    /**
     * 分享的相关操作
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
        if (editTddActivity.getShareContent() != null && editTddActivity.getShareImg() != null && editTddActivity.getShareUrl() != null) {

            ImageLoader.getInstance().loadImage(editTddActivity.getShareImg(), new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String arg0, View arg1) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    // TODO Auto-generated method stub
                    // 下载失败
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = editTddActivity.getShareUrl();
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = editTddActivity.getActivityTitle();
                    msg.description = editTddActivity.getActivityDescription();
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
                    webpage.webpageUrl = editTddActivity.getShareUrl();
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = editTddActivity.getActivityTitle();
                    msg.description = editTddActivity.getActivityDescription();
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
            UiHelper.ShowOneToast(OriginateChairActivity.this, "第三方分享的内容不能为空!");
            finish();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onReq(BaseReq arg0) {

    }

    @Override
    public void onResp(BaseResp arg0) {
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
        bundle.putString("title", editTddActivity.getActivityTitle());
        bundle.putString("imageUrl", editTddActivity.getShareImg());
        bundle.putString("targetUrl", editTddActivity.getShareUrl());
        bundle.putString("summary", editTddActivity.getActivityDescription());
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
        mTencent.shareToQQ(OriginateChairActivity.this, params, new BaseUiListener() {
            protected void doComplete(JSONObject values) {
                showResult("shareToQQ:", "分享成功");
            }

            @Override
            public void onError(UiError e) {
                showResult("shareToQQ:", "分享失败未安装登陆第三方");
            }

            @Override
            public void onCancel() {
                showResult("shareToQQ", "分享取消");
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
                UiHelper.ShowOneToast(OriginateChairActivity.this, msg);
//				popupWindow.dismiss();
//				finish();
            }
        });
    }

    /*
     * 上传图片
     */
    public void upload() {

        // 构造方法1、2的参数
        filePathList = new ArrayList<String>();
        // 就只是将本地存在的传上传
        for (int i = 0; i < arrayList.size(); i++) {
            if (!"".equals(arrayList.get(i).getImageId())) {
                File file = new File(arrayList.get(i).getImagePath());
                if (file.exists()) {
                    filePathList.add(arrayList.get(i).getImagePath());// 将本地存在的抽出来
                    // 这部分不用上传的
                } else {
                    file.delete();
                }
            }
        }

        post = new HttpMultipartPostOriginate(OriginateChairActivity.this, filePathList);
        post.execute();
    }

    // 根据Uri获取到图片的路径
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    // 上传头像完之后的操作
    @SuppressWarnings("unchecked")
    public void handleHeadImg(String imgMess) {
        super.refreshDialog();
        progressDialog.setCancelable(false);
        if (imgMess != null) {
            WBResponse response = MessageUtil.JsonStrToWBResponse(imgMess, HeadImgRes.class);
            if (response != null) {
                String msgString = response.getMsg();//upload success! //表示上传
                imgUrl = ((HeadImgRes) response.getRespBody()).getFileUrls();
                if (arrayList.size() > 0 && imgUrl != null) {
                    List<PhotoUpImageItem> tempImageItems = new ArrayList<PhotoUpImageItem>();
                    // 首先先抽搐本地存在的
                    if (arrayList.size() < 7) {
                        for (int i = 0; i < arrayList.size() - 1; i++) {
                            File file = new File(arrayList.get(i).getImagePath());
                            if (file.exists()) {
                                tempImageItems.add(arrayList.get(i));

                            } else {
                                file.delete();

                            }

                        }
                        for (int i = 0; i < arrayList.size() - 1; i++) {
                            File file = new File(arrayList.get(i).getImagePath());

                            if (file.exists()) {
                                arrayList.remove(arrayList.get(i));
                                i -= 1;
                            } else {
                                file.delete();
                            }
                        }

                        arrayList.remove(arrayList.size() - 1);
                        for (int i = 0; i < tempImageItems.size(); i++) {
                            File file = new File(tempImageItems.get(i).getImagePath());
                            if (file.exists()) {
                                tempImageItems.get(i).setImagePath(imgUrl.get(i));
                                arrayList.add(tempImageItems.get(i));
                            } else {
                                file.delete();
                            }

                        }

                        tempImageItems.clear();
                    } else {

                        for (int i = 0; i < arrayList.size(); i++) {
                            File file = new File(arrayList.get(i).getImagePath());
                            if (file.exists()) {
                                tempImageItems.add(arrayList.get(i));

                            } else {
                                file.delete();

                            }

                        }
                        for (int i = 0; i < arrayList.size(); i++) {
                            File file = new File(arrayList.get(i).getImagePath());

                            if (file.exists()) {
                                arrayList.remove(arrayList.get(i));
                                i -= 1;
                            } else {
                                file.delete();
                            }
                        }
                        for (int i = 0; i < tempImageItems.size(); i++) {
                            File file = new File(tempImageItems.get(i).getImagePath());
                            if (file.exists()) {
                                tempImageItems.get(i).setImagePath(imgUrl.get(i));
                                arrayList.add(tempImageItems.get(i));
                            } else {
                                file.delete();
                            }

                        }

                        tempImageItems.clear();
                    }

                }
            }
            refresh();// 刷新
        } else {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Intent intent0 = new Intent();//切记
            // 这里的Action参数与IntentFilter添加的Action要一样才可以
            // intent0.setAction("RECEIVE_TDDACTIVITY");
            // Bundle bundle=new Bundle();//tddactivity
            // bundle.putSerializable("tddactivity",editTddActivity);
            // intent0.putExtras(bundle);
            // sendBroadcast(intent0);//发送广播了
            if (StringUtil.isNotEmpty(mOriginate_originateactivity_title_edt.getText().toString()) || StringUtil.isNotEmpty(mOriginate_originateactivity_starttime_tv.getText().toString())
                    || StringUtil.isNotEmpty(mOriginate_originateactivity_address_edt.getText().toString()) || StringUtil.isNotEmpty(mOriginate_originateactivity_endtime_tv.getText().toString())
                    || StringUtil.isNotEmpty(mOriginate_originateactivity_describe_edt.getText().toString())) {
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("确定退出吗？").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                dialog.setCanceledOnTouchOutside(false);
            } else {
                finish();
            }
            return true;
        }
        return false;
    }

    /**
     * 根据字符串进行的判断
     * @param endTimeString
     * @param startTimeString
     */
    private boolean judgeStringOk(String startTimeString, String endTimeString) {
        //“讲座类型”
        if (activityType == ActivityType.typeChair) {
            if (StringUtil.isEmpty(mOriginate_originateactivity_title_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, StringUtil.intType2Str(activityType) + "主题不能为空");
                return false;
            } else if (StringUtil.isEmpty(startTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开讲时间不能为空");
                return false;
            } else if (StringUtil.isEmpty(mOriginate_originateactivity_address_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开讲地点不能为空");
                return false;
            } else if (StringUtil.isEmpty(endTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能为空");
                return false;
            } else if (DateUtil.judgeTimeLarge(startTimeString, endTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能迟于开讲时间");
                return false;
            } else if (!DateUtil.judgeTimeLarge(endTimeString, DateUtil.getDateNowString2())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能早于当前时间");
                return false;
            } else if (!DateUtil.judgeTimeLarge(startTimeString, DateUtil.getDateNowString2())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开讲时间不能早于当前时间");
                return false;
            } else {
                return true;
            }
        }
        //“聚会类型”
        else if (activityType == ActivityType.typeParty) {
            if (StringUtil.isEmpty(mOriginate_originateactivity_title_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, StringUtil.intType2Str(activityType) + "主题不能为空");
                return false;
            } else if (StringUtil.isEmpty(startTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开始时间不能为空");
                return false;
            } else if (StringUtil.isEmpty(mOriginate_originateactivity_address_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "聚会地点不能为空");
                return false;
            } else if (StringUtil.isEmpty(endTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能为空");
                return false;
            } else if (!DateUtil.judgeTimeLarge(startTimeString, endTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能早于开始时间");
                return false;
            } else if (!DateUtil.judgeTimeLarge(endTimeString, DateUtil.getDateNowString2())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能早于当前时间");
                return false;
            } else if (!DateUtil.judgeTimeLarge(startTimeString, DateUtil.getDateNowString2())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开始时间不能早于当前时间");
                return false;
            } else {
                return true;
            }
        }
        //“旅游类型”
        else if (activityType == ActivityType.typeTravel) {
            if (StringUtil.isEmpty(mOriginate_originateactivity_title_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, StringUtil.intType2Str(activityType) + "主题不能为空");
                return false;
            } else if (StringUtil.isEmpty(startTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开始时间不能为空");
                return false;
            } else if (StringUtil.isEmpty(endTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能为空");
                return false;
            } else if (!DateUtil.judgeTimeLarge(startTimeString, endTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能早于开始时间");
                return false;
            } else if (!DateUtil.judgeTimeLarge(endTimeString, DateUtil.getDateNowString2())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能早于当前时间");
                return false;
            } else if (!DateUtil.judgeTimeLarge(startTimeString, DateUtil.getDateNowString2())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开始时间不能早于当前时间");
                return false;
            } else {
                return true;
            }
        }
        //“通知类型”
        else if (activityType == ActivityType.typeNotification) {
            if (StringUtil.isEmpty(mOriginate_originateactivity_title_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, StringUtil.intType2Str(activityType) + "主题不能为空");
                return false;
            } else if (StringUtil.isEmpty(endTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "通知有效期不能为空");
                return false;
            } else if (!DateUtil.judgeTimeLarge(endTimeString, DateUtil.getDateNowString2())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "通知有效期不能早于当前时间");
                return false;
            } else {
                return true;
            }
        }
        //“运动类型”
        else if (activityType == ActivityType.typeSporting) {
            if (StringUtil.isEmpty(mOriginate_originateactivity_title_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, StringUtil.intType2Str(activityType) + "主题不能为空");
                return false;
            } else if (StringUtil.isEmpty(startTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开始时间不能为空");
                return false;
            } else if (StringUtil.isEmpty(mOriginate_originateactivity_address_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "运动地点不能为空");
                return false;
            } else if (!DateUtil.judgeTimeLarge(startTimeString, DateUtil.getDateNowString2())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开始时间不能早于当前时间");
                return false;
            } else {
                return true;
            }
        }
        //“会议类型”
        else if (activityType == ActivityType.typeMeeting) {
            if (StringUtil.isEmpty(mOriginate_originateactivity_title_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, StringUtil.intType2Str(activityType) + "主题不能为空");
                return false;
            } else if (StringUtil.isEmpty(startTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开始时间不能为空");
                return false;
            } else if (StringUtil.isEmpty(mOriginate_originateactivity_address_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "会议地点不能为空");
                return false;
            } else if (!DateUtil.judgeTimeLarge(startTimeString, DateUtil.getDateNowString2())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开始时间不能早于当前时间");
                return false;
            } else {
                return true;
            }
        }
        //“活动类型”
        else if (activityType == ActivityType.typeActivity) {
            if (StringUtil.isEmpty(mOriginate_originateactivity_title_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, StringUtil.intType2Str(activityType) + "主题不能为空");
                return false;
            } else if (StringUtil.isEmpty(startTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开始时间不能为空");
                return false;
            } else if (StringUtil.isEmpty(mOriginate_originateactivity_address_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "活动地点不能为空");
                return false;
            } else if (StringUtil.isEmpty(endTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能为空");
                return false;
            } else if (!DateUtil.judgeTimeLarge(startTimeString, endTimeString)) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能早于开始时间");
                return false;
            } else if (!DateUtil.judgeTimeLarge(endTimeString, DateUtil.getDateNowString2())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "报名截止不能早于当前时间");
                return false;
            } else if (!DateUtil.judgeTimeLarge(startTimeString, DateUtil.getDateNowString2())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, "开始时间不能早于当前时间");
                return false;
            } else {
                return true;
            }
        }
        //“一句话类型”快捷发布
        else if (activityType == ActivityType.typeShortCut) {

            if (StringUtil.isEmpty(mOriginate_originateactivity_describe_edt.getText().toString())) {
                UiHelper.ShowOneToast(OriginateChairActivity.this, StringUtil.intType2Str(activityType) + "内容不能为空");
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
