package com.newland.wstdd.mine.minesetting.feedbackandhelp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.EditTextUtil;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.widget.PengTextView;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.login.beanresponse.HeadImgRes;
import com.newland.wstdd.mine.minesetting.feedbackandhelp.bean.FeedBackReq;
import com.newland.wstdd.mine.minesetting.feedbackandhelp.bean.FeedBackRes;
import com.newland.wstdd.mine.minesetting.feedbackandhelp.heandle.FeedBackHandle;
import com.newland.wstdd.mine.minesetting.feedbackandhelp.selectphoto.AlbumsActivity;
import com.newland.wstdd.mine.minesetting.feedbackandhelp.selectphoto.PhotoUpImageItem;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.netutils.MessageUtil;
import com.newland.wstdd.netutils.WBResponse;

/**
 * 反馈界面
 *
 * @author Administrator 2015-12-7
 */
public class FeedBackActivity extends BaseFragmentActivity implements OnClickListener, OnPostListenerInterface {
    private static final String TAG = "FeedBackActivity";//收集异常日志tag
    private EditText activity_mine_setting_feedback_content_edt;// 请输入您的宝贵意见与建议...
    private PengTextView activity_mine_setting_feedback_remainwords_tv;// 剩下多少字
    // private ImageView activity_mine_setting_feedback_addimg_iv;// 添加照片
    private EditText activity_mine_setting_feedback_phone_edt;// 请输入有效手机号以便给您反馈
    private int inputNum = 150;// 限制输入的字数

    /**
     * 动态添加图片
     */
    private GridView selected_images_gridv;// 动态添加图片
    private ArrayList<PhotoUpImageItem> arrayList = new ArrayList<PhotoUpImageItem>();// 活动图片的选择
    private FeedBackSelectedImagesAdapter adapter;// 活动图片的适配器
    private IntentFilter filter;// 定一个广播接收过滤器
    /** 动态添加图片 */

    /**
     * 图片上传的相关信息
     */
    private FeedBackHttpMultipartPostOriginate post;
    private List<String> filePathList;// 本地图片的列表
    private List<String> imgUrl = new ArrayList<String>();// 服务器返回的图片列表
    /**
     * 图片上传的相关信息
     */

    FeedBackRes feedBackRes;
    FeedBackHandle handle = new FeedBackHandle(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        setContentView(R.layout.activity_mine_setting_feedback);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        // 注册广播
        filter = new IntentFilter("ORIGUNATE_CHAIR_PHOTO");
        registerReceiver(broadcastReceiver, filter);// 在对象broadcastReceiver中进行接收的相应处理

        setTitle();
        initView();
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
            center_tv.setText("意见反馈");
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
            right_tv.setOnClickListener(this);
            right_tv.setVisibility(View.VISIBLE);
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
                    FeedBackReq feedBackReq = new FeedBackReq();
                    feedBackReq.setAdviceContent(activity_mine_setting_feedback_content_edt.getText().toString());
                    feedBackReq.setMobliePhone(activity_mine_setting_feedback_phone_edt.getText().toString());
                    BaseMessageMgr mgr = new HandleNetMessageMgr();
                    RetMsg<FeedBackRes> retMsg = mgr.getFeedBackInfo(feedBackReq);
                    Message message = new Message();
                    message.what = FeedBackHandle.FEED_BACK;// 设置死
                    // 访问服务器成功1 否则访问服务器失败
                    if (retMsg.getCode() == 1) {
                        feedBackRes = (FeedBackRes) retMsg.getObj();
                        message.obj = feedBackRes;
                    } else {
                        message.obj = retMsg.getMsg();
                    }
                    handle.sendMessage(message);
                }

