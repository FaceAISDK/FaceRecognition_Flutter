package component.android.com.component_base;

import android.content.Context;

import component.android.com.component_base.base.IFragmentService;
import component.android.com.component_base.base.ILoginService;
import component.android.com.component_base.service.DefultLoginService;

/**
 * 1.首先部分Fragment 下沉到这里
 *
 *
 */
public class ComponentServiceFactory {

    private static volatile ComponentServiceFactory instance;
    private IFragmentService newsFragmentService;

//    private IFragmentService loginFragmentService;

    public static ComponentServiceFactory getInstance(Context context){
        if (instance == null){
            synchronized (ComponentServiceFactory.class){
                if (instance == null){
                    instance = new ComponentServiceFactory();
                }
            }
        }
        return instance;
    }



    public void setNewsFragmentService(IFragmentService iFragmentService){
        newsFragmentService = iFragmentService;
    }

    public IFragmentService getNewsFragmentService() {
        return newsFragmentService;
    }





//    private ILoginService loginService;
//
//    public void setLoginService(ILoginService iloginService){
//        loginService = iloginService;
//    }
//
//    public ILoginService getLoginService(){
//        if (loginService == null){
//            loginService = new DefultLoginService();
//        }
//        return loginService;
//    }
//
//    public void setLoginFragmentService(IFragmentService iFragmentService){
//        loginFragmentService = iFragmentService;
//    }
//
//    public IFragmentService getLoginFragmentService() {
//        return loginFragmentService;
//    }


}
