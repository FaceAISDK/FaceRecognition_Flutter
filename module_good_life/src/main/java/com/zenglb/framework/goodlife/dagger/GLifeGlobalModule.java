package com.zenglb.framework.goodlife.dagger;

import android.content.Context;

import com.zenglb.framework.goodlife.http.GlifeApiService;
import com.zlb.Sp.SPDao;
import com.zlb.http.HttpRetrofit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A MODULE  使用的全局的东西，API Service 等
 *
 *
 * Created by anylife.zlb@gmail.com on 2018/1/11.
 */
@Module
public class GLifeGlobalModule {

    /**
     * AModule 的ApiService 的形成
     *
     * 对象的依赖性几乎没有，真正的 0 耦合啊
     *
     * @param spDao      这个参数在BaseGlobal Module 中已经被依赖注入了
     * @param mContext   同理，没有显式的传入。
     * @return           AModule 的ApiService
     */
    @Provides
    @Singleton
    public GlifeApiService provideApiService(SPDao spDao, Context mContext) {
        //Retrofit 的create 真是精华所在啊！
        return HttpRetrofit.getRetrofit(spDao, mContext).create(GlifeApiService.class);
    }




}