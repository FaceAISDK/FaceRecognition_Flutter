package com.zenglb.framework.module_note.dagger

import com.zenglb.framework.module_note.NoteMainActivity
import com.zenglb.framework.module_note.debug.NoteLauncherActivity
import com.zlb.dagger.component.BaseActivityComponent
import com.zlb.dagger.module.DefaultActivityModule
import com.zlb.dagger.scope.ActivityScope

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * 全部放在这里来统一的管理 ！
 * 新建了一个Activity 的并且需要inject 的只需要添加两行代码
 *
 * 大部分的页面都不需要再额外的提供对象的话只需要DefaultActivityModule 就好了，否则自定义XXActivityModule
 *
 *
 * 对个人而言，这样的好处在于：
 * 1.每次不再需要额外声明一个SubCompoent，再次减少模板代码
 * 2.每个Activity的Module都放在同一个AllActivitysModule中进行统一管理，每次修改只需要修改这一个类即可
 * 3.每个Activity所单独需要的依赖，依然由各自的Module进行管理和实例化，依然没有任何耦合
 *
 */
@Module(subcomponents = [BaseActivityComponent::class  //1111111111 subcomponent=BaseActivityComponent
])
abstract class NoteModuleAllActivityModule {

    //其实这里还是要配置很多的东西，自动化配置还是不能和Spring相比

    @ActivityScope
    @ContributesAndroidInjector(modules = [DefaultActivityModule::class])
    internal abstract fun noteLauncherActivityInjector(): NoteLauncherActivity


    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun noteMainActivityInjector(): NoteMainActivity

}



//java --> Kotlin

//@Module(subcomponents = {
//BaseActivityComponent.class  //1111111111 subcomponent=BaseActivityComponent
//})
//public abstract class NoteModuleAllActivityModule {
//
//@ActivityScope
//@ContributesAndroidInjector(modules = DefaultActivityModule.class)
//abstract NoteLauncherActivity noteLauncherActivityInjector();
//
//}


