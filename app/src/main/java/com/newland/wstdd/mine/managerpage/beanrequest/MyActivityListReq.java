package com.newland.wstdd.mine.managerpage.beanrequest;

import java.util.List;

import com.newland.wstdd.common.bean.TddActivity;

/**
 * @author Administrator
 */
public class MyActivityListReq {


    private String page;//默认从0开始
    private String row;//行数
    private String filterVaule;//过滤条件（“all” 全部活动  “sign”报名中 “closed”报名已截止 “filterActivityType”按活动类型）默认传“all”
    private String type;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getFilterVaule() {
        return filterVaule;
    }

    public void setFilterVaule(String filterVaule) {
        this.filterVaule = filterVaule;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
