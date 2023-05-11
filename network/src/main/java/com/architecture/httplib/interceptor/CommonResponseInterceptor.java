package com.architecture.httplib.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 请求返回拦截器
 *
 */
public class CommonResponseInterceptor implements Interceptor {
    private static final String TAG = "ResponseInterceptor";
    @Override
    public Response intercept(Chain chain) throws IOException {
        long requestTime = System.currentTimeMillis();

        //只能有一次chain.proceed
        Response response = chain.proceed(chain.request());

        //Token 过期，解密，通用错误拦截等等
        Log.d(TAG, "requestTime="+ (System.currentTimeMillis() - requestTime));

        return response;
    }
}
