package com.anylife.module_main.dagger;

import com.zlb.base.BaseApplication;
import com.zlb.dagger.module.BaseGlobalModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * 全局的单例的东西提到这里来，比如SharedPrefenced,DaoSession 等等 ！
 * <p>
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
@Singleton
@Component(modules = {
        BaseGlobalModule.class,      //App 全局的Module,要确保提供的对象是全局唯一的

        AndroidInjectionModule.class,   //在应用程序的MainComponent（application 中inject了）中，注入AndroidInjectionModule，

        //这个是专门为了在Activity & Fragment 中使用依赖注入使用的
        MainModuleAllActivityModule.class,  //减少模版代码,需要依赖注入的只需要添加两行代码就好了

        //可以再加一个MainModule 的单独的全局Module的对象提供者
        MainGlobalModule.class

})

//YourApplicationComponent
public interface MainModuleComponent {

    void inject(BaseApplication application);

}
