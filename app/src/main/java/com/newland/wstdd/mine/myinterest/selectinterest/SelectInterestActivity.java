package com.newland.wstdd.mine.myinterest.selectinterest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.listview.DefineListViews;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.myinterest.beanrequest.InterestFirstItemInfo;
import com.newland.wstdd.mine.myinterest.beanrequest.InterestSecondItemInfo;
import com.newland.wstdd.mine.myinterest.beanrequest.MyInterestTagsReq;
import com.newland.wstdd.mine.myinterest.beanrequest.TddUserTagQuery;
import com.newland.wstdd.mine.myinterest.beanresponse.MyInterestTagsRes;
import com.newland.wstdd.mine.myinterest.handle.SelectInterestHandle;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;

/**
 * 我的兴趣 1.编辑状态----有打X的状态就是点击可以删除 2.完成状态----去除打X 3.可以添加更多
 * 
 * @author Administrator
 * 
 */
public class SelectInterestActivity extends BaseFragmentActivity implements OnPostListenerInterface {
	private static final String TAG = "SelectInterestActivity";//收集异常日志tag
	private ImageView closeImageView;// 点击关闭页面
	private boolean isShowEdit;// 是否显示删除的界面样式
	boolean isCanDeltet = false;// 是否是可以用删除的状态 true表示可以删除plu键
	private List<InterestFirstItemInfo> firstItemInfos = new ArrayList<InterestFirstItemInfo>();// 通用
	private List<TddUserTagQuery> myTags = new ArrayList<TddUserTagQuery>();// 选择的兴趣标签
																			// 这个是要返回给上一页的数据，通过StartActivity的方式
	private List<TddUserTagQuery> allTags = new ArrayList<TddUserTagQuery>();// 所有感兴趣的标签
	private List<String> tagNamesList = new ArrayList<String>();
	// 所有的
	private SelectInterestFirstAdapter allAdapter;// 兴趣界面的item
	private DefineListViews allGridView = null; // 取得组件

	// private Handler handler = new MyInterestHandle(this);
	// 控件
	private Vibrator mVibrator;
	private Button finishButton;// 添加完成
	// 点击之后获取的两个值 一个是运动的名称 是否显示删除 在列表中的位置
	String interestName = null;
	String isDeleteInterest = null;
	IntentFilter filter;
	Intent intent;
	private SelectInterestHandle handler = new SelectInterestHandle(this);
	// 服务器返回信息
	MyInterestTagsRes myInterestTagsRes;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		setContentView(R.layout.activity_select_interest);
		
