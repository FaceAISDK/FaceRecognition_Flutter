package com.zlb.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import javax.inject.Inject;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

/**
 * 感觉这里命名为 BaseDaggerActivity 会好一点吧
 *
 * 需要依赖注入extends this ，其实可以不要这么多Base, 在
 * Application 中完成所有的就好了，是不是 ！～
 *
 * Created by zlb on 2017/8/20.
 */
public abstract class BaseDaggerActivity2 extends BaseActivity implements HasAndroidInjector {

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        /**
         * 一处注入就好了，处处使用。
         */
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
    }


    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
}
