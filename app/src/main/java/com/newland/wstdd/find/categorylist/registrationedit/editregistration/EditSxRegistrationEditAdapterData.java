package com.newland.wstdd.find.categorylist.registrationedit.editregistration;

import java.util.List;
import java.util.Map;

/**
 * 报名编辑的adapter的数据
 *
 * @author Administrator
 */
public class EditSxRegistrationEditAdapterData {

    private String name;// 姓名
    private String phone;// 手机
    // private List<String> childString;//子listview对应的数据
    private List<Map<String, String>> map;
    private List<Map<String, String>> inputTempList;
    private List<Map<String, String>> lastTempList;
    private boolean isShowRl1 = true;// 是否显示出来 rl1是否显示
    private boolean isShowRl2;// R2是否显示出来
    private boolean isShowListView;
    private int inParent;// 在父控件中的位置
    private EditSxRegistrationEditChildAdapter adapter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // public List<String> getChildString() {
    // return childString;
    // }
    // public void setChildString(List<String> childString) {
    // this.childString = childString;
    // }
    public List<Map<String, String>> getMap() {
        return map;
    }

    public void setMap(List<Map<String, String>> maplist) {
        this.map = maplist;
    }

    public boolean isShowRl1() {
        return isShowRl1;
    }

    public void setShowRl1(boolean isShowRl1) {
        this.isShowRl1 = isShowRl1;
    }

    public int getInParent() {
        return inParent;
    }

    public void setInParent(int inParent) {
        this.inParent = inParent;
    }

    public EditSxRegistrationEditChildAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(EditSxRegistrationEditChildAdapter adapter) {
        this.adapter = adapter;
    }

    public boolean isShowRl2() {
        return isShowRl2;
    }

    public void setShowRl2(boolean isShowRl2) {
        this.isShowRl2 = isShowRl2;
    }

    public boolean isShowListView() {
        return isShowListView;
    }

    public void setShowListView(boolean isShowListView) {
        this.isShowListView = isShowListView;
    }

    public List<Map<String, String>> getInputTempList() {
        return inputTempList;
    }

    public void setInputTempList(List<Map<String, String>> inputTempList) {
        this.inputTempList = inputTempList;
    }

    public List<Map<String, String>> getLastTempList() {
        return lastTempList;
    }

    public void setLastTempList(List<Map<String, String>> lastTempList) {
        this.lastTempList = lastTempList;
    }

}
