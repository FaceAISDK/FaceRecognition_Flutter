package com.architecture.httplib.interceptor;

import com.architecture.httplib.base.IHttpRequestBaseInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 通用的请求拦截器，
 *
 */
public class CommonRequestInterceptor implements Interceptor {
    private IHttpRequestBaseInfo requiredInfo;

    public CommonRequestInterceptor(IHttpRequestBaseInfo requiredInfo){
        this.requiredInfo = requiredInfo;
    }

    /**
     * 怎么设置拦截器
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        builder.addHeader("X-Version", this.requiredInfo.getAppVersionCode());
        builder.addHeader("X-Platform", "Android");
        builder.addHeader("Connection", "Keep-Alive");
        builder.addHeader("User-Agent", "yourAppLabel-Android");

        return chain.proceed(builder.build());
    }


}
