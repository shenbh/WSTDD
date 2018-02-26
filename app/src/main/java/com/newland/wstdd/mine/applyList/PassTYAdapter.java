/**
 * @fields PassTYAdapter.java
 */
package com.newland.wstdd.mine.applyList;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.updownloadimg.ImageDownLoad;
import com.newland.wstdd.common.widget.RoundImageView;
import com.newland.wstdd.mine.applyList.bean.TddActivitySignVoInfo;

/**
 * 通过名单 团员listView的 Adapter
 * 
 * @author H81 2015-11-29
 * 
 */
public class PassTYAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater layoutInflater;
	private List<TddActivitySignVoInfo> passTYList;
	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> isSelected;

	public PassTYAdapter(FragmentActivity fragmentActivity, List<TddActivitySignVoInfo> passTYList) {
		this.passTYList = passTYList;
		this.context = fragmentActivity;
		layoutInflater = LayoutInflater.from(fragmentActivity);
		isSelected = new HashMap<Integer, Boolean>();
		// 初始化数据
		initDate();
	}

	// 初始化isSelected的数据
	private void initDate() {
		for (int i = 0; i < passTYList.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	@Override
	public int getCount() {
		return passTYList == null ? 0 : passTYList.size();
	}

	@Override
	public Object getItem(int position) {
		if (passTYList != null && passTYList.size() > 0) {
			return passTYList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		PassTYAdapter.isSelected = isSelected;
	}
	
	private onItemClickListener iListener = null;//点选事件
	

	public void setOnItemClickListener (onItemClickListener listener) {
		iListener = listener;
	}
	
	public interface onItemClickListener {
		void onItemClick(View v, int position);
	}

	int positions;
	 ViewHolder viewHolder;
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		this.positions=position;
		if (convertView == null ) {
			convertView = layoutInflater.inflate(R.layout.activity_passty_item,null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (PassListFragment .getmBottomlayout().getVisibility() == View.GONE) {
			viewHolder.mCheckBox.setVisibility(View.GONE);
			viewHolder.mNum_tv.setVisibility(View.VISIBLE);
			viewHolder.mPhone_iv.setVisibility(View.VISIBLE);
			/*RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Util.dip2px(context, 40),Util.dip2px(context, 40));
			lp.setMargins(Util.dip2px(context, 20), Util.dip2px(context, 5), 0 ,Util.dip2px(context, 5));
			viewHolder.mHeadimg_iv.setLayoutParams(lp);*/
		}else if (PassListFragment .getmBottomlayout().getVisibility() == View.VISIBLE) {
			viewHolder.mCheckBox.setVisibility(View.VISIBLE);
			viewHolder.mNum_tv.setVisibility(View.GONE);
			viewHolder.mPhone_iv.setVisibility(View.GONE);
		}
		viewHolder.mCheckBox.setChecked(passTYList.get(position).isSelected());
//		viewHolder.mCheckBox.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (iListener != null) {
//					 iListener.onItemClick(v,position);
//				}
//			}
//		});
//		viewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				try {
//					passTYList.get(position).setSelected(isChecked);
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//		});
				
//		viewHolder.signlist_item_rl.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(viewHolder.mCheckBox.isChecked()){
//					viewHolder.mCheckBox.setChecked(false);
//					
//				}else{
//					viewHolder.mCheckBox.setChecked(true);
//					
//				}
////				try {
////					passTYList.get(positions).setSelected(viewHolder.mCheckBox.isChecked());
////				} catch (Exception e) {
////					// TODO: handle exception
////				}
//				
//				
//				
//			}
//		});
		ImageDownLoad.getDownLoadCircleImg(passTYList.get(position).getTddActivitySignVo().getSignHeadimgurl(), viewHolder.mHeadimg_iv);
		viewHolder.mNickname_tv.setText(passTYList.get(position).getTddActivitySignVo().getSignNickName());
		viewHolder.mTruename_tv.setText(passTYList.get(position).getTddActivitySignVo().getConnectUserName());
//		viewHolder.mNum_tv.setText((Integer.valueOf(signNoPassList.get(position).getKidNum()) + Integer
//		.valueOf(signNoPassList.get(position).getAdultNum())) + "");
		viewHolder.mNum_tv.setText("+1");
		//TODO 打电话
		
		return convertView;
	}

	class ViewHolder {
		private CheckBox mCheckBox;
		private RoundImageView mHeadimg_iv;// 头像
		private TextView mNickname_tv;// 昵称
		private TextView mTruename_tv;// 真实姓名
		private ImageView mPhone_iv;// 电话
		private TextView mNum_tv;// 随行人数
		private RelativeLayout signlist_item_rl;

		public ViewHolder(View convertView) {
			mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
			mHeadimg_iv = (RoundImageView) convertView.findViewById(R.id.headimg_iv);
			mNickname_tv = (TextView) convertView.findViewById(R.id.nickname_tv);
			mTruename_tv = (TextView) convertView.findViewById(R.id.truename_tv);
			mPhone_iv = (ImageView) convertView.findViewById(R.id.phone_iv);
			mNum_tv = (TextView) convertView.findViewById(R.id.num_tv);
			signlist_item_rl = (RelativeLayout) convertView.findViewById(R.id.signlist_item_rl);
		}

		public CheckBox getmCheckBox() {
			return mCheckBox;
		}

		public void setmCheckBox(CheckBox mCheckBox) {
			this.mCheckBox = mCheckBox;
		}
		
	}

	public List<TddActivitySignVoInfo> getPassTYList() {
		return passTYList;
	}

	public void setPassTYList(List<TddActivitySignVoInfo> passTYList) {
		this.passTYList = passTYList;
	}
	
	
	
}
