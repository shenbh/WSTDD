package com.newland.wstdd.mine.twocode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.wstdd.R;
import com.newland.wstdd.common.base.BaseFragment;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.UrlManager;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.BitmapImageUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.view.LoadingDialog;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.mine.twocode.beanrequest.TwoCodePayReq;
import com.newland.wstdd.mine.twocode.beanresponse.TwoCodePayRes;
import com.newland.wstdd.mine.twocode.beanresponse.WeiXinCodeImgRes;
import com.newland.wstdd.mine.twocode.handle.TwoCodeWeiXinHandle;
import com.newland.wstdd.netutils.BaseMessageMgr;
import com.newland.wstdd.netutils.HandleNetMessageMgr;
import com.newland.wstdd.netutils.MessageUtil;
import com.newland.wstdd.netutils.WBResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 我的-支付二维码-微信界面
 * 
 * @author H81 2015-11-9
 * 
 */
@SuppressLint("ValidFragment")
public class WeiXinFragment extends BaseFragment implements OnPostListenerInterface {
	private DisplayImageOptions options; // 设置图片显示相关参数
	/**
	 * 头像上传
	 */
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private Bitmap localBitmap;
	private Bitmap bitmap;
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "weixincode.png";
	private File tempFile;
	private List<String> filePathList;// 保存本地图片的地址
	private Uri uriImg;// 获取到了图片的Uri
	HttpMultipartPostWeiXinFragment post;//
	private Context context;
	String headImgUrlString = null;// 头像地址
	// 控件
	private ImageView addTwoCode;// 添加二维码的图片 带有监听事件
	private TextView changeTextView;// 内容需要根据是否有二维码图片进行变化的
	private TextView modifyTextView;// 修改的标签
	// 服务器相关的
	private TwoCodePayRes twoCodePayRes;
	private TwoCodeWeiXinHandle handler = new TwoCodeWeiXinHandle(this);

