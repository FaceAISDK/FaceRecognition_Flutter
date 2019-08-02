package com.zlb.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.FrameLayout

import com.alibaba.android.arouter.launcher.ARouter
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.zlb.httplib.HttpUiTips
import com.zlb.httplib.R

/**
 * [FBI WARMING] 不要为了方便，只有某几个Activity 才会用的（定位，Wi-Fi 数据收集啊，写在Base里面，那还abstract什么）
 * 基类就只做基类的事情,不要把业务层面的代码写到这里来
 *
 *
 *
 *
 * 1.toolbar 的处理封装
 * 2.增加Error，empty,Loading,timeout,等通用的场景处理，一处Root注入，处处可用
 * 3.有些简单的页面真的没有必要弄 MVP 了，可以分别的继承BaseActivity 和 BaseMVPActivity
 * 4.
 *
 * @author anylife.zlb@gmail.com 20170301
 */
// TODO: 2019/1/30    MVP 要写的代码太多了，准备搞一套自动代码生成工具，填入业务名称自动生成 MVP 相关
abstract class BaseActivityCopy : AppCompatActivity(), View.OnClickListener {
    private var mToolbar: Toolbar? = null
    var context: Context?=null

    //Http Error，empty,Loading,timeout状态管理器
    private var mBaseLoadService: LoadService<*> ?=null

    protected abstract val layoutId: Int //获取相应的布局啊

    /**
     * Get toolbar
     *
     * @return support.v7.widget.Toolbar.
     */
    val toolbar: Toolbar?
        get() = findViewById<View>(R.id.toolbar) as Toolbar


    /**
     * 是否显示后退按钮,默认显示
     * 主页面重写该方法不需要显示
     *
     * @return
     */
    private val isShowBackIcon: Boolean
        get() = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this@BaseActivityCopy
        ARouter.getInstance().inject(this)
        val rootView = customContentView(View.inflate(this, R.layout.activity_base, null))
        setContentView(rootView)
        initViews()

        //在这里进行Http 的请求
        loadHttp()

    }


    /**
     * 定制Custom View，Content 区域先留空，后面再动态的添加，同时
     * 增加Error，empty,Loading,timeout,等通用的场景处理，一处Root注入，处处可用
     *
     *
     * 组件化以后 R2 资源问题。ButterKnife 不能使用了，考虑使用 其他自动生成FindViewById 工具
     */
    private fun customContentView(rootView: View): View {
        mToolbar = findViewById(R.id.toolbar)
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
        }

        val contentView = View.inflate(this, layoutId, null)
        if (contentView != null) {
            val contentLayout = rootView.findViewById<FrameLayout>(R.id.content_layout)

            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT)
            contentLayout.addView(contentView, params)

            //ButterKnife.bind(this, rootView);   //组件化后 ButterKnife 很不好使用，改用Android View Generator

            //增加Error，empty,Loading,timeout,等通用的场景处理,这个需要重新的组织一下
//            mBaseLoadService = LoadSir.getDefault().register(contentView, { v -> onHttpReload(v) } as Callback.OnReloadListener)

            mBaseLoadService = LoadSir.getDefault().register(contentView) { v -> this@BaseActivityCopy.onHttpReload(v) }

        }
        return rootView
    }

    /**
     * 点击按钮的监听
     *
     * @param view
     */
    override fun onClick(view: View) {

    }

    /**
     * 如果没有重写，说明那个页面不需要Http 请求，直接是成功
     */
    protected fun loadHttp() {
        mBaseLoadService?.showSuccess()
    }

    /**
     * Http 请求的重新加载
     */
    public fun onHttpReload(v: View) {}

    protected abstract fun initViews()

    /**
     * 设置头部标题
     *
     * @param title
     */
    fun setToolBarTitle(title: CharSequence) {
        toolbar!!.title = title
        setSupportActionBar(toolbar)
    }


    /**
     * 设置ToolBar 是否是可见的，默认是可见的;ToolBar  的Menu怎么设置
     *
     * @param visible
     */
    fun setToolBarVisible(visible: Int) {
        toolbar!!.visibility = visible
        setSupportActionBar(toolbar)
        mToolbar = null
    }

    override fun onStart() {
        super.onStart()
        setToolbar()
    }


    /**
     * 设置toolbar
     *
     */
    fun setToolbar() {
        if (null != toolbar) {
            if (isShowBackIcon) {
                toolbar!!.setNavigationIcon(R.drawable.ic_back_copy)
                toolbar!!.setNavigationOnClickListener { v -> onBackPressed() }
            } else {
                toolbar!!.navigationIcon = null
                toolbar!!.titleMarginStart = 66 //转为DP
            }
        }
    }


    /**
     * 防止内存泄漏需要：
     *
     * 1.移除消息队列中MessageQueue 中的所有的消息
     * 2.监听器注册的取消
     * 3.停止异步任务
     * 4.静态的变量置 null
     * 5.
     */
    override fun onDestroy() {
        super.onDestroy()
        //强制的取消显示
        HttpUiTips.dismissDialog(context)
    }

//    /**
//     *
//     */
//    override fun onBackPressed() {
//        super.onBackPressed()
//    }


}
