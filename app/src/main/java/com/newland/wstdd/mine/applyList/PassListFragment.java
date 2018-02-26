/**
 * @fields PassListFragment.java
 */
package com.newland.wstdd.mine.applyList;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragment;
import com.newland.wstdd.common.smsphone.CallPhoneUtil;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.Util;
import com.newland.wstdd.common.updownloadimg.ImageDownLoad;
import com.newland.wstdd.common.view.CustomListViews.IXListViewListener;
import com.newland.wstdd.common.widget.RoundImageView;
import com.newland.wstdd.mine.applyList.PassTMAdapter.onItemClickListener;
import com.newland.wstdd.mine.applyList.bean.TddActivitySignVo;
import com.newland.wstdd.mine.applyList.bean.TddActivitySignVoInfo;

/**
 * 通过名单
 * 
 * @author H81 2015-11-28
 * 
 */
@SuppressLint("ValidFragment")
public class PassListFragment extends BaseFragment implements IXListViewListener, OnClickListener, OnCheckedChangeListener {
	final String FLAG = "right_tv_change";
	public int signRole = 9;// 报名用户角色 1.团长 2.团秘 9.团员
	private String isLeader;// 是否是团大

	/** 团大 */
	private RelativeLayout relativelayout1;
	private RoundImageView mHeadimg_iv;// 头像
	private TextView mNickname_tv;// 昵称
	private TextView mTruename_tv;// 真实姓名
	private ImageView mPhone_iv;// 电话
	private TextView mNum_tv;// 随行人员
	/** 团大 */
	/** 团秘 */
	private TextView mPassTD_tv;// 团大&amp;团秘 (3)
	private BasePassListView mPassTM_listview;//
	private ImageView tuanda_iv;// 团大标识图片
	/** 团秘 */
	/** 团员 */
	private TextView mPassTY_tv;//
	private BasePassListView mPassTY_listview;//
	/** 团员 */
	/** 底部 团大权限 */
	private static LinearLayout mBottomlayout;//
	// private CheckBox mAllchose_checkbox;// 全选
	private TextView mAllchose_tv;// 全选
	private TextView mSetTM_tv;// 设置团秘
	private TextView mNoPass_tv;// 未通过
	/** 底部 团大权限 */
	/** 底部 团秘权限 */
	private static LinearLayout applylist_tm_bottom_ll;// 团秘权限下显示的底部布局
	private TextView applist_tm_allchose_tv;// 全选
	private TextView applist_tm_nopass_tv;// 未通过
	/** 底部 团秘权限 */
	private Context context;
	private ManagerApplyListActivity activity;
	TddActivitySignVo tdTddActivitySignVo = new TddActivitySignVo();// 团大
	private List<TddActivitySignVoInfo> passAllList;// 通过的所有名单
	private List<TddActivitySignVoInfo> passTMList;// 团秘
	private PassTMAdapter passTMAdapter;

	private List<TddActivitySignVoInfo> passTYList = new ArrayList<TddActivitySignVoInfo>();// 团员
	private PassTYAdapter passTYAdapter;// 团员
	IntentFilter intentFilter;
	IntentFilter updateTYFilter;// 更新团员列表

	private boolean isInit = false;

	private boolean hasTd = false;// 是否有团大
	private int td_tm_num = 0;// 团大&团秘个数
	private int tynum = 0;// 团员个数

	public static PassListFragment newInstance(Context context) {
		return new PassListFragment(context);
	}

	@SuppressLint("ValidFragment")
	public PassListFragment(Context context) {
		this.context = context;
		activity = (ManagerApplyListActivity) context;
	}

	@Override
	public void onListViewRefresh() {

	}

