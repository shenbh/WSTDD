package com.newland.wstdd.common.filedownload;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class FileListActivity extends FileBaseActivity {
	private static final String TAG = "FileListActivity";//收集异常日志tag
	private ListView mListImageLv;
	private DisplayImageOptions options; // 设置图片显示相关参数
	private String[] imageUrls; // 图片路径

	@Override
	public int initResource() {
		return R.layout.activity_file_list;
	}

	@Override
	public void initComponent() {
		mListImageLv = (ListView) findViewById(R.id.lv_image);
	}

	@Override
	public void initData() {
		 /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "initData",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		Bundle bundle = getIntent().getExtras();
		imageUrls = bundle.getStringArray(Constants.IMAGES);

		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				
				.build(); // 构建完成

		mListImageLv.setAdapter(new ItemListAdapter());
	}

	@Override
	protected void onDestroy() {
		/** 收集异常日志 */
		LogManager.getManager(getApplicationContext()).unregisterActivity(this);
		/** 收集异常日志 */
		super.onDestroy();
	}
	@Override
	public void addListener() {

	}

	class ItemListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return imageUrls[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_list,
						null);
				viewHolder = new ViewHolder();
				viewHolder.image = (ImageView) convertView
						.findViewById(R.id.iv_image);
				viewHolder.text = (TextView) convertView
						.findViewById(R.id.tv_introduce);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			/**
			 * imageUrl 图片的Url地址 imageView 承载图片的ImageView控件 options
			 * DisplayImageOptions配置文件
			 */
			imageLoader.displayImage(imageUrls[position],
					viewHolder.image, options);
			viewHolder.text.setText("Item " + (position + 1)); // TextView设置文本

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
			public TextView text;
		}

	}

}
