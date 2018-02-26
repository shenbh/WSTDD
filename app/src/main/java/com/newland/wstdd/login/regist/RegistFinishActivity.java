package com.newland.wstdd.login.regist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.wstdd.R;
import com.newland.wstdd.common.SharePreferenceUtil.SharePreferenceUtil;
import com.newland.wstdd.common.activity.HomeActivity;
import com.newland.wstdd.common.base.BaseFragmentActivity;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.common.UrlManager;
import com.newland.wstdd.common.fileupload.HttpMultipartPost;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.widget.PengRadioButton;
import com.newland.wstdd.common.widget.RoundImageView;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.login.beanrequest.RegistSecondReq;
import com.newland.wstdd.login.beanrequest.ThirdLoginReq;
import com.newland.wstdd.login.beanresponse.HeadImgRes;
import com.newland.wstdd.login.beanresponse.LoginRes;
import com.newland.wstdd.login.beanresponse.RegistSecondRes;
import com.newland.wstdd.login.beanresponse.ThirdLoginRes;
import com.newland.wstdd.login.handle.LoginFragmentHandle;
import com.newland.wstdd.login.handle.RegistFragmentFinishHandle;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.netutils.MessageUtil;
import com.newland.wstdd.netutils.WBResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class RegistFinishActivity extends BaseFragmentActivity implements OnPostListenerInterface {
	private static final String TAG = "RegistFinishActivity";//收集异常日志tag
	/**
	 * 头像上传
	 */
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private RoundImageView headImageView;// 头像
	private Bitmap localBitmap;
	private Bitmap bitmap;
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;
	private List<String> filePathList;// 保存本地图片的地址

	// URL后面的id 用常量，用完就变成空的
	private Intent intent;
	private DisplayImageOptions options; // 设置图片显示相关参数

	// 从注册第一步得到的信息 以及本界面上字符串的信息
	String userIdString = null;// 用户id
	String headImgUrlString = null;// 头像地址
	String nickNameString;// 昵称
	String pwdString;
	// 控件
	private EditText nicknameEditText, passwordEditText;// 昵称,密码
	private Button finishRegistButton;// 完成注册
	private PengRadioButton passwordShowHideBt; // 显示 隐藏密码
	private boolean judge = true;// 是否显示密码
	// 第三方直接登录了
	ThirdLoginRes thirdLoginRes;// 返回信息 第三方登录
	// 服务器返回的内容
	private RegistSecondRes registSecondRes;
	private RegistFragmentFinishHandle handler = new RegistFragmentFinishHandle(this);
	private Uri uriImg;// 获取到了图片的Uri
	private HttpMultipartPost post;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
		filePathList = new ArrayList<String>();// 保存本地头像的地址
		setContentView(R.layout.activity_regist_finish);
		
		 /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate",LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/
		
		intent = getIntent();
		registSecondRes = new RegistSecondRes();// 这个比较特殊，内容体为空
		Bundle bundle = intent.getExtras();
		userIdString = bundle.getString("userIdString");
		headImgUrlString = bundle.getString("headImgUrlString");
		nickNameString = bundle.getString("nickNameString");
		// 需要对头像跟昵称进行相应的判断 要是本地有的话直接放上去，要是本地没有的的话，再去自己设置
		initTitle();// 初始化标题栏的信息
		initView();// 初始化控件

	}
	
	@Override
	protected void onDestroy() {
		 /**收集异常日志*/
    	LogManager.getManager(getApplicationContext()).unregisterActivity(this);
    	 /**收集异常日志*/
    	super.onDestroy();
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		TextView topCenterTitleTextView = (TextView) findViewById(R.id.head_center_title);
		topCenterTitleTextView.setText("完成注册");
		ImageView leftImageView = (ImageView) findViewById(R.id.head_left_iv);
		leftImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	// 初始化控件
	public void initView() {
		// TODO Auto-generated method stub
		passwordShowHideBt = (PengRadioButton) findViewById(R.id.regist_finish_show_hide);
		headImageView = (RoundImageView) findViewById(R.id.regist_head_image);
		headImageView.setOnClickListener(this);
		finishRegistButton = (Button) findViewById(R.id.regist_finish);
		finishRegistButton.setOnClickListener(this);
		nicknameEditText = (EditText) findViewById(R.id.regist_nick_name);
		passwordEditText = (EditText) findViewById(R.id.regist_user_password);
		passwordShowHideBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (judge) {
					// 显示密码
					passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					passwordShowHideBt.setChecked(true);
					judge = false;
				} else {
					passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
					passwordShowHideBt.setChecked(false);
					judge = true;
				}
			}
		});

		if (nickNameString != null && !"".equals(nickNameString)) {
			nicknameEditText.setText(nickNameString);
		}
		if (headImgUrlString != null && !"".equals(headImgUrlString)) {
			// 说明有图片了~从服务器上下载图片
			// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_stub) // 设置图片下载期间显示的图片
					.showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
					.build(); // 构建完成
			/**
			 * imageUrl 图片的Url地址 imageView 承载图片的ImageView控件 options
			 * DisplayImageOptions配置文件
			 */
			// String
			// urlImageString="http://mario.picp.net/tdd/resources/upload/";

//			ImageLoader.getInstance().displayImage(headImgUrlString, headImageView, options);
			ImageLoader.getInstance().displayImage(headImgUrlString, headImageView, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					if(localBitmap!=null){
						headImageView.setImageBitmap(localBitmap);
					}else {
						headImageView.setImageDrawable(getResources().getDrawable(R.drawable.default_head_icon));
					}
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					localBitmap = arg2;//保存到本地
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					if(localBitmap!=null){
						headImageView.setImageBitmap(localBitmap);
					}else {
						headImageView.setImageDrawable(getResources().getDrawable(R.drawable.default_head_icon));
					}
				}
			});
			

		}else{
			headImageView.setImageResource(R.drawable.default_head_icon);
		}
	}

	@Override
	protected void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.regist_finish:
			try {
				// 先判断
				if (nicknameEditText.getText().toString() == null && "".equals(nicknameEditText.getText().toString()) && passwordEditText.getText().toString() == null
						&& "".equals(passwordEditText.getText().toString())) {
					UiHelper.ShowOneToast(RegistFinishActivity.this, "昵称密码都不能为空");
				} else {
					// 这里先进行图片的上传操作，要是bitmap为空的话，则不用进行图片上传操作 只有上传成功了，才进行完成注册的操作
					pwdString = passwordEditText.getText().toString();
					nickNameString = nicknameEditText.getText().toString();
					if (bitmap != null) {
						// 进行上传的操作
						upload();
					} else {
						refresh();// 无需头像上传直接进行上传操作
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case R.id.regist_head_image:
			showDownLoadDialog();
			break;
		default:
			break;
		}
	}

	// 提交最后一步的注册信息 注册第二步
	@Override
	public void refresh() {
		super.refreshDialog();
		try {
			new Thread() {
				public void run() {

					// 需要发送一个request的对象进行请求
					RegistSecondReq reqInfo = new RegistSecondReq();
					reqInfo.setHeadImgUrl(headImgUrlString);
					reqInfo.setNickName(nickNameString);
					reqInfo.setPwd(pwdString);
					reqInfo.setOpenId(AppContext.getAppContext().getOpenId());
					reqInfo.setPlatForm(AppContext.getAppContext().getPlatForm());
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<RegistSecondRes> ret = mgr.getRegistFinishInfo(reqInfo, userIdString);// 泛型类，
					Message message = new Message();
					message.what = RegistFragmentFinishHandle.REGIST_SECOND;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						if (ret.getObj() != null) {
							registSecondRes = (RegistSecondRes) ret.getObj();
							message.obj = registSecondRes;
						} else {
							registSecondRes = new RegistSecondRes();
							message.obj = registSecondRes;
						}

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
	 * 网络请求第三方登录的 信息
	 */
	private void getThirdLoginResMess() {
		// TODO Auto-generated method stub

		try {
			new Thread() {
				public void run() {
					// 需要发送一个request的对象进行请求
					ThirdLoginReq reqInfo = new ThirdLoginReq();
					reqInfo.setPlatForm(AppContext.getAppContext().getPlatForm());
					reqInfo.setOpenId(AppContext.getAppContext().getOpenId());
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<ThirdLoginRes> ret = mgr.getThirdLoginInfo(reqInfo);// 泛型类，
					Message message = new Message();
					message.what = LoginFragmentHandle.THIRD_LOGIN;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						thirdLoginRes = (ThirdLoginRes) ret.getObj();
						message.obj = thirdLoginRes;
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
		switch (responseId) {
		case RegistFragmentFinishHandle.REGIST_SECOND:
			if (progressDialog != null) {
				progressDialog.setContinueDialog(false);
			}
			try {
				registSecondRes = (RegistSecondRes) obj;
				// 如果你的openId为空的 说明是直接登录的 而不是同第三方登录的
				if ( AppContext.getAppContext().getOpenId() == null || "".equals(AppContext.getAppContext().getOpenId())) {
					// 创建一下SharePreference
					(new SharePreferenceUtil(this)).savesAppcntextInfo("", "", "", "", "", "", "", "", "", "", "", "", "", "", "0", "0", "0", "", "", "true","");
					LoginRes loginResInfo = new LoginRes();
					loginResInfo.setHeadImgUrl(registSecondRes.getHeadImgUrl());
					loginResInfo.setHomeAds(registSecondRes.getHomeAds());
					loginResInfo.setMyAcNum(registSecondRes.getMyAcNum());
					loginResInfo.setMyFavAcNum(registSecondRes.getMyFavAcNum());
					loginResInfo.setMySignAcNum(registSecondRes.getMySignAcNum());
					loginResInfo.setNickName(registSecondRes.getNickName());
					loginResInfo.setTags(registSecondRes.getTags());
					loginResInfo.setUserId(registSecondRes.getUserId());
					// 保存到 AppContext.getAppContext()
					if (null != AppContext.getAppContext()) {
						AppContext.getAppContext().setUserId(loginResInfo.getUserId());
						AppContext.getAppContext().setHeadImgUrl(loginResInfo.getHeadImgUrl());
						AppContext.getAppContext().setMyAcNum(loginResInfo.getMyAcNum());
						AppContext.getAppContext().setMyFavAcNum(loginResInfo.getMyFavAcNum());
						AppContext.getAppContext().setMySignAcNum(loginResInfo.getMySignAcNum());
						AppContext.getAppContext().setTags(loginResInfo.getTags());
						StringUtil.appContextTagsListToString(loginResInfo.getTags());//以字符串的形式
						AppContext.getAppContext().setHomeAds(loginResInfo.getHomeAds());
						AppContext.getAppContext().setNickName(loginResInfo.getNickName());
						AppContext.getAppContext().setUserPwd(passwordEditText.getText().toString());
						AppContext.getAppContext().setIsLogin("true");
					}
					Intent intent = new Intent(this, HomeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("loginres", loginResInfo);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();

				} else {
					// 说明是通过第三方登录的

					// 创建一下SharePreference
					(new SharePreferenceUtil(this)).savesAppcntextInfo("", "", "", "", "", "", "", "", "", "", "", "", "", "", "0", "0", "0", "", "", "true","");
					LoginRes loginResInfo = new LoginRes();
					loginResInfo.setHeadImgUrl(registSecondRes.getHeadImgUrl());
					loginResInfo.setHomeAds(registSecondRes.getHomeAds());
					loginResInfo.setMyAcNum(registSecondRes.getMyAcNum());
					loginResInfo.setMyFavAcNum(registSecondRes.getMyFavAcNum());
					loginResInfo.setMySignAcNum(registSecondRes.getMySignAcNum());
					loginResInfo.setNickName(registSecondRes.getNickName());
					loginResInfo.setTags(registSecondRes.getTags());
					loginResInfo.setUserId(registSecondRes.getUserId());
					// 保存到 AppContext.getAppContext()
					if (null != AppContext.getAppContext()) {
						AppContext.getAppContext().setUserId(loginResInfo.getUserId());
						AppContext.getAppContext().setHeadImgUrl(loginResInfo.getHeadImgUrl());
						AppContext.getAppContext().setMyAcNum(loginResInfo.getMyAcNum());
						AppContext.getAppContext().setMyFavAcNum(loginResInfo.getMyFavAcNum());
						AppContext.getAppContext().setMySignAcNum(loginResInfo.getMySignAcNum());
						AppContext.getAppContext().setTags(loginResInfo.getTags());
						StringUtil.appContextTagsListToString(loginResInfo.getTags());//以字符串的形式
						AppContext.getAppContext().setHomeAds(loginResInfo.getHomeAds());
						AppContext.getAppContext().setNickName(loginResInfo.getNickName());
						AppContext.getAppContext().setUserPwd(passwordEditText.getText().toString());
						AppContext.getAppContext().setIsLogin("true");
					}
					Intent intent = new Intent(this, HomeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("loginres", loginResInfo);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();

				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case LoginFragmentHandle.THIRD_LOGIN:
			if (progressDialog != null) {
				progressDialog.setContinueDialog(false);
			}
			thirdLoginRes = (ThirdLoginRes) obj;
			if (thirdLoginRes != null) {
				if ("true".equals(thirdLoginRes.getIfBind())) {
					// 创建一下SharePreference
					(new SharePreferenceUtil(this)).savesAppcntextInfo("", "", "", "", "", "", "", "", "", "", "", "", "", "", "0", "0", "0", "", "", "true","");
					LoginRes loginResInfo = new LoginRes();
					loginResInfo.setHeadImgUrl(thirdLoginRes.getHeadImgUrl());
					loginResInfo.setHomeAds(thirdLoginRes.getHomeAds());
					loginResInfo.setMyAcNum(thirdLoginRes.getMyAcNum());
					loginResInfo.setMyFavAcNum(thirdLoginRes.getMyFavAcNum());
					loginResInfo.setMySignAcNum(thirdLoginRes.getMySignAcNum());
					loginResInfo.setNickName(thirdLoginRes.getNickName());
					loginResInfo.setTags(thirdLoginRes.getTags());
					loginResInfo.setUserId(thirdLoginRes.getUserId());
					if (null != AppContext.getAppContext()) {
						AppContext.getAppContext().setUserId(loginResInfo.getUserId());
						AppContext.getAppContext().setHeadImgUrl(loginResInfo.getHeadImgUrl());
						AppContext.getAppContext().setMyAcNum(loginResInfo.getMyAcNum());
						AppContext.getAppContext().setMyFavAcNum(loginResInfo.getMyFavAcNum());
						AppContext.getAppContext().setMySignAcNum(loginResInfo.getMySignAcNum());
						AppContext.getAppContext().setTags(loginResInfo.getTags());
						StringUtil.appContextTagsListToString(loginResInfo.getTags());//以字符串的形式
						AppContext.getAppContext().setHomeAds(loginResInfo.getHomeAds());
						AppContext.getAppContext().setNickName(loginResInfo.getNickName());
						AppContext.getAppContext().setIsLogin("true");
					}
					Intent intent = new Intent(this, HomeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("loginres", loginResInfo);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}
				// 该账号还没有绑定的用户，需要进行绑定或者登入
				else if ("false".equals(thirdLoginRes.getIfBind())) {
					// 跳转到绑定界面
					UiHelper.ShowOneToast(this, "绑定失败");
					// Intent intent=new Intent(this,LoginBindActivity.class);
					// startActivity(intent);
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
		//上传失败的话，就用原来的图片
		if(localBitmap!=null){
			headImageView.setImageBitmap(localBitmap);
		}else {
			headImageView.setImageDrawable(getResources().getDrawable(R.drawable.default_head_icon));
		}

	}

	/**
	 * 头像选择对话框
	 */
	private void showDownLoadDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("选择获取头像方式").setPositiveButton("本地相册", new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

			}
		}).setNegativeButton("拍照上传", new android.content.DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				// 判断存储卡是否可以用，可用进行存储
				if (hasSdcard()) {
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
				}
				startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

			}
		}).show();
		dialog.setCanceledOnTouchOutside(false);
	}

	// 剪切之后得到的图片
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				uriImg = uri;
				crop(uri);
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
				uriImg = Uri.fromFile(tempFile);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(RegistFinishActivity.this, "未找到存储卡，无法存储照片！", 0).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				bitmap = data.getParcelableExtra("data");
				this.headImageView.setImageBitmap(UiHelper.CircleImageView(bitmap, 2));
				if (tempFile != null && tempFile.exists()) {
					tempFile.delete();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 剪切图片
	 * 
	 * @function:
	 * @author:Jerry
	 * @date:2013-12-30
	 * @param uri
	 */
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (bitmap != null) {
			this.headImageView.setImageBitmap(UiHelper.CircleImageView(bitmap, 2));
		}
	}

	/*
	 * 上传图片
	 */
	public void upload() {

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir = Environment.getExternalStorageDirectory();
			// 得到一个路径，内容是sdcard的文件夹路径和名字
			String path = sdcardDir.getPath() + "/cardImages";
			File path1 = new File(path);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();
				setTitle("paht ok,path:" + path);
			}
		}
		// 构造方法1、2的参数
		filePathList = new ArrayList<String>();
		String aa = uriImg.toString();
		filePathList.add(getRealFilePath(this, uriImg));
		post = new HttpMultipartPost(RegistFinishActivity.this, filePathList);
		post.execute();
	}

	// 根据Uri获取到图片的路径
	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri, new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	// 上传头像完之后的操作
	public void handleHeadImg(String imgMess) {
		try {
			WBResponse response = MessageUtil.JsonStrToWBResponse(imgMess, HeadImgRes.class);
			if(response!=null){
			String msgString = response.getMsg();
			HeadImgRes headImgRes = (HeadImgRes) response.getRespBody();
			if (headImgRes != null&&headImgRes.getFileUrls().size() > 0&&headImgRes.getFileUrls().get(0)!=null) {
					headImgUrlString = UrlManager.uploadToUrlServer + headImgRes.getFileUrls().get(0);
				if (null != AppContext.getAppContext()) {
					AppContext.getAppContext().setHeadImgUrl(headImgUrlString);
				}
				refresh();// 刷新
			}
		}else{
			
		}
	} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void setLocalBitmap(){
		if(localBitmap!=null){
			headImageView.setImageBitmap(localBitmap);
		}else {
			headImageView.setImageDrawable(getResources().getDrawable(R.drawable.default_head_icon));
		}
		
	}
}
