package com.anylife.module_main.thirdpartyAPI.http3service;

import android.content.Context;

import androidx.annotation.Nullable;

import com.zlb.httplib.BaseObserver;

/**
 * 第三方系统的APi 结构很难会是一样的，有些是Code=0表示成功，而有些是code=200 表示成功
 * 有些是data 里面有我们实际想要的数据，而有些是再result里面。所以抽象BaseObserver后
 * 重写Http3Observer
 *
 *
 *
 * Created by zenglb on 2019/11/11.
 */
public abstract class Http3Observer<T> extends BaseObserver<Http3Response<T>> {
    /**
     * @param mCtx
     */
    public Http3Observer(Context mCtx) {
        super(mCtx);
    }

    /**
     * 很多的http 请求可以不需要用户感知到在请求，showProgress=false
     *
     * @param mCtx
     * @param showProgress 默认需要显示进程，不要的话请传 false
     */
    public Http3Observer(Context mCtx, boolean showProgress) {
        super(mCtx, showProgress);
    }


    /**
     * 根据具体的Api 业务逻辑去重写 onSuccess 方法！Error 是选择重写，but 必须Super ！
     *
     * @param t
     */
    public abstract void onSuccess(@Nullable T t);


    @Override
    public final void onNext(Http3Response<T> response) {
        //这里根据具体的业务情况自己定义吧
        if (!response.isError()) {
            onSuccess(response.getData());
        } else {
            onFailure(0, response.getMessage());
        }

    }



}
