package com.newland.wstdd.find.categorylist.registrationedit.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.bean.TddActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.EditTextUtil;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.widget.PengTextView;
import com.newland.wstdd.find.categorylist.registrationedit.beaninfo.AdultInfo;
import com.newland.wstdd.find.categorylist.registrationedit.beaninfo.MainSignAttr;
import com.newland.wstdd.find.categorylist.registrationedit.beanrequest.CancelRegistrationReq;
import com.newland.wstdd.find.categorylist.registrationedit.beanrequest.SubmitRegistrationReq;
import com.newland.wstdd.find.categorylist.registrationedit.beanresponse.CancelRegistrationRes;
import com.newland.wstdd.find.categorylist.registrationedit.beanresponse.EditRegistrationRes;
import com.newland.wstdd.find.categorylist.registrationedit.beanresponse.SubmitRegistrationRes;
import com.newland.wstdd.find.categorylist.registrationedit.handle.CancelRegistrationHandle;
import com.newland.wstdd.find.categorylist.registrationedit.handle.SubmitRegistrationHandle;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 报名信息编辑
 * 
 * @author Administrator
 * 
 */
public class RegistrationSubmitActivity extends BaseFragmentActivity implements OnPostListenerInterface, IWXAPIEventHandler {
	private static final String TAG = "RegistrationSubmitActivity";//收集异常日志tag
	Intent intent;
	private String mainSignAttrs;// 必填信息项目 是一个以逗号隔开的字符串
	TddActivity tddActivity;// 得到前面一个界面传递过来的活动对象 是为了获取必填项
	// 随行人员 ListView相关信息
	SxRegistrationSubmitListViews sxListViews;
	SxRegistrationSubmitAdapter sxAdapter;
	List<SxRegistrationSubmitAdapterData> sxAdapterDatas = new ArrayList<SxRegistrationSubmitAdapterData>();
	// 本人信息
	private List<MainSignAttr> mineAdapterDatas = new ArrayList<MainSignAttr>();
	RegistrationSubmitAdapter mineEditAdapter;// 我自己的报名信息的适配器
	SxRegistrationSubmitListViews mineEditListViews;
	// 添加随行人员
	TextView addTextView;// 添加随行人员 带有监听事件
	TextView registrationActivityIcon, registrationActivityTitle;// 活动报名中的活动类型图标
																	// 跟 活动标题
	// 暂时的测试 服务器返回的信息
	EditRegistrationRes submitRegistrationRes;
	SubmitRegistrationHandle handler = new SubmitRegistrationHandle(this);

	CancelRegistrationRes cancelRegistrationRes;
	CancelRegistrationHandle handlerCancel = new CancelRegistrationHandle(this);
	/**
	 * 分享相关的
	 */
	private PopupWindow popupWindow;// 分享窗口
	// 微信
	private static final String appid = "wx1b84c30d9f380c89";// 微信的appid
	private IWXAPI wxApi;// 微信的API
	// QQ
	private Tencent mTencent;
	private static final String APP_ID = "1104957952";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		setContentView(R.layout.activity_registration_submit);
		
