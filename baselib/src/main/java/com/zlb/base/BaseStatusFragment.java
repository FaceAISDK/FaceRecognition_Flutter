package com.zlb.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zlb.httplib.HttpUiTips;

import javax.inject.Inject;

//import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Base Fragment , Dagger fragment ok
 * <p>
 * Created by zenglb on 2017/1/5.
 */
public abstract class BaseStatusFragment extends RxFragment implements HasSupportFragmentInjector {

    //保证Fragment即使在onDetach后，仍持有Activity的引用（有引起内存泄露的风险，但是相比空指针闪退，这种做法“安全”些）
    protected Activity mActivity;            //防止getActivity()== null
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
     * 如果没有重写，说明那个页面不需要Http 请求，直接是成功
     */
    protected void loadHttp() {
        mBaseLoadService.showSuccess();
    }


    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;


    @Override
    public void onAttach(Context context) {
        //使用的Fragment 是V4 包中的，不然就是AndroidInjection.inject(this)
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpUiTips.dismissDialog(getActivity());
    }

}
