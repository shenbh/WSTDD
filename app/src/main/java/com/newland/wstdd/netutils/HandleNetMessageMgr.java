package com.newland.wstdd.netutils;

import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.CoreConstants;
import com.newland.wstdd.find.bean.FindHotReq;
import com.newland.wstdd.find.bean.FindHotRes;
import com.newland.wstdd.find.bean.FindRecommendReq;
import com.newland.wstdd.find.bean.FindRecommendRes;
import com.newland.wstdd.find.categorylist.detail.bean.CollectReq;
import com.newland.wstdd.find.categorylist.detail.bean.CollectRes;
import com.newland.wstdd.find.categorylist.detail.bean.IsLikeAndCollectReq;
import com.newland.wstdd.find.categorylist.detail.bean.IsLikeAndCollectRes;
import com.newland.wstdd.find.categorylist.detail.bean.LikeReq;
import com.newland.wstdd.find.categorylist.detail.bean.LikeRes;
import com.newland.wstdd.find.categorylist.registrationedit.beanrequest.CancelRegistrationReq;
import com.newland.wstdd.find.categorylist.registrationedit.beanrequest.GetEditRegistrationReq;
import com.newland.wstdd.find.categorylist.registrationedit.beanrequest.SubmitRegistrationReq;
import com.newland.wstdd.find.categorylist.registrationedit.beanresponse.CancelRegistrationRes;
import com.newland.wstdd.find.categorylist.registrationedit.beanresponse.EditRegistrationRes;
import com.newland.wstdd.find.categorylist.registrationedit.beanresponse.GetEditRegistrationRes;
import com.newland.wstdd.find.categorylist.registrationedit.beanresponse.SubmitRegistrationRes;
import com.newland.wstdd.find.find.bean.FindReq;
import com.newland.wstdd.find.find.bean.FindRes;
import com.newland.wstdd.find.hotlist.bean.FindCategoryReq;
import com.newland.wstdd.find.hotlist.bean.FindCategoryRes;
import com.newland.wstdd.login.RetMsg;
import com.newland.wstdd.login.beanrequest.CheckCodeReq;
import com.newland.wstdd.login.beanrequest.LoginBindReq;
import com.newland.wstdd.login.beanrequest.LoginReq;
import com.newland.wstdd.login.beanrequest.RegistFirstReq;
import com.newland.wstdd.login.beanrequest.RegistSecondReq;
import com.newland.wstdd.login.beanrequest.ThirdLoginReq;
import com.newland.wstdd.login.beanresponse.CheckCodeRes;
import com.newland.wstdd.login.beanresponse.LoginBindRes;
import com.newland.wstdd.login.beanresponse.LoginRes;
import com.newland.wstdd.login.beanresponse.RegistFirstRes;
import com.newland.wstdd.login.beanresponse.RegistSecondRes;
import com.newland.wstdd.login.beanresponse.ThirdLoginRes;
import com.newland.wstdd.mine.applyList.bean.RegistrationListReq;
import com.newland.wstdd.mine.applyList.bean.RegistrationListRes;
import com.newland.wstdd.mine.applyList.bean.UpdateRegistrationListReq;
import com.newland.wstdd.mine.applyList.bean.UpdateRegistrationListRes;
import com.newland.wstdd.mine.applyList.beanreq.MailUrlReq;
import com.newland.wstdd.mine.applyList.beanres.MailUrlRes;
import com.newland.wstdd.mine.beanrequest.DeleteActivityReq;
import com.newland.wstdd.mine.beanrequest.MinePersonInfoGetReq;
import com.newland.wstdd.mine.beanresponse.DeleteActivityRes;
import com.newland.wstdd.mine.beanresponse.MinePersonInfoGetRes;
import com.newland.wstdd.mine.managerpage.beanrequest.MyActivityListReq;
import com.newland.wstdd.mine.managerpage.beanrequest.OnTddRecommendReq;
import com.newland.wstdd.mine.managerpage.beanrequest.OpenActivityPeoplesReq;
import com.newland.wstdd.mine.managerpage.beanrequest.RegistrationCheckReq;
import com.newland.wstdd.mine.managerpage.beanrequest.RegistrationStateReq;
import com.newland.wstdd.mine.managerpage.beanresponse.MyActivityListRes;
import com.newland.wstdd.mine.managerpage.beanresponse.OnTddRecommendRes;
import com.newland.wstdd.mine.managerpage.beanresponse.OpenActivityPeoplesRes;
import com.newland.wstdd.mine.managerpage.beanresponse.RegistrationCheckRes;
import com.newland.wstdd.mine.managerpage.beanresponse.RegistrationStateRes;
import com.newland.wstdd.mine.managerpage.coversetting.ModifyCoverReq;
import com.newland.wstdd.mine.managerpage.coversetting.ModifyCoverRes;
import com.newland.wstdd.mine.managerpage.ilike.beanrequest.LikeListReq;
import com.newland.wstdd.mine.managerpage.ilike.beanresponse.LikeListRes;
import com.newland.wstdd.mine.managerpage.multitext.bean.SendMessageReq;
import com.newland.wstdd.mine.managerpage.multitext.bean.SendMessageRes;
import com.newland.wstdd.mine.minesetting.beanrequest.SafeReq;
import com.newland.wstdd.mine.minesetting.beanrequest.VersionReq;
import com.newland.wstdd.mine.minesetting.beanresponse.SafeRes;
import com.newland.wstdd.mine.minesetting.beanresponse.VersionRes;
import com.newland.wstdd.mine.minesetting.feedbackandhelp.bean.FeedBackReq;
import com.newland.wstdd.mine.minesetting.feedbackandhelp.bean.FeedBackRes;
import com.newland.wstdd.mine.myinterest.beanrequest.MyInterestTagsReq;
import com.newland.wstdd.mine.myinterest.beanresponse.MyInterestTagsRes;
import com.newland.wstdd.mine.personalcenter.bean.MineEditPersonReq;
import com.newland.wstdd.mine.personalcenter.bean.MineEditPersonRes;
import com.newland.wstdd.mine.personalcenter.beanreq.BindReq;
import com.newland.wstdd.mine.personalcenter.beanreq.RemoveBindReq;
import com.newland.wstdd.mine.personalcenter.beanres.BindRes;
import com.newland.wstdd.mine.personalcenter.beanres.RemoveBindRes;
import com.newland.wstdd.mine.receiptaddress.beanrequest.MineAddAddressReq;
import com.newland.wstdd.mine.receiptaddress.beanrequest.MineDefaultAddressReq;
import com.newland.wstdd.mine.receiptaddress.beanrequest.MineDeleteAddressReq;
import com.newland.wstdd.mine.receiptaddress.beanresponse.MineAddAddressRes;
import com.newland.wstdd.mine.receiptaddress.beanresponse.MineDefaultAddressRes;
import com.newland.wstdd.mine.receiptaddress.beanresponse.MineDeleteAddressRes;
//import com.newland.wstdd.mine.registrationlist.beanrequest.RegistrationListReq;
//import com.newland.wstdd.mine.registrationlist.beanrequest.UpdateRegistrationListReq;
//import com.newland.wstdd.mine.registrationlist.beanresponse.RegistrationListRes;
//import com.newland.wstdd.mine.registrationlist.beanresponse.UpdateRegistrationListRes;
import com.newland.wstdd.mine.twocode.beanrequest.TwoCodePayReq;
import com.newland.wstdd.mine.twocode.beanresponse.TwoCodePayRes;
import com.newland.wstdd.originate.beanrequest.OriginateFragmentReq;
import com.newland.wstdd.originate.beanrequest.OriginateSearchReq;
import com.newland.wstdd.originate.beanrequest.SingleActivityReq;
import com.newland.wstdd.originate.beanresponse.OriginateFragmentRes;
import com.newland.wstdd.originate.beanresponse.OriginateSearchRes;
import com.newland.wstdd.originate.beanresponse.SingleActivityRes;
import com.newland.wstdd.originate.origateactivity.beanrequest.SingleActivityPublishReq;
import com.newland.wstdd.originate.origateactivity.beanresponse.SingleActivityPublishRes;
import com.test.DeleteUserInfoReq;
import com.test.DeleteUserInfoRes;

