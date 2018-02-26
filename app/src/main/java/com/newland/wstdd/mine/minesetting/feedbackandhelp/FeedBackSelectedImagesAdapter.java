package com.newland.wstdd.mine.minesetting.feedbackandhelp;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.common.UrlManager;
import com.newland.wstdd.mine.minesetting.feedbackandhelp.selectphoto.PhotoUpImageItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class FeedBackSelectedImagesAdapter extends BaseAdapter {

	private ArrayList<PhotoUpImageItem> arrayList;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context context;

	public FeedBackSelectedImagesAdapter(Context context, ArrayList<PhotoUpImageItem> arrayList) {
		this.arrayList = arrayList;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		// 使用DisplayImageOption.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.album_default_loading_pic) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.album_default_loading_pic) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.album_default_loading_pic) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.bitmapConfig(Config.ARGB_8888).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build(); // 创建配置过的DisplayImageOption对象
	}

	@Override
	public int getCount() {
		return arrayList == null ? 0 : arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		if (arrayList != null && arrayList.size() != 0) {
			return arrayList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.activity_mine_setting_feedback_addimg_adapter_item, parent, false);
			holder = new Holder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.selected_image_item);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (arrayList.get(position).getImagePath() == null || "".equals(arrayList.get(position).getImagePath())) {
			holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.feedback_addimg));
		} else {
			File file = new File(arrayList.get(position).getImagePath());
			if (file.exists()) {
				imageLoader.displayImage("file://" + arrayList.get(position).getImagePath(), holder.imageView, options);
			} else {
				imageLoader.displayImage(UrlManager.uploadToUrlServer + arrayList.get(position).getImagePath(), holder.imageView, options);
			}
		}
		return convertView;
	}

	class Holder {
		ImageView imageView;
	}

	public ArrayList<PhotoUpImageItem> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<PhotoUpImageItem> arrayList) {
		this.arrayList = arrayList;
	}

}
