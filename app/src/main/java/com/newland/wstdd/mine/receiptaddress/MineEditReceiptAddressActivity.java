package com.newland.wstdd.mine.receiptaddress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.EditTextUtil;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.receiptaddress.beanrequest.MineAddAddressReq;
import com.newland.wstdd.mine.receiptaddress.beanrequest.TddDeliverAddr;
import com.newland.wstdd.mine.receiptaddress.beanresponse.MineAddAddressRes;
import com.newland.wstdd.mine.receiptaddress.handle.MineUpdateAddressHandle;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;

/**
 * 我的-编辑收货地址
 * 
 * @author H81 2015-11-8
 * 
 */
public class MineEditReceiptAddressActivity extends BaseFragmentActivity implements OnClickListener, OnPostListenerInterface {
	private static final String TAG = "MineEditReceiptAddressActivity";//收集异常日志tag
	// 收件人
	private TextView mMinereceiptaddress_receiptname_tv;
	private EditText mMinereceiptaddress_receiptname_edt;
	// 手机号码
	private TextView mMinereceiptaddress_phone_tv;
	private EditText mMinereceiptaddress_phone_edt;
	// 所在区域
	private TextView mMinereceiptaddress_area_tv;
	private TextView mMinereceiptaddress_area_content_tv;
	private ImageView setting_rightexpand_iv;
	// 详细地址
	private TextView mMinereceiptaddress_address_tv;
	private EditText mMinereceiptaddress_address_edt;
	// 邮政编码
	private TextView mMinereceiptaddress_postalcode_tv;
	private EditText mMinereceiptaddress_postalcode_edt;

	MyAddressBroadcastReceiver myAddressBroadcastReceiver;

	private EditReceiptAddressPopupWindow popWin;

	TddDeliverAddr tddDeliverAddr;
	// 服务器返回的信息
	MineAddAddressRes mineAddAddressRes;// 地址列表
	MineUpdateAddressHandle handler = new MineUpdateAddressHandle(this);

	private String provinceString;// 省份
	private String cityString;// 城市

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		tddDeliverAddr = (TddDeliverAddr) getIntent().getSerializableExtra("tddDeliverAddr");
		setContentView(R.layout.activity_mine_editreceiptaddress);
		
		 /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
        
