package com.newland.wstdd.mine.applyList.bean;

import java.io.Serializable;
import java.util.List;

import com.newland.wstdd.common.bean.TddActivityJoinPerson;

/**
 * 报名活动名单返回信息
 * @author Administrator
 *
 */
public class RegistrationListRes implements Serializable{
	/**
	 * 
	 */

	private List<TddActivitySignVo> signPassList;//报名通过名单,TddActivitySignVo对象列表
	private List<TddActivitySignVo> signNoPassList;//报名未通过名单,TddActivitySignVo对象列表
	private int signPassCount;//报名通过人数
	private int signNoPassCount;//报名未通过人数
	private int leaderCount;//团大团秘人数
	private int passMenberCount;//团员人数
	private int noPassMenberCount;//未通过人数
	private int noJoinCount;//不参加人数
	private int isLeader;//当前用户职位：1、	团员 2、	团大or团秘
	private String email;//登录者邮箱
//	private String isSign;//当前登录用户是否报名"true"/”false”
	private  List<TddActivitySignVo> allSign;//所有参加信息
	private List<TddActivityJoinPerson> tddActivityJoinPersons;//对象列表 
	public List<TddActivitySignVo> getSignPassList() {
		return signPassList;
	}
	public void setSignPassList(List<TddActivitySignVo> signPassList) {
		this.signPassList = signPassList;
	}
	public List<TddActivitySignVo> getSignNoPassList() {
		return signNoPassList;
	}
	public void setSignNoPassList(List<TddActivitySignVo> signNoPassList) {
		this.signNoPassList = signNoPassList;
	}
	public int getSignPassCount() {
		return signPassCount;
	}
	public void setSignPassCount(int signPassCount) {
		this.signPassCount = signPassCount;
	}
	public int getSignNoPassCount() {
		return signNoPassCount;
	}
	public void setSignNoPassCount(int signNoPassCount) {
		this.signNoPassCount = signNoPassCount;
	}
	public int getLeaderCount() {
		return leaderCount;
	}
	public void setLeaderCount(int leaderCount) {
		this.leaderCount = leaderCount;
	}
	public int getPassMenberCount() {
		return passMenberCount;
	}
	public void setPassMenberCount(int passMenberCount) {
		this.passMenberCount = passMenberCount;
	}
	public int getNoPassMenberCount() {
		return noPassMenberCount;
	}
	public void setNoPassMenberCount(int noPassMenberCount) {
		this.noPassMenberCount = noPassMenberCount;
	}
	public int getNoJoinCount() {
		return noJoinCount;
	}
	public void setNoJoinCount(int noJoinCount) {
		this.noJoinCount = noJoinCount;
	}
	public int getIsLeader() {
		return isLeader;
	}
	public void setIsLeader(int isLeader) {
		this.isLeader = isLeader;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<TddActivitySignVo> getAllSign() {
		return allSign;
	}
	public void setAllSign(List<TddActivitySignVo> allSign) {
		this.allSign = allSign;
	}
	public List<TddActivityJoinPerson> getTddActivityJoinPersons() {
		return tddActivityJoinPersons;
	}
	public void setTddActivityJoinPersons(List<TddActivityJoinPerson> tddActivityJoinPersons) {
		this.tddActivityJoinPersons = tddActivityJoinPersons;
	}
	
	
	
	
	
}
