package com.zlb.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zlb.httplib.HttpUiTips;
import com.zlb.httplib.R;

/**
 * [FBI WARMING] 不要为了方便，只有某几个Activity 才会用的（定位，Wi-Fi 数据收集啊，写在Base里面，那还abstract什么）
 * 基类就只做基类的事情,不要把业务层面的代码写到这里来
 * <p>
 * <p>
 * 1.toolbar 的处理封装
 * 2.增加Error，empty,Loading,timeout,等通用的场景处理，一处Root注入，处处可用
 * 3.有些简单的页面真的没有必要弄 MVP 了，可以分别的继承BaseActivity 和 BaseMVPActivity
 * 4.
 *
 * @author anylife.zlb@gmail.com 20170301
 */
// TODO: 2019/1/30    MVP 要写的代码太多了，准备搞一套自动代码生成工具，填入业务名称自动生成 MVP 相关
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    public Context mContext;

    //Http Error，empty,Loading,timeout状态管理器
    public LoadService mBaseLoadService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;

        View rootView = customContentView(View.inflate(this, R.layout.activity_base, null));
        setContentView(rootView);
        initViews();

        //在这里进行Http 的请求
        loadHttp();

    }


    /**
     * 定制Custom View，Content 区域先留空，后面再动态的添加，同时
     * 增加Error，empty,Loading,timeout,等通用的场景处理，一处Root注入，处处可用
     * <p>
     * 组件化以后 R2 资源问题。ButterKnife 不能使用了，考虑使用 其他自动生成FindViewById 工具
     */
    private View customContentView(View rootView) {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        View contentView = View.inflate(this, getLayoutId(), null);
        if (contentView != null) {
            FrameLayout contentLayout = rootView.findViewById(R.id.content_layout);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            contentLayout.addView(contentView, params);

//            ButterKnife.bind(this, rootView);   //组件化后 ButterKnife 很不好使用，改用Android View Generator

            //增加Error，empty,Loading,timeout,等通用的场景处理,这个需要重新的组织一下
            mBaseLoadService = LoadSir.getDefault().register(contentView, (Callback.OnReloadListener) v -> onHttpReload(v));
        }
        return rootView;
    }

    /**
     * 点击按钮的监听
     *
     * @param view
     */
    public void onClick(View view) {

    }

    /**
     * 如果没有重写，说明那个页面不需要Http 请求，直接是成功
     */
    protected void loadHttp() {
        mBaseLoadService.showSuccess();
    }

    /**
     * Http 请求的重新加载
     */
    protected void onHttpReload(View v) {
    }

    protected abstract int getLayoutId(); //获取相应的布局啊

    protected abstract void initViews();

    public final Context getContext() {
        return mContext;
    }

    /**
     * Get toolbar
     *
     * @return support.v7.widget.Toolbar.
     */
    public final Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    public final void setToolBarTitle(CharSequence title) {
        getToolbar().setTitle(title);
        setSupportActionBar(getToolbar());
    }


    /**
     * 设置ToolBar 是否是可见的，默认是可见的;ToolBar  的Menu怎么设置
     *
     * @param visible
     */
    public final void setToolBarVisible(int visible) {
        getToolbar().setVisibility(visible);
        setSupportActionBar(getToolbar());
        mToolbar = null;
    }


    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     *
     * @return
     */
    protected boolean isShowBackIcon() {
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbar();
    }


    /**
     * 设置toolbar
     *
     */
    public void setToolbar(){
        if (null != getToolbar()) {
            if (isShowBackIcon()) {
                getToolbar().setNavigationIcon(R.drawable.ic_back_copy);
                getToolbar().setNavigationOnClickListener(v -> onBackPressed());
            } else {
                getToolbar().setNavigationIcon(null);
                getToolbar().setTitleMarginStart(66); //转为DP
            }
        }
    }


    /**
     * 防止内存泄漏需要：
     * <p>
     * 1.移除消息队列中MessageQueue 中的所有的消息
     * 2.监听器注册的取消
     * 3.停止异步任务
     * 4.静态的变量置 null
     * 5.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUiTips.dismissDialog(mContext);  // 非常的重要呢！！
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
