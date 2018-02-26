package com.newland.wstdd.find.categorylist;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.SharePreferenceUtil.SharedPreferencesRefreshUtil;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.bean.TddActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.view.CustomListViews.IXListViewListener;
import com.newland.wstdd.find.categorylist.bean.FindCategoryListViewInfo;
import com.newland.wstdd.find.categorylist.detail.FindChairDetailActivity;
import com.newland.wstdd.find.categorylist.detail.handle.SingleActivityDetailHandle;
import com.newland.wstdd.find.hotlist.bean.FindCategoryReq;
import com.newland.wstdd.find.hotlist.bean.FindCategoryRes;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.managerpage.ManagerPageActivity;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.originate.beanrequest.SingleActivityReq;
import com.newland.wstdd.originate.beanresponse.SingleActivityRes;

/**
 * 8个的分类的列表
 * 
 * @author Administrator
 * 
 */
public class ShowFindListViewActivity extends BaseFragmentActivity implements IXListViewListener, OnPostListenerInterface {
	private static final String TAG = "ShowFindListViewActivity";//收集异常日志tag
	private LinearLayout listviewEmptyImageView;//图片信息为空的时候
	private Intent intent;
	private TextView titleTextView;
	private String title;// 标题
	private String mess;// 跳转过来的信息是哪一个 比如讲座比如团购
	private String type;// 类型
	// ListView控件
	private com.newland.wstdd.common.view.CustomListViews findCategoryListViews;
	private FindCategoryListAdapter findCategoryListAdapter;
	private List<FindCategoryListViewInfo> findCategoryListViewInfos = new ArrayList<FindCategoryListViewInfo>();

	// 服务器返回信息
	private FindCategoryRes findCategoryRes;
	private FindCategoryListHandle handler = new FindCategoryListHandle(this);
	TddActivity tddActivity;
	private List<TddActivity> acList = new ArrayList<TddActivity>();// 返回的列表对象

	SharedPreferencesRefreshUtil sharedPreferencesUtil;

	private int page_index = 0;// 当前页码
	private AppContext appContext;
	
	//广播返回
	private String activityIdString;//活动id
	private String statusString;//报名状态
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		appContext = AppContext.getAppContext();
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		mess = bundle.getString("mess");
		title = mess;
		type = bundle.getString("type");
		AppContext.getAppContext().setMessUpDatetime(StringUtil.mess2Str(mess) + "UpdateTime");

		setContentView(R.layout.activity_show_find_list_view1);
		
		 /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

