package com.zenglb.framework.modulea.mvp.handylife;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zenglb.framework.modulea.http.AModuleApiService;
import com.zenglb.framework.modulea.http.result.AnyLifeResult;
import com.zlb.httplib.BaseObserver;
import com.zlb.httplib.HttpResponse;
import com.zlb.httplib.rxUtils.SwitchSchedulers;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * 这里就不做什么先加载本地的ORM DB 然后再加载 HTTP 了
 * <p>
 * Created by zlb on 2018/3/23.
 */
public class AnyLifeRepository implements IAnyLifeDataSource {

    // Get the ApiService from dagger。 用dagger 来注入 ApiService
    @Inject
    AModuleApiService apiService;

    @Inject
    public AnyLifeRepository() {

    }

    public AnyLifeRepository(AModuleApiService apiService) {
        this.apiService = apiService;
    }


    /**
     * getHandyLifeData from http server
     *
     * @param type                      数据类型，{city guide,shop,eat}
     * @param page                      page index
     * @param loadHandyLifeDataCallback the callBack
     */
    @Override
    public void getHandyLifeData(String type, int page, LoadHandyLifeDataCallback loadHandyLifeDataCallback) {

        Observable<HttpResponse<List<AnyLifeResult>>> observableTest=apiService.getHandyLifeData(type, page);

        observableTest
                .compose(SwitchSchedulers.applySchedulers())

                //BaseObserver 参数问题优化，如果不传参数context 的话，依赖context 的功能就要改
                .subscribe(new BaseObserver<List<AnyLifeResult>>(null) {
                    @Override
                    public void onSuccess(List<AnyLifeResult> lifeResultBeans) {
                        if (null != loadHandyLifeDataCallback) {
                            loadHandyLifeDataCallback.onHandyLifeDataSuccess(lifeResultBeans);
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);

//                        if (null != loadHandyLifeDataCallback) {
//                            loadHandyLifeDataCallback.onHandyLifeDataFailed(code, message);
//                        }

                        //这个API 失效了，先假设能成功吧
                        try {
                            List<AnyLifeResult> mHandyLifeResultList = new Gson().fromJson(StaticJSON.jsonStr,
                                    new TypeToken<List<AnyLifeResult>>() {
                                    }.getType());
                            loadHandyLifeDataCallback.onHandyLifeDataSuccess(mHandyLifeResultList);
                        } catch (Exception e) {
                            Log.e("JSON Exception", e.toString());
                        }


                    }
                });
    }

}
