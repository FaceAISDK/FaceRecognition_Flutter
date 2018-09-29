package debug;

import com.zlb.base.BaseApplication;
import com.zlb.dagger.module.BaseGlobalModule;

/**
 *
 *
 */
public class JSBridgeApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        login();
    }


    /**
     * 在这里模拟登陆，然后拿到sessionId或者Token
     * 这样就能够在组件请求接口了
     */
    private void login() {

    }



    @Override
    protected void injectApp() {

        com.zenglb.framework.jsbridge.dagger.DaggerJSBridgeModuleComponent.builder()
                .baseGlobalModule(new BaseGlobalModule(this))
                .build()
                .inject(this);
    }

}
