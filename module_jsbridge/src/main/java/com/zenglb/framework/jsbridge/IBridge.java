package com.zenglb.framework.jsbridge;

import android.webkit.WebView;

import com.zenglb.framework.jsbridge.Callback;

import org.json.JSONObject;

/**
 * java 7 8 9 中的接口方法都是有不同的实现
 *
 * https://blog.csdn.net/tracydragonlxy/article/details/78082600
 */
public interface IBridge {
    void getImage(WebView webView, JSONObject param, final Callback callback) ;

    void scanQRCode(WebView webView, JSONObject param, final Callback callback) ;

    void showToast(WebView webView, JSONObject param, final Callback callback) ;

}
