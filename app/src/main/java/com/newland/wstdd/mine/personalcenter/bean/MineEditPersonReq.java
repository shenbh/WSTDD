package com.newland.wstdd.mine.personalcenter.bean;

import com.newland.wstdd.mine.beanresponse.TddUserCertificate;

/**
 * 个人信息的编辑
 * @author Administrator
 *
 */
public class MineEditPersonReq {
	private TddUserCertificate tddUserCertificate;//编辑后封装的对象

	public TddUserCertificate getTddUserCertificate() {
		return tddUserCertificate;
	}

	public void setTddUserCertificate(TddUserCertificate tddUserCertificate) {
		this.tddUserCertificate = tddUserCertificate;
	}
	
}
