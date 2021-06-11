package com.newland.wstdd.netutils;

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
 * 定义一个接口    各个请求功能的操作
 *
 * @author Administrator
 */
public interface BaseMessageMgr {

    RetMsg<DeleteUserInfoRes> getCancelUserInfo(DeleteUserInfoReq deleteUserInfoReq);//0注销账户    用作测试时候用的

    RetMsg<LoginRes> getLoginInfo(LoginReq reqLoginInfo);//1获取登录操作

    RetMsg<ThirdLoginRes> getThirdLoginInfo(ThirdLoginReq thirdLoginReq);//2第三方请求

    RetMsg<RegistFirstRes> getRegistInfo(RegistFirstReq registFirstReq, String sessionId);//3获取第一步的注册操作

    RetMsg<LoginBindRes> getReqLoginBindInfo(LoginBindReq loginBindReq);//4绑定请求的操作

    RetMsg<RemoveBindRes> getRemoveBindInfo(RemoveBindReq removeBindReq);//4.4解除绑定请求的操作

    RetMsg<BindRes> getBindInfo(BindReq bindReq);//4.4解除绑定请求的操作

    RetMsg<CheckCodeRes> getCheckCodeIndo(CheckCodeReq checkCodeReq);//5获取短信验证码

    RetMsg<RegistSecondRes> getRegistFinishInfo(RegistSecondReq registSecondReq, String userIdString);//6获取第二步的注册操作

    RetMsg<MyInterestTagsRes> getMyInterestInfo(MyInterestTagsReq myInterestTagsReq);//7我的个人兴趣标签

    RetMsg<MyInterestTagsRes> getMyInterestUpdateInfo(MyInterestTagsReq myInterestTagsReq);//7我的个人兴趣标签

    RetMsg<FindRes> getFindInfo(FindReq findReq);//8发现页的相关信息

    RetMsg<OriginateSearchRes> getOriginateSearchInfo(OriginateSearchReq originateSearchReq);//9首页的搜索功能

    RetMsg<VersionRes> getVersionInfo(VersionReq versionReq);//10获取版本信息

    RetMsg<MinePersonInfoGetRes> getMinePersonInfoGetInfoMsg(MinePersonInfoGetReq minePersonInfoGetReq);//11获取个人信息

    RetMsg<MineEditPersonRes> getMineEditPersonInfo(MineEditPersonReq mineEditPersonReq);//12编辑个人信息

    RetMsg<MineAddAddressRes> getMineReceiptAddressInfo(MineAddAddressReq mineAddAddressReq);//13获取收获地址

    RetMsg<MineAddAddressRes> getMineUpdateAddressInfo(MineAddAddressReq mineAddAddressReq, String addressIdString);//更新收获地址

    RetMsg<MineDefaultAddressRes> getMineDefaultAddressInfo(MineDefaultAddressReq mineDefaultAddressReq);//14设置默认的收获地址

    RetMsg<MineDeleteAddressRes> getMineDeleteAddressInfo(MineDeleteAddressReq mineDeleteAddressReq, String addressIdString);//15删除地址的操作

    RetMsg<FindHotRes> getFindHotListInfo(FindHotReq findHotReq);//16热门活动列表

    RetMsg<FindRecommendRes> getFindRecommendListInfo(FindRecommendReq findRecommendReq);//17推荐活动列表

    RetMsg<SingleActivityRes> getSingleActivityInfo(SingleActivityReq singleActivityReq);//18单个活动

    RetMsg<SingleActivityPublishRes> getSingleActivityPublishInfo(SingleActivityPublishReq singleActivityPublishReq, String activityAction, String activityId);//19单个活动发布

    RetMsg<FindCategoryRes> getFindCategoeyInfo(FindCategoryReq findCategoryReq);//20发现类别获取列表

    RetMsg<SubmitRegistrationRes> getSubmitRegistrationInfo(SubmitRegistrationReq submitRegistrationReq, String string);//21提交报名信息

    RetMsg<CancelRegistrationRes> getCancelRegistrationInfo(CancelRegistrationReq cancelRegistrationReq);//22取消报名

    RetMsg<RegistrationListRes> getRegistrationListInfo(RegistrationListReq registrationListReq);//23获取报名人员列表

    RetMsg<UpdateRegistrationListRes> getUpdateRegistrationListInfo(UpdateRegistrationListReq updateRegistrationListReq);//24获取报名人员列表

    RetMsg<OpenActivityPeoplesRes> getOpenActivityPeoplesInfo(OpenActivityPeoplesReq openActivityPeoplesReq, String string);//25是否公开活动报名人数

    RetMsg<OnTddRecommendRes> getOnTddRecommendInfo(OnTddRecommendReq onTddRecommendReq, String string);//26是否上团大大热搜

    RetMsg<RegistrationStateRes> getRegistrationStateInfo(RegistrationStateReq registrationStateReq, String string);//27是否需要审核报名

    RetMsg<RegistrationCheckRes> getRegistrationCheckInfo(RegistrationCheckReq registrationCheckReq, String string);//28更改报名状态

    RetMsg<IsLikeAndCollectRes> getIsLikeAndCollectInfo(IsLikeAndCollectReq likeAndCollectReq);//29活动是否点赞收藏

    RetMsg<CollectRes> getCollectInfo(CollectReq collectReq);//30收藏

    RetMsg<LikeRes> getLikeInfo(LikeReq likeReq);//31点赞

    RetMsg<MyActivityListRes> getMyActivityListInfo(MyActivityListReq myActivityListReq);//获取我发起 、参加 、收藏的活动

    RetMsg<TwoCodePayRes> getTwoCodePayInfo(TwoCodePayReq twoCodePayReq, String string);

    RetMsg<SafeRes> getSafeInfo(SafeReq safeReq, String string);//修改密码

    RetMsg<FeedBackRes> getFeedBackInfo(FeedBackReq feedBackReq);//意见反馈

    RetMsg<OriginateFragmentRes> getOriginateFragmentInfo(OriginateFragmentReq originateFragmentReq);//发起界面相关数据的获取

    RetMsg<MailUrlRes> getMailUrlInfo(MailUrlReq mailUrlReq, String id);//发送邮箱地址给服务器导出名单

    RetMsg<SendMessageRes> getMultiTextInfo(SendMessageReq sendMessageReq, String id);//群发消息

    RetMsg<GetEditRegistrationRes> getEditRegistrationInfo(GetEditRegistrationReq getEditRegistrationReq);//获取编辑报名信息

    RetMsg<SubmitRegistrationRes> getOkEditRegistrationInfo(SubmitRegistrationReq submitRegistrationReq, String string);//提交报名信息

    RetMsg<LikeListRes> getLikeListInfo(LikeListReq likeListReq);//获取点赞列表

    RetMsg<DeleteActivityRes> getDeleteActivityInfo(DeleteActivityReq deleteActivityReq, String string);//删除活动的请求

    RetMsg<ModifyCoverRes> getModifyCoverInfo(ModifyCoverReq modifyCoverReq);//更改封面
}
