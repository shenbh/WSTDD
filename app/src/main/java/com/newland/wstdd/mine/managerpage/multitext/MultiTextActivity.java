package com.newland.wstdd.mine.managerpage.multitext;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.applyList.BasePassListView;
import com.newland.wstdd.mine.applyList.bean.RegistrationListReq;
import com.newland.wstdd.mine.applyList.bean.RegistrationListRes;
import com.newland.wstdd.mine.applyList.bean.TddActivitySignVoInfo;
import com.newland.wstdd.mine.managerpage.multitext.bean.SendMessageReq;
import com.newland.wstdd.mine.managerpage.multitext.bean.SendMessageRes;
import com.newland.wstdd.mine.managerpage.multitext.handle.MultiTextRegistrationHandle;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;

/**
 * 群发消息
 * 
 * @author Administrator 2015-12-11
 */
public class MultiTextActivity extends BaseFragmentActivity implements OnClickListener, OnPostListenerInterface {
	private static final String TAG = "MultiTextActivity";//收集异常日志tag
	private LinearLayout allChoiceLayout;//全选的包裹
	private CheckBox checkBox;//全选CheckBox
	private TextView allchose_tv;// 全选
	private TextView applylist_tv;// 报名名单
	private BasePassListView multitext_listview;// 名单列表
	private LinearLayout multitext_bottom_ll;// 底部linearLayout
	private EditText multitext_send_edt;// 发送消息框
	private TextView multitext_send_tv;// 发送消息按钮

	private MultiTextAdapter multiTextAdapter;

	private String activityId;// 接收到的数据
	private String nickName;// 发布者昵称
	// 服务器返回的数据
	RegistrationListRes registrationListRes;
	SendMessageRes sendMessageRes;
	MultiTextRegistrationHandle handle = new MultiTextRegistrationHandle(this);
	private List<TddActivitySignVoInfo> tddActivitySignVoInfos = new ArrayList<TddActivitySignVoInfo>();
	private List<String> receivers = new ArrayList<String>();// 接收者集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		setContentView(R.layout.activity_manager_multitext);
		