                ;
            }.start();
        } catch (Exception e) {
            // TODO: handle exception
        }
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

        post = new FeedBackHttpMultipartPostOriginate(FeedBackActivity.this, filePathList);
        post.execute();
    }

    // 上传头像完之后的操作
    @SuppressWarnings("unchecked")
    public void handleHeadImg(String imgMess) {
        if (imgMess != null) {
            WBResponse response = MessageUtil.JsonStrToWBResponse(imgMess, HeadImgRes.class);
            String msgString = response.getMsg();
            imgUrl = ((HeadImgRes) response.getRespBody()).getFileUrls();
            if (arrayList.size() > 0 && imgUrl != null) {
                List<PhotoUpImageItem> tempImageItems = new ArrayList<PhotoUpImageItem>();
                // 首先先抽出本地存在的
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

            }
        }
        refresh();// 刷新

    }

    @Override
    public void initView() {
        activity_mine_setting_feedback_content_edt = (EditText) findViewById(R.id.activity_mine_setting_feedback_content_edt);
        activity_mine_setting_feedback_remainwords_tv = (PengTextView) findViewById(R.id.activity_mine_setting_feedback_remainwords_tv);
        // activity_mine_setting_feedback_addimg_iv = (ImageView)
        // findViewById(R.id.activity_mine_setting_feedback_addimg_iv);
        selected_images_gridv = (GridView) findViewById(R.id.selected_images_gridv);
        activity_mine_setting_feedback_phone_edt = (EditText) findViewById(R.id.activity_mine_setting_feedback_phone_edt);

        // 解决滑动条冲突问题
        activity_mine_setting_feedback_content_edt.setOnTouchListener(new OnTouchListener() {

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
        // 限制输入的个数
        activity_mine_setting_feedback_content_edt.addTextChangedListener(new TextWatcher() {
            private boolean isOutOfBounds = false;
            int end;

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > inputNum) {
                    isOutOfBounds = true;
                } else {
                    activity_mine_setting_feedback_remainwords_tv.setText(s.length() + "/150");
                    isOutOfBounds = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (isOutOfBounds) {
                    UiHelper.ShowOneToast(FeedBackActivity.this, "字符超过了");
                    if (s.length() > inputNum) {
                        s.delete(inputNum, s.length());
                        end = inputNum;
                    } else if (s.length() > 20 && s.length() <= inputNum) {
                        s.delete(20, s.length());
                        end = 20;
                    }
                    end = s.length();
                    activity_mine_setting_feedback_content_edt.setSelection(end);// 设置光标在最后
                    activity_mine_setting_feedback_remainwords_tv.setText(s.length() + "/150");
                }
            }
        });

        init();
    }

    // 选择图片的相关操作
    private void init() {
        // arrayList = (ArrayList<PhotoUpImageItem>)
        // getIntent().getSerializableExtra("selectIma");
        if (arrayList != null) {
            if (arrayList.size() < 3) {// 如果
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

        adapter = new FeedBackSelectedImagesAdapter(FeedBackActivity.this, arrayList);
        selected_images_gridv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        setOnItemClick();
    }

    private void setOnItemClick() {
        // 相册选项
        selected_images_gridv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 这里的arraylist是包含空图片的
                if (position == arrayList.size() - 1 && "".equals(arrayList.get(position).getImageId())) {// 点击的是最后一个且为空
                    // 判断是否为添加标志
                    Intent intent = new Intent(FeedBackActivity.this, AlbumsActivity.class);
                    arrayList.remove(arrayList.size() - 1);
                    intent.putExtra("selectImageList", arrayList);
                    intent.putExtra("activityType", "FeedBackActivity");
                    startActivity(intent);

                } else {
                    // Intent intent = new Intent(FeedBackActivity.this,
                    // SelectedCoverActivity.class);
                    // if ("".equals(arrayList.get(arrayList.size() -
                    // 1).getImageId())) {
                    // arrayList.remove(arrayList.size() - 1);
                    // }
                    //
                    // intent.putExtra("selectImageList", arrayList);
                    // intent.putExtra("position", position);//
                    // 点击哪一个就跳转到哪一个，然后在设置封面界面上显示出当前的界面
                    // startActivity(intent);

                    // 删除
                    PhotoUpImageItem photoUpImageItem = (PhotoUpImageItem) parent.getItemAtPosition(position);
                    showIsDeleteDialog(photoUpImageItem, position);
                }
            }

        });
    }

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
                if (arrayList.size() == 2 && !"".equals(arrayList.get(arrayList.size() - 1).getImageId())) {// 删除最后一张
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
            if ("cover".equals(intent.getStringExtra("type"))) {
                arrayList = (ArrayList<PhotoUpImageItem>) intent.getSerializableExtra("selectIma");
            } else {
                // if (editTddActivity != null) {
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

                // } else {
                // arrayList = (ArrayList<PhotoUpImageItem>)
                // intent.getSerializableExtra("selectIma");
                // }
            }
            // arrayList=(ArrayList<PhotoUpImageItem>)
            // intent.getSerializableExtra("selectIma");
            init();

        }
    };

    @Override
    public void onDestroy() {
        /** 收集异常日志 */
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /** 收集异常日志 */
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left_iv:
                finish();
                break;
            case R.id.head_right_tv:// 提交
                if (StringUtil.isEmpty(activity_mine_setting_feedback_content_edt.getText().toString())) {
                    UiHelper.ShowOneToast(this, "请输入您的宝贵意见与建议...");
                } else if (StringUtil.isEmpty(activity_mine_setting_feedback_phone_edt.getText().toString())) {
                    UiHelper.ShowOneToast(this, "请输入有效手机号以便给您反馈");
                } else if (!EditTextUtil.checkMobileNumber(activity_mine_setting_feedback_phone_edt.getText().toString())) {
                    UiHelper.ShowOneToast(this, "输入手机号格式有误，请确认！");
                } else {
                    upload();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void OnHandleResultListener(Object obj, int responseId) {
        try {
            switch (responseId) {
                case FeedBackHandle.FEED_BACK:
                    if (progressDialog != null) {
                        progressDialog.setContinueDialog(false);
                    }
                    finish();
                    feedBackRes = (FeedBackRes) obj;
                    if (feedBackRes != null) {
                        UiHelper.ShowOneToast(this, "提交成功");
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (StringUtil.isNotEmpty(activity_mine_setting_feedback_content_edt.getText().toString())
                    || StringUtil.isNotEmpty(activity_mine_setting_feedback_phone_edt.getText().toString())) {
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("确定退出吗？？").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

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
}
