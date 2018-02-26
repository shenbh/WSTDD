package com.newland.wstdd.common.bean;

import java.io.Serializable;

/**
 * TddActivity domain对象
 */
public class TddActivity implements Serializable {

	/**
	 * @fields activityId 活动标识
	 */
	private java.lang.String activityId;
	/**
	 * 活动的参加对象的id
	 */
	private String signId;
	/**
	 * @fields userId 发布用户标识
	 */
	private java.lang.String userId;
	/**
	 * @fields activityTitle 标题
	 */
	private java.lang.String activityTitle;
	/**
	 * @fields sponsor 主办方
	 */
	private java.lang.String sponsor;
	/**
	 * @fields activityTime 活动时间
	 */
	private java.lang.String activityTime;
	/**
	 * @fields activityContent 活动内容
	 */
	private java.lang.String activityContent;
	/**
	 * @fields activityAddress 活动地址
	 */
	private java.lang.String activityAddress;
	/**
	 * @fields activityDescription 活动说明
	 */
	private java.lang.String activityDescription;
	/**
	 * @fields offTime 截止时间
	 */
	private java.lang.String offTime;
	/**
	 * @fields limitPerson 限制人数  0为不限制
	 */
	private java.lang.Integer limitPerson;
	/**
	 * @fields signAttr 报名属性
	 */
	private java.lang.String signAttr;
	/**
	 * @fields accompanyAttr 随行人员报名信息
	 */
	private java.lang.String accompanyAttr;
	/**
	 * @fields needAudit 是否需要审核  1.需要  2.不需要
	 */
	private java.lang.Integer needAudit;
	/**
	 * @fields needAllInfo 是否需要随行人员信息  1需要 2不需要
	 */
	private java.lang.Integer needAllInfo;
	/**
	 * @fields image1 图片一，封面图片
	 */
	private java.lang.String image1;
	/**
	 * @fields image2 图片二
	 */
	private java.lang.String image2;
	/**
	 * @fields image3 图片三
	 */
	private java.lang.String image3;
	/**
	 * @fields image3 活动图片
	 */
	private java.lang.String image;
	/**
	 * @fields status 状态  1.进行中 2.已满员 3.已结束 4.已取消
	 */
	private java.lang.Integer status;
	/**
	 * @fields creatTime 创建时间
	 */
	private java.lang.String createTime;
	/**
	 * @fields payType 费用说明  1.无  2.团大买单 3.AA  4. 性别分类
	 */
	private java.lang.Integer payType;
	/**
	 * @fields feeAdult 成人金额 ，单位：分(团购活动的 售价)
	 */
	private java.lang.Integer feeAdult;
	/**
	 * @fields feeKid 儿童金额，单位：分（团购活动原价）
	 */
	private java.lang.Integer feeKid;
	/**
	 * @fields lng 经度
	 */
	private java.lang.Float lng;
	/**
	 * @fields lat 纬度
	 */
	private java.lang.Float lat;
	/**
	 * @fields needSelectActivityItem 是否允许用户选择活动项
	 */
	private java.lang.Integer needSelectActivityItem;
	/**
	 * @fields userName 发布者姓名
	 */
	private java.lang.String userName;
	/**
	 * @fields nickName 发布者昵称
	 */
	private java.lang.String nickName;
	/**
	 * @fields headimgurl 团长头像
	 */
	private java.lang.String headimgurl;
	/**
	 * @fields userName 发布者联系电话
	 */
	private java.lang.String userMobilePhone;
	/**
	 * @fields viewCount 浏览人次
	 */
	private java.lang.Integer viewCount;
	/**
	 * @fields signCount 报名人数
	 */
	private java.lang.Integer signCount;
	/**
	 * @fields joinCount 参加人数
	 */
	private java.lang.Integer joinCount;
	/**
	 * @fields likeCount 点赞次数
	 */
	private java.lang.Long likeCount;
	/**
	 * @fields favoriteCount 收藏次数
	 */
	private java.lang.Long favoriteCount;
	/**
	 * @fields shareCount 分享次数
	 */
	private java.lang.Long shareCount;
	/**
	 * @fields shareScope 计划分享范围  1.申请公开   2.团员可见   3.个人分享
	 */
	private java.lang.Integer shareScope;
	/**
	 * @fields needPublicSigninfo 是否公开报名情况 1.公开 2不公开
	 */
	private java.lang.Integer needPublicSigninfo;
	/**
	 * @fields activityType 活动分类:0.高级 1.讲座 2.通知 3.会议 4.运动 5.招聘 6.旅游 7.众筹 8.赛事 9.聚会 10.投票 11.活动 12.答题 31.高级快捷活动 32.一句话 33.一张图片 34.一段语音  50.团购 99.微店
	 */
	private java.lang.Integer activityType;
	/**
	 * @fields voice 语音
	 */
	private java.lang.String voice;
	/**
	 * @fields voiceDuration 语音时长
	 */
	private java.lang.Integer voiceDuration;
	/**
	 * @fields tags 活动标签
	 */
	private java.lang.String tags;
	/**
	 * @fields needPay 是否需要缴费 1.缴费 2.不缴费
	 */
	private java.lang.Integer needPay;
	/**
	 * @fields subType 发起来源： 1.微信
	 */
	private java.lang.Integer subType;
	/**
	 * 评论数
	 */
	private Integer commentCount;
	/**
	 * @fields isPaySelf 是否线下支付 1是 2否
	 */
	private java.lang.Short isPaySelf;
	/**
	 * @fields isPayOnline 是否在线支付 1是 2否
	 */
	private java.lang.Short isPayOnline;
	/**
	 * @fields deliverTime 发货时间
	 */
	private java.lang.String deliverTime;
	/**
	 * @fields isPickup 是否可自提  1.自提  2无自提
	 */
	private java.lang.Short isPickup;
	/**
	 * @fields isSelfConnect 是否电话联系 1.是 2否
	 */
	private java.lang.Short isSelfConnect;
	/**
	 * @fields isDeliver 是否可快递 1.是 2否
	 */
	private java.lang.Short isDeliver;
	/**
	 * @fields isSignleItem 是否为单个宝贝团购  1.只有一个宝贝  2.有多个宝贝
	 */
	private java.lang.Short isSignleItem;
	/**
	 * @fields limitType 人数限制类型， 1.主活动限制 2.活动项限制
	 */
	private java.lang.Short limitType;
	/**
	 * @fields province 有效省份
	 */
	private java.lang.String province;
	/**
	 * @fields city 有效城市
	 */
	private java.lang.String city;
	/**
	 * @fields publicWeight 公共活动权重
	 */
	private java.lang.Short publicWeight;
	/**
	 * @fields sendAwardStatus 发放奖励状态 1.未发 2已发
	 */
	private java.lang.Short sendAwardStatus;
	/**
	 * @fields signiinId 签到标识
	 */
	private java.lang.String signiinId;
	/**
	 * @fields onlySigniinJoin 1.签到人员才可参与 2.无限制
	 */
	private java.lang.Short onlySigniinJoin;
	/**
	 * @fields needFollow 是否需要关注 1.需要关注 2.不需要关注
	 */
	private java.lang.Short needFollow;
	
	
	private java.lang.String shareUrl;
	/**
	 * 分享图片
	 */
	private java.lang.String shareImg;
	/**
	 * 分享简介
	 */
	private java.lang.String shareContent;
	