		setTitle();
		initView();

	}

	@Override
	protected void onDestroy() {
		/** 收集异常日志 */
		LogManager.getManager(getApplicationContext()).unregisterActivity(this);
		/** 收集异常日志 */
		super.onDestroy();
	}
	public void initView() {

		mMinereceiptaddress_receiptname_tv = (TextView) findViewById(R.id.minereceiptaddress_receiptname_tv);
		mMinereceiptaddress_receiptname_edt = (EditText) findViewById(R.id.minereceiptaddress_receiptname_edt);
		mMinereceiptaddress_phone_tv = (TextView) findViewById(R.id.minereceiptaddress_phone_tv);
		mMinereceiptaddress_phone_edt = (EditText) findViewById(R.id.minereceiptaddress_phone_edt);
		StringUtil.limitPatternPhone(mMinereceiptaddress_phone_edt);// 设置格式
		mMinereceiptaddress_area_tv = (TextView) findViewById(R.id.minereceiptaddress_area_tv);
		mMinereceiptaddress_area_content_tv = (TextView) findViewById(R.id.minereceiptaddress_area_content_tv);
		mMinereceiptaddress_address_tv = (TextView) findViewById(R.id.minereceiptaddress_address_tv);
		mMinereceiptaddress_address_edt = (EditText) findViewById(R.id.minereceiptaddress_address_edt);
		setting_rightexpand_iv = (ImageView) findViewById(R.id.setting_rightexpand_iv);
		mMinereceiptaddress_postalcode_tv = (TextView) findViewById(R.id.minereceiptaddress_postalcode_tv);
		mMinereceiptaddress_postalcode_edt = (EditText) findViewById(R.id.minereceiptaddress_postalcode_edt);

		mMinereceiptaddress_receiptname_edt.setText(tddDeliverAddr.getConnectUserName());
		mMinereceiptaddress_phone_edt.setText(tddDeliverAddr.getConnectPhone());
		mMinereceiptaddress_area_content_tv.setText(tddDeliverAddr.getDistrict());
		mMinereceiptaddress_address_edt.setText(tddDeliverAddr.getAddress());
		mMinereceiptaddress_postalcode_edt.setText(tddDeliverAddr.getZipCode());
		mMinereceiptaddress_area_content_tv.setOnClickListener(this);
	}

	private void setTitle() {
		ImageView leftIv = (ImageView) findViewById(R.id.head_left_iv);
		TextView centerTitle = (TextView) findViewById(R.id.head_center_title);
		TextView rightTv = (TextView) findViewById(R.id.head_right_tv);
		leftIv.setVisibility(View.VISIBLE);
		centerTitle.setText("编辑收货地址");
		rightTv.setText("完成");
		rightTv.setTextColor(getResources().getColor(R.color.red));
		rightTv.setVisibility(View.VISIBLE);
		leftIv.setOnClickListener(this);
		rightTv.setOnClickListener(this);
	}

	private void popWindow() {
		// 设置透明度
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.3f;
		getWindow().setAttributes(lp);
		popWin = new EditReceiptAddressPopupWindow(MineEditReceiptAddressActivity.this, null);
		popWin.setOnDismissListener(new OnDismissListener() {

			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
				setting_rightexpand_iv.setImageDrawable(getResources().getDrawable(R.drawable.setting_rightexpand));
			}
		});
		setting_rightexpand_iv.setImageDrawable(getResources().getDrawable(R.drawable.setting_down_expand_icon));
		// 显示窗口
		popWin.showAtLocation(MineEditReceiptAddressActivity.this.findViewById(R.id.layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	@Override
	protected void onResume() {
		super.onResume();
		myAddressBroadcastReceiver = new MyAddressBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppContext.ACTION_ADDRESS);
		registerReceiver(myAddressBroadcastReceiver, intentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(myAddressBroadcastReceiver);
	}

	class MyAddressBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String proviceNameString = bundle.getString("mCurrentProviceName");
			String cityNameString = bundle.getString("mCurrentCityName");
			String districtNameString = bundle.getString("mCurrentDistrictName");
			String zipCodeString = bundle.getString("mCurrentZipCode");// 邮政编码
			System.out.println("获取到：" + proviceNameString + "," + cityNameString + "," + districtNameString + "," + zipCodeString);
			mMinereceiptaddress_area_content_tv.setText(proviceNameString + cityNameString + districtNameString);
			mMinereceiptaddress_postalcode_edt.setText(zipCodeString);
			provinceString = proviceNameString;
			cityString = cityNameString;
		}
	}

	@Override
	protected void processMessage(Message msg) {

	}

	@Override
	public void refresh() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					MineAddAddressReq addAddressReq = new MineAddAddressReq();
					tddDeliverAddr.setConnectUserName(mMinereceiptaddress_receiptname_edt.getText().toString());// 收货人名字

					tddDeliverAddr.setConnectPhone(mMinereceiptaddress_phone_edt.getText().toString());// 收货人电话
					tddDeliverAddr.setDistrict(mMinereceiptaddress_area_content_tv.getText().toString());// 行政区域
					tddDeliverAddr.setAddress(mMinereceiptaddress_address_edt.getText().toString());// 详细地址
					tddDeliverAddr.setZipCode(mMinereceiptaddress_postalcode_edt.getText().toString());// 邮政编码

					tddDeliverAddr.setProvince(provinceString);// 省份
					tddDeliverAddr.setCity(cityString);// 城市
					tddDeliverAddr.setUserId(AppContext.getAppContext().getUserId());
					addAddressReq.setTddDeliverAddr(tddDeliverAddr);
					RetMsg<MineAddAddressRes> ret = mgr.getMineUpdateAddressInfo(addAddressReq, tddDeliverAddr.getAddressId());// 泛型类，
					Message message = new Message();
					message.what = MineUpdateAddressHandle.UPDATE_ADDRESS;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						mineAddAddressRes = (MineAddAddressRes) ret.getObj();
						message.obj = mineAddAddressRes;
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

	private void judgeOk() {
		if (mMinereceiptaddress_receiptname_edt != null && StringUtil.isNotEmpty(mMinereceiptaddress_receiptname_edt.getText().toString()) && mMinereceiptaddress_phone_edt != null
				&& StringUtil.isNotEmpty(mMinereceiptaddress_phone_edt.getText().toString()) && mMinereceiptaddress_area_content_tv != null
				&& StringUtil.isNotEmpty(mMinereceiptaddress_area_content_tv.getText().toString()) && mMinereceiptaddress_address_edt != null
				&& StringUtil.isNotEmpty(mMinereceiptaddress_address_edt.getText().toString()) && mMinereceiptaddress_postalcode_edt != null
				&& StringUtil.isNotEmpty(mMinereceiptaddress_postalcode_edt.getText().toString())) {
			if (EditTextUtil.checkMobileNumber(mMinereceiptaddress_phone_edt.getText().toString())) {
				refresh();
			} else {
				UiHelper.ShowOneToast(this, "手机号格式不对，请确认!");
			}
		} else {
			UiHelper.ShowOneToast(this, "数据未填充完全，请检查!");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_left_iv:// 返回
			finish();
			break;
		case R.id.head_right_tv:// 完成
			// 完成添加并提交 需要做一些字符串方面的判断
			judgeOk();// 判断输入什么的是否符合需求
			break;
		case R.id.minereceiptaddress_area_content_tv:
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MineEditReceiptAddressActivity.this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			popWindow();
			break;
		default:
			break;
		}
	}

	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		switch (responseId) {
		case MineUpdateAddressHandle.UPDATE_ADDRESS:
			if (progressDialog != null) {
				progressDialog.setContinueDialog(false);
			}
			mineAddAddressRes = (MineAddAddressRes) obj;
			if (mineAddAddressRes != null) {
				// UiHelper.ShowOneToast(this,
				// "14.	收货地址更新成功！！！"+mineAddAddressRes.getAddresses().get(0).getAddress());
				if (mineAddAddressRes.getAddresses().size() > 0) {
					UiHelper.ShowOneToast(this, "更新地址成功！！！" + mineAddAddressRes.getAddresses().get(0).getAddress());
					finish();
				}
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
}
