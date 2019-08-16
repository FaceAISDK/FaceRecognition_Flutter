package com.zlb.base

import android.os.Bundle
import androidx.fragment.app.Fragment

import javax.inject.Inject
import dagger.android.AndroidInjection

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

/**
 * 感觉这里命名为 BaseDaggerActivity 会好一点吧
 *
 * 需要依赖注入extends this ，其实可以不要这么多Base, 在
 * Application 中完成所有的就好了，是不是 ！～
 *
 * Created by zlb on 2017/8/20.
 */
abstract class BaseDaggerActivity : BaseActivity(), HasSupportFragmentInjector {

    //使用dagger2注入时变量要使用关键字lateinit修饰，就是延迟初始化的意思
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {

        /**
         * 一处注入就好了，处处使用。
         */
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return supportFragmentInjector
    }

}
