package com.zlb.httplib;

import android.content.Context;
import android.os.NetworkOnMainThreadException;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.zlb.httplib.dialog.HttpUiTips;
import com.zlb.httplib.utils.TextConvertUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

import static com.zlb.httplib.HttpRetrofit.CUSTOM_REPEAT_REQ_PROTOCOL;

/**
 * 默认的Http Observer 的处理
 *
 * 我们不同业务后台的差异是HttpResponse<T> 不同
 * 和code=0,200 标示成功不同而已
 *
 * 不同的业务后台extends BaseObserver 修改就好了
 *
 * Created by zenglb on 2019/11/11.
 */
public abstract class DefaultObserver<T> extends BaseObserver<HttpResponse<T>> {

    public final int RESPONSE_CODE_OK = 0;  //这里返回的CODE=0 表示成功，其他的看后台定义


    /**
     * @param mCtx
     */
    public DefaultObserver(Context mCtx) {
        super(mCtx);
    }

    /**
     * 很多的http 请求可以不需要用户感知到在请求，showProgress=false
     *
     * @param mCtx
     * @param showProgress 默认需要显示进程，不要的话请传 false
     */
    public DefaultObserver(Context mCtx, boolean showProgress) {
        super(mCtx, showProgress);
    }


    /**
     * 根据具体的Api 业务逻辑去重写 onSuccess 方法！Error 是选择重写，but 必须Super ！
     *
     * @param k
     */
    public abstract void onSuccess(@Nullable T k);


    @Override
    public final void onNext(HttpResponse<T> response) {

        //这里根据具体的业务情况自己定义吧
        if (response.getCode() == RESPONSE_CODE_OK) {
            // 这里拦截一下使用测试
            onSuccess(response.getData());
        } else {
            onFailure(response.getCode(), response.getMsg());
        }

    }


    /**
     * 好像也不需要什么特殊处理吧
     *
     *
     * @param code
     * @param message
     */
    @CallSuper  //if overwrite,you should let it run.
    public void onFailure(int code, String message) {
        super.onFailure(code,message);
        //同样的逻辑
    }


    /**
     * 对通用问题的统一拦截处理,Demo 项目的特定的做法
     *
     * @param code
     */
    private final void disposeEorCode(String message, int code) {
        switch (code) {
            case 101:  //业务code ,要求重新登陆
            case 112:
            case 123:
            case 401:
                //退回到登录页面
                if (mContext != null) {  //Context 可以使Activity BroadCast Service !
                    // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
                    ARouter.getInstance().build("/login/activity").navigation();
                }
                break;
        }

        if (mContext != null && Thread.currentThread().getName().equals(Thread_Main)) {
            Toasty.error(mContext.getApplicationContext(), message + "   code=" + code, Toast.LENGTH_SHORT).show();
        }

    }


}
