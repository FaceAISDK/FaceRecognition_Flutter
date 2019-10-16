package com.zlb.dagger.module;


import dagger.Module;
import dagger.Provides;

/**
 * 其实大部分的Activity 都只需要全局的对象依赖注入就好了
 * 如果只是需要全局的依赖注入那么就只要{@link XXModuleaAllActivityModule}加入
 *
 * @ActivityScope
 * @ContributesAndroidInjector(modules = DefaultActivityModule.class)
 * abstract LoginActivity contributeMainActivitytInjector();
 *
 *
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
@Module
public abstract class DefaultActivityModule {

    /**
     * 这是测试性质的提供一个String,实际上并不会使用的
     * @return
     */
    @Provides
    static String provideName() {
        return "NULL,It is DefaultActivityModule";
    }

}