	/**
	 * 报名用户角色 1.团大 2.团秘 9.团员
	 */
	private java.lang.Integer signRole	;
	/**
	 * 时间
	 * @return
	 */
	public java.lang.String friendActivityTime;
	
	
	public java.lang.String getActivityId() {
		return activityId;
	}
	public void setActivityId(java.lang.String activityId) {
		this.activityId = activityId;
	}
	public java.lang.String getUserId() {
		return userId;
	}
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	public java.lang.String getActivityTitle() {
		return activityTitle;
	}
	public void setActivityTitle(java.lang.String activityTitle) {
		this.activityTitle = activityTitle;
	}
	public java.lang.String getSponsor() {
		return sponsor;
	}
	public void setSponsor(java.lang.String sponsor) {
		this.sponsor = sponsor;
	}
	public java.lang.String getActivityTime() {
		return activityTime;
	}
	public void setActivityTime(java.lang.String activityTime) {
		this.activityTime = activityTime;
	}
	public java.lang.String getActivityContent() {
		return activityContent;
	}
	public void setActivityContent(java.lang.String activityContent) {
		this.activityContent = activityContent;
	}
	public java.lang.String getActivityAddress() {
		return activityAddress;
	}
	public void setActivityAddress(java.lang.String activityAddress) {
		this.activityAddress = activityAddress;
	}
	public java.lang.String getActivityDescription() {
		return activityDescription;
	}
	public void setActivityDescription(java.lang.String activityDescription) {
		this.activityDescription = activityDescription;
	}
	public java.lang.String getOffTime() {
		return offTime;
	}
	public void setOffTime(java.lang.String offTime) {
		this.offTime = offTime;
	}
	public java.lang.Integer getLimitPerson() {
		return limitPerson;
	}
	public void setLimitPerson(java.lang.Integer limitPerson) {
		this.limitPerson = limitPerson;
	}
	public java.lang.String getSignAttr() {
		return signAttr;
	}
	public void setSignAttr(java.lang.String signAttr) {
		this.signAttr = signAttr;
	}
	public java.lang.String getAccompanyAttr() {
		return accompanyAttr;
	}
	public void setAccompanyAttr(java.lang.String accompanyAttr) {
		this.accompanyAttr = accompanyAttr;
	}
	public java.lang.Integer getNeedAudit() {
		return needAudit;
	}
	public void setNeedAudit(java.lang.Integer needAudit) {
		this.needAudit = needAudit;
	}
	public java.lang.Integer getNeedAllInfo() {
		return needAllInfo;
	}
	public void setNeedAllInfo(java.lang.Integer needAllInfo) {
		this.needAllInfo = needAllInfo;
	}
	public java.lang.String getImage1() {
		return image1;
	}
	public void setImage1(java.lang.String image1) {
		this.image1 = image1;
	}
	public java.lang.String getImage2() {
		return image2;
	}
	public void setImage2(java.lang.String image2) {
		this.image2 = image2;
	}
	public java.lang.String getImage3() {
		return image3;
	}
	public void setImage3(java.lang.String image3) {
		this.image3 = image3;
	}
	public java.lang.String getImage() {
		return image;
	}
	public void setImage(java.lang.String image) {
		this.image = image;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.lang.String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.lang.String createTime) {
		this.createTime = createTime;
	}
	public java.lang.Integer getPayType() {
		return payType;
	}
	public void setPayType(java.lang.Integer payType) {
		this.payType = payType;
	}
	public java.lang.Integer getFeeAdult() {
		return feeAdult;
	}
	public void setFeeAdult(java.lang.Integer feeAdult) {
		this.feeAdult = feeAdult;
	}
	public java.lang.Integer getFeeKid() {
		return feeKid;
	}
	public void setFeeKid(java.lang.Integer feeKid) {
		this.feeKid = feeKid;
	}
	public java.lang.Float getLng() {
		return lng;
	}
	public void setLng(java.lang.Float lng) {
		this.lng = lng;
	}
	public java.lang.Float getLat() {
		return lat;
	}
	public void setLat(java.lang.Float lat) {
		this.lat = lat;
	}
	public java.lang.Integer getNeedSelectActivityItem() {
		return needSelectActivityItem;
	}
	public void setNeedSelectActivityItem(java.lang.Integer needSelectActivityItem) {
		this.needSelectActivityItem = needSelectActivityItem;
	}
	public java.lang.String getUserName() {
		return userName;
	}
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}
	public java.lang.String getNickName() {
		return nickName;
	}
	public void setNickName(java.lang.String nickName) {
		this.nickName = nickName;
	}
	public java.lang.String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(java.lang.String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public java.lang.String getUserMobilePhone() {
		return userMobilePhone;
	}
	public void setUserMobilePhone(java.lang.String userMobilePhone) {
		this.userMobilePhone = userMobilePhone;
	}
	public java.lang.Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(java.lang.Integer viewCount) {
		this.viewCount = viewCount;
	}
	public java.lang.Integer getSignCount() {
		return signCount;
	}
	public void setSignCount(java.lang.Integer signCount) {
		this.signCount = signCount;
	}
	public java.lang.Integer getJoinCount() {
		return joinCount;
	}
	public void setJoinCount(java.lang.Integer joinCount) {
		this.joinCount = joinCount;
	}
	public java.lang.Long getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(java.lang.Long likeCount) {
		this.likeCount = likeCount;
	}
	public java.lang.Long getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(java.lang.Long favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	public java.lang.Long getShareCount() {
		return shareCount;
	}
	public void setShareCount(java.lang.Long shareCount) {
		this.shareCount = shareCount;
	}
	public java.lang.Integer getShareScope() {
		return shareScope;
	}
	public void setShareScope(java.lang.Integer shareScope) {
		this.shareScope = shareScope;
	}
	public java.lang.Integer getNeedPublicSigninfo() {
		return needPublicSigninfo;
	}
	public void setNeedPublicSigninfo(java.lang.Integer needPublicSigninfo) {
		this.needPublicSigninfo = needPublicSigninfo;
	}
	public java.lang.Integer getActivityType() {
		return activityType;
	}
	public void setActivityType(java.lang.Integer activityType) {
		this.activityType = activityType;
	}
	public java.lang.String getVoice() {
		return voice;
	}
	public void setVoice(java.lang.String voice) {
		this.voice = voice;
	}
	public java.lang.Integer getVoiceDuration() {
		return voiceDuration;
	}
	public void setVoiceDuration(java.lang.Integer voiceDuration) {
		this.voiceDuration = voiceDuration;
	}
	public java.lang.String getTags() {
		return tags;
	}
	public void setTags(java.lang.String tags) {
		this.tags = tags;
	}
	public java.lang.Integer getNeedPay() {
		return needPay;
	}
	public void setNeedPay(java.lang.Integer needPay) {
		this.needPay = needPay;
	}
	public java.lang.Integer getSubType() {
		return subType;
	}
	public void setSubType(java.lang.Integer subType) {
		this.subType = subType;
	}
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public java.lang.Short getIsPaySelf() {
		return isPaySelf;
	}
	public void setIsPaySelf(java.lang.Short isPaySelf) {
		this.isPaySelf = isPaySelf;
	}
	public java.lang.Short getIsPayOnline() {
		return isPayOnline;
	}
	public void setIsPayOnline(java.lang.Short isPayOnline) {
		this.isPayOnline = isPayOnline;
	}
	public java.lang.String getDeliverTime() {
		return deliverTime;
	}
	public void setDeliverTime(java.lang.String deliverTime) {
		this.deliverTime = deliverTime;
	}
	public java.lang.Short getIsPickup() {
		return isPickup;
	}
	public void setIsPickup(java.lang.Short isPickup) {
		this.isPickup = isPickup;
	}
	public java.lang.Short getIsSelfConnect() {
		return isSelfConnect;
	}
	public void setIsSelfConnect(java.lang.Short isSelfConnect) {
		this.isSelfConnect = isSelfConnect;
	}
	public java.lang.Short getIsDeliver() {
		return isDeliver;
	}
	public void setIsDeliver(java.lang.Short isDeliver) {
		this.isDeliver = isDeliver;
	}
	public java.lang.Short getIsSignleItem() {
		return isSignleItem;
	}
	public void setIsSignleItem(java.lang.Short isSignleItem) {
		this.isSignleItem = isSignleItem;
	}
	public java.lang.Short getLimitType() {
		return limitType;
	}
	public void setLimitType(java.lang.Short limitType) {
		this.limitType = limitType;
	}
	public java.lang.String getProvince() {
		return province;
	}
	public void setProvince(java.lang.String province) {
		this.province = province;
	}
	public java.lang.String getCity() {
		return city;
	}
	public void setCity(java.lang.String city) {
		this.city = city;
	}
	public java.lang.Short getPublicWeight() {
		return publicWeight;
	}
	public void setPublicWeight(java.lang.Short publicWeight) {
		this.publicWeight = publicWeight;
	}
	public java.lang.Short getSendAwardStatus() {
		return sendAwardStatus;
	}
	public void setSendAwardStatus(java.lang.Short sendAwardStatus) {
		this.sendAwardStatus = sendAwardStatus;
	}
	public java.lang.String getSigniinId() {
		return signiinId;
	}
	public void setSigniinId(java.lang.String signiinId) {
		this.signiinId = signiinId;
	}
	public java.lang.Short getOnlySigniinJoin() {
		return onlySigniinJoin;
	}
	public void setOnlySigniinJoin(java.lang.Short onlySigniinJoin) {
		this.onlySigniinJoin = onlySigniinJoin;
	}
	public java.lang.Short getNeedFollow() {
		return needFollow;
	}
	public void setNeedFollow(java.lang.Short needFollow) {
		this.needFollow = needFollow;
	}
	public java.lang.String getShareUrl() {
		return shareUrl;
	}
	public void setShareUrl(java.lang.String shareUrl) {
		this.shareUrl = shareUrl;
	}
	public java.lang.String getShareImg() {
		return shareImg;
	}
	public void setShareImg(java.lang.String shareImg) {
		this.shareImg = shareImg;
	}
	public java.lang.String getShareContent() {
		return shareContent;
	}
	public void setShareContent(java.lang.String shareContent) {
		this.shareContent = shareContent;
	}
	public java.lang.String getFriendActivityTime() {
		return friendActivityTime;
	}
	public void setFriendActivityTime(java.lang.String friendActivityTime) {
		this.friendActivityTime = friendActivityTime;
	}
	public String getSignId() {
		return signId;
	}
	public void setSignId(String signId) {
		this.signId = signId;
	}
	public java.lang.Integer getSignRole() {
		return signRole;
	}
	public void setSignRole(java.lang.Integer signRole) {
		this.signRole = signRole;
	}

	
	
	
}
