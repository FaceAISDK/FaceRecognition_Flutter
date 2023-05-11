package com.architecture.httplib.base;

import android.app.Application;

/**
 * 基础信息，可以根据后期情况扩充
 *
 * 依赖倒置，请求的基本信息,
 *
 *
 */
public interface IHttpRequestBaseInfo {

    String getAppVersionName();

    String getAppVersionCode();

    boolean isDebug(); //是否调试模式

    Application getApplicationContext();
}
