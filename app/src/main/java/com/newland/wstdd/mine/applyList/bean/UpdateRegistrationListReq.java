package com.newland.wstdd.mine.applyList.bean;

import java.util.List;

/**
 * 更新报名人员列表
 *
 * @author Administrator
 */
public class UpdateRegistrationListReq {
    private List<TddActivitySignVo> tddActivitySigns;

    public List<TddActivitySignVo> getTddActivitySigns() {
        return tddActivitySigns;
    }

    public void setTddActivitySigns(List<TddActivitySignVo> tddActivitySigns) {
        this.tddActivitySigns = tddActivitySigns;
    }


}
