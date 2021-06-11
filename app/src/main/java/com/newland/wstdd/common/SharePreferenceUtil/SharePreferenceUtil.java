package com.newland.wstdd.common.SharePreferenceUtil;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtil {
    private String userId = null;
    private String userName;
    private String headImgUrl;
    private String nickName;
    private String mobilePhone;
    private String userPwd;
    private String sex;
    private String email;
    private String identity;
    private String cerStatus;
    private String wechat_union_id;
    private String shop_phone;
    private String weibo_open_id;
    private String myQucode;
    private String myAcNum;
    private String mySignAcNum;
    private String myFavAcNum;
    private String openId;//第三方的ID  就是根据什么登录的时候   19
    private String platForm;//第三方平台  就是哪一个第三方的平台  20
    private String isLogin;//判断是否登入
    private String myTags;//我的兴趣标签
    private Context context;
    private final String USER_INFO = "user_infos";

    public SharePreferenceUtil(Context context) {
        this.context = context;
//		SharedPreferences preferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    }

    /**
     * 保存参数
     *
     * @param name 账户名
     * @param age  密码
     */
    public void savesAppcntextInfo(
            String userId, String userName, String headImgUrl,
            String nickName, String mobilePhone,
            String userPwd, String sex,
            String email, String identity,
            String cerStatus, String wechat_union_id,
            String shop_phone, String weibo_open_id, String myQucode, String myAcNum,
            String mySignAcNum, String myFavAcNum, String openId, String platForm, String isLogin, String myTags) {
        if (userId != null && !"".equals(userId)) {


            SharedPreferences preferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
            Editor editor = preferences.edit();
            editor.remove("userId");
            editor.remove("userName");
            editor.remove("headImgUrl");
            editor.remove("nickName");
            editor.remove("mobilePhone");
            editor.remove("userPwd");
            editor.remove("sex");
            editor.remove("email");
            editor.remove("identity");
            editor.remove("cerStatus");
            editor.remove("wechat_union_id");
            editor.remove("shop_phone");
            editor.remove("weibo_open_id");
            editor.remove("myQucode");
            editor.remove("myAcNum");
            editor.remove("mySignAcNum");
            editor.remove("myFavAcNum");
            editor.remove("openId");
            editor.remove("platForm");
            editor.remove("isLogin");
            editor.remove("myTags");
            editor.putString("userId", userId);
            editor.putString("userName", userName);
            editor.putString("headImgUrl", headImgUrl);
            editor.putString("nickName", nickName);
            editor.putString("mobilePhone", mobilePhone);
            editor.putString("userPwd", userPwd);
            editor.putString("sex", sex);
            editor.putString("email", email);
            editor.putString("identity", identity);
            editor.putString("cerStatus", cerStatus);
            editor.putString("wechat_union_id", wechat_union_id);
            editor.putString("shop_phone", shop_phone);
            editor.putString("weibo_open_id", weibo_open_id);
            editor.putString("myQucode", myQucode);
            editor.putString("myAcNum", myAcNum);
            editor.putString("mySignAcNum", mySignAcNum);
            editor.putString("myFavAcNum", myFavAcNum);
            editor.putString("openId", openId);
            editor.putString("platForm", platForm);
            editor.putString("isLogin", isLogin);
            editor.putString("myTags", myTags);
            editor.commit();
        }
    }


    /**
     * 保存参数
     *
     * @param string
     */
    public void saveMessToSp(String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.remove(key);
        editor.putString(key, value);
        editor.commit();
    }

    public String getMessFromSp(String key) {
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }


    /**
     * 获取各项配置参数,相当于读取文件中的数据
     * 集合都是键-值对的形式，可以通过key找到相应的值
     *
     * @return
     */
    public Map<String, String> getPreferences() {
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        /**
         * 理解一下这里的put方法只是为了封装该类，以便在另外一个类中可以去调用
         *
         * preferences.getString("name", "1");如果没有参数name的话，这时候呢默认为1，否则就是按name对应的值
         */
        params.put("userId", preferences.getString("userId", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("userName", preferences.getString("userName", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("headImgUrl", preferences.getString("headImgUrl", ""));//
        params.put("nickName", preferences.getString("nickName", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("mobilePhone", preferences.getString("mobilePhone", ""));//
        params.put("userPwd", preferences.getString("userPwd", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("sex", preferences.getString("sex", ""));//
        params.put("email", preferences.getString("email", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("identity", preferences.getString("identity", ""));//
        params.put("cerStatus", preferences.getString("cerStatus", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("wechat_union_id", preferences.getString("wechat_union_id", ""));//
        params.put("qq_open_id", preferences.getString("shop_phone", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("weibo_open_id", preferences.getString("weibo_open_id", ""));//
        params.put("myQucode", preferences.getString("myQucode", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("myAcNum", preferences.getString("myAcNum", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("mySignAcNum", preferences.getString("mySignAcNum", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("myFavAcNum", preferences.getString("myFavAcNum", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("openId", preferences.getString("openId", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("platForm", preferences.getString("platForm", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("isLogin", preferences.getString("isLogin", ""));//1表示的是如果不存在name键时候，默认为1
        params.put("myTags", preferences.getString("myTags", ""));//1表示的是如果不存在name键时候，默认为1
        return params;
    }

    /**
     * 清空记录
     */
    public void clear() {
        SharedPreferences sp = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