	@Override
	public void onListViewLoadMore() {

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		intentFilter = new IntentFilter(FLAG);
		getActivity().registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		passAllList = (List<TddActivitySignVoInfo>) bundle.getSerializable("passAllList");
		for (int i = 0, size = passAllList.size(); i < size; i++) {
			if (passAllList.get(i).getTddActivitySignVo().getAuditStatus() == 2 && passAllList.get(i).getTddActivitySignVo().getSignRole() == 1) {
				tdTddActivitySignVo = passAllList.get(i).getTddActivitySignVo();
			}
		}
		// tdTddActivitySignVo = (TddActivitySignVo)
		// bundle.getSerializable("tdTddActivitySignVo");
		signRole = (Integer) bundle.getSerializable("signRole");
		isLeader = (String) bundle.getSerializable("isLeader");
		View mView = inflater.inflate(R.layout.activity_applylist_pass, container, false);
		bindViews(mView);
		intentFilter = new IntentFilter(FLAG);
		getActivity().registerReceiver(broadcastReceiver, intentFilter);
		updateTYFilter = new IntentFilter("updatePassTYList");
		// getActivity().registerReceiver(updateReceiver, updateTYFilter);
		refresh();

		activity.passNum = passAllList.size();
		activity.noPassNum = ManagerApplyListActivity.allList.size() - activity.passNum;
		// if (hasTd) {
		// activity.passNum ++ ;
		// }
		// activity.hasTD = hasTd;
		// activity.setPassTextViewData(activity.passNum);
		// activity.setNoPassTextViewData(activity.noPassNum);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		isInit = true;
	}

	@Override
	public void onStop() {
		super.onStop();
		getActivity().unregisterReceiver(broadcastReceiver);
		// getActivity().unregisterReceiver(updateReceiver);
	}

