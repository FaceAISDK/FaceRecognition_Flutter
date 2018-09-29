package com.zenglb.framework.jsbridge;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.zenglb.framework.jsbridge.IBridge;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * JSBridge 的主控
 *
 */
public class JSBridge {
    //前端和移动端都需要固定的指定这个方法
    public static final String exposeClassName = "NativeBridgeClsName";

    //所有JS能调用的native 的方法都需要注册，防止反编译后重新注入native方法
    private static Map<String, HashMap<String, Method>> NativeJSBridgeMethods = new HashMap<>();

    /**
     * 注入JS 可以调用的Native方法！
     *
     * @param exposedName
     * @param clazz
     */
    public static void register(String exposedName, Class<? extends IBridge> clazz) {
        if (!NativeJSBridgeMethods.containsKey(exposedName)) {
            try {
                NativeJSBridgeMethods.put(exposedName, getAllMethod(clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 防止反编译后方法注入，只有满足一定规则的方法才能被执行，
     *
     * @param injectedCls
     * @return
     * @throws Exception
     */
    private static HashMap<String, Method> getAllMethod(Class injectedCls)  {
        HashMap<String, Method> mMethodsMap = new HashMap<>();
        Method[] methods = injectedCls.getDeclaredMethods();
        for (Method method : methods) {
            String name;
            if (method.getModifiers() != Modifier.PUBLIC || (name = method.getName()) == null) {
                continue;
            }
            Class[] parameters = method.getParameterTypes();
            if (null != parameters && parameters.length == 3) {
                if (parameters[0] == WebView.class && (parameters[1] == JSONObject.class || parameters[1] == Integer.class) && parameters[2] == Callback.class) {
                    mMethodsMap.put(name, method);
                }
            }
        }
        return mMethodsMap;
    }



    /**
     * 需要处理没法调用的Native方法等问题
     *
     * @param webView
     * @param JSBridgeURL
     * @return
     */
    public static String callJavaNative(WebView webView, String JSBridgeURL) {
        String methodName = "";
        //native层要完成某个功能的集合，NativeBridgeClsName
        String className = "";
        String param = "{}";
        String port = "";
        if (!TextUtils.isEmpty(JSBridgeURL) && JSBridgeURL.startsWith("JSBridge")) {
            //如果不是JSBridge，就直接的丢弃不处理
            Uri uri = Uri.parse(JSBridgeURL);
            className = uri.getHost();
            param = uri.getQuery();
            port = uri.getPort() + "";
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                methodName = path.replace("/", "");
            }
        }

        if (NativeJSBridgeMethods.containsKey(className)) {
            HashMap<String, Method> methodHashMap = NativeJSBridgeMethods.get(className);
            if (methodHashMap != null && methodHashMap.size() != 0) {
                Method method = methodHashMap.get(methodName);

                BridgeImpl bridge = new BridgeImpl();
                if (method != null) {
                    try { //http://azrael6619.iteye.com/blog/429797   Java的反射机制
                        method.invoke(bridge, webView, new JSONObject(param), new Callback(webView, port));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("NativeMethodError", method.getName() + "不存在这样的方法");

                    //本地没有这个方法，比如App版本太老了，没有提前埋好点； 给予友好的提示信息
                    Method eorMethod = methodHashMap.get("returnCommonEor");
                    //需要处理本地方法不存在的情况
                    try {
                        eorMethod.invoke(bridge, Callback.NATIVE_FUNCTION_NULL, new Callback(webView, port));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return null;
    }


}