	@SuppressLint("ValidFragment")
	public WeiXinFragment(Context context, Bitmap bitmap) {
		this.context = context;
		this.bitmap = bitmap;
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_weixin, container, false);
		initView(view);// 初始化控件
		return view;
	}

	private void initView(View view) {
		addTwoCode = (ImageView) view.findViewById(R.id.fragment_weixin_addimg);
		addTwoCode.setOnClickListener(new MyClick());
		changeTextView = (TextView) view.findViewById(R.id.fragment_weixin_text_change);
		changeTextView.setOnClickListener(new MyClick());
		modifyTextView = (TextView) view.findViewById(R.id.fragment_weixin_modify_tv);
		modifyTextView.setOnClickListener(new MyClick());
		if (bitmap != null) {
			addTwoCode.setImageBitmap(bitmap);
			localBitmap = bitmap;
		} else {
			refresh();
		}
	}

	// 当图片上传成功之后需要把对应的信息也传给服务器
	@Override
	public void refresh() {
		super.refreshDialog();
		progressDialog.setCancelable(false);
		try {
			new Thread() {
				public void run() {

					// 需要发送一个request的对象进行请求
					TwoCodePayReq reqInfo = new TwoCodePayReq();
					reqInfo.setQqPay(headImgUrlString);
					reqInfo.setAliPay(((TwoDimensionCodeActivity) context).getZhifubaoUrl());
					BaseMessageMgr mgr = new HandleNetMessageMgr();
					RetMsg<TwoCodePayRes> ret = mgr.getTwoCodePayInfo(reqInfo, AppContext.getAppContext().getUserId());// 泛型类，
					Message message = new Message();
					message.what = TwoCodeWeiXinHandle.TWO_CODE_WEIXIN;// 设置死
					// 访问服务器成功 1 否则访问服务器失败
					if (ret.getCode() == 1) {
						twoCodePayRes = (TwoCodePayRes) ret.getObj();
						message.obj = twoCodePayRes;
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	/**
	 * 自定义的监听事件
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyClick implements OnClickListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			// 添加添加二维码的图片
			case R.id.fragment_weixin_addimg:
				showDownLoadDialog();
				break;
			case R.id.fragment_weixin_modify_tv:
				showDownLoadDialog();
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 头像选择对话框
	 */
	private void showDownLoadDialog() {
		AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("温馨提示").setMessage("选择获取二维码方式").setPositiveButton("本地相册", new android.content.DialogInterface.OnClickListener() {

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

	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
				uriImg = uri;
				String pathString = BitmapImageUtil.getRealFilePath(getActivity(), uriImg);
				bitmap = BitmapImageUtil.getBitmapFromLocal(pathString);
				addTwoCode.setImageBitmap(bitmap);
				;
				if (bitmap != null) {
					// 进行上传的操作
					upload();

				}

				// crop(uri);
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
				if (tempFile.exists()) {
					uriImg = Uri.fromFile(tempFile);
					String pathString = BitmapImageUtil.getRealFilePath(getActivity(), uriImg);
					bitmap = BitmapImageUtil.getBitmapFromLocal(pathString);
					addTwoCode.setImageBitmap(bitmap);
					;
					if (bitmap != null) {
						// 进行上传的操作
						
						upload();
					}
				} else {
					if (tempFile != null && tempFile.exists()) {
						tempFile.delete();
					}
					UiHelper.ShowOneToast(context, "未执行拍照操作!");
					addTwoCode.setImageBitmap(localBitmap);
				}

				// crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！", 0).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				localBitmap = data.getParcelableExtra("data");
				bitmap = localBitmap;
				this.addTwoCode.setImageBitmap(UiHelper.CircleImageView(localBitmap, 2));
				boolean delete = tempFile.delete();
				System.out.println("delete = " + delete);

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
				getActivity().setTitle("paht ok,path:" + path);
			}
		}
		// 构造方法1、2的参数
		filePathList = new ArrayList<String>();
		String aa = uriImg.toString();
		filePathList.add(BitmapImageUtil.getRealFilePath(getActivity(), uriImg));

		post = new HttpMultipartPostWeiXinFragment(getActivity(), this, filePathList);
		post.execute();
	}

	// 上传头像完之后的操作
	public void handleHeadImg(String imgMess) {
		WBResponse response = MessageUtil.JsonStrToWBResponse(imgMess, WeiXinCodeImgRes.class);
		if(response!=null){
		String msgString = response.getMsg();
		WeiXinCodeImgRes headImgRes = (WeiXinCodeImgRes) response.getRespBody();
		if(headImgRes!=null&&headImgRes.getFileUrls().size()>0&&"upload success!".equals(msgString)){
		headImgUrlString = headImgRes.getFileUrls().get(0);
		if(bitmap!=null){
			localBitmap = bitmap;
		}
		if (tempFile != null && tempFile.exists()) {
			tempFile.delete();
		}
		refresh();// 刷新
		}
	}
}

	@SuppressLint("NewApi")
	@Override
	public void OnHandleResultListener(Object obj, int responseId) {
		try {
			switch (responseId) {
			case TwoCodeWeiXinHandle.TWO_CODE_WEIXIN:
				
				twoCodePayRes = (TwoCodePayRes) obj;
				// qqPay == 2015-12-02/1449044077335.png
				if (twoCodePayRes != null) {
					headImgUrlString = twoCodePayRes.getQqPay();
					if (headImgUrlString != null && !"".equals(headImgUrlString)) {
						// 显示布局控件
						modifyTextView.setVisibility(View.VISIBLE);
						changeTextView.setText("我的微信收款码");
						changeTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.textview_weixintwocode_radius_shap));

						// 根据url下载图片
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

						// ImageLoader.getInstance().displayImage(headImgUrlString,
						// addTwoCode, options);

						ImageLoader.getInstance().displayImage(UrlManager.uploadToUrlServer + headImgUrlString, addTwoCode, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
								// TODO Auto-generated method stub
								// TODO Auto-generated method stub
								if(localBitmap!=null){
									addTwoCode.setImageBitmap(localBitmap);
								}else {
									addTwoCode.setImageDrawable(getResources().getDrawable(R.drawable.two_code));
								}
							}

							@Override
							public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
								// TODO Auto-generated method stub
								((TwoDimensionCodeActivity) context).setWeixinBitmap(arg2);
								((TwoDimensionCodeActivity) context).setWeixinUrl(headImgUrlString);
								localBitmap = arg2;
								if (progressDialog != null) {
									progressDialog.setContinueDialog(false);
								}
								// UiHelper.ShowOneToast(getActivity(),
								// "完成微信二维码设置");

							}

							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
								// TODO Auto-generated method stub
								// TODO Auto-generated method stub
								if(localBitmap!=null){
									addTwoCode.setImageBitmap(localBitmap);
								}else {
									addTwoCode.setImageDrawable(getResources().getDrawable(R.drawable.two_code));
								}
							}
						});

					} else {
						if (progressDialog != null) {
							progressDialog.setContinueDialog(false);
						}
						addTwoCode.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.two_code));
						modifyTextView.setVisibility(View.GONE);
						changeTextView.setText("怎么添加？");
						changeTextView.setBackground(null);
					}
				}else{
					if (progressDialog != null) {
						progressDialog.setContinueDialog(false);
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

	@SuppressLint("NewApi")
	@Override
	public void OnFailResultListener(String mess) {
		if (progressDialog != null) {
			progressDialog.setContinueDialog(false);
			UiHelper.ShowOneToast(getActivity(), mess);
		}
		// TODO Auto-generated method stub
		addTwoCode.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.two_code));
		modifyTextView.setVisibility(View.GONE);
		changeTextView.setText("怎么添加？");
		changeTextView.setBackground(null);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (bitmap != null) {
			bitmap.recycle();
		}
		if(localBitmap!=null){
			localBitmap.recycle();
		}
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if (bitmap != null) {
			bitmap.recycle();
		}
		if(localBitmap!=null){
			localBitmap.recycle();
		}
	}

	public void setLocalBitmap(){
		addTwoCode.setImageBitmap(localBitmap);
	}
}
