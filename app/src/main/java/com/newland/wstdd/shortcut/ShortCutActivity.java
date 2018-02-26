package com.newland.wstdd.shortcut;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.EditTextUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.widget.PengTextView;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.login.beanrequest.CheckCodeReq;
import com.newland.wstdd.login.beanresponse.CheckCodeRes;
import com.newland.wstdd.login.handle.RegistFragmentHandle;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.test.DeleteUserInfoReq;
import com.test.DeleteUserInfoRes;
import com.test.TestHandle;

/**
 * 快捷界面
 * 
 * @author H81 2015-11-4
 * 
 */
public class ShortCutActivity extends BaseFragmentActivity implements OnClickListener,OnPostListenerInterface {
	private static final String TAG = "ShortCutActivity";//收集异常日志tag
	private static final int PICTURE = 1; // 选择图片
	private EditText mActivity_shortcut_contentedt;// 输入的内容
	private PengTextView mActivity_shortcut_remainwords_tv;// 还可输入150字
	private ImageView mActivity_shortcut_choosepicture_iv;// 选择图片
	private String phtotFilename;// 图片路径
	private String photoname;
	private int inputNum = 150;// 限制输入的字数
	// 标题栏
	private TextView mHead_left_tv;
	private TextView mHead_right_tv;
	
	//服务器     注销用户测试用的
	DeleteUserInfoRes deleteUserInfoRes;//注销用户的信息
	TestHandle testHandle = new TestHandle(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		setContentView(R.layout.activity_shortcut_main);
		
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
	 
	/**
	 * 设置标题
	 */
	private void setTitle() {
		mHead_left_tv = (TextView) findViewById(R.id.head_left_tv);
		mHead_right_tv = (TextView) findViewById(R.id.head_right_tv);
		TextView mHead_center_title = (TextView) findViewById(R.id.head_center_title);
		initView();
		if (mHead_center_title != null)
			mHead_center_title.setText("快捷发布");
		if (mHead_left_tv != null) {// 返回
			mHead_left_tv.setVisibility(View.VISIBLE);
			mHead_left_tv.setText("取消");
			mHead_left_tv.setOnClickListener(this);
		}
		if (mHead_right_tv != null) {
			mHead_right_tv.setVisibility(View.VISIBLE);
			mHead_right_tv.setText("发布");
//			mHead_right_tv.setOnClickListener(this);
		}

		mHead_left_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//暂时作为注销账户的测试
		mHead_right_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				testCancelUser();//注销用户
			}
		});
	}

	public void initView() {
		mActivity_shortcut_contentedt = (EditText) findViewById(R.id.activity_shortcut_contentedt);
		mActivity_shortcut_remainwords_tv = (PengTextView) findViewById(R.id.activity_shortcut_remainwords_tv);
		mActivity_shortcut_choosepicture_iv = (ImageView) findViewById(R.id.activity_shortcut_choosepicture_iv);
		mActivity_shortcut_choosepicture_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showImgFileChooser();
			}
		});
		//解决滑动条冲突
		mActivity_shortcut_contentedt.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_UP:
					v.getParent().requestDisallowInterceptTouchEvent(false);
					break;

				default:
					break;
				}
				return false;
			}
		});
		// 限制输入的个数
		mActivity_shortcut_contentedt.addTextChangedListener(new TextWatcher() {
			private boolean isOutOfBounds = false;
			int end;

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > inputNum) {
					isOutOfBounds = true;
				} else {
					mActivity_shortcut_remainwords_tv.setText(s.length() + "/150");
					isOutOfBounds = false;
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
				if (isOutOfBounds) {
					UiHelper.ShowOneToast(ShortCutActivity.this, "字符超过了");
					if (s.length() > inputNum) {
						s.delete(inputNum, s.length());
						end = inputNum;
					} else if (s.length() > 20 && s.length() <= inputNum) {
						s.delete(20, s.length());
						end = 20;
					}
					end = s.length();
					mActivity_shortcut_contentedt.setSelection(end);// 设置光标在最后
					mActivity_shortcut_remainwords_tv.setText(s.length() + "/150");
				}
			}
		});
	}

	/** 调用文件选择软件来选择文件 **/
	private void showImgFileChooser() {
		Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(picture, PICTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PICTURE) {
				Uri selectedImage = data.getData();
				String[] filePathColumns = { MediaStore.Images.Media.DATA };
				Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePathColumns[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				// 获取图片并显示
				Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
				mActivity_shortcut_choosepicture_iv.setImageBitmap(bitmap);
			}
		}
	}

	@Override
	protected void processMessage(Message msg) {

	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	
	//注销账户
	private void testCancelUser() {
		// TODO Auto-generated method stub

		super.refreshDialog();
		try {
			new Thread() {
				public void run() {
						// 需要发送一个request的对象进行请求
						DeleteUserInfoReq reqInfo = new DeleteUserInfoReq();
						reqInfo.setUserId(AppContext.getAppContext().getUserId());;
						BaseMessageMgr mgr = new HandleNetMessageMgr();
						RetMsg<DeleteUserInfoRes> ret = mgr.getCancelUserInfo(reqInfo);// 泛型类，
						Message message = new Message();
						message.what = TestHandle.CANCEL_USER;// 设置死
						// 访问服务器成功 1 否则访问服务器失败
						if (ret.getCode() == 1) {
							if (ret.getObj() != null) {
								deleteUserInfoRes = (DeleteUserInfoRes) ret.getObj();
								message.obj = deleteUserInfoRes;
							} else {
								deleteUserInfoRes = new DeleteUserInfoRes();
								message.obj = deleteUserInfoRes;
							}

						} else {

							message.obj = ret.getMsg();
						}

						testHandle.sendMessage(message);
					

				};
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}

	
	}

	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		// TODO Auto-generated method stub
		if (progressDialog != null) {
			progressDialog.setContinueDialog(false);
			
		}
	}

	@Override
	public void OnFailResultListener(String mess) {
		// TODO Auto-generated method stub
		if (progressDialog != null) {
			progressDialog.setContinueDialog(false);
			
		}
	}
	
	
}
