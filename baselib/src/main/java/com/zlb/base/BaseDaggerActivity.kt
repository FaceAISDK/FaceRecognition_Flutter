package com.zlb.base

import android.os.Bundle
import androidx.annotation.Nullable
import javax.inject.Inject
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector

/**
 * 感觉这里命名为 BaseDaggerActivity 会好一点吧
 *
 * 需要依赖注入extends this ，其实可以不要这么多Base, 在
 * Application 中完成所有的就好了，是不是 ！～
 *
 * Created by zlb on 2017/8/20.
 */
abstract class BaseDaggerActivity : BaseActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun androidInjector(): AndroidInjector<Any>? {
        return androidInjector
    }
}