/**
 * 连接服务器 并且返回想要的数据
 *
 * @author Administrator
 * <p>
 * <p>
 * <p>
 * 0是失败 1是成功 ret -1是response为null 0 是Res为空 1 是Res不为空
 */
public class HandleNetMessageMgr implements BaseMessageMgr {

    private void setRequest(WBRequest request) {
        request.setUserId(AppContext.getAppContext().getUserId());
        request.setPlatform("app");
        request.setTimestamp(DateUtil.getDateNowSecondString());
    }

    //注销用户，测试的时候使用
    @Override
    public RetMsg<DeleteUserInfoRes> getCancelUserInfo(DeleteUserInfoReq deleteUserInfoReq) {
        // TODO Auto-generated method stub
        RetMsg<DeleteUserInfoRes> ret = new RetMsg<DeleteUserInfoRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(deleteUserInfoReq);// 请求传入的对象
        request.set_method("DELETE");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, DeleteUserInfoRes.class, CoreConstants.CONNECT_DELETE_USER, deleteUserInfoReq.getUserId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                DeleteUserInfoRes respInfo = (DeleteUserInfoRes) response.getRespBody();
                // 设置服务器返回信息，response 的code 0 是失败 1 是成功
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {
                    ret.setCode(1);
                    respInfo = new DeleteUserInfoRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 登录操作
     */
    @Override
    public RetMsg<LoginRes> getLoginInfo(LoginReq reqLoginInfo) {
        RetMsg<LoginRes> ret = new RetMsg<LoginRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(reqLoginInfo);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, LoginRes.class, CoreConstants.CONNECT_LOGIN, "");// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                LoginRes respInfo = (LoginRes) response.getRespBody();
                // 设置服务器返回信息，response 的code 0 是失败 1 是成功
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {
                    ret.setCode(1);
                    respInfo = new LoginRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 第三方登录获取
     */
    @Override
    public RetMsg<ThirdLoginRes> getThirdLoginInfo(ThirdLoginReq thirdLoginReq) {
        RetMsg<ThirdLoginRes> ret = new RetMsg<ThirdLoginRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(thirdLoginReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, ThirdLoginRes.class, CoreConstants.CONNECT_THIRD_LOGIN, "");// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                ThirdLoginRes respInfo = (ThirdLoginRes) response.getRespBody();
                // 设置服务器返回信息，response 的code 0 是失败 1 是成功
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {
                    ret.setCode(1);
                    respInfo = new ThirdLoginRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 请求绑定操作
     */
    @Override
    public RetMsg<LoginBindRes> getReqLoginBindInfo(LoginBindReq loginBindReq) {
        RetMsg<LoginBindRes> ret = new RetMsg<LoginBindRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(loginBindReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, LoginBindRes.class, CoreConstants.CONNECT_BIND_LOGIN, loginBindReq.getPhoneNum());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                LoginBindRes respInfo = (LoginBindRes) response.getRespBody();
                // 设置服务器返回信息，response 的code 0 是失败 1 是成功
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {
                    ret.setCode(1);
                    respInfo = new LoginBindRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 注册第一步
     */
    @Override
    public RetMsg<RegistFirstRes> getRegistInfo(RegistFirstReq registFirstReq, String session) {
        RetMsg<RegistFirstRes> ret = new RetMsg<RegistFirstRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(registFirstReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, RegistFirstRes.class, CoreConstants.CONNECT_REGIST_FIRST, session);// 响应对象
        // //
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            // 设置服务器返回信息，要是成功就设置RetMsg 的code为0表示失败了 1是成功
            if ("1".equals(response.getCode())) {
                RegistFirstRes respInfo = (RegistFirstRes) response.getRespBody();
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new RegistFirstRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 注册完成
     */
    @Override
    public RetMsg<RegistSecondRes> getRegistFinishInfo(RegistSecondReq registSecondReq, String id) {
        RetMsg<RegistSecondRes> ret = new RetMsg<RegistSecondRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(registSecondReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, RegistSecondRes.class, CoreConstants.CONNECT_REGIST_SECOND, id);// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                RegistSecondRes respInfo = (RegistSecondRes) response.getRespBody();
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new RegistSecondRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 个人兴趣标签
     */
    @Override
    public RetMsg<MyInterestTagsRes> getMyInterestInfo(MyInterestTagsReq myInterestTagsReq) {
        RetMsg<MyInterestTagsRes> ret = new RetMsg<MyInterestTagsRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(myInterestTagsReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, MyInterestTagsRes.class, CoreConstants.CONNECT_MY_INTEREST, "0");// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                MyInterestTagsRes respInfo = (MyInterestTagsRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new MyInterestTagsRes();
                    ret.setObj(respInfo);// 设置

                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }

        return ret;
    }

    /**
     * 个人兴趣标签
     */
    @Override
    public RetMsg<MyInterestTagsRes> getMyInterestUpdateInfo(MyInterestTagsReq myInterestTagsReq) {
        RetMsg<MyInterestTagsRes> ret = new RetMsg<MyInterestTagsRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(myInterestTagsReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, MyInterestTagsRes.class, CoreConstants.CONNECT_MY_INTEREST, "0");// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                MyInterestTagsRes respInfo = (MyInterestTagsRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new MyInterestTagsRes();
                    ret.setObj(respInfo);// 设置

                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }

        return ret;
    }

    // 发现页的相关信息
    @Override
    public RetMsg<FindRes> getFindInfo(FindReq findReq) {
        RetMsg<FindRes> ret = new RetMsg<FindRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(findReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, FindRes.class, CoreConstants.CONNECT_FIND, "0");// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                FindRes respInfo = (FindRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new FindRes();
                    ret.setObj(respInfo);// 设置

                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 首页搜索/分类列表
     */
    @Override
    public RetMsg<OriginateSearchRes> getOriginateSearchInfo(OriginateSearchReq originateSearchReq) {
        RetMsg<OriginateSearchRes> ret = new RetMsg<OriginateSearchRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(originateSearchReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, OriginateSearchRes.class, CoreConstants.CONNECT_ORIGINATE_SEARCH, "0");// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                OriginateSearchRes respInfo = (OriginateSearchRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new OriginateSearchRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 获取个人信息
     */
    @Override
    public RetMsg<MinePersonInfoGetRes> getMinePersonInfoGetInfoMsg(MinePersonInfoGetReq minePersonInfoGetReq) {
        RetMsg<MinePersonInfoGetRes> ret = new RetMsg<MinePersonInfoGetRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(minePersonInfoGetReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, MinePersonInfoGetRes.class, CoreConstants.CONNECT_MINE_PERSONINFO_SEARCH, minePersonInfoGetReq.getUserId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                MinePersonInfoGetRes respInfo = (MinePersonInfoGetRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new MinePersonInfoGetRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 获取收货地址 (13. 收货地址新增/列表)
     */
    @Override
    public RetMsg<MineAddAddressRes> getMineReceiptAddressInfo(MineAddAddressReq mineAddAddressReq) {
        RetMsg<MineAddAddressRes> ret = new RetMsg<MineAddAddressRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(mineAddAddressReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, MineAddAddressRes.class, CoreConstants.CONNECT_MINE_RECEIPT_ADDRESS, AppContext.getAppContext().getUserId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                MineAddAddressRes respInfo = (MineAddAddressRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new MineAddAddressRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 更新地址
     */
    @Override
    public RetMsg<MineAddAddressRes> getMineUpdateAddressInfo(MineAddAddressReq mineAddAddressReq, String addressIdString) {
        RetMsg<MineAddAddressRes> ret = new RetMsg<MineAddAddressRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(mineAddAddressReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, MineAddAddressRes.class, CoreConstants.CONNECT_MINE_UPDATE_ADDRESS, addressIdString);// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                MineAddAddressRes respInfo = (MineAddAddressRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new MineAddAddressRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 设置默认收货地址
     */
    @Override
    public RetMsg<MineDefaultAddressRes> getMineDefaultAddressInfo(MineDefaultAddressReq mineDefaultAddressReq) {
        RetMsg<MineDefaultAddressRes> ret = new RetMsg<MineDefaultAddressRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(mineDefaultAddressReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, MineDefaultAddressRes.class, CoreConstants.CONNECT_MINE_SET_DEFAULTADDRESS, mineDefaultAddressReq.getNewId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                MineDefaultAddressRes respInfo = (MineDefaultAddressRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new MineDefaultAddressRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 删除地址的操作
     */
    @Override
    public RetMsg<MineDeleteAddressRes> getMineDeleteAddressInfo(MineDeleteAddressReq mineDeleteAddressReq, String addressIdString) {
        RetMsg<MineDeleteAddressRes> ret = new RetMsg<MineDeleteAddressRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(mineDeleteAddressReq);// 请求传入的对象
        request.set_method("DELETE");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, MineDeleteAddressRes.class, CoreConstants.CONNECT_MINE_DELETE_ADDRESS, addressIdString);// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                MineDeleteAddressRes respInfo = (MineDeleteAddressRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new MineDeleteAddressRes();

                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 跳转到热门活动的处理信息
     */
    @Override
    public RetMsg<FindHotRes> getFindHotListInfo(FindHotReq findHotReq) {
        RetMsg<FindHotRes> ret = new RetMsg<FindHotRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(findHotReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, FindHotRes.class, CoreConstants.CONNECT_FIND_HOT_LIST, AppContext.getAppContext().getUserId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                FindHotRes respInfo = (FindHotRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new FindHotRes();
                    ret.setObj(respInfo);// 设置

                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 推荐活动
     */
    @Override
    public RetMsg<FindRecommendRes> getFindRecommendListInfo(FindRecommendReq findRecommendReq) {
        RetMsg<FindRecommendRes> ret = new RetMsg<FindRecommendRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(findRecommendReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, FindRecommendRes.class, CoreConstants.CONNECT_FIND_RECOMMEND_LIST, AppContext.getAppContext().getUserId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                FindRecommendRes respInfo = (FindRecommendRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new FindRecommendRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 单个活动
     */
    @Override
    public RetMsg<SingleActivityRes> getSingleActivityInfo(SingleActivityReq singleActivityReq) {
        RetMsg<SingleActivityRes> ret = new RetMsg<SingleActivityRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(singleActivityReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, SingleActivityRes.class, CoreConstants.CONNECT_SINGLE_ACTIVITY, singleActivityReq.getActivityId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                SingleActivityRes respInfo = (SingleActivityRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new SingleActivityRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 发现-8类数据 列表数据
     */
    @Override
    public RetMsg<FindCategoryRes> getFindCategoeyInfo(FindCategoryReq findCategoryReq) {
        RetMsg<FindCategoryRes> ret = new RetMsg<FindCategoryRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(findCategoryReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, FindCategoryRes.class, CoreConstants.CONNECT_FIND_CATEGORY, "");// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                FindCategoryRes respInfo = (FindCategoryRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new FindCategoryRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 获取短信验证码
     */
    @Override
    public RetMsg<CheckCodeRes> getCheckCodeIndo(CheckCodeReq checkCodeReq) {
        RetMsg<CheckCodeRes> ret = new RetMsg<CheckCodeRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(checkCodeReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, CheckCodeRes.class, CoreConstants.CONNECT_CHECK_CODE, "");// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                CheckCodeRes respInfo = (CheckCodeRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new CheckCodeRes();
                    respInfo.setSessionId(ret.getMsg());
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 个人信息编辑
     */
    @Override
    public RetMsg<MineEditPersonRes> getMineEditPersonInfo(MineEditPersonReq mineEditPersonReq) {
        RetMsg<MineEditPersonRes> ret = new RetMsg<MineEditPersonRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(mineEditPersonReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, MineEditPersonRes.class, CoreConstants.CONNECT_MINE_EDITPERSON_INFO, AppContext.getAppContext().getUserId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                MineEditPersonRes respInfo = (MineEditPersonRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    //服务器返回空，自己构造一个有内容的返回对象（确保obj不为空）
                    ret.setCode(1);
                    respInfo = new MineEditPersonRes();
                    respInfo.setGetResMess(response.getMsg());
                    ret.setObj(respInfo);
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 单个活动发布
     */
    @Override
    public RetMsg<SingleActivityPublishRes> getSingleActivityPublishInfo(SingleActivityPublishReq singleActivityPublishReq, String activityAction, String activityId) {
        RetMsg<SingleActivityPublishRes> ret = new RetMsg<SingleActivityPublishRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(singleActivityPublishReq);// 请求传入的对象
        if ("publish".equals(activityAction)) {
            request.set_method("POST");
        } else if ("edit".equals(activityAction)) {
            request.set_method("PUT");
        }
        setRequest(request);
        WBResponse response;
        if ("publish".equals(activityAction)) {
            response = ServiceClient.CallService(request, SingleActivityPublishRes.class, CoreConstants.CONNECT_SINGLE_ACTIVITY_PUBLISH, activityId);// 响应对象
            // 封装好的
        } else {
            response = ServiceClient.CallService(request, SingleActivityPublishRes.class, CoreConstants.CONNECT_SINGLE_ACTIVITY_EDIT, activityId);// 响应对象

        }

        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                SingleActivityPublishRes respInfo = (SingleActivityPublishRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new SingleActivityPublishRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 27----提交报名信息
     */
    @Override
    public RetMsg<SubmitRegistrationRes> getSubmitRegistrationInfo(SubmitRegistrationReq submitRegistrationReq, String activityId) {
        RetMsg<SubmitRegistrationRes> ret = new RetMsg<SubmitRegistrationRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(submitRegistrationReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, SubmitRegistrationRes.class, CoreConstants.CONNECT_SUBMIT_REGISTRATION, activityId);// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                SubmitRegistrationRes respInfo = (SubmitRegistrationRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 28取消报名
     */
    @Override
    public RetMsg<CancelRegistrationRes> getCancelRegistrationInfo(CancelRegistrationReq cancelRegistrationReq) {
        RetMsg<CancelRegistrationRes> ret = new RetMsg<CancelRegistrationRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(cancelRegistrationReq);// 请求传入的对象
        request.set_method("DELETE");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, CancelRegistrationRes.class, CoreConstants.CONNECT_CANCEL_REGISTRATION, cancelRegistrationReq.getActivityId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                CancelRegistrationRes respInfo = (CancelRegistrationRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new CancelRegistrationRes();
                    respInfo.setGetResMess(response.getMsg());
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 获取报名活动人员的列表
     */
    @Override
    public RetMsg<RegistrationListRes> getRegistrationListInfo(RegistrationListReq registrationListReq) {
        RetMsg<RegistrationListRes> ret = new RetMsg<RegistrationListRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(registrationListReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, RegistrationListRes.class, CoreConstants.CONNECT_REGISTRATION_LIST, registrationListReq.getActivityId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                RegistrationListRes respInfo = (RegistrationListRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 更新报名人员列表
     */
    @Override
    public RetMsg<UpdateRegistrationListRes> getUpdateRegistrationListInfo(UpdateRegistrationListReq updateRegistrationListReq) {
        RetMsg<UpdateRegistrationListRes> ret = new RetMsg<UpdateRegistrationListRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(updateRegistrationListReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, UpdateRegistrationListRes.class, CoreConstants.CONNECT_UPDATE_REGISTRATION_LIST, "0");// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                UpdateRegistrationListRes respInfo = (UpdateRegistrationListRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    //服务器返回空，自己构造一个有内容的返回对象（确保obj不为空）
                    ret.setCode(1);
                    respInfo = new UpdateRegistrationListRes();
                    respInfo.setGetResMess(response.getMsg());
                    ret.setObj(respInfo);
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 34. 设置是否公开活动报名人数
     */
    @Override
    public RetMsg<OpenActivityPeoplesRes> getOpenActivityPeoplesInfo(OpenActivityPeoplesReq openActivityPeoplesReq, String activityId) {
        RetMsg<OpenActivityPeoplesRes> ret = new RetMsg<OpenActivityPeoplesRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(openActivityPeoplesReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, OpenActivityPeoplesRes.class, CoreConstants.CONNECT_OPENACTIVITY_PEOPLES, activityId);// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                OpenActivityPeoplesRes respInfo = (OpenActivityPeoplesRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(0);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 是否上团大大热搜
     */
    @Override
    public RetMsg<OnTddRecommendRes> getOnTddRecommendInfo(OnTddRecommendReq onTddRecommendReq, String activityId) {
        RetMsg<OnTddRecommendRes> ret = new RetMsg<OnTddRecommendRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(onTddRecommendReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, OnTddRecommendRes.class, CoreConstants.CONNECT_ONTDD_HOTRECOMMEND, activityId);// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                OnTddRecommendRes respInfo = (OnTddRecommendRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(0);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 是否更改报名状态
     */
    @Override
    public RetMsg<RegistrationStateRes> getRegistrationStateInfo(RegistrationStateReq registrationStateReq, String activityId) {
        RetMsg<RegistrationStateRes> ret = new RetMsg<RegistrationStateRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(registrationStateReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, RegistrationStateRes.class, CoreConstants.CONNECT_REGISTRATION_STATE, activityId);// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                RegistrationStateRes respInfo = (RegistrationStateRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(0);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 报名是否需要审核
     */
    @Override
    public RetMsg<RegistrationCheckRes> getRegistrationCheckInfo(RegistrationCheckReq registrationCheckReq, String activityId) {
        RetMsg<RegistrationCheckRes> ret = new RetMsg<RegistrationCheckRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(registrationCheckReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, RegistrationCheckRes.class, CoreConstants.CONNECT_REGISTRATION_STATE, activityId);// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                RegistrationCheckRes respInfo = (RegistrationCheckRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(0);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 更新版本信息
     */
    @Override
    public RetMsg<VersionRes> getVersionInfo(VersionReq versionReq) {
        RetMsg<VersionRes> ret = new RetMsg<VersionRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(versionReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, VersionRes.class, CoreConstants.CONNECT_VERSION_INFO, "0");// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                VersionRes respInfo = (VersionRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(0);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 活动是否点赞收藏
     */
    @Override
    public RetMsg<IsLikeAndCollectRes> getIsLikeAndCollectInfo(IsLikeAndCollectReq isLikeAndCollectReq) {
        RetMsg<IsLikeAndCollectRes> ret = new RetMsg<IsLikeAndCollectRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(isLikeAndCollectReq);
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, IsLikeAndCollectRes.class, CoreConstants.CONNECT_ISLIKEANDCOLLECT, "");
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                IsLikeAndCollectRes respInfo = (IsLikeAndCollectRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg的code为1表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);
                } else {// 服务器返回的参数是空的
                    ret.setCode(0);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 收藏/取消收藏
     */
    @Override
    public RetMsg<CollectRes> getCollectInfo(CollectReq collectReq) {
        RetMsg<CollectRes> ret = new RetMsg<CollectRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(collectReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, CollectRes.class, CoreConstants.CONNECT_COLLECT, "");// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                CollectRes respInfo = (CollectRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(0);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 点赞/取消点赞
     */
    @Override
    public RetMsg<LikeRes> getLikeInfo(LikeReq likeReq) {
        RetMsg<LikeRes> ret = new RetMsg<LikeRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(likeReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, LikeRes.class, CoreConstants.CONNECT_LIKE, likeReq.getTargetId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                LikeRes respInfo = (LikeRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(0);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 获取我发起、我参与、我收藏的活动列表
     */
    @Override
    public RetMsg<MyActivityListRes> getMyActivityListInfo(MyActivityListReq myActivityListReq) {
        // TODO Auto-generated method stub
        RetMsg<MyActivityListRes> ret = new RetMsg<MyActivityListRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(myActivityListReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, MyActivityListRes.class, CoreConstants.CONNECT_MYACTIVITY_LIST, AppContext.getAppContext().getUserId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                MyActivityListRes respInfo = (MyActivityListRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(0);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 上传 获取支付二维码的网络地址
     */
    @Override
    public RetMsg<TwoCodePayRes> getTwoCodePayInfo(TwoCodePayReq twoCodePayReq, String id) {
        // TODO Auto-generated method stub
        RetMsg<TwoCodePayRes> ret = new RetMsg<TwoCodePayRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(twoCodePayReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, TwoCodePayRes.class, CoreConstants.CONNECT_TWOCODE_IMG, AppContext.getAppContext().getUserId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                TwoCodePayRes respInfo = (TwoCodePayRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new TwoCodePayRes();
                    ret.setObj(respInfo);// 设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 修改密码
     */
    @Override
    public RetMsg<SafeRes> getSafeInfo(SafeReq safeReq, String string) {
        RetMsg<SafeRes> ret = new RetMsg<SafeRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(safeReq);
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, SafeRes.class, CoreConstants.CONNECT_MINE_CHANGEPSW, AppContext.getAppContext().getUserId());

        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                SafeRes respInfo = (SafeRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);
                } else {
                    ret.setCode(1);
                    ret.setObj(null);
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 意见反馈
     */
    @Override
    public RetMsg<FeedBackRes> getFeedBackInfo(FeedBackReq feedBackReq) {
        RetMsg<FeedBackRes> ret = new RetMsg<FeedBackRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(feedBackReq);
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, FeedBackRes.class, CoreConstants.CONNECT_MINE_FEEDBACK, AppContext.getAppContext().getUserId());

        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                FeedBackRes respInfo = (FeedBackRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);
                } else {
                    //服务器返回空，自己构造一个有内容的返回对象（确保obj不为空）
                    ret.setCode(1);
                    respInfo = new FeedBackRes();
                    respInfo.setReturnResMess(response.getMsg());
                    ret.setObj(respInfo);
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 根据userId获取到相关的数据  发起界面的首页
     */
    @Override
    public RetMsg<OriginateFragmentRes> getOriginateFragmentInfo(OriginateFragmentReq originateFragmentReq) {
        // TODO Auto-generated method stub
        RetMsg<OriginateFragmentRes> ret = new RetMsg<OriginateFragmentRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(originateFragmentReq);
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, OriginateFragmentRes.class, CoreConstants.CONNECT_ORIGINATE_FRAGMENT, originateFragmentReq.getUserId());

        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                OriginateFragmentRes respInfo = (OriginateFragmentRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);
                } else {
                    ret.setCode(1);
                    ret.setObj(null);
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 发送邮箱号码   导出名单
     */
    @Override
    public RetMsg<MailUrlRes> getMailUrlInfo(MailUrlReq mailUrlReq, String id) {
        // TODO Auto-generated method stub
        RetMsg<MailUrlRes> ret = new RetMsg<MailUrlRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(mailUrlReq);
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, MailUrlRes.class, CoreConstants.CONNECT_MAIL_URL_TOSERVER, id);

        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                MailUrlRes respInfo = (MailUrlRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);
                } else {
                    ret.setCode(1);
                    ret.setObj(null);
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 群发消息
     */
    @Override
    public RetMsg<SendMessageRes> getMultiTextInfo(SendMessageReq sendMessageReq, String id) {
        // TODO Auto-generated method stub
        RetMsg<SendMessageRes> ret = new RetMsg<SendMessageRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(sendMessageReq);
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, SendMessageRes.class, CoreConstants.CONNECT_MULTITEXT, id);

        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                SendMessageRes respInfo = (SendMessageRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);
                } else {
                    ret.setCode(1);
                    ret.setObj(null);
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    //获取编辑报名信息
    @Override
    public RetMsg<GetEditRegistrationRes> getEditRegistrationInfo(GetEditRegistrationReq getEditRegistrationReq) {
        // TODO Auto-generated method stub
        RetMsg<GetEditRegistrationRes> ret = new RetMsg<GetEditRegistrationRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(getEditRegistrationReq);
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, GetEditRegistrationRes.class, CoreConstants.CONNECT_EDIT_REGISTRATION_INFO, getEditRegistrationReq.getSignId());

        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                GetEditRegistrationRes respInfo = (GetEditRegistrationRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);
                } else {
                    ret.setCode(1);
                    ret.setObj(null);
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 提交编辑的报名信息
     */
    @Override
    public RetMsg<SubmitRegistrationRes> getOkEditRegistrationInfo(SubmitRegistrationReq submitRegistrationReq, String signId) {
        // TODO Auto-generated method stub
        RetMsg<SubmitRegistrationRes> ret = new RetMsg<SubmitRegistrationRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(submitRegistrationReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, SubmitRegistrationRes.class, CoreConstants.CONNECT_OK_EDIT_REGISTRATION_INFO, signId);// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                SubmitRegistrationRes respInfo = (SubmitRegistrationRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    @Override
    public RetMsg<LikeListRes> getLikeListInfo(LikeListReq likeListReq) {
        // TODO Auto-generated method stub
        RetMsg<LikeListRes> ret = new RetMsg<LikeListRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(likeListReq);// 请求传入的对象
        request.set_method("POST");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, LikeListRes.class, CoreConstants.CONNECT_LIKE_LIST, likeListReq.getActivityId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                LikeListRes respInfo = (LikeListRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    ret.setMsg(response.getMsg());
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    //解除绑定操作
    @Override
    public RetMsg<RemoveBindRes> getRemoveBindInfo(RemoveBindReq removeBindReq) {
        // TODO Auto-generated method stub
        RetMsg<RemoveBindRes> ret = new RetMsg<RemoveBindRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(removeBindReq);// 请求传入的对象
        request.set_method("DELETE");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, RemoveBindRes.class, CoreConstants.CONNECT_REMOVE_BIND, AppContext.getAppContext().getMobilePhone());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                RemoveBindRes respInfo = (RemoveBindRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new RemoveBindRes();
                    respInfo.setGetResMess(response.getMsg());
                    ret.setObj(respInfo);//设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    //绑定操作
    @Override
    public RetMsg<BindRes> getBindInfo(BindReq bindReq) {
        // TODO Auto-generated method stub
        RetMsg<BindRes> ret = new RetMsg<BindRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(bindReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, BindRes.class, CoreConstants.CONNECT_BIND, AppContext.getAppContext().getMobilePhone());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                BindRes respInfo = (BindRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new BindRes();
                    respInfo.setGetResMess(response.getMsg());
                    ret.setObj(respInfo);//设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    //删除活动的请求
    @Override
    public RetMsg<DeleteActivityRes> getDeleteActivityInfo(DeleteActivityReq deleteActivityReq, String activityId) {
        // TODO Auto-generated method stub
        RetMsg<DeleteActivityRes> ret = new RetMsg<DeleteActivityRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(deleteActivityReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, DeleteActivityRes.class, CoreConstants.CONNECT_DELETE_ACTIVITY, activityId);// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                DeleteActivityRes respInfo = (DeleteActivityRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new DeleteActivityRes();
                    respInfo.setGetResMess(response.getMsg());
                    ret.setObj(respInfo);//设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }

    /**
     * 设置封面
     */
    @Override
    public RetMsg<ModifyCoverRes> getModifyCoverInfo(ModifyCoverReq modifyCoverReq) {
        // TODO Auto-generated method stub
        RetMsg<ModifyCoverRes> ret = new RetMsg<ModifyCoverRes>();
        WBRequest request = new WBRequest();
        request.setReqBody(modifyCoverReq);// 请求传入的对象
        request.set_method("PUT");
        setRequest(request);
        WBResponse response = ServiceClient.CallService(request, ModifyCoverRes.class, CoreConstants.CONNECT_DELETE_ACTIVITY, modifyCoverReq.getTddActivity().getActivityId());// 响应对象
        // 封装好的
        if (response == null) {
            ret.setCode(-1);
            ret.setMsg("网络服务器异常");
            return ret;
        } else {
            if ("1".equals(response.getCode())) {
                ModifyCoverRes respInfo = (ModifyCoverRes) response.getRespBody();
                // 设置服务器返回信息，要是成功就设置RetMsg 的code为1 表示成功了
                if (respInfo != null) {
                    ret.setCode(1);
                    ret.setObj(respInfo);// 设置
                } else {// 服务器返回的参数是空的
                    ret.setCode(1);
                    respInfo = new ModifyCoverRes();
                    respInfo.setGetMessRes(response.getMsg());
                    ret.setObj(respInfo);//设置
                }
            } else if ("0".equals(response.getCode())) {
                ret.setCode(0);
                ret.setMsg(response.getMsg());
            }
        }
        return ret;
    }


}
