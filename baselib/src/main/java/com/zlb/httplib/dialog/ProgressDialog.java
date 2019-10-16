package com.zlb.httplib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.zlb.httplib.R;

/**
 * 自定义Dialog
 */
public class ProgressDialog extends Dialog {
    private Context context;
    private String msg; //跟随Dialog 一起显示的message 信息！

    public ProgressDialog(Context context, int theme, String msg) {
        super(context, theme);
        this.context = context;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view ;
        view = View.inflate(context, R.layout.custom_wait_dialog, null);

        if (!TextUtils.isEmpty(msg)) {
            ((TextView)view.findViewById(R.id.tv_loading_msg)).setText(msg);
        }

        setContentView(view);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

}
