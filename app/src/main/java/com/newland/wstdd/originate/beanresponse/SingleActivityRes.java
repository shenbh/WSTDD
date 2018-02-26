package com.newland.wstdd.originate.beanresponse;

import java.io.Serializable;
import java.util.List;

import com.newland.wstdd.common.bean.DetailSecretaryTel;
import com.newland.wstdd.common.bean.TddActivity;

/**
 * 单个  活动查询
 * @author Administrator
 *
 */
public class SingleActivityRes implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TddActivity tddActivity;//获取到的活动对象
	private String isLike;//是否点过赞"0"否“1”是
	private String iscollect;//是否收藏过"0"否“1”是
	private List<DetailSecretaryTel> tddSecretaryList;//团秘对象DetailSecretaryTel列表
	private String activityStatue;//活动状态“none”：不限人数
	//“full”：大于限制人数人数
	//“timeLater”：截止时间晚于现在
	//“closed”：已关闭
	//“closedSign”：已关闭报名
	
	private String need_public_signinfo;//报名信息是否公开“0”不公开“1”公开
	private String userSignstate;//用户是否报名是：”sign“ 否：” noSign “
	private String signId;//报名对象TddActivitySignVo的Id
	private int joinStatus;//报名状态  1.参加  2.不参加
	private int signRole;//	报名用户角色 1.团长 2.团秘 9.团员	Int,若userSignstate显示不参加，则null
	private String isLeader;//是否是团大 true/false
	public TddActivity getTddActivity() {
		return tddActivity;
	}
	public void setTddActivity(TddActivity tddActivity) {
		this.tddActivity = tddActivity;
	}
	public String getIsLike() {
		return isLike;
	}
	public void setIsLike(String isLike) {
		this.isLike = isLike;
	}
	public String getIscollect() {
		return iscollect;
	}
	public void setIscollect(String iscollect) {
		this.iscollect = iscollect;
	}
	public List<DetailSecretaryTel> getTddSecretaryList() {
		return tddSecretaryList;
	}
	public void setTddSecretaryList(List<DetailSecretaryTel> tddSecretaryList) {
		this.tddSecretaryList = tddSecretaryList;
	}
	public String getActivityStatue() {
		return activityStatue;
	}
	public void setActivityStatue(String activityStatue) {
		this.activityStatue = activityStatue;
	}
	public String getNeed_public_signinfo() {
		return need_public_signinfo;
	}
	public void setNeed_public_signinfo(String need_public_signinfo) {
		this.need_public_signinfo = need_public_signinfo;
	}
	public String getUserSignstate() {
		return userSignstate;
	}
	public void setUserSignstate(String userSignstate) {
		this.userSignstate = userSignstate;
	}
	public String getSignId() {
		return signId;
	}
	public void setSignId(String signId) {
		this.signId = signId;
	}
	public int getJoinStatus() {
		return joinStatus;
	}
	public void setJoinStatus(int joinStatus) {
		this.joinStatus = joinStatus;
	}
	public int getSignRole() {
		return signRole;
	}
	public void setSignRole(int signRole) {
		this.signRole = signRole;
	}
	public String getIsLeader() {
		return isLeader;
	}
	public void setIsLeader(String isLeader) {
		this.isLeader = isLeader;
	}
	
}
