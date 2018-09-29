package com.zenglb.framework.modulea.mvp.login;

import com.zenglb.framework.modulea.http.result.LoginResult;
import com.zlb.base.BasePresenter;
import com.zlb.base.BaseView;
import com.zlb.http.param.LoginParams;

/**
 *
 *  * Created by anylife.zlb@gmail.com on 2018/1/11.
 */

public interface LoginContract {

    /**
     * 对UI 的操作的接口有哪些，一看就只明白了
     *
     */
    interface LoginView extends BaseView<LoginPresenter> {
        void loginSuccess(LoginResult loginBean); // 登录成功，展示数据
        void loginFail(String failMsg);
    }


    interface LoginPresenter extends BasePresenter<LoginView> {
        void login(LoginParams loginParams);    // Model层面拿回数据后通过回调通知Presenter 再通知View
    }

}



