package com.zlb.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zlb.httplib.dialog.HttpUiTips;

import dagger.android.support.AndroidSupportInjection;

/**
 * Base Fragment , Dagger fragment ok
 * <p>
 * Created by zenglb on 2017/1/5.
 */
public abstract class BaseStatusFragment extends RxFragment  {

    //保证Fragment即使在onDetach后，仍持有Activity的引用（有引起内存泄露的风险，但是相比空指针闪退，这种做法“安全”些）
    protected LoadService mBaseLoadService;  //基础的页面状态显示器

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View rootView = View.inflate(getActivity(), onCreateFragmentView(), null);

        mBaseLoadService = LoadSir.getDefault().register(rootView, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                onHttpReload(v);
            }
        });

        initView(rootView);

        loadHttp();

        //返回全局的加载反馈页面 ！
        return mBaseLoadService.getLoadLayout();
    }



    protected abstract int onCreateFragmentView();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        loadHttp();
    }


    public  abstract void initView(View rootView);

    /**
     * Http 请求的重新加载,这个干啥 ！！！！
     *
     */
    protected void onHttpReload(View v) {

    }

    /**
     * 如果没有重写，说明那个页面不需要Http 请求，直接是成功  HelloKitty7
     */
    protected void loadHttp() {
        mBaseLoadService.showSuccess();
    }


    @Override
    public void onAttach(Activity activity) {
        AndroidSupportInjection.inject(this);
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpUiTips.dismissDialog(getActivity());
    }

}