		/**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		// 适配器
		allAdapter = new SelectInterestFirstAdapter(this, firstItemInfos);

		initView();// 初始化控件
		// test();
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		// 需要进行判空操作
		try {
			myTags = (List<TddUserTagQuery>) bundle.getSerializable("myTags");
			allTags = (List<TddUserTagQuery>) bundle.getSerializable("allTags");
		} catch (Exception e) {
			// TODO: handle exception
		}

		initType();// 初始化类型

	}

	private void initType() {
		// TODO Auto-generated method stub
		/**
		 * 首先需要把allTags中所有的标签类型拿出来
		 */
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < allTags.size(); i++) {
			list.add(allTags.get(i).getType());
		}
		list = StringUtil.getNewList(list);// 去掉重复的标签类型名称
		for (int i = 0; i < list.size(); i++) {
			InterestFirstItemInfo tempFirstItemInfo = new InterestFirstItemInfo();
			tempFirstItemInfo.setTypeName(list.get(i));
			firstItemInfos.add(tempFirstItemInfo);
		}
		// 先遍历一级列表，将对应的二级列表的值，放入对应的类型标签中
		for (int i = 0; i < firstItemInfos.size(); i++) {
			List<InterestSecondItemInfo> infos = new ArrayList<InterestSecondItemInfo>();
			for (int j = 0; j < allTags.size(); j++) {
				if (firstItemInfos.get(i).getTypeName().equals(allTags.get(j).getType())) {
					InterestSecondItemInfo info = new InterestSecondItemInfo();
					info.setInterestName(allTags.get(j).getTagName());
					for (int k = 0; k < myTags.size(); k++) {
						if (allTags.get(j).getTagName().equals(myTags.get(k).getTagName())) {
							info.setSelect(true);
							break;
						} else {
							info.setSelect(false);
						}
					}

					infos.add(info);// 添加到二级列表
				}
			}
			firstItemInfos.get(i).setItemsList(infos);

		}

		allAdapter.setFirstItemInfos(firstItemInfos);
		allAdapter.notifyDataSetChanged();

	}

	// private void test() {
	// // TODO Auto-generated method stub
	// for (int i = 0; i < 51; i++) {
	// InterestSecondItemInfo info=new InterestSecondItemInfo();
	// info.setInterestName("运动将");
	// info.setSelect(false);
	// firstItemInfos.add(info);
	// }
	//
	//
	// LayoutParams params = new LayoutParams(
	// LayoutParams.MATCH_PARENT,
	// LayoutParams.MATCH_PARENT);
	// allGridView.setLayoutParams(params);
	// allGridView.setStretchMode(GridView.NO_STRETCH);
	// allGridView.setColumnWidth((int)(AppContext.getAppContext().getScreenWidth()/4.5));
	// allGridView.setHorizontalSpacing(AppContext.getAppContext().getScreenWidth()/50);
	// allGridView.setVerticalSpacing(AppContext.getAppContext().getScreenWidth()/500);
	// allAdapter.setListDate(firstItemInfos);
	// allAdapter.notifyDataSetChanged();
	//
	// }

	// 初始化控件
	public void initView() {
		// TODO Auto-generated method stub
		filter = new IntentFilter("ShopFragment");
		registerReceiver(broadcastReceiver, filter);
		mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		// 设置数据
		this.allGridView = (com.newland.wstdd.common.listview.DefineListViews) findViewById(R.id.all_interest_interest);
		this.allGridView.setAdapter(allAdapter);
		closeImageView = (ImageView) findViewById(R.id.activity_select_interest_close);
		closeImageView.setOnClickListener(this);
		finishButton = (Button) findViewById(R.id.select_interest_finish_bt);
		finishButton.setOnClickListener(this);

	}

	// private void changeEditState(Boolean isEditState) {
	//
	// // TODO Auto-generated method stub
	// // 设置可以编辑的状态
	// if (isEditState) {// 刪除
	//
	// if (!isShowEdit) {
	// isShowEdit = true;
	// allGridView.setDrag(true);
	// }
	// } else {
	//
	// if (isShowEdit) {
	//
	// isShowEdit = false;
	// allGridView.setDrag(false);
	// } else {
	// interestName = "";
	//
	// }
	// }
	// allAdapter.setShowDelete(isShowEdit);
	// }

	/**
	 * 接收到广播之后 在这里要做三件事情 1.更新数据到界面上去 2.更新pluinfoList的值 3.上传到服务器
	 */
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	protected void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	// 监听事件服务器
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.select_interest_finish_bt:
			myTags.clear();
			if (firstItemInfos != null && !firstItemInfos.isEmpty()) {
				for (int i = 0; i < firstItemInfos.size(); i++) {
					for (int j = 0; j < firstItemInfos.get(i).getItemsList().size(); j++) {
						if (firstItemInfos.get(i).getItemsList().get(j).isSelect()) {
							TddUserTagQuery info = new TddUserTagQuery();
							info.setType(firstItemInfos.get(i).getTypeName());
							for (int k = 0; k < allTags.size(); k++) {
								if (firstItemInfos.get(i).getItemsList().get(j).getInterestName().equals(allTags.get(k).getTagName())) {
									info.setTag(allTags.get(k).getTag());
									info.setUserId(allTags.get(k).getUserId());
									info.setTagName(allTags.get(k).getTagName());
								}
							}

							myTags.add(info);
						}
					}

				}
			}

			refresh();

			break;
		case R.id.activity_select_interest_close:
			finish();
			break;
		default:
			break;
		}
	}

	// 提交的所选择的内容的服务器
	@Override
	public void refresh() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {

					// 需要发送一个request的对象进行请求
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					MyInterestTagsReq myInterestTagsReq = new MyInterestTagsReq();
					RetMsg<MyInterestTagsRes> ret = null;
					if (myTags != null && !myTags.isEmpty()) {
						myInterestTagsReq.setMyTags(myTags);
						ret = mgr.getMyInterestUpdateInfo(myInterestTagsReq);// 泛型类，
					} else {
						ret = mgr.getMyInterestUpdateInfo(myInterestTagsReq);// 泛型类，
					}
					Message message = new Message();
					message.what = SelectInterestHandle.SELECT_INTEREST;// 设置死
					// 访问服务器成功 1否则访问服务器失败
					if (ret.getCode() == 1) {
						myInterestTagsRes = (MyInterestTagsRes) ret.getObj();
						message.obj = myInterestTagsRes;
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
	public void OnHandleResultListener(Object obj, int responseId) {
		// TODO Auto-generated method stub
		switch (responseId) {
		case SelectInterestHandle.SELECT_INTEREST:
			if (progressDialog != null) {
				progressDialog.setContinueDialog(false);
			}
			myInterestTagsRes = (MyInterestTagsRes) obj;
			if (myInterestTagsRes != null) {
				// 发送广播 更新界面
				// Intent intent0 =getIntent();//切记
				// 这里的Action参数与IntentFilter添加的Action要一样才可以
				// intent0.setAction("Refresh_MineFragment");
				// Bundle bundle0=new Bundle();
				// bundle0.putSerializable("TAG",(Serializable) myTags);
				// intent0.putExtra("budle",bundle0);
				// sendBroadcast(intent0);//发送广播了

				// 根据返回的结果进行相应的处理
				// 一下内容换成
				Bundle bundle = new Bundle();
				bundle = intent.getExtras();
				bundle.putSerializable("result", (Serializable) myTags);
				intent.putExtras(bundle);
				// 设置返回数据
				SelectInterestActivity.this.setResult(01, intent);
				// 设置全局变量 appcontext
				for (int i = 0; i < myTags.size(); i++) {
					tagNamesList.add(myTags.get(i).getTagName());
				}

				AppContext.getAppContext().setTags(tagNamesList);
				StringUtil.appContextTagsListToString(tagNamesList);// 以字符串的形式
				// 关闭Activity
				SelectInterestActivity.this.finish();

			}
			break;

		default:
			break;
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
	protected void onDestroy() {
		/** 收集异常日志 */
		LogManager.getManager(getApplicationContext()).unregisterActivity(this);
		/** 收集异常日志 */
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}
}
