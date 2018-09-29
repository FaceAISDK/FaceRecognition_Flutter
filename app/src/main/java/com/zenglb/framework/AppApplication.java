package com.zenglb.framework;

import com.kingja.loadsir.core.LoadSir;
import com.squareup.leakcanary.LeakCanary;

import com.zlb.commontips.CustomCallback;
import com.zlb.commontips.EmptyCallback;
import com.zlb.commontips.ErrorCallback;
import com.zlb.commontips.LoadingCallback;
import com.zlb.commontips.TimeoutCallback;
import com.zlb.base.BaseApplication;
import com.zlb.dagger.module.BaseGlobalModule;


/**
 * 整个项目的全局的Application，其他的Debug 目录下的 XX Application 都会expect
 *
 * 参考{@link dagger.android.DaggerApplication}Beta 项目，项目组没有3个以上的Android 开发不建议使用Dagger XXX
 * <p>
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



    /**
     * 根据不同的进程来初始化不同的东西
     * 比如web进程就不需要初始化推送，也不需要图片加载等等
     * <p>
     * 发新版 或 测试版也有不同的初始化
     * 比如调试工具stetho 在debug 环境是要的，Release 是不需要的
     */
    private void initApplication() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        //部分 初始化服务最好能新开一个IntentService 去处理,bugly 在两个进程都有初始化
        String processName = getProcessName();
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
