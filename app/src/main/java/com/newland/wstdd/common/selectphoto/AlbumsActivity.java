package com.newland.wstdd.common.selectphoto;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.selectphoto.PhotoUpAlbumHelper.GetAlbumList;

/**
 * 这个是相册显示图片的界面
 * 
 * @author Administrator
 * 
 */
public class AlbumsActivity extends Activity {
	private static final String TAG = "AlbumsActivity";//收集异常日志tag
	public static int selectNum = 0;// 还可以选择几张图片
	private GridView gridView;// 用来显示相册类型图片文件--------是一个列表文件
	private AlbumsAdapter adapter;// 列表的适配器------用来存放内容的
	private PhotoUpAlbumHelper photoUpAlbumHelper;// 用户获取相册图片的路径、缩略图等的集成工具类，可以直接调用
	private List<PhotoUpImageBucket> list;// 列表中的item
	private ArrayList<PhotoUpImageItem> arrayList = new ArrayList<PhotoUpImageItem>();// 用来接收第一个界面传过来的已选的图片
	private String activityType;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selectNum = 0;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		setContentView(R.layout.albums_gridview);
		
		 /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		arrayList = (ArrayList<PhotoUpImageItem>) getIntent().getSerializableExtra("selectImageList");
		activityType = getIntent().getStringExtra("activityType");
		init();// 初始化控件跟适配器
		loadData();// 通过图片集成工具获取相册的数据
		onItemClick();
	}

	private void init() {
		gridView = (GridView) findViewById(R.id.album_gridv);
		adapter = new AlbumsAdapter(AlbumsActivity.this);// 初始化适配器得到一个list那就是
															// List<PhotoUpImageBucket>
		gridView.setAdapter(adapter);// 设置适配器
	}

	private void loadData() {
		photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();// 获取相册集成对象
		photoUpAlbumHelper.init(AlbumsActivity.this);// 初始化相册集成工具
		// 接口中的方法是异步任务执行完之后才会去执行的
		photoUpAlbumHelper.setGetAlbumList(new GetAlbumList() {
			@Override
			public void getAlbumList(List<PhotoUpImageBucket> list) {
				adapter.setArrayList(list);// 将list数据传入适配器
				adapter.notifyDataSetChanged();// 刷新适配器
				AlbumsActivity.this.list = list;// 图片集列表
				for (int i = 0; i < arrayList.size(); i++) {
					File file = new File(arrayList.get(i).getImagePath());
					if (!file.exists()) {
						selectNum += 1;
						file.delete();
					}
				}
				if (arrayList != null && !arrayList.isEmpty()) {
					for (int i = 0; i < arrayList.size(); i++) {
						String imgId = arrayList.get(i).getImageId();

						for (int j = 0; j < AlbumsActivity.this.list.size(); j++) {
							for (int j2 = 0; j2 < list.get(j).getImageList().size(); j2++) {
								if (imgId.equals(list.get(j).getImageList().get(j2).getImageId())) {
									list.get(j).getImageList().get(j2).setSelected(true);
								}
							}
						}
					}
				}
			}
		});
		photoUpAlbumHelper.execute(false);// 异步任务去执行，false的作用？刷新

	}

	// 为每个图片集设置监听事件，点击之后就会进入到相应的图片集中的列表中去
	private void onItemClick() {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(AlbumsActivity.this, AlbumItemActivity.class);
				intent.putExtra("imagelist", list.get(position));// 点击的是哪一个图片集就进入到哪一个列表中去
				intent.putExtra("imagelists", (Serializable) list);// 相册目录
				intent.putExtra("activityType", activityType);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {
		/** 收集异常日志 */
		LogManager.getManager(getApplicationContext()).unregisterActivity(this);
		/** 收集异常日志 */
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent0 = new Intent();// 切记
											// 这里的Action参数与IntentFilter添加的Action要一样才可以
			intent0.setAction("ORIGUNATE_CHAIR_PHOTO");
			intent0.putExtra("selectIma", (Serializable) arrayList);
			sendBroadcast(intent0);// 发送广播了
			finish();
			return true;
		}
		return false;
	}
}
