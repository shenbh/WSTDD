package com.newland.wstdd.mine.applyList.bean;

import java.io.Serializable;

import android.R.integer;

/**
 * 参加活动对象
 */
public class TddActivitySignVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String signId;// 报名标识1
	private String signUserId;// 报名用户标识2
	private String activityId;// 活动标识3
	private String signUserName;// 报名用户4
	private String signTime;// 报名时间5
	private int adultNum;// 随行成人人数6
	private int kidNum;// 随行儿童人数7
	private int totalJoinNum;// 总人数8
	private int joinStatus;// 报名状态 1.参加 2.不参加9
	private int auditStatus;// 审核状态 1.未审核 2.已通过 3.未通过10
	private String signNickName;// 报名用户昵称11
	private String signHeadimgurl;// 报名用户头像12
	private String wechatOpenId;// 微信openid13
	private String signUserPhone;// 报名用户电话14
	private int signRole;// 报名用户角色 1.团大 2.团秘 9.团员15
	private String signAttr;// json格式{"手机":"13222222222","姓名":"111111"}16
	private int payStatus;// 付款状态 1.未付款 2.已付款 3.已确认1默认为0 17
	private int totalFee;// 总费用，默认0 18
	private String salerId;// salerId 销售者标识 19
	private int deliverType;// 取货方式：1.邮寄 2.自提点自取 3.电话联系 20
	private String connectUserName;// 收货人姓名 21
	private String address;// 详细地址 22
	private String pickupAlias;// 自提点 23
	private String orderNo;// 订单编号 24
	private String message;// 留言 25
	private String payTime;// 付款时间 26
	private String confirmTime;// 确认时间 27
	private String payVoucher;// 付款凭证 28
	private int payType;// 付款方式 1.在线支付 2.货到付款 29
	private String activityTime;// 参加活动时间 30
	private String image1;// 活动封面 31
	private String status;// 活动状态32
	private String activityTitle;// 活动标题33
	private String createTime;// 活动创建时间34
	private String offTime;// 活动截止时间35
	private int limitPerson;// 活动限制人数36
	private int joinCount;// 活动参加数37
	private String headimgurl;// 团大头像38
	private String nickName;// 团大昵称39
	private String clientSignTimeString;// 参加时间40
	private java.lang.Short answerRight;
	private java.lang.Short answerErr;
	private java.lang.Float answerScore;
	private String auditStatusName;
	private String connectPhone;
	private String id;
	private String joinItems;
	private String offtimeName;
	private String payStatusIn;
	private int rank;
	private String signPerson;
	private String statusName;

	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
	}

	public String getSignUserId() {
		return signUserId;
	}

	public void setSignUserId(String signUserId) {
		this.signUserId = signUserId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getSignUserName() {
		return signUserName;
	}

	public void setSignUserName(String signUserName) {
		this.signUserName = signUserName;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public int getAdultNum() {
		return adultNum;
	}

	public void setAdultNum(int adultNum) {
		this.adultNum = adultNum;
	}

	public int getKidNum() {
		return kidNum;
	}

	public void setKidNum(int kidNum) {
		this.kidNum = kidNum;
	}

	public int getTotalJoinNum() {
		return totalJoinNum;
	}

	public void setTotalJoinNum(int totalJoinNum) {
		this.totalJoinNum = totalJoinNum;
	}

	public int getJoinStatus() {
		return joinStatus;
	}

	public void setJoinStatus(int joinStatus) {
		this.joinStatus = joinStatus;
	}

	public int getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getSignNickName() {
		return signNickName;
	}

	public void setSignNickName(String signNickName) {
		this.signNickName = signNickName;
	}

	public String getSignHeadimgurl() {
		return signHeadimgurl;
	}

	public void setSignHeadimgurl(String signHeadimgurl) {
		this.signHeadimgurl = signHeadimgurl;
	}

	public String getWechatOpenId() {
		return wechatOpenId;
	}

	public void setWechatOpenId(String wechatOpenId) {
		this.wechatOpenId = wechatOpenId;
	}

	public String getSignUserPhone() {
		return signUserPhone;
	}

	public void setSignUserPhone(String signUserPhone) {
		this.signUserPhone = signUserPhone;
	}

	public int getSignRole() {
		return signRole;
	}

	public void setSignRole(int signRole) {
		this.signRole = signRole;
	}

	public String getSignAttr() {
		return signAttr;
	}

	public void setSignAttr(String signAttr) {
		this.signAttr = signAttr;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public String getSalerId() {
		return salerId;
	}

	public void setSalerId(String salerId) {
		this.salerId = salerId;
	}

	public int getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(int deliverType) {
		this.deliverType = deliverType;
	}

	public String getConnectUserName() {
		return connectUserName;
	}

	public void setConnectUserName(String connectUserName) {
		this.connectUserName = connectUserName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPickupAlias() {
		return pickupAlias;
	}

	public void setPickupAlias(String pickupAlias) {
		this.pickupAlias = pickupAlias;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getPayVoucher() {
		return payVoucher;
	}

	public void setPayVoucher(String payVoucher) {
		this.payVoucher = payVoucher;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOffTime() {
		return offTime;
	}

	public void setOffTime(String offTime) {
		this.offTime = offTime;
	}

	public int getLimitPerson() {
		return limitPerson;
	}

	public void setLimitPerson(int limitPerson) {
		this.limitPerson = limitPerson;
	}

	public int getJoinCount() {
		return joinCount;
	}

	public void setJoinCount(int joinCount) {
		this.joinCount = joinCount;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getClientSignTimeString() {
		return clientSignTimeString;
	}

	public void setClientSignTimeString(String clientSignTimeString) {
		this.clientSignTimeString = clientSignTimeString;
	}

	public java.lang.Short getAnswerRight() {
		return answerRight;
	}

	public void setAnswerRight(java.lang.Short answerRight) {
		this.answerRight = answerRight;
	}

	public java.lang.Short getAnswerErr() {
		return answerErr;
	}

	public void setAnswerErr(java.lang.Short answerErr) {
		this.answerErr = answerErr;
	}

	public java.lang.Float getAnswerScore() {
		return answerScore;
	}

	public void setAnswerScore(java.lang.Float answerScore) {
		this.answerScore = answerScore;
	}

	public String getAuditStatusName() {
		return auditStatusName;
	}

	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}

	public String getConnectPhone() {
		return connectPhone;
	}

	public void setConnectPhone(String connectPhone) {
		this.connectPhone = connectPhone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJoinItems() {
		return joinItems;
	}

	public void setJoinItems(String joinItems) {
		this.joinItems = joinItems;
	}

	public String getOfftimeName() {
		return offtimeName;
	}

	public void setOfftimeName(String offtimeName) {
		this.offtimeName = offtimeName;
	}

	public String getPayStatusIn() {
		return payStatusIn;
	}

	public void setPayStatusIn(String payStatusIn) {
		this.payStatusIn = payStatusIn;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getSignPerson() {
		return signPerson;
	}

	public void setSignPerson(String signPerson) {
		this.signPerson = signPerson;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
