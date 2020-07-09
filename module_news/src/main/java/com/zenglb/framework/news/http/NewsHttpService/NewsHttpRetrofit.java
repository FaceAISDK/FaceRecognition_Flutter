package com.zenglb.framework.news.http.NewsHttpService;

import android.util.Log;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.zenglb.framework.news.http.NewsHttpService.callAdapter.LiveDataCallAdapterFactory;
import com.zlb.base.BaseApplication;
import com.zlb.httplib.utils.MyHttpInterceptor;
import com.zlb.utils.MD5Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 其实是可以复用的
 *
 * Created by Anylife.zlb@gmail.com on 2019/11/12.
 */
public class NewsHttpRetrofit {
    public static final String baseUrl = "http://gank.io/api/";
    //throw a custom IOException("Unexpected protocol: " + protocol)
    public static final String CUSTOM_REPEAT_REQ_PROTOCOL = "MY_CUSTOM_REPEAT_REQ_PROTOCOL";

    /**
     * 下面apiService对象其实是一个动态代理对象，并不是一个真正的ApiService接口的implements产生的对象，
     * 当apiService对象调用getXxxx方法时会被动态代理拦截，然后调用Proxy.newProxyInstance方法中的InvocationHandler对象，
     * 它的invoke方法会传入3个参数：{@link Retrofit}
     */
    private static Retrofit retrofit;

    //Value 里面保存的是时间，我们实际业务是有用的
    public static Map<String, Long> requestIdsMap = new HashMap<>();

    /**
     * @return
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            //1.处理没有认证  这里没有Oauth,所以不会出现需要认证的情况
            Authenticator mAuthenticator2 = (route, response) -> {
                if (responseCount(response) >= 2) {
                    // If both the original call and the call with refreshed token failed,it will probably keep failing,
                    // so don't try again.
                    return null;
                }
                return response.request().newBuilder()
                        .header("Authorization", "no need oauth here")
                        .build();
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

                    Request myRequest = originalRequest.newBuilder()
                            .header("Authorization", "no need oauth here")
                            .header("Connection", "Keep-Alive")  //新添加，time-out默认是多少呢？
                            .header("Content-Encoding", "gzip")  //使用GZIP 压缩内容
                            .build();

                    //拦截处理重复的HTTP 请求,类似 防止快速点击按钮去重 可以不去处理了，全局统一处理
                    String requestKey = MD5Util.getUpperMD5Str(myRequest.toString());

                    if (null == requestIdsMap.get(requestKey)) {
                        requestIdsMap.put(requestKey, System.currentTimeMillis());
                        Log.e("REPEAT-REQUEST", "注册请求:" + requestKey + " ----  " + Thread.currentThread().getName());
                    } else {
                        //如果是重复的请求，抛出一个自定义的错误，这个错误大家根据自己的业务定义吧
                        Log.i("REPEAT-REQUEST", "重复请求" + requestKey + "  ---重复请求 ----" + Thread.currentThread().getName());
                        return new Response.Builder()
                                .protocol(Protocol.get(CUSTOM_REPEAT_REQ_PROTOCOL))
                                .request(myRequest) //multi thread
                                .build();
                    }

                    Response originalResponse = chain.proceed(myRequest);
                    //把统一拦截的header 打印出来
                    logRequestHeaders(myRequest);

                    return originalResponse.newBuilder().build();
                }

            };


            /**
             * 如果不喜欢系统的Http 的打印方式，可以自己去实现Interceptor 接口
             * 但是统一拦截的header 是无法打印的，因为是在请求发出后统一拦截打印的。
             *
             */
            MyHttpInterceptor myHttpInterceptor = new MyHttpInterceptor(requestIdsMap);
            myHttpInterceptor.setLevel(MyHttpInterceptor.Level.BODY);

            //Retrofit 默认的网络请求执行期（callFactory）就是OkHttpClient
            //这里仍然需要指定是因为参数，行为的设置等
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)  //是否在网络差的时候重试
                    .connectTimeout(17, TimeUnit.SECONDS)
                    .readTimeout(16, TimeUnit.SECONDS)
                    .writeTimeout(17, TimeUnit.SECONDS)
                    .addNetworkInterceptor(mRequestInterceptor)
                    .authenticator(mAuthenticator2)
                    .addInterceptor(myHttpInterceptor)
                    .addInterceptor(new ChuckInterceptor(BaseApplication.getAppContext()))
                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)       //Set the Api Base URL
                    .client(okHttpClient)   //The HTTP client used for requests.
                    .addConverterFactory(GsonConverterFactory.create())
                    //把response封装成rxjava的Observeble，然后进行流式操作
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    //LiveData Call Factory
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())  //CallAdapter
                    .build();

        }
        return retrofit;
    }


    /**
     * 打印全局统一拦截添加的Http Headers
     * <p>
     * 全局拦截的http 没法在配置中直接打印处理，因为先http 请求然后打印然后拦截添加的
     *
     * @param request
     */
    private static void logRequestHeaders(Request request) {
        Log.w("OKhttp ", "  开始打印HTTP请求  Headers \n");
        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                Log.i("OKhttp: " + i + " ", name + ": " + headers.value(i));
            }
        }
        Log.w("OKhttp ", "  打印HTTP请求完成  Headers \n");
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


}
