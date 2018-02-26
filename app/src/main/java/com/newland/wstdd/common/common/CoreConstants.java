package com.newland.wstdd.common.common;

import java.io.Serializable;
/**
 * 具体请求连接到具体的网址
 * @author Administrator
 *
 */
public class CoreConstants implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 请求URL连接的匹配号
	public final static String CONNECT_DELETE_USER = "0";//注册第一步
	public final static String CONNECT_REGIST_FIRST = "1";//注册第一步
	public final static String CONNECT_CHECK_CODE = "2";//获取短信验证码
	public final static String CONNECT_REGIST_SECOND = "3";//注册第二步
	public final static String CONNECT_MY_INTEREST = "4";//获取个人兴趣标签
	public final static String CONNECT_LOGIN = "5";//登入
	public final static String CONNECT_THIRD_LOGIN="6";//第三方登录
	public final static String CONNECT_BIND_LOGIN="7";//第三方登录
	public final static String CONNECT_FIND = "8";//发现
	public final static String CONNECT_ORIGINATE_SEARCH = "9";//首页搜索列表
	public final static String CONNECT_VERSION_INFO = "10";//版本信息
	public final static String CONNECT_MINE_PERSONINFO_SEARCH = "11";//个人信息获取
	public final static String CONNECT_MINE_EDITPERSON_INFO="12";//编辑个人信息
	public final static String CONNECT_MINE_RECEIPT_ADDRESS="13";//收获地址的获取
	public final static String CONNECT_MINE_UPDATE_ADDRESS="14";//更新地址
	public final static String CONNECT_MINE_SET_DEFAULTADDRESS="15";//设置收获地址
	public final static String CONNECT_MINE_DELETE_ADDRESS="16";//删除地址的操作
	public final static String CONNECT_FIND_HOT_LIST="17";//热门活动
	public final static String CONNECT_FIND_RECOMMEND_LIST="18";//推荐活动
	public final static String CONNECT_SINGLE_ACTIVITY="19";//单个活动查询
	public final static String CONNECT_FIND_CATEGORY="20";//发现列表
	public final static String CONNECT_SINGLE_ACTIVITY_PUBLISH="21";//单个活动发布
	public final static String CONNECT_SINGLE_ACTIVITY_EDIT="211";//编辑活动
	public final static String CONNECT_SUBMIT_REGISTRATION="22";//提交报名信息
	public final static String CONNECT_CANCEL_REGISTRATION="23";//取消报名信息
	public final static String CONNECT_REGISTRATION_LIST = "24";//获取到报名人员列表
	public final static String CONNECT_UPDATE_REGISTRATION_LIST = "25";//更新报名人员列表
	public final static String CONNECT_OPENACTIVITY_PEOPLES = "26";//是否公开活动报名人数
	public final static String CONNECT_ONTDD_HOTRECOMMEND = "27";//是否上团大大热点推荐
	public final static String CONNECT_REGISTRATION_STATE = "28";//报名是否需要审核
	public final static String CONNECT_REGISTRATION_CHECK = "29";//报名是否需要审核	
	public final static String CONNECT_COLLECT = "30";//收藏、取消收藏
	public final static String CONNECT_LIKE = "31";//点赞、取消点赞
	public final static String CONNECT_ISLIKEANDCOLLECT="32";//活动是否点赞收藏
	public final static String CONNECT_MYACTIVITY_LIST="33";//我发起，参加，收藏的活动
	public final static String CONNECT_TWOCODE_IMG="34";//微信qq二维码的上传下载
	public final static String CONNECT_MINE_CHANGEPSW="35";//修改密码
	public final static String CONNECT_MINE_FEEDBACK="36";//意见反馈
	public final static String CONNECT_ORIGINATE_FRAGMENT="37";//发起界面上相关数据的请求
	public final static String CONNECT_MAIL_URL_TOSERVER="38";//发送邮箱地址  导出报名名单
	public final static String CONNECT_EDIT_REGISTRATION_INFO="39";//获取编辑报名信息
	public final static String CONNECT_OK_EDIT_REGISTRATION_INFO="40";//提交编辑好的报名信息
	public final static String CONNECT_MULTITEXT = "41";//群发消息
	public final static String CONNECT_LIKE_LIST = "42";//获取点赞列表
	public final static String CONNECT_REMOVE_BIND = "43";//解除绑定
	public final static String CONNECT_BIND = "44";//绑定
	public final static String CONNECT_DELETE_ACTIVITY = "45";//删除活动
	public final static String CONNECT_MODIFY_COVER = "46";//删除活动
	

	
}
