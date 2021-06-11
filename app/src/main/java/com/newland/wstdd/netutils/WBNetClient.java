package com.newland.wstdd.netutils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;
import android.util.Log;

import com.newland.wstdd.common.common.CoreConstants;
import com.newland.wstdd.common.common.UrlManager;

/**
 * 根据网址Url请求连接
 *
 * @author Administrator
 */
public class WBNetClient extends BaseClient {

    private String encoding = "UTF-8";
    private HttpURLConnection httpURLConnection;

    @Override
    public String call(BaseMessage message, Object operation, String id) {
        try {

            URL url = null;
            // 注册1
            if (operation.equals(CoreConstants.CONNECT_REGIST_FIRST)) {
                url = new URL(UrlManager.requestURL + "/reg;jsessionid=" + id);
            }

            // 注册2
            else if (operation.equals(CoreConstants.CONNECT_REGIST_SECOND)) {
                url = new URL(UrlManager.requestURL + "/user/" + id);
            }
            // 短信注册
            else if (operation.equals(CoreConstants.CONNECT_CHECK_CODE)) {
                url = new URL(UrlManager.requestURL + "/sendSms");
            }
            // 注销用户
            else if (operation.equals(CoreConstants.CONNECT_DELETE_USER)) {
                url = new URL(UrlManager.requestURL + "/userInfo/" + id);
            }
            // 获取个人兴趣标签
            else if (operation.equals(CoreConstants.CONNECT_MY_INTEREST)) {
                url = new URL(UrlManager.requestURL + "/tags/");
            }

            //登录
            else if (operation.equals(CoreConstants.CONNECT_LOGIN)) {
                url = new URL(UrlManager.requestURL + "/login/");
            }
            //第三方登录
            else if (operation.equals(CoreConstants.CONNECT_THIRD_LOGIN)) {
                url = new URL(UrlManager.requestURL + "/userBind/");
            } else if (operation.equals(CoreConstants.CONNECT_BIND_LOGIN)) {
                url = new URL(UrlManager.requestURL + "/userBind/" + id);
            }
            //发现页的相关信息
            else if (operation.equals(CoreConstants.CONNECT_FIND)) {
                url = new URL(UrlManager.requestURL + "/ac/discovery");
            }
            //首页搜索列表
            else if (operation.equals(CoreConstants.CONNECT_ORIGINATE_SEARCH)) {
                url = new URL(UrlManager.requestURL + "/ac/search");
            }
            //版本信息的获取
            else if (operation.equals(CoreConstants.CONNECT_VERSION_INFO)) {
                url = new URL(UrlManager.requestURL + "/appVersion");
            }
            //个人信息搜索
            else if (operation.equals(CoreConstants.CONNECT_MINE_PERSONINFO_SEARCH)) {
                url = new URL(UrlManager.requestURL + "/userInfo/" + id);
            }
            //个人信息的编辑
            else if (operation.equals(CoreConstants.CONNECT_MINE_EDITPERSON_INFO)) {
                url = new URL(UrlManager.requestURL + "/userInfo/" + id);
            }
            //收获地址的获取设置 (13.	收货地址新增/列表)
            else if (operation.equals(CoreConstants.CONNECT_MINE_RECEIPT_ADDRESS)) {
                url = new URL(UrlManager.requestURL + "/tddDeliverAddr/");
            }
            //14.更新地址
            else if (operation.equals(CoreConstants.CONNECT_MINE_UPDATE_ADDRESS)) {
                url = new URL(UrlManager.requestURL + "/tddDeliverAddr/" + id);
            }
            //设置默认地址
            else if (operation.equals(CoreConstants.CONNECT_MINE_SET_DEFAULTADDRESS)) {
                url = new URL(UrlManager.requestURL + "/tddDeliverAddr/" + id);
            }
            //删除收获地址信息
            else if (operation.equals(CoreConstants.CONNECT_MINE_DELETE_ADDRESS)) {
                url = new URL(UrlManager.requestURL + "/tddDeliverAddr/" + id);
            }
            //热门活动的请求
            else if (operation.equals(CoreConstants.CONNECT_FIND_HOT_LIST)) {
                url = new URL(UrlManager.requestURL + "/ac/hotList");
            }
            //推荐活动的请求
            else if (operation.equals(CoreConstants.CONNECT_FIND_RECOMMEND_LIST)) {
                url = new URL(UrlManager.requestURL + "/ac/tjList");
            }
            //单个活动查询
            else if (operation.equals(CoreConstants.CONNECT_SINGLE_ACTIVITY)) {
                url = new URL(UrlManager.requestURL + "/ac/" + id);
            }
            //单个活动发布
            else if (operation.equals(CoreConstants.CONNECT_SINGLE_ACTIVITY_PUBLISH)) {
                url = new URL(UrlManager.requestURL + "/ac");
            }
            //单个活动编辑
            else if (operation.equals(CoreConstants.CONNECT_SINGLE_ACTIVITY_EDIT)) {
                url = new URL(UrlManager.requestURL + "/ac/" + id);
            }
            //发现类别列表
            else if (operation.equals(CoreConstants.CONNECT_FIND_CATEGORY)) {
                url = new URL(UrlManager.requestURL + "/ac/search");
            }
            //27提交报名信息
            else if (operation.equals(CoreConstants.CONNECT_SUBMIT_REGISTRATION)) {
                url = new URL(UrlManager.requestURL + "/acSign/" + id);
            }

            //28取消报名信息
            else if (operation.equals(CoreConstants.CONNECT_CANCEL_REGISTRATION)) {
                url = new URL(UrlManager.requestURL + "/acSign/" + id);
            }
            //30活动报名人员列表
            else if (operation.equals(CoreConstants.CONNECT_REGISTRATION_LIST)) {
                url = new URL(UrlManager.requestURL + "/acSignList/" + id);
            }
            //31更新活动报名列表
            else if (operation.equals(CoreConstants.CONNECT_UPDATE_REGISTRATION_LIST)) {
                url = new URL(UrlManager.requestURL + "/acSignList/");
            }
            //34.	设置是否公开活动报名人数
            else if (operation.equals(CoreConstants.CONNECT_OPENACTIVITY_PEOPLES)) {
                url = new URL(UrlManager.requestURL + "/ac/" + id);
            }
            //35.	设置是否上团大大热点推荐
            else if (operation.equals(CoreConstants.CONNECT_ONTDD_HOTRECOMMEND)) {
                url = new URL(UrlManager.requestURL + "/ac/" + id);
            }
            //36.	设置报名是否需要审核
            else if (operation.equals(CoreConstants.CONNECT_REGISTRATION_CHECK)) {
                url = new URL(UrlManager.requestURL + "/ac/" + id);
            }
            //37.	设置报名状态
            else if (operation.equals(CoreConstants.CONNECT_REGISTRATION_STATE)) {
                url = new URL(UrlManager.requestURL + "/ac/" + id);
            }
            //活动是否点赞收藏			
            else if (operation.equals(CoreConstants.CONNECT_ISLIKEANDCOLLECT)) {
                url = new URL(UrlManager.requestURL + "/isLikeAndCollect");
            }
            //收藏
            else if (operation.equals(CoreConstants.CONNECT_COLLECT)) {
                url = new URL(UrlManager.requestURL + "/collect");
            }
            //点赞
            else if (operation.equals(CoreConstants.CONNECT_LIKE)) {
                url = new URL(UrlManager.requestURL + "/like");
            }
            //我发起参加收藏的活动列表
            else if (operation.equals(CoreConstants.CONNECT_MYACTIVITY_LIST)) {
                url = new URL(UrlManager.requestURL + "/myAc/" + id);
            }
            //二维码上传   下载
            else if (operation.equals(CoreConstants.CONNECT_TWOCODE_IMG)) {
                url = new URL(UrlManager.requestURL + "/payImg/" + id);
            }
            //密码修改 http://test.wstdd.cn/tddApp/pwd/{userId}
            else if (operation.equals(CoreConstants.CONNECT_MINE_CHANGEPSW)) {
                url = new URL(UrlManager.requestURL + "/pwd/" + id);
            }
            //46.	我的设置-反馈帮助 http://test.wstdd.cn/tddApp/advice/
            else if (operation.equals(CoreConstants.CONNECT_MINE_FEEDBACK)) {
                url = new URL(UrlManager.requestURL + "/advice/");
            }
            //46.	我的设置-反馈帮助 http://test.wstdd.cn/tddApp/advice/
            else if (operation.equals(CoreConstants.CONNECT_ORIGINATE_FRAGMENT)) {
                url = new URL(UrlManager.requestURL + "/login/" + id);
            }
            //47.	发送邮件，活动参加对象导出名单
            else if (operation.equals(CoreConstants.CONNECT_MAIL_URL_TOSERVER)) {
                url = new URL(UrlManager.requestURL + "/activitySignMailto/" + id);
            }
            //群发消息 http://test.wstdd.cn/tddApp/msg/{activityId}
            else if (operation.equals(CoreConstants.CONNECT_MULTITEXT)) {
                url = new URL(UrlManager.requestURL + "/msg/" + id);
            }
            //48.	获取报名信息  编辑
            else if (operation.equals(CoreConstants.CONNECT_EDIT_REGISTRATION_INFO)) {
                url = new URL(UrlManager.requestURL + "/acSignInfo/" + id);
            }
            //49.	提交编辑号的报名信息  编辑 acSign
            else if (operation.equals(CoreConstants.CONNECT_OK_EDIT_REGISTRATION_INFO)) {
                url = new URL(UrlManager.requestURL + "/acSign/" + id);
            }

            //50.	提交编辑号的报名信息  编辑 acSign
            else if (operation.equals(CoreConstants.CONNECT_LIKE_LIST)) {
                url = new URL(UrlManager.requestURL + "/likeList/" + id);
            }
            //51 解除绑定
            else if (operation.equals(CoreConstants.CONNECT_REMOVE_BIND)) {
                url = new URL(UrlManager.requestURL + "/userBind/" + id);
            }
            //51 绑定
            else if (operation.equals(CoreConstants.CONNECT_BIND)) {
                url = new URL(UrlManager.requestURL + "/userBind/" + id);
            }

            //52 删除活动
            else if (operation.equals(CoreConstants.CONNECT_DELETE_ACTIVITY)) {
                url = new URL(UrlManager.requestURL + "/ac/status/" + id);
            }
            //53 修改封面
            else if (operation.equals(CoreConstants.CONNECT_MODIFY_COVER)) {
                url = new URL(UrlManager.requestURL + "/ac/" + id);
            }
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10 * 1000);
            httpURLConnection.setDoOutput(true);// 设置允许输出
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Fiddler");
            httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Charset", encoding);
            WBRequest request = (WBRequest) message;
            requestMessage = "json=" + MessageUtil.WBRequestToJson(request);
            writeData(requestMessage);
            Log.i("-ClientReq-", requestMessage);
            byte[] data = requestMessage.getBytes(encoding);
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(data);
            os.flush();
            os.close();
            // System.out.println("httpURLConnection.getResponseCode()  "+httpURLConnection.getResponseCode());
            // Log.d("-Client-", httpURLConnection.getResponseCode() + ""); //
            // 200表示成功
            if (httpURLConnection.getResponseCode() == 200) {
                InputStream is = httpURLConnection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf1 = new byte[1024];
                int len = 0;
                while ((len = is.read(buf1)) != -1) {
                    bos.write(buf1, 0, len);
                }
                byte[] back_data = bos.toByteArray();
                bos.flush();
                bos.close();
                is.close();
                responseMessage = new String(back_data);

                Log.i("-ClientRes-", responseMessage);

                // System.out.println("responseMessage  "+responseMessage);
                // Log.i("-Client-", responseMessage);

            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return responseMessage;
    }

    // 测试数据
    public void writeData(String string) {
        try {

            if (hasSdcard()) {
                // SD卡路径
                String filePath = Environment.getExternalStorageDirectory() + File.separator + "a.txt";

                // 注意这里不是new，而是调用activity提供的openFileOutout的方法，去打开文件
                FileOutputStream outStream = new FileOutputStream(new File(filePath));
                // 字符串装换成字节数组只要调用getBytes()便可以
                // 字节数组转换成字符串 ①String string=new
                // String(bybuff);string=String.copyValueOf(string.toCharArray(),0,bybuff.length));
                outStream.write(string.getBytes());
                outStream.close();// 关闭输出流
            }
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }

    }

    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

}
