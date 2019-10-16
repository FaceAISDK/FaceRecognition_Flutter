package com.zlb.dagger.component;

import com.zlb.base.BaseDaggerActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 *
 * 在 XXXModuleAllActivityModule 等中被使用
 * 不要在每个Activity 中建立一个ActivitySubComponent，麻烦而且重复的无聊代码
 *
 * 每一个继承BaseActivity（BaseDaggerActivity）的Activity，都共享同一个SubComponent
 * 这样能减少非常多的重复的无聊的代码
 *
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
@Subcomponent(modules = {
        AndroidInjectionModule.class,
})

public interface BaseActivityComponent extends AndroidInjector<BaseDaggerActivity> {

    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<BaseDaggerActivity> {

    }

}
