package com.zenglb.framework.modulea.mvp.login;

import android.app.Activity;

import com.zenglb.framework.modulea.http.AModuleApiService;
import com.zenglb.framework.modulea.http.result.LoginResult;
import com.zlb.http.param.LoginParams;
import com.zlb.httplib.BaseObserver;
import com.zlb.httplib.HttpResponse;
import com.zlb.httplib.rxUtils.SwitchSchedulers;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Login Presenter
 *
 * 这是一种错误的示范，MVP 不应该这样写
 *
 * Presenter  这里最好不要进行Http请求，
 * 测试的时候保持Presenter 的单纯行，方便测试，单元测试
 *
 *
 * ！！！！！！！！！ 反例 ！！！！！！！！！！
 *
 *
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
public class LoginPresenter implements LoginContract.LoginPresenter {

    AModuleApiService apiService;
    LoginContract.LoginView mLoginView;  // 需要抽象出来

    /**
     * 构造方法被  @Inject  注解标注了
     *
     * @Inject
     * LoginPresenter loginPresenter;
     * 在  Activity 中很方便的依赖注入
     *
     * @param apiService
     */
    @Inject
    public LoginPresenter(AModuleApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * 这是一种错误的示范，MVP 不应该这样写
     *
     * Presenter  这里最好不要进行Http请求，
     * 测试的时候保持Presenter 的单纯行，方便测试，单元测试
     *
     *
     * ！！！！！！！！！ 反例 ！！！！！！！！！！
     *
     * @param loginParams
     */
    @Override
    public void login(LoginParams loginParams) {

        Observable<HttpResponse<LoginResult>>  responseObservable=apiService.goLoginByRxjavaObserver(loginParams);

        responseObservable
                .compose(SwitchSchedulers.applySchedulers())
                .subscribe(new BaseObserver<LoginResult>((Activity) mLoginView) {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if(null!=mLoginView){
                            mLoginView.loginSuccess(loginResult);
                        }
                    }

                    /**
                     * 因为LICENCE 等原因，现在不能登录接口废弃，这里假装就是就是登录成功
                     *
                     * @param code
                     * @param message
                     */
                    @Override
                    public void onFailure(int code, String message) {
//                        super.onFailure(code, message);
                        if(null!=mLoginView){
                            mLoginView.loginFail(message);
                        }
                    }
                });
    }


    /**
     * 这下面的两行能不能 Base化解
     *
     * @param view the view associated with this presenter
     */
    @Override
    public void takeView(LoginContract.LoginView view) {
        mLoginView = view;
    }


    /**
     * 防止异步请求回来后View 已经不在了
     *
     */
    @Override
    public void dropView() {
        mLoginView=null;  //f
    }


}
