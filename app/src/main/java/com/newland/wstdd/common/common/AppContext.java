package com.newland.wstdd.common.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.newland.wstdd.R;
import com.newland.wstdd.common.SharePreferenceUtil.SharePreferenceUtil;
import com.newland.wstdd.common.base.BaseFragment;
import com.newland.wstdd.common.bean.TddAdvCfgVo;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 利用Application进行程序全局变量的初始化
 * 
 * @author Administrator
 * 
 */
@SuppressLint("Recycle")
public class AppContext extends Application {
	public static IWXAPI api;
	public static String CASH_DIR_APK = Environment.getExternalStorageDirectory() + "/newland/WBBusiness/apk/";// 缓存spk地址
	private List<Activity> activities = new ArrayList<Activity>();
	private static AppContext appContext;
	private Context context;
	private Activity currentActivity;
	private FragmentTransaction transaction;// FragmentTransaction对fragment进行添加,移除,替换,以及执行其他动作。
	
	/**-----------需要保持的数据    start-------*/
	private String userId;//保存用户的ID 1
	private String userName;//真实姓名 2
	private String headImgUrl;//第三方头像 3
	private String nickName;//昵称 4
	private String mobilePhone;//手机号 5
	private String userPwd;//保存用户的密码 6
	private String sex;//性别 7
	private String email;//邮箱 8
	private String identity;//身份证 9
	private String cerStatus;//认证状态   1.未认证 30.初步认证  90.身份认证  10
	private String wechat_union_id;	//微信开放平台标识	没绑定 则为null  11
	private String qq_open_id;	//QQ统一认证标识	没绑定 则为null  12
	private String weibo_open_id;	//微博认证ID	没绑定 则为null  13
	private String myQucode;	//我的二维码字符串  14
	private List<String> tags = new ArrayList<String>();//标签数组ps:[登山、电影]  15
	private List<String> searchTags = new ArrayList<String>();//标签数组ps:[登山、电影]  15
    private String myAcNum;	     //   我发起的活动数  16
    private String mySignAcNum;	//我参加的活动数  17
    private String myFavAcNum;	//我收藏的活动数  18
	//保留的全局变量
	private String openId;//第三方的ID  就是根据什么登录的时候   19
	private String platForm;//第三方平台  就是哪一个第三方的平台  20
	private String myTags;//我的兴趣标签
	private String isLogin;//判断是否已经登录了  true为登录   false  为没有登录
	/**-----------需要保持的数据    end-------*/
	
	
	private String messUpDatetime;//上拉刷新保存的时间  key
	private String versionCode;//版本更新 版本号
	private List<TddAdvCfgVo> homeAds;
	
	public static final int REQUEST_LOGIN = 76637;
	public static final int REQUEST_APPBAR = 10102;
	
	public static final String ACTION_ADDRESS ="choose_address";

	public static String pageRow="10"; //每页显示10行

	
	private String tempUserNameString;//保存登录失败时的临时用户名/注册后的用户名
	public static AppContext getAppContext() {
		return appContext;
	}

	public Context getContext() {
		return context;
	}

	public Activity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(Activity currentActivity) {
		this.currentActivity = currentActivity;
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
		context = this.getBaseContext();
		appContext = this;
		//注册微信

		api = WXAPIFactory.createWXAPI(this, "wx525b6e6792c4c562",  true); 

		api.registerApp("wx525b6e6792c4c562");
		
		
		initImageLoader(getApplicationContext());
	}

	public static void setAppContext(AppContext appContext) {
		AppContext.appContext = appContext;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	
	
	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 用户注销
	 */
	public void logout() {
		if (null != currentActivity) {
			// LoginCache.clearLoginInfo(appContext);
			// UiHelper.showLogin(currentActivity);// 显示登录
			AppManager.getAppManager().finishActivity(currentActivity);
		}
	}

	/**
	 * 获取屏幕宽度
	 */
	public int getScreenWidth() {
		WindowManager windowManager = (WindowManager) appContext.getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = windowManager.getDefaultDisplay().getWidth();
		return width;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @return
	 */
	public int getScreenHeight() {
		WindowManager windowManager = (WindowManager) appContext.getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int height = windowManager.getDefaultDisplay().getHeight();
		return height;
	}

	@Override
	// 遍历存放在list中的Activity并退出
	public void onTerminate() {

		{
			super.onTerminate();
			for (Activity activity : activities) {
				activity.finish();
			}

			android.os.Process.killProcess(android.os.Process.myPid());
		}

	}

	public void addActivity(Activity activity) {
		activities.add(activity);
	}
	
	
	/**
	 * 改变当前的Fragment
	 */
	public void replaceFragment(FragmentManager fragmentmanager,
			int containerViewId, BaseFragment fragment) {
		transaction = fragmentmanager.beginTransaction();// 对fragment进行添加,移除,替换,以及执行其他动作。
		// 设置切换Fragment时的动画效果
		transaction.setCustomAnimations(R.anim.move_x_in, R.anim.move_x_out);// 利用xml弄出这个动画的效果																			// 先暗后明的效果
		transaction.replace(containerViewId, fragment);// 使用的是FrameLayout当中容器，然后将fragment替换进来
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);// 只是设置了切换效果而已																		// 没用效果
		transaction.commit();// 记得提交 这样才有效果
		fragmentmanager.executePendingTransactions();// 立即执行事务
	}
	/**
	 * 改变当前的Fragment
	 */
	public void addFragment(FragmentManager fragmentmanager,
			int containerViewId, BaseFragment fragment) {
		transaction = fragmentmanager.beginTransaction();// 对fragment进行添加,移除,替换,以及执行其他动作。
		// 设置切换Fragment时的动画效果
		transaction.setCustomAnimations(R.anim.move_x_in, R.anim.move_x_out);// 利用xml弄出这个动画的效果
																				// 先暗后明的效果
		transaction.add(containerViewId, fragment);// 使用的是FrameLayout当中容器，然后将fragment替换进来
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);// 只是设置了切换效果而已
																				// 没用效果
		transaction.commit();// 记得提交 这样才有效果
		fragmentmanager.executePendingTransactions();// 立即执行事务
	}
	