		/**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		activityId = getIntent().getStringExtra("activityId");
		nickName = getIntent().getStringExtra("nickName");
		setTitle();
		initView();
		refresh();
	}

	@Override
	protected void onDestroy() {
		/** 收集异常日志 */
		LogManager.getManager(getApplicationContext()).unregisterActivity(this);
		/** 收集异常日志 */
		super.onDestroy();
	}
	/**
	 * 设置标题
	 */
	private void setTitle() {
		ImageView left_btn = (ImageView) findViewById(R.id.head_left_iv);
		ImageButton right_btn = (ImageButton) findViewById(R.id.head_right_btn);
		TextView right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView center_tv = (TextView) findViewById(R.id.head_center_title);
		if (center_tv != null)
			center_tv.setText("群发消息");
		if (left_btn != null) {
			left_btn.setVisibility(View.VISIBLE);
			left_btn.setImageDrawable(getResources().getDrawable(R.drawable.left_expandsion));
			left_btn.setOnClickListener(this);
		}
		if (right_btn != null) {
			right_btn.setVisibility(View.GONE);
		}
		if (right_tv != null) {
			right_tv.setVisibility(View.GONE);
		}
	}

	@Override
	protected void processMessage(Message msg) {
	}

	@Override
	public void initView() {
		allChoiceLayout = (LinearLayout) findViewById(R.id.manager_multitext_allchoice_ll);//全选的布局监听事件 
		allChoiceLayout.setOnClickListener(this);
		
		checkBox= (CheckBox) findViewById(R.id.checkBox);
		allchose_tv = (TextView) findViewById(R.id.allchose_tv);
		applylist_tv = (TextView) findViewById(R.id.applylist_tv);
		multitext_listview = (BasePassListView) findViewById(R.id.multitext_listview);
		multitext_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				CheckBox mCheckBox = (CheckBox) view.findViewById(R.id.item_checkBox);
				if (mCheckBox.isChecked()) {
					mCheckBox.setChecked(false);
				}else {
					mCheckBox.setChecked(true);
				}
//				MultiTextAdapter.getIsSelected().put(position, checkBox.isChecked());
				tddActivitySignVoInfos.get(position).setSelected(checkBox.isChecked());
			}
		});
		
		
		multitext_bottom_ll = (LinearLayout) findViewById(R.id.multitext_bottom_ll);
		multitext_send_edt = (EditText) findViewById(R.id.multitext_send_edt);
		multitext_send_tv = (TextView) findViewById(R.id.multitext_send_tv);

		allchose_tv.setOnClickListener(this);
		multitext_send_tv.setOnClickListener(this);
		
		checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (checkBox.isChecked()) {
					allchose_tv.setText("取消");
//					checkBox.setChecked(false);
				}else {
					allchose_tv.setText("全选");
//					checkBox.setChecked(true);
				}
				for (int i = 0 , size = tddActivitySignVoInfos.size(); i < size; i++) {
					if (allchose_tv.getText().toString().equals("全选")) {
						tddActivitySignVoInfos.get(i).setSelected(false);
//						MultiTextAdapter.getIsSelected().put(i, false);
					} else if (allchose_tv.getText().toString().equals("取消")) {
//						MultiTextAdapter.getIsSelected().put(i, true);
						tddActivitySignVoInfos.get(i).setSelected(true);
					}
				}
				// 刷新listview和TextView的显示
				multiTextAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void refresh() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					RegistrationListReq reqInfo = new RegistrationListReq();
					reqInfo.setActivityId(activityId);
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<RegistrationListRes> ret = mgr.getRegistrationListInfo(reqInfo);// 泛型类，
					Message message = new Message();
					message.what = MultiTextRegistrationHandle.REGISTRATION_LIST;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						registrationListRes = (RegistrationListRes) ret.getObj();
						message.obj = registrationListRes;
					} else {
						message.obj = ret.getMsg();
					}
					handle.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 群发消息
	 * 
	 */
	public void multiText() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					SendMessageReq sendMessageReq = new SendMessageReq();
					sendMessageReq.setMessage(multitext_send_edt.getText().toString());// 消息内容
					sendMessageReq.setReceivers(receivers);// 接受者userId数组
					sendMessageReq.setSponsor(nickName);// 发起人（可不填，默认当前用户nikename）
					sendMessageReq.setType("活动提醒");// 消息类型（可不填，默认“活动提醒”）
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<SendMessageRes> retMsg = mgr.getMultiTextInfo(sendMessageReq, activityId);
					Message message = new Message();
					message.what = MultiTextRegistrationHandle.MULTITEXT;
					// 访问服务器成功1，否则访问服务器失败
					if (retMsg.getCode() == 1) {
						if (retMsg.getObj() != null) {
							sendMessageRes = (SendMessageRes) retMsg.getObj();
							message.obj = sendMessageRes;
						} else {
							sendMessageRes = new SendMessageRes();
							message.obj = sendMessageRes;
						}
					} else {
						message.obj = retMsg.getMsg();
					}
					handle.sendMessage(message);
				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		try {
			switch (responseId) {
			case MultiTextRegistrationHandle.REGISTRATION_LIST:// 活动报名人员列表
//				UiHelper.ShowOneToast(this, "请求名单数据成功");
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				registrationListRes = (RegistrationListRes) obj;
				if (registrationListRes != null) {
					for (int i = 0; i < registrationListRes.getSignPassList().size(); i++) {
						TddActivitySignVoInfo tddActivitySignVoInfo = new TddActivitySignVoInfo();
						tddActivitySignVoInfo.setTddActivitySignVo(registrationListRes.getSignPassList().get(i));
						tddActivitySignVoInfo.setSelected(false);
						tddActivitySignVoInfos.add(tddActivitySignVoInfo);
					}
					applylist_tv.setText("报名名单(" + registrationListRes.getSignPassCount() + ")");
					multiTextAdapter = new MultiTextAdapter(this, tddActivitySignVoInfos);
					multitext_listview.setAdapter(multiTextAdapter);
					multiTextAdapter.notifyDataSetChanged();
				}
				break;
			case MultiTextRegistrationHandle.MULTITEXT:// 群发消息
				if (progressDialog != null) {
					progressDialog.setContinueDialog(false);
				}
				sendMessageRes = (SendMessageRes) obj;
				if (sendMessageRes != null) {
					UiHelper.ShowOneToast(this, "群发消息成功");
					finish();
				}

				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_left_iv:// 返回
			finish();
			break;
		case R.id.multitext_send_tv:// 发送
			if (StringUtil.isNotEmpty(multitext_send_edt.getText().toString())) {
				for (int i = 0, size = tddActivitySignVoInfos.size(); i < size; i++) {
					if (tddActivitySignVoInfos.get(i).isSelected()) {
						receivers.add(tddActivitySignVoInfos.get(i).getTddActivitySignVo().getSignUserId());
					}
				}
				if (receivers.size() != 0) {
					multiText();
				} else {
					UiHelper.ShowOneToast(this, "接收者不能为空");
				}
			} else {
				UiHelper.ShowOneToast(this, "不能发送空消息");
			}
			break;
		case R.id.allchose_tv:// 全选
			if (allchose_tv.getText().toString().equals("全选")) {
				allchose_tv.setText("取消");
				checkBox.setChecked(true);
			} else if (allchose_tv.getText().toString().equals("取消")) {  // "123".equ(aa);
				allchose_tv.setText("全选");
				checkBox.setChecked(false);
			}
			for (int i = 0; i < tddActivitySignVoInfos.size(); i++) {
				if (allchose_tv.getText().toString().equals("全选")) {
//					MultiTextAdapter.getIsSelected().put(i, false);
					tddActivitySignVoInfos.get(i).setSelected(false);
				} else if (allchose_tv.getText().toString().equals("取消")) {
//					MultiTextAdapter.getIsSelected().put(i, true);
					tddActivitySignVoInfos.get(i).setSelected(true);
				}
			}
			// 刷新listview和TextView的显示
			multiTextAdapter.notifyDataSetChanged();
			break;
		case R.id.manager_multitext_allchoice_ll:
			if (allchose_tv.getText().toString().equals("全选")) {
				allchose_tv.setText("取消");
				checkBox.setChecked(true);
			} else if (allchose_tv.getText().toString().equals("取消")) {  // "123".equ(aa);
				allchose_tv.setText("全选");
				checkBox.setChecked(false);
			}
			for (int i = 0; i < tddActivitySignVoInfos.size(); i++) {
				if (allchose_tv.getText().toString().equals("全选")) {
//					MultiTextAdapter.getIsSelected().put(i, false);
					tddActivitySignVoInfos.get(i).setSelected(false);
				} else if (allchose_tv.getText().toString().equals("取消")) {
//					MultiTextAdapter.getIsSelected().put(i, true);
					tddActivitySignVoInfos.get(i).setSelected(true);
				}
			}
			// 刷新listview和TextView的显示
			multiTextAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

}
