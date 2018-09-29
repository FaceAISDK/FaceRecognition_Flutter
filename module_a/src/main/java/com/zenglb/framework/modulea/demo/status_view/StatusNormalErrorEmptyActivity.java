package com.zenglb.framework.modulea.demo.status_view;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;
import com.zenglb.framework.modulea.R;
import com.zlb.base.BaseActivity;
import com.zlb.commontips.EmptyCallback;
import com.zlb.commontips.LoadingCallback;
import com.zlb.commontips.PostUtil;

/**
 * 状态测试页面
 *
 */
public class StatusNormalErrorEmptyActivity extends BaseActivity {

    private LoadService loadService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_content);
        // Your can change the callback on sub thread directly.
        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                //  点击重试

                // Your can change the status out of Main thread.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadService.showCallback(LoadingCallback.class);
                        //do retry logic...
                        SystemClock.sleep(500);
                        //callback
                        loadService.showSuccess();
                    }
                }).start();


            }
        }).setCallBack(EmptyCallback.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                TextView mTvEmpty = (TextView) view.findViewById(R.id.tv_empty);
                mTvEmpty.setText("fine, no data. You must fill it!");
            }
        });

        PostUtil.postCallbackDelayed(loadService, EmptyCallback.class);

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_content;
    }

    @Override
    protected void initViews() {

    }

}
