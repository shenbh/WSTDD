package com.newland.wstdd.find.find;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.updownloadimg.ImageDownLoad;
import com.newland.wstdd.find.hotlist.bean.HotListInfo;

/**发现-listview适配器
 * @author H81 2015-11-6
 *
 */
public class FindListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context context;
//	private List<FindListViewData> findListViewDatas;// 数据
	private List<HotListInfo> findListViewDatas;// 数据

//	public FindListAdapter(Context context, List<FindListViewData> findListViewDatas) {
//		this.context = context;
//		this.findListViewDatas = findListViewDatas;
//		mInflater = LayoutInflater.from(context);
//	}
	public FindListAdapter(Context context, List<HotListInfo> findListViewDatas) {
		this.context = context;
		this.findListViewDatas = findListViewDatas;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return findListViewDatas == null ? 0 : findListViewDatas.size();
	}

	@Override
	public HotListInfo getItem(int position) {
		if (findListViewDatas != null && findListViewDatas.size() != 0) {
			return findListViewDatas.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			// 根据自定义的Item布局加载布局
			convertView = mInflater.inflate(R.layout.find_listview_item, null);
			holder.find_hot_activitytype=(TextView) convertView.findViewById(R.id.find_hot_logoname_tv);
			
			holder.logoImageView = (ImageView) convertView.findViewById(R.id.find_hot_logo);
			holder.hotTimeTextView = (TextView) convertView.findViewById(R.id.find_hot_time);
			holder.hotCityTextView = (TextView) convertView.findViewById(R.id.find_hot_city);
			holder.titleTextView = (TextView) convertView.findViewById(R.id.find_hot_title);
			holder.peopleTextView = (TextView) convertView.findViewById(R.id.find_hot_people);
			holder.isChargeImageView = (ImageView) convertView.findViewById(R.id.find_hot_ischarge);
			holder.find_hot_signstate = (TextView) convertView.findViewById(R.id.find_hot_signstate);
			// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 先设置死的 后面根据屏幕大小进行设置
		// convertView.setLayoutParams(new GridView.LayoutParams(
		// /180, 100));

		convertView.setLayoutParams(new ListView.LayoutParams((int) (ListView.LayoutParams.FILL_PARENT), (int) (AppContext.getAppContext().getScreenHeight() / 6)));// 动态设置item的高度

		HotListInfo findFragmentListViewData = getItem(position);

//		holder.logoImageView.setImageDrawable(findFragmentListViewData.getLogo().getDrawable());// 利用这样的方法进行图片的参数传递
//		holder.hotTimeTextView.setText(findFragmentListViewData.getHotTime());
//		holder.hotCityTextView.setText(findFragmentListViewData.getHotCity());
//		holder.peopleTextView.setText(findFragmentListViewData.getPeople());
//		holder.titleTextView.setText(findFragmentListViewData.getHottitle());
		
		
		String activitytype=StringUtil.intType2Str(findFragmentListViewData.getActivityType());
		ImageDownLoad.getDownLoadSmallImg(findFragmentListViewData.getImgUrl(), holder.logoImageView);
		holder.find_hot_activitytype.setText(activitytype);
		holder.hotTimeTextView.setText(findFragmentListViewData.getFriendActivityTime());
		holder.hotCityTextView.setText(findFragmentListViewData.getActivityAddress());
		if (findFragmentListViewData.getSignCount() == 0) {
			holder.peopleTextView.setText("0");
		}else {
			holder.peopleTextView.setText(""+findFragmentListViewData.getSignCount());
		}
		holder.titleTextView.setText(findFragmentListViewData.getActivityTitle());
		if ("0".equals(findFragmentListViewData.getNeedPay())) {
			holder.isChargeImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.test11));
		} else {
			holder.isChargeImageView.setVisibility(View.GONE);
		}
		if (0==findFragmentListViewData.getStatus()) {//状态  0.未发布 1.报名中 2.关闭报名 4.已撤销
			holder.find_hot_signstate.setText("未发布 ");
		}else if (1==findFragmentListViewData.getStatus()) {//状态  0.未发布 1.报名中 2.关闭报名 4.已撤销
			holder.find_hot_signstate.setText("报名中");
		}else if (2==findFragmentListViewData.getStatus()) {//状态  0.未发布 1.报名中 2.关闭报名 4.已撤销
			holder.find_hot_signstate.setText("关闭报名");
		}else if (4==findFragmentListViewData.getStatus()) {//状态  0.未发布 1.报名中 2.关闭报名 4.已撤销
			holder.find_hot_signstate.setText("已撤销");
		}
		return convertView;
	}

	// ViewHolder静态类
	static class ViewHolder {
		public TextView find_hot_activitytype;//活动分类
		public ImageView logoImageView;// 内容图片
		public TextView hotTimeTextView;// 热门标题
		public TextView hotCityTextView;// 热门城市
		public TextView peopleTextView;// 报名人数
		public TextView titleTextView;// 热门标题
		public ImageView isChargeImageView;// 是否收费
		public TextView find_hot_signstate;//报名状态
	}

	public List<HotListInfo> getHotFragmentListInfo() {
		return findListViewDatas;
	}

//	public void setHotFragmentListInfo(List<FindListViewData> findListViewDatas) {
//		this.findListViewDatas = findListViewDatas;
//	}
	public void setHotFragmentListInfo(List<HotListInfo> findListViewDatas) {
		this.findListViewDatas = findListViewDatas;
	}
}
