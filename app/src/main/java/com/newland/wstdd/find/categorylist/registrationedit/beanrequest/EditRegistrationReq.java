package com.newland.wstdd.find.categorylist.registrationedit.beanrequest;

import java.util.List;

import com.newland.wstdd.find.categorylist.registrationedit.beaninfo.MainSignAttr;

/**
 * 修改报名信息的请求消息
 *
 * @author Administrator
 */
public class EditRegistrationReq {
    private String joinStatus;//1,参加  2,不参加
    private String phoneNum;//报名者phoneNum
    private List<MainSignAttr> mainSignAttr;//报名信息map  MainSignAttr

    public String getJoinStatus() {
        return joinStatus;
    }

    public void setJoinStatus(String joinStatus) {
        this.joinStatus = joinStatus;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public List<MainSignAttr> getMainSignAttr() {
        return mainSignAttr;
    }

    public void setMainSignAttr(List<MainSignAttr> mainSignAttr) {
        this.mainSignAttr = mainSignAttr;
    }


}
