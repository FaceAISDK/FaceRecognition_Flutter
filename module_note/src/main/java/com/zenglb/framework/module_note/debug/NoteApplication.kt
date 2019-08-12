package debug

import com.zenglb.framework.module_note.dagger.DaggerNoteModuleComponent
import com.zenglb.framework.module_note.objectbox.ObjectBox
import com.zlb.base.BaseApplication
import com.zlb.dagger.module.BaseGlobalModule

/**
 * Kotlin 编写，Dagger 不太会结合Kotlin 使用
 *
 * NoteApplication 只是单独调试的时候使用，合并到壳App 不需要，但是这里初始化的数据和服务要在AppApplication
 *
 */
class NoteApplication : BaseApplication() {


    /**
     *
     *
     */
    override fun onCreate() {
        super.onCreate()
//        ObjectBox.init(this)
    }


    /**
     * Dagger 依赖注入
     *
     */
    override fun injectApp() {
        DaggerNoteModuleComponent.builder()
                .baseGlobalModule(BaseGlobalModule(this))
                .build()
                .inject(this)
    }




}