		 /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		tddActivity = (TddActivity) bundle.getSerializable("tddActivity");
		initTitle();// 初始化标题
		initView();// 初始化控件
		initMustSelect();// 获取必填项目
		/**
		 * 分享
		 */
		// QQ
		final Context ctxContext = this.getApplicationContext();
		mTencent = Tencent.createInstance(APP_ID, ctxContext);
		mHandler = new Handler();
		// weixin
		wxApi = WXAPIFactory.createWXAPI(this, appid);
		wxApi.registerApp(appid);
	}
	@Override
    protected void onDestroy() {
    	 /**收集异常日志*/
    	LogManager.getManager(getApplicationContext()).unregisterActivity(this);
    	 /**收集异常日志*/
    	super.onDestroy();
    }
	// 显示出必填的项目
	private void initMustSelect() {
		// TODO Auto-generated method stub
		if (tddActivity != null) {
			mainSignAttrs = tddActivity.getSignAttr();
			// 如果是以逗号结尾的，需要进行相应的处理
			if (mainSignAttrs.endsWith(",")) {
				mainSignAttrs = mainSignAttrs.substring(0, mainSignAttrs.length() - 1);
			}
			if (mainSignAttrs != null && !"".equals(mainSignAttrs)) {
				String[] strs = mainSignAttrs.split(",");
				// 把String数组输入list
				for (String substr : strs) {
					MainSignAttr mainSignAttr = new MainSignAttr();
					mainSignAttr.setName(substr);
					if ("姓名".equals(substr)) {
						mainSignAttr.setValue(tddActivity.getUserName());
					} else if ("手机".equals(substr)) {
						mainSignAttr.setValue(tddActivity.getUserMobilePhone());
					} else {
						mainSignAttr.setValue(null);
					}
					// mainSignAttr.setValue(null);
					mineAdapterDatas.add(mainSignAttr);
				}
				mineEditAdapter.setRegistrationData(mineAdapterDatas);
				mineEditListViews.setAdapter(mineEditAdapter);
				mineEditAdapter.notifyDataSetChanged();
			} else {
				MainSignAttr mainSignAttr = new MainSignAttr();
				mainSignAttr.setName("姓名");
				mainSignAttr.setValue(tddActivity.getUserName());

				MainSignAttr mainSignAttr1 = new MainSignAttr();
				mainSignAttr1.setName("手机");
				mainSignAttr1.setValue(tddActivity.getUserMobilePhone());

				mineAdapterDatas.add(mainSignAttr);
				mineAdapterDatas.add(mainSignAttr1);
				mineEditAdapter.setRegistrationData(mineAdapterDatas);
				mineEditListViews.setAdapter(mineEditAdapter);
				mineEditAdapter.notifyDataSetChanged();
			}
		}

	}

	/**
	 * 设置标题
	 * 
	 */
	private void initTitle() {
		ImageView leftBtn = (ImageView) findViewById(R.id.head_left_iv);
		TextView centerTitle = (TextView) findViewById(R.id.head_center_title);
		centerTitle.setText("编辑报名信息");
		TextView rightTv = (TextView) findViewById(R.id.head_right_tv);
		ImageButton rightBtn = (ImageButton) findViewById(R.id.head_right_btn);
		leftBtn.setVisibility(View.VISIBLE);
		rightTv.setVisibility(View.VISIBLE);
		rightTv.setTextColor(getResources().getColor(R.color.text_red));
		rightTv.setText("提交");
		rightBtn.setVisibility(View.GONE);
		rightBtn.setImageDrawable(getResources().getDrawable(R.drawable.find));
		leftBtn.setOnClickListener(this);
		rightTv.setOnClickListener(this);
	}


	public void initView() {
		// TODO Auto-generated method stub
		registrationActivityIcon = (TextView) findViewById(R.id.activity_mine_personalcenter_icon_tv);
		registrationActivityTitle = (TextView) findViewById(R.id.activity_mine_personalcenter_title_tv);
		registrationActivityIcon.setText(StringUtil.intType2Str(tddActivity.getActivityType()));
		registrationActivityTitle.setText(tddActivity.getActivityTitle());
		sxListViews = (SxRegistrationSubmitListViews) findViewById(R.id.registration_sx_listview);
		mineEditListViews = (SxRegistrationSubmitListViews) findViewById(R.id.registration_listview);
		sxAdapter = new SxRegistrationSubmitAdapter(this, sxAdapterDatas);
		sxListViews.setAdapter(sxAdapter);
		mineEditAdapter = new RegistrationSubmitAdapter(this, mineAdapterDatas);
		addTextView = (TextView) findViewById(R.id.registration_add_people);
		addTextView.setOnClickListener(this);
	}

	// 监听事件
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.registration_add_people:
			// 进行判断，只有添加完最后一个才可以进行下一个的判断
			if (sxAdapterDatas.size() > 0) {
				if (sxAdapterDatas.get(sxAdapterDatas.size() - 1).getName() == null || sxAdapterDatas.get(sxAdapterDatas.size() - 1).getPhone() == null
						|| "".equals(sxAdapterDatas.get(sxAdapterDatas.size() - 1).getName()) || "".equals(sxAdapterDatas.get(sxAdapterDatas.size() - 1).getPhone())) {
					UiHelper.ShowOneToast(this, "随行人员信息未完成，无法添加");
				} else {

					SxRegistrationSubmitAdapterData data = new SxRegistrationSubmitAdapterData();
					data.setName(null);
					data.setPhone(null);
					List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
					List<Map<String, String>> tempInputMaplist = new ArrayList<Map<String, String>>();
					List<Map<String, String>> tempLastMaplist = new ArrayList<Map<String, String>>();
					if (tddActivity != null) {
						mainSignAttrs = tddActivity.getSignAttr();
						// 如果是以逗号结尾的，需要进行相应的处理
						if (mainSignAttrs.endsWith(",")) {
							mainSignAttrs = mainSignAttrs.substring(0, mainSignAttrs.length() - 1);
						}
						if (mainSignAttrs != null && !"".equals(mainSignAttrs)) {
							String[] strs = mainSignAttrs.split(",");
							// 把String数组输入list
							for (String substr : strs) {
								Map<String, String> map = new HashMap<String, String>();
								map.put(substr, null);
								maplist.add(map);
							}
							for (String substr : strs) {
								Map<String, String> map = new HashMap<String, String>();
								map.put(substr, null);
								tempInputMaplist.add(map);
							}
							for (String substr : strs) {
								Map<String, String> map = new HashMap<String, String>();
								map.put(substr, null);
								tempLastMaplist.add(map);
							}
						} else {
							Map<String, String> map0 = new HashMap<String, String>();
							map0.put("姓名", null);
							Map<String, String> map1 = new HashMap<String, String>();
							map1.put("手机", null);
							maplist.add(map0);
							maplist.add(map1);

							Map<String, String> map2 = new HashMap<String, String>();
							map2.put("姓名", null);
							Map<String, String> map3 = new HashMap<String, String>();
							map3.put("手机", null);
							tempInputMaplist.add(map2);
							tempInputMaplist.add(map3);

							Map<String, String> map4 = new HashMap<String, String>();
							map4.put("姓名", null);
							Map<String, String> map5 = new HashMap<String, String>();
							map5.put("手机", null);
							tempLastMaplist.add(map4);
							tempLastMaplist.add(map5);

						}

					} else {
						UiHelper.ShowOneToast(RegistrationSubmitActivity.this, "活动信息为空，无法添加随行人员");
					}
					data.setMap(maplist);
					data.setInputTempList(tempInputMaplist);
					data.setLastTempList(tempLastMaplist);
					data.setShowRl1(false);
					data.setShowRl2(true);
					data.setShowListView(true);
					data.setInParent(sxAdapterDatas.size());
					sxAdapterDatas.add(data);
					sxAdapter.setRegistrationData(sxAdapterDatas);
					sxAdapter.notifyDataSetChanged();
				}
			} else {
				// 首次添加随行人员的时候，列表是为0开始的
				SxRegistrationSubmitAdapterData data = new SxRegistrationSubmitAdapterData();
				data.setName(null);
				data.setPhone(null);
				List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
				List<Map<String, String>> tempInputMaplist = new ArrayList<Map<String, String>>();
				List<Map<String, String>> tempLastMaplist = new ArrayList<Map<String, String>>();
				if (tddActivity != null) {
					mainSignAttrs = tddActivity.getSignAttr();
					// 如果是以逗号结尾的，需要进行相应的处理
					if (mainSignAttrs.endsWith(",")) {
						mainSignAttrs = mainSignAttrs.substring(0, mainSignAttrs.length() - 1);
					}
					if (mainSignAttrs != null && !"".equals(mainSignAttrs)) {
						String[] strs = mainSignAttrs.split(",");
						// 把String数组输入list
						for (String substr : strs) {
							Map<String, String> map = new HashMap<String, String>();
							map.put(substr, null);
							maplist.add(map);
						}
						for (String substr : strs) {
							Map<String, String> map = new HashMap<String, String>();
							map.put(substr, null);
							tempInputMaplist.add(map);
						}
						for (String substr : strs) {
							Map<String, String> map = new HashMap<String, String>();
							map.put(substr, null);
							tempLastMaplist.add(map);
						}
					} else {
						Map<String, String> map0 = new HashMap<String, String>();
						map0.put("姓名", null);
						Map<String, String> map1 = new HashMap<String, String>();
						map1.put("手机", null);
						maplist.add(map0);
						maplist.add(map1);

						Map<String, String> map2 = new HashMap<String, String>();
						map2.put("姓名", null);
						Map<String, String> map3 = new HashMap<String, String>();
						map3.put("手机", null);
						tempInputMaplist.add(map2);
						tempInputMaplist.add(map3);

						Map<String, String> map4 = new HashMap<String, String>();
						map4.put("姓名", null);
						Map<String, String> map5 = new HashMap<String, String>();
						map5.put("手机", null);
						tempLastMaplist.add(map4);
						tempLastMaplist.add(map5);

					}

				} else {
					UiHelper.ShowOneToast(RegistrationSubmitActivity.this, "活动信息为空，无法添加随行人员");
				}
				data.setMap(maplist);
				data.setInputTempList(tempInputMaplist);
				data.setLastTempList(tempLastMaplist);
				data.setShowRl1(false);
				data.setShowRl2(true);
				data.setShowListView(true);
				data.setInParent(0);// 首次添加随行人员的时候，
				sxAdapterDatas.add(data);
				sxAdapter.setRegistrationData(sxAdapterDatas);
				sxAdapter.notifyDataSetChanged();

			}
			break;
		case R.id.head_right_tv:
			// 提交验证之前需要判断
			boolean isEmpty = false;
			mineAdapterDatas = mineEditAdapter.getRegistrationData();
			for (int i = 0; i < mineAdapterDatas.size(); i++) {
				if ("".equals(mineAdapterDatas.get(i).getValue())) {
					UiHelper.ShowOneToast(this, "不能有空信息");
					isEmpty = true;
					break;
				} else if ("手机".equals(mineAdapterDatas.get(i).getName())) {
					if (!EditTextUtil.checkMobileNumber(mineAdapterDatas.get(i).getValue())) {
						UiHelper.ShowOneToast(this, "主报名人手机输入有误");
						isEmpty = true;
						break;
					}
				} else if ("邮箱".equals(mineAdapterDatas.get(i).getName())) {
					if (!EditTextUtil.checkEmail(mineAdapterDatas.get(i).getValue())) {
						UiHelper.ShowOneToast(this, "主报名人邮箱输入有误");
						isEmpty = true;
						break;
					}
				} else if ("身份证".equals(mineAdapterDatas.get(i).getName())) {
					if (!EditTextUtil.checkChinaIDCard(mineAdapterDatas.get(i).getValue())) {
						UiHelper.ShowOneToast(this, "主报名人身份证输入有误");
						isEmpty = true;
						break;
					}
				} else if ("性别".equals(mineAdapterDatas.get(i).getName())) {
					if (!"男".equals(mineAdapterDatas.get(i).getValue()) && !"女".equals(mineAdapterDatas.get(i).getValue())) {
						UiHelper.ShowOneToast(this, "主报名人性别只能为男或女");
						isEmpty = true;
						break;
					}
				} else {
					continue;
				}

			}
			if (!isEmpty) {
				refreshSumit();
//				submitCheck();
			}

			break;
		default:
			break;
		}

	}

	@Override
	protected void processMessage(Message msg) {

	}

	@Override
	public void refresh() {

	}

	// 服务器返回的相关的处理
	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		try {
			if (progressDialog != null) {
				progressDialog.setContinueDialog(false);
			}
			switch (responseId) {
			case SubmitRegistrationHandle.SUBMIT_REGISTRATION:
				submitRegistrationRes = (EditRegistrationRes) obj;
				if (submitRegistrationRes != null) {
					String mess = null;
					mess = submitRegistrationRes.getGetResMess();
					/**
					 * 返回成功之后进行分享
					 */
					// 报名成功之后通知更改装填
					Intent intent1 = new Intent();
					intent1.setAction("ManagerPageActivityRefresh_Cancel_Regist");
					intent1.putExtra("SignState", "Sign");
					sendBroadcast(intent1);
					if (null != AppContext.getAppContext())
						AppContext.getAppContext().setMySignAcNum(Integer.valueOf(AppContext.getAppContext().getMySignAcNum()) + 1 + "");
					getPopWindow();
					
					sendSignedNumAddBroadCast();
				}
				break;

			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 发送增加报名人数广播
	 */
	private void sendSignedNumAddBroadCast() {
		Intent intent = new Intent();
		intent.setAction("SignedNumChange");
		intent.putExtra("activityId", tddActivity.getActivityId());
		intent.putExtra("signNumType", "add");
		sendBroadcast(intent);
	}

	@Override
	public void OnFailResultListener(String mess) {
		if (progressDialog != null) {
			progressDialog.setContinueDialog(false);
			UiHelper.ShowOneToast(this, mess);
		}
	}

	/**
	 * 请求服务器信息 提交报名
	 */
	private void refreshSumit() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					SubmitRegistrationReq reqInfo = new SubmitRegistrationReq();
					// 主报名信息
					mineAdapterDatas = mineEditAdapter.getRegistrationData();
					reqInfo.setMainSignAttr(mineAdapterDatas);
					// 随行人员报名信息
					List<AdultInfo> adultInfos = new ArrayList<AdultInfo>();
					for (int i = 0; i < sxAdapterDatas.size(); i++) {
						// 添加到adultInfos
						AdultInfo adultInfo = new AdultInfo();
						adultInfo.setAdultPersonType(2);
						adultInfo.setAdultUserName(sxAdapter.getRegistrationData().get(i).getName());
						List<MainSignAttr> mainSignAttrs = new ArrayList<MainSignAttr>();
						List<Map<String, String>> values = sxAdapter.getRegistrationData().get(i).getMap();
						for (Map<String, String> map : values) {
							for (Entry<String, String> entry : map.entrySet()) {
								Object key = entry.getKey();
								Object val = entry.getValue();
								MainSignAttr mainSignAttr = new MainSignAttr();
								if (!"".equals((String) key) && key != null) {
									mainSignAttr.setName((String) key);
								}
								if (!"".equals((String) val) && val != null) {
									mainSignAttr.setValue((String) val);
								}
								// 只有有数据的才加进去，否则不添加 不提交
								if (!"".equals(mainSignAttr.getName()) && !"".equals(mainSignAttr.getValue()) && mainSignAttr.getValue() != null && mainSignAttr.getName() != null) {
									mainSignAttrs.add(mainSignAttr);
								} else {
									break;
								}

							}
						}
						if (mainSignAttrs.size() > 0) {
							adultInfo.setAdultSignAttr(mainSignAttrs);
							adultInfos.add(adultInfo);
						}

					}
					reqInfo.setAdultInfos(adultInfos);
					// 人员类型
					reqInfo.setPersonType(1);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<SubmitRegistrationRes> ret = mgr.getSubmitRegistrationInfo(reqInfo, tddActivity.getActivityId());// 泛型类，
					Message message = new Message();
					message.what = SubmitRegistrationHandle.SUBMIT_REGISTRATION;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						submitRegistrationRes = new EditRegistrationRes();
						submitRegistrationRes.setGetResMess(StringUtil.noNull(ret.getMsg()));
						message.obj = submitRegistrationRes;
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

	/**
	 * 取消报名
	 */
	private void refreshCancelReg() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					CancelRegistrationReq reqInfo = new CancelRegistrationReq();
					reqInfo.setActivityId("activityID");
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<CancelRegistrationRes> ret = mgr.getCancelRegistrationInfo(reqInfo);// 泛型类，
					Message message = new Message();
					message.what = CancelRegistrationHandle.CANCEL_REGISTRATION;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						cancelRegistrationRes = new CancelRegistrationRes();
						cancelRegistrationRes.setGetResMess(StringUtil.noNull(ret.getMsg()));
						message.obj = cancelRegistrationRes;
					} else {
						message.obj = ret.getMsg();
					}
					handlerCancel.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 分享相关的操作
	 */

	private void getPopWindow() {
		if (popupWindow != null) {
			popupWindow.dismiss();
			return;
		} else {
			initPopupWindow();
		}
	}

	/**
	 * 分享窗口初始化
	 */
	private void initPopupWindow() {
		View popupWindow_view = this.getLayoutInflater().inflate(R.layout.popwindow_registration_shares, null, false);
		TextView public_type_tv = (TextView) popupWindow_view.findViewById(R.id.public_type_tv);// 发布类型
		TextView public_title_tv = (TextView) popupWindow_view.findViewById(R.id.public_title_tv);// 发布主题
		public_type_tv.setText(StringUtil.intType2Str(tddActivity.getActivityType()));
		public_title_tv.setText(registrationActivityTitle.getText().toString());
		popupWindow = new PopupWindow(popupWindow_view, AppContext.getAppContext().getScreenWidth() * 4 / 5, LayoutParams.WRAP_CONTENT, true);
		ImageView cancelImageView = (ImageView) popupWindow_view.findViewById(R.id.originate_chair_popwindow_delete);
		cancelImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				Intent intent = new Intent();
				intent.setAction("Refresh_FindChairDetailActivity");
				intent.putExtra("registration_state", "已报名");
				sendBroadcast(intent);
				finish();

			}
		});
		popupWindow.setOutsideTouchable(false);
		PengTextView weixinFriend, weixinZone, qqFriend;// 弹出窗口三个控件
		weixinFriend = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_weixin);
		weixinZone = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_weixin_zone);
		qqFriend = (PengTextView) popupWindow_view.findViewById(R.id.activity_find_chairdetail_pop_qq);
		// 微信好友分享
		weixinFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				friend(v);
			}
		});

		// 微信朋友圈分享
		weixinZone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				friendline(v);
			}
		});

		// qq好友分享
		qqFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickShareToQQ();
			}
		});

		// 设置透明度
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.3f;
		getWindow().setAttributes(lp);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
		// 显示窗口 位置
		popupWindow.showAtLocation(this.findViewById(R.id.layout), Gravity.CENTER, 0, 0);//
		popupWindow.setOutsideTouchable(false);

	}

	/**
	 * 分享相关的方法
	 * 
	 * @param v
	 */
	public void friend(View v) {
		share(0);
	}

	public void friendline(View v) {
		share(1);
	}

	private void share(int flag) {
		downloadWeiXinImg(flag);

	}

	// 微信分享需要 先去下载封面的图片，然后才会分享出去
	private void downloadWeiXinImg(final int flag) {
		// TODO Auto-generated method stub
		if (tddActivity.getShareContent() != null && tddActivity.getShareImg() != null && tddActivity.getShareUrl() != null) {

			ImageLoader.getInstance().loadImage(tddActivity.getShareImg(), new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					// 下载失败
					WXWebpageObject webpage = new WXWebpageObject();
					webpage.webpageUrl = tddActivity.getShareUrl();
					WXMediaMessage msg = new WXMediaMessage(webpage);
					msg.title = tddActivity.getActivityTitle();
					msg.description = tddActivity.getActivityDescription();
					// 根据ImgUrl下载下来一张图片，弄出bitmap格式
					// 这里替换一张自己工程里的图片资源
					Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
					msg.setThumbImage(thumb);
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("webpage");
					req.message = msg;
					req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
					boolean fla = wxApi.sendReq(req);
					System.out.println("fla=" + fla);
				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
					// TODO Auto-generated method stub
					// 表示下载成功了
					WXWebpageObject webpage = new WXWebpageObject();
					webpage.webpageUrl = tddActivity.getShareUrl();
					WXMediaMessage msg = new WXMediaMessage(webpage);
					msg.title = tddActivity.getActivityTitle();
					msg.description = tddActivity.getActivityDescription();
					// 根据ImgUrl下载下来一张图片，弄出bitmap格式
					// 这里替换一张自己工程里的图片资源
					Bitmap thumb = bitmap;
					msg.setThumbImage(thumb);
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("webpage");
					req.message = msg;
					req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
					boolean fla = wxApi.sendReq(req);
					System.out.println("fla=" + fla);
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub

				}
			});
		} else {
			UiHelper.ShowOneToast(RegistrationSubmitActivity.this, "第三方分享的内容不能为空!");
			finish();
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub
		finish();
	}

	private void onClickShareToQQ() {
		Bundle b = getShareBundle();
		if (b != null) {
			shareParams = b;
			Thread thread = new Thread(shareThread);
			thread.start();
		}
	}

	private Bundle getShareBundle() {
		Bundle bundle = new Bundle();
		bundle.putString("title", tddActivity.getActivityTitle());
		bundle.putString("imageUrl", tddActivity.getShareImg());
		bundle.putString("targetUrl", tddActivity.getShareUrl());
		bundle.putString("summary", tddActivity.getActivityDescription());
		bundle.putString("site", "1104957952");
		bundle.putString("appName", "我是TDD");
		return bundle;
	}

	Bundle shareParams = null;

	Handler shareHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		}
	};

	// 线程类，该类使用匿名内部类的方式进行声明
	Runnable shareThread = new Runnable() {

		public void run() {
			doShareToQQ(shareParams);
			Message msg = shareHandler.obtainMessage();

			// 将Message对象加入到消息队列当中
			shareHandler.sendMessage(msg);

		}
	};

	private void doShareToQQ(Bundle params) {
		mTencent.shareToQQ(RegistrationSubmitActivity.this, params, new BaseUiListener() {
			protected void doComplete(JSONObject values) {
				showResult("shareToQQ:", "分享成功");
			}

			@Override
			public void onError(UiError e) {
				showResult("shareToQQ:", "分享失败未安装登陆第三方");
			}

			@Override
			public void onCancel() {
				showResult("shareToQQ", "分享取消");
			}
		});
	}

	private class BaseUiListener implements IUiListener {

		// @Override
		// public void onComplete(JSONObject response) {
		// // mBaseMessageText.setText("onComplete:");
		// // mMessageText.setText(response.toString());
		// doComplete(response);
		// }

		protected void doComplete(Object values) {

		}

		@Override
		public void onError(UiError e) {
			showResult("onError:", "分享失败未安装登陆第三方");
		}

		@Override
		public void onCancel() {
			showResult("onCancel", "分享取消");
		}

		@Override
		public void onComplete(Object arg0) {
			// TODO Auto-generated method stub
			doComplete(arg0);
		}
	}

	private Handler mHandler;

	// qq分享的结果处理
	private void showResult(final String base, final String msg) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				UiHelper.ShowOneToast(RegistrationSubmitActivity.this, msg);
//				popupWindow.dismiss();
//				finish();
			}
		});
	}

	/**
	 * 提交验证
	 * 
	 */
	private void submitCheck() {
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("是否提交？").setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// 首先需要检测主报名人的信息是否符合规范

				refreshSumit();
			}
		}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();
	}

}
