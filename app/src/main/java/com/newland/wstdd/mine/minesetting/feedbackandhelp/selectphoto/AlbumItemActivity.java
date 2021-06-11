package com.newland.wstdd.mine.minesetting.feedbackandhelp.selectphoto;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.mine.minesetting.feedbackandhelp.FeedBackActivity;
import com.newland.wstdd.originate.origateactivity.OriginateChairActivity;


/**
 * 具体某一个相册集里面的界面
 *
 * @author Administrator
 */
public class AlbumItemActivity extends Activity implements OnClickListener {
    private static final String TAG = "AlbumItemActivity";//收集异常日志tag
    private GridView gridView;// 相册列表
    private TextView back, ok;// 控件取消 确定
    private PhotoUpImageBucket photoUpImageBucket;// 相册集对象 用来接收的
    private List<PhotoUpImageBucket> list;// 列表中的item 用来接收的
    private ArrayList<PhotoUpImageItem> selectImages;// 选中相片时候保存的集合列表
    private AlbumItemAdapter adapter;// 相册适配器
    private String activityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.album_item_images);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        activityType = getIntent().getStringExtra("activityType");
        init();// 同样进行初始化控件
        setListener();// 设置每一张图片的监听事件
    }

    @Override
    protected void onDestroy() {
        /** 收集异常日志 */
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /** 收集异常日志 */
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        gridView = (GridView) findViewById(R.id.album_item_gridv);
        back = (TextView) findViewById(R.id.back);
        ok = (TextView) findViewById(R.id.sure);
        selectImages = new ArrayList<PhotoUpImageItem>();
        Intent intent = getIntent();
        photoUpImageBucket = (PhotoUpImageBucket) intent.getSerializableExtra("imagelist");// 通过意图的方式获取到
        list = (List<PhotoUpImageBucket>) intent.getSerializableExtra("imagelists");
        adapter = new AlbumItemAdapter(photoUpImageBucket.getImageList(), AlbumItemActivity.this);// 从相册集里面得到相册集的图片列表，放到适配器中

        adapter.notifyDataSetChanged();
        // 主要作用是 存放已经有的相册中的图片
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getImageList().size(); j++) {
                if (list.get(i).getImageList().get(j).isSelected()) {
                    if (adapter.getSelectImages().contains(list.get(i).getImageList().get(j))) {

                    } else {
                        adapter.getSelectImages().add(list.get(i).getImageList().get(j));
                    }
                } else {
                    if (adapter.getSelectImages().contains(list.get(i).getImageList().get(j))) {
                        adapter.getSelectImages().remove(list.get(i).getImageList().get(j));
                    } else {

                    }
                }
            }
        }
        gridView.setAdapter(adapter);
    }

    private void setListener() {
        back.setOnClickListener(this);
        ok.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure:

                // 利用广播的方式进行更新界面 试一试

                Intent intent0 = new Intent();// 切记
                // 这里的Action参数与IntentFilter添加的Action要一样才可以
                intent0.setAction("ORIGUNATE_CHAIR_PHOTO");
                intent0.putExtra("selectIma", adapter.getSelectImages());
                sendBroadcast(intent0);// 发送广播了
                // adapter.notifyDataSetChanged();
                if (activityType.equals("OriginateChairActivity")) {
                    Intent intent = new Intent(AlbumItemActivity.this, OriginateChairActivity.class);
                    intent.putExtra("selectIma", adapter.getSelectImages());
                    startActivity(intent);
                } else if (activityType.equals("FeedBackActivity")) {
                    Intent intent2 = new Intent(AlbumItemActivity.this, FeedBackActivity.class);
                    intent2.putExtra("selectIma", adapter.getSelectImages());
                    startActivity(intent2);
                }

                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent0 = new Intent();// 切记
            // 这里的Action参数与IntentFilter添加的Action要一样才可以
            intent0.setAction("ORIGUNATE_CHAIR_PHOTO");
            intent0.putExtra("selectIma", adapter.getSelectImages());
            sendBroadcast(intent0);// 发送广播了

            adapter.notifyDataSetChanged();
            Intent intent = new Intent(AlbumItemActivity.this, FeedBackActivity.class);
            intent.putExtra("selectIma", adapter.getSelectImages());
            startActivity(intent);

            finish();
            return true;
        }
        return false;
    }
}
