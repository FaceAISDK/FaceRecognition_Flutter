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
 * 去除一切的关于UI层面的操作
 *
 *
 * 应对聚合型的App可能有多个Host和不同的返回Response 情况，需要再抽象一层
 *
 * 还需要再抽象一层，毕竟不同的域名HttpResponse 字段不太一样，处理逻辑也会有一点不一样
 *
 * Context 的引入是为了方便Loading 和 全局的Toast的默认处理，为了更好的进行自动化测试还是不要引入了吧
 * 改用其他的方式
 *
 * Created by zenglb on 2017/4/14.
 */
public abstract class BaseObserver<K> implements Observer<K> {
    public final static String Thread_Main = "main"; //防止非主线程异常

    public final static int RESPONSE_FATAL_EOR = -1;       //返回数据失败,严重的错误
    public final static int CUSTOM_REPEAT_REQ_ERROR = -2;  //同样的一个请求并且还是重复的话返回错误

    public final static int SOCKET_TIMEOUT_EOR = -3;       //服务器响应超时
    public final static int ConnectException_EOR= -4;      //网络连接异常
    public Context mContext;

    private int errorCode = -1111;
    private String errorMsg = "未知的错误！";

    private Disposable disposable;


    /**
     * Default
     */
    public BaseObserver() {

    }


    /**
     * 严格的分层设计是不允许这里有UI相关的操作的
     *
     * @param mCtx
     */
    public BaseObserver(Context mCtx) {
        this.mContext = mCtx;
        HttpUiTips.showDialog(mContext, null);
    }

    /**
     * 严格的分层设计是不允许这里有UI相关的操作的
     *
     * 很多的http 请求可以不需要用户感知到在请求，showProgress=false
     *
     * @param mCtx
     * @param showProgress 默认需要显示进程，不要的话请传 false
     */
    public BaseObserver(Context mCtx, boolean showProgress) {
        this.mContext = mCtx;
        if (showProgress) {
            HttpUiTips.showDialog(mContext, null);
        }
    }


    @Override
    public final void onSubscribe(Disposable d) {
        disposable = d;
    }


    /**
     * 需要根据逻辑重写
     *
     * @param response
     */
    @Override
    public  void onNext(K response) {
        HttpUiTips.dismissDialog(mContext);
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }

//        //这里根据具体的业务情况自己定义吧
//        if (response.getCode() == RESPONSE_CODE_OK) {
//            // 这里拦截一下使用测试
//            onSuccess(response.getData());
//        } else {
//            onFailure(response.getCode(), response.getMsg());
//        }

    }


    /**
     * 通用异常错误的处理，不能弹出一样的东西出来
     *
     * @param t
     */
    @Override
    public final void onError(Throwable t) {
        //根据throwable 获取错位code和msg
        getErrorMessage(t);

        HttpUiTips.dismissDialog(mContext);
        onFailure(errorCode, errorMsg);
    }


    /**
     * 处理Throwable 获取对用的error msg&code
     *
     * @param t TH
     */
    private void getErrorMessage(Throwable t){
        //把里面的东西分离出来
        if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            errorCode = httpException.code();
            errorMsg = httpException.getMessage();
        } else if (t instanceof SocketTimeoutException) {  //VPN open
            errorCode = SOCKET_TIMEOUT_EOR;
            errorMsg = "服务器响应超时";
        } else if (t instanceof ConnectException) {
            errorCode = ConnectException_EOR;
            errorMsg = "无网络，请检查网络";
        } else if (t instanceof UnknownHostException) {
            errorCode = RESPONSE_FATAL_EOR;
            errorMsg = "无网络，请检查网络";
        } else if (t instanceof UnknownServiceException) {
            errorCode = RESPONSE_FATAL_EOR;
            errorMsg = "未知的服务器错误";
        } else if (t instanceof IOException) {   //飞行模式等
            //这里处理一下重复请求的自定义返回的Response 导致的错误，
            if (t.getMessage().contains(CUSTOM_REPEAT_REQ_PROTOCOL)) {
                errorCode = CUSTOM_REPEAT_REQ_ERROR;
                errorMsg = "可以忽略的重复的请求";
            } else {
                errorCode = RESPONSE_FATAL_EOR;
                errorMsg = "读取网络数据失败";
            }
        } else if (t instanceof NetworkOnMainThreadException) {
            //主线程不能网络请求，这个很容易发现
            errorCode = RESPONSE_FATAL_EOR;
            errorMsg = "主线程不能网络请求";
        } else if (t instanceof RuntimeException) {
            //很多的错误都是extends RuntimeException
            errorCode = RESPONSE_FATAL_EOR;
            errorMsg = "运行时错误" + t.toString();
        }
    }

    /**
     * 简单的把Dialog 处理掉
     */
    @Override
    public final void onComplete() {

    }

    /**
     * Default error dispose!
     * 一般的就是 AlertDialog 或 SnackBar
     *
     * @param code
     * @param message
     */
    @CallSuper  //if overwrite,you should let it run.
    public void onFailure(int code, String message) {
        if (code == RESPONSE_FATAL_EOR && mContext != null) {
            HttpUiTips.alertTip(mContext, message, code);
        } else if (code == CUSTOM_REPEAT_REQ_ERROR) {
            Log.i("Repeat http Req ", "onFailure: " + message);
        } else {
            disposeEorCode(message, code);
        }
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

        //如果要严格的分层设计，这里是不允许有UI 相关的操作的
        if (mContext != null && Thread.currentThread().getName().equals(Thread_Main)) {
            Toasty.error(mContext.getApplicationContext(), message + "   code=" + code, Toast.LENGTH_SHORT).show();
        }
    }

}
