package com.zlb.base;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.kingja.loadsir.core.LoadService;
import com.zlb.commontips.EmptyCallback;
import com.zlb.commontips.ErrorCallback;
import com.zlb.commontips.TimeoutCallback;
import com.zlb.httplib.BaseObserver;

import es.dmoral.toasty.Toasty;

/**
 * 通用的错误处理，从项目中拿出来作为Demo，写法根据需求自己修改啊
 */
public class BaseDispose {

    /**
     * Http 请求回来后的通用的错误处理，UI和数据处理分离开
     * UI 的空页面提示，错误页面提示+Toast 提示
     *
     * @param errorMsg
     * @param errorCode
     */
    public static void errorDispose(LoadService mBaseLoadService, Context context, String errorMsg, int errorCode){
        switch (errorCode) {
            case BaseObserver.RESPONSE_FATAL_EOR:
                mBaseLoadService.showCallback(ErrorCallback.class);
                break;

            case BaseObserver.SOCKET_TIMEOUT_EOR:
                mBaseLoadService.showCallback(TimeoutCallback.class);
                break;

            case BaseObserver.ConnectException_EOR:
                mBaseLoadService.showCallback(EmptyCallback.class);
                break;

            //下面这些Code 需要跳转到登陆页面，一般不会变动。具体含义看DOC
            case 101:
            case 112:
            case 123:
            case 401:
                //退回到登录页面,无感知的访问就不需要处理了
                ARouter.getInstance().build("/login/activity").navigation();
                if (context != null && Thread.currentThread().getName().equals("main")) {
                    Toasty.error(context, errorMsg+" ["+errorCode+"]").show();
                }
                return;
        }

        if (context != null && Thread.currentThread().getName().equals("main")) {
            Toasty.error(context, errorMsg+"["+errorCode+"]").show();
        }
    }

}
