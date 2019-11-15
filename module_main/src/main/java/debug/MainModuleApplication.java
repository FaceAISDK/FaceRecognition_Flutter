package debug;

import com.anylife.module_main.dagger.DaggerMainModuleComponent;
import com.zlb.base.BaseApplication;
import com.zlb.dagger.module.BaseGlobalModule;


/**
 * Module 调试模式的时候使用，集成模式不会使用..
 *
 * 主工程模块的调试 Application，所有模块的Application 都要同步配置到App 壳工程Application
 *
 * Created by anylife.zlb@gmail.com on 2019/2/15.
 */
public class MainModuleApplication extends BaseApplication {
    public static final String TAG = MainModuleApplication.class.getSimpleName();
    public static final String MAIN_PROCESS_NAME = "com.zenglb.framework.module_main";
    public static final String WEB_PROCESS_NAME = "ccom.anylife.module_main:webprocessXXXXXXXX";

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }


    /**
     * 全局的注入，不用每个Activity 都写
     *
     * 能提供的注入能力在 BaseGlobalModule 中
     *
     */
    @Override
    protected void injectApp() {
        DaggerMainModuleComponent.builder()
                .baseGlobalModule(new BaseGlobalModule(this))
                .build()
                .inject(this);
    }


    /**
     * 需要重新规划
     *
     */
    private void initApplication() {

        //部分 初始化服务最好能新开一个IntentService 去处理
        String processName = getMyProcessName();
        switch (processName) {

            case MAIN_PROCESS_NAME:
                //UI status Builder

                break;

            case WEB_PROCESS_NAME:  //WebView 在单独的进程中

                break;

        }
    }


}
