package com.zlb.base;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.zlb.httplib.R;


/**
 * BaseWebViewActivity
 *
 */
public abstract class BaseWebViewActivity extends BaseActivity {
    public static final String WEB_ACTION = "my.intent.action.GOTOWEB";
    public static final String WEB_CATEGORY = "my.intent.category.WEB";
    public static final String SCANQR_ACTION = "my.intent.action.GOSCANQR";
    public static final String SCANQR_CATEGORY = "my.intent.category.SCANQR";

    public final static int ZXING_REQUEST_CODE = 101;    //扫码
    public final static int GET_IMG_REQUEST_CODE = 102;    //读取照片

    public static final String URL = "url";//网页url
    public static final String TITLE = "title";//标题内容

    public WebView mWebView;
    private ProgressBar topLoadingBar;
    protected String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initViews() {

        System.gc();  //尽量少的减少人为的干预

        setTitle("Android JSBridge");

        topLoadingBar = findViewById(R.id.progress_bar);
        mWebView = findViewById(R.id.webview);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        //Android 4.4 开始，默认的浏览器已经是 chrome 了，所以 webview 也是 chrome 了，这就给了 webview 远程调试的能力。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setWebContentsDebuggingEnabled(true);  //开发模式打开
        }

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        // 设置可以支持缩放
        settings.setSupportZoom(true);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(false);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        //手动设置UA,让运营商劫持DNS的浏览器广告不生效 http://my.oschina.net/zxcholmes/blog/596192
        settings.setUserAgentString("suijishu" + "#" + settings.getUserAgentString() + "01234560");
        setWebViewClient();
        setWebChromeClient();

    }


    /**
     * @param url
     */
    public void setURL(String url) {
        mWebView.loadUrl(url);
    }


    //处理物理按键返回键的处理
    private boolean isGoBack = false;  //是不是有按下物理返回键的操作？
    private long goBackTime = 0;       //记录一下按下物理返回键的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            isGoBack = true;
            goBackTime = System.currentTimeMillis();
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private String startUrl=null; //加载一个URL开始的时候的（记录重定向的）

    private void setWebViewClient() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (!TextUtils.isEmpty(startUrl) && !startUrl.equals(url) && isGoBack && System.currentTimeMillis() - goBackTime < 600) {
                    isGoBack = false;
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish(); //现在都是单进程
                    }
                } else {
                    view.loadUrl(url);
                    isGoBack = false;
                    return true;
                }

                return false;

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                String message = "SSL证书错误";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "不是可信任的证书颁发机构。";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "证书已过期。";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "证书的主机名不匹配。";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "证书还没有生效。";
                        break;
                }
                message += " 你想要继续吗？";
                builder.setTitle("SSL证书错误");
                builder.setMessage(message);
                builder.setPositiveButton("continue", (dialog, which) -> handler.proceed());
                builder.setNegativeButton("cancel", (dialog, which) -> handler.cancel());
                final AlertDialog dialog = builder.create();
                dialog.show();
            }

        });

    }

    /**
     * 在JavaScript中，当调用window对象的prompt方法时，会触发Java中的WebChromeClient对象的onJsPrompt方法
     * jsbridge://className:port/methodName?jsonObj
     *
     * @return 结束后把Webview 所在的进程killed ,所有的手机都OK吗？没有测试哦
     */
    private void setWebChromeClient() {
        mWebView.setWebChromeClient(new WebChromeClient() {

//            /**
//             * 在JavaScript中，当调用window对象的prompt方法时，会触发Java中的WebChromeClient对象的onJsPrompt方法
//             * jsbridge://className:port/methodName?jsonObj
//             *
//             * @param view            WebView
//             * @param url             file:///android_asset/index.html
//             * @param message         JSBridge://NativeBridgeClsName:798787206/getImage?{"msg":"这是带给移动端的msg参数"}
//             * @param defaultValue    defvale(拓展，目前没有使用)
//             * @param result
//             * @return
//             */

//            @Override
//            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
//                String callBackData = JSBridge.callJavaNative(view, message);
//
//                //了
//                result.confirm(callBackData);
//                return true;
//            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getToolbar().setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    topLoadingBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == topLoadingBar.getVisibility()) {
                        topLoadingBar.setVisibility(View.VISIBLE);
                    }
                    topLoadingBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }


    /**
     * 结束后把Webview 所在的进程killed ,所有的手机都OK吗？没有测试哦
     */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}