	private void bindViews(View mView) {
		tuanda_iv = (ImageView) mView.findViewById(R.id.tuanda_iv);
		relativelayout1 = (RelativeLayout) mView.findViewById(R.id.relativelayout1);
		mPassTD_tv = (TextView) mView.findViewById(R.id.passTD_tv);
		mPassTM_listview = (BasePassListView) mView.findViewById(R.id.passTM_listview);

		mHeadimg_iv = (RoundImageView) mView.findViewById(R.id.headimg_iv);
		mNickname_tv = (TextView) mView.findViewById(R.id.nickname_tv);
		mTruename_tv = (TextView) mView.findViewById(R.id.truename_tv);
		mPhone_iv = (ImageView) mView.findViewById(R.id.phone_iv);
		mNum_tv = (TextView) mView.findViewById(R.id.num_tv);

		mPassTY_tv = (TextView) mView.findViewById(R.id.passTY_tv);
		mPassTY_listview = (BasePassListView) mView.findViewById(R.id.passTY_listview);
//		mPassTY_listview.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		mPassTY_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				CheckBox mCheckBox =(CheckBox) view.findViewById(R.id.checkBox);
				if(mCheckBox.isChecked()){
					mCheckBox.setChecked(false);
					
				}else{
					mCheckBox.setChecked(true);
					
				}
				passTYList.get(position).setSelected(mCheckBox.isChecked());
			}
		});
		
		
		
		
		

		// 团秘权限下显示的底部布局
		applylist_tm_bottom_ll = (LinearLayout) mView.findViewById(R.id.applylist_tm_bottom_ll);
		applist_tm_allchose_tv = (TextView) mView.findViewById(R.id.applist_tm_allchose_tv);
		applist_tm_nopass_tv = (TextView) mView.findViewById(R.id.applist_tm_nopass_tv);

		// 团大权限下显示的底部布局
		mBottomlayout = (LinearLayout) mView.findViewById(R.id.bottomlayout);
		// mAllchose_checkbox = (CheckBox)
		// mView.findViewById(R.id.allchose_checkbox);
		mAllchose_tv = (TextView) mView.findViewById(R.id.allchose_tv);
		mSetTM_tv = (TextView) mView.findViewById(R.id.setTM_tv);
		mNoPass_tv = (TextView) mView.findViewById(R.id.nopass_tv);
		// mAllchose_checkbox.setOnCheckedChangeListener(this);

		mAllchose_tv.setOnClickListener(this);
		mSetTM_tv.setOnClickListener(this);
		mNoPass_tv.setOnClickListener(this);
		applist_tm_allchose_tv.setOnClickListener(this);
		applist_tm_nopass_tv.setOnClickListener(this);
	}

	/**
	 * 设置团大&团秘个数
	 * 
	 * @param num
	 */
	private void setTdTmNum(int num) {

		mPassTD_tv.setText("团大&团秘(" + num + ")");
	}

	/**
	 * 设置团员个数
	 * 
	 * @param num
	 */
	private void setTyNum(int num) {
		mPassTY_tv.setText("团员(" + num + ")");
	}

	@Override
	public void refresh() {

		if (getActivity() == null) // 说明上层activity 已经被销毁了
		{
			System.out.println("getActivity() 拦截");
			return;
		}
		// 团秘
		passTMList = new ArrayList<TddActivitySignVoInfo>();

		// TODO 设置团大信息
		if (tdTddActivitySignVo != null && StringUtil.isNotEmpty(tdTddActivitySignVo.getActivityId())) {
			relativelayout1.setVisibility(View.VISIBLE);
			if (StringUtil.isNotEmpty(tdTddActivitySignVo.getSignHeadimgurl())) {
				ImageDownLoad.getDownLoadCircleImg(tdTddActivitySignVo.getSignHeadimgurl(), mHeadimg_iv);// 头像
			} else {
				mHeadimg_iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.defaultheadimg));
			}

			mNickname_tv.setText(tdTddActivitySignVo.getSignNickName());// 昵称
			mTruename_tv.setText(tdTddActivitySignVo.getSignUserName());// 真实姓名
			tuanda_iv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.signlist_td_bg));
			mPhone_iv.setOnClickListener(this);//手机
			if (tdTddActivitySignVo.getTotalJoinNum() != 0) {
				mNum_tv.setText("+"+tdTddActivitySignVo.getTotalJoinNum() + ""); // 随行人员
			}else {
				mNum_tv.setVisibility(View.GONE);
			}
			hasTd = true;
		} else {
			relativelayout1.setVisibility(View.INVISIBLE);
		}

		/**
		 * //团大
		 * 
		 * //团秘
		 * 
		 * //团员
		 */
		passTMList.clear();
		passTYList.clear();

		for (int i = 0; i < passAllList.size(); i++) {
			if (passAllList.get(i).getTddActivitySignVo().getSignRole() == 2) {// signRole
				// 报名用户角色
				// 1.团大
				// 2.团秘
				// 9.团员
				passTMList.add(passAllList.get(i));
				td_tm_num = 0;
				td_tm_num = passTMList.size();
			} else if (passAllList.get(i).getTddActivitySignVo().getSignRole() == 9) {// signRole
				// 报名用户角色
				// 1.团大
				// 2.团秘
				// 9.团员
				passTYList.add(passAllList.get(i));
				tynum = passTYList.size();
			}
		}
		passTMAdapter = new PassTMAdapter(getActivity(), passTMList);
		mPassTM_listview.setAdapter(passTMAdapter);
		passTMAdapter.notifyDataSetChanged();
		for (int i = 0, size = passTMList.size(); i < size; i++) {
			passTMList.get(i).setSelected(false);
		}
		for (int i = 0, size = passTYList.size(); i < size; i++) {
			passTYList.get(i).setSelected(false);
		}
		passTYAdapter = new PassTYAdapter(getActivity(), passTYList);
		mPassTY_listview.setAdapter(passTYAdapter);
		passTYAdapter.notifyDataSetChanged();
		// mBottomlayout.setVisibility(View.GONE);
		if (hasTd) {
			td_tm_num++;
		}

		setTdTmNum(td_tm_num);
		setTyNum(tynum);

		// 取消团秘
		passTMAdapter.setOnItemClickListener(new onItemClickListener() {

			@Override
			public void onItemClick(View v, int position) {
				// passTYList.clear();
				ok: for (int i = 0; i < ManagerApplyListActivity.allList.size(); i++) {
					// for (int j = 0; j < passTMList.size(); j++) {
					if (passTMList != null && passTMList.size() > 0) {

						if (passTMList.get(position).getTddActivitySignVo().getSignId().equals(ManagerApplyListActivity.allList.get(i).getTddActivitySignVo().getSignId())) {
							ManagerApplyListActivity.allList.get(i).getTddActivitySignVo().setAuditStatus(2);// 审核状态
							// 1.未审核2.已通过
							// 3.未通过
							ManagerApplyListActivity.allList.get(i).getTddActivitySignVo().setSignRole(9);// 报名用户角色
							// 1.团大2.团秘
							// 9.团员
							passTMList.remove(position);
							passTYList.add(ManagerApplyListActivity.allList.get(i));
							List<TddActivitySignVoInfo> pmList = passTMList;
							List<TddActivitySignVoInfo> pList = passTYList;
							td_tm_num--;
							tynum++;
							setTdTmNum(td_tm_num);
							setTyNum(tynum);
							for (int j = 0; j < passTYList.size(); j++) {
								passTYList.get(j).setSelected(false);
							}
							break ok;
						}
					}
					// }
				}
				for (int i = 0; i < passTYList.size(); i++) {
					passTYList.get(i).setSelected(false);
				}
				// passTYAdapter = new PassTYAdapter(getActivity(), passTYList);
				passTYAdapter.setPassTYList(passTYList);
				passTMAdapter.notifyDataSetChanged();
				passTYAdapter.notifyDataSetChanged();
				// mAllchose_checkbox.setChecked(false);

			}
		});

		/*passTYAdapter.setOnItemClickListener(new com.newland.wstdd.mine.applyList.PassTYAdapter.onItemClickListener() {

			@Override
			public void onItemClick(View v, int position) {
				if (passTYList.get(position).isSelected()) {
					passTYList.get(position).setSelected(false);
//					passTYAdapter.viewHolder.getmCheckBox().setChecked(false);
				}else {
					passTYList.get(position).setSelected(true);
//					passTYAdapter.viewHolder.getmCheckBox().setChecked(true);
				}
				passTYAdapter.setPassTYList(passTYList);
				passTYAdapter.notifyDataSetChanged();
			}
		});*/
	}

	@Override
	public void onClick(View v) {
		boolean isChecked = false;
		switch (v.getId()) {
		case R.id.setTM_tv:// 设置团秘
			for (int i = 0; i < ManagerApplyListActivity.allList.size(); i++) {
				for (int j = 0; j < passTYList.size(); j++) {
					boolean passTYSelected = passTYList.get(j).isSelected();
					System.out.println("passTYSelected----------------------------" + passTYSelected);
					if (passTYSelected) {
						if (passTYList.get(j).getTddActivitySignVo().getSignId().equals(ManagerApplyListActivity.allList.get(i).getTddActivitySignVo().getSignId())) {
							ManagerApplyListActivity.allList.get(i).getTddActivitySignVo().setAuditStatus(2);
							ManagerApplyListActivity.allList.get(i).getTddActivitySignVo().setSignRole(2);
							passTYList.remove(j);
							j--;
							passTMList.add(ManagerApplyListActivity.allList.get(i));
							tynum--;
							td_tm_num++;
						}
					}
				}
			}
			setTdTmNum(td_tm_num);
			setTyNum(tynum);

			mAllchose_tv.setText("全选");

			passTMAdapter.notifyDataSetChanged();
			passTYAdapter.notifyDataSetChanged();
			break;
		case R.id.nopass_tv:// 未通过
			for (int j = 0, size = ManagerApplyListActivity.allList.size(); j < size; j++) {
				for (int i = 0; i < passTYList.size(); i++) {
					boolean isSelected = passTYList.get(i).isSelected();
					System.out.println("passTYisSelected----------------------------" + isSelected);
					if (isSelected) {
						if (passTYList.get(i).getTddActivitySignVo().getSignId().equals(ManagerApplyListActivity.allList.get(j).getTddActivitySignVo().getSignId())) {
							ManagerApplyListActivity.allList.get(j).getTddActivitySignVo().setAuditStatus(3);// 审核状态
							// 1.未审核2.已通过
							// 3.未通过
							passTYList.remove(i);
							tynum--;
						}
					}
				}
			}
			activity.passNum = passTMList.size() + passTYList.size();
			activity.noPassNum = ManagerApplyListActivity.allList.size() - activity.passNum;
			if (hasTd) {
				activity.passNum++;
			}
			activity.hasTD = hasTd;
			activity.setPassTextViewData(activity.passNum);
			activity.setNoPassTextViewData(activity.noPassNum);
			setTyNum(tynum);

			mAllchose_tv.setText("全选");

			passTYAdapter.notifyDataSetChanged();
			break;
		case R.id.allchose_tv:// 全选
			if (mAllchose_tv.getText().toString().equals("全选")) {
				mAllchose_tv.setText("取消");
				isChecked = true;
			} else if (mAllchose_tv.getText().toString().equals("取消")) {
				mAllchose_tv.setText("全选");
				isChecked = false;
			}

			for (int i = 0; i < passTYList.size(); i++) {
				// PassTYAdapter.getIsSelected().put(i, isChecked);
				passTYList.get(i).setSelected(isChecked);
			}
			// 刷新listview和TextView的显示
			// dataChanged();
			passTYAdapter.notifyDataSetChanged();
			passTMAdapter.notifyDataSetChanged();
			break;
		case R.id.applist_tm_allchose_tv:// 団秘权限下全选
			if (applist_tm_allchose_tv.getText().toString().equals("全选")) {
				applist_tm_allchose_tv.setText("取消");
				isChecked = true;
			} else if (applist_tm_allchose_tv.getText().toString().equals("取消")) {
				applist_tm_allchose_tv.setText("全选");
				isChecked = false;
			}
			for (int i = 0; i < passTYList.size(); i++) {
				// PassTYAdapter.getIsSelected().put(i, isChecked);
				passTYList.get(i).setSelected(isChecked);
			}
			// 刷新listview和TextView的显示
			// dataChanged();
			passTYAdapter.notifyDataSetChanged();
			passTMAdapter.notifyDataSetChanged();
			break;
		case R.id.applist_tm_nopass_tv:// 团秘权限下未通过
			for (int j = 0, size = ManagerApplyListActivity.allList.size(); j < size; j++) {
				for (int i = 0; i < passTYList.size(); i++) {
					boolean isSelected = passTYList.get(i).isSelected();
					System.out.println("passTYisSelected----------------------------" + isSelected);
					if (isSelected) {
						if (passTYList.get(i).getTddActivitySignVo().getSignId().equals(ManagerApplyListActivity.allList.get(j).getTddActivitySignVo().getSignId())) {
							ManagerApplyListActivity.allList.get(j).getTddActivitySignVo().setAuditStatus(3);// 审核状态
							// 1.未审核2.已通过
							// 3.未通过
							passTYList.remove(i);
							tynum--;
						}
					}
				}
			}
			activity.passNum = passTMList.size() + passTYList.size();
			activity.noPassNum = ManagerApplyListActivity.allList.size() - activity.passNum;
			if (hasTd) {
				activity.passNum++;
			}
			activity.hasTD = hasTd;
			activity.setPassTextViewData(activity.passNum);
			activity.setNoPassTextViewData(activity.noPassNum);
			setTyNum(tynum);

			applist_tm_allchose_tv.setText("全选");

			passTYAdapter.notifyDataSetChanged();
			break;
		case R.id.phone_iv://团大拨打电话
			if (tdTddActivitySignVo != null) {
				Util.showCustomConfirmCancelDialog(context, "温馨提示", "是否拨打电话"+tdTddActivitySignVo.getSignUserPhone(), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CallPhoneUtil.callPhone(tdTddActivitySignVo.getSignUserPhone(), context);
					}
				});
			}
			break;
		default:
			break;
		}
	}

	public static LinearLayout getmBottomlayout() {
		return mBottomlayout;
	}

	public static void setmBottomlayout(LinearLayout mBottomlayout) {
		PassListFragment.mBottomlayout = mBottomlayout;
	}

	/**
	 * 接收到广播之后
	 */
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			TextView rightTv = (TextView) getActivity().findViewById(R.id.head_right_tv);
			if (rightTv.getText().toString().equals("编辑")) {
				applylist_tm_bottom_ll.setVisibility(View.GONE);
				mBottomlayout.setVisibility(View.GONE);
			} else if (rightTv.getText().toString().equals("完成")) {
				if (signRole == 2) {
					applylist_tm_bottom_ll.setVisibility(View.VISIBLE);
					mBottomlayout.setVisibility(View.GONE);
				} else if (isLeader.equals("true")) {
					applylist_tm_bottom_ll.setVisibility(View.GONE);
					mBottomlayout.setVisibility(View.VISIBLE);
				}
			}
			dataChanged();
		}
	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		for (int i = 0; i < passTYList.size(); i++) {
			// PassTYAdapter.getIsSelected().put(i, isChecked);
			passTYList.get(i).setSelected(isChecked);
		}
		// 刷新listview和TextView的显示
		// dataChanged();
		passTYAdapter.notifyDataSetChanged();
		passTMAdapter.notifyDataSetChanged();
	}

	// 刷新listview和TextView的显示
	public void dataChanged() {
		// 通知listView刷新
		passAllList.clear();
		td_tm_num = 0;
		tynum = 0;
		for (int i = 0; i < ManagerApplyListActivity.allList.size(); i++) {
			if (ManagerApplyListActivity.allList.get(i).getTddActivitySignVo().getAuditStatus() == 2) {
				passAllList.add(ManagerApplyListActivity.allList.get(i));
			}
		}
		passTMList.clear();
		passTYList.clear();
		for (int i = 0; i < passAllList.size(); i++) {
			if (passAllList.get(i).getTddActivitySignVo().getSignRole() == 1) {// signRole
				// 报名用户角色 1.团大
				// 2.团秘 9.团员
				tdTddActivitySignVo = passAllList.get(i).getTddActivitySignVo();
			} else if (passAllList.get(i).getTddActivitySignVo().getSignRole() == 2) {// signRole
				// 报名用户角色
				// 1.团大
				// 2.团秘
				// 9.团员
				passTMList.add(passAllList.get(i));
				td_tm_num = passTMList.size();
			} else if (passAllList.get(i).getTddActivitySignVo().getSignRole() == 9) {// signRole
				// 报名用户角色
				// 1.团大
				// 2.团秘
				// 9.团员
				passTYList.add(passAllList.get(i));
				tynum = passTYList.size();
			}
		}
		if (hasTd) {// 有团大
			td_tm_num++;
		}

		activity.passNum = passTMList.size() + passTYList.size();
		activity.noPassNum = ManagerApplyListActivity.allList.size() - activity.passNum;
		if (hasTd) {
			activity.passNum++;
		}
		activity.hasTD = hasTd;
		activity.setPassTextViewData(activity.passNum);
		activity.setNoPassTextViewData(activity.noPassNum);

		setTdTmNum(td_tm_num);
		setTyNum(tynum);

		for (int i = 0; i < passTYList.size(); i++) {
			passTYList.get(i).setSelected(false);
		}
		try {
			passTYAdapter.setPassTYList(passTYList);
			passTMAdapter.setPassTYList(passTMList);
			passTYAdapter.notifyDataSetChanged();
			passTMAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 对用户可见的时候，刷新界面
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && isInit) {
			// mAllchose_checkbox.setChecked(false);
			dataChanged();
		}

	};

	// /**
	// * 接收到团秘列表编号广播之后
	// */
	// BroadcastReceiver updateReceiver = new BroadcastReceiver() {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// for (int i = 0; i < ManagerApplyListActivity.allList.size(); i++) {
	// if (ManagerApplyListActivity.allList.get(i).getSignRole()!=null) {
	//
	// if (ManagerApplyListActivity.allList.get(i).getSignRole().equals("9"))
	// {// signRole
	// // 报名用户角色
	// // 1.团大
	// // 2.团秘
	// // 9.团员
	// passTYList.add(ManagerApplyListActivity.allList.get(i));
	// }
	// }
	// }
	// for (int i = 0; i < passTYList.size(); i++) {
	// passTYList.get(i).setSelected(false);
	// }
	// passTYAdapter = new PassTYAdapter(getActivity(), passTYList);
	// passTYAdapter.setPassTYList(passTYList);
	// passTYAdapter.notifyDataSetChanged();
	// }
	// };

}
