package com.zlb.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.zlb.Sp.SPDao;
import com.zlb.Sp.SPKey;
import com.zlb.base.BaseApplication;
import com.zlb.httplib.MyHttpInterceptor;
import com.zlb.utils.MD5Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * HTTP 请求Retrofit 配置
 * <p>
 * Created by Anylife.zlb@gmail.com on 2017/3/16.
 */
public class HttpRetrofit {
    private static final String TAG = HttpRetrofit.class.getSimpleName() + "OKHTTP";
    private static final String baseUrl = "http://t1" + ".int." + "owl1" + "024.com/";  // WARMING-just for test !

    //throw a custom IOException("Unexpected protocol: " + protocol)
    public static final String CUSTOM_REPEAT_REQ_PROTOCOL = "MY_CUSTOM_REPEAT_REQ_PROTOCOL";

    /**
     * 下面apiService对象其实是一个动态代理对象，并不是一个真正的ApiService接口的implements产生的对象，
     * 当apiService对象调用getXxxx方法时会被动态代理拦截，然后调用Proxy.newProxyInstance方法中的InvocationHandler对象，
     * 它的invoke方法会传入3个参数：{@link Retrofit}
     */
    private static Retrofit retrofit;

    private static String TOKEN;

    public static void setToken(String token) {
        TOKEN = token;
    }

    //Value 里面保存的是时间，我们实际业务是有用的
    public static Map<String, Long> requestIdsMap = new HashMap<>();

    /**
     * @param spDao
     * @param mContext
     * @return
     */
    public static Retrofit getRetrofit(SPDao spDao, Context mContext) {
        if (retrofit == null) {
            //1.处理没有认证  http 401 Not Authorised
            Authenticator mAuthenticator2 = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    if (responseCount(response) >= 2) {
                        // If both the original call and the call with refreshed token failed,it will probably keep failing, so don't try again.
                        return null;
                    }
                    //如果发现Token过去了，可以使用refreshToken刷新获取新的Token ，根据自己的业务情况进行吧，为了简单说明，删除了

//                    refreshToken();
                    return response.request().newBuilder()
                            .header("Authorization", TOKEN)
                            .build();
                }
            };

            //2. 请求的拦截处理
            /**
             * 如果你的 token 是空的，就是还没有请求到 token，比如对于登陆请求，是没有 token 的，
             * 只有等到登陆之后才有 token，这时候就不进行附着上 token。另外，如果你的请求中已经带有验证 header 了，
             * 比如你手动设置了一个另外的 token，那么也不需要再附着这一个 token.
             */
            Interceptor mRequestInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    if (TextUtils.isEmpty(TOKEN)) {
                        TOKEN = spDao.getData(SPKey.KEY_ACCESS_TOKEN, "", String.class);
                    }

                    /**
                     * TOKEN == null，Login/Register noNeed Token
                     * noNeedAuth(originalRequest)    refreshToken api request is after log in before log out,but  refreshToken api no need auth
                     */
                    if (TextUtils.isEmpty(TOKEN) || alreadyHasAuthorizationHeader(originalRequest) || noNeedAuth(originalRequest)) {
                        Response originalResponse = chain.proceed(originalRequest);
                        return originalResponse.newBuilder()
                                //get http request progress,et download app
                                .build();
                    }

                    Request authorisedRequest = originalRequest.newBuilder()
                            .header("Authorization", TOKEN)
                            .header("Connection", "Keep-Alive")  //新添加，time-out默认是多少呢？
                            .header("Content-Encoding", "gzip")  //使用GZIP 压缩内容
                            .build();

                    //拦截处理重复的HTTP 请求,类似 防止快速点击按钮去重 可以不去处理了，全局统一处理
                    String requestKey = MD5Util.getUpperMD5Str(authorisedRequest.toString());

                    if (null == requestIdsMap.get(requestKey)) {
                        requestIdsMap.put(requestKey, System.currentTimeMillis());
                        Log.e("REPEAT-REQUEST", "注册请求:" + requestKey+" ----  "+Thread.currentThread().getName());
                    } else {
                        //如果是重复的请求，抛出一个自定义的错误，这个错误大家根据自己的业务定义吧
                        Log.i("REPEAT-REQUEST", "重复请求" + requestKey+"  ---------------重复请求 ----"+Thread.currentThread().getName());
                        return new Response.Builder()
                                .protocol(Protocol.get(CUSTOM_REPEAT_REQ_PROTOCOL))
                                .request(authorisedRequest) //multi thread
                                .build();
                    }

                    Response originalResponse = chain.proceed(authorisedRequest);
                    //把统一拦截的header 打印出来
                    new MyHttpInterceptor().logRequestHeaders(authorisedRequest);

                    return originalResponse.newBuilder().build();
                }
            };


            /**
             * 如果不喜欢系统的Http 的打印方式，可以自己去实现Interceptor 接口
             * 但是统一拦截的header 是无法打印的，因为是在请求发出后统一拦截打印的。
             *
             */
            MyHttpInterceptor myHttpInterceptor = new MyHttpInterceptor();
            myHttpInterceptor.setLevel(MyHttpInterceptor.Level.BODY);

            //Retrofit 默认的网络请求执行期（callFactory）就是OkHttpClient
            //这里仍然需要指定是因为参数，行为的设置等
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)  //是否在网络差的时候重试
                    .connectTimeout(77, TimeUnit.SECONDS)
                    .readTimeout(76, TimeUnit.SECONDS)
                    .writeTimeout(77, TimeUnit.SECONDS)
                    .addNetworkInterceptor(mRequestInterceptor)
                    .authenticator(mAuthenticator2)
                    .addInterceptor(myHttpInterceptor)
                    .addInterceptor(new ChuckInterceptor(BaseApplication.getAppContext()))

                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)       //Set the Api Base URL
                    .client(okHttpClient)   //The HTTP client used for requests.
                    .addConverterFactory(GsonConverterFactory.create())
                    //CallAdapter用于对原始Call进行再次封装，如Call<R>到Observable<R>
                    //把response封装成rxjava的Observeble，然后进行流式操作
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }
        return retrofit;
    }


    /**
     * If both the original call and the call with refreshed token failed,it will probably keep failing, so don't try again.
     * count times ++
     *
     * @param response
     * @return
     */
    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

    /**
     * check if already has oauth header
     *
     * @param originalRequest
     */
    private static boolean alreadyHasAuthorizationHeader(Request originalRequest) {
        if (originalRequest.headers().toString().contains("Authorization")) {
            Log.w(TAG, "already add Auth header");
            return true;
        }
        return false;
    }

    /**
     * some request after login/oauth before logout
     * but they no need oauth,so do not add auth header
     *
     * @param originalRequest
     */
    private static boolean noNeedAuth(Request originalRequest) {
        if (originalRequest.headers().toString().contains("NeedOauthFlag")) {
            Log.d("WW", "no need auth !");
            return true;
        }
        return false;
    }


}