	/** 初始化ImageLoader */
	public static void initImageLoader(Context context) {
		//缓存文件的目录
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache"); 
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽 
				.threadPoolSize(3) //线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
				.diskCacheSize(50 * 1024 * 1024)  // 50 Mb sd卡(本地)缓存的最大值
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 由原先的discCache -> diskCache
				.diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径  
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
				.writeDebugLogs() // Remove for release app
				.build();
		//全局初始化此配置  
		ImageLoader.getInstance().init(config);
	}

	
	/**
	 * get  set
	 * 
	 */
	public String getUserId() {
		//如果appcontext为null的话，这时候可以直接从sharepreference
		if(userId==null||"".equals(userId)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("userId");
		}
		return userId;
	}

	public void setUserId(String userId) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("userId")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("userId"))){
			(new SharePreferenceUtil(context)).saveMessToSp("userId", userId);
//		}
		this.userId = userId;
	}

	public String getUserName() {
		if(userName==null||"".equals(userName)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("userName");
		}
		
		return userName;
	}

	public void setUserName(String userName) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("userName")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("userName"))){
			(new SharePreferenceUtil(context)).saveMessToSp("userName", userName);
//		}
		this.userName = userName;
	}

	public String getHeadImgUrl() {
		if(headImgUrl==null||"".equals(headImgUrl)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("headImgUrl");
		}
		
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("headImgUrl")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("headImgUrl"))){
			(new SharePreferenceUtil(context)).saveMessToSp("headImgUrl", headImgUrl);
//		}
		this.headImgUrl = headImgUrl;
	}

	public String getNickName() {
		if(nickName==null||"".equals(nickName)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("nickName");
		}
		return nickName;
	}

	public void setNickName(String nickName) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("nickName")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("nickName"))){
			(new SharePreferenceUtil(context)).saveMessToSp("nickName", nickName);
//		}
		this.nickName = nickName;
	}

	public String getMobilePhone() {
		if(mobilePhone==null||"".equals(mobilePhone)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("mobilePhone");
		}
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("mobilePhone")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("mobilePhone"))){
			(new SharePreferenceUtil(context)).saveMessToSp("mobilePhone", mobilePhone);
//		}
		this.mobilePhone = mobilePhone;
	}

	public String getUserPwd() {
		if(userPwd==null||"".equals(userPwd)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("userPwd");
		}
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("userPwd")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("userPwd"))){
			(new SharePreferenceUtil(context)).saveMessToSp("userPwd", userPwd);
//		}
		this.userPwd = userPwd;
	}

	public String getSex() {
		if(sex==null||"".equals(sex)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("sex");
		}
		return sex;
	}

	public void setSex(String sex) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("sex")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("sex"))){
			(new SharePreferenceUtil(context)).saveMessToSp("sex", sex);
//		}
		this.sex = sex;
	}

	public String getEmail() {
		if(email==null||"".equals(email)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("email");
		}
		return email;
	}

	public void setEmail(String email) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("email")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("email"))){
			(new SharePreferenceUtil(context)).saveMessToSp("email", email);
//		}
		this.email = email;
	}

	public String getIdentity() {
		if(identity==null||"".equals(identity)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("identity");
		}
		return identity;
	}

	public void setIdentity(String identity) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("identity")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("identity"))){
			(new SharePreferenceUtil(context)).saveMessToSp("identity", identity);
//		}
		this.identity = identity;
	}

	public String getCerStatus() {
		if(cerStatus==null||"".equals(cerStatus)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("cerStatus");
		}
		return cerStatus;
	}

	public void setCerStatus(String cerStatus) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("cerStatus")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("cerStatus"))){
			(new SharePreferenceUtil(context)).saveMessToSp("cerStatus", cerStatus);
//		}
		this.cerStatus = cerStatus;
	}

	public String getWechat_union_id() {
		if(wechat_union_id==null||"".equals(wechat_union_id)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("wechat_union_id");
		}
		return wechat_union_id;
	}

	public void setWechat_union_id(String wechat_union_id) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("wechat_union_id")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("wechat_union_id"))){
			(new SharePreferenceUtil(context)).saveMessToSp("wechat_union_id", wechat_union_id);
//		}
		this.wechat_union_id = wechat_union_id;
	}

	public String getQq_open_id() {
		if(qq_open_id==null||"".equals(qq_open_id)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("qq_open_id");
		}
		return qq_open_id;
	}

	public void setQq_open_id(String qq_open_id) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("qq_open_id")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("qq_open_id"))){
			(new SharePreferenceUtil(context)).saveMessToSp("qq_open_id", qq_open_id);
//		}
		this.qq_open_id = qq_open_id;
	}

	public String getWeibo_open_id() {
		if(weibo_open_id==null||"".equals(weibo_open_id)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("weibo_open_id");
		}
		return weibo_open_id;
	}

	public void setWeibo_open_id(String weibo_open_id) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("weibo_open_id")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("weibo_open_id"))){
			(new SharePreferenceUtil(context)).saveMessToSp("weibo_open_id",weibo_open_id);
//		}
		
		this.weibo_open_id = weibo_open_id;
	}

	public String getMyQucode() {
		if(myQucode==null||"".equals(myQucode)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("myQucode");
		}
		return myQucode;
	}

	public void setMyQucode(String myQucode) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("myQucode")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("myQucode"))){
			(new SharePreferenceUtil(context)).saveMessToSp("myQucode", myQucode);
//		}
		
		this.myQucode = myQucode;
	}

	public List<String> getTags() {
		
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getMyAcNum() {
		if(myAcNum==null||"".equals(myAcNum)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("myAcNum");
		}
		return myAcNum;
	}

	public void setMyAcNum(String myAcNum) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("myAcNum")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("myAcNum"))){
			(new SharePreferenceUtil(context)).saveMessToSp("myAcNum", myAcNum);
//		}
		this.myAcNum = myAcNum;
	}

	public String getMySignAcNum() {
		if(mySignAcNum==null||"".equals(mySignAcNum)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("mySignAcNum");
		}
		return mySignAcNum;
	}

	public void setMySignAcNum(String mySignAcNum) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("mySignAcNum")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("mySignAcNum"))){
			(new SharePreferenceUtil(context)).saveMessToSp("mySignAcNum", mySignAcNum);
//		}
		this.mySignAcNum = mySignAcNum;
	}

	public String getMyFavAcNum() {
		if(myFavAcNum==null||"".equals(myFavAcNum)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("myFavAcNum");
		}
		return myFavAcNum;
	}

	public void setMyFavAcNum(String myFavAcNum) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("myFavAcNum")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("myFavAcNum"))){
			(new SharePreferenceUtil(context)).saveMessToSp("myFavAcNum", myFavAcNum);
//		}
		this.myFavAcNum = myFavAcNum;
	}

	public String getOpenId() {
		if(openId==null||"".equals(openId)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("openId");
		}
		return openId;
	}

	public void setOpenId(String openId) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("openId")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("openId"))){
			(new SharePreferenceUtil(context)).saveMessToSp("openId", openId);
//		}
		this.openId = openId;
	}

	public String getPlatForm() {
		if(platForm==null||"".equals(platForm)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("platForm");
		}
		return platForm;
	}

	public void setPlatForm(String platForm) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("platForm")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("platForm"))){
			(new SharePreferenceUtil(context)).saveMessToSp("platForm", platForm);
//		}
		this.platForm = platForm;
	}

	public String getIsLogin() {
		if(isLogin==null||"".equals(isLogin)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("isLogin");
		}
		return isLogin;
	}

	public void setIsLogin(String isLogin) {
//		if((new SharePreferenceUtil(context)).getMessFromSp("isLogin")==null||"".equals((new SharePreferenceUtil(context)).getMessFromSp("isLogin"))){
			(new SharePreferenceUtil(context)).saveMessToSp("isLogin", isLogin);
//		}
		this.isLogin = isLogin;
	}	

	
	public String getMyTags() {
		if(myTags==null||"".equals(myTags)){
			return  (new SharePreferenceUtil(context)).getMessFromSp("myTags");
		}
		return myTags;
	}

	public void setMyTags(String myTags) {
		(new SharePreferenceUtil(context)).saveMessToSp("myTags", myTags);
		this.myTags = myTags;
	}

	public String getMessUpDatetime() {
	
		return messUpDatetime;
	}

	public void setMessUpDatetime(String messUpDatetime) {
		this.messUpDatetime = messUpDatetime;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public List<TddAdvCfgVo> getHomeAds() {
		return homeAds;
	}

	public void setHomeAds(List<TddAdvCfgVo> homeAds) {
		this.homeAds = homeAds;
	}

	public String getTempUserNameString() {
		return tempUserNameString;
	}

	public void setTempUserNameString(String tempUserNameString) {
		this.tempUserNameString = tempUserNameString;
	}

	public List<String> getSearchTags() {
		return searchTags;
	}

	public void setSearchTags(List<String> searchTags) {
		this.searchTags = searchTags;
	}


	
	
		


	
}
