package com.architecture.demo.application;

import android.app.Application;

import com.architecture.demo.http.HttpBaseInfo;
import com.architecture.httplib.base.BaseHttpApiService;

/**
 * MainApplication
 *
 */
public class MainApplication extends Application {
    public void onCreate(){
        super.onCreate();

        //初始化的时候要传入的参数
        BaseHttpApiService.Companion.init(new HttpBaseInfo(this));


    }
}
