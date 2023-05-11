package com.architecture.demo.http;

import android.app.Application;

import com.architecture.demo.BuildConfig;
import com.architecture.httplib.base.IHttpRequestBaseInfo;

/**
 * 全局的初始化,HTTP 请求的初始化，请求需要的基础信息
 *
 */
public class HttpBaseInfo implements IHttpRequestBaseInfo {
    private Application mApplication;

    public HttpBaseInfo(Application application){
        this.mApplication = application;
    }

    @Override
    public String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getAppVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public Application getApplicationContext() {
        return mApplication;
    }

}
