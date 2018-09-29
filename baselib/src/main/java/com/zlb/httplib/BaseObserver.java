package com.zlb.httplib;

import android.content.Context;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.CallSuper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.zlb.httplib.rxUtils.TextConvertUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Base Observer 的封装处理
 * <p>
 * 注意内存泄漏：https://github.com/trello/RxLifecycle/tree/2.x
 * <p>
 * Created by zenglb on 2017/4/14.
 */
public abstract class BaseObserver<T> implements Observer<HttpResponse<T>> {
    private final String TAG = BaseObserver.class.getSimpleName();
    public final static String Thread_Main = "main";

    private final int RESPONSE_CODE_OK = 0;       //自定义的业务逻辑，成功返回积极数据
    private final int RESPONSE_FATAL_EOR = -1;    //返回数据失败,严重的错误

    private Context mContext;
    private static Gson gson = new Gson();

    private int errorCode = -1111;
    private String errorMsg = "未知的错误！";

    private Disposable disposable;

    /**
     * 根据具体的Api 业务逻辑去重写 onSuccess 方法！Error 是选择重写，but 必须Super ！
     *
     * @param t
     */
    public abstract void onSuccess(T t);


    /**
     * @param mCtx
     */
    public BaseObserver(Context mCtx) {
        this.mContext = mCtx;
        HttpUiTips.showDialog(mContext, null);
    }

    /**
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

    @Override
    public final void onNext(HttpResponse<T> response) {

        Log.e("Thread-io",Thread.currentThread().getName());


        HttpUiTips.dismissDialog(mContext);

        if (!disposable.isDisposed()) {
            disposable.dispose();
        }

        if (response.getCode() == RESPONSE_CODE_OK || response.getCode() == 200) { //response.getCode() == 200 GOOD LIFE  的API真够奇怪的
            // 这里拦截一下使用测试

            onSuccess(response.getResult());
        } else {
            onFailure(response.getCode(), response.getError());
        }
    }

    /**
     * 通用异常错误的处理，不能弹出一样的东西出来
     *
     * @param t
     */
    @Override
    public final void onError(Throwable t) {
        Log.e("okhttp", "Throwable t:" + t.toString());  //打印出异常信息

        HttpUiTips.dismissDialog(mContext);
        if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            errorCode = httpException.code();
            errorMsg = httpException.getMessage();
            getErrorMsg(httpException);
        } else if (t instanceof SocketTimeoutException) {  //VPN open
            errorCode = RESPONSE_FATAL_EOR;
            errorMsg = "服务器响应超时";
        } else if (t instanceof ConnectException) {
            errorCode = RESPONSE_FATAL_EOR;
            errorMsg = "网络连接异常，请检查网络";
        } else if (t instanceof UnknownHostException) {
            errorCode = RESPONSE_FATAL_EOR;
            errorMsg = "无法解析主机，请检查网络连接";
        } else if (t instanceof UnknownServiceException) {
            errorCode = RESPONSE_FATAL_EOR;
            errorMsg = "未知的服务器错误";
        } else if (t instanceof IOException) {   //飞行模式等
            errorCode = RESPONSE_FATAL_EOR;
            errorMsg = "读取网络数据失败";
        } else if (t instanceof NetworkOnMainThreadException) {
            //主线程不能网络请求，这个很容易发现
            errorCode = RESPONSE_FATAL_EOR;
            errorMsg = "主线程不能网络请求";
        } else if (t instanceof RuntimeException) {
            //很多的错误都是extends RuntimeException
            errorCode = RESPONSE_FATAL_EOR;
            errorMsg = "运行时错误" + t.toString();
        }

        onFailure(errorCode, errorMsg);
    }


    /**
     * 简单的把Dialog 处理掉
     */
    @Override
    public final void onComplete() {
//        HttpUiTips.dismissDialog(mContext);
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
            case 101:
            case 112:
            case 123:
            case 401:
                //退回到登录页面
                if (mContext != null) {  //Context 可以使Activity BroadCast Service !
//                    Intent intent = new Intent();
//                    //不要hard Code, 使用灵活的Intent 来做吧，ARouter 解耦
//                    intent.setAction("app.intent.action.LOGIN");
//                    mContext.startActivity(intent);

                    // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
                    ARouter.getInstance().build("/login/activity").navigation();

                }
                break;
        }

        if (mContext != null && Thread.currentThread().getName().toString().equals(Thread_Main)) {
            Toasty.error(mContext.getApplicationContext(), message + "   code=" + code, Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 获取详细的错误的信息 errorCode,errorMsg
     * <p>
     * 以登录的时候的Grant_type 故意写错为例子,这个时候的http 应该是直接的返回401=httpException.code()
     * 但是是怎么导致401的？我们的服务器会在response.errorBody 中的content 中说明
     */
    private final void getErrorMsg(HttpException httpException) {
        String errorBodyStr = "";
        try {      //我们的项目需要的UniCode转码 ,!!!!!!!!!!!!!!
            errorBodyStr = TextConvertUtils.convertUnicode(httpException.response().errorBody().string());
        } catch (IOException ioe) {
            Log.e("errorBodyStr ioe:", ioe.toString());
        }

        if (TextUtils.isEmpty(errorBodyStr)) {
            return;
        }

        try {
            HttpResponse errorResponse = gson.fromJson(errorBodyStr, HttpResponse.class);
            if (null != errorResponse) {
                errorCode = errorResponse.getCode();
                errorMsg = errorResponse.getError();
            }
        } catch (Exception jsonException) {
            jsonException.printStackTrace();
        }

    }

}
