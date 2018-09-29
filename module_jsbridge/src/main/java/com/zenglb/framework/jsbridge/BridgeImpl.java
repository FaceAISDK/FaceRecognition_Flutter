package com.zenglb.framework.jsbridge;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.webkit.WebView;
import android.widget.Toast;


import com.zlb.base.BaseWebViewActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个类中的方法都是JS 通过Bridge 来调用的，使用前需要注入才能给前端赋能
 * <p>
 * <p>
 * 里面的所有的方法（除了方法名字）都必须满足一定的规则！
 */
public class BridgeImpl implements IBridge {
    private static Map<Integer, Callback> callbackCache = new HashMap();  //缓存callback,onActResult 用
    public static String filterTAG = "MY.intent.action.BridgeImpl";       //

    /**
     * 从缓存的CallBack cache 中移除某个callback!
     */
    public static Callback getCallback(Integer key) {
        Callback callback = callbackCache.get(key);
        callbackCache.remove(key);
        return callback;
    }

    /**
     * 拍照，扫码等需要调用新的Activity 来获取
     * <p>
     * 调用新的Activity处理需要的数据结果，需要在Activity接收数据
     *
     * @param webView
     * @param intentCode 根据code 区分不同的服务
     */
    private void callActivityForJSResult(WebView webView, int intentCode) {
        Intent intent = new Intent();
        intent.setAction(filterTAG);
        intent.putExtra("code", intentCode);
        //全部的改为本地的广播
        LocalBroadcastManager.getInstance(webView.getContext()).sendBroadcast(intent);
    }

    /**
     * 处理通用的错误，比如调用了本地并不存在的方法！
     *
     * @param code     -1 不存在的Native方法   -2
     * @param callback
     */
    public void returnCommonEor(final Integer code, final Callback callback) {
        if (null != callback) {
            Map<String, String> data = new HashMap<>();
            data.put("Connected wifi", "Vanke Service(前端显示的数据),");
            JSBridgeResult jsBridgeResult = JSBridgeResult.Builder.start()
                    .code(code)
                    .result(data).build();

            callback.applyDataToJS(jsBridgeResult);
        }
    }

    /**
     * @param webView
     * @param param
     * @param callback
     */
    public void getImage(WebView webView, JSONObject param, final Callback callback) {
        callbackCache.put(BaseWebViewActivity.GET_IMG_REQUEST_CODE, callback);
        callActivityForJSResult(webView, BaseWebViewActivity.GET_IMG_REQUEST_CODE);
    }


    /**
     * 扫码回传给JS，这个处理和其他的有些不一样，需要新启动一个Activity 来获取数据
     *
     * @param webView
     * @param param
     * @param callback
     */
    public void scanQRCode(WebView webView, JSONObject param, final Callback callback) {
        callbackCache.put(BaseWebViewActivity.ZXING_REQUEST_CODE, callback);
        callActivityForJSResult(webView, BaseWebViewActivity.ZXING_REQUEST_CODE);
    }


    public void showToast(WebView webView, JSONObject param, final Callback callback) {
        String message = param.optString("msg");
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
        if (null != callback) {
            Map<String, String> data = new HashMap<>();
            data.put("Connected wifi", "Vanke Service(前端显示的数据),");

            JSBridgeResult jsBridgeResult = JSBridgeResult.Builder.start()
                    .code(0)
                    .result(data).build();

            callback.applyDataToJS(jsBridgeResult);
        }
    }

}
