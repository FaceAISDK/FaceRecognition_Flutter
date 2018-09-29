Android中的JSBridge是H5与Native通信的桥梁，其作用是实现H5与Native间的双向通信。要实现H5与Native的双向通信，解决如下四个问题即可：
1、Java如何调用JavaScript
2、JavaScript如何调用Java
3、方法参数以及回调如何处理
4、通信的数据格式是怎么样的

下面从以上问题依次开始讨论:

# 1、Java如何调用JavaScript

在Android 中，Java与JavaScript的一切交互都是依托于WebView的。可通过如下方法来完成，其中function（）即为JavaScript代码，来实现相应的具体H5层功能

```
WebView.loadUrl("javascript:function()");

```

# 2、JavaScript如何调用Java

要实现在JavaScript中调用Java，就需要在JavaScript中有触发Java方法的对象和方法。在JavaScript中，当调用window对象的prompt方法时，会触发Java中的WebChromeClient对象的onJsPrompt方法，因此可以利用这个机制来实现js调用native的代码

# 3、方法参数以及回调处理

任何IPC通信都涉及到参数序列化的问题，同理，Java与JavaScript之间只能传递基础类型（包括基本类型和字符串），不包括其他对象或者函数。所以可以采用json格式来传递数据。JavaScript与Java相互调用不能直接获取返回值，只能通过回调的方式来获取返回结果。

# 4、通信的数据格式

Java与JavaScript通信需要遵循一定的通信协议，可以仿照HTTPS协议来将此协议定义为jsbridge协议：

```
jsbridge://className:port/methodName?jsonObj

```

当js调用native功能时，应当指定native层要完成某个功能调用的类名（className）和方法名（methodName），以及js传递过来的参数（jsonObj）。port值是指当native需要将操作结果返回给js时，在js中定义一个callback，并将这个callback存储在指定的位置上，这个port就定义了callback的存储位置。

![JSBridge原理](https://upload-images.jianshu.io/upload_images/2376786-fc6d78fc8e8f866b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## JSBridge的具体工作流程图如上所示：

1、js触发调用native层的行为

```
JSBridge.call(className, methodName, params, callback);

```

将call方法中的参数组合成jsbridge协议格式的url。然后通过prompt方法将url传递到native层。

```
window.prompt(url);

```

2、通过WebChromeClient来获取js传递过来的url.

```
public class JSBridgeWebChromeClient extends WebChromeClient {
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        result.confirm(JSBridge.callJava(view, message));
        return true;
    }
}

```

3、JSBridge类管理暴露给前端方法，前端调用的方法应该在此类中注册才可使用。register的实现是从Map中查找key是否存在，不存在则反射取得对应class中的所有方法,具体方法是在BridgeImpl中定义的，方法包括三个参数分别为WebView、JSONObject、CallBack。如果满足条件，则将所有满足条件的方法put到map中。

```
private static Map<String, HashMap<String, Method>> exposedMethods = new HashMap<>();
public static void register(String exposedName, Class<? extends IBridge> clazz) {
        if (!exposedMethods.containsKey(exposedName)) {
            try {
                exposedMethods.put(exposedName, getAllMethod(clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

```

JSBridge类中的callJava方法就是将js传递过来的URL解析，根据将要调用的类名从刚刚建立的Map中找出，根据方法名调用具体的方法，并将解析出的三个参数传递进去。

```
public static String callJava(WebView webView, String uriString) {
        String methodName = "";
        String className = "";
        String param = "{}";
        String port = "";
        if (!TextUtils.isEmpty(uriString) && uriString.startsWith("JSBridge")) {
            Uri uri = Uri.parse(uriString);
            className = uri.getHost();
            param = uri.getQuery();
            port = uri.getPort() + "";
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                methodName = path.replace("/", "");
            }
        }

        if (exposedMethods.containsKey(className)) {
            HashMap<String, Method> methodHashMap = exposedMethods.get(className);

            if (methodHashMap != null && methodHashMap.size() != 0 && methodHashMap.containsKey(methodName)) {
                Method method = methodHashMap.get(methodName);
                if (method != null) {
                    try {
                        method.invoke(null, webView, new JSONObject(param), new Callback(webView, port));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

```

4、CallBack类是用来回调js中回调方法的Java对应类。Java层处理好的返回结果是通过CallBack类来实现的。在这个回调类中传递的参数是JSONObject（返回结果）、WebView和port，port应与js传递过来的port相对应。

```
private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static final String CALLBACK_JS_FORMAT = "javascript:JSBridge.onFinish('%s', %s);";
    private String mPort;
    private WeakReference<WebView> mWebViewRef;

    public Callback(WebView view, String port) {
        mWebViewRef = new WeakReference<>(view);
        mPort = port;
    }
    public void apply(JSONObject jsonObject) {
        final String execJs = String.format(CALLBACK_JS_FORMAT, mPort, String.valueOf(jsonObject));
        if (mWebViewRef != null && mWebViewRef.get() != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebViewRef.get().loadUrl(execJs);
                }
            });
        }
    }

```

5、JSBridgeImpl类中定义所有暴露给前端的方法的具体实现。本文以showToast为例来通过native代码显示toast，并给出js的回调函数，返回一个JSONObject对象。

```
public static void showToast(WebView webView, JSONObject param, final Callback callback) {
        String message = param.optString("msg");
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_SHORT).show();
        if (null != callback) {
            try {
                JSONObject object = new JSONObject();
                object.put("key", "value");
                object.put("key1", "value1");
                callback.apply(getJSONObject(0, "ok", object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

```

6、在js中通过

```
JSBridge.call('bridge','showToast',{'msg': 'Hello'}, function(res){alert(JSON.stringi
    fy(res))})"

```

即可调用在Java层定义的showToast方法，调用前不要忘记在java层的JSBridge中注册该方法。

```
JSBridge.register("bridge", BridgeImpl.class);

```

# 二、总结：

JSBridge的基本原理为：
H5->通过某种方式触发一个url->Native捕获到url,进行分析->原生做处理->Native调用H5的JSBridge对象传递回调。如下图

![h5与原生页面通过jsbridge交互](https://upload-images.jianshu.io/upload_images/2376786-759a04dc30957e25.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# 三、安全性：

1、Android4.2以下，addJavascriptInterface方法有安全漏洞，js代码可以获取到Java层的运行时对象，来伪造当前用户执行恶意代码。
2、ios7以下，JavaScript无法调用native代码。
3、通过js声明的对象，是通过loadUrl注入到页面中的，所以这个对象是js对象，而不是Java对象，没有getClass等Object方法，因此也无法获得Runtime对象，避免了恶意代码的注入。
4、JSBridge采用URL解析的交互方式，是一套成熟的解决方案，便于拓展，无重大安全性问题。


