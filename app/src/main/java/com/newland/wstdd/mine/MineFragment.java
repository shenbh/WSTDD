package com.newland.wstdd.mine;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragment;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.tools.UiToUiHelper;
import com.newland.wstdd.common.updownloadimg.ImageDownLoad;
import com.newland.wstdd.common.widget.RoundImageView;
import com.newland.wstdd.mine.managerpage.MyActivitiesListAcitivity;
import com.newland.wstdd.mine.minesetting.MineSettingActivity;
import com.newland.wstdd.mine.myinterest.MyInterestActivity;
import com.newland.wstdd.mine.myinterest.beanrequest.TddUserTagQuery;
import com.newland.wstdd.mine.personalcenter.MinePersonalCenterActivity;
import com.newland.wstdd.mine.receiptaddress.MineReceiptAddressListActivity;
import com.newland.wstdd.mine.servicecenter.MineServiceCenterActivity;
import com.newland.wstdd.mine.twocode.TwoDimensionCodeActivity;

/**
 * 我的界面
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ValidFragment")
public class MineFragment extends BaseFragment implements OnClickListener {
	private boolean initBoolean=false;//初始化的boolean 用来作为初始化用的
	private TextView mMine_nickname_tv;// 昵称
	private RoundImageView mMine_nickname_iv;// 昵称图片
	private TextView mMine_originate_content_tv;// 我发起数值
	private TextView mMine_originate_tv;// 我发起
	private TextView mMine_participation_content_tv;// 我参与数值
	private TextView mMine_participation_tv;// 我参与
	private TextView mMine_collect_content_tv;// 我收藏数值
	private TextView mMine_collect_tv;// 我收藏
	private ImageView mMine_setting_iv;// 设置
	private TextView mFragment_mine_myproperty_tv;// 我的资产
	private TextView mFragment_mine_integral_tv;// 积分
	private TextView mFragment_mine_found_tv;// 资金
	private TextView mFragment_mine_accountbook_tv;// 记账本
	private TextView mFragment_mine_redpacket_tv;// 红包
	private TextView mFragment_mine_ithaca_tv;// 卡券
	private RelativeLayout mMine_receiveraddress_rl;// 设置收货地址
	private RelativeLayout mMine_qrcode_rl;// 支付二维码
	private RelativeLayout mMine_servicecenter_rl;// 服务中心
	private RelativeLayout mMine_interesttag_rl;// 兴趣标签
	private LinearLayout mMine_originate_content_ll;// 我发起
	private LinearLayout mMine_participation_content_ll;// 我参与
	private LinearLayout mMine_collect_content_ll;// 我收藏
	private TextView mMine_mytags_tv1,mMine_mytags_tv2,mMine_mytags_tv3;//标签
	private View view;
	List<TddUserTagQuery> infos;//这个是用来接收 修改标签之后  通过广播发送回来更新界面用的
	/**
	 * 注册广播，等报名结束之后，更新界面时候用的   或者编辑报名的时候使用
	 */
	private IntentFilter filter;// 定一个广播接收过滤器
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("ExampleFragment--onCreate");
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		} else {
			try {
				view = inflater.inflate(R.layout.fragment_mine, container, false);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	
		
		initView();
		filter = new IntentFilter("Refresh_MineFragment");//用来接收从这个界面出去之后回来之后的tddactivity
		getActivity().registerReceiver(broadcastReceiver, filter);// 在对象broadcastReceiver中进行接收的相应处理
//		setUserVisibleHint(true);
		super.onActivityCreated(savedInstanceState);
	}

	


	private void initView() {

		mMine_nickname_tv = (TextView) view.findViewById(R.id.mine_nickname_tv);
		mMine_nickname_iv = (RoundImageView) view.findViewById(R.id.mine_nickname_iv);
		mMine_originate_content_tv = (TextView) view.findViewById(R.id.mine_originate_content_tv);
		mMine_originate_tv = (TextView) view.findViewById(R.id.mine_originate_tv);
		mMine_participation_content_tv = (TextView) view.findViewById(R.id.mine_participation_content_tv);
		mMine_participation_tv = (TextView) view.findViewById(R.id.mine_participation_tv);
		mMine_collect_content_tv = (TextView) view.findViewById(R.id.mine_collect_content_tv);
		mMine_collect_tv = (TextView) view.findViewById(R.id.mine_collect_tv);
		mMine_setting_iv = (ImageView) view.findViewById(R.id.mine_setting_iv);
		mFragment_mine_myproperty_tv = (TextView) view.findViewById(R.id.fragment_mine_myproperty_tv);
		mFragment_mine_integral_tv = (TextView) view.findViewById(R.id.fragment_mine_integral_tv);
		mFragment_mine_found_tv = (TextView) view.findViewById(R.id.fragment_mine_found_tv);
		mFragment_mine_accountbook_tv = (TextView) view.findViewById(R.id.fragment_mine_accountbook_tv);
		mFragment_mine_redpacket_tv = (TextView) view.findViewById(R.id.fragment_mine_redpacket_tv);
		mFragment_mine_ithaca_tv = (TextView) view.findViewById(R.id.fragment_mine_ithaca_tv);
		mMine_receiveraddress_rl = (RelativeLayout) view.findViewById(R.id.mine_receiveraddress_rl);
		mMine_qrcode_rl = (RelativeLayout) view.findViewById(R.id.mine_qrcode_rl);
		mMine_servicecenter_rl = (RelativeLayout) view.findViewById(R.id.mine_servicecenter_rl);
		mMine_interesttag_rl = (RelativeLayout) view.findViewById(R.id.mine_interesttag_rl);
		mMine_originate_content_ll = (LinearLayout) view.findViewById(R.id.mine_originate_content_ll);
		mMine_participation_content_ll = (LinearLayout) view.findViewById(R.id.mine_participation_content_ll);
		mMine_collect_content_ll = (LinearLayout) view.findViewById(R.id.mine_collect_content_ll);
		mMine_mytags_tv1 = (TextView) view.findViewById(R.id.mine_mytags1_tv);
		mMine_mytags_tv2 = (TextView) view.findViewById(R.id.mine_mytags2_tv);
		mMine_mytags_tv3 = (TextView) view.findViewById(R.id.mine_mytags3_tv);

		setData();
		setClickListener();
	}
	
	
	/**设置数据
	 * 
	 */
	@SuppressLint("NewApi")
	private void setData(){
		
		if (AppContext.getAppContext().getHeadImgUrl() != null && !"".equals(AppContext.getAppContext().getHeadImgUrl())) {
			ImageDownLoad.getDownLoadSmallImg(AppContext.getAppContext().getHeadImgUrl(), mMine_nickname_iv);
		}else {

			try {
				InputStream is = getResources().openRawResource(R.drawable.default_head_icon);  
				Bitmap mBitmap = BitmapFactory.decodeStream(is);
				mMine_nickname_iv.setImageBitmap((UiHelper.CircleImageView(mBitmap, 2)));
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mMine_nickname_tv.setText(AppContext.getAppContext().getNickName());
		
		if(AppContext.getAppContext().getMyAcNum()!=null&&!"".equals(AppContext.getAppContext().getMyAcNum())){
			mMine_originate_content_tv.setText(AppContext.getAppContext().getMyAcNum());
		}else{
			mMine_originate_content_tv.setText("0");
		}
		if(AppContext.getAppContext().getMySignAcNum()!=null&&!"".equals(AppContext.getAppContext().getMySignAcNum())){
			mMine_participation_content_tv.setText(AppContext.getAppContext().getMySignAcNum());
		}else{
			mMine_collect_content_tv.setText("0");
		}
		if(AppContext.getAppContext().getMyFavAcNum()!=null&&!"".equals(AppContext.getAppContext().getMyFavAcNum())){
			mMine_collect_content_tv.setText(AppContext.getAppContext().getMyFavAcNum());
		}else{
			mMine_collect_content_tv.setText("0");
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		if(AppContext.getAppContext().getTags()==null||AppContext.getAppContext().getTags().isEmpty()){
			String aa= AppContext.getAppContext().getMyTags();
			AppContext.getAppContext().setTags(StringUtil.appContextTagsStringToList(AppContext.getAppContext().getMyTags()));
		}
		if(AppContext.getAppContext().getTags()!=null&&!"".equals(AppContext.getAppContext().getTags())){
		
		if(AppContext.getAppContext().getTags().size()>0&&AppContext.getAppContext().getTags().size()==1&&!"".equals(AppContext.getAppContext().getTags().get(0))){
			 mMine_mytags_tv3.setText(AppContext.getAppContext().getTags().get(0));
			 mMine_mytags_tv2.setVisibility(View.INVISIBLE);
			 mMine_mytags_tv1.setVisibility(View.INVISIBLE);
			 mMine_mytags_tv3.setVisibility(View.VISIBLE);
		}else if(AppContext.getAppContext().getTags().size()>0&&AppContext.getAppContext().getTags().size()==2){
			 mMine_mytags_tv2.setText(AppContext.getAppContext().getTags().get(0));
			 mMine_mytags_tv3.setText(AppContext.getAppContext().getTags().get(1));
			 mMine_mytags_tv3.setBackground(getResources().getDrawable(R.drawable.text_rectanglegrayboderpadding1style));
			 mMine_mytags_tv1.setVisibility(View.INVISIBLE);
			 mMine_mytags_tv2.setVisibility(View.VISIBLE);
			 mMine_mytags_tv3.setVisibility(View.VISIBLE);
			 
		}
		else if(AppContext.getAppContext().getTags().size()>2){
			    mMine_mytags_tv1.setText(AppContext.getAppContext().getTags().get(0));
			    mMine_mytags_tv2.setText(AppContext.getAppContext().getTags().get(1));
			    mMine_mytags_tv3.setBackground(null);
			    mMine_mytags_tv3.setText("...");
			    mMine_mytags_tv1.setVisibility(View.VISIBLE);
			    mMine_mytags_tv2.setVisibility(View.VISIBLE);
			    mMine_mytags_tv3.setVisibility(View.VISIBLE);
		}else{
			 mMine_mytags_tv2.setVisibility(View.GONE);
			 mMine_mytags_tv1.setVisibility(View.GONE);
			 mMine_mytags_tv3.setVisibility(View.GONE);
		}
		
	}else{
		 mMine_mytags_tv2.setVisibility(View.GONE);
		 mMine_mytags_tv1.setVisibility(View.GONE);
		 mMine_mytags_tv3.setVisibility(View.GONE);
	}
}
	
	//设置监听事件
	private void setClickListener() {
		mMine_nickname_iv.setOnClickListener(this);
		mMine_setting_iv.setOnClickListener(this);
		mMine_receiveraddress_rl.setOnClickListener(this);
		mMine_qrcode_rl.setOnClickListener(this);
		mMine_servicecenter_rl.setOnClickListener(this);
		mMine_interesttag_rl.setOnClickListener(this);

		mFragment_mine_integral_tv.setOnClickListener(this);
		mFragment_mine_found_tv.setOnClickListener(this);
		mFragment_mine_accountbook_tv.setOnClickListener(this);
		mFragment_mine_redpacket_tv.setOnClickListener(this);
		mFragment_mine_ithaca_tv.setOnClickListener(this);

		mMine_originate_content_ll.setOnClickListener(this);
		mMine_participation_content_ll.setOnClickListener(this);
		mMine_collect_content_ll.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.mine_nickname_iv:// 头像
			if(null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin())||"false".equals(AppContext.getAppContext().getIsLogin())){
//				UiHelper.ShowOneToast(getActivity(), "该操作需要登录后进行！");
				UiToUiHelper.showLogin(getActivity());
			}else if("true".equals(AppContext.getAppContext().getIsLogin())){
			intent = new Intent(getActivity(), MinePersonalCenterActivity.class);
			startActivity(intent);
			}
			break;

		case R.id.mine_originate_content_ll:
			if(null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin())||"false".equals(AppContext.getAppContext().getIsLogin())){
//				UiHelper.ShowOneToast(getActivity(), "该操作需要登录后进行！");
				UiToUiHelper.showLogin(getActivity());
				
			}else if("true".equals(AppContext.getAppContext().getIsLogin())){
			intent = new Intent(getActivity(), MyActivitiesListAcitivity.class);
			intent.putExtra("activitybtn", "originate");
			startActivity(intent);
			}
			break;
		case R.id.mine_participation_content_ll:
			if(null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin())||"false".equals(AppContext.getAppContext().getIsLogin())){
//				UiHelper.ShowOneToast(getActivity(), "该操作需要登录后进行！");
				UiToUiHelper.showLogin(getActivity());
				
			}else if("true".equals(AppContext.getAppContext().getIsLogin())){
			intent = new Intent(getActivity(), MyActivitiesListAcitivity.class);
			intent.putExtra("activitybtn", "participation");
			startActivity(intent);
			}
			break;
		case R.id.mine_collect_content_ll:
			if(null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin())||"false".equals(AppContext.getAppContext().getIsLogin())){
//				UiHelper.ShowOneToast(getActivity(), "该操作需要登录后进行！");
				UiToUiHelper.showLogin(getActivity());
				
			}else if("true".equals(AppContext.getAppContext().getIsLogin())){
			intent = new Intent(getActivity(), MyActivitiesListAcitivity.class);
			intent.putExtra("activitybtn", "collect");
			startActivity(intent);
			}
			break;
		case R.id.mine_setting_iv:// 设置
			if(null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin())||"false".equals(AppContext.getAppContext().getIsLogin())){
//				UiHelper.ShowOneToast(getActivity(), "该操作需要登录后进行！");
				UiToUiHelper.showLogin(getActivity());
			}else if("true".equals(AppContext.getAppContext().getIsLogin())){
			intent = new Intent(getActivity(), MineSettingActivity.class);
			startActivity(intent);
			}
			break;
		case R.id.mine_receiveraddress_rl:// 收货地址
			if(null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin())||"false".equals(AppContext.getAppContext().getIsLogin())){
//				UiHelper.ShowOneToast(getActivity(), "该操作需要登录后进行！");
				UiToUiHelper.showLogin(getActivity());
			}else if("true".equals(AppContext.getAppContext().getIsLogin())){
			intent = new Intent(getActivity(), MineReceiptAddressListActivity.class);
			startActivity(intent);
			}
			break;
		case R.id.mine_qrcode_rl:// 二维码
			if(null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin())||"false".equals(AppContext.getAppContext().getIsLogin())){
//				UiHelper.ShowOneToast(getActivity(), "该操作需要登录后进行！");
				UiToUiHelper.showLogin(getActivity());
			}else if("true".equals(AppContext.getAppContext().getIsLogin())){
			intent = new Intent(getActivity(), TwoDimensionCodeActivity.class);
			startActivity(intent);
			}
			break;
		case R.id.mine_servicecenter_rl:// 服务中心
			intent = new Intent(getActivity(), MineServiceCenterActivity.class);
			startActivity(intent);
			break;
		case R.id.mine_interesttag_rl:// 兴趣标签
			if(null != AppContext.getAppContext() && "".equals(AppContext.getAppContext().getIsLogin())||"false".equals(AppContext.getAppContext().getIsLogin())){
//				UiHelper.ShowOneToast(getActivity(), "该操作需要登录后进行！");
				UiToUiHelper.showLogin(getActivity());
			}else if("true".equals(AppContext.getAppContext().getIsLogin())){
			intent = new Intent(getActivity(), MyInterestActivity.class);
			startActivity(intent);
			}
			break;
		case R.id.fragment_mine_integral_tv:// 积分
			UiHelper.ShowOneToast(getActivity(), "该功能正在开发中,敬请期待");
			break;
		case R.id.fragment_mine_found_tv:// 资金
			UiHelper.ShowOneToast(getActivity(), "该功能正在开发中,敬请期待");
			break;
		case R.id.fragment_mine_accountbook_tv:// 记账本
			UiHelper.ShowOneToast(getActivity(), "该功能正在开发中,敬请期待");
			break;
		case R.id.fragment_mine_redpacket_tv:// 红包
			UiHelper.ShowOneToast(getActivity(), "该功能正在开发中,敬请期待");
			break;
		case R.id.fragment_mine_ithaca_tv:// 卡券
			UiHelper.ShowOneToast(getActivity(), "该功能正在开发中,敬请期待");
			break;
		default:
			break;
		}
	}
	
	/**
	 * 设置一个广播，用来接收activity
	 */
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@SuppressWarnings({ "unchecked", "null" })
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
//			Bundle bundle=intent.getBundleExtra("bundle");
			@SuppressWarnings("unused")
			StringBuffer bufferString = null;
//			infos=(List<TddUserTagQuery>) bundle.getSerializable("TAG");
//			for (int i = 0; i < infos.size(); i++) {
//				bufferString.append(infos.get(i).getTagName());
//			}
//			mMine_mytags_tv.setText(bufferString);
//			infos.clear();
		}
	};
	
	// 执行完setUserVisibleHint 才会去执行onStart();
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			if (initBoolean) {
				initBoolean = false;
				if(getUserVisibleHint()){
					setUserVisibleHint(true);
				}
				
			
			} else {
				initBoolean = true;
			}
			super.onStart();
		}




	//优先于Oncreate()方法执行  但是第一次执行的话为false
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
//		    首次运行不可见    第二次运行this.isVisible可见   
	        if(this.isVisible()){
	        	if(isVisibleToUser){
//	        		UiHelper.ShowOneToast(getActivity(), "mine");
	        		if (isAdded()) {
							setData();
							initBoolean=true;
					}
			
				
			}
	      }
	      super.setUserVisibleHint(isVisibleToUser);
	
		
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}


	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}
}





