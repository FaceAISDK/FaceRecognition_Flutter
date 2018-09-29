package com.zlb.httplib.dialog;

import android.app.Activity;
import android.content.Context;


import com.zlb.httplib.R;

import java.lang.ref.WeakReference;


/**
 * 控制一个页面不能有多个Dialog
 *
 *
 */
public class ProgressDialogUtil {

    private static WeakReference<Context> mThreadActivityRef;  //弱引用
    private static WeakReference<ProgressDialog> waitDialog;   //弱引用

    /**
     * 自定义用于等待的dialog,有动画和message提示
     * 调用stopWaitDialog()方法来取消
     *
     * @param mContext
     * @param message
     */
    public static void showWaitDialog(Context mContext, String message) {
        if (waitDialog != null && waitDialog.get() != null && waitDialog.get().isShowing()) {
            waitDialog.get().dismiss();
        }

        if (mContext == null || !(mContext instanceof Activity) || ((Activity) mContext).isFinishing()){
            return;
        }

        mThreadActivityRef = new WeakReference<>(mContext);
        waitDialog = new WeakReference<>(new ProgressDialog(mThreadActivityRef.get(), R.style.CustomHttpWaitDialog, message));

        if (waitDialog != null && waitDialog.get() != null && !waitDialog.get().isShowing()) {
            waitDialog.get().show();  //
        }
    }


    /**
     * 取消等待dialog
     */
    public static void stopWaitDialog() {
        if (waitDialog != null && waitDialog.get() != null && waitDialog.get().isShowing()) {
            waitDialog.get().dismiss();
        }
    }
}
