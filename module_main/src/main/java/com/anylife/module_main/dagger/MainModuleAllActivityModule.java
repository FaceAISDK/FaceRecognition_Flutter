package com.anylife.module_main.dagger;

import androidx.lifecycle.ViewModel;

import com.anylife.module_main.ModuleMainLauncherActivity;
import com.anylife.module_main.blog.viewmodel.BlogViewModel;
import com.anylife.module_main.business.login.LoginActivity;
import com.anylife.module_main.business.navigation.fragment.MainFragment;
import com.anylife.module_main.business.navigation.MainActivityBottomNavi;
import com.anylife.module_main.business.navigation.fragment.MeFragment;
import com.anylife.module_main.business.navigation.fragment.NewsFragmentShell;
import com.anylife.module_main.blog.ui.BlogListFragment;
import com.zlb.dagger.viewmodel.ViewModelKey;
import com.zlb.dagger.component.BaseActivityComponent;
import com.zlb.dagger.module.DefaultActivityModule;
import com.zlb.dagger.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * 全部放在这里来统一的管理 ！(感觉要这样配置还是很麻烦，能不能一行注解就可以了呢？？？？？？)
 * 新建了一个Activity 的并且需要inject 的只需要添加两行代码
 *
 * 大部分的页面都不需要再额外的提供对象的话只需要DefaultActivityModule 就好了，否则自定义XXActivityModule
 *
 *
 * 对个人而言，这样的好处在于：
 * 1.每次不再需要额外声明一个SubCompoent，再次减少模板代码
 * 2.每个Activity的Module都放在同一个AllActivitysModule中进行统一管理，每次修改只需要修改这一个类即可
 * 3.每个Activity所单独需要的依赖，依然由各自的Module进行管理和实例化，依然没有任何耦合
 */
@Module(subcomponents = {
        BaseActivityComponent.class   //1111111111 subComponent=BaseActivityComponent
})
public abstract class MainModuleAllActivityModule {
    //2222222 新建了一个Activity 的并且需要inject 的只需要添加两行代码 DefaultActivityModule 适用于只要全局Module 中的内容

    //要是能不声明就更好了，有点像Manifest 中的配置文件
    @ActivityScope
    @ContributesAndroidInjector(modules = DefaultActivityModule.class)
    abstract ModuleMainLauncherActivity moduleMainLauncherActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = DefaultActivityModule.class)
    abstract MainActivityBottomNavi mainActivityBottomNavi();


    @ActivityScope
    @ContributesAndroidInjector(modules = DefaultActivityModule.class)
    abstract LoginActivity loginActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = DefaultActivityModule.class)
    abstract MainFragment mainFragmentInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = DefaultActivityModule.class)
    abstract MeFragment meFragmentInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = DefaultActivityModule.class)
    abstract NewsFragmentShell meFragmentxxInjector();


    /**
     *
     *
     * @return
     */
    @ActivityScope
    @ContributesAndroidInjector(modules = DefaultActivityModule.class)
    abstract BlogListFragment mBlogInjector();


    @Binds
    @IntoMap
    @ViewModelKey(BlogViewModel.class)
    abstract ViewModel bindBlogViewModel(BlogViewModel blogViewModel);





//    More：https://google.github.io/dagger//android.html

//    If your subcomponent and its builder have no other methods or supertypes than the ones mentioned in step #2,
//    you can use @ContributesAndroidInjector to generate them for you. Instead of steps 2 and 3, add an abstract
//    module method that returns your activity, annotate it with @ContributesAndroidInjector, and specify the modules
//    you want to install into the subcomponent. If the subcomponent needs scopes,
//    apply the scope annotations to the method as well.
//
//    @ActivityScope
//    @ContributesAndroidInjector(modules = { /* modules to install into the subcomponent */ })
//    abstract YourActivity contributeYourActivityInjector();


}
