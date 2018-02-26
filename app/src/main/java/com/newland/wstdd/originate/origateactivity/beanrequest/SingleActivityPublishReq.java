package com.newland.wstdd.originate.origateactivity.beanrequest;

import com.newland.wstdd.common.bean.TddActivity;
import com.newland.wstdd.mine.beanresponse.TddUserCertificate;

/**
 * 单个活动发布的请求信息
 * @author Administrator
 *
 */
public class SingleActivityPublishReq {
	private TddActivity tddActivity;//活动对象
	private TddUserCertificate tddUserCertificate;//个人信息对象
	public TddActivity getTddActivity() {
		return tddActivity;
	}
	public void setTddActivity(TddActivity tddActivity) {
		this.tddActivity = tddActivity;
	}
	public TddUserCertificate getTddUserCertificate() {
		return tddUserCertificate;
	}
	public void setTddUserCertificate(TddUserCertificate tddUserCertificate) {
		this.tddUserCertificate = tddUserCertificate;
	}
	
	
	
}