		sharedPreferencesUtil = new SharedPreferencesRefreshUtil(this);
		setTitle();
		initView(title);// 初始化控件
		selectShow(mess);

		
		IntentFilter intentFilter = new IntentFilter("StatusChange");
		registerReceiver(broadcastReceiver, intentFilter);
		// 报名人数变化的广播
		IntentFilter intentFilter2 = new IntentFilter("SignedNumChange");
		registerReceiver(signedNumChangeReceiver, intentFilter2);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		updateStatus();
		updateSignNum();
	}
	
	/**
	 * 更新报名状态
	 */
	private void updateStatus() {
		if (findCategoryListViewInfos != null&&findCategoryListViewInfos.size()>0) {
			for (int j = 0 , size =findCategoryListViewInfos.size(); j < size; j++) {
				if (activityIdString!=null&&activityIdString.equals(findCategoryListViewInfos.get(j).getActivityId())) {
					if ("关闭报名".equals(statusString)) {
						findCategoryListViewInfos.get(j).setStatus(2);//状态  0.未发布 1.报名中 2.关闭报名 4.已撤销
						break;
					}else if ("开启报名".equals(statusString)) {
						findCategoryListViewInfos.get(j).setStatus(1);
						break;
					}
				}
			}
			if (findCategoryListAdapter!=null) {
				findCategoryListAdapter.notifyDataSetChanged();
			}
		}
	}

	/**更新报名人数
	 * 
	 */
	private void updateSignNum(){
		for (int i = 0 , size = findCategoryListViewInfos.size(); i < size; i++) {
			if (activityIdString2!=null && activityIdString2.equals(findCategoryListViewInfos.get(i).getActivityId())) {
				if ("add".equals(signNumTypeString) ){//增加
					findCategoryListViewInfos.get(i).setSignCount(findCategoryListViewInfos.get(i).getSignCount()+1);
					break;
				}else if ("del".equals(signNumTypeString) ){
					findCategoryListViewInfos.get(i).setSignCount(findCategoryListViewInfos.get(i).getSignCount()-1);
					break;
				}
			}
		}
		if (findCategoryListAdapter!=null) {
			findCategoryListAdapter.setHotFragmentListInfo(findCategoryListViewInfos);
			findCategoryListAdapter.notifyDataSetChanged();
		}
	}
	private void selectShow(String mess) {
		if ("chair".equals(mess)) {
//			UiHelper.ShowOneToast(this, "讲座");

		} else if ("groupbuying".equals(mess)) {
//			UiHelper.ShowOneToast(this, "团购");

		} else if ("invite".equals(mess)) {
//			UiHelper.ShowOneToast(this, "招聘");

		} else if ("meeting".equals(mess)) {
//			UiHelper.ShowOneToast(this, "聚会");

		} else if ("travel".equals(mess)) {
//			UiHelper.ShowOneToast(this, "旅行");

		} else if ("crowdfunding".equals(mess)) {
//			UiHelper.ShowOneToast(this, "众筹");

		} else if ("vote".equals(mess)) {
//			UiHelper.ShowOneToast(this, "投票");

		} else if ("all".equals(mess)) {
//			UiHelper.ShowOneToast(this, "全部");

		}

	}

	private void setTitle() {
		ImageView leftBtn = (ImageView) findViewById(R.id.head_left_iv);
		TextView centerTitle = (TextView) findViewById(R.id.head_center_title);
		TextView rightTv = (TextView) findViewById(R.id.head_right_tv);
		ImageButton rightBtn = (ImageButton) findViewById(R.id.head_right_btn);
		leftBtn.setVisibility(View.VISIBLE);
		centerTitle.setText(mess);
		// rightTv.setText("发布");
		rightTv.setVisibility(View.GONE);
		rightBtn.setVisibility(View.GONE);
		// rightBtn.setImageDrawable(getResources().getDrawable(R.drawable.find));
		leftBtn.setOnClickListener(this);
	}

	private void initView(String string) {
		listviewEmptyImageView = (LinearLayout) findViewById(R.id.listview_empty_iv);
		titleTextView = (TextView) findViewById(R.id.head_center_title);
		titleTextView.setText(string);
		findCategoryListViews = (com.newland.wstdd.common.view.CustomListViews) findViewById(R.id.category_listview);
		findCategoryListViews.setPullLoadEnable(true);
		findCategoryListViews.setXListViewListener(this);

		findCategoryListViews.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				try {
					tddActivity = acList.get(position - 1);

					singleActivitySearch(tddActivity.getActivityId());
				} catch (Exception e) {
					return;
				}
			}
		});
		refresh();
	}

	/**
	 * 单个活动查询   用来判断   活动报名人数   是否已经报名 等等
	 */
	SingleActivityRes singleActivityRes;
	private void singleActivitySearch(final String activityString) {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					SingleActivityReq reqInfo = new SingleActivityReq();
					reqInfo.setActivityId(activityString);
				
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<SingleActivityRes> ret = mgr.getSingleActivityInfo(reqInfo);// 泛型类，																
					Message message = new Message();
					message.what = SingleActivityDetailHandle.SINGLE_ACTIVITY;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						singleActivityRes = (SingleActivityRes) ret.getObj();
						message.obj = singleActivityRes;
					} else {
						message.obj = ret.getMsg();
					}
					handler.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Override
	protected void processMessage(Message msg) {

	}

	/**
	 * 8类数据中某一类
	 * 
	 */
	@Override
	public void refresh() {
		// findCategoryListViewInfos.clear();
		super.refreshDialog();
		if (type != null) {
			try {
				new Thread() {
					public void run() {
						// 保存更新时间
						String messUpdateTime = StringUtil.mess2Str(mess) + "UpdateTime";// chairUpdateTime
						AppContext.getAppContext().setMessUpDatetime(messUpdateTime);
						String value = StringUtil.getNowTime(StringUtil.SECOND_TIME);
						sharedPreferencesUtil.saveComment(messUpdateTime, value);

						// 需要发送一个request的对象进行请求
						FindCategoryReq reqInfo = new FindCategoryReq();
						reqInfo.setCity("全国");
						reqInfo.setActivityType(type);
						reqInfo.setPage(page_index + "");
						reqInfo.setProvince("全国");
						reqInfo.setRow(AppContext.getAppContext().pageRow);
						reqInfo.setSearchText("");
						BaseMessageMgr mgr = new HandleNetMessageMgr();
						RetMsg<FindCategoryRes> ret = mgr.getFindCategoeyInfo(reqInfo);// 泛型类，
						Message message = new Message();
						message.what = FindCategoryListHandle.FIND_LIST;// 设置死
						// 访问服务器成功 1 否则访问服务器失败
						if (ret.getCode() == 1) {
							if (page_index == 0) {
								acList.clear();
								findCategoryRes = (FindCategoryRes) ret.getObj();
								acList = findCategoryRes.getAcList();
							} else {
								findCategoryRes = (FindCategoryRes) ret.getObj();
								if (findCategoryRes != null && findCategoryRes.getAcList().size() > 0) {
									for (int i = 0, size = findCategoryRes.getAcList().size(); i < size; i++) {
										TddActivity tddActivity = findCategoryRes.getAcList().get(i);
										acList.add(tddActivity);
									}
								}
							}
							message.obj = findCategoryRes;
						} else {
							message.obj = ret.getMsg();
						}
						handler.sendMessage(message);
					};
				}.start();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	@Override
	public void onListViewRefresh() {

		page_index = 0;
		refresh();// 网络刷新
		onLoad();

	}

	@Override
	public void onListViewLoadMore() {
		page_index++;
		refresh();// 网络刷新
		onLoad();
	}

	private void onLoad() {
		findCategoryListViews.stopRefresh();
		findCategoryListViews.stopLoadMore();
		// String date = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分").format(new
		// Date());
		// listView.setRefreshTime(date);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_left_iv:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		try {
			switch (responseId) {
			case FindCategoryListHandle.FIND_LIST:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				findCategoryRes = (FindCategoryRes) obj;
				List<FindCategoryListViewInfo> list = new ArrayList<FindCategoryListViewInfo>();
				if (findCategoryRes != null) {
					if(!findCategoryRes.getAcList().isEmpty()) {
						listviewEmptyImageView.setVisibility(View.GONE);
					}else{
						listviewEmptyImageView.setVisibility(View.VISIBLE);
					}
					/**
					 * 获取到了数据
					 */
					// UiHelper.ShowOneToast(this, "获取数据成功");
					if (findCategoryRes.getAcList().size() > 0) {
						list.clear();
						for (int i = 0, size = findCategoryRes.getAcList().size(); i < size; i++) {
							FindCategoryListViewInfo findCategoryListViewInfo = new FindCategoryListViewInfo();
							findCategoryListViewInfo.setActivityId(findCategoryRes.getAcList().get(i).getActivityId());
							findCategoryListViewInfo.setActivityType(findCategoryRes.getAcList().get(i).getActivityType());
							findCategoryListViewInfo.setImage1(findCategoryRes.getAcList().get(i).getImage1());
							findCategoryListViewInfo.setActivityTitle(findCategoryRes.getAcList().get(i).getActivityTitle());
							findCategoryListViewInfo.setFriendActivityTime(findCategoryRes.getAcList().get(i).getFriendActivityTime());
							findCategoryListViewInfo.setActivityAddress(findCategoryRes.getAcList().get(i).getActivityAddress());
							findCategoryListViewInfo.setSignCount(findCategoryRes.getAcList().get(i).getSignCount());
							findCategoryListViewInfo.setNeedPay(findCategoryRes.getAcList().get(i).getNeedPay());
							findCategoryListViewInfo.setStatus(findCategoryRes.getAcList().get(i).getStatus());
							list.add(findCategoryListViewInfo);
						}
						if (page_index == 0) {
							findCategoryListViewInfos.clear();
							findCategoryListViewInfos.addAll(list);
							findCategoryListAdapter = new FindCategoryListAdapter(this, findCategoryListViewInfos);
							findCategoryListViews.setAdapter(findCategoryListAdapter);
							findCategoryListAdapter.notifyDataSetChanged();
						} else {
							findCategoryListViewInfos.addAll(list);
							findCategoryListAdapter.notifyDataSetChanged();
						}
						if (list.size() < Integer.valueOf(AppContext.getAppContext().pageRow)) {// 数据没必要分页时，隐藏“加载更多”按钮
							findCategoryListViews.setPullLoadEnable(false);
						} else {
							findCategoryListViews.setPullLoadEnable(true);
						}
					}else {
						findCategoryListViews.setPullLoadEnable(false);
					}
				}else {
					//如果没有数据
					if(findCategoryListViewInfos.isEmpty()) {
						listviewEmptyImageView.setVisibility(View.VISIBLE);
					}
				}
				break;
			case FindCategoryListHandle.SINGLE_ACTIVITY:
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				singleActivityRes=(SingleActivityRes) obj;
				if(singleActivityRes!=null){
					if (singleActivityRes.getIsLeader().equals("true")) {//团大
						Intent intent = new Intent(this, ManagerPageActivity.class);// 跳转到管理
						Bundle bundle = new Bundle();
						singleActivityRes.getTddActivity().setSignRole(1);
						bundle.putSerializable("singleActivityRes", singleActivityRes);
						intent.putExtras(bundle);
						startActivity(intent);
					}else if (singleActivityRes.getSignRole() == 2) {//团秘
						Intent intent = new Intent(this, ManagerPageActivity.class);// 跳转到管理
						Bundle bundle = new Bundle();
						singleActivityRes.getTddActivity().setSignRole(2);
						bundle.putSerializable("singleActivityRes", singleActivityRes);
						intent.putExtras(bundle);
						startActivity(intent);
					}else if (singleActivityRes.getSignRole() == 9) {//团员
						Intent intent = new Intent(this, FindChairDetailActivity.class);// 跳转到详情
						Bundle bundle = new Bundle();
						singleActivityRes.getTddActivity().setSignRole(9);
						bundle.putSerializable("singleActivityRes", singleActivityRes);
						intent.putExtras(bundle);
						startActivity(intent);
					}else if (singleActivityRes.getSignRole() == 0) {//没参加
						Intent intent = new Intent(this, FindChairDetailActivity.class);// 跳转到详情
						Bundle bundle = new Bundle();
						singleActivityRes.getTddActivity().setSignRole(0);
						bundle.putSerializable("singleActivityRes", singleActivityRes);
						intent.putExtras(bundle);
						startActivity(intent);
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

	@Override
	public void OnFailResultListener(String mess) {if (progressDialog != null) {progressDialog.setContinueDialog(false);}
		if(findCategoryListViewInfos.isEmpty()) {
			listviewEmptyImageView.setVisibility(View.VISIBLE);
			UiHelper.ShowOneToast(this, mess);
		}
	}

	@Override
	public void initView() {

	}
	
	@Override
	protected void onDestroy() {
		 /**收集异常日志*/
    	LogManager.getManager(getApplicationContext()).unregisterActivity(this);
    	 /**收集异常日志*/
    	
		unregisterReceiver(broadcastReceiver);
		unregisterReceiver(signedNumChangeReceiver);
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return false;
	}
	
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			activityIdString = intent.getStringExtra("activityId");
			statusString = intent.getStringExtra("status");
		}
	};
	
	// 报名人数变化的广播
	private String activityIdString2;// 活动id
	private String signNumTypeString;// 报名人数增加还是减少
	BroadcastReceiver signedNumChangeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			activityIdString2 = intent.getStringExtra("activityId");
			signNumTypeString = intent.getStringExtra("signNumType");
		}
	};
}
