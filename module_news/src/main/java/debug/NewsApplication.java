package debug;

import com.zenglb.framework.news.FragmentService.NewsFragmentService;
import com.zenglb.framework.news.dagger.DaggerNewsModuleComponent;
import com.zlb.base.BaseApplication;
import com.zlb.dagger.module.BaseGlobalModule;

import component.android.com.component_base.ComponentServiceFactory;

/**
 * NewsApplication 只是单独调试的时候使用，合并到壳App 不需要，但是这里初始化的数据和服务要在AppApplication
 *
 */
public class NewsApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        login();

        //Fragment 组件化注册®️
        ComponentServiceFactory.getInstance(this)
                .setNewsFragmentService(new NewsFragmentService());

    }


    /**
     * 在这里模拟登陆，然后拿到sessionId或者Token
     * 这样就能够在组件请求接口了
     */
    private void login() {

    }


    /**
     * 配置好依赖注入
     *
     */
    @Override
    protected void injectApp() {
        DaggerNewsModuleComponent.builder()
                .baseGlobalModule(new BaseGlobalModule(this))  //这一行是把Base中的
                .build()
                .inject(this);
    }


}
