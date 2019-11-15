package com.zlb.httplib.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.zlb.httplib.R;
import com.zlb.httplib.dialog.ProgressDialogUtil;
import java.lang.ref.WeakReference;

/**
 * maybe not useful
 *
 * Created by zenglb on 2017/3/24.
 */
public class HttpUiTips {

    private static AlertDialog fatalEorTips;
    private static WeakReference<Context> lastContext;


    /**
     * showDialog & dismissDialog 在http 请求开始的时候显示，结束的时候消失
     * 当然不是必须需要显示的 !
     */
    public static void showDialog(final Context mContext, final String messageText) {
        if (!(mContext instanceof Activity) || ((Activity) mContext).isFinishing() || ((Activity) mContext).isDestroyed())
            return;

        ((Activity) mContext).runOnUiThread(() -> ProgressDialogUtil.showWaitDialog(mContext, messageText));
    }


    /**
     * Dialog 消失
     *
     * @param mContext
     */
    public static void dismissDialog(final Context mContext) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(ProgressDialogUtil::stopWaitDialog);
        }
    }



    /**
     * http 请求遇阻提示，比如没有网络不提示，再重试也无用
     *
     */
    public static void alertTip(Context mContext, String message, int code) {
        //只有主线程才有提示
        if (!Thread.currentThread().getName().equals("main")) {
            return;
        }
        //Activity作为窗口的载体不能无效的
        if (!(mContext instanceof Activity) || ((Activity) mContext).isFinishing() || ((Activity) mContext).isDestroyed())
            return;

        if (fatalEorTips == null) {  //首次创建
            fatalEorTips = new AlertDialog.Builder(mContext).create();
        } else {
            if (mContext != lastContext.get()) { //换了一个新的Activity
                fatalEorTips = null;
                fatalEorTips = new AlertDialog.Builder(mContext).create();
            }
        }

        lastContext = new WeakReference(mContext);

        fatalEorTips.setTitle(R.string.fatal_net_error_tips_title);
        fatalEorTips.setMessage(message + code);
        fatalEorTips.setButton(DialogInterface.BUTTON_POSITIVE, "知道了", (dialog, which) -> {
            fatalEorTips = null;
        });

        fatalEorTips.show();
    }


}
