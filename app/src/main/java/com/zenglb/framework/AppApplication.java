package com.zenglb.framework;

import com.kingja.loadsir.core.LoadSir;
import com.zlb.base.BaseApplication;
import com.zlb.commontips.CustomCallback;
import com.zlb.commontips.EmptyCallback;
import com.zlb.commontips.ErrorCallback;
import com.zlb.commontips.LoadingCallback;
import com.zlb.commontips.TimeoutCallback;
import com.zlb.dagger.module.BaseGlobalModule;

import dagger.android.AndroidInjector;

/**
 * 组件化开发模式 集成打包的壳工程的Application，其他组件Module模块工程在开发的时候配置Debug目录中的Application
 *
 * 在集成打包的时候其他组件Module模块工程下的Debug 目录下的 XXApplication 都配置会被expect
 *
 *
 * 每个Module 中的Application的初始化在集成模式中也应该在这里一起初始化
 *
 *
 * 参考{@link dagger.android.DaggerApplication}Beta 项目，项目组没有3个以上的Android 开发不建议引入Dagger
 * Created by anylife.zlb@gmail.com on 2017/3/15.
 */
public class AppApplication extends BaseApplication  {
    public static final String TAG = AppApplication.class.getSimpleName();
    public static final String MAIN_PROCESS_NAME = "com.zenglb.framework";
    public static final String WEB_PROCESS_NAME = "com.zenglb.framework:webprocess";

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }


    @Override
    protected void injectApp() {
        DaggerAppComponent.builder()
                .baseGlobalModule(new BaseGlobalModule(this))
                .build()
                .inject(this);
    }


//    /**
//     * 各个子模块的服务初始化
//     *
//     * @param application
//     */
//    @Override
//    public void initMoudleApp(Application application) {
//        for (String moduleApp : AppConfig.moduleApps) {
//            try {
//                Class clazz = Class.forName(moduleApp);
//                BaseApp baseApp = (BaseApp) clazz.newInstance();
//                baseApp.initMoudleApp(this);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            }
//        }
//    }



//    /**
//     * 各个子模块的数据初始化
//     *
//     * @param application
//     */
//    @Override
//    public void initMoudleData(Application application) {
//        for (String moduleApp : AppConfig.moduleApps) {
//            try {
//                Class clazz = Class.forName(moduleApp);
//                BaseApp baseApp = (BaseApp) clazz.newInstance();
//                baseApp.initMoudleData(this);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            }
//        }
//    }



    /**
     * 根据不同的进程来初始化不同的东西
     * 比如web进程就不需要初始化推送，也不需要图片加载等等
     * <p>
     * 发新版 或 测试版也有不同的初始化
     * 比如调试工具stetho 在debug 环境是要的，Release 是不需要的
     */
    private void initApplication() {
        //部分 初始化服务最好能新开一个IntentService 去处理,bugly 在两个进程都有初始化
        String processName = getMyProcessName();
        switch (processName) {

            case MAIN_PROCESS_NAME:
                //Module  带有构造方法并且参数被使用的情况下所产生的DaggerXXComponent 是没有Create方法的
//                DaggerMainComponent.create().inject(this);
//                DaggerMainComponent.builder().mainModule(new MainModule(this)).build().inject(this);
//                DaggerModuleAComponent.builder().moduleaModule(new ModuleaModule(this)).build().inject(this);

                //UI status Builder,需要
                LoadSir.beginBuilder()
                        .addCallback(new ErrorCallback())      //添加各种状态页
                        .addCallback(new EmptyCallback())
                        .addCallback(new LoadingCallback())
                        .addCallback(new TimeoutCallback())
                        .addCallback(new CustomCallback())
                        .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                        .commit();

                break;

            case WEB_PROCESS_NAME:  //WebView 在单独的进程中

                break;

        }
    }


}
