package com.newland.wstdd.common.bean;

/**
 * 7.	活动参加者明细对象TddActivityJoinPerson
 *
 * @author Administrator
 * 2015-12-7
 */
public class TddActivityJoinPerson {
    private String joinId;//参加者标识
    private String signId;//报名标识
    private String userName;//报名者姓名
    private String signAttr;//报名信息
    private int personType;//	参加者分类 1.主报名者  2.随行成人  3.随行儿童
    private String activityId;//报名活动标示

    public String getJoinId() {
        return joinId;
    }

    public void setJoinId(String joinId) {
        this.joinId = joinId;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSignAttr() {
        return signAttr;
    }

    public void setSignAttr(String signAttr) {
        this.signAttr = signAttr;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

}
