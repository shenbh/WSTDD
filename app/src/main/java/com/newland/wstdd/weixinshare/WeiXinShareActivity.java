package com.newland.wstdd.weixinshare;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.newland.wstdd.R;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.common.AppManager;
import com.newland.wstdd.common.filedownload.ImageDownLoader;
import com.newland.wstdd.common.log.transaction.log.manager.LogManager;
import com.newland.wstdd.common.log.transaction.utils.LogUtils;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.originate.origateactivity.OriginateChairActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class WeiXinShareActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WeiXinShareActivity";//收集异常日志tag
    private static final String appid = "wx1b84c30d9f380c89";
    private IWXAPI wxApi;
    private Button bt1, bt2;
    private Button bt3;//分享到qq
    //QQ
    private Tencent mTencent;
    private static final String APP_ID = "222222";
    private AppContext appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        AppManager.getAppManager().addActivity(this);// 添加这个Activity到相应的栈中
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮
        appContext = AppContext.getAppContext();
        setContentView(R.layout.activity_wei_xin_share);

        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).registerActivity(this);
        //如果你要收集普通的日志到文件或者服务器，那么调用下面的方法即可。
        LogManager.getManager(getApplicationContext()).log(TAG, "onCreate", LogUtils.LOG_TYPE_2_FILE_AND_LOGCAT);
        /**收集异常日志*/

        //QQ
        final Context ctxContext = this.getApplicationContext();
        mTencent = Tencent.createInstance(APP_ID, ctxContext);
        mHandler = new Handler();
        //weixin
        wxApi = WXAPIFactory.createWXAPI(this, appid);
        wxApi.registerApp(appid);
        bt1 = (Button) findViewById(R.id.share_weibo1);
        bt2 = (Button) findViewById(R.id.share_weibo2);
        bt3 = (Button) findViewById(R.id.share_qq1);
        bt1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                friend(v);
            }
        });

        bt2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                friendline(v);
            }
        });

        bt3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onClickShareToQQ();
            }
        });

    }

    @Override
    protected void onDestroy() {
        /**收集异常日志*/
        LogManager.getManager(getApplicationContext()).unregisterActivity(this);
        /**收集异常日志*/
        super.onDestroy();
    }

    public void friend(View v) {
        share(0);
    }

    public void friendline(View v) {
        share(1);
    }


    private void share(int flag) {
        downloadWeiXinImg(flag);

    }

    private void downloadWeiXinImg(final int flag) {
        // TODO Auto-generated method stub
        ImageDownLoader iamDownLoader1 = new ImageDownLoader(WeiXinShareActivity.this);
        iamDownLoader1.loadImage("http://www.it165.net/uploadfile/2011/1218/20111218070928328.jpg", 100, 100, new ImageDownLoader.AsyncImageLoaderListener() {
            @Override
            public void onImageLoader(Bitmap bitmap) {

                if (bitmap != null) {
                    // 表示下载成功了
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = "http://blog.csdn.net/yeyuehei/article/details/28854667";
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = "test_myblog";
                    msg.description = "test_myblog";
                    //根据ImgUrl下载下来一张图片，弄出bitmap格式
                    //这里替换一张自己工程里的图片资源
                    Bitmap thumb = bitmap;
                    msg.setThumbImage(thumb);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                    boolean fla = wxApi.sendReq(req);
                    System.out.println("fla=" + fla);
                } else {
                    // 下载失败
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = "http://blog.csdn.net/yeyuehei/article/details/28854667";
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = "test_myblog";
                    msg.description = "test_myblog";
                    //根据ImgUrl下载下来一张图片，弄出bitmap格式
                    //这里替换一张自己工程里的图片资源
                    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                    msg.setThumbImage(thumb);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                    boolean fla = wxApi.sendReq(req);
                    System.out.println("fla=" + fla);
                }
            }
        });


    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResp(BaseResp arg0) {
        // TODO Auto-generated method stub

    }

    private void onClickShareToQQ() {
        Bundle b = getShareBundle();
        if (b != null) {
            shareParams = b;
            Thread thread = new Thread(shareThread);
            thread.start();
        }
    }

    private Bundle getShareBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("title", "在线一起走");
        bundle.putString("imageUrl", "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
        bundle.putString("targetUrl", "http://www.com179.com/path/cms/downloads/client/");
        bundle.putString("summary", "我正在使用在线一起走科学健身管理运动和健康");
        bundle.putString("site", "2222");
        bundle.putString("appName", "在线一起走");
        return bundle;
    }

    Bundle shareParams = null;

    Handler shareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };

    // 线程类，该类使用匿名内部类的方式进行声明
    Runnable shareThread = new Runnable() {

        public void run() {
            doShareToQQ(shareParams);
            Message msg = shareHandler.obtainMessage();

            // 将Message对象加入到消息队列当中
            shareHandler.sendMessage(msg);

        }
    };

    private void doShareToQQ(Bundle params) {
        mTencent.shareToQQ(WeiXinShareActivity.this, params, new BaseUiListener() {
            protected void doComplete(JSONObject values) {
                showResult("shareToQQ:", "分享成功");
            }

            @Override
            public void onError(UiError e) {
                showResult("shareToQQ:", "分享失败未安装登陆第三方");
            }

            @Override
            public void onCancel() {
                showResult("shareToQQ", "分享取消");
            }
        });
    }

    private class BaseUiListener implements IUiListener {

//		@Override
//		public void onComplete(JSONObject response) {
//			// mBaseMessageText.setText("onComplete:");
//			// mMessageText.setText(response.toString());
//			doComplete(response);
//		}

        protected void doComplete(Object values) {

        }

        @Override
        public void onError(UiError e) {
            showResult("onError:", "分享失败未安装登陆第三方");
        }

        @Override
        public void onCancel() {
            showResult("onCancel", "分享取消");
        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            doComplete(arg0);
        }
    }

    private Handler mHandler;

    private void showResult(final String base, final String msg) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                UiHelper.ShowOneToast(WeiXinShareActivity.this, msg);
//				finish();//结束
            }
        });
    }

}