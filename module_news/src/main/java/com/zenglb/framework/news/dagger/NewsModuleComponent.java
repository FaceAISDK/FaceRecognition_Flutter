package com.zenglb.framework.news.dagger;

import com.zlb.base.BaseApplication;
import com.zlb.dagger.module.BaseGlobalModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * 全局的单例的东西提到这里来，比如SP,DaoSession 等等 ！
 *
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
@Singleton
@Component(modules = {
        BaseGlobalModule.class,         //全局的Module,要确保提供的对象是全局唯一的
        AndroidInjectionModule.class,   //在应用程序的MainComponent（application 中inject了）中，注入AndroidInjectionModule，

        NewsModuleAllActivityModule.class,    //减少模版代码,需要依赖注入的只需要添加两行代码就好了
        NewsGlobalModule.class,
})


//YourModuleApplicationComponent
public interface NewsModuleComponent {

    void inject(BaseApplication application);

}
